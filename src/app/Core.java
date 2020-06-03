package APP;

import java.util.concurrent.Semaphore;

import Logging.Log;

public class Core extends Thread{
    //Atrubutos
    public boolean powerON = false;
    private boolean L1Miss = false;
    private boolean BusWr = false;
    private static Semaphore mutex = new Semaphore(1);
    private Log log;
    private String coreState = "";
    private String id = "";
    private String fatherId = "";
    private String memDir = "";
    private String data = "";
    private String logName;
    private String[][] L1 = {{"Bloque", "Coherencia", "Dir. Mem", "Dato"},
                             {"0","","",""},
                             {"1","","",""}};
    InstructionGenerator instr = new InstructionGenerator();

    //Constructor
    public Core(String msg,String _fatherId, String _id){
        super(msg);
        this.id = _id;
        this.fatherId = _fatherId;
        this.logName = "SystemLog";
        this.log = new Log(this.logName);
    }

    
    //Metodos
    public void run(){
        while (this.powerON){
            String instruction = this.instr.newInstruction(this.fatherId, this.id);
            String[] instrDecoded = this.instrDecoder(instruction);
            this.coreState = "Nueva instruccion "+instruction+" generada para "+fatherId+","+id;
            this.newLog(this.coreState);
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

    public void newLog(String msg){
        this.log.newInfo(msg);

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

    private boolean invalidCheck(String[] instr){
        for (int i = 1; i < this.L1.length; i++) {
            if(instr.length > 3){
                if(instr[3].equals(this.L1[i][2])){
                   if(L1[i][1].equals("I")){
                       return false;
                   }
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

    public void invalidCache(String dir){
        for (int i = 1; i < this.L1.length; i++) {
            if(dir.equals(this.L1[i][2])){
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
            this.coreState = "Dato escrito en L1 de "+fatherId+","+id;
            this.newLog(this.coreState);
            this.memDir = instr[3];
            this.data = instr[4];
            this.BusWr = true;
            while(this.BusWr){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            try{
                Thread.sleep(3000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            this.coreState = "Fin de escritura en "+fatherId+","+id;
            this.newLog(this.coreState);
        }else if(instr[2].equals("CALC")){
            this.coreState = "Inicio de calculo en "+fatherId+","+id;
            this.newLog(this.coreState);
            try{
                Thread.sleep(4000);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
            this.coreState = "Fin de calculo en "+fatherId+","+id;
            this.newLog(this.coreState);
        }else if (instr[2].equals("READ")){
            if(instrCheck(instr)){
                this.coreState = "Inicio de lectura en L1 de "+fatherId+","+id;
                this.newLog(this.coreState);
                if(this.invalidCheck(instr)){
                    this.memDir = instr[3];
                    this.L1Miss = true;
                    while(this.L1Miss){
                        try{
                            Thread.sleep(1000);
                        }catch(Exception e){
                            System.out.println(e.getMessage());
                        }
                    }
                    this.busWrL1(this.memDir);
                }
                try{
                    Thread.sleep(2000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }else{
                this.coreState = "Dato no esta en L1 de "+fatherId+","+id;
                try{
                    Thread.sleep(2000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
                this.newLog(this.coreState);
                this.memDir = instr[3];
                this.L1Miss = true;
                this.coreState = "Trayendo el dato de "+fatherId+","+id+" desde L2";
                this.newLog(this.coreState);
                while(this.L1Miss){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                this.coreState = "Actualizando L1 de "+fatherId+","+id+" con el dato leido";
                this.newLog(this.coreState);
                this.busWrL1(this.memDir);
            }
            this.coreState = "Fin de lectura en L1 de "+fatherId+","+id;
            this.newLog(this.coreState);
            
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

    public String getCoreState() {
        return coreState;
    }

    public void setCoreState(String coreState) {
        this.coreState = coreState;
    }

    
    
}