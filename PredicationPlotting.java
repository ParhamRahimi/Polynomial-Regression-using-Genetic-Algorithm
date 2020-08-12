package two;

/**
 * Created by Parham on 07-Jun-19.
 */

import com.opencsv.CSVReader;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PredicationPlotting extends Application {
    private static double[] coefficient;
    private static boolean type;
    private static ArrayList<Double> maxFitness;
    private static ArrayList<Double> minFitness;
    private static ArrayList<Double> avgFitness;

    @Override
    public void start(Stage stage) {
        if (type) {
            //Defining the axes
            NumberAxis xAxis = new NumberAxis(0, 10, 0.1);
            xAxis.setLabel("X");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Y");

            //Creating the Scatter chart
            ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);

            //Prepare XYChart.Series objects by setting data
            XYChart.Series series = new XYChart.Series();
            try (CSVReader dataReader = new CSVReader(new FileReader("input.csv"))) {
                String[] nextLine;
                while ((nextLine = dataReader.readNext()) != null) {
                    double x = Double.parseDouble(nextLine[0]);
                    double y = Double.parseDouble(nextLine[1]);
                    series.getData().add(new XYChart.Data(x, y));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            series.setName("Input");

            XYChart.Series series2 = new XYChart.Series();
/*
        for (int i = 0; i < 10000; i++) {
            double x = i / 1000;
            double y = 10 * (coefficient[0] * i * i * i + coefficient[1] * i * i + coefficient[2] * i + coefficient[3]);
            series2.getData().add(new XYChart.Data(x, y));
        }
*/
            for (double i = 0; i < 9.9; i += 0.1) {
                double x = i;
                double y = (coefficient[0] * i * i * i + coefficient[1] * i * i + coefficient[2] * i + coefficient[3]);
                series2.getData().add(new XYChart.Data(x, y));
            }
            series2.setName("Predication");
            //Setting the data to scatter chart
            scatterChart.getData().addAll(series, series2);
/*
        scatterChart.getData().addAll(series2);
*/

            //Creating a Group object
            Group root = new Group(scatterChart);

            //Creating a scene object
            Scene scene = new Scene(root, 1400, 800);
            scatterChart.setPrefSize(1400, 800);

            //Setting title to the Stage
            stage.setTitle("Parham Rahimi - Generic");

            //Adding scene to the stage
            stage.setScene(scene);

            //Displaying the contents of the stage
            stage.show();
        } else {
            //Defining the axes
            NumberAxis xAxis = new NumberAxis();
            xAxis.setLabel("Generations");

            NumberAxis yAxis = new NumberAxis();
            yAxis.setLabel("Fitness");

            //Creating the Scatter chart
            ScatterChart<String, Number> scatterChart = new ScatterChart(xAxis, yAxis);

            //Prepare XYChart.Series objects by setting data
            XYChart.Series series = new XYChart.Series();
            for (int i = 0; i < maxFitness.size(); i++) {
                double x = i;
                double y = maxFitness.get(i);
                series.getData().add(new XYChart.Data(x, y));
            }
            series.setName("Best");

            XYChart.Series series2 = new XYChart.Series();
            for (int i = 0; i < minFitness.size(); i++) {
                double x = i;
                double y = minFitness.get(i);
                series2.getData().add(new XYChart.Data(x, y));
            }
            series2.setName("Worst");

            XYChart.Series series3 = new XYChart.Series();
            for (int i = 0; i < avgFitness.size(); i++) {
                double x = i;
                double y = avgFitness.get(i);
                series3.getData().add(new XYChart.Data(x, y));
            }
            series3.setName("Average");

            //Setting the data to scatter chart
            scatterChart.getData().addAll(series2, series3, series);

            //Creating a Group object
            Group root = new Group(scatterChart);

            //Creating a scene object
            Scene scene = new Scene(root, 1400, 800);
            scatterChart.setPrefSize(1400, 800);

            //Setting title to the Stage
            stage.setTitle("Parham Rahimi - Generic");

            //Adding scene to the stage
            stage.setScene(scene);

            //Displaying the contents of the stage
            stage.show();

        }
    }

    /*
        public static void main(String args[]) {
            launch(args);
        }

    */

    public static void run(String args[], double[] genes) {
        type = true;
        coefficient = genes;
        launch(args);
    }

    public static void run(String args[], ArrayList<Double> max, ArrayList<Double> min, ArrayList<Double> avg) {
        type = false;
        maxFitness = max;
        minFitness = min;
        avgFitness = avg;
        launch(args);
    }

}