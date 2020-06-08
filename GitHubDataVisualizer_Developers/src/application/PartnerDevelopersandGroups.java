package application;

import static java.util.stream.Collectors.toMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import com.sun.prism.paint.Color;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.geometry.VerticalDirection;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class PartnerDevelopers extends Application {
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
     private static ArrayList <String> DevelopersNames = new ArrayList<String>();
     private static ArrayList <String> Developers = new ArrayList<String>();
 	 
     
     private static ArrayList <String> clusterList = new ArrayList<String>();
	 private static ArrayList <String> clusterFilelList = new ArrayList<String>();
	 private static ArrayList <String> developersFilesCount = new ArrayList<String>();
	 private static ArrayList <String> FileandDevelopers = new ArrayList<String>();
	 private static ArrayList <String> FileandDevelopers2 = new ArrayList<String>();
	 private static ArrayList <String> Files = new ArrayList<String>();
	 ArrayList<String> fileNames = new ArrayList<>();
	 ArrayList<String> authorNames = new ArrayList<>();
	 ArrayList<Integer> editCount = new ArrayList<>();
	 private static ArrayList <String> FilesMaxChange = new ArrayList<String>();
	 private static ArrayList <String> SubDevelopers = new ArrayList<String>();
	 private static ArrayList <String> clusterControl = new ArrayList<String>();
			
	

	@Override
	public void  start(Stage stage) throws IOException {
		 
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
		 f.close();
		/* for(String fileinfo:filesinfo){
			
		     System.out.println(fileinfo);
		 } */
		
		 
		 
	//authors's name's list
	    for( String name:names){
		     if(!authors.contains(name)){
			     authors.add(name);
			    
		     }
	    }
	  
	  
	  
        // this part split file names line but include "edited file"
        for(int i=0;i<filenamesline.size();i++){
        	String [] filename =filenamesline.get(i).split(",");
        		
        	for(int j=0;j<filename.length;j++){
        	    filenamesUseless.add(filename[j]);
        			
           }
        	
        }	
      //control part
       // for(String filename:filenamesUseless){
       // System.out.println(filename);
       // }
        
        
        //this part include all file names 
        for(int i=0;i<filenamesUseless.size();i++){
        	String [] filename = filenamesUseless.get(i).split("Edited files:");
        	for(int j=0;j<filename.length;j++){
            	
				allfilenames.add(filename[j]);
			
   }
        	
        	
        }
       // for each filename used only once
        for(String filename:allfilenames){
		    if(!filenames.contains(filename)){
			 filenames.add(filename);
			 
		    }
	    }
        
  
    
      
        //this part create filechangeinfos that includes author name, file name and count of edits of this file
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
					    //System.out.println(name+" "+filename +" "+ count);
					    
					    //for exclude those whose file edit count are zero
					    if( count !=0 ){
					     fileChangeInfos.add(name+" "+filename+" "+count);
					     clusterList.add(name+""+filename+" "+count);
					    }
			            count=0;
		     
			      }
   	           }
    
        	
     
   	      //control part
        /*	for(String filechangeinfo:fileChangeInfos){
        		System.out.println(filechangeinfo);
        	} 
   	           */
   	           
        	//this part create pair developer list, count number is minumum changed 
        	int counter =0;
        	for(int i=0;i<fileChangeInfos.size();i++){
        		String [] fileinfoarray = fileChangeInfos.get(i).split(" ");
        		
        		for(int j=i+1;j<fileChangeInfos.size();j++){
            		String [] fileinfoarray2 = fileChangeInfos.get(j).split(" ");
            		
            		if(!fileinfoarray[0].equals(fileinfoarray2[0]) && fileinfoarray[2].equals(fileinfoarray2[2])){
            			
            			if(Integer.valueOf(fileinfoarray[3])<Integer.valueOf(fileinfoarray2[3])){
            				counter=Integer.valueOf(fileinfoarray[3]);
            			}else{
            				counter=Integer.valueOf(fileinfoarray2[3]);
            			}
            			//control part
            		
            			//System.out.println(fileinfoarray[0]+" "+fileinfoarray2[0]+" "+counter+ " " +fileinfoarray[2]);
            			PairPersonsFile.add(fileinfoarray[0]+" "+fileinfoarray2[0]+" "+fileinfoarray[2]+" "+counter);
            		}
            		
            		
            		
            		
            		
        	  }	
        		
        		counter=0;
        		
        	}
        	//total of all changes files add in PairPersonFileTotal list
        	 int commonFileCount=0;
        	 for(int i=0; i<PairPersonsFile.size();i++){
        		String [] pairPerson= PairPersonsFile.get(i).split(" ");
        		for(int j =0;j<PairPersonsFile.size();j++){
        			String [] pairPerson2 =PairPersonsFile.get(j).split(" ");
        			
        			if( pairPerson[0].equals(pairPerson2[0]) && pairPerson[1].equals(pairPerson2[1])){
        				commonFileCount += Integer.valueOf(pairPerson2[3]);
        			
        			}
        		}
        	//	System.out.println(pairPerson[0]+" "+pairPerson[1]);

        	//	System.out.println(pairPerson[0]+" "+pairPerson[1]+" "+commonFileCount);
        		PairPersonsFileTotal.add(pairPerson[0]+" "+pairPerson[1]+" "+commonFileCount);
        		commonFileCount=0;
        		
        	}
        	 // for each information to be found only once 
        	for(String info :PairPersonsFileTotal){
        		if(!PairPersonsFileTotalS.contains(info)){
        			PairPersonsFileTotalS.add(info);
        		}
        	}
        	
        	//control part
        	for(String info :PairPersonsFileTotalS ){
        		
        		System.out.println(info);
        	} 
        
        	//add in hashmap 
        	for(int i=0;i<PairPersonsFileTotalS.size();i++){
        	    String [] pairPerson= PairPersonsFileTotalS.get(i).split(" ");
        	    PartnerPerson.put(pairPerson[0]+","+pairPerson[1],Integer.valueOf(pairPerson[2]));
        	    PartnerPerson2.put(pairPerson[1]+","+pairPerson[0],Integer.valueOf(pairPerson[2]));	
        		
        	}
        	
        	//controls part
       /* 	for(Entry<String, Integer> pairs : PartnerPerson.entrySet()) {
                System.out.println(pairs);
       } */
        	
        	//hasmap sort        	
        	 Map<String, Integer> sorted = PartnerPerson
             		.entrySet()
                     .stream()
                     .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                     .collect(
                         toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                             LinkedHashMap::new));
              
                 System.out.println("map after sorting by values in descending order: "
                     + sorted);
            
                 for(Entry<String, Integer> pairs : sorted.entrySet()) {
                		String [] DevelopersName =  pairs.getKey().split(",");
                	
                        System.out.println(pairs);
                        
                        DevelopersNames.add(DevelopersName[0]);
                        DevelopersNames.add(DevelopersName[1]);
                        
               }
                 //simplifying developer names for appearance when sorted
                	for(String developer:DevelopersNames){
         		    if(!Developers.contains(developer)){
         			 Developers.add(developer);
         		    }
         	    }
            
           
        
        //CLUSTER PART
                	for(int i=0;i<clusterList.size();i++){
        	        	String filename =clusterList.get(i).replace('/',' ');
        	        	developersFilesCount.add(filename);
        	        //	System.out.println(filename);
        	 } 
                /*	//control part
                	for(String info:developersFilesCount){
                		 	System.out.println(info);
                	} */
                	
                	for(int i=0;i<developersFilesCount.size();i++){
                		String [] info =developersFilesCount.get(i).split(" ");
                		if(!SubDevelopers.contains(info[0]+" "+info[1])){
                			SubDevelopers.add(info[0]+" "+info[1]);
                		}
                	}
                /*	//control part
                	for(String info:SubDevelopers){
                		 	System.out.println(info);
                	}*/
        		
        		 for(int i=0;i<developersFilesCount.size();i++){
        	        	String [] filename =developersFilesCount.get(i).split(" ");
        	        	
        	        	int count2=Integer.valueOf(filename[filename.length-1]);       	
        	        	for(int j=i+1;j<developersFilesCount.size();j++){
        	        		String [] filename2 =developersFilesCount.get(j).split(" ");
        	        		
        	        		if(filename[0].equals(filename2[0]) && filename[1].equals(filename2[1])){
        	        			
        	        			count2 += Integer.valueOf(filename2[filename2.length-1]);
        	        			
        	        		}
        	        	}
        	        		
        	        	clusterFilelList.add(filename[0]+" "+filename[1]+" "+count2);
            		//	 System.out.println(filename[0]+" "+filename[1]+" "+count2);
        	        	
        	        } 
        		 for(int i=0;i<clusterFilelList.size();i++){
        			 String [] filedelete= clusterFilelList.get(i).split(" ");
        			 if(!filedelete[1].equals("README.md")){
        				 FileandDevelopers2.add(filedelete[0]+" "+filedelete[1]+" "+filedelete[2]);
        			 }
        		 }
        		 
			 
        	/*	 for(String filess:FileandDevelopers){
        			 
        			 System.out.println(filess);
        	        	
        		 } */
        		 
        		 // tekkiþi tarafýndan deðiþtirilmiþ kýsýmlarý  silme 
        		
        		 System.out.println("-----------------------------");
 	        	
        		 int x = 0;
        		 for(int i=0;i<FileandDevelopers2.size();i++){
        			 String [] filedelete= FileandDevelopers2.get(i).split(" ");
        			 for(int j=0;j<FileandDevelopers2.size();j++){
        				 String [] filedelete2= FileandDevelopers2.get(j).split(" ");
        				 if(i != j){
        				   if(filedelete[1].equals(filedelete2[1])){
        					 x++;
        				   }
        				 }
        			 }
        			 if(x != 0){
        				 FileandDevelopers.add(FileandDevelopers2.get(i));
        			 }
        			 x=0;
        		 }
        		 
        		 
        		 
        		 /*    for(String filess:FileandDevelopers){
        			 
        			 System.out.println(filess);
        	        	
        		 } */
        		
        		 for(int i=0;i<FileandDevelopers.size();i++){
        			 String[] split = FileandDevelopers.get(i).split(" ");
        			 if (!authorNames.contains(split[0])) {
        					authorNames.add(split[0]);
        					fileNames.add(split[1]);
        					editCount.add(Integer.parseInt(split[2]));
        				} else {
        					int currentIndex = authorNames.lastIndexOf(split[0]);
        					if (editCount.get(currentIndex) < Integer.parseInt(split[2])) {
        						
        						authorNames.set(currentIndex, split[0]);
        						fileNames.set(currentIndex, split[1]);
        						editCount.set(currentIndex, Integer.parseInt(split[2]));
        					
        						
        					}
        				}
        			}
        			for (int i = 0; i < fileNames.size(); i++) {
        			//	System.out.println(authorNames.get(i)+" " +fileNames.get(i) + " " + editCount.get(i));
        				FilesMaxChange.add(authorNames.get(i)+" " +fileNames.get(i) + " " + editCount.get(i));
        			}
        			
        			for(int i=0;i<FilesMaxChange.size();i++){
        				String[] split = FilesMaxChange.get(i).split(" ");
        				if(!Files.contains(split[1])){
        					Files.add(split[1]);
        				}
        			}
        			
       
	
	   //visualation part
	 
      stage.setTitle("GridPane Experiment and Cluster");
      Group group = new Group();
      Label label1 = new Label();
      label1.setFont(new Font("Arial", 24));
      
      ListView listView = new ListView();

        
        
        GridPane gridPane = new GridPane();
    
          //first horizontal and vertical label add developers name  
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
        		
        		 label.setText("");
        		 VerticalLabel columnNames = new VerticalLabel(VerticalDirection.UP);
                 columnNames.setText(authors.get(i-1));
                 columnNames.setMinHeight(Region.USE_PREF_SIZE);
                 gridPane.add(columnNames, i, 0);
        	 }
        	 else if(i>0 && j>0){    // place common file change part
        		String name1= authors.get(i-1);
         		String name2= authors.get(j-1);
         		String finalname = name1+","+name2;
         		
         		label.setText(finalname);
         		  for(int k=0;k<PairPersonsFileTotalS.size();k++){
         			  String[] pairPerson = PairPersonsFileTotalS.get(k).split(" ");
         			 if(finalname.equals(pairPerson[0]+","+pairPerson[1])){
         				int value=PartnerPerson.get(finalname);
         				
         				 if(label.getText().equals(finalname)){  //colored part
         					 label.setText(""+value);
         					int red = value * 20;
         			        int green = 255 - (value * 10);
         			        
         					label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0); -fx-border-color: black;");
         					label.getStyleClass().add("matrixCells");
         			        label.setMinSize(20, 20);
         			        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
            			         @Override
            			           public void handle(MouseEvent e) {  // click in number list visualation 
            			        	 listView.getItems().clear();
            			        	
            			        	 for (int i=0; i<PairPersonsFile.size(); i++){
            			        	 String[] filenames =PairPersonsFile.get(i).split(" ");
            			        	
            			        	 if(finalname.equals(filenames[0]+","+filenames[1])){
            			        		 listView.getItems().add(filenames[2]+" + "+filenames[3]);
            			        	 }
            			        	 
            			        	 }
            			        	 
            			            
            			       
            			           }
            			        });
      				 
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
        			        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
           			         @Override
           			           public void handle(MouseEvent e) {
           			        	 listView.getItems().clear();
           			        	 
           			        	 for (int i=0; i<PairPersonsFile.size(); i++){
           			        	 String[] filenames =PairPersonsFile.get(i).split(" ");
           			        	 if(finalname.equals(filenames[1]+","+filenames[0])){
           			        		 listView.getItems().add(filenames[2]+" + "+filenames[3]);
           			        	 }
           			        	 }
           			        	 
           			            
           			       
           			           }
           			        });
        				 }
         			 }
         		  }
         		  
         		  if(label.getText().equals(finalname)){
         			 label.setText(""+0);
         			 label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
         			 // label.getStyleClass().add("matrixCells");
         	         label.setMinSize(20, 20);
         		  } 
         		   
         		
         		
         		
        	 }  else if(i==0 && j==0){
        		 Button btn = new Button();
      	        btn.setText(" SORT ");
      	        btn.setOnAction(new EventHandler<ActionEvent>() { //click soert button
      	            @Override
      	            public void handle(ActionEvent event) {
      	              Group group = new Group();
      	            	
      	            	 GridPane gridPane = new GridPane();
      	            	
      	                
      	                for(int i=0;i<=Developers.size();i++){
      	                	
      	                	for(int j=0;j<=Developers.size();j++){
      	                		
      	                	    Label label =new Label(i+" "+j);
      	                	    
      	                	 gridPane.add(label, i, j,1,1);
      	                	 if(i>0 && j>0){
      	                	    label.setStyle("-fx-border-color: black; -fx-font-size: 10;");
      	                	 }
      	                	 if(i==0 && j>0){
      	                		 label.setText(Developers.get(j-1));
      	                		 label.getStyleClass().add("matrixCells");
      	             	         label.setMinSize(20, 20);
      	             	         
      	             	         
      	                	 }else if(j==0 && i>0){
      	                		// label.setText(authors.get(i-1));
      	                		// label.setMinSize(10, 10);
      	             	        // label.setRotate(-45);
      	                		 label.setText("    ");
      	                		 VerticalLabel columnNames = new VerticalLabel(VerticalDirection.UP);
      	                         columnNames.setText(Developers.get(i-1));
      	                         columnNames.setMinHeight(Region.USE_PREF_SIZE);
      	                         gridPane.add(columnNames, i, 0);
      	                	 }
      	                	 else if(i>0 && j>0){
      	                		String name1= Developers.get(i-1);
      	                 		String name2= Developers.get(j-1);
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
      	                 			        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
      	              			         @Override
      	              			           public void handle(MouseEvent e) {
      	              			        	 listView.getItems().clear();
      	              			        	 
      	              			        	 for (int i=0; i<PairPersonsFile.size(); i++){
      	              			        	 String[] filenames =PairPersonsFile.get(i).split(" ");
      	              			        	 
      	              			        	 if(finalname.equals(filenames[0]+","+filenames[1])){
      	              			        		 listView.getItems().add(filenames[2]+" + "+filenames[3]);
      	              			        	 }
      	              			        	 }
      	              			        	 
      	              			            
      	              			       
      	              			           }
      	              			          });
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
      	                			        label.setOnMouseClicked(new EventHandler<MouseEvent>() {
      	               			          @Override
      	               			           public void handle(MouseEvent e) {
      	               			        	 listView.getItems().clear();
      	               			        	 
      	               			        	 for (int i=0; i<PairPersonsFile.size(); i++){
      	               			        	 String[] filenames =PairPersonsFile.get(i).split(" ");
      	               			        	 if(finalname.equals(filenames[1]+","+filenames[0])){
      	               			        		 listView.getItems().add(filenames[2]+" + "+filenames[3]);
      	               			        	 }
      	               			        	 }
      	               			        	 
      	               			            
      	               			       
      	               			           }
      	               			        });
      	                				 }
      	                 			 }
      	                 		  }
      	                 		  
      	                 		  if(label.getText().equals(finalname)){
      	                 			 label.setText(""+0);
      	                 			 label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
      	                 			 // label.getStyleClass().add("matrixCells");
      	                 	         label.setMinSize(20, 20);
      	                 		  } 
      	                 		   
      	                 		
      	                 		
      	                 		
      	                	 } else if( i==0 && j==0){
      	                		 label.setText("SORTED");
      	                		 label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
      	                		 
      	                	 }
      	                	 
      	                	       	                	       	    	      	                	      	                	       	                	      	                        	         	         	                	
        	 }
        	
        }
      	                
      	            Button clusterbtn = new Button();
      		        clusterbtn.setText(" GROUPS ");
      		        clusterbtn.setOnAction(new EventHandler<ActionEvent>() { 
      		            @Override
      		            public void handle(ActionEvent event) {
      		             ArrayList<String> controlNames = new ArrayList<>();
      		              for(int i=0;i<Files.size();i++){
      		       	    	    Label label = new Label();
      		       		        label.setFont(new Font("Arial", 9));
      		       		        
      		       		        label.setText(Files.get(i)+" Group ");
      		       		        label.setLayoutX((i*100)+10);
      		       		        label.setLayoutY(485);
      		       		        
      		       		        group.getChildren().add(label);
      		       		        
      		       		        Label mainDevelopers = new Label();
      		       		        mainDevelopers.setFont(new Font("Arial", 9));
      		       		        mainDevelopers.setText("Main Developers: ");
      		       		        mainDevelopers.setLayoutX((i*100)+10);
      		       		        mainDevelopers.setLayoutY(500);
      		       		        group.getChildren().add(mainDevelopers);
      		       		        
      		       		        Label subDevelopers = new Label();
      		       		        subDevelopers.setFont(new Font("Arial", 9));
      		       		        subDevelopers.setText("Sub Developers: ");
      		       		        subDevelopers.setLayoutX((i*100)+10);
      		       		        subDevelopers.setLayoutY(590);
      		       		        group.getChildren().add(subDevelopers);
      		       		     
      		       		        int value=0;
      		       		        
      		       	    	 
      		       	    	   ListView listView2 = new ListView();
      		       	    	   listView2.setPrefWidth(75);
      		       	    	   listView2.setPrefHeight(75);
      		       	    	   listView2.setLayoutX(((i)*100)+10);
      		       	           listView2.setLayoutY(510);
      		       	           group.getChildren().add(listView2);
      		       	          
      		       	           ListView listView3 = new ListView();
      		       	           listView3.setPrefWidth(75);
      	      	    	       listView3.setPrefHeight(75);
      	      	    	       listView3.setLayoutX(((i)*100)+10);
      	      	               listView3.setLayoutY(600);
      	      	               group.getChildren().add(listView3);
      		       	    	
      		       	           for(int j=0;j<FilesMaxChange.size();j++){
      		       	    		 
      		       	    		  String[] filenames =FilesMaxChange.get(j).split(" ");
      		       	    		  if(Files.get(i).equals(filenames[1])){
      		       	    			  listView2.getItems().add(filenames[0]);  
      		       	    			  value++;
      		       	    			  controlNames.add(filenames[0]);
      		       	    		  }
      		       	    		 
      		       	    		
      		       	          }
      		       	          for(int j=0;j<SubDevelopers.size();j++){
      		       	    		 
      		       	    		  String[] filenames =SubDevelopers.get(j).split(" ");
      		       	    		  if(Files.get(i).equals(filenames[1]) && !controlNames.contains(filenames[0])){
      		       	    			  listView3.getItems().add(filenames[0]);  
      		       	    			  value++;
      		       	    			  
      		       	    	      }
      		       	    		  
      		       	    		int red = value * 50;
      	   			            int green = 255 - (value * 30);
      	   			         
      	   					    label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0); -fx-border-color: black;");
      		       	          } 
      		       	          controlNames.clear();
      		       	     
      		            	  
      		             }
      		       	      
      		       	    
      		       	        	 
      		       	        	 
      		       	        	 
      		       	        	
      		              
      		            }
      		        
      		        });
      		
      		        clusterbtn.setStyle("-fx-background-color: red ; -fx-border-color: black;");
      		        clusterbtn.setLayoutX(10);
      		        clusterbtn.setLayoutY(450);
      	          
      	           
      	        
      	                
      	                
      	            gridPane.setAlignment(Pos.CENTER);
      	            gridPane.setLayoutX(10);
      	            gridPane.setLayoutY(10);
      	            
      	           
      	            Label label = new Label();
      	            label.setFont(new Font("Arial", 16.5));
      	            label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
      	            label.setText("File Name - Count of Interchange");
      	            label.setLayoutX(500);
      	            label.setLayoutY(24);
      	           
      	           
      	            listView.setLayoutX(500);
      	            listView.setLayoutY(50);
      	            //VBox vBox = new VBox(listView, gridPane);
      	            
      	          
      	            group.getChildren().add(gridPane);
      	            group.getChildren().add(label);
      	            group.getChildren().add(listView);
      	            group.getChildren().add(clusterbtn);
      	        
      	           
      	            stage.setTitle(" Partner Developers Matrix and Groups");
      	            Scene scene = new Scene(group, 800, 700);
      	            stage.setScene(scene);
      	            stage.show();
     	            }
     	        });
      	        btn.setStyle("-fx-background-color: red ; -fx-border-color: black;");
     	        gridPane.add(btn, i, j,1,1);
     	        
     	 }
           	 
           	 
           	 
           	
           	
           	 }
           	
           }
       
        //CLUSTER PART
        Button clusterbtn = new Button();
	        clusterbtn.setText(" GROUPS ");
	        clusterbtn.setOnAction(new EventHandler<ActionEvent>() { 
	            @Override
	            public void handle(ActionEvent event) {
	             ArrayList<String> controlNames = new ArrayList<>();
	              for(int i=0;i<Files.size();i++){
	       	    	    Label label = new Label();
	       		        label.setFont(new Font("Arial", 9));
	       		        
	       		        label.setText(Files.get(i)+" Group ");
	       		        label.setLayoutX((i*100)+10);
	       		        label.setLayoutY(485);
	       		        
	       		        group.getChildren().add(label);
	       		        
	       		        Label mainDevelopers = new Label();
	       		        mainDevelopers.setFont(new Font("Arial", 9));
	       		        mainDevelopers.setText("Main Developers: ");
	       		        mainDevelopers.setLayoutX((i*100)+10);
	       		        mainDevelopers.setLayoutY(500);
	       		        group.getChildren().add(mainDevelopers);
	       		        
	       		        Label subDevelopers = new Label();
	       		        subDevelopers.setFont(new Font("Arial", 9));
	       		        subDevelopers.setText("Sub Developers: ");
	       		        subDevelopers.setLayoutX((i*100)+10);
	       		        subDevelopers.setLayoutY(590);
	       		        group.getChildren().add(subDevelopers);
	       		     
	       		        int value=0;
	       		        
	       	    	 
	       	    	   ListView listView2 = new ListView();
	       	    	   listView2.setPrefWidth(75);
	       	    	   listView2.setPrefHeight(75);
	       	    	   listView2.setLayoutX(((i)*100)+10);
	       	           listView2.setLayoutY(510);
	       	           group.getChildren().add(listView2);
	       	          
	       	           ListView listView3 = new ListView();
	       	           listView3.setPrefWidth(75);
      	    	       listView3.setPrefHeight(75);
      	    	       listView3.setLayoutX(((i)*100)+10);
      	               listView3.setLayoutY(600);
      	               group.getChildren().add(listView3);
	       	    	
	       	           for(int j=0;j<FilesMaxChange.size();j++){
	       	    		 
	       	    		  String[] filenames =FilesMaxChange.get(j).split(" ");
	       	    		  if(Files.get(i).equals(filenames[1])){
	       	    			  listView2.getItems().add(filenames[0]);  
	       	    			  value++;
	       	    			  controlNames.add(filenames[0]);
	       	    		  }
	       	    		 
	       	    		
	       	          }
	       	          for(int j=0;j<SubDevelopers.size();j++){
	       	    		 
	       	    		  String[] filenames =SubDevelopers.get(j).split(" ");
	       	    		  if(Files.get(i).equals(filenames[1]) && !controlNames.contains(filenames[0])){
	       	    			  listView3.getItems().add(filenames[0]);  
	       	    			  value++;
	       	    			  
	       	    	      }
	       	    		  
	       	    		int red = value * 50;
   			            int green = 255 - (value * 30);
   			         
   					    label.setStyle("-fx-background-color: rgb(" + red + "," + green + ",0); -fx-border-color: black;");
	       	          } 
	       	          controlNames.clear();
	       	     
	            	  
	             }
	       	      
	       	    
	       	        	 
	       	        	 
	       	        	 
	       	        	
	              
	            }
	        
	        });
	
	        clusterbtn.setStyle("-fx-background-color: red ; -fx-border-color: black;");
	        clusterbtn.setLayoutX(10);
	        clusterbtn.setLayoutY(450);
          
           
        
        
           gridPane.setAlignment(Pos.CENTER);
           gridPane.setLayoutX(10);
           gridPane.setLayoutY(10);
        
       
           Label label = new Label();
           label.setFont(new Font("Arial", 16.5));
           label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
           label.setText("File Name - Count of Interchange");
           label.setLayoutX(500);
           label.setLayoutY(24);
       
       
          listView.setLayoutX(500);
          listView.setLayoutY(50);
          //VBox vBox = new VBox(listView, gridPane);
      
       
        
          group.getChildren().add(gridPane);
          group.getChildren().add(label);
          group.getChildren().add(listView);
          group.getChildren().add(clusterbtn);
        
       
          stage.setTitle(" Partner Developers Matrix and Groups ");
          Scene scene = new Scene(group, 800, 700);
          stage.setScene(scene);
          stage.show();
    }
	public static void main(String[] args) {  
	    launch(args);  
	}

}
