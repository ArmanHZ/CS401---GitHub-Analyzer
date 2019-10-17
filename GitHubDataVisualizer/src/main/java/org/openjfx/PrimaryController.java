package org.openjfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

public class PrimaryController {

    @FXML
    private TabPane tabPane;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Tab chartTab;

    @FXML
    private TextField commitCsvPath;

//    private TreeMap<String, String> treeMap = new TreeMap<>();
//    Or should I use something like Map<List<String>, Number> map = new HashMap<>();

    private List<String> dates = new ArrayList<>();
    private List<String> commits = new ArrayList<>();   // String because nums in csv file are string

    // CSV button pressed
    @FXML
    private void addDataToBarChart() {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Commit information");
        readCsvFile();
        for (int i = 0; i < dates.size(); ++i) {
            String date = dates.get(i);
            int commitCount = Integer.parseInt(commits.get(i));
            series.getData().add(new XYChart.Data<>(date, commitCount));
        }
        barChart.getData().add(series);
    }

    private void readCsvFile() {
        String csvFilePath = commitCsvPath.getText();
        try {
            FileReader fileReader = new FileReader(csvFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] values = line.split(",");
                dates.add(values[0]);
                commits.add(values[1]);
            }
            tabPane.getSelectionModel().select(chartTab);
            bufferedReader.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("CSV File could not be opened!");
            alert.setHeaderText(null);
            alert.setContentText("Error while opening the CSV file.\nPlease check your file path." +
                    "\nYour file path was: " + csvFilePath);

            alert.showAndWait();
        }
    }

    @FXML
    private void csvTextFieldPressEnter(ActionEvent ae) { addDataToBarChart(); }

}
