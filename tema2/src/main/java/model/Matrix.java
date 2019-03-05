package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public class Matrix {
    private static double precision = Math.pow(10, -5);
    private int n;
    private double[][] matrix;

    public Matrix(int dim) {
        this.n = dim;
        this.matrix = new double[dim][dim];
    }

    public Matrix(double[][] matrix) {
        Objects.requireNonNull(matrix);

        n = matrix.length;
        this.matrix = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                this.matrix[i][j] = matrix[i][j];
            }
        }
    }

    public Matrix(String matrixFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(matrixFile));
        int lines = scanner.nextInt();
        int columns = scanner.nextInt();
        n = lines;

        matrix = new double[lines + 1][columns + 1];
        for (int i = 1; i <= lines; ++i) {
            for (int j = 1; j <= columns; ++j) {
                matrix[i][j] = scanner.nextDouble();
            }
        }
    }

    public Matrix(String inputMatrix, boolean b) {
        Scanner scanner = new Scanner(inputMatrix);
        int lines = scanner.nextInt();
        int columns = scanner.nextInt();
        n = lines;

        matrix = new double[lines + 1][columns + 1];
        for (int i = 1; i <= lines; ++i) {
            for (int j = 1; j <= columns; ++j) {
                matrix[i][j] = scanner.nextDouble();
            }
        }

    }

    public void LUDecomposition() {
        if (!isNonsingular()) {
            throw new IllegalStateException("There is no LU Decomposition for this matrix.");
        }

        for (int p = 1; p <= n; p++) {
            // calculul elementelor coloanei p din U
            for (int i = 1; i <= p - 1; i++) {
                double bip = 0;
                for (int k = 1; k <= i - 1; k++) {
                    bip += matrix[i][k] * matrix[k][p];
                }

                matrix[i][p] = (matrix[i][p] - bip) / matrix[i][i];
            }

            // calculul elementelor liniei p din L
            for (int i = 1; i <= p; i++) {
                double bip = 0;
                for (int k = 1; k <= i - 1; k++) {
                    bip += matrix[p][k] * matrix[k][i];
                }

                matrix[p][i] = matrix[p][i] - bip;
            }
        }
    }

    public double[] solve(double[] b) {
        double[] y = new double[b.length];

        for (int i = 1; i <= n; i++) {
            double sum = 0;
            for (int j = 1; j <= i - 1; j++) {
                sum += matrix[i][j] * y[j];
            }
            y[i] = (b[i] - sum) / matrix[i][i];
        }

        double[] x = new double[b.length];

        for (int i = n; i >= 1; i--) {
            double sum = 0;
            for (int j = i + 1; j <= n; j++) {
                sum += matrix[i][j] * x[j];
            }

            x[i] = y[i] - sum;
        }

        return x;
    }

    public double[][] getMatrix1() {
        double[][] m = new double[n][n];

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                m[i - 1][j - 1] = matrix[i][j];
            }
        }

        return m;
    }

    public double norm2(double[] x, double[] b) {
        Matrix z = this.multiply(x).minus(b);

        double sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += z.matrix[1][i] * z.matrix[1][i];
        }

        return Math.sqrt(sum);
    }

    private Matrix minus(double[] b) {
        for (int i = 1; i < b.length; i++) {
            this.matrix[1][i] -= b[i];
        }

        return this;
    }

    private Matrix multiply(double[] x) {
        double[][] m = new double[2][x.length + 1];

        for (int i = 1; i < x.length; i++) {
            double sum = 0;
            for (int j = 1; j < x.length; j++) {
                sum += this.matrix[i][j] * x[j];
            }
            m[1][i] = sum;
        }

        this.matrix = m;

        return this;
    }

    private boolean isZero(double value) {
        return Math.abs(value) <= precision;
    }

    public Matrix multiply(Matrix other) {
        Matrix result = new Matrix(other.n);

        for (int i = 0; i < this.n; i++) {
            for (int j = 0; j < other.matrix[0].length; j++) {
                for (int k = 0; k < this.matrix[0].length; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }

        return result;
    }

    private boolean isNonsingular() {
        for (int i = 1; i <= n; i++) {
            if (isZero(matrix[i][i])) {
                return false;
            }
        }

        return true;
    }

    public Matrix getUpper() {
        Matrix result = new Matrix(n + 1);
        double[][] U = result.getMatrix();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i <= j) {
                    U[i][j] = matrix[i][j];
                } else {
                    U[i][j] = 0.0;
                }
            }
        }

        return result;
    }

    public Matrix getLower() {
        Matrix result = new Matrix(n + 1);
        double[][] L = result.getMatrix();
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i > j) {
                    L[i][j] = matrix[i][j];
                } else if (i == j) {
                    L[i][j] = 1.0;
                } else {
                    L[i][j] = 0.0;
                }
            }
        }
        return result;
    }

    public double det() {
        return detL() * detU();
    }

    private double detL() {
        double det = 1;
        for (int i = 1; i <= n; i++) {
            det *= matrix[i][i];
        }

        return det;
    }

    private double detU() {
        return 1;
    }

    private double det(double[][] matrix) {
        double temporary[][];
        double result = 0;

        if (matrix.length == 1) {
            result = matrix[0][0];
            return (result);
        }

        if (matrix.length == 2) {
            result = ((matrix[0][0] * matrix[1][1]) - (matrix[0][1] * matrix[1][0]));
            return (result);
        }

        for (int i = 0; i < matrix[0].length; i++) {
            temporary = new double[matrix.length - 1][matrix[0].length - 1];

            for (int j = 1; j < matrix.length; j++) {
                for (int k = 0; k < matrix[0].length; k++) {
                    if (k < i) {
                        temporary[j - 1][k] = matrix[j][k];
                    } else if (k > i) {
                        temporary[j - 1][k - 1] = matrix[j][k];
                    }
                }
            }

            result += matrix[0][i] * Math.pow(-1, (double) i) * det(temporary);
        }
        return result;
    }

    public int getN() {
        return n;
    }

    public double[][] getMatrix() {
        return matrix;
    }

    public void print() {
        for (int i = 1; i < matrix.length; ++i) {
            for (int j = 1; j < matrix[0].length; ++j) {
                System.out.print(matrix[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < matrix.length; ++i) {
            for (int j = 1; j < matrix[0].length; ++j) {
                sb.append(matrix[i][j]);
                sb.append(" ");
            }

            sb.append("\n");
        }

        return sb.toString();

    }
}
