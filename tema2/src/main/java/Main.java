import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Matrix;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Main extends javafx.application.Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Homework 2");
        primaryStage.setScene(new Scene(root, 1000, 800));
        primaryStage.show();
    }

    public static void main(String[] args) throws FileNotFoundException {
        launch(args);

        Matrix A = new Matrix("matrix1.txt");
        Matrix Ainit = new Matrix("matrix1.txt");

        System.out.println("-----------INITIAL-----------");
        A.print();

        System.out.println("-----------DECOMPOSED-----------");
        A.LUDecomposition();
        A.print();

        System.out.println("-----------UPPER-----------");
        System.out.println(A.getUpper());

        System.out.println("-----------LOWER-----------");
        System.out.println(A.getLower());

        System.out.println("-----------DETERMINANT-----------");
        System.out.println("det(A) = " + A.det());
        System.out.println("det(Ainit) = " + Ainit.det() + "\n");

        System.out.println("-----------SYSTEM SOLUTION-----------");
        double[] b = new double[]{0, 1, 2, 3};
        double[] x = A.solve(b);
        System.out.println("Ax = b => x = " + Arrays.toString(x) + "\n");

        System.out.println("-----------NORM2-----------");
        System.out.println("Norm2 = " + Ainit.norm2(x, b) + "\n");

        System.out.println("---------------JAMA LIBRARY--------------");
        Jama.Matrix matrix = new Jama.Matrix(readMatrix("matrix1.txt"));
        double[][] a = {{1}, {2}, {3}};
        Jama.Matrix B = new Jama.Matrix(a);

        Jama.Matrix result = matrix.solve(B);
        System.out.println("System solution: x = ");
        result.transpose().print(10, 10);

        System.out.println("inverse(A) = ");
        matrix.inverse().print(5, 5);
        double norm1 = norm2(Arrays.copyOfRange(x, 1, x.length), result.transpose().getArray()[0]);
        System.out.println("Norm1 = " + norm1);

        double norm2 = norm2(Arrays.copyOfRange(x, 1, x.length), matrix.inverse().times(B).transpose().getArray()[0]);
        System.out.println("Norm2 = " + norm2);

    }

    public static double norm2(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
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
