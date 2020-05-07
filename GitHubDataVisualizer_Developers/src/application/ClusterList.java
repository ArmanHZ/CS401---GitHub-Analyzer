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

public class ClusterList extends Application {
	
	 private static ArrayList <String> clusterList = new ArrayList<String>();
	 private static ArrayList <String> clusterFilelList = new ArrayList<String>();
	 private static ArrayList <String> developersFilesCount = new ArrayList<String>();
	 private static ArrayList <String> FileandDevelopers = new ArrayList<String>();
	 private static ArrayList <String> Files = new ArrayList<String>();
	 ArrayList<String> fileNames = new ArrayList<>();
	 ArrayList<String> authorNames = new ArrayList<>();
	 ArrayList<Integer> editCount = new ArrayList<>();
	 private static ArrayList <String> FilesMaxChange = new ArrayList<String>();
	 private static ArrayList <String> FilesMaxChange2 = new ArrayList<String>();
	 private static ArrayList <String> clusterFilelList2 = new ArrayList<String>();
	 ArrayList<String> fileNames2 = new ArrayList<>();
	 ArrayList<String> authorNames2 = new ArrayList<>();
	 ArrayList<Integer> editCount2 = new ArrayList<>();
	 private static ArrayList <String> Files2 = new ArrayList<String>();



	public void  start(Stage stage) throws IOException {
		File file = new File("Cluster.txt");
		Scanner f = new Scanner(file);
		 while(f.hasNextLine()) {
			
			clusterList.add(f.nextLine());	
		   
				
	    }
		 f.close();
		 for(String fileinfo:clusterList){
			
	  //   System.out.println(fileinfo);
		 }
		 
	    for(int i=0;i<clusterList.size();i++){
	        	String filename =clusterList.get(i).replace('/',' ');
	        	developersFilesCount.add(filename);
	        //	System.out.println(filename);
	 } 
		
		 for(int i=0;i<developersFilesCount.size();i++){
	        	String [] filename =developersFilesCount.get(i).split(" ");
	        	
	        	int count=Integer.valueOf(filename[filename.length-1]);       	
	        	for(int j=i+1;j<developersFilesCount.size();j++){
	        		String [] filename2 =developersFilesCount.get(j).split(" ");
	        		
	        		if(filename[0].equals(filename2[0]) && filename[1].equals(filename2[1])){
	        			
	        			count += Integer.valueOf(filename2[filename2.length-1]);
	        			
	        		}
	        	}
	        		
	        	clusterFilelList.add(filename[0]+" "+filename[1]+" "+count);
    			 System.out.println(filename[0]+" "+filename[1]+" "+count);
	        	
	        } 
		 for(int i=0;i<clusterFilelList.size();i++){
			 String [] filedelete= clusterFilelList.get(i).split(" ");
			 if(!filedelete[1].equals("README.md")){
				 FileandDevelopers.add(filedelete[0]+" "+filedelete[1]+" "+filedelete[2]);
			 }
		 }
		 for(String filess:FileandDevelopers){
			 
		//	 System.out.println(filess);
	        	
		 }
		 
		 
		
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
			
			
			for(String info: FileandDevelopers){
				clusterFilelList2.add(info);
			}
	
			for(int i=0;i<FilesMaxChange.size();i++){
				int num=0;
				String[] split = FilesMaxChange.get(i).split(" ");
				for(int j=i+1;j<FilesMaxChange.size();j++){
					String[] split2 = FilesMaxChange.get(j).split(" ");
					if(split[1].equals(split2[1])){
						num++;
					}
				}
				if(num==0){
					for(int j=0;j<clusterFilelList2.size();j++){
						String[] split2 = clusterFilelList2.get(j).split(" ");
						if(split[1].equals(split2[1])){
							clusterFilelList2.remove(j);
						}
					}
					
				}
				
			}
			
			 for(int i=0;i<clusterFilelList2.size();i++){
				 String[] split = clusterFilelList2.get(i).split(" ");
				 if (!authorNames2.contains(split[0])) {
						authorNames2.add(split[0]);
						fileNames2.add(split[1]);
						editCount2.add(Integer.parseInt(split[2]));
					} else {
						int currentIndex = authorNames2.lastIndexOf(split[0]);
						if (editCount2.get(currentIndex) < Integer.parseInt(split[2])) {
							authorNames2.set(currentIndex, split[0]);
							fileNames2.set(currentIndex, split[1]);
							editCount2.set(currentIndex, Integer.parseInt(split[2]));
						}
					}
				}
				for (int i = 0; i < fileNames2.size(); i++) {
				//	System.out.println(authorNames.get(i)+" " +fileNames.get(i) + " " + editCount.get(i));
					FilesMaxChange2.add(authorNames2.get(i)+" " +fileNames2.get(i) + " " + editCount2.get(i));
				}
				
				for(int i=0;i<FilesMaxChange2.size();i++){
					String[] split = FilesMaxChange2.get(i).split(" ");
					if(!Files2.contains(split[1])){
						Files2.add(split[1]);
					}
				}

		/*	for(String info: clusterFilelList2){
				System.out.println(info);
			}*/
	/*	int counter =0;
		 for(int i=0; i<clusterFilelList.size();i++){
			 String[] info = clusterFilelList.get(i).split(" ");
			  counter=Integer.valueOf(info[2]);
			 for(int j=0;j<clusterFilelList.size();j++){
				String[] info2 = clusterFilelList.get(j).split(" ");
				
				if(info[0].equals(info2[0]) && info[1].equals(info2[1])){
					if(counter >= Integer.valueOf(info2[2])){
						counter= Integer.valueOf(info[2]);
					}else{
						counter= Integer.valueOf(info2[2]);;
					}
				}
			 }
			 System.out.println(info[0]+" "+info[1]+" "+counter);
			
		 }  */
		 
		 
		 
		  stage.setTitle("Cluster Experiment");
	      Label label1 = new Label();
	      label1.setFont(new Font("Arial", 24));
	      Group group = new Group();
	      
	      for(int i=0;i<Files2.size();i++){
	    	   Label label = new Label();
		        label.setFont(new Font("Arial", 11));
		        label.setStyle("-fx-background-color: #ADFF2F; -fx-border-color: black;");
		        label.setText(Files2.get(i)+" group ");
		        label.setLayoutX(i*100);
		        label.setLayoutY(24);
		        
		        group.getChildren().add(label);
	    	 
	    	  ListView listView = new ListView();
	    	  listView.setPrefWidth(75);
	    	  listView.setPrefHeight(150);
	    	  listView.setLayoutX((i)*100);
   	          listView.setLayoutY(50);
   	          group.getChildren().add(listView);
	    	
   	          for(int j=0;j<FilesMaxChange2.size();j++){
	    		 
	    		  String[] filenames =FilesMaxChange2.get(j).split(" ");
	    		  if(Files2.get(i).equals(filenames[1])){
	    			  listView.getItems().add(filenames[0]);  
	    		  }
	    		
	    		
		        	
	    		 
	    		  
	      }
	      }
	      
	    
	        	 
	        	 
	        	 
	        	
	        		
	       
	      
	       
	       
	        stage.setTitle(" Partner Developers Matrix ");
	        Scene scene = new Scene(group, 800, 600);
	        stage.setScene(scene);
	        stage.show();

		 
		
		
	}
	
	public static void main(String[] args) {  
	    launch(args);  
	}
}
