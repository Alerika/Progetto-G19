package it.unipv.gui.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

public class BorderedTitledPane extends StackPane {

    public BorderedTitledPane(String titleString, Node content) {
        Label title = new Label(" " + titleString + " ");
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("system", FontWeight.NORMAL, FontPosture.REGULAR, 20));

        title.getStyleClass().add("bordered-titled-title");
        StackPane.setMargin(title, new Insets(0, 0, 0, 10));
        StackPane.setAlignment(title, Pos.TOP_LEFT);

        StackPane contentPane = new StackPane();
        contentPane.getChildren().add(content);

        getStyleClass().add("bordered-titled-border");
        getStylesheets().add("css/BorderedTitledPaneStyle.css");
        getChildren().addAll(contentPane, title);
    }
}