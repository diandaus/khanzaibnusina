package bridging;

import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.IOException;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.ArrayList;
import java.util.List;

public class ApiPeruri {

    private static final String BASE_URL = "https://apg.peruri.co.id:9055/gateway/";
    private static final String SYSTEM_ID = "RSI-IBNUSINA-SIGLI";
    private static final String API_KEY = "fc77df73-ee4d-42f9-91cc-98922dd85c4b";

    private static String jwtToken = null; // Variabel untuk menyimpan JWT token
    private SSLContext sslContext;

    // Getter for SYSTEM_ID
    public static String getSystemId() {
        return SYSTEM_ID;
    }

    // SSL Configured RestTemplate
    public RestTemplate getRestTemplate() throws NoSuchAlgorithmException, KeyManagementException {
        sslContext = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers = {
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {}

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                }
        };
        sslContext.init(null, trustManagers, new SecureRandom());
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        return new RestTemplate(factory);
    }

 public String generateJwtToken() throws Exception {
    String url = BASE_URL + "jwt/1.0/getJsonWebToken/v1";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);

    // Membuat struktur JSON yang benar sesuai dengan format yang diharapkan
    Map<String, Object> param = new HashMap<>();
    param.put("systemId", SYSTEM_ID);
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // Debugging untuk memastikan header dan body sesuai
    System.out.println("Request URL: " + url);
    System.out.println("Request Headers: " + headers);
    System.out.println("Request Body: " + new ObjectMapper().writeValueAsString(body));

    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody());

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> responseMap = mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
            
            if (responseMap.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) responseMap.get("data");
                if (data != null && data.containsKey("jwt")) {
                    String jwt = (String) data.get("jwt");
                    System.out.println("JWT Token berhasil diperoleh: " + jwt);
                    return jwt;
                }
            }
            
            // Jika tidak menemukan JWT, cek pesan error
            String resultCode = (String) responseMap.get("resultCode"); 
            String resultDesc = (String) responseMap.get("resultDesc");
            throw new RuntimeException("Gagal mendapatkan JWT. Code: " + resultCode + ", Desc: " + resultDesc);

        } else {
            throw new RuntimeException("Failed to generate JWT: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error calling JWT API: " + e.getMessage());
    }
} 

private String parseJwtFromResponse(String responseBody) {
    // Parsing manual untuk mendapatkan nilai "jwt" dari respons JSON
    int startIndex = responseBody.indexOf("\"jwt\":\"") + 7;
    int endIndex = responseBody.indexOf("\"", startIndex);
    return responseBody.substring(startIndex, endIndex);
}
 

    public void setJwtToken(String token) {
    this.jwtToken = token;
}

    public String registration(Map<String, Object> userData) throws Exception {
    String url = BASE_URL + "digitalSignatureFullJwtSandbox/1.0/registration/v1";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Pastikan jwtToken sudah di-set

    Map<String, Object> body = new HashMap<>();
    body.put("param", userData);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Mengembalikan respons jika berhasil
        } else {
            throw new RuntimeException("Gagal melakukan pendaftaran: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil API pendaftaran: " + e.getMessage());
    }
}
    
    public String videoVerification(String email, String base64Video) throws Exception {
    String url = BASE_URL + "digitalSignatureFullJwtSandbox/1.0/videoVerification/v1";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Pastikan jwtToken sudah di-set

    Map<String, Object> param = new HashMap<>();
    param.put("email", email);
    param.put("systemId", SYSTEM_ID);
    param.put("videoStream", base64Video);

    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Mengembalikan respons jika berhasil
        } else {
            throw new RuntimeException("Gagal melakukan verifikasi video: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Video Verification API: " + e.getMessage());
    }
}
    
    public String sendSpeciment(String email, String base64Specimen) throws Exception {
    String url = BASE_URL + "digitalSignatureFullJwtSandbox/1.0/sendSpeciment/v1";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Pastikan jwtToken sudah di-set

    Map<String, Object> param = new HashMap<>();
    param.put("email", email);
    param.put("systemId", SYSTEM_ID);
    param.put("speciment", base64Specimen);

    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Mengembalikan respons jika berhasil
        } else {
            throw new RuntimeException("Gagal mengirim spesimen: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Send Specimen API: " + e.getMessage());
    }
}
    
public String checkCertificate(String email) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/checkCertificate/v1";

    // Membuat headers untuk permintaan
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Pastikan jwtToken sudah di-set

    // Membuat struktur JSON yang benar sesuai dengan format yang diharapkan
    Map<String, Object> param = new HashMap<>();
    param.put("email", email); // Mengirim email sebagai parameter
    param.put("systemId", SYSTEM_ID); // Pastikan SYSTEM_ID sudah didefinisikan di tempat lain

    Map<String, Object> body = new HashMap<>();
    body.put("param", param); // Memasukkan parameter ke dalam body

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    RestTemplate restTemplate = getRestTemplate(); // Mendapatkan instance RestTemplate

    try {
        // Melakukan panggilan POST dan mendapatkan respons
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        // Memeriksa status kode respons
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody(); // Mengembalikan respons jika berhasil
        } else {
            throw new RuntimeException("Gagal memeriksa sertifikat: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Check Certificate API: " + e.getMessage());
    } catch (Exception e) {
        // Menangani pengecualian lain yang mungkin terjadi
        System.err.println("Terjadi kesalahan: " + e.getMessage());
        throw new RuntimeException("Error memanggil Check Certificate API: " + e.getMessage());
    }
}

    
public String initiateSession(String email) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/sessionInitiate/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Gunakan JWT token yang sudah di-set

    Map<String, Object> param = new HashMap<>();
    param.put("email", email);
    param.put("systemId", SYSTEM_ID);
    param.put("sendEmail", "1");
    param.put("sendSms", "0");
    param.put("sendWhatsapp", "0");
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gagal melakukan inisiasi session: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Session Initiate API: " + e.getMessage());
    }
}

public String validateSession(String email, String tokenSession, String otpCode) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/sessionValidation/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken);

    Map<String, Object> param = new HashMap<>();
    param.put("email", email);
    param.put("systemId", SYSTEM_ID);
    param.put("tokenSession", tokenSession);
    param.put("otpCode", otpCode);
    param.put("duration", "1440");//1440
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gagal memvalidasi session: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Session Validation API: " + e.getMessage());
    }
}

public String signingSession(String orderId) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/signingSession/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken);

    Map<String, Object> param = new HashMap<>();
    param.put("orderId", orderId);
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gagal melakukan signing session: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Signing Session API: " + e.getMessage());
    }
}

   public String setSignature(String orderId) throws Exception {
    String url = BASE_URL + "digitalSignatureFullJwtSandbox/1.0/setSignature/v1";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken); // Gunakan JWT token yang sudah di-set

    // Membuat struktur JSON sesuai dengan format API
    Map<String, Object> signer = new HashMap<>();
    signer.put("isVisualSign", "YES");
    signer.put("lowerLeftX", "471");
    signer.put("lowerLeftY", "68");
    signer.put("upperRightX", "505");
    signer.put("upperRightY", "102");
    signer.put("page", "1");
    signer.put("certificateLevel", "NOT_CERTIFIED");
    signer.put("varLocation", "Sigli");
    signer.put("varReason", "Signed");

    Map<String, Object> requestSetSignature = new HashMap<>();
    requestSetSignature.put("orderId", orderId);
    requestSetSignature.put("signer", signer);

    Map<String, Object> body = new HashMap<>();
    body.put("requestSetSignature", requestSetSignature);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return "Signature berhasil diset untuk OrderID: " + orderId;
        } else {
            throw new RuntimeException("Gagal menyet signature: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Set Signature API: " + e.getMessage());
    }
}
 
   private static String sendPostRequest(String url, String payload) throws Exception {
        RestTemplate restTemplate = createRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Client-Id", SYSTEM_ID);
        headers.set("Client-Key", API_KEY);
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> entity = new HttpEntity<>(payload, headers);
        return restTemplate.postForObject(url, entity, String.class);
    }

    private static RestTemplate createRestTemplate() throws Exception {
        return new ApiPeruri().getRestTemplate();
    }
    
public Map<String, Object> sendDocument(String jwtToken, String email, String fileName, String base64Document) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/sendDocument/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken);

    // Setup request body
    Map<String, Object> signer = new HashMap<>();
    signer.put("isVisualSign", "YES");
    signer.put("lowerLeftX", "537");
    signer.put("lowerLeftY", "8");
    signer.put("upperRightX", "572");
    signer.put("upperRightY", "42");
    signer.put("page", "1");
    signer.put("certificateLevel", "NOT_CERTIFIED");
    signer.put("varLocation", "Sigli");
    signer.put("varReason", "Signed");
    signer.put("teraImage", "QR-DETECSI");

    List<Map<String, Object>> signers = new ArrayList<>();
    signers.add(signer);

    Map<String, Object> payload = new HashMap<>();
    payload.put("fileName", fileName);
    payload.put("base64Document", base64Document);
    payload.put("signer", signers);

    Map<String, Object> param = new HashMap<>();
    param.put("email", email);
    param.put("payload", payload);
    param.put("systemId", SYSTEM_ID);
    param.put("orderType", "INDIVIDUAL");

    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {});
        } else {
            throw new RuntimeException("Failed to send document: " + response.getStatusCode());
        }
    } catch (Exception e) {
        throw new RuntimeException("Error calling Send Document API: " + e.getMessage());
    }
}

public String checkDocumentStatus(String orderId) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/checkDocumentStatus/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken);

    Map<String, Object> param = new HashMap<>();
    param.put("orderId", orderId);
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gagal mengecek status dokumen: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Check Document Status API: " + e.getMessage());
    }
}

public String downloadDocument(String orderId) throws Exception {
    String url = BASE_URL + "digitalSignatureSession/1.0/downloadDocument/v1";
    
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("x-Gateway-APIKey", API_KEY);
    headers.set("Authorization", "Bearer " + jwtToken);

    Map<String, Object> param = new HashMap<>();
    param.put("orderId", orderId);
    
    Map<String, Object> body = new HashMap<>();
    body.put("param", param);

    HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    RestTemplate restTemplate = getRestTemplate();

    try {
        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.POST,
            requestEntity,
            String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Gagal mengunduh dokumen: " + response.getStatusCode());
        }
    } catch (HttpClientErrorException e) {
        System.err.println("HTTP Status Code: " + e.getStatusCode());
        System.err.println("Error Response Body: " + e.getResponseBodyAsString());
        throw new RuntimeException("Error memanggil Download Document API: " + e.getMessage());
        }
    }
}
