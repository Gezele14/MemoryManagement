package GUI;

import java.io.InputStream;
import java.net.URL;

import APP.Sistema;
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
    stage.titleProperty();
    stage.setX(320);stage.setY(100);
    stage.setResizable(false);

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

    Sistema system = new Sistema("sistema");
    system.start();

    
    Thread thread = new Thread(() -> {
      while(true){
        Platform.runLater(() -> controller.printMatrix(controller.L1P00,system.getCpu0().getCore0().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P01,system.getCpu0().getCore1().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P10,system.getCpu1().getCore0().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L1P11,system.getCpu1().getCore1().getL1()));
        Platform.runLater(() -> controller.printMatrix(controller.L2P0,system.getCpu0().getL2()));
        Platform.runLater(() -> controller.printMatrix(controller.L2P1,system.getCpu1().getL2()));
        Platform.runLater(() -> controller.printMatrix(controller.MEMPRIN,system.getMemory()));
        try {
          Thread.sleep(500);
        } catch (InterruptedException exc) {
            throw new Error("Unexpected interruption", exc);
        }
      }
    });
    thread.setDaemon(true);
    thread.start();
    
  }
    
}