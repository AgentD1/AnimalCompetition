package tech.jaboc.animalcompetition.gui;

import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import tech.jaboc.animalcompetition.animal.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class AnimalMain extends Application {
	public static Properties properties;
	
	@Override
	public void start(Stage stage) throws Exception {
		new LandMovementModule(); // initialize all modules that aren't explicitly referenced so that they get loaded. Java moment
		new AirMovementModule();
		new BaseModule();
		new BeakAttackModule();
		new ClawAttackModule();
		new BiteAttackModule();
		
		properties = new Properties();
		properties.load(getClass().getClassLoader().getResourceAsStream(".properties"));
		
		Scene scene = createMainScene(stage);
		
		stage.setScene(scene);
		
		stage.show();
	}
	
	public Scene createMainScene(Stage stage) {
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
		exitItem.setOnAction(e -> stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST)));
		
		fileMenu.getItems().addAll(aboutItem, exitItem);
		
		menuBar.getMenus().add(fileMenu);
		
		root.setTop(menuBar);
		
		VBox layout = new VBox();
		
		Label titleLabel = new Label("Welcome to the Friendly Fighting Arena (FFA for short)");
		titleLabel.setFont(Font.font(24.0));
		
		List<Animal> baseAnimals = getBaseAnimals();
		
		HBox userAnimalBox = new HBox();
		Label userAnimalLabel = new Label("Choose your fighter! (Or make your own!)");
		ChoiceBox<String> userAnimalChoiceBox = new ChoiceBox<>();
		userAnimalChoiceBox.getItems().addAll(baseAnimals.stream().map(t -> t.name).toList());
		Button userAnimalAddNewButton = new Button("+");
		userAnimalAddNewButton.setOnAction(e -> {
			Stage animalStage = new Stage();
			AnimalCreationReturnType animalCreationScene = createAnimalCreationScene(animalStage);
			// TODO: left off here
		});
		
		
		
		layout.getChildren().addAll(titleLabel);

		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		
		stage.setTitle("Create your animal");
		stage.sizeToScene();
		
		return scene;
	}
	
	record AnimalCreationReturnType(Scene scene, Animal animal) {}
	
	AnimalCreationReturnType createAnimalCreationScene(Stage stage) {
		VBox layout = new VBox();
		layout.prefWidthProperty().bind(stage.widthProperty());
		layout.setPadding(new Insets(5.0));
		layout.setSpacing(5.0);
		
		Label label = new Label("Let's create an animal!!");
		label.setFont(Font.font(24.0));
		
		//region Name Field
		HBox nameBox = new HBox();
		nameBox.alignmentProperty().set(Pos.CENTER_LEFT);
		Label nameLabel = new Label("Enter the name of your animal: ");
		TextField nameField = new TextField();
		nameField.setPromptText("Name here...");
		nameBox.getChildren().addAll(nameLabel, nameField);
		//endregion
		
		//region Species Field
		HBox speciesBox = new HBox();
		speciesBox.alignmentProperty().set(Pos.CENTER_LEFT);
		Label speciesLabel = new Label("Enter the species of your animal: ");
		TextField speciesField = new TextField();
		speciesField.setPromptText("Species here...");
		speciesBox.getChildren().addAll(speciesLabel, speciesField);
		//endregion
		
		//region Base Traits Field
		List<Trait> availableBaseTraits = List.of(
				new Trait("Lion Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 100.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 50.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 20.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.8, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.accuracy", 0.8, false, false, true),
				}),
				new Trait("Eagle Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 50.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 100.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 1.0, false, false, true),
						new ReflectiveModifier("AirMovementModule.speed", 40.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damage", 40.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.7, false, false, true),
						new ReflectiveModifier("BeakAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BeakAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("BeakAttackModule.accuracy", 1.1, false, false, true),
				}),
				new Trait("Cheetah Body", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 75.0, false, false, true),
						new ReflectiveModifier("BaseModule.damage", 75.0, false, false, true),
						new ReflectiveModifier("LandMovementModule.speed", 30.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
						new ReflectiveModifier("ClawAttackModule.accuracy", 0.9, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
						new ReflectiveModifier("BiteAttackModule.accuracy", 0.9, false, false, true),
				})
		);
		
		HBox baseTraitBox = new HBox();
		baseTraitBox.alignmentProperty().set(Pos.CENTER_LEFT);
		Label baseTraitLabel = new Label("Pick a base to build your animal from: ");
		ChoiceBox<String> baseTraitChoiceBox = new ChoiceBox<>();
		baseTraitChoiceBox.getItems().addAll(availableBaseTraits.stream().map(t -> t.name).toList());
		baseTraitBox.getChildren().addAll(baseTraitLabel, baseTraitChoiceBox);
		
		//endregion
		
		//region Other Traits Field
		List<Trait> otherTraits = List.of(
				new Trait("Fast", new ReflectiveModifier[] {
						new ReflectiveModifier("MovementModule.speed", 1.5, true, false, false),
				}),
				new Trait("Slow", new ReflectiveModifier[] {
						new ReflectiveModifier("MovementModule.speed", 0.666667, true, false, false),
				}),
				new Trait("Violent", new ReflectiveModifier[] {
						new ReflectiveModifier("AttackModule.damage", 1.25, true, false, false),
						new ReflectiveModifier("BaseModule.damage", 1.25, true, false, false),
				}),
				new Trait("Glass Cannon", new ReflectiveModifier[] {
						new ReflectiveModifier("BaseModule.health", 0.75, true, false, false),
						new ReflectiveModifier("BaseModule.damage", 1.5, true, false, false),
						new ReflectiveModifier("AttackModule.damage", 1.5, true, false, false),
				}),
				new Trait("Long Legs", new ReflectiveModifier[] {
						new ReflectiveModifier("LandMovementModule.speed", 1.5, true, false, true),
				})
		);
		
		HBox otherTraitsBox = new HBox();
		otherTraitsBox.alignmentProperty().set(Pos.CENTER_LEFT);
		Label otherTraitsLabel = new Label("Pick the optional traits you want: ");
		ListView<String> otherTraitsListView = new ListView<>();
		otherTraitsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		otherTraitsListView.getItems().addAll(otherTraits.stream().map(t -> t.name).toList());
		otherTraitsListView.setMaxHeight(24 * 5);
		otherTraitsBox.getChildren().addAll(otherTraitsLabel, otherTraitsListView);
		
		//endregion
		
		AtomicReference<Animal> animal = new AtomicReference<>(); // Can't use regular variables in swing lambdas because of some reason. Java moment
		
		Scene scene = new Scene(layout, 600, 400);
		
		Button button = new Button("Create!");
		button.setOnAction(e -> {
			if (nameField.getText().length() == 0 || nameField.getText().length() > 64) {
				new Alert(Alert.AlertType.ERROR, "Name must be between 1 and 64 characters!").showAndWait();
				return;
			}
			if (speciesField.getText().length() == 0 || speciesField.getText().length() > 64) {
				new Alert(Alert.AlertType.ERROR, "Species must be between 1 and 64 characters!").showAndWait();
				return;
			}
			if (baseTraitChoiceBox.getValue() == null) {
				new Alert(Alert.AlertType.ERROR, "You must select a base trait!").showAndWait();
				return;
			}
			animal.set(new Animal());
			animal.get().name = nameField.getText();
			animal.get().species = speciesField.getText();
			animal.get().addTrait(availableBaseTraits.stream().filter(t -> t.name.equals(baseTraitChoiceBox.getValue())).findFirst().orElseThrow());
			for (Trait trait : otherTraitsListView.getSelectionModel().getSelectedItems().stream().map(s -> otherTraits.stream().filter(t -> t.name.equals(s)).findFirst().orElseThrow()).toList()) {
				animal.get().addTrait(trait);
			}
			
			System.out.println(animal);
			stage.close();
		});
		
		layout.getChildren().addAll(label, nameBox, speciesBox, baseTraitBox, otherTraitsBox, button);
		
		
		stage.setScene(scene);
		
		stage.setTitle("Create your animal");
		stage.sizeToScene();
		
		stage.showAndWait();
		
		return new AnimalCreationReturnType(scene, animal.get());
	}
	
	public static void main(String[] args) {
		Application.launch(AnimalMain.class);
	}
	
	public List<Animal> getBaseAnimals() {
		List<Animal> animals = new ArrayList<>();
		
		Animal lion = new Animal();
		lion.species = "Lion";
		lion.addTrait(new Trait("Lion Body", new ReflectiveModifier[] {
				new ReflectiveModifier("BaseModule.health", 100.0, false, false, true),
				new ReflectiveModifier("BaseModule.damage", 50.0, false, false, true),
				new ReflectiveModifier("LandMovementModule.speed", 20.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.accuracy", 0.8, false, false, true),
				new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
				new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
				new ReflectiveModifier("BiteAttackModule.accuracy", 0.8, false, false, true),
		}));
		lion.addTrait(new Trait("Violent", new ReflectiveModifier[] {
				new ReflectiveModifier("AttackModule.damage", 1.25, true, false, false),
				new ReflectiveModifier("BaseModule.damage", 1.25, true, false, false),
		}));
		animals.add(lion);
		
		
		Animal eagle = new Animal();
		eagle.species = "Eagle";
		eagle.addTrait(new Trait("Eagle Body", new ReflectiveModifier[] {
				new ReflectiveModifier("BaseModule.health", 50.0, false, false, true),
				new ReflectiveModifier("BaseModule.damage", 100.0, false, false, true),
				new ReflectiveModifier("LandMovementModule.speed", 1.0, false, false, true),
				new ReflectiveModifier("AirMovementModule.speed", 40.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damage", 40.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damageRandomRange", 10.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.accuracy", 0.7, false, false, true),
				new ReflectiveModifier("BeakAttackModule.damage", 10.0, false, false, true),
				new ReflectiveModifier("BeakAttackModule.damageRandomRange", 5.0, false, false, true),
				new ReflectiveModifier("BeakAttackModule.accuracy", 1.1, false, false, true),
		}));
		eagle.addTrait(new Trait("Large Feet", new ReflectiveModifier[] {
				new ReflectiveModifier("LandMovementModule.speed", 1.5, true, false, true),
		}));
		eagle.addTrait(new Trait("Glass Cannon", new ReflectiveModifier[] {
				new ReflectiveModifier("BaseModule.health", 0.75, true, false, false),
				new ReflectiveModifier("BaseModule.damage", 1.5, true, false, false),
				new ReflectiveModifier("AttackModule.damage", 1.5, true, false, false),
		}));
		animals.add(eagle);
		
		Animal cheetah = new Animal();
		cheetah.species = "Cheetah";
		eagle.addTrait(new Trait("Cheetah Body", new ReflectiveModifier[] {
				new ReflectiveModifier("BaseModule.health", 75.0, false, false, true),
				new ReflectiveModifier("BaseModule.damage", 75.0, false, false, true),
				new ReflectiveModifier("LandMovementModule.speed", 30.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damage", 20.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.damageRandomRange", 5.0, false, false, true),
				new ReflectiveModifier("ClawAttackModule.accuracy", 0.9, false, false, true),
				new ReflectiveModifier("BiteAttackModule.damage", 10.0, false, false, true),
				new ReflectiveModifier("BiteAttackModule.damageRandomRange", 10.0, false, false, true),
				new ReflectiveModifier("BiteAttackModule.accuracy", 0.9, false, false, true),
		}));
		cheetah.addTrait(new Trait("Fast", new ReflectiveModifier[] {
				new ReflectiveModifier("MovementModule.speed", 1.5, true, false, false),
		}));
		animals.add(cheetah);
		
		
		return animals;
	}
}
