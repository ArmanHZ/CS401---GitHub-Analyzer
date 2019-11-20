package application;



import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class VisualizationThread extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("GridPane Experiment");
        
        GridPane gridPane = new GridPane();
        for(int i=0;i<10;i++){
        	for(int j=0;j<10;j++){
        	Label label =new Label("label"+i+j);
        	 gridPane.add(label, i, j, 1,1);
        	 if(i>0 && j>0){
        	    label.setStyle("-fx-border-color: black; -fx-font-size: 10;");
        	 }
        	 
        		 
        	// setStyle("-fx-background-color: green;")
        	 }
        }
             
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}