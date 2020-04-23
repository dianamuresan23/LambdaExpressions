package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import data.Duration;
import data.MonitoredData;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//1.Read data
		System.out.println("1.Read data");
		String fileName="Activity.txt";
		ArrayList<MonitoredData> activities=new ArrayList<MonitoredData>();
		Stream<String> stream = null;
		try {
			    stream = Files.lines(Paths.get(fileName)); 
				
			    stream.forEach(line->{
			    	String[] st=line.split("\t\t");
			    	activities.add(new MonitoredData(st[0],st[1],st[2]));
			    });
				
			    activities.stream().forEach(System.out::println);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2.Number of monitored days
		System.out.println("2.Number of monitored days");
		List<String> days=activities.stream().map(m->{
			String day=String.valueOf(m.getStartTime().getDate());
			String month=String.valueOf(m.getStartTime().getMonth());
			String date=day+month;
			return date;}).distinct().collect(Collectors.toList());
			
		System.out.println(days.size());
		
		//3. Appearance of each activity
		System.out.println("3.Appearance of each activity");
		Map<String, Long> appearance=activities.stream().collect(Collectors.groupingBy(MonitoredData::getActivity,Collectors.counting()));
		
		
		appearance.forEach((activitate,ap)->{
			System.out.println(activitate+" "+ap);
		});
		
		
		//4.Appearance of each activity per day
		System.out.println("4.Appearance of each activity per day");
		
		Map<String,Map<String,Long>> dayappearance=activities.stream()
			.collect(Collectors.groupingBy((m->{
				String day=String.valueOf(m.getStartTime().getDate());
				int mon=m.getStartTime().getMonth();
				mon+=1;
				String month=String.valueOf(mon);
				String date=day+"."+month;
				return date;}),Collectors.groupingBy(MonitoredData::getActivity,Collectors.counting())));
		

	     Map<String, Map<String,Long>> treeMap1 = new TreeMap<String, Map<String,Long>>(dayappearance);
	
	      treeMap1.entrySet().stream().forEach((e->{
	    	  e.getValue().entrySet().stream().forEach(m->{
	    		  System.out.println(e.getKey()+" "+m.getKey()+" "+m.getValue());
	    	  });
	  
	      }));
	      
		
		//5.Duration for each activity
	   System.out.println("5.Duration for each activity");
		
		  Duration d=(a,b)->a-b;
		  
	      Map<Date, Map<String,Long>> duration=activities.stream()
			 
	    		.collect(Collectors.groupingBy(MonitoredData::getStartTime,
	    				Collectors.toMap(MonitoredData::getActivity,m->d.computeDuration(m.getEndTime().getTime(),m.getStartTime().getTime()))));
	    
	      Map<Date, Map<String,Long>> treeMap = new TreeMap<Date, Map<String,Long>>(duration);
	      
	
	      treeMap.entrySet().stream().forEach((e->{
	    	  e.getValue().entrySet().stream().forEach(m->{
	    		  Long value=m.getValue()/1000;
				 String time=String.valueOf(value/3600);
		            time+=" hours ";
		            time+=String.valueOf((value%3600)/60);
		            time+=" min ";
		            time+=String.valueOf((value%60));
		            time+=" sec ";
	    		  
	    		  System.out.println(e.getKey()+" "+m.getKey()+" "+time);
	    	  });
	  
	      }));
		
		//6.Entire duration for each activity
	   System.out.println("6.Entire duration for each activity");
	  
	    
		Map<String,Long> eduration=activities.stream()
				.collect(Collectors.groupingBy(MonitoredData::getActivity,
						Collectors.summingLong(n-> d.computeDuration(n.getEndTime().getTime(),n.getStartTime().getTime()))));
				

		eduration.forEach((activity,durata)->{
			   Long dur=durata/1000;
			   String time=String.valueOf(dur/3600);
	            time+=" hours ";
	            time+=String.valueOf((dur%3600)/60);
	            time+=" min ";
	            time+=String.valueOf((dur%60));
	            time+=" sec ";

	            System.out.println(activity + " " +time);  
	         
		});
		
		
		//7.Filter the activities that have 90% of the monitoring records with duration less than 5 minutes
		System.out.println("7.Activities that have 90% of the monitoring records with duration less than 5");
		
		
		Map<String,Long> map1=activities.stream().collect(Collectors.groupingBy(MonitoredData::getActivity,Collectors.counting()));
		Map<String,Long> map2=activities.stream()
				.filter(m->d.computeDuration(m.getEndTime().getTime(),m.getStartTime().getTime())<300000)
				.collect(Collectors.groupingBy(MonitoredData::getActivity,Collectors.counting()));
		
		List<String> filtered=(List)map2.entrySet().stream()
				.filter(a->a.getValue()>= 0.9*map1.get(a.getKey()))
				.map(x->x.getKey())
				.collect(Collectors.toList());
		
		System.out.println("Activities that have the monitoring records with duration less than 5 min");
		map2.forEach((activity,ap)->{
			System.out.println(activity+" "+ap);
		});
		System.out.println("Filtered");
		filtered.forEach(System.out::println);
		
		
		
		


	}


   
	

	

}
