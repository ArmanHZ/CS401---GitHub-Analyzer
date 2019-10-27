package org.openjfx;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PrimaryController {

    @FXML
    private TabPane tabPane;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Tab chartTab;

    @FXML
    private TextField commitCsvPath;

    @FXML
    private ScrollPane matrixPane;

    @FXML
    private BorderPane matrixBorderPane;


//    private TreeMap<String, String> treeMap = new TreeMap<>();
//    Or should I use something like Map<List<String>, Number> map = new HashMap<>();
    private List<String> dates = new ArrayList<>();
    private List<String> commits = new ArrayList<>();   // String because nums in csv file are string

    private List<String> fileNames = new ArrayList<>();

    private Map<String, Integer> commitTogetherCount = new HashMap<>();

    // CSV button pressed
    @FXML
    private void addDataToBarChart() {
        barChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Commit information");
        readCsvFileForGraph();
        for (int i = 0; i < dates.size(); ++i) {
            String date = dates.get(i);
            int commitCount = Integer.parseInt(commits.get(i));
            series.getData().add(new XYChart.Data<>(date, commitCount));
        }
        barChart.getData().add(series);
    }

    private void readCsvFileForGraph() {
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
    private void csvTextFieldPressEnter(ActionEvent ae) {
        setGridPaneMatrix();    // For test purposes.
//        addDataToBarChart();
    }

    private void setGridPaneMatrix() {
        getDifferentFilesInCsv();
        GridPane gridPane = new GridPane();
//        gridPane.setGridLinesVisible(true); // For debugging
        setGridPaneConstraints(gridPane);
        gridPane.add(new Button("Options"), 0, 0);
        setGridPaneFirstColumn(gridPane);
        setGridPaneFirstRow(gridPane);
        setGridPaneMiddleRowsColumns(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        matrixBorderPane.setCenter(gridPane);
    }

    private void getDifferentFilesInCsv() {
        fileNames.clear();
        String csvFilePath = commitCsvPath.getText();
        try {
            FileReader fileReader = new FileReader(csvFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String lineAsArray[] = line.split(",");
                for (int i = 2; i < lineAsArray.length; ++i)
                    addToArrayIfDistinct(lineAsArray[i]);
            }
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

    private void addToArrayIfDistinct(String fileName) {
        if (fileNames.isEmpty())
            fileNames.add(fileName);
        else {
            for (String fileNameInArray : fileNames) {
                if (fileNameInArray.equals(fileName))
                    return;
            }
            fileNames.add(fileName);
        }
    }

    private void setGridPaneConstraints(GridPane gridPane) {
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.BOTTOM);
        gridPane.getRowConstraints().add(rowConstraints);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.RIGHT);
        gridPane.getColumnConstraints().add(columnConstraints);

        gridPane.getStyleClass().add("matrix");
        matrixBorderPane.prefWidthProperty().bind(matrixPane.widthProperty());
        matrixBorderPane.prefHeightProperty().bind(matrixPane.heightProperty());
        gridPane.setHgap(2);
        gridPane.setVgap(2);
    }

    private void setGridPaneFirstColumn(GridPane gridPane) {
        for (int columns = 1; columns < fileNames.size(); ++columns) {
            VerticalLabel columnNames = new VerticalLabel(VerticalDirection.DOWN);
            columnNames.setText(fileNames.get(columns - 1));
            columnNames.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.add(columnNames, columns, 0);
        }
    }

    private void setGridPaneFirstRow(GridPane gridPane) {
        for (int rows = 1; rows < fileNames.size(); ++rows) {
            Label rowNames = new Label(fileNames.get(rows - 1));
            rowNames.setMinWidth(Region.USE_PREF_SIZE);
            gridPane.add(rowNames, 0, rows);
        }
    }

    private void setGridPaneMiddleRowsColumns(GridPane gridPane) {
        cartesianProductCommits();
        for (int rows = 1; rows < fileNames.size(); ++rows) {
            for (int columns = 1; columns < fileNames.size(); ++columns) {
                Label label = setMatrixCell(rows - 1, columns - 1);
                gridPane.add(label, rows, columns);
            }
        }
    }

    private void cartesianProductCommits() {
        commitTogetherCount.clear();
        String csvFilePath = commitCsvPath.getText();
        try {
            FileReader fileReader = new FileReader(csvFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String lineAsArray[] = line.split(",");
                for (int i = 2; i < lineAsArray.length; ++i) {
                    for (int j = i + 1; j < lineAsArray.length; ++j) {
                        String commit = lineAsArray[i];
                        commit += "," + lineAsArray[j];
                        increaseCommitTogetherCount(commit);
                    }
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Label setMatrixCell(int row, int column) {
        String cell = fileNames.get(row) + "," + fileNames.get(column);
        int numChangedTogether;
        if (commitTogetherCount.get(cell) == null) {
            cell = reverseCell(cell);
            if (commitTogetherCount.get(cell) == null)
                numChangedTogether = 0;
            else
                numChangedTogether = commitTogetherCount.get(cell);
        } else {
            numChangedTogether = commitTogetherCount.get(cell);
        }
        Label label = new Label();
        Tooltip tooltip = new Tooltip();
        tooltip.setText("Files: \n" + fileNames.get(row) + "\n" + fileNames.get(column) + "\nChanged together count: " + numChangedTogether);
        label.setTooltip(tooltip);
        int red = numChangedTogether * 20;
        int green = 255 - (numChangedTogether * 10);
        label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0);");
        label.getStyleClass().add("matrixCells");
        label.setMinSize(20, 20);
        return label;
    }

    private String reverseCell(String cell) {
        String[] splitCell = cell.split(",");
        return splitCell[1] + "," + splitCell[0];
    }

    private void increaseCommitTogetherCount(String commit) {
        commitTogetherCount.merge(commit, 1, (a, b) -> a + b);
    }


}
