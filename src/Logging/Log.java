package Logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Log {

  private String fileName;
  private Logger Log;
  private FileHandler logFileHandler;

  public Log(String fileName) {
    this.fileName = fileName;
    try {
      this.Log = Logger.getLogger(this.fileName);
      this.logFileHandler = new FileHandler("Logs\\" + fileName + ".log");
      this.Log.addHandler(this.logFileHandler);
      SimpleFormatter formatter = new SimpleFormatter();
      this.logFileHandler.setFormatter(formatter);
    } catch (SecurityException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  //Methods
  public void newInfo(String msg){
    this.Log.info(msg+"\n");
  }

  public void newWarn(String msg){
    this.Log.warning(msg+"\n");
  }
}