package GUI;

import java.io.InputStream;
import java.net.URL;

import APP.Cpu;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(final Stage stage) throws Exception {
    stage.show();
    stage.setTitle("Memory Management");
    stage.setX(320);stage.setY(100);
    stage.setResizable(false);
    stage.setFullScreenExitHint("cerrar");
    Platform.setImplicitExit(true);
    stage.setOnCloseRequest((ae) -> {
      Platform.exit();
      System.exit(0);
    });

    //Adding icon to the to the window top
    final InputStream iconStream = getClass().getResourceAsStream("icon.png");
    final Image image = new Image(iconStream);
    stage.getIcons().add(image);

    //Adding fxml file
    FXMLLoader loader = new FXMLLoader();
    URL xmlUrl = getClass().getResource("../FXML/main2.fxml");
    loader.setLocation(xmlUrl);
    Parent root = loader.load();
    MainSceneController controller = loader.getController();
    stage.setScene(new Scene(root));

    Cpu cpu0 = new Cpu("Processor0", "P0"); 
    Cpu cpu1 = new Cpu("Processor1","P1");
        
    cpu0.start(); cpu1.start();

    
    Thread thread = new Thread(() -> {
      while(true){
        try {
            Thread.sleep(500);
        } catch (InterruptedException exc) {
            throw new Error("Unexpected interruption", exc);
        }
        Platform.runLater(() -> controller.printMatrix(controller.L1P00,cpu0.getCore0().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P01,cpu0.getCore1().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P10,cpu1.getCore0().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P11,cpu1.getCore1().getL1()));
        
        
      }
    });
    thread.setDaemon(true);
    thread.start();
    
  }

  public static void main(final String[] args) {
    // Here you can work with args - command line parameters
    Application.launch(args);
  }
    
}