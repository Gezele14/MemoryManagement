package APP;

public class Core extends Thread{

    //Atrubutos
    public boolean powerON = false;
    //private boolean L1Miss = false;
    private boolean update = false;
    private String id = "";
    private String fatherId = "";
    private String memDirMiss = "";
    private String[][] L1 = {{"Bloque", "Coherencia", "Dir. Mem", "Dato"},
                             {"","","",""},
                             {"","","",""}};
    InstructionGenerator instr = new InstructionGenerator();

    //Constructor
    public Core(String msg,String _fatherId, String _id){
        super(msg);
        this.id = _id;
        this.fatherId = _fatherId;
    }

    
    //Metodos
    public void run(){
        while (!this.powerON){
            String instruction = this.instr.newInstruction(this.fatherId, this.id);
            System.out.println(instruction);
            this.L1[1][1] = instruction; 
            this.update = true;
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    private String[] instrDecoder(String instr){
        String[] instrDecoded = instr.split(";");
        return instrDecoded;
    }

    private boolean instrCheck(String[] instr){
        for (int i = 0; i < this.L1.length; i++) {
            if(instr[3] == this.L1[i][2]){
                return true;
            }
        }
        return false;
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
    public String[][] getL1() {
        return L1;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    
}