package it.unipv.gui.common;

import it.unipv.utils.CloseableUtils;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.InputStream;

public class GUIUtils {

    public static void setFadeInOutOnControl(Node control) {
        final FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
        fadeIn.setNode(control);
        fadeIn.setToValue(1);
        control.setOnMouseEntered(e -> {
            fadeIn.playFromStart();
            control.setCursor(Cursor.HAND);
        });

        final FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
        fadeOut.setNode(control);
        fadeOut.setToValue(0.5);
        control.setOnMouseExited(e -> {
            fadeOut.playFromStart();
            control.setCursor(Cursor.DEFAULT);
        } );

        control.setOpacity(0.5);
    }

    public static void setScaleTransitionOnControl(Node control) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), control);
        scaleTransition.setCycleCount(1);
        scaleTransition.setInterpolator(Interpolator.EASE_BOTH);
        DoubleProperty expandToMaxProperty = new SimpleDoubleProperty(1.2);

        control.setOnMouseEntered(event -> {
            control.setCursor(Cursor.HAND);
            scaleTransition.setFromX(control.getScaleX());
            scaleTransition.setFromY(control.getScaleY());
            scaleTransition.setToX(expandToMaxProperty.get());
            scaleTransition.setToY(expandToMaxProperty.get());
            scaleTransition.playFromStart();
        });


        control.setOnMouseExited(event -> {
            control.setCursor(Cursor.DEFAULT);
            scaleTransition.setFromX(control.getScaleX());
            scaleTransition.setFromY(control.getScaleY());
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            scaleTransition.playFromStart();
        } );
    }

    public static ImageView getIconView(InputStream iconInputStream) {
        ImageView view = new ImageView(new Image(iconInputStream));
        view.setFitWidth(25);
        view.setFitHeight(25);

        CloseableUtils.close(iconInputStream);

        return view;
    }
}
