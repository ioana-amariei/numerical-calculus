import utils.AlgebraUtils;
import utils.SparseMatrix;

import java.io.FileNotFoundException;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        computeSolution1();
        computeSolution2();
        computeSolution("separated/m_rar_2019_3_matrix.txt", "separated/m_rar_2019_3_vector.txt", 0.8);
        computeSolution("separated/m_rar_2019_4_matrix.txt", "separated/m_rar_2019_4_vector.txt", 0.8);
        computeSolution("separated/m_rar_2019_5_matrix.txt", "separated/m_rar_2019_5_vector.txt", 0.8);
    }

    private static void computeSolution1() throws FileNotFoundException {
        computeSolution("separated/m_rar_2019_1_matrix.txt", "separated/m_rar_2019_1_vector.txt", 0.8);
    }

    private static void computeSolution2() throws FileNotFoundException {
        double[] solution = computeSolution("separated/m_rar_2019_2_matrix.txt", "separated/m_rar_2019_2_vector.txt", 0.8);

        int n = solution.length;
        for (int i = 0; i < n; ++i) {
            if (!AlgebraUtils.equal(solution[i], 10.0 * i / 6)) {
                throw new IllegalArgumentException("Solution 2 is not correct");
            }
        }
    }

    private static double[] computeSolution(String matrixFile, String vectorFile, double omega) throws FileNotFoundException {
        double[] solution = null;

        try {
            SparseMatrix sparseMatrix = new SparseMatrix(matrixFile);
            double[] b = AlgebraUtils.readVector(vectorFile);

            solution = sparseMatrix.computeSolutionUsingSOR(b, omega);
            AlgebraUtils.displayVector(solution);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return solution;
    }
}
