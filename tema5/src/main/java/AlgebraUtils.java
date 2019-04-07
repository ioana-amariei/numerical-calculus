import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class AlgebraUtils {
    private static double precision = Math.pow(10, -3);

    public static double[] readVector(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int n = scanner.nextInt();

        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = scanner.nextDouble();
        }

        return b;
    }

    public static double getPrecision() {
        return precision;
    }

    public static double[] generateXi(int n) {
        double[] x = new double[n];

        for (int i = 0; i < n; i++) {
            x[i] = n - i;
        }

        return x;
    }

    public static boolean equal(double[] a, double[] b) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            if (!equal(a[i], b[i])) {
                return false;
            }
        }

        return true;
    }

    public static boolean equal(double a, double b) {
        return Math.abs(a - b) < precision;
    }

    public static double computeNorm(double[] a, double[] b) {
        int n = b.length;

        double sum = 0.0;
        for (int i = 0; i < n; i++) {
            sum += Math.abs(a[i] - b[i]);
        }

        return sum;
    }

    public static double euclideanNorm(double[] a) {
        double sum = 0.0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i] * a[i];
        }

        return Math.sqrt(sum);
    }

    public static void displayVector(double[] vector) {
        System.out.println(Arrays.toString(vector));
    }

    public static double[] getCopy(double[] a) {
        return Arrays.copyOf(a, a.length);
    }

    public static double[] generateVectorWithEuclideanNormOne(int n) {
        double[] v = new double[n];
        for (int i = 0; i < n; i++) {
            v[i] = Math.sqrt(1.0 / n);
        }

        return v;
    }

    public static double times(double[] a, double[] b) {
        double value = 0;
        for (int i = 0; i < a.length; i++) {
            value += a[i] * b[i];
        }

        return value;
    }

    public static double[] times(double scalar, double[] b) {
        double[] a = new double[b.length];
        for (int i = 0; i < b.length; i++) {
            a[i] = scalar * b[i];
        }

        return a;
    }
}
