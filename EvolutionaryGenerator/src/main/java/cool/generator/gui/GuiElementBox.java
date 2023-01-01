package cool.generator.gui;

import cool.generator.Animal;
import cool.generator.Plant;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class GuiElementBox {
    HBox hBox = new HBox();
    ProgressBar energyBar;
    Label label;



    public GuiElementBox(Plant element, int width, int height){
        try {
            Image image = new Image(new FileInputStream(element.getImageSrc()));
            ImageView imageView = new ImageView(image);

            //wymiary obrazka
            imageView.setFitHeight(height-1);
            imageView.setFitWidth(width-1);
            hBox.getChildren().addAll(imageView);
            hBox.setAlignment(Pos.CENTER);
        } catch (FileNotFoundException e) { //jeśli obrazek nie istnieje
            throw new RuntimeException("Source file not found");
        }
    }

    public GuiElementBox(ArrayList<Animal> elements, int width, int height, int maxEnergy){
        try {
            Image image = new Image(new FileInputStream(elements.get(0).getImageSrc()));
            ImageView imageView = new ImageView(image);
            label = new Label(String.valueOf(elements.size()));
            //wymiary obrazka
            imageView.setFitHeight(height*0.7);
            imageView.setFitWidth(width*0.7);

            energyBar = new ProgressBar();
            int energy = elements.get(0).getEnergy();
            double progress = energy <= maxEnergy ? (double) energy / maxEnergy*100.0 : 100.0;
            energyBar.setProgress(progress / 100.0);
            energyBar.setStyle("-fx-padding: 0;");

            VBox vBox = new VBox();
            vBox.getChildren().addAll(imageView, energyBar);

            hBox.getChildren().addAll(vBox, label);
            hBox.setAlignment(Pos.CENTER);

        } catch (FileNotFoundException e) { //jeśli obrazek nie istnieje
            throw new RuntimeException("Source file not found");
        }
    }
}
