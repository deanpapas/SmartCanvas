/*
 * Text Options Controller
 * 
 * V1.0
 *
 * 30/05/2022
 */

package controller;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/*
 * The TextOptionsController class defines the back end functions for the Text Options View
 * and its
 * related elements
 */

public class TextOptionsController {

    @FXML
    private MenuItem algerian;
    @FXML
    private MenuItem calibri;
    @FXML
    private MenuItem papyrus;
    @FXML
    private ColorPicker backgroundcolorpicker;
    @FXML
    private Button bold;
    @FXML
    private ColorPicker bordercolorpicker;
    @FXML
    private TextField borderwidth;
    @FXML
    private MenuButton fontchoice;
    @FXML
    private TextField fontsize;
    @FXML
    private Button italics;
    @FXML
    private Button left;
    @FXML
    private Button middle;
    @FXML
    private Button right;
    @FXML
    private Button rotate;
    @FXML
    private Button delete;
    @FXML
    private TextField textchange;
    @FXML
    private ColorPicker textcolorpicker;
    @FXML
    private Slider rotateslider;

    TextField text;
    Circle sizehandle;
    Circle movehandle;
    Pane canvas;
    Pane optionpane;

    String font;
    Double size;
    FontWeight weight;
    FontPosture posture;

    Color textColor;
    Color textBorderColor;
    Color borderColor;
    BorderWidths borderWidth;
    Color backgroundColor;
    Label anglelabel;

    public TextOptionsController(TextField text, Circle sizehandle, Circle movehandle, Pane canvas, Pane optionpane,
            Label anglelabel) {
        this.text = text;
        this.sizehandle = sizehandle;
        this.movehandle = movehandle;
        this.canvas = canvas;
        this.optionpane = optionpane;
        this.anglelabel = anglelabel;
    }

    @FXML
    public void initialize() {

        // Prepare Panel
        preparepanel();
        setFontSize();

        // Text Content
        textchange.setOnAction(event -> {
            text.setText(textchange.getText());
        });

        // Fonts
        algerian.setOnAction(event -> {
            font = "Algerian";
            setFont();
        });

        calibri.setOnAction(event -> {
            font = "Calibri";
            setFont();
        });

        papyrus.setOnAction(event -> {
            font = "Papyrus";
            setFont();
        });

        // Font Size
        fontsize.setOnAction(event -> {
            size = Double.parseDouble(fontsize.getText());
            setFont();
            relocateHandle();
        });

        // Font Weight
        bold.setOnAction(event -> {
            if (weight != FontWeight.BOLD) {
                weight = FontWeight.BOLD;
                setFont();
            } else {
                weight = FontWeight.NORMAL;
                setFont();
            }
        });

        // Font Posture
        italics.setOnAction(event -> {
            if (posture != FontPosture.ITALIC) {
                posture = FontPosture.ITALIC;
                setFont();
            } else {
                posture = FontPosture.REGULAR;
                setFont();
            }
        });

        // Text Color
        textcolorpicker.setOnAction(event -> {
            Color textColor = textcolorpicker.getValue(); // Get chosen color
            text.setStyle("-fx-text-fill: " + toRgbString(textColor) + ";");
        });

        // Text Alignment
        left.setOnAction(event -> {
            text.setAlignment(Pos.CENTER_LEFT);
        });

        middle.setOnAction(event -> {
            text.setAlignment(Pos.CENTER);
        });

        right.setOnAction(event -> {
            text.setAlignment(Pos.CENTER_RIGHT);
        });

        // Border Color
        bordercolorpicker.setOnAction(event -> {
            borderColor = bordercolorpicker.getValue(); // Get chosen color
            setBorder();
        });

        // Border Width
        borderwidth.setOnAction(event -> {
            borderWidth = new BorderWidths(Double.parseDouble(borderwidth.getText()));
            setBorder();
        });

        // Background Color
        backgroundcolorpicker.setOnAction(event -> {
            backgroundColor = backgroundcolorpicker.getValue();
            setBackground();
        });

        // Rotate
        rotate.setOnAction(event -> {
            Double angle = text.getRotate();
            angle = angle + 90;
            rotateslider.setValue(angle);
            text.setRotate(angle);
            anglelabel.setText((String.valueOf(Math.round(angle))));
            relocateHandle();

        });

        rotateslider.valueProperty().addListener((ov, oldValue, newValue) -> {
            text.setRotate(newValue.intValue());
            anglelabel.setText((String.valueOf(Math.round((Double) newValue))));
            relocateHandle();
        });

        // Delete
        delete.setOnAction(event -> {
            canvas.getChildren().remove(text);
            canvas.getChildren().remove(sizehandle);
            canvas.getChildren().remove(movehandle);
            optionpane.getChildren().clear();
        });

    }

    // Prepare Options Panel
    public void preparepanel() {
        textchange.setText(text.getText());
        fontchoice.setText(text.getFont().getFamily());
        fontsize.setText(String.valueOf(text.getFont().getSize()));
        textcolorpicker.setValue(textColor);
        bordercolorpicker.setValue(borderColor);
        backgroundcolorpicker.setValue(backgroundColor);
        rotateslider.setValue(text.getRotate());
    }

    // Set Style Methods
    public void setFont() {
        text.setFont(Font.font(font, weight, posture, size));
        relocateHandle();
    }

    public void setFontSize() {
        String sizeString = text.getFont().toString();
        sizeString = sizeString.substring(sizeString.indexOf("size=") + 5);
        sizeString = sizeString.substring(0, sizeString.indexOf("]"));
        size = Double.parseDouble(sizeString);
    }

    public void setBorder() {
        text.setBorder(new Border(new BorderStroke(borderColor, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                borderWidth)));
        relocateHandle();
    }

    public void setBackground() {
        text.setBackground(new Background(new BackgroundFill(backgroundColor, null, null)));
    }

    // Relocate
    public void relocateHandle() {
        sizehandle.relocate(text.getBoundsInParent().getMaxX() - sizehandle.getRadius(),
                text.getBoundsInParent().getMaxY() - sizehandle.getRadius());
        movehandle.relocate(
                text.getBoundsInParent().getCenterX() - movehandle.getRadius(),
                text.getBoundsInParent().getMaxY() + movehandle.getRadius() * 2);
    }

    // Color Methods
    private String toRgbString(Color c) {
        return "rgb("
                + to255Int(c.getRed())
                + "," + to255Int(c.getGreen())
                + "," + to255Int(c.getBlue())
                + ")";
    }

    private int to255Int(double d) {
        return (int) (d * 255);
    }

}
