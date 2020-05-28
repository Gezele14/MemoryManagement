package APP;

public class Cpu extends Thread{
    //Atributos
    private boolean L2Miss;
    public boolean powerON;
    private String id = "";
    private Core core0;
    private Core core1;
    private String memDir = "";
    private String[][] L2={{"Bloque", "Estado", "Dir, Memoria", "Dato"},
                           {"0","","","",""},
                           {"1","","","",""},
                           {"2","","","",""},
                           {"3","","","",""}};

    //Constructor
    public Cpu(String msg, String _id, boolean _powerON){
        this.id = _id;
        this.powerON = _powerON;
        this.core0 = new Core("Core0", this.id, "0");
        this.core1 = new Core("Core1", this.id, "1");

        this.core0.setPowerON(this.powerON);
        this.core1.setPowerON(this.powerON);
        
    }

    //Metodos
    public void run(){
        this.core0.start();
        this.core1.start();
        while(this.powerON){

        }
    }

    public Core getCore0() {
        return core0;
    }

    public Core getCore1() {
        return core1;
    }

    public boolean isL2Miss() {
        return L2Miss;
    }

    public void setL2Miss(boolean l2Miss) {
        L2Miss = l2Miss;
    }

    public boolean isPowerON() {
        return powerON;
    }

    public void setPowerON(boolean powerON) {
        this.powerON = powerON;
    }

    public String getMemDir() {
        return memDir;
    }

    public void setMemDir(String memDir) {
        this.memDir = memDir;
    }

    public String[][] getL2() {
        return L2;
    }

    public void setL2(String[][] l2) {
        L2 = l2;
    }

}