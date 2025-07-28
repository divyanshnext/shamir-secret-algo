import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) throws IOException {
        reconstructSecret("input1.json");
        reconstructSecret("input2.json");
    }

    public static void reconstructSecret(String fileName) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(fileName)));
        JSONObject json = new JSONObject(content);
        
        long[][] points;
        
        // Check if this is the simple format (input1.json) or complex format (input2.json)
        if (json.has("shares")) {
            // Simple format
            JSONArray shares = json.getJSONArray("shares");
            points = new long[shares.length()][2];
            for (int i = 0; i < shares.length(); i++) {
                JSONObject share = shares.getJSONObject(i);
                points[i][0] = share.getInt("x");
                points[i][1] = share.getInt("y");
            }
        } else {
            // Complex format with base conversion
            JSONObject keys = json.getJSONObject("keys");
            int k = keys.getInt("k"); // minimum number of shares needed
            
            List<long[]> sharesList = new ArrayList<>();
            
            // Process shares 1 through n
            for (int i = 1; i <= keys.getInt("n"); i++) {
                String shareKey = String.valueOf(i);
                if (json.has(shareKey)) {
                    JSONObject share = json.getJSONObject(shareKey);
                    int base = Integer.parseInt(share.getString("base"));
                    String value = share.getString("value");
                    
                    long decimalValue = convertToDecimal(value, base);
                    sharesList.add(new long[]{i, decimalValue});
                }
            }
            
            // Use only k shares (minimum required)
            points = new long[Math.min(k, sharesList.size())][2];
            for (int i = 0; i < points.length; i++) {
                points[i] = sharesList.get(i);
            }
        }

        long secret = lagrangeInterpolation(points);
        System.out.println("Secret from " + fileName + ": " + secret);
    }

    public static long convertToDecimal(String value, int base) {
        long result = 0;
        long multiplier = 1;
        
        // Process from right to left
        for (int i = value.length() - 1; i >= 0; i--) {
            char digit = value.charAt(i);
            int digitValue;
            
            if (digit >= '0' && digit <= '9') {
                digitValue = digit - '0';
            } else if (digit >= 'a' && digit <= 'z') {
                digitValue = digit - 'a' + 10;
            } else if (digit >= 'A' && digit <= 'Z') {
                digitValue = digit - 'A' + 10;
            } else {
                throw new IllegalArgumentException("Invalid digit: " + digit);
            }
            
            if (digitValue >= base) {
                throw new IllegalArgumentException("Digit " + digit + " is invalid for base " + base);
            }
            
            result += digitValue * multiplier;
            multiplier *= base;
        }
        
        return result;
    }

    public static long lagrangeInterpolation(long[][] points) {
        long secret = 0;
        for (int i = 0; i < points.length; i++) {
            long xi = points[i][0];
            long yi = points[i][1];
            double term = yi;
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    long xj = points[j][0];
                    term *= (double)(0 - xj) / (xi - xj);
                }
            }
            secret += Math.round(term);
        }
        return secret;
    }
}