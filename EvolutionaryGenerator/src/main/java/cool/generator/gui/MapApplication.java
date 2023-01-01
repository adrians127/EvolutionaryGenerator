package cool.generator.gui;

import cool.generator.Coordinates;
import cool.generator.WorldMap;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MapApplication {
    Stage mapStage = new Stage();
    final int widthRes = 1366;
    final int heightRes = 768;
    final int delay = 20;

    WorldMap map;

//    void init(){
//        mapStage.show();
//    }

    void run(WorldMap map) {
        this.map = map;

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                map.cycle();
                show();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }.start();

    }

    void show() {

        GridPane grid = new GridPane();
        drawGrid(grid);

        VBox leftBox = new VBox();
        drawStats(leftBox);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(leftBox, grid);

        Scene scene = new Scene(hbox, widthRes, heightRes);
        mapStage.setScene(scene);
        mapStage.show();
    }

    void drawStats(VBox box) {
        box.setPrefWidth(366);

        Label label = new Label("siemano");
        //dzień symulacji
        Label labelDays = new Label("Dzien symulacji: " + map.getSimulationDays());

        //liczba zwierząt i liczba roślin - wykres od dnia symulacji
        Label labelPlants = new Label("Liczba roslin: " + map.plants.size());

        Label labelAnimals = new Label("Liczba zwierzat: " + map.aliveAnimalsNumber);

        //liczba wolnych pól
        Label labelFields = new Label("Liczba wolnych pol : " + map.emptyFields);

        //najpopularniejsze genotypy - 3 np
        Label labelGenotypes = new Label("Najpopularniejsze genotypy: ");

        //średni poziom energii żyjących
        Label labelLifeDuration = new Label("Sredni poziom energii zyjacych: ");

        //średnia długość życia martwych - od początku symulacji
        Label labelDead = new Label("Srednia dlugość zycia martwych: ");


        box.getChildren().addAll(label, labelDays, labelPlants, labelAnimals,
                labelFields, labelGenotypes, labelLifeDuration, labelDead);
    }

    void drawGrid(GridPane grid) {

        int maxGridWidth = widthRes - 366;
        int maxGridHeight = heightRes;

        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();
        grid.getChildren().clear();

        int boxWidth = maxGridWidth / map.width;
        int boxHeight = maxGridHeight / map.height;

        for (int x = 0; x < map.width; x++) {
            grid.getColumnConstraints().add(new ColumnConstraints(boxWidth));
        }

        for (int y = 0; y < map.height; y++) {
            grid.getRowConstraints().add(new RowConstraints(boxHeight));
        }
        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                Coordinates position = new Coordinates(x, y);
                if (map.mapForPlants.get(position)) {
                    StackPane stackPane = new StackPane();
                    stackPane.setStyle("-fx-background-color: rgba(75,0,0,0.27);");
                    grid.add(stackPane, x, y);
                }
                if (map.plantAt(position) != null) {
                    GuiElementBox photoBox = new GuiElementBox(map.plantAt(position), boxWidth, boxHeight);
                    grid.add(photoBox.hBox, x, y);
                }
                if (map.animalsAt(position) != null) {
                    GuiElementBox photoBox = new GuiElementBox(map.animalsAt(position), boxWidth, boxHeight, map.getReadyToCoupleEnergy());
                    grid.add(photoBox.hBox, x, y);
                }
            }
        }
        grid.setGridLinesVisible(true);
    }
}
