package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map.Entry;

public class PartnerPersons extends Application {
	 private static  ArrayList <String> names = new ArrayList<String>();
	 private static ArrayList <String> authors = new ArrayList<String>();
	 
	 
	 private static ArrayList <String> filenamesline = new ArrayList<String>();
	 private static ArrayList <String> filenamesUseless = new ArrayList<String>();
	 private static ArrayList <String> allfilenames = new ArrayList<String>();
	 private static ArrayList <String> filenames = new ArrayList<String>();
	 private static ArrayList <String> filesinfo = new ArrayList<String>();
	 private static ArrayList <String> fileChangeInfos = new ArrayList<String>();
	 private static ArrayList <String> PairPersonsFile = new ArrayList<String>();
	 private static ArrayList <String> PairPersonsFileTotal = new ArrayList<String>();
	 private static ArrayList <String> PairPersonsFileTotalS = new ArrayList<String>();
     private static HashMap<String,Integer> PartnerPerson =new HashMap<String,Integer>();
     private static HashMap<String,Integer> PartnerPerson2 =new HashMap<String,Integer>();
	 
	 

	@Override
	public void start(Stage stage) throws IOException {
		 File file = new File("final_dump.txt");
		 // Buffered reader
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
		 f.close();
		/* for(String fileinfo:filesinfo){
			
		     System.out.println(fileinfo);
		 } */
		
		 
		 
	
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
        
    /*   int co=0;
       for(String filename:filenames){
       for(String allfilename:allfilenames){
    	   
    	   if(filename.equals(allfilename)){
    		   co++;
    	   }
    	   }
       System.out.println(co+" "+filename);
       co=0;
       } */
        
    
      
        
   	              int count =0;
   	           for(String name:authors){
		        for(String filename:filenames){
		        
					 for(int i=0; i<filesinfo.size();i++){
						 String [] fileinfoarray =filesinfo.get(i).split(" ");
					    
						 for(int j=3;j<fileinfoarray.length;j++){
					        if(filename.equals(" "+fileinfoarray[j]) && name.equals(fileinfoarray[0])){
						       count++;
					        }
					        else if((filename+",").equals(" "+fileinfoarray[j]) && name.equals(fileinfoarray[0])){
					        	count++;
					        }
				         }
					   
			          }
				 
					 // System.out.println(name+" "+filename +" "+ count);
					 if( count !=0 ){
					  fileChangeInfos.add(name+" "+filename+" "+count);
					 }
			          count=0;
		     
			    }
   	          }
    
        	
     
        		
        /*	for(String filechangeinfo:fileChangeInfos){
        		System.out.println(filechangeinfo);
        	}*/
        	
        	int counter =0;
        	for(int i=0;i<fileChangeInfos.size();i++){
        		String [] fileinfoarray = fileChangeInfos.get(i).split(" ");
        		
        		for(int j=i+1;j<fileChangeInfos.size();j++){
            		String [] fileinfoarray2 = fileChangeInfos.get(j).split(" ");
            		
            		if(!fileinfoarray[0].equals(fileinfoarray2[0]) && fileinfoarray[2].equals(fileinfoarray2[2])){
            			
            			counter++;
            			//System.out.println(fileinfoarray[0]+" "+fileinfoarray2[0]+" "+counter+ " " +fileinfoarray[2]);
            			PairPersonsFile.add(fileinfoarray[0]+" "+fileinfoarray2[0]+" "+fileinfoarray[2]+" "+counter);
            		}
            		
            		
        	  }	
        		
        		counter=0;
        		
        	}
        	 int commonFileCount=0;
        	for(int i=0; i<PairPersonsFile.size();i++){
        		String [] pairPerson= PairPersonsFile.get(i).split(" ");
        		for(int j =0;j<PairPersonsFile.size();j++){
        			String [] pairPerson2 =PairPersonsFile.get(j).split(" ");
        			
        			if( pairPerson[0].equals(pairPerson2[0]) && pairPerson[1].equals(pairPerson2[1])){
        				commonFileCount += Integer.valueOf(pairPerson2[3]);
        			
        			}
        		}
        		//System.out.println(pairPerson[0]+" "+pairPerson[1]+" "+commonFileCount);
        		PairPersonsFileTotal.add(pairPerson[0]+" "+pairPerson[1]+" "+commonFileCount);
        		commonFileCount=0;
        		
        	}
        	for(String info :PairPersonsFileTotal){
        		if(!PairPersonsFileTotalS.contains(info)){
        			PairPersonsFileTotalS.add(info);
        		}
        	}
        	
        	for(String info :PairPersonsFileTotalS ){
        		
        		System.out.println(info);
        	} 
        
        	for(int i=0;i<PairPersonsFileTotalS.size();i++){
        	String [] pairPerson= PairPersonsFileTotalS.get(i).split(" ");
        	PartnerPerson.put(pairPerson[0]+","+pairPerson[1],Integer.valueOf(pairPerson[2]));
        	PartnerPerson2.put(pairPerson[1]+","+pairPerson[0],Integer.valueOf(pairPerson[2]));	
        		
        	}
        	
        	for(Entry<String, Integer> pairs : PartnerPerson.entrySet()) {
                System.out.println(pairs);
       }
        	
        
        
       
	
	   
	 
      stage.setTitle("GridPane Experiment");
        
        GridPane gridPane = new GridPane();
    
           
        for(int i=0;i<=authors.size();i++){
        	
        	for(int j=0;j<=authors.size();j++){
        		
        	    Label label =new Label(i+" "+j);
        	    
        	 gridPane.add(label, i, j,1,1);
        	 if(i>0 && j>0){
        	    label.setStyle("-fx-border-color: black; -fx-font-size: 10;");
        	 }
        	 if(i==0 && j>0){
        		 label.setText(authors.get(j-1));
        		 label.getStyleClass().add("matrixCells");
     	         label.setMinSize(20, 20);
     	         
        	 }else if(j==0 && i>0){
        		// label.setText(authors.get(i-1));
        		// label.setMinSize(10, 10);
     	        // label.setRotate(-45);
        		 label.setText("");
        		 VerticalLabel columnNames = new VerticalLabel(VerticalDirection.UP);
                 columnNames.setText(authors.get(i-1));
                 columnNames.setMinHeight(Region.USE_PREF_SIZE);
                 gridPane.add(columnNames, i, 0);
        	 }
        	 else if(i>0 && j>0){
        		String name1= authors.get(i-1);
         		String name2= authors.get(j-1);
         		String finalname = name1+","+name2;
         		
         		label.setText(finalname);
         		  for(int k=0;k<PairPersonsFileTotalS.size();k++){
         			  String[] pairPerson = PairPersonsFileTotalS.get(k).split(" ");
         			 if(finalname.equals(pairPerson[0]+","+pairPerson[1])){
         				int value=PartnerPerson.get(finalname);
         				
         				 if(label.getText().equals(finalname)){
         					 label.setText(""+value);
         					int red = value * 20;
         			        int green = 255 - (value * 10);
         			        
         					label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0); -fx-border-color: black;");
         					label.getStyleClass().add("matrixCells");
         			        label.setMinSize(20, 20);
         				 }
         			 }else if(finalname.equals(pairPerson[1]+","+pairPerson[0])){
         				int value=PartnerPerson2.get(finalname); 
         				if(label.getText().equals(finalname)){
        					 label.setText(""+value);
        					int red = value * 20;
        			        int green = 255 - (value * 10);
        			        
        					label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0); -fx-border-color: black;");
        					label.getStyleClass().add("matrixCells");
        			        label.setMinSize(20, 20);
        				 }
         			 }
         		  }
         		  
         		  if(label.getText().equals(finalname)){
         			 label.setText(""+0);
         			 label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
         			 // label.getStyleClass().add("matrixCells");
         	         label.setMinSize(20, 20);
         		  } 
         		   
         		
         		
         		
        	 } 
        	 
        	 
        	 
        	
        	
        	 }
        	
        }
       
             
        gridPane.setAlignment(Pos.CENTER);
       
        stage.setTitle(" Partner Person Matrix ");
        Scene scene = new Scene(gridPane, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
	public static void main(String[] args) {  
	    launch(args);  
	}

}

