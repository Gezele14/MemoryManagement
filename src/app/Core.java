package APP;

public class Core extends Thread{

    //Atrubutos
    public boolean powerON = false;
    private boolean L1Miss = false;
    private boolean BusWr = false;
    private boolean BusRd = false;
    private String id = "";
    private String fatherId = "";
    private String memDir = "";
    private String[][] L1 = {{"Bloque", "Coherencia", "Dir. Mem", "Dato"},
                             {"0","","",""},
                             {"1","","",""}};
    InstructionGenerator instr = new InstructionGenerator();

    //Constructor
    public Core(String msg,String _fatherId, String _id){
        super(msg);
        this.id = _id;
        this.fatherId = _fatherId;
    }

    
    //Metodos
    public void run(){
        while (this.powerON){
            String instruction = this.instr.newInstruction(this.fatherId, this.id);
            String[] instrDecoded = this.instrDecoder(instruction);
            this.instrCheck(instrDecoded);
            this.MSIprotocol(instrDecoded);
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * 
     * @param instr string with the instruction
     * @return Array with parts of the instruction
     */
    private String[] instrDecoder(String instr){
        String[] instrDecoded = instr.split(";");
        return instrDecoded;
    }

    private boolean instrCheck(String[] instr){
        for (int i = 1; i < this.L1.length; i++) {
            if(instr.length > 3){
                if(instr[3].equals(this.L1[i][2])){
                    return true;
                }
            }
        }
        return false;
    }

    private void writeCache(String[] instr){
        for (int i = 1; i < this.L1.length; i++) {
            if(instr[3].equals(this.L1[i][2])){
                this.L1[i][1] = "M";
                this.L1[i][2] = instr[3];
                this.L1[i][3] = instr[4];
            }
        }
    }

    public void invalidCache(String[] instr){
        for (int i = 1; i < this.L1.length; i++) {
            if(instr[3].equals(this.L1[i][2])){
                this.L1[i][1] = "I";
            }
        }
    }

    private void MSIprotocol(String[] instr){
        if(instr[2].equals("WRITE")){
            if(instrCheck(instr)){
                this.BusWr = true;
                this.writeCache(instr);
            }else{
                int i = dirt2int(instr[3]) + 1;
                this.L1[i][1] = "M";
                this.L1[i][2] = instr[3];
                this.L1[i][3] = instr[4];
                this.BusWr = true;
            }
        }else if(instr[2].equals("CALC")){
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else if (instr[2].equals("READ")){
            if(instrCheck(instr)){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                this.L1Miss = true;
                while(this.L1Miss);
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private int dirt2int(String dir){
        int data = Integer.parseInt(dir,2);
        return data%2;
    }

    //Getters and setters
    public String[][] getL1() {
        return L1;
    }

    public boolean isPowerON() {
        return powerON;
    }

    public void setPowerON(boolean powerON) {
        this.powerON = powerON;
    }

    public boolean isL1Miss() {
        return L1Miss;
    }

    public void setL1Miss(boolean l1Miss) {
        L1Miss = l1Miss;
    }

    public boolean isBusWr() {
        return BusWr;
    }

    public void setBusWr(boolean busWr) {
        BusWr = busWr;
    }

    public boolean isBusRd() {
        return BusRd;
    }

    public void setBusRd(boolean busRd) {
        BusRd = busRd;
    }

    public String getMemDir() {
        return memDir;
    }

    public void setMemDir(String memDir) {
        this.memDir = memDir;
    }
    
    
    
}