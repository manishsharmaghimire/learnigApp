//package com.elearn.services.serviceImpl;
//
//import org.springframework.stereotype.Component;
//
//// EsewaClient.java
//@Component
//public class EsewaClient {
//    @Value("${esewa.merchant.code}")
//    private String merchantCode;
//
//    public boolean verifyTransaction(String refId, String pid, String amt) {
//        String verificationUrl = "https://uat.esewa.com.np/epay/transrec";
//
//        try {
//            String requestBody = "amt=" + amt +
//                    "&scd=" + merchantCode +
//                    "&pid=" + pid +
//                    "&rid=" + refId;
//
//            HttpURLConnection conn = (HttpURLConnection) new URL(verificationUrl).openConnection();
//            conn.setDoOutput(true);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//
//            try (OutputStream os = conn.getOutputStream()) {
//                os.write(requestBody.getBytes(StandardCharsets.UTF_8));
//            }
//
//            StringBuilder response = new StringBuilder();
//            try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    response.append(line);
//                }
//            }
//
//            return response.toString().contains("<response_code>Success</response_code>");
//        } catch (IOException e) {
//            return false;
//        }
//    }
//}
