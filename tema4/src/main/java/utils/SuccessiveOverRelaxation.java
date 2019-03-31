package utils;

import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Map;

public class SuccessiveOverRelaxation {
    private SparseMatrix sparseMatrix;
    private double[] b;
    private double[] xSor;

    public SuccessiveOverRelaxation(String matrixFilename, String vectorFilename) throws FileNotFoundException {
        this.sparseMatrix = new SparseMatrix(matrixFilename);

        if (!isMainDiagonalNonZero()) {
            System.out.println("Cannot compute SOR. There is at least one 0 value on main diagonal.");
            return;
        }

        this.b = AlgebraUtils.readVector(vectorFilename);
        //        automatically initializes vector with 0
        this.xSor = new double[sparseMatrix.getSize()];
    }

    public double[] getXSor() {
        return xSor;
    }

    public boolean hasDominantDiagonalRelativeToLines() {
        for (Map.Entry<Integer, Map<Integer, Double>> line : sparseMatrix.getMatrix().entrySet()) {
            double aii = 0.0;
            double lineSum = 0.0;
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                int i = line.getKey();
                int j = column.getKey();
                if (i == j) {
                    aii = column.getValue();
                } else {
                    lineSum += Math.abs(column.getValue());
                }
            }
            if (Math.abs(aii) <= lineSum) {
                return false;
            }
        }

        return true;
    }

    private boolean isMainDiagonalNonZero() {
        for (int i = 0; i < sparseMatrix.getSize(); i++) {
            Double value = sparseMatrix.getMatrix().getOrDefault(i, Collections.emptyMap()).get(i);
            if(value != null) {
                if (Math.abs(value) <= AlgebraUtils.getPrecision()) {
                    return false;
                }
            }
        }
        return true;
    }

    private double[] getCopy(double[] array) {
        int n = array.length;
        double[] copy = new double[n];
        System.arraycopy(array, 0, copy, 0, n);

        return copy;
    }

    private Double getValue(int i, int j) {
        return sparseMatrix.getMatrix().getOrDefault(i, Collections.emptyMap()).get(j);
    }

    private double getDeltaX(double[] xk1, double[] xk) {
        double sum = 0.0;
        for (int i = 0; i < xk.length; i++) {
            sum += xk1[i] - xk[i];
        }

        return sum;
    }

    public double[] computeSolution(double omega) {
        int kMax = 10000;
        double deltaX = 1.0;
        int n = xSor.length;
        double[] newXSor = new double[n];

        int k = 0;
        do {
            System.out.println("--------------------------");
            xSor = getCopy(newXSor);
            for (int i = 0; i < n; i++) {
                double sum1 = 0.0;
                for (int j = 0; j < i - 1; j++) {
                    Double value = getValue(i, j);
                    if (value != null) {
                        sum1 += value * newXSor[j];
                    }
                }

                double sum2 = 0.0;
                for (int j = i; j < n; j++) {
                    Double value = getValue(i, j);
                    if (value != null) {
                        sum2 += value * xSor[j];
                    }
                }

                Double aii = getValue(i, i);
                if (aii != null) {
                    newXSor[i] = (1 - omega) * xSor[i] + (omega / aii) * (b[i] - sum1 - sum2);
                }
                System.out.println("New xSor[" + i + "]=" + newXSor[i]);
            }
            deltaX = getDeltaX(newXSor, xSor);
            k++;
            System.out.println("k: " + k);
        } while (deltaX >= AlgebraUtils.getPrecision() && k <= kMax && deltaX <= Math.pow(10, 8));

        // check convergence is reached
        if (deltaX < AlgebraUtils.getPrecision()) {
            System.out.println("Iterations: " + k);
            return newXSor;
        } else {
            // divergenta???
            return null;
        }
    }

    public void displayVector(double[] vector) {
        for (double elem : vector) {
            System.out.println(elem);
        }
    }
}
