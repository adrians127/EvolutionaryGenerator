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


        menu.setAlignment(Pos.CENTER);


        Label newLabel = new Label("siema");
        menu.getChildren().add(newLabel);

        Button submitBtn = new Button("Start");
        submitBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (heightInput.getText() != null){
                    newLabel.setText(heightInput.getText());
                }
            }
        });

        menu.getChildren().add(submitBtn);
        Scene scene = new Scene(menu, 500, 700);
        stage.setScene(scene);
    }

    private VBox mapProperties(){
        VBox box = new VBox();

        //height
        HBox elementBox = new HBox();
        Label label = new Label("Map height: ");
        TextField heightInput = new TextField();
        heightInput.setMaxWidth(100);
        elementBox.getChildren().addAll(label, heightInput);
        box.getChildren().add(elementBox);
        return box;
    }

    private void setMap() {
        MapApplication application = new MapApplication();
        WorldMap map = new WorldMap();
        application.run(map);
    }

}
