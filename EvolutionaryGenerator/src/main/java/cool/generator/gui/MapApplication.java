package cool.generator.gui;

import com.opencsv.CSVWriter;
import cool.generator.Animal;
import cool.generator.Coordinates;
import cool.generator.WorldMap;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class MapApplication {
    Stage mapStage = new Stage();
    final int widthRes = 1366;
    final int heightRes = 768;
    final int delay;
    Animal followedAnimal = null;

    WorldMap map;

    Button startBtn;

    Button stopBtn;

    CSVWriter csvWriter;
    private final boolean saveToCSV;

    {
        try {
            csvWriter = new CSVWriter(new FileWriter("output.csv"));
        } catch (IOException e) {
            System.out.println("PROBLEM Z CSV!");
        }
    }

    public MapApplication(int delay, boolean saveToCSV) {
        this.delay = delay;
        this.saveToCSV = saveToCSV;
    }

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
                mapStage.setOnCloseRequest(event -> {
                    mapStage.close();
                    stop();
                });
                stopBtn.setOnAction(event -> stop());
                startBtn.setOnAction(event -> start());
            }
        }.start();

    }

    void show() {

        GridPane grid = new GridPane();
        drawGrid(grid);

        HBox hbox = new HBox();
        hbox.getChildren().addAll(drawStats(), grid);

        Scene scene = new Scene(hbox, widthRes, heightRes);
        mapStage.setScene(scene);
        mapStage.show();
    }

    void setStyles(Label label, VBox box){
        label.setFont(new Font(15));
        box.getChildren().add(label);
    }

    VBox drawStats() {
        ArrayList<Integer> value;
        if (!map.popularGenotypes.isEmpty()) value = Collections.max(map.popularGenotypes.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey();
        else value = null;

        if (this.saveToCSV){
            ArrayList<String[]> info = new ArrayList<>();
            String[] line = {"Dzien symulacji: ",  String.valueOf(map.getSimulationDays()),
                    "Liczba roslin: ", String.valueOf(map.plants.size()),
                    "Liczba zwierzat: ", String.valueOf(map.aliveAnimalsNumber),
                    "Liczba wolnych pol : ", String.valueOf(map.emptyFields),
                    "Najpopularniejszy genotyp: ", String.valueOf(value),
                    "Sredni poziom energii zyjacych: ", String.format("%.2f",map.getAverageAliveEnergy()),
                    "Srednia dlugosc zycia martwych: ", String.format("%.2f",map.getAverageDeadEnergy())
            };
            csvWriter.writeNext(line);

            try {
                csvWriter.flush();
            } catch (IOException e) {
                System.out.println("ZAPIS PROBLEM");
            }
        }

        VBox box = new VBox();
        box.setPrefWidth(366);

        Label label = new Label("Parametry symulacji");
        label.setFont(new Font(20));
        box.getChildren().add(label);

        //dzień symulacji
        label = new Label("Dzien symulacji: " + map.getSimulationDays());
        setStyles(label, box);

        //liczba zwierząt i liczba roślin - wykres od dnia symulacji
        label = new Label("Liczba roslin: " + map.plants.size());
        setStyles(label, box);

        label = new Label("Liczba zwierzat: " + map.aliveAnimalsNumber);
        setStyles(label, box);

        //liczba wolnych pól
        label = new Label("Liczba wolnych pol : " + map.emptyFields);
        setStyles(label, box);

        //najpopularniejsze genotypy - 3 np
        label = new Label("Najpopularniejszy genotyp: " + value);
        setStyles(label, box);

        //średni poziom energii żyjących
        label = new Label("Sredni poziom energii zyjacych: "
                        + String.format("%.2f",map.getAverageAliveEnergy()));
        setStyles(label, box);

        //średnia długość życia martwych - od początku symulacji
        label = new Label("Srednia dlugosc zycia martwych: " +
                String.format("%.2f",map.getAverageDeadEnergy()));
        setStyles(label, box);

        HBox hBox = new HBox();

        startBtn = new Button("START");
        startBtn.setStyle("-fx-background-color: green; -fx-padding: 20px;" +
                "-fx-text-fill: white");

        stopBtn = new Button("STOP");
        stopBtn.setStyle("-fx-background-color: red; -fx-padding: 20px;" +
                "-fx-text-fill: white");

        hBox.getChildren().addAll(startBtn, stopBtn);
        hBox.setSpacing(20);

        box.getChildren().add(hBox);

        if (followedAnimal != null){
            box.getChildren().add(animalStatistics());
        }
        box.setStyle("-fx-padding: 20px");
        return box;

    }

    private VBox animalStatistics(){
        VBox vBox = new VBox();
        Label label = new Label("Statystyki wybranego zwierzecia");
        label.setFont(new Font(20));
        vBox.getChildren().add(label);

        Label elementLabel = new Label("Pozycja: " + followedAnimal.getPosition());
        setStyles(elementLabel, vBox);

        elementLabel = new Label("Genotyp: " + followedAnimal.getGenotype());
        setStyles(elementLabel, vBox);

        elementLabel = new Label("Indeks aktywnego genu: " + followedAnimal.getActiveGeneIndex());
        setStyles(elementLabel, vBox);

        elementLabel = new Label("Energia: " + followedAnimal.getEnergy());
        setStyles(elementLabel, vBox);

        elementLabel = new Label("Zjedzone rosliny: " + followedAnimal.getPlantsEaten());
        setStyles(elementLabel, vBox);

        elementLabel = new Label("Stworzone dzieci: " + followedAnimal.getChildrenCreated());
        setStyles(elementLabel, vBox);

        if (followedAnimal.isAlive()){
            elementLabel = new Label("Wiek w dniach: " + followedAnimal.getAliveFor());
            setStyles(elementLabel, vBox);
        }
        else {
            elementLabel = new Label("Dzien smierci: " + followedAnimal.getDayOfDeath());
            setStyles(elementLabel, vBox);
        }

        return vBox;
    }
    private void setFollowedAnimal(Coordinates position){
        followedAnimal = map.animalsAt(position).get(0);
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
                    stackPane.setStyle("-fx-background-color: rgba(146,208,144,0.73);");
                    grid.add(stackPane, x, y);
                }
                if (map.plantAt(position) != null) {
                    GuiElementBox photoBox = new GuiElementBox(boxWidth, boxHeight);
                    grid.add(photoBox.hBox, x, y);
                }
                if (map.animalsAt(position) != null) {
                    Button animalBtn = new Button();
                    animalBtn.setMinHeight(boxHeight-1);
                    animalBtn.setMinWidth(boxWidth-1);
                    animalBtn.setOpacity(0);
                    int finalX = x;
                    int finalY = y;
                    animalBtn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            setFollowedAnimal(new Coordinates(finalX, finalY));
                        }
                    });

                    GuiElementBox photoBox = new GuiElementBox(map.animalsAt(position), boxWidth, boxHeight,
                            map.getReadyToCoupleEnergy());

                    StackPane stackPane = new StackPane();

                    if (followedAnimal != null && followedAnimal.getPosition().equals(position) && followedAnimal.isAlive()){
                        stackPane.setStyle("-fx-background-color: rgba(255,2,2,0.5);");
                    }
                    stackPane.getChildren().addAll(photoBox.hBox, animalBtn);
                    grid.add(stackPane, x, y);
                }
            }
        }
        grid.setGridLinesVisible(true);
    }
}
