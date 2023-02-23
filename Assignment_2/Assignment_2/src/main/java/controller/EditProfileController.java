/*
 * Edit Profile Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.FileChooser.ExtensionFilter;
import model.Model;

/*
 * The EditProfileController class defines the back end functions for the Edit Profile View and its
 * related elements
 */

public class EditProfileController {

    @FXML
    private Button changefirstname;
    @FXML
    private Button changelastname;
    @FXML
    private Button close;
    @FXML
    private Label currentfirstname;
    @FXML
    private Label currentlastname;
    @FXML
    private TextField firstname;
    @FXML
    private TextField lastname;
    @FXML
    private Label message;
    @FXML
    private ImageView profilepic;
    @FXML
    private Label username;

    private Stage stage;
    private Stage parentStage;
    private Model model;
    private Label fullname;
    private ImageView canvasprofilepic;
    private File chosenprofilepic;

    public EditProfileController(Stage parentStage, Model model, Label fullname, ImageView canvasprofilepic,
            String profilepic) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
        this.fullname = fullname;
        this.canvasprofilepic = canvasprofilepic;
        this.chosenprofilepic = new File(profilepic); // Set Default Profile Pic
    }

    @FXML
    public void initialize() {

        // Change Profile Picture
        profilepic.setOnMouseClicked(event -> {

            try {
                FileChooser filechooser = new FileChooser();// Create File chooser
                filechooser.getExtensionFilters()
                        .add(new ExtensionFilter("Image Files", "*jpg", "*png", "*gif", "*jpeg"));
                filechooser.setTitle("Select Profile Picture");// Name File Chooser Window
                chosenprofilepic = filechooser.showOpenDialog(stage);// Opening filechooser for user
                profilepic.setImage(new Image(chosenprofilepic.getAbsolutePath(), 219, 204, false, false));

                editUser(model.getCurrentUser().getFirstname(), model.getCurrentUser().getLastname());
                message.setText("Profile Picture Changed!");
                message.setTextFill(Color.GREEN);
            } catch (NullPointerException e) {
                message.setText("Error When Selecting Image!");
            }

        });

        // Edit Users First Name
        changefirstname.setOnAction(event -> {

            if (!firstname.getText().isEmpty()) {
                editUser(firstname.getText(), model.getCurrentUser().getLastname());
                message.setText("Firstname Changed!");
                message.setTextFill(Color.GREEN);
            } else {
                message.setText("Empty First Name!");
                message.setTextFill(Color.RED);
            }

        });

        // Edit Users Last Name
        changelastname.setOnAction(event -> {

            if (!lastname.getText().isEmpty()) {
                editUser(model.getCurrentUser().getFirstname(), lastname.getText());
                message.setText("Lastname Changed!");
                message.setTextFill(Color.GREEN);
            } else {
                message.setText("Empty Last Name!");
                message.setTextFill(Color.RED);
            }

        });

        // Close
        close.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });

    }

    // Edit User Details
    public void editUser(String firstname, String lastname) {

        try {
            String filename = chosenprofilepic.getName();
            File fileoutput = new File("./src/main/resources/view/images/" + filename); // Custom File Name
            BufferedImage bufferedimage = SwingFXUtils.fromFXImage(profilepic.getImage(), null);
            ImageIO.write(bufferedimage, "jpeg", fileoutput); // Save File to Images folder

            model.getUserDao().editUser(model.getCurrentUser().getUsername(), firstname, lastname,
                    fileoutput.toString()); // Apply changes to user
            model.setCurrentUser(model.getUserDao().getUser(model.getCurrentUser().getUsername(),
                    model.getCurrentUser().getPassword())); // Reset current user

        } catch (SQLException | IOException e) {
            message.setText(e.getMessage());
            message.setTextFill(Color.RED);
        }

        // Change User Details Displayed
        currentfirstname.setText((model.getCurrentUser().getFirstname()));
        currentlastname.setText((model.getCurrentUser().getLastname()));

        // Change Details Displayed On Canvas
        try {
            String profilePicPath = model.getCurrentUser().getProfilepic();
            if (profilePicPath != null) {
                InputStream inputstream = new FileInputStream(profilePicPath);
                Image profileimage = new Image(inputstream);
                canvasprofilepic.setImage(profileimage);
            }
        } catch (FileNotFoundException e) {
        }
        fullname.setText(model.getCurrentUser().getFirstname() + " " + model.getCurrentUser().getLastname());

    }

    // Show Stage
    public void showStage(Pane root) throws SQLException, FileNotFoundException {
        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Edit Profile");
        stage.setAlwaysOnTop(true);

        try {
            String profilePicPath = model.getCurrentUser().getProfilepic();
            if (profilePicPath != null) {
                InputStream inputstream = new FileInputStream(profilePicPath);
                Image profileimage = new Image(inputstream);
                profilepic.setImage(profileimage);
            }
        } catch (FileNotFoundException e) {
            message.setText("Error retrieving profile picture!");
        }

        username.setText((model.getCurrentUser().getUsername()));
        currentfirstname.setText((model.getCurrentUser().getFirstname()));
        currentlastname.setText((model.getCurrentUser().getLastname()));
        stage.show();
    }

}
