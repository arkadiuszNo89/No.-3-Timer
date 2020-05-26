package application;

import java.time.LocalDate;
import java.time.LocalTime;

public class Alarm 
{
	String name;
	LocalDate date;
	LocalTime time;
	String hour;
	String min;	
	int minutesLong;
	
	public Alarm(String name, LocalDate date, String hour, String min)
	{
		this.name = name;
		this.date = date;
		this.hour = hour;
		this.min = min;
		
		time = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(min));	
		minutesLong = initTools.minutesLongCounter(this.date, this.time);
	}
	public Alarm(String name, LocalDate date, String hour, String min, int minutesLong)
	{
		this.name = name;
		this.date = date;
		this.hour = hour;
		this.min = min;
		
		time = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(min));		
		this.minutesLong = minutesLong;
	}
	
	public String toString()
	{
		return name;	
	}
	
	


}
