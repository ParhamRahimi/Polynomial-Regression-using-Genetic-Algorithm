package two;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Parham on 07-Jun-19.
 */
public class InputGenerator {
    private double[] coefficients;

    public InputGenerator() {
//        coefficients = new double[]{3.375, -33.75, 112.5, -122};
        coefficients = new double[]{1, -11, 31, -21};
    }

    public InputGenerator(double[] coefficients) {
        this.coefficients = coefficients;
    }

    public void execute() {
        try {
            writeDataToFile(generator(), "input");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<ArrayList<Double>> generator() {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        ArrayList<Double> inside;
        for (double i = 0; i < 9.9; i += 0.1) {
            inside = new ArrayList<>();
            inside.add(0, i);
            inside.add(1, coefficients[0] * i * i * i + coefficients[1] * i * i + coefficients[2] * i + coefficients[3]);
            result.add(inside);
        }
        return result;
    }

    private void writeDataToFile(ArrayList<ArrayList<Double>> inpData, String fileName) throws IOException {
        FileWriter fileWriter = new FileWriter("./" + fileName + ".csv");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        for (int i = 0; i < inpData.size(); i++) {
            String res = "";
            for (int j = 0; j < inpData.get(i).size(); j++) {
                if (j == inpData.get(i).size() - 1)
                    res += inpData.get(i).get(j);
                else
                    res += inpData.get(i).get(j) + ",";
            }
            printWriter.println(res);
        }
        printWriter.close();
    }

}
