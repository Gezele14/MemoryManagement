package app;

public class Cpu extends Thread{
    //Atributos
    private boolean L2Miss;
    private String id = "";

    private Core core0;
    private Core core1;

    //Constructor
    public Cpu(String msg, String _id){
        this.id = _id;
        
        core0 = new Core("Core0", this.id, "0");
        core1 = new Core("Core1", this.id, "1");
    }

    //Metodos
    public void run(){
        this.core0.start();
        this.core1.start();
    }

}