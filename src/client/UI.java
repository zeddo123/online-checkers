package src.client;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import src.server.GameMetaData;
import src.server.Piece;

import java.rmi.ConnectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class UI extends Application {
    public Client client;
    public CheckersBoard root;
    public Label turnLabel;

    public Client getClient() {
        return client;
    }
    @Override
    public void start(Stage stage) {
        try {
            this.client = new Client();
        } catch (RemoteException | NotBoundException e) {
            throw new RuntimeException(e);
        }
        this.root = this.client.board;

        var hb = new HBox();
        hb.setSpacing(10);

        var colorPicker = new ColorPicker();
        turnLabel = this.client.turnLabel;
        var registerButton = new Button("Register to Play!");

        hb.getChildren().add(0, colorPicker);
        hb.getChildren().add(1, turnLabel);
        hb.getChildren().add(2, registerButton);

        registerButton.setOnAction(e -> {
            var fx = colorPicker.getValue();
            var color = new java.awt.Color((float) fx.getRed(),
                    (float) fx.getGreen(),
                    (float) fx.getBlue(),
                    (float) fx.getOpacity());
            client.registerToPlay(new GameMetaData(color));
        });

        var vb = new VBox();
        vb.getChildren().add(0, hb);
        vb.getChildren().add(1, this.root);

        stage.setOnCloseRequest(e -> {
            this.getClient().Deregister();
            Platform.exit();
        });

        stage.setTitle("Checkers client");
        stage.setScene(new Scene(vb));
        stage.setResizable(false);
        stage.show();
    }

    public void show() {
        launch();
    }

    public static void showAlert(String message) {
        Dialog<String> dialog = new Dialog<String>();
        //Setting the title
        dialog.setTitle("Dialog");
        ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        //Setting the content of the dialog
        dialog.setContentText(message);
        //Adding buttons to the dialog pane
        dialog.getDialogPane().getButtonTypes().add(type);

        dialog.initModality(Modality.APPLICATION_MODAL);
        //dialog.initOwner(primaryStage);

        dialog.showAndWait();
    }
}
