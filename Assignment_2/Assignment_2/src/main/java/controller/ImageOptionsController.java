/*
 * Image Options Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/*
 * The ImageOptionsController class defines the back end functions for the Image Options View and its
 * related elements
 */

public class ImageOptionsController {

    Stage stage;
    ImageView image;
    Circle sizehandle;
    Circle movehandle;
    Pane canvas;
    Pane optionpane;
    Label anglelabel;
    File newimage;

    public ImageOptionsController(ImageView image, Circle sizehandle, Circle movehandle, Pane canvas, Pane optionpane,
            Label anglelabel) {
        this.stage = new Stage();
        this.image = image;
        this.sizehandle = sizehandle;
        this.movehandle = movehandle;
        this.canvas = canvas;
        this.optionpane = optionpane;
        this.anglelabel = anglelabel;
    }

    @FXML
    private Button changeppath;
    @FXML
    private Button rotate;
    @FXML
    private Slider rotateslider;
    @FXML
    private Button delete;

    public void initialize() {

        // Set Slider Value
        rotateslider.setValue(image.getRotate());

        // Change Image
        changeppath.setOnAction(event -> {
            try {
                FileChooser filechooser = new FileChooser();// Create File chooser
                filechooser.getExtensionFilters()
                        .add(new ExtensionFilter("Image Files", "*jpg", "*png", "*gif", "*jpeg"));
                filechooser.setTitle("Select Profile Picture");// Name File Chooser Window
                newimage = filechooser.showOpenDialog(stage);// Opening filechooser for user
                image.setImage(new Image(newimage.getAbsolutePath(), 219, 204, false, false));
            } catch (NullPointerException e) {
            }
        });

        // Rotate
        rotate.setOnAction(event -> {
            Double angle = image.getRotate();
            angle = angle + 90;
            rotateslider.setValue(angle);
            image.setRotate(angle);
            anglelabel.setText((String.valueOf(Math.round(angle))));
            relocateHandle();
        });

        rotateslider.valueProperty().addListener((ov, oldValue, newValue) -> {
            image.setRotate(newValue.intValue());
            anglelabel.setText((String.valueOf(Math.round((Double) newValue))));
            relocateHandle();
        });

        // Delete
        delete.setOnAction(event -> {
            canvas.getChildren().remove(image);
            canvas.getChildren().remove(sizehandle);
            canvas.getChildren().remove(movehandle);
            optionpane.getChildren().clear();
        });
    }

    // Relocate
    public void relocateHandle() {

        sizehandle.relocate(
                image.getBoundsInParent().getMinX() + image.getFitWidth() - sizehandle.getRadius(),
                image.getBoundsInParent().getMinY() + image.getFitHeight() - sizehandle.getRadius());
        movehandle.relocate(
                image.getBoundsInParent().getCenterX(),
                image.getBoundsInParent().getMaxY() + movehandle.getRadius() * 2);
    }

}