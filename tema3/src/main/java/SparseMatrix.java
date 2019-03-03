import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

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

    private double dotProduct(Map<Integer, Double> line, Map<Integer, Double> column) {
        double result = 0;

        for (Map.Entry<Integer, Double> elem : line.entrySet()) {
            result += elem.getValue() * column.getOrDefault(elem.getKey(), 0.0);
        }

        return result;
    }

    public SparseMatrix times(SparseMatrix b) {
        SparseMatrix c = new SparseMatrix(this.n);

        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                int i = line.getKey();
                int j = column.getKey();

                Map<Integer, Double> first = line.getValue();
                Map<Integer, Double> second = b.getColumn(j);

                double value = dotProduct(first, second);
                c.add(value, i, j);
            }
        }

        return c;
    }

    private Map<Integer, Double> getColumn(Integer n) {
        Map<Integer, Double> column = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Double>> line : matrix.entrySet()) {
            Double value = line.getValue().get(n);
            if (value != null) {
                column.put(line.getKey(), value);
            }
        }

        return column;
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
