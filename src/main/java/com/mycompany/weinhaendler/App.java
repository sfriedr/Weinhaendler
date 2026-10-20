package com.mycompany.weinhaendler;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static javafx.application.Application.launch;
import javafx.scene.image.Image;

/**
 * JavaFX App
 */
public class App extends Application {

    static final String VIEW = "HauptfensterView.fxml";
    static final String TITLE = "S.F. Weinhandel - Anwendung";
    final String ICON = "Firmenlogo.png";
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(VIEW));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        
        HauptfensterController controller = fxmlLoader.getController();
        controller.hauptfensterSchliessenMeldungErzeugen(stage);
        
        stage.getIcons().add(new Image(getClass().getResourceAsStream(ICON))); //Fenster-Icon anlegen.
        System.out.println(getClass().getResource(ICON));
        System.out.println(getClass().getResourceAsStream(ICON));
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}