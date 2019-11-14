package org.openjfx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.*;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class PrimaryController {

    private static final String SCRIPTS_FILEPATH = "\\scripts_v2";
    private static final String CSV_FILEPATH_EXTENSION = "\\scripts_v2\\files_changed_together.csv";

    @FXML
    private TabPane tabPane;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private Tab chartTab;

    @FXML
    private TextField repoDirectory;

    @FXML
    private ScrollPane matrixPane;

    @FXML
    private BorderPane matrixBorderPane;

    @FXML
    private Label currentDirectoryLabel;

    @FXML
    private Button generateButton;

    @FXML
    private ListView<Label> fileExtensionFilter;


//    private TreeMap<String, String> treeMap = new TreeMap<>();
//    Or should I use something like Map<List<String>, Number> map = new HashMap<>();
//    private List<String> dates = new ArrayList<>();
//    private List<String> commits = new ArrayList<>();   // String because nums in csv file are string

    private List<String> fileNames = new ArrayList<>();
    private Map<String, Integer> commitTogetherCount = new HashMap<>();
    private List<String> activeFilters = new ArrayList<>();

    @FXML
    private void generateButtonPressed() {
        if (isValidDirectory()) {
            constructTheRightFile(GitDataCollector.UNIQUE_FILE_EXTENSIONS);
            createFilterTable();
            currentDirectoryLabel.setText("Current directory: " + repoDirectory.getText());
        } else {
            System.out.println(SCRIPTS_FILEPATH + " does not exists!");   // Dummy call
            // TODO counter measures
        }
    }

    private void createFilterTable() {
        try {
            FileReader fileReader = new FileReader( repoDirectory.getText() + SCRIPTS_FILEPATH + "\\Unique_file_extensions.txt");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            ArrayList<Label> list = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Label extension = new Label(line);
                extension.maxWidthProperty().bind(fileExtensionFilter.widthProperty());
                extension.setFont(new Font("System Bold", 14));
                list.add(extension);
            }
            ObservableList<Label> items = FXCollections.observableArrayList(list);
            fileExtensionFilter.setItems(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isValidDirectory() {
        Path path = Paths.get(repoDirectory.getText() + SCRIPTS_FILEPATH);
        if (repoDirectory.getText().isEmpty()) {
            wrongPathAlert();
            return false;
        } else if (Files.notExists(path)) {
            wrongPathAlert();
            return false;
        } else
            return true;
    }

    private void wrongPathAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Wrong Path!");
        alert.setHeaderText(null);
        alert.setContentText("Either you have not entered a path or the repository does not contain \"" + SCRIPTS_FILEPATH + "\" folder.");
        alert.showAndWait();
    }

    @FXML
    private void browseButtonPressed() {
        activeFilters.clear();  // Reset the filter just in case.
        DirectoryChooser directoryChooser = new DirectoryChooser();
        try {
            String path = directoryChooser.showDialog(App.stage).toString();
            repoDirectory.setText(path);
        } catch (NullPointerException e) {
            System.out.println("No file selected.");    // Actually not an error.
        }
    }

    @FXML
    private void setFileExtensionFilterClicked(MouseEvent click) {
        if (click.getClickCount() == 2) {
            String selectionStyle = fileExtensionFilter.getSelectionModel().getSelectedItem().getStyle();
            if (selectionStyle.equals(("-fx-text-fill: maroon; -fx-background-color: peachpuff;"))) {
                fileExtensionFilter.getSelectionModel().getSelectedItem().setStyle("-fx-text-fill: black; -fx-background-color: inherit");
                activeFilters.remove(fileExtensionFilter.getSelectionModel().getSelectedItem().getText());
            } else {
                fileExtensionFilter.getSelectionModel().getSelectedItem().setStyle("-fx-text-fill: maroon; -fx-background-color: peachpuff;");
                activeFilters.add(fileExtensionFilter.getSelectionModel().getSelectedItem().getText());
            }
        }
    }

    // TODO Accept data from the filter and create matrix according to the filter
    @FXML
    private void generateCoOccurrenceMatrix() {
        setGridPaneMatrix();
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
        String csvFilePath = repoDirectory.getText() + CSV_FILEPATH_EXTENSION;
//        System.out.println(csvFilePath);    // Test
        if (!activeFilters.isEmpty()) {
            constructTheRightFile(GitDataCollector.FILTER_FINAL_DUMP);
            constructTheRightFile(GitDataCollector.FILES_CHANGED_TOGETHER);
        } else {
            // TODO Implement else
            System.out.println("Not yet implemented");
            System.exit(0);
        }
        try {
            FileReader fileReader = new FileReader(csvFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
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

    // TODO Find a better name for this.
    private void constructTheRightFile(GitDataCollector argument) {
        StringBuilder argumentsAsString = new StringBuilder(argument.toString());
        String result;
        if (argument.equals(GitDataCollector.FILTER_FINAL_DUMP)) {
            argumentsAsString.append(" ");
            for (String filter: activeFilters)
                argumentsAsString.append(filter).append(", ");
            result = argumentsAsString.toString().substring(0, argumentsAsString.toString().length() - 2);
            System.out.println("Filter arguments: " + result);
        } else
            result = argumentsAsString.toString();
        try {
            String path = repoDirectory.getText() + SCRIPTS_FILEPATH + "\\dummy.bat";
            Runtime runtime = Runtime.getRuntime();
            Process p = runtime.exec("cmd.exe /c start \"\" \""+ path + "\" " + result, null, new File(repoDirectory.getText() + SCRIPTS_FILEPATH));
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
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
        String csvFilePath = repoDirectory.getText();
        csvFilePath += CSV_FILEPATH_EXTENSION;
        try {
            FileReader fileReader = new FileReader(csvFilePath);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineAsArray = line.split(",");
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
        commitTogetherCount.merge(commit, 1, Integer::sum);
    }


}
