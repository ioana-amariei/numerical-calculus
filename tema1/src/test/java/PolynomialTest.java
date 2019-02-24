import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PolynomialTest {

    @Test
    public void testCompute() {
        Term term1 = new Term(1.0, 1);
        Term term2 = new Term(-Utils.C1, 3);
        Term term3 = new Term(Utils.C2, 5);

        List<Term> p = Arrays.asList(term1, term2, term3);
        System.out.println(Polynomial.compute(p, 1));
    }

}