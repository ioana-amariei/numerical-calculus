import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws Exception {
        SparseMatrix a = new SparseMatrix("separated/a.txt");
        SparseMatrix b = new SparseMatrix("separated/b.txt");

        SparseMatrix aplusb = new SparseMatrix("separated/aplusb.txt");
        SparseMatrix aorib = new SparseMatrix("separated/aorib.txt");

        double[] Ab = AlgebraUtils.readVector("separated/a.vector.txt");
        double[] Bb = AlgebraUtils.readVector("separated/b.vector.txt");
        double[] aoribb = AlgebraUtils.readVector("separated/aorib.vector.txt");

        boolean maximumElementsPerLine = a.hasAtMostNonNullElementsOnEveryLine(12);
        System.out.println("Max 12 elements per line respected: " + maximumElementsPerLine);
//        System.out.println(a.getCompactVisualization());

        System.out.println("aplusbcalc = aplusb: " + a.add(b).equal(aplusb));
//        System.out.println(a.times(b).getCompactVisualization());
//        System.out.println(aorib.getCompactVisualization());
        System.out.println("aoribCalc = aorib: " + a.times(b).equal(aorib));

        double[] x = AlgebraUtils.generateXi(a.getSize());

        double[] aorixCalc = a.times(x);
        System.out.println("A * x = ");
        System.out.println(Arrays.toString(aorixCalc));
        System.out.println(Arrays.toString(Ab));
        System.out.println("Equal: " + AlgebraUtils.equal(aorixCalc, Ab));

        double[] borixCalc = b.times(x);
        System.out.println("B * x = ");
        System.out.println(Arrays.toString(borixCalc));
        System.out.println(Arrays.toString(Bb));
        System.out.println("Equal: " + AlgebraUtils.equal(borixCalc, Bb));

        double[] aoribCalc = a.times(b).times(x);
        System.out.println("A * B * x = ");
        System.out.println(Arrays.toString(aoribCalc));
        System.out.println(Arrays.toString(aoribb));
        System.out.println("Equal: " + AlgebraUtils.equal(aoribCalc, aoribb));
    }
}
