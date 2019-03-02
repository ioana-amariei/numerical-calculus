import model.Matrix;

public class Application {
    public static void main(String[] args) {
        Matrix A = new Matrix(new double[][]{
                {1.5, 3, 3},
                {2, 6.5, 14},
                {1, 3, 8}}
        );

        A.print();

//        Matrix copyA = A.deepCopy();
        A.decompose();
        A.print();

        A.getLower().print();
        A.getUpper().print();

        Matrix result = A.getLower().multiply(A.getUpper());
        result.print();
    }
}
