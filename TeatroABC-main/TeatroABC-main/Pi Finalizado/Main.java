import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;
import Telas.Telalogin;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        Telalogin login = new Telalogin(primaryStage);
        Scene scene = new Scene((Parent) login.getView());
        primaryStage.setTitle("Teatro ABC");
        primaryStage.setScene(scene);
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 