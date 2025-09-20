// Save as FindPolynomialConstant.java
import java.io.*;
import java.math.BigInteger;
import java.nio.file.*;
import java.util.*;
import org.json.*;

public class FindPolynomialConstant {

    public static void main(String[] args) throws Exception {
        // Read input.json
        String content = Files.readString(Path.of("input.json"));
        JSONObject json = new JSONObject(content);

        JSONObject keys = json.getJSONObject("keys");
        int k = keys.getInt("k");

        // Parse the first k roots
        List<Integer> sortedKeys = new ArrayList<>();
        for (String key : json.keySet()) {
            if (!key.equals("keys")) {
                sortedKeys.add(Integer.parseInt(key));
            }
        }
        Collections.sort(sortedKeys);

        List<BigInteger> xs = new ArrayList<>();
        List<BigInteger> ys = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int x = sortedKeys.get(i);
            JSONObject root = json.getJSONObject(String.valueOf(x));
            int base = Integer.parseInt(root.getString("base"));
            String value = root.getString("value");
            BigInteger y = new BigInteger(value, base);

            xs.add(BigInteger.valueOf(x));
            ys.add(y);
        }

        // Perform Lagrange interpolation at x = 0 to get constant C
        BigInteger C = lagrangeInterpolationAtZero(xs, ys);
        System.out.println(C);
    }

    private static BigInteger lagrangeInterpolationAtZero(List<BigInteger> xs, List<BigInteger> ys) {
        BigInteger result = BigInteger.ZERO;
        int k = xs.size();

        for (int i = 0; i < k; i++) {
            BigInteger xi = xs.get(i);
            BigInteger yi = ys.get(i);
            BigInteger num = BigInteger.ONE;
            BigInteger den = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i == j) continue;
                BigInteger xj = xs.get(j);
                num = num.multiply(xj.negate());
                den = den.multiply(xi.subtract(xj));
            }

            BigInteger term = yi.multiply(num).divide(den);
            result = result.add(term);
        }

        return result;
    }
}
