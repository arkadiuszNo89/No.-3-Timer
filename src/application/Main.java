package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;


public class Main extends Application {
	
	public static Stage mainStage;
	
	@Override
	public void start(Stage primaryStage) {
		try 
		{
			mainStage = primaryStage;
			
			FXMLLoader loader = new FXMLLoader(getClass().getResource("GenSetView.fxml"));
			BorderPane root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();	
			primaryStage.setResizable(false);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
