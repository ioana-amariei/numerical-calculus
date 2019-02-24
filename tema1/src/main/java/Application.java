import model.Approximation;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        System.out.println("System precision: " + Utils.getMachineAccuracy() + "\n");
        System.out.println("The addition operation is non-associative: " + Utils.checkAdditionOperationAssociativity() + "\n");
        System.out.println("The multiplication operation is non-associative: " + Utils.checkMultiplicationOperationAssociativity() + "\n");

        Map<String, Approximation> approximations = Utils.computeApproximations();
        Utils.printApproximationsInOrderOfAccuracy(approximations);
        System.out.println();
        Utils.printApproximationsInOrderOfTime(approximations);

    }
}

