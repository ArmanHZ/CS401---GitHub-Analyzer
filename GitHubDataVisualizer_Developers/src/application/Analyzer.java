package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

public class Analyzer {

	 private static  ArrayList <String> names = new ArrayList<String>();
	 private static ArrayList <String> authors = new ArrayList<String>();
	 private static  ArrayList <Integer> commitCount = new ArrayList<Integer>();
	 private static ArrayList <String> allDatesandName= new ArrayList<String>();
	 private static ArrayList <String> datesandName =new ArrayList<String>();
	 private static ArrayList<String> justDates = new ArrayList<String>();
	 private static ArrayList<String> justDatesAll = new ArrayList<String>();
	 
	 
	
	 public static void main(String[] args) throws IOException {
		 
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
					String justDate= month +" "+day;
					justDatesAll.add(justDate);
					String date = name+" "+month +" "+day;
					allDatesandName.add(date);
					String y= f.nextLine();
					String z= f.nextLine();
					String v= f.nextLine();
					String u= f.nextLine();
					
					
						
						  
				
					
					System.out.println(name);
					System.out.println(date);
		     }
		
			 
			 int count=0;
		     for( String name:names){
			   if(!authors.contains(name)){
				 authors.add(name);
			   }
		     }
		 
		     for(String date:allDatesandName){
			   if(!datesandName.contains(date)){
				 datesandName.add(date);
			   }
		     }
		     for(String date:justDatesAll){
		    	 if(!justDates.contains(date)){
		    		 justDates.add(date);
		    	 }
		     }
		
		 
			 for(String date:datesandName){
				for(String alldate: allDatesandName){
				  if(date.equals(alldate)){
							
							   count++;
				 }
			   }
					
		        commitCount.add(count);
			    System.out.println("Occurrence of " + date + " is " + count + " times.");
	            count=0;
	            
		     }
			 int num=0;
			 String[] str=null;
			 for(String name:authors){
			  for (String date:allDatesandName){
					str= date.split(" ");
					//System.out.println(str[0]);
					//System.out.println(str[1]);
					//System.out.println(str[2]);
					for(String justdate:justDates){
						if(name.equals(str[0] )&& justdate.equals(str[1]+" "+str[2])){
							num++;
							
						}
					}
					System.out.println(str[0]+" "+num);
					num=0;
			  }
	      }
		           
	      
		 
	    	
	   /* 	BufferedReader reader = null ;
	        reader = new BufferedReader(new FileReader(file));
	        int i=0;
	        int k=7;
	        int b=0;
	        int a=0;
	        String line = reader.readLine();
	        
	        while (line != null) {
	              
	          if(i==2 || i==2+k  ){
	            	  
	        	 String[] array = line.split(" ");
	             for (int j = 0; j <array.length ; j++)
		         {
		        	 allwords.add(array[j]);
		        	 k +=7;
		        	 b++;
		        	 a +=3;
		       }
	             
	           
	         }
		         i++;
	             line = reader.readLine();
	       }
	 for(String word:allwords){
    	 if(b==2){
    		 names.add(word);
    	 }
    	
     }
	 for(String name:names){
		 System.out.println(name);
	 }
	 */
}  


	public ArrayList<String> getNames() {
		return names;
	}

	public void setNames(ArrayList<String> name) {
		this.names = name;
	}


	public static ArrayList<String> getAuthors() {
		return authors;
	}


	public static void setAuthors(ArrayList<String> authors) {
		Analyzer.authors = authors;
	}


	public static ArrayList<Integer> getCommitCount() {
		return commitCount;
	}


	public static void setCommitCount(ArrayList<Integer> commitCount) {
		Analyzer.commitCount = commitCount;
	}
	
	

	
}
