import java.util.Map;

public class Application {
    public static void main(String[] args) {
        Utils accuracy = new Utils();
        System.out.println("System precision: " + accuracy.getMachineAccuracy());
        System.out.println("The addition operation is non-associative: " + accuracy.checkAdditionOperationAssociativity());
        System.out.println("The multiplication operation is non-associative: " + accuracy.checkMultiplicationOperationAssociativity());
        Map<String, Approximation> approx = accuracy.computeApproximations();

        accuracy.printApproximationsInOrderOfAccuracy(approx);
        accuracy.printApproximationsInOrderOfTime(approx);

    }
}

