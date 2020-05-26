package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;


public class TimeData 
{
	private static ArrayList <TimeEvent> timeEvents;
	Timeline timeline;
	
	public LocalDate date;
	public LocalTime time;	
	
	public String dateString;
	public String timeString;
	
	public TimeData()
	{
		timeEvents = new ArrayList <TimeEvent> ();
		
		timeline = new Timeline(
			    new KeyFrame(Duration.millis(1000), e->
			    {
					date = LocalDate.now();
					time = LocalTime.now();		
					dateString = date.format(DateTimeFormatter.ofPattern("dd LLLL yyyy"));
					timeString = time.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
			    	for(TimeEvent item : timeEvents) item.makeItDone(this);
			    }));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}


	public void onExit()
	{
		timeline.stop();
	}
	public static void addTimeEvent(TimeEvent timeEvent)
	{
		timeEvents.add(timeEvent);
	}
}
