import utils.SparseMatrix;
import utils.SuccessiveOverRelaxation;

import java.io.FileNotFoundException;

public class Application {
    public static void main(String[] args) throws FileNotFoundException {
        SuccessiveOverRelaxation sor = new SuccessiveOverRelaxation("separated/m_rar_2019_1_matrix.txt", "separated/m_rar_2019_1_vector.txt");
        double[] xSor = sor.computeSolution(0.8);

        sor.displayVector(xSor);
    }
}
