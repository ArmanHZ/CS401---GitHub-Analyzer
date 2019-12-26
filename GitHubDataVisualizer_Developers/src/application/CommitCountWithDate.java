package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CommitCountWithDate extends Application {
	 private static  ArrayList <String> names = new ArrayList<String>();
	 private static ArrayList <String> authors = new ArrayList<String>();
	 private static  ArrayList <Integer> commitCount = new ArrayList<Integer>();
	 private static ArrayList <String> allDates= new ArrayList<String>();
	 private static ArrayList <String> dates =new ArrayList<String>();
	 private static ArrayList <String> allinfo =new ArrayList<String>();
	 
	 

	@Override
	public void start(Stage stage) throws IOException {
		 File file = new File("final_dump.txt");
		 Scanner f = new Scanner(file);
		 while(f.hasNextLine()) {
				String commitHash = f.nextLine();
				String Author = f.next();
				String  a = f.next();
				String name = f.next();
				names.add(name);
			    String nameline = f.nextLine();
			   
				String x= f.nextLine();
				String dateline01 = f.next();
				String dateline02 = f.next();
				String dateline03 = f.next();
				String month = f.next();
				String day = f.next();
				String date = name+" "+month +" "+day;
				allDates.add(date);
				String y= f.nextLine();
				String z= f.nextLine();
				String v= f.nextLine();
				String u= f.nextLine();
				
		     //	System.out.println(name);
			//	System.out.println(date);
	    }
	    int count=0;
	    int totalcommit=0;
	    for( String name:names){
		     if(!authors.contains(name)){
			     authors.add(name);
		     }
	    }
	 
	    for(String date:allDates){
		    if(!dates.contains(date)){
			 dates.add(date);
		    }
	    }
	    int max=0;
        for(String date:dates){
		    for(String alldate: allDates){
			    if(date.equals(alldate)){
				count++;
				totalcommit++;
			    }
		   }
		    
		    allinfo.add(date+" "+count);
		    commitCount.add(count);
		    //System.out.println("Occurrence of " + date + " is " + count + " times.");
		   
		    if(count>max){
		    	max=count;
		    }
            count=0;
       }
     /*   for (String all:allinfo){
        	System.out.println(all);
        } */
        
	 
	     String [][] info  = new String[dates.size()][4];
	     String [] x = new String[2];
	
	     for(int i=0;i<allinfo.size();i++){
		     for(int j=0;j<4;j++){
		 
			     x=allinfo.get(i).split(" ");
			     if(j==0){
				 
			     info[i][j]=x[0];
			    //System.out.println(info[i][j]);
			     } else if(j==1){
				 
			     info[i][j]=x[1];
			     // System.out.println(info[i][j]);
				 } else if(j==2){
				
				 info[i][j]=x[2];
				 // System.out.println(info[i][j]);
				 }else if(j==3){
				
				 info[i][j]=x[3];
				 // System.out.println(info[i][j]);
				 }
		    }
	   }
	 
	    for(int i=0;i<info.length;i++){
		    for(int j=0;j<info[0].length;j++){
			    System.out.print(info[i][j]);
		    }
		        System.out.println(" ");
	   } 
	    ListView listView = new ListView();
	    
	    for(String author:authors){
	    	 listView.getItems().add(author);
	    }
	    CategoryAxis xaxis = new CategoryAxis();  
	    NumberAxis yaxis = new NumberAxis(0,max*2,1);  
	    xaxis.setLabel("Date");  
	    yaxis.setLabel("Commit Count");  
	    //Creating StackedAreaChart   
	    StackedAreaChart stack = new StackedAreaChart(xaxis,yaxis);  
	    stack.setTitle("Commit Frequency by Date ");  
			    
	    for(String name: authors){
			    
		    XYChart.Series person = new XYChart.Series<>();
			    
		    for(int i=0;i<info.length;i++){
			    if(name.equals(info[i][0])){
			    	 
			       person.getData().add(new XYChart.Data(info[i][1]+" "+info[i][2],Integer.valueOf(info[i][3])));
			      
				  
			    }
		   // TODO Auto-generated method stub
		    }
		    stack.getData().add(person);
		    stack.setCreateSymbols(false);
		    person.setName(name);  
		   
	   }
	    listView.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
			 stack.getData().clear();
			 XYChart.Series person= new XYChart.Series();
			for(int i=0;i<allinfo.size();i++){
				
				String[] data= allinfo.get(i).split(" ");
				
				if(listView.getSelectionModel().getSelectedItem().equals(data[0])){
					  CategoryAxis xaxis = new CategoryAxis();  
					    NumberAxis yaxis = new NumberAxis(0,50,1);  
					    xaxis.setLabel("Date");  
					    yaxis.setLabel("Commit Count");  
					    //Creating StackedAreaChart   
					    StackedAreaChart stack = new StackedAreaChart(xaxis,yaxis);  
					    stack.setTitle("Commit Frequency by Date ");  
							    
					   
							    
					  
							    
						  
							    	 
							     person.getData().add(new XYChart.Data(data[1]+" "+data[2],Integer.valueOf(data[3])));
							      
								  
							    
						   // TODO Auto-generated method stub
						    
						  
							       
						   
					   
				}
				
			}
			  
			  stack.getData().add(person);
			  stack.setCreateSymbols(false);
			  person.setName((String)listView.getSelectionModel().getSelectedItem());     
			    
			 
			   // listView.setLayoutX(600);
		      //  listView.setLayoutY(50);
					    
			 /*   Group root = new Group();  
			    root.getChildren().add(stack); 
			    root.getChildren().add(listView);
			    Scene scene = new Scene(root,600,400);  
			    stage.setScene(scene);  
			    stage.setTitle("Commit Frequency By Date");  
			    stage.show();  */
			
			}
		});
	    stack.setScaleX(1.2);
	    stack.setScaleY(1.2);
	    stack.setLayoutX(50);
	    stack.setLayoutY(50);
	    
	    listView.setLayoutX(600);
        listView.setLayoutY(50);
			    
	    Group root = new Group();  
	    root.getChildren().add(stack); 
	    root.getChildren().add(listView);
	    Scene scene = new Scene(root,600,400);  
	    stage.setScene(scene);  
	    stage.setTitle("Commit Frequency By Date");  
	    stage.show();          
    }
	public static void main(String[] args) {  
	    launch(args);  
	}

}

