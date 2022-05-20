package tech.jaboc.animalcompetition.gui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Application;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import org.jetbrains.annotations.Nullable;
import tech.jaboc.animalcompetition.animal.*;
import tech.jaboc.animalcompetition.contest.*;
import tech.jaboc.animalcompetition.environment.Environment;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The GUI class, which controls the GUI.
 */
public class AnimalMain extends Application {
	public static Properties properties;
	static Environment.JsonEnvironmentalFactorList factorList;
	
	/**
	 * Starts the GUI
	 *
	 * @param stage The stage
	 * @throws Exception A couple unlikely IO errors I can't be bothered to catch.
	 */
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
		
		try {
			factorList = new ObjectMapper().readValue(new File("src/main/resources/environmentalFactors.json"),
					Environment.JsonEnvironmentalFactorList.class);
		} catch(FileNotFoundException e) {
			factorList = new ObjectMapper().readValue(new File("environmentalFactors.json"),
					Environment.JsonEnvironmentalFactorList.class);
		}
		
		
		Scene scene = createMainScene(stage);
		
		stage.setScene(scene);
		
		stage.show();
	}
	
	/**
	 * Creates the main scene
	 *
	 * @param stage The stage
	 * @return The scene
	 */
	public Scene createMainScene(Stage stage) {
		BorderPane root = new BorderPane();
		
		AtomicReference<List<Animal>> baseAnimals = new AtomicReference<>(new ArrayList<>(getBaseAnimals()));
		ObservableValue<ObservableList<String>> animalStrings = new SimpleObjectProperty<>(FXCollections.observableArrayList(baseAnimals.get().stream().map(a -> a.species).toList()));

		
		
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
		
		Menu editMenu = new Menu("Edit");
		MenuItem saveItem = new MenuItem("Save Animals");
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON file", "*.json"));
		fileChooser.setInitialFileName("animals.json");
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.setDefaultPrettyPrinter(new DefaultPrettyPrinter());
		
		saveItem.setOnAction(e -> {
			File selectedFile = fileChooser.showSaveDialog(new Stage());
			try {
				mapper.writerWithDefaultPrettyPrinter().writeValue(selectedFile, baseAnimals);
			} catch (IOException ex) {
				showErrorDialogue(ex);
			}
		});
		
		MenuItem loadItem = new MenuItem("Load Animals");
		loadItem.setOnAction(e -> {
			File selectedFile = fileChooser.showOpenDialog(new Stage());
			try {
				baseAnimals.set(mapper.readValue(selectedFile, new TypeReference<ArrayList<Animal>>() {}));
				animalStrings.getValue().clear();
				animalStrings.getValue().addAll(baseAnimals.get().stream().map(a -> a.species).toList());
			} catch (IOException ex) {
				showErrorDialogue(ex);
			}
		});
		
		editMenu.getItems().addAll(saveItem, loadItem);
		
		menuBar.getMenus().addAll(fileMenu, editMenu);
		
		root.setTop(menuBar);
		
		VBox layout = new VBox();
		layout.setPadding(new Insets(5.0));
		layout.setSpacing(5.0);
		
		Label titleLabel = new Label("Welcome to the Friendly Fighting Arena (FFA for short)");
		titleLabel.setFont(Font.font(24.0));
		
		
		HBox userAnimalBox = new HBox();
		userAnimalBox.alignmentProperty().set(Pos.CENTER_LEFT);
		userAnimalBox.setSpacing(5.0);
		Label userAnimalLabel = new Label("Choose your fighter! (Or make your own!)");
		ChoiceBox<String> userAnimalChoiceBox = new ChoiceBox<>();
		userAnimalChoiceBox.itemsProperty().bind(animalStrings);
		Button userAnimalAddNewButton = new Button("+");
		userAnimalAddNewButton.setOnAction(e -> {
			Stage animalStage = new Stage();
			Animal animal = askUserForAnimal(animalStage);
			if (animal == null) return;
			Animal match = baseAnimals.get().stream().filter(a -> a.species.equals(animal.species)).findAny().orElse(null);
			if (match == null) {
				baseAnimals.get().add(animal);
			} else {
				baseAnimals.get().remove(match);
				baseAnimals.get().add(animal);
			}
			animalStrings.getValue().add(animal.species);
			userAnimalChoiceBox.getSelectionModel().select(animal.species);
		});
		Button userAnimalEditButton = new Button("Edit");
		userAnimalEditButton.setOnAction(e -> {
			Animal a = baseAnimals.get().stream().filter(x -> x.species.equals(userAnimalChoiceBox.getSelectionModel().getSelectedItem())).findFirst().orElse(null);
			Animal animal = askUserForAnimal(new Stage(), a);
			if (animal == null) return;
			Animal match = baseAnimals.get().stream().filter(a1 -> a1.species.equals(animal.species)).findAny().orElse(null);
			if (match == null) {
				baseAnimals.get().add(animal);
				animalStrings.getValue().add(animal.species);
			} else {
				baseAnimals.get().remove(match);
				baseAnimals.get().add(animal);
			}
			userAnimalChoiceBox.getSelectionModel().select(animal.species);
		});
		userAnimalBox.getChildren().addAll(userAnimalLabel, userAnimalChoiceBox, userAnimalAddNewButton, userAnimalEditButton);
		
		HBox opponentAnimalBox = new HBox();
		opponentAnimalBox.alignmentProperty().set(Pos.CENTER_LEFT);
		opponentAnimalBox.setSpacing(5.0);
		Label opponentAnimalLabel = new Label("Choose your opponent! (Or make your own!)");
		ChoiceBox<String> opponentAnimalChoiceBox = new ChoiceBox<>();
		opponentAnimalChoiceBox.itemsProperty().bind(animalStrings);
		Button opponentAnimalAddNewButton = new Button("+");
		opponentAnimalAddNewButton.setOnAction(e -> {
			Stage animalStage = new Stage();
			Animal animal = askUserForAnimal(animalStage);
			if (animal == null) return;
			Animal match = baseAnimals.get().stream().filter(a -> a.species.equals(animal.species)).findAny().orElse(null);
			if (match == null) {
				baseAnimals.get().add(animal);
			} else {
				baseAnimals.get().remove(match);
				baseAnimals.get().add(animal);
			}
			animalStrings.getValue().add(animal.species);
			opponentAnimalChoiceBox.getSelectionModel().select(animal.species);
		});
		Button opponentAnimalEditButton = new Button("Edit");
		opponentAnimalEditButton.setOnAction(e -> {
			Animal a = baseAnimals.get().stream().filter(x -> x.species.equals(opponentAnimalChoiceBox.getSelectionModel().getSelectedItem())).findFirst().orElse(null);
			Animal animal = askUserForAnimal(new Stage(), a);
			if (animal == null) return;
			Animal match = baseAnimals.get().stream().filter(a1 -> a1.species.equals(animal.species)).findAny().orElse(null);
			if (match == null) {
				baseAnimals.get().add(animal);
				animalStrings.getValue().add(animal.species);
			} else {
				baseAnimals.get().remove(match);
				baseAnimals.get().add(animal);
			}
			opponentAnimalChoiceBox.getSelectionModel().select(animal.species);
		});
		opponentAnimalBox.getChildren().addAll(opponentAnimalLabel, opponentAnimalChoiceBox, opponentAnimalAddNewButton, opponentAnimalEditButton);
		
		AtomicReference<Environment> environment = new AtomicReference<>(); // Java moment part 2
		
		HBox environmentBox = new HBox();
		environmentBox.setSpacing(5.0);
		environmentBox.setAlignment(Pos.CENTER_LEFT);
		Button environmentButton = new Button("Generate an environment");
		Label environmentLabel = new Label("");
		
		
		environmentBox.getChildren().addAll(environmentButton, environmentLabel);
		
		GridPane environmentDisplayGrid = new GridPane();
		environmentDisplayGrid.setHgap(10.0);
		environmentDisplayGrid.setVgap(10.0);
		environmentDisplayGrid.setPadding(new Insets(10.0));
		environmentDisplayGrid.setAlignment(Pos.TOP_CENTER);
		Label environmentTimeLabel = new Label("Time:\n");
		Label environmentTerrainLabel = new Label("Terrain:\n");
		Label environmentTemperatureLabel = new Label("Temperature:\n");
		Label environmentWeatherLabel = new Label("Weather:\n");
		Label environmentFeaturesLabel = new Label("Features:\n");
		
		environmentDisplayGrid.addColumn(0, environmentTimeLabel);
		environmentDisplayGrid.addColumn(1, environmentTerrainLabel);
		environmentDisplayGrid.addColumn(2, environmentTemperatureLabel);
		environmentDisplayGrid.addColumn(3, environmentWeatherLabel);
		environmentDisplayGrid.addColumn(4, environmentFeaturesLabel);
		
		environmentDisplayGrid.getRowConstraints().forEach(c -> c.setValignment(VPos.TOP));
		
		environmentButton.setOnAction(e -> {
			Environment env = Environment.generateEnvironment(factorList);
			environment.set(env);
			environmentTimeLabel.setText("Time:\n" + env.timeFactor.name);
			environmentTerrainLabel.setText("Terrain:\n" + env.terrainFactor.name);
			environmentTemperatureLabel.setText("Temperature:\n" + env.temperatureFactor.name);
			environmentWeatherLabel.setText("Weather:\n" + env.weatherFactor.name);
			if (env.features.size() == 0) {
				environmentFeaturesLabel.setText("Features:\nNone");
			} else {
				environmentFeaturesLabel.setText("Features:\n" + String.join(", ", env.features.stream().map(f -> f.name).toList()));
				System.out.println(env.features.size());
			}
			
			environmentDisplayGrid.setPrefHeight(75.0);
		});
		
		ScrollPane fightTextScrollPane = new ScrollPane();
		VBox fightTextContainer = new VBox();
		fightTextScrollPane.setContent(fightTextContainer);
		VBox scrollPaneContainer = new VBox(fightTextScrollPane);
		scrollPaneContainer.setPrefHeight(100.0);
		VBox.setVgrow(scrollPaneContainer, Priority.ALWAYS);
		fightTextScrollPane.prefHeightProperty().bind(scrollPaneContainer.heightProperty());
		
		Button startButton = new Button("Fight!!!");
		startButton.setOnAction(e -> {
			Contest contest = new FightContest();
			Animal userAnimal;
			Animal opponentAnimal;
			try {
				userAnimal = baseAnimals.get().stream().filter(x -> x.species.equals(userAnimalChoiceBox.getSelectionModel().getSelectedItem())).findFirst().orElseThrow();
				opponentAnimal = baseAnimals.get().stream().filter(x -> x.species.equals(opponentAnimalChoiceBox.getSelectionModel().getSelectedItem())).findFirst().orElseThrow();
			} catch (Exception ignored) {
				new Alert(Alert.AlertType.ERROR, "You must select an animal for both you and your opponent!").showAndWait();
				return;
			}
			
			System.out.println(userAnimal);
			System.out.println(opponentAnimal);
			
			fightTextContainer.getChildren().clear();
			
			PrintStream fightOutputStream = new PrintStream(new OutputStream() {
				Label currentLabel;
				
				@Override
				public void write(int b) {
					if (b == '\n') {
						fightTextContainer.getChildren().add(currentLabel);
						fightTextScrollPane.setVvalue(Double.MAX_VALUE);
						currentLabel = new Label();
					} else {
						if(currentLabel == null) {
							currentLabel = new Label();
						}
						currentLabel.setText(currentLabel.getText() + new String(Character.toChars(b)));
					}
				}
			});
			
			Optional<Animal> winner = contest.resolve(userAnimal, opponentAnimal, environment.get(), fightOutputStream);
			
			if (winner.isEmpty()) {
				fightOutputStream.println("The fight was a draw!");
			} else {
				if (winner.get() == userAnimal) {
					fightOutputStream.println("You won!");
				} else {
					fightOutputStream.println("You lost...");
				}
			}
			
			fightTextScrollPane.setVvalue(Double.MAX_VALUE);
			
			System.out.println("Done");
		});
		
		layout.getChildren().addAll(titleLabel, userAnimalBox, opponentAnimalBox, environmentBox, environmentDisplayGrid, startButton, scrollPaneContainer);
		
		root.setCenter(layout);
		
		Scene scene = new Scene(root);
		
		stage.setScene(scene);
		
		stage.setTitle("Create your animal");
		stage.sizeToScene();
		
		return scene;
	}
	
	/**
	 * Asks the user for an animal
	 *
	 * @param stage A stage to display the prompt on
	 * @return The user's animal choice. Null if the user cancels.
	 */
	Animal askUserForAnimal(Stage stage) {
		return askUserForAnimal(stage, new Animal());
	}
	
	/**
	 * Asks the user for an animal
	 *
	 * @param stage    A stage to display the prompt on
	 * @param defaults An existing animal to edit/build upon
	 * @return The user's animal choice. Null if the user cancels.
	 */
	Animal askUserForAnimal(Stage stage, @Nullable Animal defaults) {
		VBox layout = new VBox();
		layout.prefWidthProperty().bind(stage.widthProperty());
		layout.setPadding(new Insets(5.0));
		layout.setSpacing(5.0);
		
		Label label = new Label("Let's create an animal!!");
		label.setFont(Font.font(24.0));
		
		//region Species Field
		HBox speciesBox = new HBox();
		speciesBox.alignmentProperty().set(Pos.CENTER_LEFT);
		Label speciesLabel = new Label("Enter the species of your animal: ");
		TextField speciesField = new TextField();
		speciesField.setPromptText("Species here...");
		speciesField.setText(defaults == null ? "" : defaults.species);
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
		if (defaults != null) {
			String defaultBaseTrait = defaults.getTraits().stream().filter(t -> availableBaseTraits.stream().anyMatch(x -> x.name.equals(t.name))).map(t -> t.name).findFirst().orElse(null);
			if (defaultBaseTrait != null) {
				System.out.println(defaultBaseTrait);
				baseTraitChoiceBox.getSelectionModel().select(defaultBaseTrait);
			}
			baseTraitBox.getChildren().addAll(baseTraitLabel, baseTraitChoiceBox);
		}
		
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
		Label otherTraitsLabel = new Label("Pick the optional traits you want: \n(Use CTRL to select multiple)");
		ListView<String> otherTraitsListView = new ListView<>();
		otherTraitsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		otherTraitsListView.getItems().addAll(otherTraits.stream().map(t -> t.name).toList());
		otherTraitsListView.setMaxHeight(24 * 5);
		
		if (defaults != null) {
			List<String> otherTraitsDefaults = defaults.getTraits().stream().filter(t -> otherTraits.stream().anyMatch(x -> x.name.equals(t.name))).map(t -> t.name).toList();
			otherTraitsDefaults.stream().filter(t -> otherTraits.stream().anyMatch(x -> x.name.equals(t))).forEach(t -> otherTraitsListView.getSelectionModel().select(t));
		}
		
		otherTraitsBox.getChildren().addAll(otherTraitsLabel, otherTraitsListView);
		
		//endregion
		
		AtomicReference<Animal> animal = new AtomicReference<>(); // Can't use regular non-final variables in lambdas because of some reason. Java moment 💀
		
		Scene scene = new Scene(layout, 600, 400);
		
		Button button = new Button("Create!");
		button.setOnAction(e -> {
			if (speciesField.getText() == null || speciesField.getText().length() == 0 || speciesField.getText().length() > 64) {
				new Alert(Alert.AlertType.ERROR, "Species must be between 1 and 64 characters!").showAndWait();
				return;
			}
			if (baseTraitChoiceBox.getValue() == null) {
				new Alert(Alert.AlertType.ERROR, "You must select a base trait!").showAndWait();
				return;
			}
			animal.set(new Animal());
			animal.get().species = speciesField.getText();
			animal.get().addTrait(availableBaseTraits.stream().filter(t -> t.name.equals(baseTraitChoiceBox.getValue())).findFirst().orElseThrow());
			for (Trait trait : otherTraitsListView.getSelectionModel().getSelectedItems().stream().map(s -> otherTraits.stream().filter(t -> t.name.equals(s)).findFirst().orElseThrow()).toList()) {
				animal.get().addTrait(trait);
			}
			
			System.out.println(animal);
			stage.close();
		});
		
		layout.getChildren().addAll(label, speciesBox, baseTraitBox, otherTraitsBox, button);
		
		
		stage.setScene(scene);
		
		stage.setTitle("Create your animal");
		stage.sizeToScene();
		
		stage.showAndWait();
		
		return animal.get();
	}
	
	/**
	 * Gets a list of the base animals
	 *
	 * @return The list of the base animals
	 */
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
		cheetah.addTrait(new Trait("Cheetah Body", new ReflectiveModifier[] {
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
	
	static void showErrorDialogue(Exception e) {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(writer);
		e.printStackTrace(out);
		String output = writer.toString();
		new Alert(Alert.AlertType.ERROR, "An error occurred!\n" + output).showAndWait();
	}
}
