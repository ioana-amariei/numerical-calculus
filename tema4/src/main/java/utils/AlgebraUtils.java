package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class AlgebraUtils {
    private static double precision = Math.pow(10, -5);

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

    public static void displayVector(double[] vector) {
        for (double elem : vector) {
            System.out.println(elem);
        }
    }

    public static double[] getCopy(double[] a) {
        return Arrays.copyOf(a, a.length);
    }
}
