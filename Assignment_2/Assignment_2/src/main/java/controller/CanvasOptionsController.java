/*
 * Canvas Options Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/*
 * The CanvasOptionsController class defines the back end functions for the Canvas Options View and its
 * related elements
 */

public class CanvasOptionsController {

    @FXML
    private ColorPicker colorpicker;
    @FXML
    private Label lab;

    private Pane canvas;

    public CanvasOptionsController(Pane canvas) {
        this.canvas = canvas;
    }

    @FXML
    public void initialize() {

        colorpicker.setOnAction(event -> {
            Color color = colorpicker.getValue(); //Get chosen color
            canvas.setBackground(new Background(new BackgroundFill(color, null, null))); //Set Chosen Color
        });

    }

}
