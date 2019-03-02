import model.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        Matrix A = new Matrix("matrix1.txt");
        Matrix Ainit = new Matrix("matrix1.txt");

        System.out.println("-----------INITIAL-----------");
        A.print();

        System.out.println("-----------DECOMPOSED-----------");
        A.LUDecomposition2();
        A.print();

        System.out.println(A.det());
        System.out.println("Determinant: " + Ainit.det() + "\n");

        double[] b = new double[]{0, 1, 2, 3};

        double[] x = A.solve(b);
        System.out.println(Arrays.toString(x));

        System.out.println(Ainit.norm2(x, b));

        System.out.println("---------------Jama Library--------------");
        Jama.Matrix matrix = new Jama.Matrix(readMatrix("matrix1.txt"));
        Jama.Matrix B = new Jama.Matrix(new double[][]{{1}, {2}, {3}});
        Jama.Matrix result = matrix.solve(B);
        result.transpose().print(10,10);

        double norm1 = norm2(Arrays.copyOfRange(x, 1, x.length), result.transpose().getArray()[0]);
        System.out.println(norm1);

        double norm2 = norm2(Arrays.copyOfRange(x, 1, x.length), matrix.inverse().times(B).transpose().getArray()[0]);
        System.out.println(norm2);

    }

    public static double norm2(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff ;
        }

        return Math.sqrt(sum);
    }

    public static double[][] readMatrix(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));
        int lines = scanner.nextInt();
        int columns = scanner.nextInt();

        double[][] matrix = new double[lines][columns];
        for (int i = 0; i < lines; ++i) {
            for (int j = 0; j < columns; ++j) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

        return matrix;
    }
}
