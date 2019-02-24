import java.util.List;

public class Polynomial {
    public static double compute(List<Term> terms, double x) {
        double result = 0.0;

        int currentDegree = 0;
        double power = 1.0;


        for (Term term : terms) {
            while(currentDegree < term.getDegree()) {
                power *= x;
                currentDegree++;
            }

            result += (term.getCoefficient() * power);
        }

        return result;
    }
}
