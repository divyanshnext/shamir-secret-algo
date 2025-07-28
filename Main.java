import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;
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
        JSONArray shares = json.getJSONArray("shares");

        int[][] points = new int[shares.length()][2];
        for (int i = 0; i < shares.length(); i++) {
            JSONObject share = shares.getJSONObject(i);
            points[i][0] = share.getInt("x");
            points[i][1] = share.getInt("y");
        }

        int secret = lagrangeInterpolation(points);
        System.out.println("Secret from " + fileName + ": " + secret);
    }

    public static int lagrangeInterpolation(int[][] points) {
        int secret = 0;
        for (int i = 0; i < points.length; i++) {
            int xi = points[i][0];
            int yi = points[i][1];
            double term = yi;
            for (int j = 0; j < points.length; j++) {
                if (i != j) {
                    int xj = points[j][0];
                    term *= (double)(0 - xj) / (xi - xj);
                }
            }
            secret += (int)Math.round(term);
        }
        return secret;
    }
}