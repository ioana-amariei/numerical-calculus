import com.google.common.base.Stopwatch;

import java.util.*;
import java.util.concurrent.TimeUnit;

class Utils {
    public static final double C1 = 0.16666666666666666666666666666667;
    public static final double C2 = 0.00833333333333333333333333333333;
    public static final double C3 = 1.984126984126984126984126984127e-4;
    public static final double C4 = 2.7557319223985890652557319223986e-6;
    public static final double C5 = 2.5052108385441718775052108385442e-8;
    public static final double C6 = 1.6059043836821614599392377170155e-10;

    private static final Term term1 = new Term(1.0, 1);
    private static final Term term2 = new Term(-Utils.C1, 3);
    private static final Term term3 = new Term(Utils.C2, 5);
    private static final Term term4 = new Term(-Utils.C3, 7);
    private static final Term term5 = new Term(Utils.C4, 9);
    private static final Term term6 = new Term(-Utils.C5, 11);
    private static final Term term7 = new Term(Utils.C6, 13);

    private static final List<Term> P1 = p1();
    private static final List<Term> P2 = p2();
    private static final List<Term> P3 = p3();
    private static final List<Term> P4 = p4();
    private static final List<Term> P5 = p5();
    private static final List<Term> P6 = p6();

    private static final Map<String, List<Term>> polynomials =
            new HashMap<String, List<Term>>() {
                {
                    put("P1", P1);
                    put("P2", P2);
                    put("P3", P3);
                    put("P4", P4);
                    put("P5", P5);
                    put("P6", P6);
                }
            };

    private static List<Term> p1() {
        return Arrays.asList(term1, term2, term3);
    }

    private static List<Term> p2() {
        return Arrays.asList(term1, term2, term3, term4);
    }

    private static List<Term> p3() {
        return Arrays.asList(term1, term2, term3, term4, term5);
    }

    private static List<Term> p4() {
        Term term2 = new Term(-0.166, 3);
        Term term3 = new Term(0.00833, 5);

        return Arrays.asList(term1, term2, term3, term4, term5);
    }

    private static List<Term> p5() {
        return Arrays.asList(term1, term2, term3, term4, term5, term6);
    }

    private static List<Term> p6() {
        return Arrays.asList(term1, term2, term3, term4, term5, term6, term7);
    }

    public static double getRandomDoubleBetweenRange(double min, double max) {
        return (Math.random() * ((max - min) + 1)) + min;
    }

    double getMachineAccuracy() {
        double u = 1.0;

        while (1.0d + u != 1.0d) {
            u = u / 10;
        }

        return u;
    }

    boolean checkAdditionOperationAssociativity() {
        double x = 1.0;
        double y = getMachineAccuracy();
        double z = getMachineAccuracy();

        double firstExpression = (x + y) + z;
        double secondExpression = x + (y + z);

        return firstExpression != secondExpression;
    }

    boolean checkMultiplicationOperationAssociativity() {
        double x = 1.01;
        double y = getMachineAccuracy();
        double z = getMachineAccuracy();

        System.out.println("x = " + x + " ");
        double firstExpression = (x * y) * z;
        double secondExpression = x * (y * z);

        return firstExpression != secondExpression;
    }

    public Map<String, Approximation> computeApproximations() {
        HashMap<String, Approximation> approximations = new HashMap<>();

        double min = -Math.PI / 2;
        double max = Math.PI / 2;

        int counter = 0;
        while (counter < 10000) {
            double x = getRandomDoubleBetweenRange(min, max);
            double sin = Math.sin(x);

            for (Map.Entry<String, List<Term>> entry : polynomials.entrySet()) {
                Stopwatch stopwatch = Stopwatch.createStarted();
                double p = Polynomial.compute(entry.getValue(), x);
                stopwatch.stop();
                double error = Math.abs(p - sin);

                approximations.putIfAbsent(entry.getKey(), new Approximation());
                approximations.get(entry.getKey()).addValue(error);
                approximations.get(entry.getKey()).addTime(stopwatch.elapsed(TimeUnit.NANOSECONDS));
            }
            counter++;
        }

        return approximations;
    }

    public void printApproximationsInOrderOfAccuracy(Map<String, Approximation> approximations){
        Set<Map.Entry<String, Approximation>> entries = computeApproximations().entrySet();
        List<Map.Entry<String, Approximation>> sortedEntries = new LinkedList<>(entries);
        sortedEntries.sort(errorComparator);

        System.out.println("Approximations in order of accuracy");
        print(sortedEntries);
    }

    public void printApproximationsInOrderOfTime(Map<String, Approximation> approximations){
        Set<Map.Entry<String, Approximation>> entries = computeApproximations().entrySet();
        List<Map.Entry<String, Approximation>> sortedEntries = new LinkedList<>(entries);
        sortedEntries.sort(timeComparator);

        System.out.println("Approximations in order of time");
        print(sortedEntries);
    }

    private void print(List<Map.Entry<String, Approximation>> sortedEntries) {
        for (Map.Entry<String, Approximation> entry : sortedEntries) {
            System.out.println(entry);
        }
    }

    private Comparator<Map.Entry<String, Approximation>> errorComparator = new Comparator<Map.Entry<String, Approximation>>() {
        @Override
        public int compare(Map.Entry<String, Approximation> o1, Map.Entry<String, Approximation> o2) {
            return o1.getValue().getError().compareTo(o2.getValue().getError());
        }
    };

    private Comparator<Map.Entry<String, Approximation>> timeComparator = new Comparator<Map.Entry<String, Approximation>>() {
        @Override
        public int compare(Map.Entry<String, Approximation> o1, Map.Entry<String, Approximation> o2) {
            return o1.getValue().getTotalTime().compareTo(o2.getValue().getTotalTime());
        }
    };


}



