import Jama.Matrix;
import Jama.SingularValueDecomposition;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class Application {

    public static void main(String[] args) throws FileNotFoundException {
        computeSolution0();
        computeSolution1();
        computeSolution2();
        computeSolution3();
        computeSolution4();
    }
    private static Matrix getPseudoInverse(SingularValueDecomposition svd) {
        Matrix s = svd.getS();
        Matrix v = svd.getV();
        Matrix u = svd.getU();

        return v.times(s).times(u.transpose());
    }

    private static void computeSolution0() throws FileNotFoundException {
        System.out.println("Matrice generata aleator");
        SparseMatrix sparseMatrix = SparseMatrix.newRandomlyGenerated(750);
        System.out.println("Symmetric: " + sparseMatrix.isSymmetric());
        sparseMatrix.svd();

        System.out.println("Using Jama:--------");
        Matrix matrix = new Matrix(sparseMatrix.getAsArray());
        SingularValueDecomposition svd = matrix.svd();
        System.out.println("Values: " + Arrays.toString(svd.getSingularValues()));
        System.out.println("Rank: " + svd.rank());
        System.out.println("Numarul de conditionare :" + svd.cond());
        System.out.println("PseudoInverse: ");
//        getPseudoInverse(svd).print(2,2);
    }

    private static void computeSolution1() throws FileNotFoundException {
        System.out.println("m_rar_sim_2019_500.txt");
        computeSolution("original/m_rar_sim_2019_500.txt");
    }

    private static void computeSolution2() throws FileNotFoundException {
        System.out.println("m_rar_sim_2019_1000.txt");
        computeSolution("original/m_rar_sim_2019_1000.txt");
    }

    private static void computeSolution3() throws FileNotFoundException {
        System.out.println("m_rar_sim_2019_1500.txt");
        computeSolution("original/m_rar_sim_2019_1500.txt");
    }

    private static void computeSolution4() throws FileNotFoundException {
        System.out.println("m_rar_sim_2019_2019.txt");
        computeSolution("original/m_rar_sim_2019_2019.txt");
    }

    private static double[] computeSolution(String matrixFile) throws FileNotFoundException {
        SparseMatrix sparseMatrix = new SparseMatrix(matrixFile);
        System.out.println("Symmetric: " + sparseMatrix.isSymmetric());
        sparseMatrix.svd();

        System.out.println("Using Jama:--------");
        Matrix matrix = new Matrix(sparseMatrix.getAsArray());
        SingularValueDecomposition svd = matrix.svd();
        System.out.println("Values: " + Arrays.toString(svd.getSingularValues()));
        System.out.println("Rank: " + svd.rank());
        System.out.println("Numarul de conditionare :" + svd.cond());
        System.out.println("PseudoInverse: ");
        Matrix a = getPseudoInverse(svd);
//        a.print(2,2);
        double[] vb = AlgebraUtils.generateVectorWithEuclideanNormOne(sparseMatrix.getSize());
        Matrix b = new Matrix(vb, vb.length);
        Matrix x = a.inverse().times(b);
        System.out.println("xI: ");
//        x.print(2,2);

        System.out.println("Norm: " + b.minus(a.inverse().times(x)).norm2());


        return null;
    }
}
