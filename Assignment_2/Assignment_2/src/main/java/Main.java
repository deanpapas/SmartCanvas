/*
 * Main
 * 
 * V1.0
 *
 * 30/05/2022
 */

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;

import model.Model;
import controller.LoginController;

/*
 * The main class used to access the Smart Canvas application
 */

public class Main extends Application {
	private Model model;

	//Initialize New Model
	@Override
	public void init() {
		model = new Model();
	}

	// Load Login View
	@Override
	public void start(Stage primaryStage) {
		try {
			model.setup();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
			
			// Customize controller instance
			Callback<Class<?>, Object> controllerFactory = param -> {
				return new LoginController(primaryStage, model);
			};
			
			loader.setControllerFactory(controllerFactory);
			VBox root = loader.load();

			LoginController loginController = loader.getController();
			loginController.showStage(root);
		} catch (IOException | SQLException | RuntimeException e) {
			Scene scene = new Scene(new Label(e.getMessage()), 200, 100);
			primaryStage.setTitle("Error");
			primaryStage.setScene(scene);
			primaryStage.show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
