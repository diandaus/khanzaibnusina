package bridging;

public class JwtPeruri {
    private static String cachedJwtToken = null;
    private static long tokenExpiredTime = 0;
    
    public static String getValidJwtToken() {
        long currentTime = System.currentTimeMillis();
        
        // Cek apakah token masih valid (kurang dari 23 jam - buffer 1 jam)
        if (cachedJwtToken != null && currentTime < tokenExpiredTime) {
            return cachedJwtToken;
        }
        
        // Generate token baru jika belum ada atau sudah expired
        try {
            ApiPeruri apiPeruri = new ApiPeruri();
            String newToken = apiPeruri.generateJwtToken();
            
            if (newToken != null) {
                cachedJwtToken = newToken;
                // Set expired time 23 jam dari sekarang (buffer 1 jam)
                tokenExpiredTime = currentTime + (23 * 60 * 60 * 1000);
            }
            
            return cachedJwtToken;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // Optional: Method untuk force refresh token
    public static void refreshToken() {
        cachedJwtToken = null;
        tokenExpiredTime = 0;
    }
}
/*

public class JwtPeruriUtil {
    public static String getValidJwtToken() {
        // Cek token di database
        String token = Sequel.cariIsi(
            "SELECT token FROM peruri_token WHERE expired > NOW() ORDER BY id DESC LIMIT 1"
        );
        
        if (!token.isEmpty()) {
            return token;
        }
        
        // Generate token baru
        ApiPeruri apiPeruri = new ApiPeruri();
        String newToken = apiPeruri.generateJwtToken();
        
        if (newToken != null) {
            // Simpan token baru ke database
            Sequel.menyimpan("peruri_token", "?,?", 
                new String[]{
                    newToken,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Date(System.currentTimeMillis() + (23 * 60 * 60 * 1000)))
                }
            );
        }
        
        return newToken;
    }
}

*/