/*
 * About Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * The AboutController class defines the back end functions for the About View
 */
public class AboutController {

    @FXML
    private Stage parentstage;
    @FXML
    private Stage stage;

    public AboutController(Stage parentstage) {
        this.stage = new Stage();
        this.parentstage = parentstage;
    }

    // Show Stage
    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 700);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("About");
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
