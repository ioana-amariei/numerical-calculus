package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class SparseMatrix {
    private int n;
    private Map<Integer, Map<Integer, Double>> matrix = new HashMap<>();

    public SparseMatrix(int n) {
        this.n = n;
    }

    public SparseMatrix(SparseMatrix that) {
        this.n = that.n;
        this.matrix = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Double>> line : that.matrix.entrySet()) {
            this.matrix.put(line.getKey(), new HashMap<>(line.getValue()));
        }
    }

    public SparseMatrix(String filename) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(filename));

        n = Integer.parseInt(scanner.nextLine());
        while (scanner.hasNextLine()) {
            String[] lineItems = scanner.nextLine().split(", ");

            double value = Double.valueOf(lineItems[0]);
            int line = Integer.valueOf(lineItems[1]);
            int column = Integer.valueOf(lineItems[2]);

            add(value, line, column);
        }
    }

    public void add(double value, int line, int column) {
        Map<Integer, Double> m = matrix.get(line);
        if (Objects.isNull(m)) {
            m = new HashMap<>();
            matrix.put(line, m);
        }

        if (m.get(column) == null) {
            m.put(column, value);
        } else {
            m.put(column, m.get(column) + value);
        }
    }

    public SparseMatrix add(SparseMatrix b) throws FileNotFoundException {
        SparseMatrix c = new SparseMatrix(this);

        for (Map.Entry<Integer, Map<Integer, Double>> line : b.matrix.entrySet()) {
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                double value = column.getValue();
                int i = line.getKey();
                int j = column.getKey();

                c.add(value, i, j);
            }
        }

        return c;
    }

    public SparseMatrix times(SparseMatrix b) {
        SparseMatrix c = new SparseMatrix(this.n);

        for (Map.Entry<Integer, Map<Integer, Double>> leftLine : matrix.entrySet()) {
            for (Map.Entry<Integer, Double> column : leftLine.getValue().entrySet()) {
                double e1 = column.getValue();
                for (Map.Entry<Integer, Double> rightLine : b.matrix.get(column.getKey()).entrySet()) {
                    double e2 = rightLine.getValue();

                    double value = e1 * e2;
                    c.add(value, leftLine.getKey(), rightLine.getKey());
                }
            }
        }

        return c;
    }

    public double[] times(double[] b) {
        double[] result = new double[b.length];
        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
            double e = 0;
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                double value = column.getValue();
                int i = line.getKey();
                int j = column.getKey();
                e += b[j] * value;
            }
            result[line.getKey()] = e;
        }

        return result;
    }

    public Map<Integer, Map<Integer, Double>> getMatrix() {
        return matrix;
    }

    public int getSize() {
        return n;
    }

    public String getCompactVisualization() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                sb.append(String.format("%s %s %s\n", column.getValue(), line.getKey(), column.getKey()));
            }
        }

        return sb.toString();
    }

    public String getFullVisualization() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; ++j) {
                double e = matrix.get(i) == null ? 0 : matrix.get(i).get(j) == null ? 0 : matrix.get(i).get(j);
                sb.append(e).append(" ");
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    public boolean hasAtMostNonNullElementsOnEveryLine(int k) {
        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
            if (line.getValue().entrySet().size() > k) {
                return false;
            }
        }

        return true;
    }

    public boolean isMainDiagonalNonZero() {
        for (int i = 0; i < n; i++) {
            Double value = get(i, i);
            if (value == null) {
                return false;
            } else if (Math.abs(value) <= AlgebraUtils.getPrecision()) {
                return false;
            }
        }

        return true;
    }

    public Double get(int i, int j) {
        return matrix.getOrDefault(i, Collections.emptyMap()).get(j);
    }

    public boolean hasDominantDiagonalRelativeToLines() {
        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
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

    public double[] computeSolutionUsingSOR(double[] b, double omega) {
        if (!this.isMainDiagonalNonZero()) {
            throw new IllegalArgumentException("Cannot compute SOR. There is at least one 0 value on main diagonal.");
        }
        System.out.println("Main diagonal has no 0 values.");

        double deltaX;
        double[] currentSolution = new double[n];
        double[] previousSolution = new double[n];

        int k = 0, kMax = 10_000;
        do {
            previousSolution = AlgebraUtils.getCopy(currentSolution);

            for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
                int i = line.getKey();

                double sum1 = 0, sum2 = 0;
                for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                    int j = column.getKey();
                    double value = column.getValue();

                    if (j < i) {
                        sum1 += value * currentSolution[j];
                    } else if (j > i) {
                        sum2 += value * previousSolution[j];
                    }
                }

                Double aii = this.get(i, i);
                currentSolution[i] = (1 - omega) * previousSolution[i] + (omega / aii) * (b[i] - sum1 - sum2);
            }

            k++;
            deltaX = AlgebraUtils.computeNorm(previousSolution, currentSolution);
        } while (deltaX >= AlgebraUtils.getPrecision() && k <= kMax && deltaX <= Math.pow(10, 8));

        System.out.println("Iterations = " + k);

        // check if convergence is reached
        if (deltaX < AlgebraUtils.getPrecision()) {
            return currentSolution;
        } else {
            System.out.println("Dominant diagonal: " + hasDominantDiagonalRelativeToLines());
            throw new IllegalArgumentException("The solution cannot be approximated; it diverged.");
        }
    }

    public double computeInfiniteNorm(double[] xSor, double[] b) {
        double[] multipliedMatrixSor = times(xSor);
        double max = multipliedMatrixSor[0] - b[0];

        for (int i = 0; i < n; i++) {
            double diff = multipliedMatrixSor[i] - b[i];
            if(diff > max){
                max = diff;
            }
        }

        return max;
    }

    public boolean equal(SparseMatrix that) {
        if (this.matrix.size() != that.matrix.size()) {
            return false;
        }
        for (Map.Entry<Integer, Map<Integer, Double>> line : this.matrix.entrySet()) {
            if (this.matrix.get(line.getKey()) == null) {
                return false;
            }
            if (line.getValue().size() != that.matrix.get(line.getKey()).size()) {
                return false;
            }
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                double a = column.getValue();
                double b = that.matrix.get(line.getKey()).getOrDefault(column.getKey(), 0.0);

                if (!AlgebraUtils.equal(a, b)) {
                    return false;
                }
            }
        }

        return true;
    }
}
