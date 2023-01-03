package cool.generator.gui;

import cool.generator.Animal;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GuiElementBox {
    HBox hBox = new HBox();
    ProgressBar energyBar;
    Label label;

    public GuiElementBox(int width, int height){
        int min = Math.min(width, height);
        Circle circle = new Circle((min/2)-1, Color.GREEN);
        hBox.getChildren().add(circle);
        hBox.setAlignment(Pos.CENTER);
    }

    public GuiElementBox(ArrayList<Animal> elements, int width, int height, int maxEnergy){
        int min = Math.min(height, width);
        int energy = elements.get(0).getEnergy();
        int numberOfAnimals = elements.size();

        if (width >= 50 && height >= 50) {
            try {
                Image image = new Image(new FileInputStream(elements.get(0).getImageSrc()));
                ImageView imageView = new ImageView(image);

                //wymiary obrazka
                imageView.setFitHeight(min * 0.7);
                imageView.setFitWidth(min * 0.7);

                energyBar = new ProgressBar();
                double progress = energy <= maxEnergy ? (double) energy / maxEnergy * 100.0 : 100.0;
                energyBar.setProgress(progress / 100.0);
                energyBar.setStyle("-fx-padding: 0;");

                VBox vBox = new VBox();
                vBox.getChildren().addAll(imageView, energyBar);

                hBox.getChildren().add(vBox);
                label = new Label(String.valueOf(numberOfAnimals));
                label.setFont(new Font(15));
                hBox.getChildren().add(label);

            } catch (FileNotFoundException e) { //jeśli obrazek nie istnieje
                throw new RuntimeException("Source file not found");
            }
        } else {
            if (energy < maxEnergy){
                Circle circle = new Circle((min/2)-1, Color.rgb(129,100,100));
                hBox.getChildren().add(circle);
            }
            else {
                Circle circle = new Circle((min/2)-1, Color.rgb(89,30,30));
                hBox.getChildren().add(circle);
            }

            if (numberOfAnimals > 1){
                label = new Label(String.valueOf(elements.size()));
                hBox.getChildren().add(label);
            }
        }
        hBox.setAlignment(Pos.CENTER);
    }
}
