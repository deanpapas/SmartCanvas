/*
 * Shape Options Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class ShapeOptionsController {

    Shape shape;
    Circle sizehandle;
    Circle movehandle;
    Pane canvas;
    Pane optionpane;
    Label anglelabel;

    /*
     * The ShapeOptionsController class defines the back end functions for the Shape Options View
     * and its
     * related elements
     */

    public ShapeOptionsController(Shape shape, Circle sizehandle, Circle movehandle, Pane canvas, Pane optionpane,
            Label anglelabel) {
        this.shape = shape;
        this.sizehandle = sizehandle;
        this.movehandle = movehandle;
        this.canvas = canvas;
        this.optionpane = optionpane;
        this.anglelabel = anglelabel;
    }

    @FXML
    private ColorPicker backgroundcolorpicker;
    @FXML
    private ColorPicker bordercolorpicker;
    @FXML
    private TextField borderwidth;
    @FXML
    private Button rotate;
    @FXML
    private Slider rotateslider;
    @FXML
    private Button delete;

    public void initialize() {

        // Prepare Panel
        preparepanel();

        // Prepare Background Colorpicker
        backgroundcolorpicker.setOnAction(event -> {
            shape.setFill(backgroundcolorpicker.getValue());
        });
        
        // Prepare Border Width
        borderwidth.setOnAction(event -> {
            shape.setStrokeWidth(Double.parseDouble(borderwidth.getText()));
        });

        // Prepare Border Colorpicker
        bordercolorpicker.setOnAction(event -> {
            shape.setStroke(bordercolorpicker.getValue());
        });

        // Rotate
        rotate.setOnAction(event -> {

            Double angle = shape.getRotate();
            angle = angle + 90;
            rotateslider.setValue(angle);
            shape.setRotate(angle);
            anglelabel.setText((String.valueOf(Math.round(angle))));
            relocateHandle();

        });

        rotateslider.valueProperty().addListener((ov, oldValue, newValue) -> {

            shape.setRotate(newValue.intValue());
            anglelabel.setText((String.valueOf(Math.round((Double) newValue))));
            relocateHandle();

        });

        // Delete
        delete.setOnAction(event -> {
            canvas.getChildren().remove(shape);
            canvas.getChildren().remove(sizehandle);
            canvas.getChildren().remove(movehandle);
            optionpane.getChildren().clear();
        });

    }

    // Relocate
    public void relocateHandle() {
        if (shape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) shape;
            sizehandle.relocate(
                    rectangle.getBoundsInParent().getMaxX() - sizehandle.getRadius(),
                    rectangle.getBoundsInParent().getMaxY() - sizehandle.getRadius());
            movehandle.relocate(
                    rectangle.getBoundsInParent().getCenterX() - movehandle.getRadius(),
                    rectangle.getBoundsInParent().getMaxY() + movehandle.getRadius() * 2);
        }
    }

    // Prepare Option Panel
    public void preparepanel() {
        bordercolorpicker.setValue((Color) shape.getStroke());
        borderwidth.setText(Double.toString(shape.getStrokeWidth()));
        backgroundcolorpicker.setValue((Color) shape.getFill());
        rotateslider.setValue(shape.getRotate());
    }

}
