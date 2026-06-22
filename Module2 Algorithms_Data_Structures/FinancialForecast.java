import java.util.HashMap;
import java.util.Map;

public class FinancialForecast {

    // Naive recursion: future value = present value * (1 + growthRate)^years
    static double futureValueRecursive(double presentValue, double growthRate, int years) {
        if (years == 0) {
            return presentValue;
        }
        return futureValueRecursive(presentValue, growthRate, years - 1) * (1 + growthRate);
    }

    // Optimized with memoization: caches results so repeated forecasts
    // (e.g. recalculating year 5 after already computing year 10) become
    // O(1) lookups instead of recomputation.
    static Map<Integer, Double> memo = new HashMap<>();

    static double futureValueMemoized(double presentValue, double growthRate, int years) {
        if (years == 0) {
            return presentValue;
        }
        if (memo.containsKey(years)) {
            return memo.get(years);
        }
        double result = futureValueMemoized(presentValue, growthRate, years - 1) * (1 + growthRate);
        memo.put(years, result);
        return result;
    }

    public static void main(String[] args) {
        double presentValue = 10000.0; // starting investment
        double growthRate = 0.07;      // 7% annual growth

        System.out.println("=== Naive Recursive Forecast ===");
        for (int year = 1; year <= 5; year++) {
            double fv = futureValueRecursive(presentValue, growthRate, year);
            System.out.printf("Year %d -> $%.2f%n", year, fv);
        }

        System.out.println("\n=== Memoized Forecast (avoids recomputation) ===");
        memo.clear();
        for (int year = 1; year <= 5; year++) {
            double fv = futureValueMemoized(presentValue, growthRate, year);
            System.out.printf("Year %d -> $%.2f%n", year, fv);
        }

        System.out.println("\nRe-querying year 3 (should hit cache): $" +
                String.format("%.2f", futureValueMemoized(presentValue, growthRate, 3)));

        System.out.println("\n30-year forecast (memoized): $" +
                String.format("%.2f", futureValueMemoized(presentValue, growthRate, 30)));
    }
}