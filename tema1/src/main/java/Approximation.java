import java.util.LinkedList;
import java.util.List;

public class Approximation {
    private List<Double> values;
    private List<Double> times;

    public Approximation() {
        this.values = new LinkedList<>();
        this.times = new LinkedList<>();
    }


    public void addValue(double value) {
        values.add(value);
    }

    public void addTime(double time) {
        times.add(time);
    }

    public Double getTotalTime(){
        return times.stream().mapToDouble(Double::doubleValue).sum();
    }


    public Double getError(){
        return values.stream().mapToDouble(Double::doubleValue).average().getAsDouble();
    }

    @Override
    public String toString() {
        return "Approximation{" +
                "values=" + getError() +
                ", times=" + getTotalTime() +
                '}';
    }
}
