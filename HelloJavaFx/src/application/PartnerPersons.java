package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class PartnerPersons extends Application {
	 private static  ArrayList <String> names = new ArrayList<String>();
	 private static ArrayList <String> authors = new ArrayList<String>();
	 
	 
	 private static ArrayList <String> filenamesline = new ArrayList<String>();
	 private static ArrayList <String> filenamesUseless = new ArrayList<String>();
	 private static ArrayList <String> allfilenames = new ArrayList<String>();
	 private static ArrayList <String> filenames = new ArrayList<String>();
	 private static ArrayList <String> filesinfo = new ArrayList<String>();
	 
	 
	 

	@Override
	public void start(Stage stage) throws IOException {
		 File file = new File("final_dump.txt");
		 Scanner f = new Scanner(file);
		 while(f.hasNextLine()) {
				String line1 = f.nextLine();
				String Author = f.next();
				String  a = f.next();
				String name = f.next();
				
				names.add(name);
			    String nameline = f.nextLine();
			 	String line3= f.nextLine();
				String line4= f.nextLine();
				String line5= f.nextLine();
				String filenameline= f.nextLine();
				
				filenamesline.add(filenameline);
				String line7= f.nextLine();
				
				filesinfo.add(name+" "+filenameline);
				
		     //	System.out.println(name);
			//	System.out.println(date);
			//	System.out.println(filenameline);
				
				
	    }
		 for(String fileinfo:filesinfo){
			 System.out.println(fileinfo);
		 }
	    int count=0;
	    int totalcommit=0;
	    for( String name:names){
		     if(!authors.contains(name)){
			     authors.add(name);
		     }
	    }
	 
	   
	
        
        
        
        for(int i=0;i<filenamesline.size();i++){
        	
        	
        	String [] filename =filenamesline.get(i).split(",");
        		
        	for(int j=0;j<filename.length;j++){
        	
        				filenamesUseless.add(filename[j]);
        			
           }
        	
        }	
       // for(String filename:filenamesUseless){
       // System.out.println(filename);
       // }
        for(int i=0;i<filenamesUseless.size();i++){
        	String [] filename = filenamesUseless.get(i).split("Edited files:");
        	for(int j=0;j<filename.length;j++){
            	
				allfilenames.add(filename[j]);
			
   }
        	
        	
        }
        for(String filename:allfilenames){
		    if(!filenames.contains(filename)){
			 filenames.add(filename);
		    }
	    }
        
        for(String filename:filenames){
        	if(filename.equals("README.md")){
        		filenames.remove(filename);
        	}
            System.out.println(filename);
            }
        
    
        	
     
        		
        	
        
        
       
	
	   
	 
      stage.setTitle("GridPane Experiment");
        
        GridPane gridPane = new GridPane();
           
        for(int i=0;i<=authors.size();i++){
        	
        	for(int j=0;j<=authors.size();j++){
        	Label label =new Label("label"+i+j);
        	 gridPane.add(label, i, j, 1,1);
        	 if(i>0 && j>0){
        	    label.setStyle("-fx-border-color: black; -fx-font-size: 15;");
        	 }
        	 if(i==0 && j>0){
        		 label.setText(authors.get(j-1));
        	 }else if(j==0 && i>0){
        		 label.setText(authors.get(i-1)); 
        	 }
        	 
        
        		 
        	// setStyle("-fx-background-color: green;")
        	 }
        	
        }
             
        gridPane.setAlignment(Pos.CENTER);
        Scene scene = new Scene(gridPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
	public static void main(String[] args) {  
	    launch(args);  
	}

}
