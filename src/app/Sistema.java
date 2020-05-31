package APP;

import java.util.concurrent.Semaphore;

public class Sistema extends Thread{

  private Cpu cpu0;
  private Cpu cpu1;
  private boolean powerON = true;
  private String data= "";
  private static Semaphore mutex = new Semaphore(1);

  private String[][] Memory ={
    {"Bloque", "Dueño", "Dato"},
    {"0000","","0000"},
    {"0001","","0000"},
    {"0010","","0000"},
    {"0011","","0000"},
    {"0100","","0000"},
    {"0101","","0000"},
    {"0110","","0000"},
    {"0111","","0000"},
    {"1000","","0000"},
    {"1001","","0000"},
    {"1010","","0000"},
    {"1011","","0000"},
    {"1100","","0000"},
    {"1101","","0000"},
    {"1110","","0000"},
    {"1111","","0000"},
  };

  public Sistema(String msg){
    super(msg);

    this.cpu0 = new Cpu("Processor0", "P0", true); 
    this.cpu1 = new Cpu("Processor1","P1", true);
  }

  private void caheManage(){
    if(this.cpu0.isL2Miss()){
      if(cpu1.memDirCheck(cpu0.getMemDir())){
        String Owner = this.cpu1.getL2Value(2, this.cpu0.getMemDir());
        int len = Owner.length();
        if(!Owner.substring(len-2).equals(";E")){
          this.cpu1.setL2Value(2, cpu0.getMemDir(), ";E");
        }
        this.cpu0.setData(this.cpu1.getDataDir(this.cpu0.getMemDir()));
        this.cpu0.setFromL2(true);
      }else{
        this.readMem(this.cpu0.getMemDir(),this.cpu0.getCpuId());
        int bloque = Integer.parseInt(cpu0.getMemDir(),2) % 4;
        String owner = cpu0.getCpuId()+","+cpu0.getReqId();
        String[] Row = {Integer.toString(bloque),"DS",owner,cpu0.getMemDir(),this.data};
        this.cpu0.setL2Row(Row);
      }
      cpu0.setL2Miss(false);

    }else if(this.cpu1.isL2Miss()){
      if(cpu0.memDirCheck(cpu1.getMemDir())){
        String Owner = this.cpu0.getL2Value(2, this.cpu1.getMemDir());
        int len = Owner.length();
        if(!Owner.substring(len-2).equals(";E")){
          this.cpu0.setL2Value(2, cpu1.getMemDir(), ";E");
        }
        this.cpu1.setData(this.cpu0.getDataDir(this.cpu1.getMemDir()));
        this.cpu1.setFromL2(true);
      }else{
        this.readMem(this.cpu1.getMemDir(),this.cpu1.getCpuId());
        int bloque = Integer.parseInt(cpu1.getMemDir(),2) % 4;
        String owner = cpu1.getCpuId()+","+cpu1.getReqId();
        String[] Row = {Integer.toString(bloque),"DS",owner,cpu1.getMemDir(),this.data};
        this.cpu1.setL2Row(Row);
      }
      cpu1.setL2Miss(false);
    }
  }

  private void readMem(String dir, String reqId){
    for (int i = 1; i < Memory.length; i++){
      if(dir.equals(this.Memory[i][0])){
        if(this.Memory[i][1].equals("")){
          this.Memory[i][1] = reqId;

        }else if(Memory[i][1].equals("P0")){
          this.Memory[i][1] += ", P1";

        }else if(Memory[i][1].equals("P1")){
          this.Memory[i][1] += ", P0";
        }
        this.data = this.Memory[i][2];
      }
    }
  }

  public void run(){
    this.cpu0.start();
    this.cpu1.start();
    while(this.powerON){
      try {
        mutex.acquire();
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
      this.caheManage();
      mutex.release();
    }
  }

  //Getters and Setters
  public String[][] getMemory() {
    return Memory;
  }

  public Cpu getCpu0() {
    return cpu0;
  }

  public Cpu getCpu1() {
    return cpu1;
  }
  
}