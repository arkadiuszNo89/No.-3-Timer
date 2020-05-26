package application;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Queue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class GenSetController 
{
    @FXML
    private Label DateLabel;

    @FXML
    private Label TimeLabel;

    @FXML
    private ProgressIndicator progressIn1;

    @FXML
    private ListView<Alarm> listView;

    @FXML
    private TextField nameTextField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Spinner<String> hourSpinner;

    @FXML
    private Spinner<String> minSpinner;

    @FXML
    private ToggleButton addShowButton;

    @FXML
    private Button moveButton;

    @FXML
    private ProgressIndicator progressIn2;

    @FXML
    private ProgressIndicator progressIn3;

    @FXML
    private ProgressIndicator progressIn4;

    @FXML
    private Label dateChoosenOne;

    @FXML
    private Label timeChoosenOne;

    @FXML
    private Label nameChoosenOne;
    
    @FXML
    private Label infoLabel;
    
    @FXML
    private Button saveButton;

    @FXML
    private Button loadButton;

    @FXML
    private ListView<Alarm> listViewArchive;
    
    
	/////////////////////////////////////////////	
   /////////// ZMIENNE OBIEKTU /////////////////
  /////////////////////////////////////////////
    
	TimeEvent initLabels, checkAlarms, startAlarm, checkProgress, startRing;
	boolean flagaAlarm, flash;
	Queue <Alarm> kolejkaAlarm, kolejkaRemove;
	Comparator <Alarm> compareAlarms, compareAlarmsArchive;
	EventHandler <ActionEvent> createAlarm, moveToArchive;
	ProgressIndicator [] progressIns; 
	Alarm pointedAlarm;
	
	/////////////////////////////////////////////	
   /////////// INICJALIZACJA OBIEKTU ///////////
  /////////////////////////////////////////////
	
	public void initialize()
	{
		initNodes();
		createActions();
		initInters();
		moveButton.setOnAction(createAlarm);

		new TimeData();
		
		TimeData.addTimeEvent(initLabels);
		TimeData.addTimeEvent(checkAlarms);
		TimeData.addTimeEvent(startAlarm);
		TimeData.addTimeEvent(checkProgress);
	}
	
	/////////////////////////////////////////////	
   ////////////////// METODY ///////////////////
  /////////////////////////////////////////////

	private void initInters()
	{
//						# 1
		initLabels = (TimeData timeData) ->
		{
			DateLabel.setText(timeData.dateString);
			TimeLabel.setText(timeData.timeString);
		};
//						# 2
		checkAlarms = timeData ->
		{
			for (Alarm alarm : listView.getItems())
			{
				if (timeData.date.isEqual(alarm.date) && timeData.time.isAfter(alarm.time))
				{
					kolejkaAlarm.add(alarm);
					kolejkaRemove.add(alarm);
				}
				else if (timeData.date.isAfter(alarm.date))
				{
					kolejkaAlarm.add(alarm);
					kolejkaRemove.add(alarm);
				}
			}
			while (kolejkaRemove.peek() != null)
			{
				listViewArchive.getItems().add(kolejkaRemove.peek());
				listView.getItems().remove(kolejkaRemove.poll());
				listViewArchive.getItems().sort(compareAlarmsArchive);
			}
		};
//						# 3
		startAlarm = timeData ->
		{
			if (kolejkaAlarm.peek() != null) flagaAlarm = true;
			if (flagaAlarm)
			{
				infoLabel.setText(kolejkaAlarm.peek().toString());
				flashText();
			}
		};
//						# 4
		checkProgress = timeData ->
		{
			for (int x = 0; x < listView.getItems().size(); x++) 
			{
				if (x == 4) break;
				progressIns[x].setProgress(initTools.progressCounter(listView.getItems().get(x)));
			}
			if (listView.getItems().size() < 4)
			{
				for (int x = 3; x >= listView.getItems().size(); x--)
				{
					progressIns[x].setProgress(0);
				}
			}
			if (listView.getSelectionModel().getSelectedItem() != null)
				{
					progressBar.setProgress(initTools.progressCounter(listView.getSelectionModel().getSelectedItem()));
				}
			else progressBar.setProgress(0);
		};
	}
	
	
	private void initNodes()
	{
		initTools.textFieldSetter(nameTextField, 22, "Nazwa obiektu");		
		initTools.disableOldDays(datePicker);	
		initTools.initSpinners(hourSpinner, 0, 24);
		initTools.initSpinners(minSpinner, 0, 60);
		primalState();
		listViewArchive.setVisible(false);
		progressIns = new ProgressIndicator [] {progressIn1 , progressIn2 , progressIn3 , progressIn4};
		
		infoLabel.setVisible(false);
		flagaAlarm = false;	
		flash = false;
		kolejkaAlarm = new ArrayDeque <Alarm> ();
		kolejkaRemove = new ArrayDeque <Alarm> ();
	}
	
	
	private void createActions()
	{
		
		createAlarm = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) 
			{
				if (nameTextField.getText().isEmpty())
				{
					infoLabel.setVisible(true);
					if (!flagaAlarm) infoLabel.setText("Nie wpisano nazwy");
				}
				else if (listView.getItems().toString().contains(nameTextField.getText()))
				{
					infoLabel.setVisible(true);
					if (!flagaAlarm) infoLabel.setText("Nazwa istnieje");
				}		
				else if (LocalTime.now().isAfter(LocalTime.of(Integer.parseInt(hourSpinner.getValue()), Integer.parseInt(minSpinner.getValue())))
						&& LocalDate.now().isEqual(datePicker.getValue()))
				{
					infoLabel.setVisible(true);
					if (!flagaAlarm) infoLabel.setText("Jest po wyznaczonej godzinie");
				}
				else
				{
					if (!flagaAlarm)
					{
						infoLabel.setVisible(false);
						infoLabel.setText("");
					}
					String name = nameTextField.getText();
					LocalDate date = datePicker.getValue();
					String hour = hourSpinner.getValue();
					String min = minSpinner.getValue();
		
					listView.getItems().add(new Alarm (name, date, hour, min));
					listView.getItems().sort(compareAlarms);
					primalState();
				}
			}
		};
		
		moveToArchive = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) 
			{
				if (listView.getSelectionModel().getSelectedItem() != null)
				{
					Alarm alarm = listView.getSelectionModel().getSelectedItem();
					listViewArchive.getItems().add(alarm);
					listViewArchive.getItems().sort(compareAlarmsArchive);
					listView.getItems().remove(alarm);
				}
			}
		};
		
		compareAlarms = new Comparator<Alarm>() {

			@Override
			public int compare(Alarm o1, Alarm o2) {
				
				if (o1.date.isEqual(o2.date) && o1.time.isAfter(o2.time)) return 1;
				else if (o1.date.isEqual(o2.date) && o1.time.isBefore(o2.time)) return -1;
				else if (o1.date.isAfter(o2.date)) return 1;
				else if (o1.date.isBefore(o2.date)) return -1;

				return 0;
			}
		};
		compareAlarmsArchive = new Comparator<Alarm>() {

			@Override
			public int compare(Alarm o1, Alarm o2) {
				
				if (o1.date.isEqual(o2.date) && o1.time.isAfter(o2.time)) return -1;
				else if (o1.date.isEqual(o2.date) && o1.time.isBefore(o2.time)) return 1;
				else if (o1.date.isAfter(o2.date)) return -1;
				else if (o1.date.isBefore(o2.date)) return 1;

				return 0;
			}
		};

		
		
		
		listView.setOnMouseClicked( event -> 
		{			
			if (listView.getSelectionModel().getSelectedItem() != null) 
				{
					choosenAlarmView(listView.getSelectionModel().getSelectedItem());
					progressBar.setProgress(initTools.progressCounter(listView.getSelectionModel().getSelectedItem()));
				}
		});
		listViewArchive.setOnMouseClicked( event -> 
		{			
			if (listViewArchive.getSelectionModel().getSelectedItem() != null) choosenAlarmView(listViewArchive.getSelectionModel().getSelectedItem());
		});
		
		
		infoLabel.setOnMouseClicked( event ->
		{
			if (kolejkaAlarm.peek() != null) 
			{	
					kolejkaAlarm.poll();
					
					if (kolejkaAlarm.peek() == null) 
						{
							primalStateText();
							flagaAlarm = false;
						}
					else 
						{
							infoLabel.setText(kolejkaAlarm.peek().toString());
							flashText();
						}
			}
		});
		
		addShowButton.setOnMouseClicked( event ->
		{
			if (addShowButton.isSelected())
			{
				addShowButton.setText("add");
				listViewArchive.setVisible(true);
				moveButton.setText(">");
				moveButton.setOnAction(moveToArchive);
			}
			else
			{
				addShowButton.setText("arch");
				listViewArchive.setVisible(false);
				moveButton.setText("<");
				moveButton.setOnAction(createAlarm);
			}
		});
		
		saveButton.setOnMouseClicked( event ->
		{
			initTools.saveAlarms(listView.getItems(), listViewArchive.getItems());
			
		});
		
		loadButton.setOnMouseClicked( event ->
		{
			initTools.loadAlarms(listView.getItems(), listViewArchive.getItems());
			
		});
	}
	
	/////////////////////////////////////////////	
   //////////// METODY DODATKOWE ///////////////
  /////////////////////////////////////////////	
	
	private void flashText()
	{
		infoLabel.setVisible(true);
		
		if (flash)
		{
			infoLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(10), Insets.EMPTY)));
			infoLabel.setTextFill(Color.YELLOW);
			flash = false;
		}
		else
		{
			infoLabel.setBackground(new Background(new BackgroundFill(Color.YELLOW, new CornerRadii(10), Insets.EMPTY)));
			infoLabel.setTextFill(Color.BLACK);
			flash = true;
		}
	}
	
	private void primalStateText()
	{
		infoLabel.setVisible(false);
		infoLabel.setText("");
		infoLabel.setBackground(new Background(new BackgroundFill(null, CornerRadii.EMPTY, Insets.EMPTY)));
		infoLabel.setTextFill(Color.BLACK);
	}
	
	private void primalState()
	{
		nameTextField.clear();
        datePicker.setValue(LocalDate.now());
        hourSpinner.getValueFactory().setValue("12");
        minSpinner.getValueFactory().setValue("00");
	}
	
	private void choosenAlarmView(Alarm alarm)
	{
		nameChoosenOne.setText(alarm.name);
		dateChoosenOne.setText(alarm.date.format(DateTimeFormatter.ofPattern("dd LLLL yyyy")));
		timeChoosenOne.setText(alarm.time.format(DateTimeFormatter.ofPattern("HH:mm")));
	}
}






