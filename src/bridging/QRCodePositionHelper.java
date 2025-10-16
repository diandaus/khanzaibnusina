package bridging;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class untuk mendeteksi posisi tag/keyword di PDF
 * dan menghitung koordinat untuk QR Code TTE
 */
public class QRCodePositionHelper {

    private static final String DEFAULT_TAG = "#TTD#";
    private static final int QR_WIDTH = 35;
    private static final int QR_HEIGHT = 34;

    /**
     * Deteksi posisi tag di PDF dan return koordinat untuk QR code
     *
     * @param pdfPath Path ke file PDF
     * @param tag Tag yang dicari (default: "#TTD#")
     * @return Map berisi page, lowerLeftX, lowerLeftY, upperRightX, upperRightY
     */
    public static Map<String, String> detectQRPosition(String pdfPath, String tag) throws IOException {
        PDDocument document = null;
        try {
            document = PDDocument.load(new java.io.File(pdfPath));
            TagPositionFinder finder = new TagPositionFinder(tag != null ? tag : DEFAULT_TAG);
            finder.getText(document);

            if (finder.isTagFound()) {
                Map<String, String> position = new HashMap<>();
                // Center QR code pada posisi tag dengan mengurangi setengah ukuran QR
                // Tambahkan offset +10 ke kanan
                float centeredX = finder.getX() - (QR_WIDTH / 2.0f) + 5;
                float centeredY = finder.getY() - (QR_HEIGHT / 2.0f);

                position.put("page", String.valueOf(finder.getPage()));
                position.put("lowerLeftX", String.valueOf((int) centeredX));
                position.put("lowerLeftY", String.valueOf((int) centeredY));
                position.put("upperRightX", String.valueOf((int) (centeredX + QR_WIDTH)));
                position.put("upperRightY", String.valueOf((int) (centeredY + QR_HEIGHT)));
                return position;
            } else {
                // Jika tag tidak ditemukan, return posisi default (halaman terakhir, kanan bawah)
                return getDefaultPosition(document.getNumberOfPages());
            }
        } finally {
            if (document != null) {
                document.close();
            }
        }
    }

    /**
     * Overload tanpa tag parameter (gunakan default #TTD#)
     */
    public static Map<String, String> detectQRPosition(String pdfPath) throws IOException {
        return detectQRPosition(pdfPath, DEFAULT_TAG);
    }

    /**
     * Get posisi default (kanan bawah halaman terakhir)
     */
    public static Map<String, String> getDefaultPosition(int totalPages) {
        Map<String, String> position = new HashMap<>();
        position.put("page", String.valueOf(totalPages));
        position.put("lowerLeftX", "537");
        position.put("lowerLeftY", "8");
        position.put("upperRightX", "572");
        position.put("upperRightY", "42");
        return position;
    }

    /**
     * Inner class untuk mencari posisi tag dalam PDF
     */
    private static class TagPositionFinder extends PDFTextStripper {
        private String searchTag;
        private boolean tagFound = false;
        private int page = 1;
        private float x = 0;
        private float y = 0;

        public TagPositionFinder(String searchTag) throws IOException {
            super();
            this.searchTag = searchTag;
        }

        @Override
        protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
            if (!tagFound && string.contains(searchTag)) {
                tagFound = true;
                page = getCurrentPageNo();

                // Ambil posisi dari karakter pertama tag
                if (!textPositions.isEmpty()) {
                    TextPosition firstPosition = textPositions.get(0);
                    x = firstPosition.getXDirAdj();

                    // Konversi Y dari top-origin ke bottom-origin (PDF coordinate system)
                    // PDF coordinate: (0,0) di kiri bawah
                    float pageHeight = getCurrentPage().getMediaBox().getHeight();
                    y = pageHeight - firstPosition.getYDirAdj();
                }
            }
            super.writeString(string, textPositions);
        }

        public boolean isTagFound() {
            return tagFound;
        }

        public int getPage() {
            return page;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }
    }
}
