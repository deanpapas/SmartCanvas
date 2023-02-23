/*
 * Signup Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Model;
import model.User;

/*
 * The SignupController class defines the back end functions for the Signup View
 * and its
 * related elements
 */

public class SignupController {

	@FXML
	private Button close;
	@FXML
	private Button createUser;
	@FXML
	private TextField firstname;
	@FXML
	private Label imagepath;
	@FXML
	private TextField lastname;
	@FXML
	private PasswordField password;
	@FXML
	private ImageView profilepic;
	@FXML
	private Label status;
	@FXML
	private TextField username;

	private Stage stage;
	private Stage parentStage;
	private Model model;
	// Default Profile Pic
	private File chosenprofilepic = new File("./src/main/resources/view/images/defaultProfilePhoto.jpeg");

	public SignupController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}

	@FXML
	public void initialize() {

		// Create User
		createUser.setOnAction(event -> {
			if (!username.getText().isEmpty() && !password.getText().isEmpty()) {

				// Hashing Password
				String generatedpassword = hash(password.getText());

				// Write User to Database
				User user;
				try {

					String filename = chosenprofilepic.getName();
					File fileoutput = new File("./src/main/resources/view/images/" + filename);// naming file
					BufferedImage bufferedimage = SwingFXUtils.fromFXImage(profilepic.getImage(), null);
					ImageIO.write(bufferedimage, "jpeg", fileoutput);

					user = model.getUserDao().createUser(username.getText(), generatedpassword, firstname.getText(),
							lastname.getText(), fileoutput.toString());
					if (user != null) {
						status.setText("Created " + user.getUsername());
						status.setTextFill(Color.GREEN);
					} else {
						status.setText("Cannot create user");
						status.setTextFill(Color.RED);
					}
				} catch (SQLException e) {
					status.setText("Username already taken!");
					status.setTextFill(Color.RED);
				} catch (IOException e) {
					status.setText(e.toString());
				}

			} else {
				status.setText("Empty username or password");
				status.setTextFill(Color.RED);
			}
		});

		// Set Profile Picture
		profilepic.setOnMouseClicked(event -> {

			try {
				FileChooser filechooser = new FileChooser();// Create File chooser
				filechooser.getExtensionFilters()
						.add(new ExtensionFilter("Image Files", "*jpg", "*png", "*gif", "*jpeg"));
				filechooser.setTitle("Select Profile Picture");// Name File Chooser Window
				chosenprofilepic = filechooser.showOpenDialog(stage);// Opening filechooser for user
				profilepic.setImage(new Image(chosenprofilepic.getAbsolutePath(), 219, 204, false, false));
			} catch (NullPointerException e) {
			}

		});

		// Close
		close.setOnAction(event -> {
			stage.close();
			parentStage.show();
		});
	}

	// Show Stage
	public void showStage(Pane root) {
		Scene scene = new Scene(root, 400, 600);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Sign Up");
		stage.show();
	}

	// Check Password With Hash
	public String hash(String passwordToHash) {
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
			status.setText(e.toString());
		}
		return generatedPassword;
	}

}
