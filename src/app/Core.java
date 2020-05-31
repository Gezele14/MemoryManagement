package APP;

public class Core extends Thread{
    //Atrubutos
    public boolean powerON = false;
    private boolean L1Miss = false;
    private boolean BusWr = false;
    private boolean BusRd = false;;
    private String id = "";
    private String fatherId = "";
    private String memDir = "";
    private String data = "";
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
            System.out.println("Nueva instruccion "+instruction+" generada para "+fatherId+","+id);
            this.MSIprotocol(instrDecoded);
            
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

    private void busWrL1(String dir){
        int x = dirt2int(dir);
        for (int i = 1; i < this.L1.length; i++) {
            if(this.L1[i][0].equals(Integer.toString(x))){
                this.L1[i][1] = "S";
                this.L1[i][2] = dir;
                this.L1[i][3] = this.data;
            }
        }
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
            int i = dirt2int(instr[3]) + 1;
            this.L1[i][1] = "M";
            this.L1[i][2] = instr[3];
            this.L1[i][3] = instr[4];
            System.out.println("Dato escrito en L1 de "+fatherId+","+id);
            this.BusWr = true;
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }else if(instr[2].equals("CALC")){
            System.out.println("Inicio de calculo en "+fatherId+","+id);
            try{
                Thread.sleep(1000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            System.out.println("Fin de calculo en "+fatherId+","+id);
        }else if (instr[2].equals("READ")){
            if(instrCheck(instr)){
                System.out.println("Inicio de lectura en L1 de "+fatherId+","+id);
                try{
                    Thread.sleep(250);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                System.out.println("Dato no esta en L1 de "+fatherId+","+id);
                this.L1Miss = true;
                this.memDir = instr[3];
                System.out.println("Trayendo el dato de "+fatherId+","+id+" desde L2");
                while(this.L1Miss){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                System.out.println("Actualizando L1 de "+fatherId+","+id+" con el dato leido");
                this.busWrL1(this.memDir);
            }
            System.out.println("Fin de lectura en L1 de "+fatherId+","+id);
            
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    public String getCoreId(){
        return this.id;
    }

    public boolean isL1Miss() {
        return L1Miss;
    }

    public void setL1Miss(boolean l1Miss) {
        L1Miss = l1Miss;
    }    
}