package cool.generator.gui;

import cool.generator.WorldMap;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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

    private int delay;

    @Override
    public void start(Stage primaryStage) {
        loadFromFile(1);
        initialMenu(primaryStage);
        primaryStage.show();
//        setMap();
    }

    private void loadFromFile(int number) {
        try {
            File myFile = switch (number) {
                case 1 ->
                        new File("C:\\Users\\aleks\\OneDrive\\Pulpit\\Studia\\SEMESTR 3\\programowanie_obiektowe\\EvolutionaryGenerator\\EvolutionaryGenerator\\src\\main\\resources\\configs\\firstConfig.txt");
                case 2 ->
                        new File("C:\\Users\\aleks\\OneDrive\\Pulpit\\Studia\\SEMESTR 3\\programowanie_obiektowe\\EvolutionaryGenerator\\EvolutionaryGenerator\\src\\main\\resources\\configs\\secondConfig.txt");
                case 3 ->
                        new File("C:\\Users\\aleks\\OneDrive\\Pulpit\\Studia\\SEMESTR 3\\programowanie_obiektowe\\EvolutionaryGenerator\\EvolutionaryGenerator\\src\\main\\resources\\configs\\thirdConfig.txt");
                default -> throw new FileNotFoundException();
            };
            Scanner scanner = new Scanner(myFile);
            ArrayList<Integer> list = new ArrayList<>();
            while (scanner.hasNext()){
                scanner.next();
                list.add(Integer.valueOf(scanner.next()));
            }
            this.width = list.get(0);
            this.height = list.get(1);
            this.energyFromPlant = list.get(2);
            this.dailyPlants = list.get(3);
            this.genotypeLength = list.get(4);
            this.startEnergyForAnimal = list.get(5);
            this.readyToCoupleEnergy = list.get(6);
            this.lossOnCoupleEnergy = list.get(7);
            this.minMutations = list.get(8);
            this.maxMutations = list.get(9);
            this.initialNumberOfPlants = list.get(10);
            this.initialNumberOfAnimals = list.get(11);
            this.delay = list.get(12);
            this.isPortalEnabled = list.get(13) > 0;
            this.preferedPlantSpawnToxic = list.get(14) > 0;
            this.mutationsRandomness = list.get(15) > 0;
            this.moveRandomness = list.get(16) > 0;
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("ERROR WITH FILE");
            alert.setContentText("CAN T LOAD FILE");
            alert.showAndWait();
        }

    }

    private void loadValuesIntoMenu(TextField heightInput, TextField widthInput, TextField energyFromPlantsInput,
                                    TextField dailySpawningPlantsInput, TextField genotypeLengthInput,
                                    TextField initialAnimalsEnergyInput, TextField readyToCoupleEnergyInput,
                                    TextField energyLostOnCopulationInput, TextField minMutationsInput,
                                    TextField maxMutationsInput, TextField initialNumberOfPlantsInput,
                                    TextField initialNumberOfAnimalsInput, TextField delayInput, CheckBox mapCheckBox,
                                    CheckBox moveCheckBox, CheckBox plantsCheckBox, CheckBox mutationsCheckBox){
        heightInput.setText(String.valueOf(height));
        widthInput.setText(String.valueOf(width));
        energyFromPlantsInput.setText(String.valueOf(energyFromPlant));
        dailySpawningPlantsInput.setText(String.valueOf(dailyPlants));
        genotypeLengthInput.setText(String.valueOf(genotypeLength));
        initialAnimalsEnergyInput.setText(String.valueOf(startEnergyForAnimal));
        readyToCoupleEnergyInput.setText(String.valueOf(readyToCoupleEnergy));
        energyLostOnCopulationInput.setText(String.valueOf(lossOnCoupleEnergy));
        minMutationsInput.setText(String.valueOf(minMutations));
        maxMutationsInput.setText(String.valueOf(maxMutations));
        initialNumberOfPlantsInput.setText(String.valueOf(initialNumberOfPlants));
        initialNumberOfAnimalsInput.setText(String.valueOf(initialNumberOfAnimals));
        delayInput.setText(String.valueOf(delay));
        mapCheckBox.setSelected(isPortalEnabled);
        moveCheckBox.setSelected(moveRandomness);
        plantsCheckBox.setSelected(preferedPlantSpawnToxic);
        mutationsCheckBox.setSelected(mutationsRandomness);
    }

    private void addStyles(HBox elementBox) {
        elementBox.setAlignment(Pos.CENTER);
        elementBox.setSpacing(10);
        elementBox.setMinHeight(30);
    }

    private void initialMenu(Stage stage) {
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

        elementBox = new HBox();
        elementLabel = new Label("Choose a premade configuration: ");
        Spinner<Integer> spinner = new Spinner<>(1,3,1);
        spinner.setMaxWidth(50);

        loadValuesIntoMenu(heightInput, widthInput, energyFromPlantsInput, dailySpawningPlantsInput, genotypeLengthInput,
                initialAnimalsEnergyInput, readyToCoupleEnergyInput, energyLostOnCopulationInput,
                minMutationsInput, maxMutationsInput, initialNumberOfPlantsInput, initialNumberOfAnimalsInput,
                delayInput, mapCheckBox, moveCheckBox, plantsCheckBox, mutationsCheckBox);

        spinner.valueProperty().addListener((observable, oldValue, newValue) -> {
            int configuration = newValue;
            loadFromFile(configuration);
            loadValuesIntoMenu(heightInput, widthInput, energyFromPlantsInput, dailySpawningPlantsInput, genotypeLengthInput,
                    initialAnimalsEnergyInput, readyToCoupleEnergyInput, energyLostOnCopulationInput,
                    minMutationsInput, maxMutationsInput, initialNumberOfPlantsInput, initialNumberOfAnimalsInput,
                    delayInput, mapCheckBox, moveCheckBox, plantsCheckBox, mutationsCheckBox);
        });

        elementBox.getChildren().addAll(elementLabel, spinner);
        elementBox.setAlignment(Pos.CENTER);
        menu.getChildren().add(elementBox);

        menu.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("Start");
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("PROBLEM!");

        submitBtn.setOnAction(event -> {
            try {
                alert.setContentText(" ");
                height = Integer.parseInt(heightInput.getText());
                if (height > 500 || height <= 0) {
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
                if (width > 500 || width <= 0) {
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
                if (initialNumberOfPlants < 0) {
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
                if (initialNumberOfAnimals < 0) {
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
                if (startEnergyForAnimal <= 0) {
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
                if (energyFromPlant < 0) {
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
                } else if (lossOnCoupleEnergy < 0) {
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
                if (minMutations < 0) {
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
                if (maxMutations < 0) {
                    throw new Exception();
                } else if (maxMutations < minMutations) {
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
                if (genotypeLength <= 0) {
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
                if (delay < 5) {
                    throw new Exception();
                }
            } catch (Exception e) {
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
        });

        menu.getChildren().add(submitBtn);
        Scene scene = new Scene(menu, 500, 768);
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
