package app;

public class Core extends Thread{

    //Atrubutos
    public boolean powerON = false;
    //private boolean L1Miss = false;
    private String id = "";
    private String fatherId = "";
    private String memDirMiss = "";
    private String[][] L1 = new String[3][4];

    //Constructor
    public Core(String msg,String _fatherId, String _id){
        super(msg);
        this.id = _id;
        this.fatherId = _fatherId;
        this.L1[0][0] = "Bloque";
        this.L1[0][1] = "Coherencia";
        this.L1[0][2] = "DirMem";
        this.L1[0][3] = "Dato";
        this.L1[1][0] = "0";
        this.L1[2][0] = "1";
    }

    //Getter and Setters
    public boolean isPowerON() {
		return powerON;
	}

	public void setPowerON(boolean powerON) {
		this.powerON = powerON;
    }

	public String getMemDirMiss() {
		return memDirMiss;
    }
    
    //Metodos
    public void run(){
        InstructionGenerator instr = new InstructionGenerator();
        while (!this.powerON){
            String instruction = instr.newInstruction(this.fatherId, this.id);
            System.out.println(instruction);
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
}