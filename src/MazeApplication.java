import maze.visualisation.*;
import maze.Maze;
import maze.routing.RouteFinder;
import javafx.application.Application;
import javafx.stage.*;
import javafx.scene.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MazeApplication extends Application {
	private Scene s;
	
	private String name;
	private String path;

    
	public void start(Stage stage) throws Exception{		
		Visualisation v = new Visualisation();

		FlowPane pane1=new FlowPane();
		Stage newStage = new Stage();
		TextField pushMeTextField = new TextField();
		pushMeTextField.setMaxWidth(400);

		Label pushMeLabel= new Label(); 
		pushMeLabel.setTextFill(Color.RED);
		pushMeLabel.setFont(Font.font("Arial", 20));

		Button pushMeButton = new Button();
		pushMeButton.setText("Type name of maze in the box then push me");
		pushMeButton.setOnAction(e -> {
			pushMeLabel.setText("You entered: " + pushMeTextField.getText());
			newStage.close();
		});

		pane1.setHgap(20);
		pane1.setStyle("-fx-background-color:tan;-fx-padding:10px;");
		pane1.getChildren().addAll(pushMeTextField, pushMeButton, pushMeLabel);
		Scene scene1 = new Scene(pane1, 400, 300);
		newStage.setScene(scene1);
		newStage.initModality(Modality.APPLICATION_MODAL);
		newStage.setTitle("Pop up window");
		newStage.showAndWait();
		
		
		

		
	}

    public static void main(String[] args) {
        launch(args);
    }
}



















