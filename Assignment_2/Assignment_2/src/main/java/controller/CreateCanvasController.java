/*
 * Create Canvas Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * The CreateCanvasController class defines the back end functions for the Canvas Options View and its
 * related elements
 */

public class CreateCanvasController {

    @FXML
    private Button cancel;
    @FXML
    private TextField height;
    @FXML
    private Label message;
    @FXML
    private Button ok;
    @FXML
    private TextField width;

    private Stage stage;
    private Stage parentStage;
    private Pane canvas;

    public CreateCanvasController(Stage parentStage, Pane canvas) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.canvas = canvas;
    }

    @FXML
    public void initialize() {

        // Create User Defined Canvas
        ok.setOnAction(event -> {

            if (canvas.isVisible()) { // Check Canvas
                message.setText("Delete Current Canvas!");
                message.setTextFill(Color.RED);
            } else {

                try { 
                    int userwidth = Integer.parseInt(width.getText()); // Get Canvas Dimensions
                    int userheight = Integer.parseInt(height.getText());

                    if (userwidth >= 750) {
                        canvas.setPrefWidth(750);

                        if (userheight >= 590) {
                            canvas.setPrefHeight(590);
                        } else {
                            canvas.setPrefHeight(userheight);
                        }

                    } else {

                        canvas.setPrefWidth(userwidth);

                        if (userheight >= 590) {
                            canvas.setPrefHeight(590);
                        } else {
                            canvas.setPrefHeight(userheight);
                        }
                    }

                    canvas.setVisible(true);
                    stage.close();
                } catch (NumberFormatException e) {
                    message.setText("Unvalid Width or Height!");
                    message.setTextFill(Color.RED);
                }

            }

        });

        // Exit
        cancel.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

    }

    // Show Stage
    public void showStage(Pane root) {
        Scene scene = new Scene(root, 240, 150);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Create New Canvas");
        stage.setAlwaysOnTop(true);
        stage.show();
    }

}
