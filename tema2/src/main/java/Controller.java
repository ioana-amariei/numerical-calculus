import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import model.Matrix;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.*;

public class Controller {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button LUDecompositionButton;

    @FXML
    private ScrollPane LUDecompositionScrollPane;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea LUDecompositionTextArea;

    @FXML
    private TextArea infoTextArea;

    @FXML
    private TextArea vectorTextArea;


    @FXML
    void initialize() {
        assert LUDecompositionButton != null : "fx:id=\"LUDecompositionButton\" was not injected: check your FXML file 'sample.fxml'.";
        assert LUDecompositionScrollPane != null : "fx:id=\"LUDecompositionScrollPane\" was not injected: check your FXML file 'sample.fxml'.";
        assert inputTextArea != null : "fx:id=\"inputTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert LUDecompositionTextArea != null : "fx:id=\"LUDecompositionTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert infoTextArea != null : "fx:id=\"infoTextArea\" was not injected: check your FXML file 'sample.fxml'.";
        assert vectorTextArea != null : "fx:id=\"vectorTextArea\" was not injected: check your FXML file 'sample.fxml'.";

    }

    private double[] getPrependedArray(String text) {
        double[] v = readArray(text);
        double[] b = new double[v.length + 1];

        for (int i = 0; i < v.length; i++) {
            b[i + 1] = v[i];
        }

        return b;
    }

    private double[] readArray(String s) {
        List<Double> array = new ArrayList<>();

        Scanner scanner = new Scanner(s);
        while (scanner.hasNextDouble()) {
            array.add(scanner.nextDouble());
        }

        double[] t = new double[array.size()];
        for (int i = 0; i < array.size(); ++i) {
            t[i] = array.get(i);
        }

        return t;

    }

    @FXML
    void LUDecompositionHandler(ActionEvent event) throws FileNotFoundException {
        String inputMatrix = inputTextArea.getText();
        Matrix A = new Matrix(inputMatrix, true);
        Matrix Ainit = new Matrix(inputMatrix, true);

        A.LUDecomposition();
        LUDecompositionTextArea.setText(A.toString());

        double[] b = getPrependedArray(vectorTextArea.getText());
        double[] x = A.solve(b);

        Jama.Matrix matrix = new Jama.Matrix(Ainit.getMatrix1());
        double[] b2 = readArray(vectorTextArea.getText());
        System.out.println(Arrays.toString(b2));
        double[][] b3 = new double[b2.length][1];

        for (int i = 0; i < b2.length; i++) {
            b3[i][0] = b2[i];
        }


        Jama.Matrix B = new Jama.Matrix(b3);
        B.print(2,3);
        Jama.Matrix result = matrix.solve(B);
        result.transpose().print(10, 10);

        double norm1 = Main.norm2(Arrays.copyOfRange(x, 1, x.length), result.transpose().getArray()[0]);
        double norm2 = Main.norm2(Arrays.copyOfRange(x, 1, x.length), matrix.inverse().times(B).transpose().getArray()[0]);

        System.out.println(norm1);
        System.out.println(norm2);

        StringBuilder sb = new StringBuilder();
        sb.append("Det: " + A.det() + "\n");
        sb.append("Ainit norm: " + Ainit.norm2(x, b) + "\n");
        sb.append("Ax = b: " + Arrays.toString(A.solve(b)) + "\n");
        sb.append("Norm 1: " + norm1 + "\n");
        sb.append("Norm 2: " + norm2 + "\n");

        infoTextArea.setText(sb.toString());
    }

}
