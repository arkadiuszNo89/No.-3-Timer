package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Scanner;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;

public class initTools 
{
	public static void initSpinners (Spinner <String> spinner, int from, int till)
	{
		ObservableList <String> tempListNumbers = FXCollections.observableArrayList();
		for (int x = from; x < till; x++) 
		{
			if (x < 10) 
				{
					tempListNumbers.add("0"+x);
				}
			else
				{
					tempListNumbers.add(x+"");
				}
		}
		SpinnerValueFactory <String> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<String>(tempListNumbers);
		
		spinner.setValueFactory(valueFactory); 
		
		spinner.setEditable(true); 
		
		spinner.valueProperty().addListener(new javafx.beans.value.ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> arg0, String olds, String news) {
				
				try
				{
					int x = Integer.parseInt(news);
					if (x >= from && x < 10 && x < till)	spinner.getValueFactory().setValue("0"+x);
					else if (x >= 10 && x < till)	spinner.getValueFactory().setValue(""+x);
					else tempListNumbers.remove(news);
				}
				catch (Exception e)
				{
					spinner.getValueFactory().setValue(olds);
				}
			}
		});	
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void disableOldDays (DatePicker picker)
	{
		Callback <DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) 
            {
                return new DateCell() 
                {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                       
                        if (item.isBefore(LocalDate.now())) 
                        { 
                                setDisable(true);
                                setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            	}};
        picker.setDayCellFactory(dayCellFactory);
        picker.setEditable(false);
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void textFieldSetter (TextField field, int max)
	{	
		field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
		{
			if(newValue.length() > max) field.setText(oldValue);
		});
	}
	public static void textFieldSetter (TextField field, int max, String promptText)
	{
		field.setPromptText(promptText);		
		field.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) ->
		{
			if(newValue.length() > max) field.setText(oldValue);
		});
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static int minutesLongCounter(LocalDate date,  LocalTime time)
	{		
		int days = date.getDayOfYear() - LocalDate.now().getDayOfYear();
		int x = LocalTime.now().getHour()*60 + LocalTime.now().getMinute();
		int minutesCycledPerDays = (days * 60 * 24) - x;
		
		return minutesCycledPerDays + (time.getHour() *60) + time.getMinute();
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static double progressCounter(Alarm alarm)
	{	
		int x = alarm.minutesLong - initTools.minutesLongCounter(alarm.date, alarm.time);
		return (double) x / alarm.minutesLong; 
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void saveAlarms(List <Alarm> list, List <Alarm> listArch)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("*.txt", "*.txt"));		
		File file = fileChooser.showSaveDialog(null);
		
		if (file != null)
		{
			try
			{
				FileWriter writer = new FileWriter(file);
				writer.write("# Timer\n");
				
				for (Alarm alarm : list)
				{
					writer.write("#Cont\n");
					writer.write(alarm.name+"\n");
					writer.write(alarm.date.toString()+"\n");
					writer.write(alarm.hour+"\n");
					writer.write(alarm.min+"\n");	
					writer.write(""+alarm.minutesLong+"\n");
				}
				for (Alarm alarm : listArch)
				{
					writer.write("#Arch\n");
					writer.write(alarm.name+"\n");
					writer.write(alarm.date.toString()+"\n");
					writer.write(alarm.hour+"\n");
					writer.write(alarm.min+"\n");	
					writer.write(""+alarm.minutesLong+"\n");
				}
				
				writer.close();
			}
			catch (Exception e)
			{
				
			}
		}	
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public static void loadAlarms(List <Alarm> list, List <Alarm> listArch)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new ExtensionFilter("*.txt", "*.txt"));		
		File file = fileChooser.showOpenDialog(null);
		
		if (file != null)
		{
			try
			{
				Scanner reader = new Scanner(new BufferedReader(new FileReader(file)));
				if (reader.nextLine().equals("# Timer"))
				{
					list.clear();
					listArch.clear();
					while(reader.hasNextLine())
					{
						String readedLine = reader.nextLine();
						if (readedLine.equals("#Cont"))
						{
							System.out.println();
							String name = reader.nextLine();
							LocalDate date = LocalDate.parse(reader.nextLine());
							String hour = reader.nextLine();
							String min = reader.nextLine();
							int minutesLong = Integer.parseInt(reader.nextLine());
							
							Alarm alarm = new Alarm(name, date, hour, min, minutesLong);
							System.out.println(alarm);
							list.add(alarm);
						}
						else if (readedLine.equals("#Arch"))
						{
							String name = reader.nextLine();
							LocalDate date = LocalDate.parse(reader.nextLine());
							String hour = reader.nextLine();
							String min = reader.nextLine();
							int minutesLong = Integer.parseInt(reader.nextLine());
							
							Alarm alarm = new Alarm(name, date, hour, min, minutesLong);
							System.out.println(alarm);
							listArch.add(alarm);
						}
					}
					
				}
				reader.close();
			}
			catch (Exception e)
			{
				
			}
		}	
	}
}














