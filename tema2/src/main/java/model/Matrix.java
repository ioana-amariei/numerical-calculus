/**
 * https://math.nist.gov/javanumerics/jama/doc/
 * https://stackoverflow.com/questions/19648240/java-best-way-to-print-2d-array
 */

package model;

import java.util.Arrays;

public class Matrix {
    private int dim;
    private double[][] array;

    public Matrix(int dim) {
        this.dim = dim;
        this.array = new double[dim][dim];
    }

    public Matrix(double[][] array) {
        dim = array.length;
        this.array = new double[dim][dim];

        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                this.array[i][j] = array[i][j];
            }
        }
    }

    public Matrix deepCopy() {
        if (array == null) {
            return null;
        }

        final double[][] copy = new double[dim][dim];

        for (int i = 0; i < array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }

        return new Matrix(copy);
    }

    public void decompose() {
        double[][] upper = new double[dim][dim];
        double[][] lower = new double[dim][dim];

        if (!this.isNonsingular()) {
            System.out.println("The LU Decomposition cannot be solved");
            return;
        }

        for (int i = 0; i < dim; i++) {
            // upper triangular
            for (int p = i; p < dim; p++) {
                // L(i, k) * U(k, p)
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += lower[i][k] * upper[k][p];
                }
                upper[i][p] = array[i][p] - sum;
                array[i][p] = upper[i][p];
            }

            // lower triangular
            for (int p = i; p < dim; p++) {
                // L(p, k) * U(k, i)
                double sum = 0;
                for (int k = 0; k < i; k++) {
                    sum += lower[p][k] * upper[k][i];
                }
                lower[p][i] = (array[p][i] - sum) / upper[i][i];
                array[p][i] = lower[p][i];
            }
        }

    }

    public Matrix multiply(Matrix other) {
        Matrix result = new Matrix(other.dim);

        for (int i = 0; i < this.dim; i++) {
            for (int j = 0; j < other.array[0].length; j++) {
                for (int k = 0; k < this.array[0].length; k++) {
                    result.array[i][j] += this.array[i][k] * other.array[k][j];
                }
            }
        }

        return result;
    }

    private boolean isNonsingular() {
        for (int i = 0; i < dim; i++) {
            if (array[i][i] == 0) {
                return false;
            }
        }

        return true;
    }

    public Matrix getUpper() {
        Matrix result = new Matrix(dim);
        double[][] U = result.getArray();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i <= j) {
                    U[i][j] = array[i][j];
                } else {
                    U[i][j] = 0.0;
                }
            }
        }

        return result;
    }

    public Matrix getLower() {
        Matrix result = new Matrix(dim);
        double[][] L = result.getArray();
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (i > j) {
                    L[i][j] = array[i][j];
                } else if (i == j) {
                    L[i][j] = 1.0;
                } else {
                    L[i][j] = 0.0;
                }
            }
        }
        return result;
    }

    public int getDim() {
        return dim;
    }

    public double[][] getArray() {
        return array;
    }

    public void print() {
        for(double[] row : array){
            System.out.println(Arrays.toString(row));
        }
        System.out.println("\n");
    }

    @Override
    public String toString() {
        return "Matrix{" +
                "dim=" + dim +
                ", array=" + Arrays.deepToString(array) +
                '}';
    }
}
