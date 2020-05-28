package APP;

public class CacheL1 {
  private String bloque;
  private String coherencia; 
  private String memDir;
  private String Dato;

  public CacheL1(String bloque, String coherencia, String memDir, String dato) {
    this.bloque = bloque;
    this.coherencia = coherencia;
    this.memDir = memDir;
    Dato = dato;
  }

  public String getBloque() {
    return bloque;
  }

  public void setBloque(String bloque) {
    this.bloque = bloque;
  }

  public String getCoherencia() {
    return coherencia;
  }

  public void setCoherencia(String coherencia) {
    this.coherencia = coherencia;
  }

  public String getMemDir() {
    return memDir;
  }

  public void setMemDir(String memDir) {
    this.memDir = memDir;
  }

  public String getDato() {
    return Dato;
  }

  public void setDato(String dato) {
    Dato = dato;
  }

  
  

}