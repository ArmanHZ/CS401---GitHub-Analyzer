package org.openjfx;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PrimaryController {

    // Paths
    private static final String SCRIPTS_FILEPATH = "\\scripts_v2";
    private static final String CSV_FILEPATH_EXTENSION = "\\scripts_v2\\files_changed_together.csv";

    private static boolean WITH_MERGES = false;

    // Objects from primary.fxml
    @FXML private TabPane tabPane;
    @FXML private TextField repoDirectory;
    @FXML private ScrollPane matrixPane;
    @FXML private BorderPane matrixBorderPane;
    @FXML private Label currentDirectoryLabel;
    @FXML private ListView<Label> fileExtensionFilter;
    @FXML private CheckBox dateRestrictionCheckBox;
    @FXML private CheckBox withMergesCheckBox;
    @FXML private DatePicker datePickerBefore;
    @FXML private DatePicker datePickerAfter;

    private List<String> fileNames = new ArrayList<>();
    private Map<String, Integer> commitTogetherCount = new HashMap<>();
    private List<String> activeFilters = new ArrayList<>();

    private ArgumentParser argumentParser;

    @FXML
    private void generateButtonPressed() {
        argumentParser = new ArgumentParser(repoDirectory.getText().toString(), true, this);
        activeFilters.clear();  // Reset the filters.
        if (isValidDirectory()) {
            File fp = new File(repoDirectory.getText());
            boolean result;
            if (fp.exists()) {
                result = overWriteFinalDumpPopUp();
            } else {
                System.out.println(SCRIPTS_FILEPATH + " does not exists!");
                return;
            }
            if (result) {
                createCorrectFile();
            }
            argumentParser.parseInput(GitDataCollector.UNIQUE_FILE_EXTENSIONS);
            createFilterTable();
            currentDirectoryLabel.setText("Current directory: " + repoDirectory.getText());

        } else {
            System.out.println(SCRIPTS_FILEPATH + " does not exists!");
        }
    }

    private boolean overWriteFinalDumpPopUp() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("final_dump.txt exists.\nDo you want to overwrite it?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;    // .orElse is .get but safer. Apparently.
    }

    private void createCorrectFile() {
        if (!dateRestrictionCheckBox.isSelected() && !withMergesCheckBox.isSelected())
            argumentParser.parseInput(GitDataCollector.LOG_NO_MERGES);
        else if (!dateRestrictionCheckBox.isSelected() && withMergesCheckBox.isSelected())
            argumentParser.parseInput(GitDataCollector.LOG);
        else if (dateRestrictionCheckBox.isSelected() && !withMergesCheckBox.isSelected())
            argumentParser.parseInput(GitDataCollector.LOG_DATE_RESTRICTED_NO_MERGES);
        else if (dateRestrictionCheckBox.isSelected() && withMergesCheckBox.isSelected())
            argumentParser.parseInput(GitDataCollector.LOG_DATE_RESTRICTED);
    }

    private void createFilterTable() {
        try {
            FileReader fileReader = new FileReader(repoDirectory.getText() + SCRIPTS_FILEPATH + "\\Unique_file_extensions.txt");
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
            if (selectionStyle.equals(("-fx-text-fill: darkgreen; -fx-background-color: mediumseagreen;"))) {
                fileExtensionFilter.getSelectionModel().getSelectedItem().setStyle("-fx-text-fill: black; -fx-background-color: inherit");
                activeFilters.remove(fileExtensionFilter.getSelectionModel().getSelectedItem().getText());

            } else {
                fileExtensionFilter.getSelectionModel().getSelectedItem().setStyle("-fx-text-fill: darkgreen; -fx-background-color: mediumseagreen;");
                activeFilters.add(fileExtensionFilter.getSelectionModel().getSelectedItem().getText());
            }
        }
    }

    @FXML
    private void generateCoOccurrenceMatrix() {
        if (fileExtensionFilter.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Filters could not be loaded!");
            alert.setHeaderText(null);
            alert.setContentText("Please first select a folder and click Generate.");
            alert.showAndWait();
        } else {
            argumentParser.setActiveFilters(activeFilters);
            setGridPaneMatrix();
            tabPane.getSelectionModel().select(2);
        }
    }

    private void setGridPaneMatrix() {
        getDifferentFilesInCsv();
        GridPane gridPane = new GridPane();
//        gridPane.setGridLinesVisible(true); // For debugging
        setGridPaneConstraints(gridPane);
        Button optionsButton = new Button("Options");
        setOptionsButtonEventListener(optionsButton, gridPane);
        gridPane.add(optionsButton, 0, 0);
        setGridPaneFirstColumn(gridPane);
        setGridPaneFirstRow(gridPane);
        setGridPaneMiddleRowsColumns(gridPane);
        gridPane.setAlignment(Pos.CENTER);
        matrixBorderPane.setCenter(gridPane);
    }

    private void getDifferentFilesInCsv() {
        fileNames.clear();
        String csvFilePath = repoDirectory.getText() + CSV_FILEPATH_EXTENSION;
        if (!activeFilters.isEmpty()) {
            filterAndFindChangedTogether();
        } else {
            if (entireFilterWarning()) {
                for (Label label : fileExtensionFilter.getItems()) {
                    String labelText = label.getText();
                    activeFilters.add(labelText);
                }
                filterAndFindChangedTogether();
            } else
                return;
        }
        try {
            Thread.sleep(2500);    // Minimum necessary pause for the files get ready for visualization.
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean entireFilterWarning() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("This will create a Matrix with all the filters active.\nThis may take a very long time depending on the file size.\nDo you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.orElse(null) == ButtonType.OK;    // .orElse is .get but safer. Apparently.
    }

    private void filterAndFindChangedTogether() {
        try {
            argumentParser.parseInput(GitDataCollector.FILTER_FINAL_DUMP);
            Thread.sleep(2500);    // Minimum necessary pause for the files get ready for visualization.
            argumentParser.parseInput(GitDataCollector.FILES_CHANGED_TOGETHER);
            Thread.sleep(2500);    // Minimum necessary pause for the files get ready for visualization.
        } catch (InterruptedException e) {
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

    private void setOptionsButtonEventListener(Button optionsButton, GridPane gridPane) {
        optionsButton.setOnAction(event -> {
            VBox vBox = new VBox();
            vBox.setPadding(new Insets(10, 50, 50, 50));
            vBox.setSpacing(10);
            vBox.setAlignment(Pos.BASELINE_CENTER);
            Stage stage = new Stage();
            stage.setTitle("Options menu");
            stage.setScene(new Scene(vBox, 300, 100));

            Button sortButton = new Button("Sort matrix");
            sortMatrixButtonPressed(sortButton, gridPane);

            Button extractMapping = new Button("Extract mapping of matrix");
            extractMappingPressed(extractMapping);

            vBox.getChildren().addAll(sortButton, extractMapping);
            stage.show();
        });
    }

    private void sortMatrixButtonPressed(Button sortButton, GridPane gridPane) {
        sortButton.setOnAction(actionEvent -> {
            // Sorting Hashmap
            List<Map.Entry<String, Integer>> list = new LinkedList<>(commitTogetherCount.entrySet());
            list.sort(Comparator.comparing(Map.Entry::getValue));
            Collections.reverse(list);
            HashMap<String, Integer> sorted = new LinkedHashMap<>();
            for (Map.Entry<String, Integer> entry : list) {
                sorted.put(entry.getKey(), entry.getValue());
            }
            commitTogetherCount = sorted;
//            commitTogetherCount.forEach((s, integer) -> System.out.println(s + "\t" + integer));
            setGridPaneMiddleRowsColumnsSorted(gridPane);
        });
    }

    private void extractMappingPressed(Button extractMappingButton) {
        extractMappingButton.setOnAction(actionEvent -> {
            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter(repoDirectory.getText() + SCRIPTS_FILEPATH + "\\Data_dump\\map.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedWriter finalWriter = writer;
            commitTogetherCount.forEach((key, value) -> {
                String[] keySplit = key.split(",");
                String result = keySplit[0] + " " + keySplit[1] + " " + value;
                try {
                    assert finalWriter != null;
                    finalWriter.write(result + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });
    }

    private void setGridPaneFirstColumn(GridPane gridPane) {
        for (int columns = 0; columns < fileNames.size(); ++columns) {
            VerticalLabel columnNames = new VerticalLabel(VerticalDirection.DOWN);
            columnNames.setText(fileNames.get(columns));
            columnNames.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.add(columnNames, columns + 1, 0);
        }
    }

    private void setGridPaneFirstRow(GridPane gridPane) {
        for (int rows = 0; rows < fileNames.size(); ++rows) {
            Label rowNames = new Label(fileNames.get(rows));
            rowNames.setMinWidth(Region.USE_PREF_SIZE);
            gridPane.add(rowNames, 0, rows + 1);
        }
    }

    private void setGridPaneMiddleRowsColumns(GridPane gridPane) {
        cartesianProductCommits();
        System.out.println(fileNames.size());
        for (int rows = 0; rows < fileNames.size(); ++rows) {
            for (int columns = 0; columns < fileNames.size(); ++columns) {
                Label label = setMatrixCell(rows, columns);
                gridPane.add(label, rows + 1, columns + 1);
            }
        }
    }

    // TODO Refactor
    private void setGridPaneMiddleRowsColumnsSorted(GridPane gridPane) {
        gridPane.getChildren().clear();
        List<String> rowNames = new ArrayList<>();
        List<String> columnNames = new ArrayList<>();
        commitTogetherCount.forEach((key, value) -> {
            String[] keySplit = key.split(",");
            if (!rowNames.contains(keySplit[0]))
                rowNames.add(keySplit[0]);
            if (!columnNames.contains(keySplit[1]))
                columnNames.add(keySplit[1]);
        });
        System.out.println("Size after sort: " + commitTogetherCount.size());
        setFirstColumnAndRowFromSortedMap(gridPane, columnNames, rowNames);
        for (int rows = 0; rows < rowNames.size(); ++rows) {
            for (int columns = 0; columns < columnNames.size(); ++columns) {
                String toBeSearched = rowNames.get(rows) + "," + columnNames.get(columns);
                int numChangedTogether;
                if (commitTogetherCount.get(toBeSearched) == null)
                    numChangedTogether = 0;
                else
                    numChangedTogether = commitTogetherCount.get(toBeSearched);
                Label label = new Label();
                gridPane.add(label, columns + 1, rows + 1);
                Tooltip tooltip = new Tooltip();
                tooltip.setText("Files: \n" + rowNames.get(rows) + "\n" + columnNames.get(columns) + "\nChanged together count: " + numChangedTogether);
                label.setTooltip(tooltip);
                int red = numChangedTogether * 20;
                int green = 255 - (numChangedTogether * 10);
                label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0);");
                label.getStyleClass().add("matrixCells");
                label.setMinSize(20, 20);
            }
        }
    }

    private void setFirstColumnAndRowFromSortedMap(GridPane gridPane, List columnNames, List rowNames) {
        for (int columns = 0; columns < columnNames.size(); ++columns) {
            VerticalLabel columnName = new VerticalLabel(VerticalDirection.DOWN);
            columnName.setText((String) columnNames.get(columns));
            columnName.setMinHeight(Region.USE_PREF_SIZE);
            gridPane.add(columnName, columns + 1, 0);
        }
        for (int rows = 0; rows < rowNames.size(); ++rows) {
            Label rowName = new Label((String) rowNames.get(rows));
            rowName.setMinWidth(Region.USE_PREF_SIZE);
            gridPane.add(rowName, 0, rows + 1);
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

    @FXML
    private void dateRestrictionCheckBoxClicked() {
        if (dateRestrictionCheckBox.isSelected()) {
            datePickerBefore.setDisable(false);
            datePickerAfter.setDisable(false);
        } else {
            datePickerBefore.setDisable(true);
            datePickerAfter.setDisable(true);
        }
    }

    @FXML
    private void withMergesCheckBoxClicked() {
        WITH_MERGES = withMergesCheckBox.isSelected();
        System.out.println(WITH_MERGES);
    }

    @FXML
    private void clearFilters() {
        for (Label label : fileExtensionFilter.getItems()) {
            label.setStyle("-fx-text-fill: black; -fx-background-color: inherit");
        }
        activeFilters.clear();
    }

    String getDate() {
        if (dateRestrictionCheckBox.isSelected()) {
            StringBuilder stringBuilder = new StringBuilder();
            if (datePickerAfter.getValue() != null)
                stringBuilder.append("--after=").append(datePickerAfter.getValue());
            if (datePickerBefore.getValue() != null)
                stringBuilder.append(" ").append("--before=").append(datePickerBefore.getValue());
            return stringBuilder.toString();
        }
        return null;
    }

}
