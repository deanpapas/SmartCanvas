/*
 * Canvas Controller
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

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Model;

/*
 * The CanvasController class defines the back end functions for the Canvas View and its
 * related elements
 */

public class CanvasController {

	@FXML
	private MenuItem about;
	@FXML
	private Label anglevalue;
	@FXML
	private Button canvas;
	@FXML
	private Button circle;
	@FXML
	private MenuItem clearcanvas;
	@FXML
	private MenuItem deleteelement;
	@FXML
	private Pane currentcanvas;
	@FXML
	private MenuItem deletecanvas;
	@FXML
	private Label fullname;
	@FXML
	private Label heightvalue;
	@FXML
	private Button image;
	@FXML
	private Button logout;
	@FXML
	private MenuItem newcanvas;
	@FXML
	private Pane optionpane;
	@FXML
	private Button profile;
	@FXML
	private ImageView profilepic;
	@FXML
	private Button rectangle;
	@FXML
	private MenuItem saveas;
	@FXML
	private Button text;
	@FXML
	private Label widthvalue;
	@FXML
	private Label xvalue;
	@FXML
	private Label yvalue;
	@FXML
	private Slider zoom;
	@FXML
	private Label zoomlabel;
	@FXML
	private HBox detailbox;

	private Stage stage;
	private Stage parentStage;
	private Model model;

	// Selection Variables
	private Object selectedElement;
	private Circle selectedElementSizeHandle;
	private Circle selectedElementMoveHandle;
	private final double handleRadius = 8;

	public CanvasController(Stage parentStage, Model model) {
		this.stage = new Stage();
		this.parentStage = parentStage;
		this.model = model;
	}

	@FXML
	public void initialize() {

		// Create,Load & Show Edit Profile
		profile.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/EditProfileView.fxml"));
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new EditProfileController(stage, model, fullname, profilepic,
							model.getCurrentUser().getProfilepic());
				};
				loader.setControllerFactory(controllerFactory);
				VBox root = loader.load();
				EditProfileController editprofileviewController = loader.getController();
				editprofileviewController.showStage(root);
			} catch (IOException | SQLException e) {
				fullname.setText(e.getMessage());
			}
		});

		// Return To Login
		logout.setOnAction(event -> {
			stage.close();
			parentStage.show();
		});

		// Create New Canvas Menu
		newcanvas.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/CreateCanvasView.fxml"));
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new CreateCanvasController(stage, currentcanvas);
				};
				loader.setControllerFactory(controllerFactory);
				VBox root = loader.load();
				CreateCanvasController createcanvasController = loader.getController();
				createcanvasController.showStage(root);
			} catch (IOException e) {
				fullname.setText(e.getMessage());
				System.out.print(e.getMessage());
			}

			// Prepare Zoom
			zoom.setValue(100);
			zoomlabel.setText("100%");
			setCanvasZoom(100);

		});

		// Clear Current Canvas
		clearcanvas.setOnAction(event -> {
			currentcanvas.getChildren().clear();
			currentcanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
			optionpane.getChildren().clear();
		});

		// Delete Current Canvas
		deletecanvas.setOnAction(event -> {
			currentcanvas.getChildren().clear();
			currentcanvas.setVisible(false);
			optionpane.getChildren().clear();
			currentcanvas.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

			// Clear Zoom
			zoom.setValue(0);
			zoomlabel.setText(null);
		});

		// Save Canvas
		saveas.setOnAction(event -> {

			// Unselect All Elements
			unselect();

			// Open File Chooser
			if (currentcanvas.isVisible()) {
				Image canvasImage = currentcanvas.snapshot(new SnapshotParameters(), null);

				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("JPG", ".jpg"),
						new FileChooser.ExtensionFilter("PNG", ".png"));

				fileChooser.setTitle("Save Image");

				File file = fileChooser.showSaveDialog(stage);
				if (file != null) {
					try {
						ImageIO.write(SwingFXUtils.fromFXImage(canvasImage,
								null), "png", file);
						canvasError("Canvas Saved!");
					} catch (IOException e) {
						canvasError("Error When Saving Canvas!");
					}
				}

			} else {
				canvasError("No Canvas Found!");
			}

		});

		// Delete Element
		deleteelement.setOnAction(event -> {
			currentcanvas.getChildren().remove(selectedElement);
			currentcanvas.getChildren().remove(selectedElementSizeHandle);
			currentcanvas.getChildren().remove(selectedElementMoveHandle);
			optionpane.getChildren().clear();
		});

		// About Page
		about.setOnAction(event -> {
			try {
				FXMLLoader loader = new FXMLLoader(getClass().getResource("/AboutView.fxml"));
				Callback<Class<?>, Object> controllerFactory = param -> {
					return new AboutController(stage);
				};
				loader.setControllerFactory(controllerFactory);
				VBox root = loader.load();
				AboutController aboutviewcontroller = loader.getController();
				aboutviewcontroller.showStage(root);
			} catch (IOException e) {
				fullname.setText(e.getMessage());
			}
		});

		// Text Element
		text.setOnAction(event -> {

			// New Text
			TextField newText = new TextField("Drag me!");
			newText.setPrefWidth(85);
			centerElement(newText);

			// New Size Handle
			Circle sizehandle = new Circle(handleRadius, Color.RED);
			sizehandle.setStroke(Color.BLACK);
			sizehandle.setVisible(false);

			// New Move Handle
			Circle movehandle = new Circle(handleRadius, Color.YELLOW);
			movehandle.setStroke(Color.BLACK);
			movehandle.setVisible(false);

			// Bind Size Handle
			sizehandle.centerXProperty()
					.bind(newText.layoutXProperty().add(newText.widthProperty()));
			sizehandle.centerYProperty()
					.bind(newText.layoutYProperty().add(newText.heightProperty()));

			// Prepare Handle
			Wrapper<Point2D> mouseLocation = new Wrapper<Point2D>();
			prepareHandle(sizehandle, mouseLocation);

			// Check Canvas
			if (currentcanvas.isVisible()) {
				addElement(newText, sizehandle, movehandle);
				centerElement(newText);
			} else {
				canvasError("Please create a canvas");
			}

			// Drag Element
			movehandle.setOnMouseDragged(mouseEvent -> {
				double newXLocation = newText.getBoundsInParent().getMinX()
						+ mouseEvent.getX();
				double newYLocation = newText.getBoundsInParent().getMinY()
						+ mouseEvent.getY();

				newText.relocate(newXLocation, newYLocation);
				sizehandle.relocate(
						newText.getBoundsInParent().getMaxX() - handleRadius,
						newText.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newText.getBoundsInParent().getCenterX() - handleRadius,
						newText.getBoundsInParent().getMaxY() + handleRadius * 2);
				updateDetails(newText);

			});

			// Options Pane
			newText.setOnMouseClicked(mouseEvent -> {
				unselect();
				select(newText, sizehandle, movehandle);
				sizehandle.relocate(newText.getBoundsInParent().getMaxX() - handleRadius,
						newText.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newText.getBoundsInParent().getCenterX() - handleRadius,
						newText.getBoundsInParent().getMaxY() + handleRadius * 2);
				loadOptionPane(newText, sizehandle, movehandle);
				updateDetails(newText);
			});

			// Drag Size Handle
			sizehandle.setOnMouseDragged(mouseEvent -> {
				if (mouseLocation.value != null) {
					double deltaX = mouseEvent.getSceneX() - mouseLocation.value.getX();
					double deltaY = mouseEvent.getSceneY() - mouseLocation.value.getY();

					newText.setPrefWidth(newText.getWidth() + deltaX);
					newText.setPrefHeight(newText.getHeight() + deltaY);

					mouseLocation.value = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

					movehandle.relocate(
							newText.getBoundsInParent().getCenterX() - handleRadius,
							newText.getBoundsInParent().getMaxY() + handleRadius * 2);

				}
				updateDetails(newText);
			});

		});

		// Image Element
		image.setOnAction(event -> {

			// New Image
			ImageView newImage = new ImageView();
			centerElement(newImage);
			final double handleRadius = 8;

			// New Size Handle
			Circle sizehandle = new Circle(handleRadius, Color.RED);
			sizehandle.setStroke(Color.BLACK);
			sizehandle.setVisible(false);

			// New Move Handle
			Circle movehandle = new Circle(handleRadius, Color.YELLOW);
			movehandle.setStroke(Color.BLACK);
			movehandle.setVisible(false);

			// Bind Size Handle
			sizehandle.centerXProperty()
					.bind(newImage.xProperty().add(newImage.fitWidthProperty()).subtract(handleRadius));
			sizehandle.centerYProperty()
					.bind(newImage.yProperty().add(newImage.fitHeightProperty()).subtract(handleRadius));

			// Prepare Handle
			Wrapper<Point2D> mouseLocation = new Wrapper<Point2D>();
			prepareHandle(sizehandle, mouseLocation);

			// Open File Chooser
			if (currentcanvas.isVisible()) {
				image.setOnMouseClicked(mouseEvent -> {
					try {
						FileChooser filechooser = new FileChooser();// Create File chooser
						filechooser.getExtensionFilters()
								.add(new ExtensionFilter("Image Files", "*jpg", "*png", "*gif", "*jpeg"));
						filechooser.setTitle("Select Profile Picture");// Name File Chooser Window
						File file = filechooser.showOpenDialog(stage);// Opening filechooser for user
						newImage.setImage(new Image(file.getAbsolutePath(), 219, 204, false, false));
					} catch (NullPointerException e) {
					}
				});

				addElement(newImage, sizehandle, movehandle);
				centerElement(newImage);

			} else {
				canvasError("Please create a canvas");
			}

			// Drag Element
			movehandle.setOnMouseDragged(mouseEvent -> {
				double newXLocation = newImage.getBoundsInParent().getMinX() - newImage.getFitWidth() / 2
						+ mouseEvent.getX();
				double newYLocation = newImage.getBoundsInParent().getMinY() - newImage.getFitHeight() / 2
						+ mouseEvent.getY();
				newImage.relocate(newXLocation, newYLocation);
				sizehandle.relocate(
						newImage.getBoundsInParent().getMaxX() - handleRadius,
						newImage.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newImage.getBoundsInParent().getCenterX(),
						newImage.getBoundsInParent().getMaxY() + handleRadius * 2);
				updateDetails(newImage);
			});

			// Options Pane
			newImage.setOnMouseClicked(mouseEvent -> {
				unselect();
				select(newImage, sizehandle, movehandle);
				sizehandle.relocate(
						newImage.getBoundsInParent().getMaxX() - handleRadius,
						newImage.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newImage.getBoundsInParent().getCenterX(),
						newImage.getBoundsInParent().getMaxY() + handleRadius * 2);
				loadOptionPane(newImage, sizehandle, movehandle);
				updateDetails(newImage);
			});

			// Drag Handle
			sizehandle.setOnMouseDragged(mouseEvent -> {

				if (mouseLocation.value != null) {
					double deltaX = mouseEvent.getSceneX() - mouseLocation.value.getX();
					double deltaY = mouseEvent.getSceneY() - mouseLocation.value.getY();

					newImage.setFitWidth(newImage.getFitWidth() + deltaX);
					newImage.setFitHeight(newImage.getFitHeight() + deltaY);

					mouseLocation.value = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

					movehandle.relocate(
							newImage.getBoundsInParent().getCenterX(),
							newImage.getBoundsInParent().getMaxY() + handleRadius * 2);
				}
				updateDetails(newImage);
			});

		});

		// Rectangle Element
		rectangle.setOnAction(event -> {

			// New Rectangle
			Rectangle newRectangle = new Rectangle();
			centerElement(newRectangle);
			final double handleRadius = 8;

			// New Size Handle
			Circle sizehandle = new Circle(handleRadius, Color.RED);
			sizehandle.setStroke(Color.BLACK);
			sizehandle.setVisible(false);

			// New Move Handle
			Circle movehandle = new Circle(handleRadius, Color.YELLOW);
			movehandle.setStroke(Color.BLACK);
			movehandle.setVisible(false);

			// Bind Handle
			sizehandle.centerXProperty()
					.bind(newRectangle.xProperty().add(newRectangle.widthProperty()).subtract(handleRadius));
			sizehandle.centerYProperty()
					.bind(newRectangle.yProperty().add(newRectangle.heightProperty()).subtract(handleRadius));

			// Prepare Handle
			Wrapper<Point2D> mouseLocation = new Wrapper<Point2D>();
			prepareHandle(sizehandle, mouseLocation);

			// Add Rectangle to Canvas
			addElement(newRectangle, sizehandle, movehandle);

			// Drag Element
			movehandle.setOnMouseDragged(mouseEvent -> {

				double newXLocation = newRectangle.getBoundsInParent().getMinX()
						+ mouseEvent.getX();
				double newYLocation = newRectangle.getBoundsInParent().getMinY()
						+ mouseEvent.getY();

				newRectangle.relocate(newXLocation, newYLocation);
				sizehandle.relocate(
						newRectangle.getBoundsInParent().getMaxX() - handleRadius,
						newRectangle.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newRectangle.getBoundsInParent().getCenterX() - handleRadius,
						newRectangle.getBoundsInParent().getMaxY() + handleRadius * 2);
				updateDetails(newRectangle);
			});

			// Options Pane
			newRectangle.setOnMouseClicked(mouseEvent -> {
				unselect();
				select(newRectangle, sizehandle, movehandle);
				sizehandle.relocate(
						newRectangle.getBoundsInParent().getMaxX() - handleRadius,
						newRectangle.getBoundsInParent().getMaxY() - handleRadius);
				movehandle.relocate(
						newRectangle.getBoundsInParent().getCenterX() - handleRadius,
						newRectangle.getBoundsInParent().getMaxY() + handleRadius * 2);
				loadOptionPane(newRectangle, sizehandle, movehandle);
				updateDetails(newRectangle);
			});

			// Drag Size Handle
			sizehandle.setOnMouseDragged(mouseEvent -> {

				if (mouseLocation.value != null) {
					double deltaX = mouseEvent.getSceneX() - mouseLocation.value.getX();
					double deltaY = mouseEvent.getSceneY() - mouseLocation.value.getY();

					newRectangle.setWidth(newRectangle.getWidth() + deltaX);
					newRectangle.setHeight(newRectangle.getHeight() + deltaY);

					mouseLocation.value = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

					movehandle.relocate(
							newRectangle.getBoundsInParent().getCenterX() - handleRadius,
							newRectangle.getBoundsInParent().getMaxY() + handleRadius * 2);

				}
				updateDetails(newRectangle);
			});

		});

		// Circle Element
		circle.setOnAction(event -> {

			// New Circle
			Circle newCircle = new Circle();
			centerElement(newCircle);
			final double handleRadius = 8;

			// New Size Handle
			Circle sizehandle = new Circle(handleRadius, Color.RED);
			sizehandle.setStroke(Color.BLACK);
			sizehandle.setVisible(false);

			// New Move Handle
			Circle movehandle = new Circle(handleRadius, Color.YELLOW);
			movehandle.setStroke(Color.BLACK);
			movehandle.setVisible(false);

			// Bind Handle to New Circle
			sizehandle.centerXProperty().bind(
					newCircle.radiusProperty().subtract(newCircle.radiusProperty()).add(newCircle.radiusProperty()));
			sizehandle.centerYProperty().bind(newCircle.radiusProperty().subtract(newCircle.radiusProperty()));

			// Prepare Handle
			Wrapper<Point2D> mouseLocation = new Wrapper<Point2D>();
			prepareHandle(sizehandle, mouseLocation);

			// Add Circle to Canvas
			addElement(newCircle, sizehandle, movehandle);

			// Drag Element
			movehandle.setOnMouseDragged(mouseEvent -> {
				double newXLocation = newCircle.getBoundsInParent().getMinX() + mouseEvent.getX();
				double newYLocation = newCircle.getBoundsInParent().getMinY() + mouseEvent.getY();
				newCircle.relocate(newXLocation, newYLocation);
				sizehandle.relocate(
						newCircle.getBoundsInParent().getCenterX() + newCircle.getRadius() - handleRadius,
						newCircle.getBoundsInParent().getCenterY() - handleRadius);
				movehandle.relocate(
						newCircle.getBoundsInParent().getCenterX() - handleRadius,
						newCircle.getBoundsInParent().getCenterY() + newCircle.getRadius() + handleRadius * 2);
				updateDetails(newCircle);
			});

			// Options Pane
			newCircle.setOnMouseClicked(mouseEvent -> {
				unselect();
				select(newCircle, sizehandle, movehandle);
				sizehandle.relocate(
						newCircle.getBoundsInParent().getCenterX() + newCircle.getRadius() - handleRadius,
						newCircle.getBoundsInParent().getCenterY() - handleRadius);
				movehandle.relocate(
						newCircle.getBoundsInParent().getCenterX() - handleRadius,
						newCircle.getBoundsInParent().getCenterY() + newCircle.getRadius() + handleRadius * 2);
				loadOptionPane(newCircle, sizehandle, movehandle);
				updateDetails(newCircle);
			});

			// Drag Handle
			sizehandle.setOnMouseDragged(mouseEvent -> {

				if (mouseLocation.value != null) {
					double deltaX = mouseEvent.getSceneX() - mouseLocation.value.getX();
					double deltaY = mouseEvent.getSceneY() - mouseLocation.value.getY();

					newCircle.setRadius(newCircle.getRadius() + deltaX);
					newCircle.setRadius(newCircle.getRadius() + deltaY);

					mouseLocation.value = new Point2D(mouseEvent.getSceneX(), mouseEvent.getSceneY());

					movehandle.relocate(
							newCircle.getBoundsInParent().getCenterX() - handleRadius,
							newCircle.getBoundsInParent().getCenterY() + newCircle.getRadius() + handleRadius * 2);
				}
				updateDetails(newCircle);
			});

		});

		// Canvas Element
		canvas.setOnAction(event -> {

			// Canvas Error Message
			if (currentcanvas.isVisible()) {
				canvasError("Please clear current canvas");
			} else {
				currentcanvas.setVisible(true);
				currentcanvas.setMaxSize(724, 585);
				currentcanvas.setPrefWidth(724);
				currentcanvas.setPrefHeight(585);
			}

			// Prepare Zoom
			zoom.setValue(100);
			setCanvasZoom(100);

		});

		// Canvas Options
		currentcanvas.setOnMouseClicked(event -> {

			if (event.getButton() == MouseButton.SECONDARY) {

				unselect();

				loadOptionPane(currentcanvas, null, null);
			}

		});

		// Canvas Zoom
		zoom.valueProperty().addListener((ov, oldValue, newValue) -> {
			setCanvasZoom(newValue.intValue());
		});

	}

	// Show Stage
	public void showStage(Pane root) throws FileNotFoundException, SQLException {

		// Prepare Scene
		Scene scene = new Scene(root, 1150, 700);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("SmartCanvas");

		// Set Current User Details
		try {
			String profilePicPath = model.getCurrentUser().getProfilepic();
			if (profilePicPath != null) {
				InputStream inputstream = new FileInputStream(profilePicPath);
				Image profileimage = new Image(inputstream);
				profilepic.setImage(profileimage);
			}
		} catch (FileNotFoundException e) {
		}
		fullname.setText(model.getCurrentUser().getFirstname() + " " + model.getCurrentUser().getLastname());
		stage.show();
	}

	// Add Element
	public void addElement(Object element, Circle sizehandle, Circle movehandle) {

		if (currentcanvas.isVisible()) {

			if (element instanceof Shape) {
				if (element instanceof Circle) { // Circle Pre-sets
					Circle circle = (Circle) element;
					circle.setRadius(50);
					circle.setFill(Color.web("#2e89ff"));
					currentcanvas.getChildren().add(circle);
					currentcanvas.getChildren().add(sizehandle);
					currentcanvas.getChildren().add(movehandle);
					centerElement(element);
				} else if (element instanceof Rectangle) { // Rectangle Pre-sets
					Rectangle rectangle = (Rectangle) element;
					((Rectangle) element).setWidth(75);
					((Rectangle) element).setHeight(50);
					((Rectangle) element).setHeight(50);
					((Rectangle) element).setFill(Color.web("#2e89ff"));
					currentcanvas.getChildren().add(rectangle);
					currentcanvas.getChildren().add(sizehandle);
					currentcanvas.getChildren().add(movehandle);
					centerElement(element);
				}
			}

			if (element instanceof TextField) { // Text Pre-sets
				TextField text = (TextField) element;
				text.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
				text.setMaxWidth(currentcanvas.getWidth());
				currentcanvas.getChildren().add(text);
				currentcanvas.getChildren().add(sizehandle);
				currentcanvas.getChildren().add(movehandle);
				centerElement(element);
			}

			if (element instanceof ImageView) { // Image Pre-sets
				currentcanvas.getChildren().add((ImageView) element);
				currentcanvas.getChildren().add(sizehandle);
				currentcanvas.getChildren().add(movehandle);
				centerElement(element);
			}

		} else {
			canvasError("Please create a canvas");
		}
	}

	// Center Element
	public void centerElement(Object element) {
		double X = (currentcanvas.getWidth() / 2);
		double Y = (currentcanvas.getHeight() / 2);
		((Node) element).relocate(X, Y);
	}

	// Select Element
	public void select(Object element, Circle sizehandle, Circle movehandle) {

		selectedElementSizeHandle = sizehandle;
		selectedElementSizeHandle.setVisible(true);

		selectedElementMoveHandle = movehandle;
		selectedElementMoveHandle.setVisible(true);

		selectedElement = element;

	}

	// Unselect Element
	public void unselect() {
		if (selectedElement != null) {
			selectedElementSizeHandle.setVisible(false);
			selectedElementMoveHandle.setVisible(false);
			selectedElement = null;
		}
	}

	// Prepare Handle
	public void prepareHandle(Circle circle, Wrapper<Point2D> mouseLocation) {

		circle.setOnDragDetected(event -> {
			mouseLocation.value = new Point2D(event.getSceneX(), event.getSceneY());
		});

		circle.setOnMouseReleased(event -> {
			mouseLocation.value = null;
		});
	}

	// Load Details
	public void updateDetails(Object element) {

		if (element instanceof TextField) { // Update Text Details
			TextField text = (TextField) element;
			xvalue.setText(String.valueOf(Math.round(text.getLayoutX())));
			yvalue.setText(String.valueOf(Math.round(text.getLayoutY())));
			widthvalue.setText(String.valueOf(Math.round(text.getWidth())));
			heightvalue.setText(String.valueOf(Math.round(text.getHeight())));
			anglevalue.setText(String.valueOf(Math.round(text.getRotate())));
		}

		if (element instanceof Shape) {

			if (element instanceof Rectangle) { // Update Rectangle Details
				Rectangle rectangle = (Rectangle) element;
				xvalue.setText(String.valueOf(Math.round(rectangle.getLayoutX())));
				yvalue.setText(String.valueOf(Math.round(rectangle.getLayoutY())));
				widthvalue.setText(String.valueOf(Math.round(rectangle.getWidth())));
				heightvalue.setText(String.valueOf(Math.round(rectangle.getHeight())));
				anglevalue.setText(String.valueOf(Math.round(rectangle.getRotate())));
			} else if (element instanceof Circle) { // Update Circle Details
				Circle circle = (Circle) element;
				xvalue.setText(String.valueOf(Math.round(circle.getLayoutX())));
				yvalue.setText(String.valueOf(Math.round(circle.getLayoutY())));
				widthvalue.setText(String.valueOf(Math.round(circle.getRadius())));
				heightvalue.setText(String.valueOf(Math.round(circle.getRadius())));
				anglevalue.setText(String.valueOf(Math.round(circle.getRotate())));
			}
		}

		if (element instanceof ImageView) { // Update Image Details
			ImageView image = (ImageView) element;
			xvalue.setText(String.valueOf(Math.round(image.getLayoutX())));
			yvalue.setText(String.valueOf(Math.round(image.getLayoutY())));
			widthvalue.setText(String.valueOf(Math.round(image.getFitWidth())));
			heightvalue.setText(String.valueOf(Math.round(image.getFitHeight())));
			anglevalue.setText(String.valueOf(Math.round(image.getRotate())));
		}

	}

	// Canvas Zoom
	public void setCanvasZoom(int zoom) {
		if (currentcanvas.isVisible()) {
			currentcanvas.setScaleX(zoom / 100.0);
			currentcanvas.setScaleY(zoom / 100.0);
			zoomlabel.setText(zoom + "%");
		}
	}

	// Clear Canvas Error Message
	public void canvasError(String errormessage) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/WarningView.fxml"));
			Callback<Class<?>, Object> controllerFactory = param -> {
				return new WarningController(stage, errormessage);
			};
			loader.setControllerFactory(controllerFactory);
			VBox root = loader.load();
			WarningController warningviewcontroller = loader.getController();
			warningviewcontroller.showStage(root);
		} catch (IOException | SQLException e) {
			fullname.setText(e.getMessage());
		}

	}

	// Load Option Pane
	public void loadOptionPane(Object element, Circle sizehandle, Circle movehandle) {

		String fxml = null;
		Callback<Class<?>, Object> controllerFactory = null;
		if (element instanceof Shape) { // Load Shape Options
			fxml = "/ShapeOptionsView.fxml";
			controllerFactory = param -> {
				return new ShapeOptionsController((Shape) element, sizehandle, movehandle, currentcanvas, optionpane,
						anglevalue);
			};
		}

		if (element instanceof TextField) { // Load Text Options
			fxml = "/TextOptionsView.fxml";
			controllerFactory = param -> {
				return new TextOptionsController((TextField) element, sizehandle, movehandle, currentcanvas, optionpane,
						anglevalue);
			};
		}

		if (element instanceof ImageView) { // Load Image Options
			fxml = "/ImageOptionsView.fxml";
			controllerFactory = param -> {
				return new ImageOptionsController((ImageView) element, sizehandle, movehandle, currentcanvas,
						optionpane, anglevalue);
			};
		}

		if (element instanceof Pane) { //Load Canvas Options
			fxml = "/CanvasOptionsView.fxml";
			controllerFactory = param -> {
				return new CanvasOptionsController(currentcanvas);
			};
		}

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
			loader.setControllerFactory(controllerFactory);
			optionpane.getChildren().add(loader.load());
		} catch (IOException e) {
			fullname.setText(e.toString());
		}
	}

	// Wrapper Class
	static class Wrapper<T> {
		T value;
	}

}
