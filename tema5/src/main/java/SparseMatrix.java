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

    public static SparseMatrix newRandomlyGenerated(int n) {
        Random random = new Random(System.currentTimeMillis());
        int numberOfElements = random.nextInt(n * n / 4);


        SparseMatrix sparseMatrix = new SparseMatrix(n);
        while (numberOfElements > 0) {
            int line = random.nextInt(n);
            int column = random.nextInt(n);

            double value = random.nextDouble();

            sparseMatrix.add(value, line, column);
            sparseMatrix.add(value, column, line);
            --numberOfElements;
        }

        return  sparseMatrix;
    }

    public boolean isSymmetric() {
        for (Map.Entry<Integer, Map<Integer, Double>> line : this.matrix.entrySet()) {
            int i = line.getKey();
            for (Map.Entry<Integer, Double> column : line.getValue().entrySet()) {
                int j = column.getKey();

                if (get(j, i) == null)  {
                    return false;
                } else if (!AlgebraUtils.equal(get(i,j), get(j, i))) {
                    return false;
                }
            }
        }

        return true;
    }

    // returns null if not found
    public Double get(int i, int j) {
        return matrix.getOrDefault(i, Collections.emptyMap()).get(j);
    }

    public void svd() {
        double[] v = AlgebraUtils.generateVectorWithEuclideanNormOne(n);
        double[] w = this.times(v);

        double lambda = AlgebraUtils.times(w, v);
        int k = 0, kMax = 1_000_000;
        double precisionLimit = n * AlgebraUtils.getPrecision();

        double[] previousV = v;
        double previousLambda = lambda;

        do {
            previousV = v;
            previousLambda = lambda;

            v = AlgebraUtils.times(1 / AlgebraUtils.euclideanNorm(w), w);
            w = this.times(v);
            lambda = AlgebraUtils.times(w, v);
            ++k;
        } while (AlgebraUtils.computeNorm(w, AlgebraUtils.times(previousLambda, previousV)) > precisionLimit && (k < kMax));


        if (k > kMax) {
            System.out.println("The algorithm did not succeed.");
        }else {
            System.out.println("The Eigenvalue approximation: " + lambda);
            System.out.println("Approximate Eigenvector: " + Arrays.toString(v));
        }
    }

    public double[][] getAsArray() {
        double[][] matrix = new double[n][n];
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j++) {
                Double val = get(i,j);
                matrix[i][j] = val == null ? 0 : val;
            }
        }

        return matrix;
    }
}
