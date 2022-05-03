package tech.jaboc.animalcompetition.gui;

import javafx.application.Application;
import javafx.event.EventType;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.*;

import java.util.Properties;

public class AnimalMain extends Application {
	public static Properties properties;
	
	@Override
	public void start(Stage stage) throws Exception {
		properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream(".properties"));
		
		BorderPane root = new BorderPane();
		
		MenuBar menuBar = new MenuBar();
		
		Menu fileMenu = new Menu("File");
		MenuItem aboutItem = new MenuItem("About");
		aboutItem.setOnAction(e -> {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.initStyle(StageStyle.UTILITY);
			alert.setContentText(String.format("""
					Animal Competition
					Version: %s
					By Jacob Parker
					Released under MIT license
					https://github.com/AgentD1/AnimalCompetition""", properties.getProperty("version")));
			alert.setHeaderText(null);
			alert.setTitle("About Animal Competition");
			alert.show();
		});
		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(e -> {
			stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});
		
		fileMenu.getItems().addAll(aboutItem, exitItem);
		
		menuBar.getMenus().add(fileMenu);
		
		root.setTop(menuBar);
		
		
		VBox layout = new VBox();
		layout.prefWidthProperty().bind(root.widthProperty());
		
		Label label = new Label("Let's create an animal!!");
		label.setTextAlignment(TextAlignment.RIGHT);
		label.prefWidthProperty().bind(layout.widthProperty());
		label.setFont(Font.font(24.0));
		
		layout.getChildren().add(label);
		
		
		root.setCenter(layout);
		
		Scene scene = new Scene(root, 600, 400);
		stage.setScene(scene);
		stage.setTitle("Animal Main");
		stage.show();
		
		
	}
	
	public static void main(String[] args) {
		Application.launch(AnimalMain.class);
	}
}
