package model;

public class Term {
    private double coefficient;
    private int degree;

    public Term(double coefficient, int degree) {
        this.coefficient = coefficient;
        this.degree = degree;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public int getDegree() {
        return degree;
    }
}
