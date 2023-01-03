package cool.generator.gui;

import cool.generator.WorldMap;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App extends Application {

    private boolean saveToCSV = false;
    private boolean isPortalEnabled;

    // false = zalesione rowniki, true = toksyczne trupy
    private boolean preferedPlantSpawnToxic;

    //true - wariant pełnej losowości //false - lekka korekta (1 w dół lub 1 w góre)
    //dla startowych zwierząt musi być true - bo nie ma genotypów rodziców, od których można przejąć tą wartość
    private boolean mutationsRandomness;
    private boolean moveRandomness;

    private int width;
    private int height;
    private int energyFromPlant;
    private int dailyPlants;
    private int genotypeLength;
    private int startEnergyForAnimal;
    private int readyToCoupleEnergy;
    private int lossOnCoupleEnergy;
    private int minMutations;
    private int maxMutations;

    private int initialNumberOfPlants;
    private int initialNumberOfAnimals;
    private final int sizeOfThreads = 5;
    private int index = 0;
    private Thread[] threads = new Thread[sizeOfThreads];

    private int delay;

    @Override
    public void start(Stage primaryStage) throws Exception {
        initialMenu(primaryStage);
        primaryStage.show();
//        setMap();
    }

    private void addStyles(HBox elementBox){
        elementBox.setAlignment(Pos.CENTER);
        elementBox.setSpacing(10);
        elementBox.setMinHeight(30);
    }

    private void initialMenu(Stage stage){
        VBox menu = new VBox();
        Label label = new Label("Map properties");
        label.setFont(new Font(25));
        menu.getChildren().add(label);

        //height
        HBox elementBox = new HBox();
        Label elementLabel = new Label("Map height: ");
        TextField heightInput = new TextField();
        heightInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, heightInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Map width: ");
        TextField widthInput = new TextField();
        widthInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, widthInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Initial number of plants: ");
        TextField initialNumberOfPlantsInput = new TextField();
        initialNumberOfPlantsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, initialNumberOfPlantsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Initial number of animals: ");
        TextField initialNumberOfAnimalsInput = new TextField();
        initialNumberOfAnimalsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, initialNumberOfAnimalsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Daily spawning plants: ");
        TextField dailySpawningPlantsInput = new TextField();
        dailySpawningPlantsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, dailySpawningPlantsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);


        //ZWIERZETA
        label = new Label("Animals properties");
        label.setFont(new Font(25));
        menu.getChildren().add(label);

        elementBox = new HBox();
        elementLabel = new Label("Initial animals energy: ");
        TextField initialAnimalsEnergyInput = new TextField();
        initialAnimalsEnergyInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, initialAnimalsEnergyInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Energy from eating plants: ");
        TextField energyFromPlantsInput = new TextField();
        energyFromPlantsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, energyFromPlantsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Ready to couple energy: ");
        TextField readyToCoupleEnergyInput = new TextField();
        readyToCoupleEnergyInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, readyToCoupleEnergyInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Energy lost on copulation: ");
        TextField energyLostOnCopulationInput = new TextField();
        energyLostOnCopulationInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, energyLostOnCopulationInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Minimum number of mutations: ");
        TextField minMutationsInput = new TextField();
        minMutationsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, minMutationsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Maximum number of mutations: ");
        TextField maxMutationsInput = new TextField();
        maxMutationsInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, maxMutationsInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Genotype length: ");
        TextField genotypeLengthInput = new TextField();
        genotypeLengthInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, genotypeLengthInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        //WARIANTY
        label = new Label("World variants");
        label.setFont(new Font(25));
        menu.getChildren().add(label);

        elementBox = new HBox();
        elementLabel = new Label("Map variant - hell portal: ");
        CheckBox mapCheckBox = new CheckBox();
        elementBox.getChildren().addAll(elementLabel, mapCheckBox);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Plants spawn variant - toxic corpses: ");
        CheckBox plantsCheckBox = new CheckBox();
        elementBox.getChildren().addAll(elementLabel, plantsCheckBox);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Mutations variant - full randomness: ");
        CheckBox mutationsCheckBox = new CheckBox();
        elementBox.getChildren().addAll(elementLabel, mutationsCheckBox);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Move variant - some craziness : ");
        CheckBox moveCheckBox = new CheckBox();
        elementBox.getChildren().addAll(elementLabel, moveCheckBox);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Animation delay: ");
        TextField delayInput = new TextField();
        delayInput.setMaxWidth(100);
        elementBox.getChildren().addAll(elementLabel, delayInput);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        elementBox = new HBox();
        elementLabel = new Label("Save statistics to CSV file: ");
        CheckBox csvCheckbox = new CheckBox();
        elementBox.getChildren().addAll(elementLabel, csvCheckbox);
        addStyles(elementBox);
        menu.getChildren().add(elementBox);

        menu.setAlignment(Pos.CENTER);


        Button submitBtn = new Button("Start");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("PROBLEM!");
        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    alert.setContentText(" ");
                    height = Integer.parseInt(heightInput.getText());
                    if (height > 500 || height <= 0){
                        alert.setContentText("Too big value or smaller than 1");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate height!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    width = Integer.parseInt(widthInput.getText());
                    if (width > 500 || width <= 0){
                        alert.setContentText("Too big value or smaller than 1");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate width!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    initialNumberOfPlants = Integer.parseInt(initialNumberOfPlantsInput.getText());
                    if (initialNumberOfPlants < 0){
                        alert.setContentText("Value smaller than 0");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate initial number of plants!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    initialNumberOfAnimals = Integer.parseInt(initialNumberOfAnimalsInput.getText());
                    if (initialNumberOfAnimals < 0){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate initial number of animals!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    startEnergyForAnimal = Integer.parseInt(initialAnimalsEnergyInput.getText());
                    if (startEnergyForAnimal <= 0){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate start energy for animal!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    dailyPlants = Integer.parseInt(dailySpawningPlantsInput.getText());
                    if (dailyPlants < 0) {
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate daily spawning plants number!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    energyFromPlant = Integer.parseInt(energyFromPlantsInput.getText());
                    if (energyFromPlant < 0){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate energy from eating plants!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    readyToCoupleEnergy = Integer.parseInt(readyToCoupleEnergyInput.getText());
                    if (readyToCoupleEnergy < 0) {
                        alert.setContentText("Ready to couple energy must be greater or equal to 0");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate ready to couple energy!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    lossOnCoupleEnergy = Integer.parseInt(energyLostOnCopulationInput.getText());
                    if (readyToCoupleEnergy < lossOnCoupleEnergy) {
                        alert.setContentText("Ready to couple energy must be greater or equal" +
                                " than loss on couple energy");
                        throw new Exception();
                    } else if (lossOnCoupleEnergy < 0){
                        alert.setContentText("Loss on couple energy can't be smaller than 0");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate energy lost on copulation!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    lossOnCoupleEnergy = Integer.parseInt(energyLostOnCopulationInput.getText());
                } catch (Exception e) {
                    alert.setHeaderText("Give validate energy lost on copulation!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    minMutations = Integer.parseInt(minMutationsInput.getText());
                    if (minMutations < 0){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate minimum number of mutations!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    maxMutations = Integer.parseInt(maxMutationsInput.getText());
                    if (maxMutations < 0){
                        throw new Exception();
                    } else if (maxMutations < minMutations){
                        alert.setContentText("Max mutations can't be smaller than minimal number of mutations");
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate maximum number of mutations!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    genotypeLength = Integer.parseInt(genotypeLengthInput.getText());
                    if (genotypeLength <= 0){
                        throw new Exception();
                    }
                } catch (Exception e) {
                    alert.setHeaderText("Give validate genotype length!");
                    alert.showAndWait();
                    return;
                }

                try {
                    alert.setContentText(" ");
                    delay = Integer.parseInt(delayInput.getText());
                    if (delay < 5){
                        throw new Exception();
                    }
                } catch (Exception e){
                    alert.setHeaderText("Give validate delay!");
                    alert.showAndWait();
                    return;
                }

                preferedPlantSpawnToxic = plantsCheckBox.isSelected();
                mutationsRandomness = mutationsCheckBox.isSelected();
                isPortalEnabled = mapCheckBox.isSelected();
                moveRandomness = moveCheckBox.isSelected();
                saveToCSV = csvCheckbox.isSelected();
                setMap();
            }
        });

        menu.getChildren().add(submitBtn);
        Scene scene = new Scene(menu, 500, 700);
        stage.setScene(scene);
    }

    private void setMap() {
        MapApplication application = new MapApplication(delay, saveToCSV);
        WorldMap map = new WorldMap(
                width, height, initialNumberOfPlants, energyFromPlant,
                dailyPlants, initialNumberOfAnimals, startEnergyForAnimal, readyToCoupleEnergy,
                lossOnCoupleEnergy, minMutations, maxMutations, mutationsRandomness, genotypeLength,
                moveRandomness, isPortalEnabled, preferedPlantSpawnToxic
        );
        application.run(map);
    }

}
