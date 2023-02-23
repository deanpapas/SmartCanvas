/*
 * Login Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;
import model.User;

/*
 * The LoginController class defines the back end functions for the Login View and its
 * related elements
 */

public class LoginController {

	@FXML
	private Button login;
	@FXML
	private ImageView logo;
	@FXML
	private Label message;
	@FXML
	private PasswordField password;
	@FXML
	private Button signup;
	@FXML
	private TextField username;

	private Model model;
	private Stage stage;

	public LoginController(Stage stage, Model model) {
		this.stage = stage;
		this.model = model;
	}

	@FXML
	public void initialize() {

		// Login User % Load Canvas
		login.setOnAction(event -> {
			if (!username.getText().isEmpty() && !password.getText().isEmpty()) {

				// Check Password by hashing user input
				String generatedpassword = hashCheck(password.getText());

				User user;
				try {
					user = model.getUserDao().getUser(username.getText(), generatedpassword);
					if (user != null) {
						model.setCurrentUser(user);
						message.setText("Login success for " + user.getUsername());
						message.setTextFill(Color.GREEN);

						FXMLLoader loader = new FXMLLoader(getClass().getResource("/CanvasView.fxml"));

						// Customize controller instance
						Callback<Class<?>, Object> controllerFactory = param -> {
							return new CanvasController(stage, model);
						};

						loader.setControllerFactory(controllerFactory);
						AnchorPane root = loader.load();

						CanvasController canvasController = loader.getController();
						canvasController.showStage(root);

						message.setText("");
						username.clear();
						password.clear();

						stage.close();

					} else {
						message.setText("Wrong username or password");
						message.setTextFill(Color.RED);
					}
				} catch (SQLException | NullPointerException e) {
					System.out.println(e);
					message.setText(e.getMessage());
					message.setTextFill(Color.RED);
				} catch (IOException e) {
					message.setText(e.getMessage());
					message.setTextFill(Color.RED);
				}
			} else {
				message.setText("Empty username or password");
				message.setTextFill(Color.RED);
			}
			username.clear();
			password.clear();
		});

		// Load Signup
		signup.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/SignupView.fxml"));

				// Customize controller instance
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new SignupController(stage, model);
				};

				loader.setControllerFactory(controllerFactory);
				VBox root = loader.load();

				SignupController signupController = loader.getController();
				signupController.showStage(root);

				message.setText("");
				username.clear();
				password.clear();

				stage.close();
			} catch (IOException e) {
				message.setText(e.getMessage());
			}
		});
	}

	// Show Stage
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 400, 600);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Smart Canvas");
		stage.show();
	}

	// Hash Password
	public String hashCheck(String passwordToHash) {
		String generatedPassword = null;
		try {
			// Create MD5 MessageDigest
			MessageDigest md = MessageDigest.getInstance("MD5");
			// Convert Password to Bytes
			md.update(passwordToHash.getBytes());
			// Make Byte Array
			byte[] bytes = md.digest();

			// Convert Byte Array to Hexadecimals
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}

			// Generate Hashed Password
			generatedPassword = sb.toString();
		} catch (NoSuchAlgorithmException e) {
			message.setText(e.toString());
		}
		return generatedPassword;
	}
}
