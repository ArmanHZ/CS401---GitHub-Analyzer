package application;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.print.DocFlavor.URL;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
 
 
public class Main extends Application {
	
	
	 private static  ArrayList <String> names = new ArrayList<String>();
	 private static ArrayList <String> authors = new ArrayList<String>();
	 private static  ArrayList <Integer> commitCount = new ArrayList<Integer>();
	
	 
	
	
	
	
	
 
   @Override public void start(Stage stage) throws IOException  {
	              File file = new File("final_dump.txt");
		          Scanner f = new Scanner(file);
		          int counter = 0;
		          while(f.hasNextLine()) {
		        	  counter++;
				      String commitHash = f.nextLine();
				      counter++;
				      String Author = f.next();
				      String  a = f.next();
				      String name = f.next();
				      
				      names.add(name);
			          String nameline = f.nextLine();
			          counter++;
			   
				      String x= f.nextLine();
				      counter++;
				
			          String y= f.nextLine();
			          counter++;
				      String z= f.nextLine();
				      counter++;
				      String v= f.nextLine();
				      counter++;
				      String u= f.nextLine();
				      counter++;
				
					System.out.println(name);
			          System.out.println(counter);
			      }
		          int count=0;
		          for( String name:names){
			         if(!authors.contains(name)){
				         authors.add(name);
			         }
		          }
		 
		          for(String author: authors){
			          for(String name:names){
				          if(author.equals(name)){
					           count++;
				          }
			          }
			           commitCount.add(count);
		              //System.out.println("Occurrence of " + author + " is " + count + " times.");
	                   count=0;
		          }
		 
		          stage.setTitle("Commited Lines");
       
                  // piechart data 
                  PieChart.Data data[] = new PieChart.Data[authors.size()]; 
    
                  // string and integer data 
                  for (int i = 0; i <authors.size(); i++) { 
                       data[i] = new PieChart.Data(authors.get(i),commitCount.get(i)); 
                  } 
    
                  // create a pie chart 
                  PieChart pie_chart = new PieChart(FXCollections.observableArrayList(data)); 
                  
                  pie_chart.setLabelLineLength(0.5);
                  pie_chart.setLegendSide(Side.BOTTOM);
             
                  // create a Group 
                 Group group = new Group(pie_chart); 
    
                  // create a scene 
                 Scene scene = new Scene(group, 600, 600); 
    
                  // set the scene 
                 stage.setScene(scene); 
    
                 stage.show(); 
 
      /*  ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data(B�kre, 13),
                new PieChart.Data(Fatih, 25),
                new PieChart.Data(Arman, 10),
                new PieChart.Data(�brahim, 22),
                new PieChart.Data(Hasan, 30));
        final PieChart chart = new PieChart(pieChartData); */
        
    } 
 
    public static void main(String[] args) throws IOException {
    	
    	
    
        launch(args);
    }
}

