import model.Polynomial;
import model.Term;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PolynomialTest {
    private static final double C1 = 0.16666666666666666666666666666667;
    private static final double C2 = 0.00833333333333333333333333333333;

    @Test
    public void testCompute() {
        Term term1 = new Term(1.0, 1);
        Term term2 = new Term(-C1, 3);
        Term term3 = new Term(C2, 5);

        List<Term> p = Arrays.asList(term1, term2, term3);
        System.out.println(Polynomial.compute(p, 1));
    }

}