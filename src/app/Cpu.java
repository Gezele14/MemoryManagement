package APP;

import java.util.concurrent.Semaphore;

public class Cpu extends Thread{
    //Atributos
    private boolean L2Miss;
    public boolean powerON;
    private boolean isFromL2 = false;
    private boolean BusWr = false;
    private static Semaphore mutex = new Semaphore(1);
    private String id = "";
    private Core core0;
    private Core core1;
    private String memDir = "";
    private String Data = "";
    private String reqId = "";
    private String[] L2Row;
    private String[][] L2={{"Bloque", "Estado","Dueño", "Dir, Memoria", "Dato"},
                           {"0","","","","",""},
                           {"1","","","","",""},
                           {"2","","","","",""},
                           {"3","","","","",""}};

    //Constructor
    public Cpu(String msg, String _id, boolean _powerON){
        super(msg);
        this.id = _id;
        this.powerON = _powerON;
        this.core0 = new Core("Core0", this.id, "0");
        this.core1 = new Core("Core1", this.id, "1");

        this.core0.setPowerON(this.powerON);
        this.core1.setPowerON(this.powerON);
        
    }

    private void l1MisssManager(){
        if(core0.isL1Miss()){
            System.out.println("Buscando el dato de "+id+","+core0.getCoreId()+" en L2");
            this.memDir = core0.getMemDir();
            if(!this.memDirCheck(this.memDir)){
                System.out.println("El dato de "+id+","+core0.getCoreId()+" no está en L2");
                this.L2Miss = true;
                this.reqId = core0.getCoreId();
                System.out.println("Buscando el dato de "+id+","+core0.getCoreId()+" en la otra L2");
                while(this.L2Miss){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                if(this.isFromL2){
                    this.core0.setData(this.getL2Data(this.memDir));
                }else{
                    System.out.println("Escribiendo el dato de "+id+","+core0.getCoreId()+" en L2");
                    this.writeL2(this.L2Row);
                    this.core0.setData(this.getL2Data(this.memDir));
                }
                
                
            }else{
                this.core0.setData(this.getL2Data(this.memDir));
            }
            this.core0.setL1Miss(false);
            
        }else if(core1.isL1Miss()){
            this.memDir = core1.getMemDir();
            if(!this.memDirCheck(this.memDir)){
                System.out.println("El dato de "+id+","+core1.getCoreId()+" no está en L2");
                this.L2Miss = true;
                this.reqId = core1.getCoreId();
                System.out.println("Trayendo el dato de "+id+","+core1.getCoreId()+" desde memoria");
                while(this.L2Miss);
                System.out.println("Escribiendo el dato de "+id+","+core1.getCoreId()+" en L2");
                while(this.L2Miss){
                    try{
                        Thread.sleep(1000);
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                }
                if(this.isFromL2){
                    this.core1.setData(this.getL2Data(this.memDir));
                }else{
                    System.out.println("Escribiendo el dato de "+id+","+core0.getCoreId()+" en L2");
                    this.writeL2(this.L2Row);
                    this.core1.setData(this.getL2Data(this.memDir));
                }
            }else{
                this.core1.setData(this.getL2Data(this.memDir));
            }
            this.core1.setL1Miss(false);
        }
    }

    private void busWrManager(){
        if(this.core0.isBusWr()){
            this.writeCache(core0.getMemDir(), core0.getData(), core0.getCoreId());
            core1.invalidCache(core0.getMemDir());
            this.memDir = core0.getMemDir();
            this.Data = core0.getData();
            this.BusWr = true;
            while(this.BusWr){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            this.core0.setBusWr(false);

        }else if(this.core1.isBusWr()){
            this.writeCache(core1.getMemDir(), core1.getData(), core1.getCoreId());
            core0.invalidCache(core1.getMemDir());
            this.memDir = core1.getMemDir();
            this.Data = core1.getData();
            this.BusWr = true;
            while(this.BusWr){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
            this.core1.setBusWr(false);

        }
    }

    private void writeCache(String dir, String Data, String coreId){
        String bloque = Integer.toString(Integer.parseInt(dir,2)%4);
        for (int i = 1; i < this.L2.length; i++) {
            if(bloque.equals(this.L2[i][0])){
                this.L2[i][1] = "DM";
                this.L2[i][2] = this.id+","+coreId;
                this.L2[i][3] = dir;
                this.L2[i][4] = Data;
                break;
            }
        }

    }

    private void writeL2(String[] row){
        for (int i = 1; i < this.L2.length; i++) {
            if(row[0].equals(L2[i][0])){
                this.L2[i] = row;
            }
        }
    }

    public void invalidCache(String dir){
        for (int i = 1; i < this.L2.length; i++) {
            if(dir.equals(this.L2[i][3])){
                this.L2[i][1] = "DI";
                break;
            }
        }
        this.core0.invalidCache(dir);
        this.core1.invalidCache(dir);
    }

    public String getDataDir(String dir){
        String Temp = "";
        for (int i = 1; i < L2.length; i++) {
            if(dir.equals(this.L2[i][3])){
                Temp = this.L2[i][4];
            }
        }

        return Temp;
    }

    public String[] getRow(String memdir) {
        for (int i = 1; i < L2.length; i++) {
            if(memdir.equals(this.L2[i][3])){
                this.L2Row = this.L2[i];
            }
        }
        return this.L2Row;
    }

    private String getL2Data(String dir){
        for (int i = 1; i < this.L2.length; i++) {
            if(dir.equals(this.L2[i][3])){
                this.Data = this.L2[i][4];
                return Data;
            }
        }
        return this.Data;
    }

    public boolean memDirCheck(String dir){
        for (int i = 1; i < this.L2.length; i++) {
            if(dir.equals(this.L2[i][3]) && !this.L2[i][1].equals("DI")){
                this.Data = this.L2[i][4];
                return true;
            }
        }
        return false;
    }

    public void setL2Value(int pos, String dir, String value){
        for (int i = 1; i < this.L2.length; i++) {
            if(dir.equals(this.L2[i][3])){
                this.L2[i][pos] += value;
                break;
            }
        }
    }

    public String getL2Value(int pos, String dir){
        String value = "";
        for (int i = 1; i < this.L2.length; i++) {
            if(dir.equals(this.L2[i][3])){
                value = this.L2[i][pos];
            }
        }
        return value;
    }

    //Metodos
    public void run(){
        this.core0.start();
        this.core1.start();
        while(this.powerON){
            try {
                mutex.acquire();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            this.busWrManager();
            this.l1MisssManager();
            mutex.release();
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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getCpuId(){
        return this.id;
    }

    public String[] getL2Row() {
        return L2Row;
    }

    public void setL2Row(String[] l2Row) {
        L2Row = l2Row;
    }

    public String getReqId() {
        return reqId;
    }

    public boolean isFromL2() {
        return isFromL2;
    }

    public void setFromL2(boolean isFromL2) {
        this.isFromL2 = isFromL2;
    }

    public boolean isBusWr() {
        return BusWr;
    }

    public void setBusWr(boolean busWr) {
        BusWr = busWr;
    }

}