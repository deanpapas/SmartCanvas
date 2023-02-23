/*
 * Warning Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import java.io.FileNotFoundException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * The Warning Controller class defines the back end functions for the Text Options View
 * and its
 * related elements
 */

public class WarningController {

    @FXML
    private Label errormessage;

    Stage stage;
    Stage parentStage;
    String errorString;

    public WarningController(Stage parentStage, String errorString) {
        this.stage = new Stage();
        this.errorString = errorString;
        this.parentStage = parentStage;
    }

    public void initialize() {
        errormessage.setText(errorString);
    }

    // Show Stage
    public void showStage(Pane root) throws SQLException, FileNotFoundException {
        Scene scene = new Scene(root, 300, 100);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Warning");
        stage.setAlwaysOnTop(true);
        stage.show();
    }
}
