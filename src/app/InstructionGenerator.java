package APP;
import java.util.Random;

public class InstructionGenerator {

    //Atributos
    private String instruction;
    private int typeInstr;

    //Constructor
    public InstructionGenerator(){
        this.instruction = "";
        this.typeInstr = 0;
    }

    //Metodos
    /**
     * @param n: 
     * @param p:
     * @return Random Binomial nummber
     */
    private int Binomial(final int n, final double p){
        final double log_q = Math.log(1.0 - p);
        int x = 0;
        double sum = 0;
        for(;;) {
            sum += Math.log(Math.random()) / (n - x);
            if(sum < log_q) {
                return x;
            }
            x++;
        }
    }

    /**
     * 
     * @return 1-16 hex string
     */
    private String newMemDir(){
        Random r = new Random();
        final int number = r.nextInt(16);
        final String memDir = Integer.toBinaryString(number);

        if (memDir.length() == 1){
            return "000"+memDir;
        }else if (memDir.length() == 2){
            return "00"+memDir;
        }else if (memDir.length() == 3){
            return "0"+memDir;
        }else{
            return memDir;
        }
    }

    /**
     * 
     * @return type of instruction
     */
    private String newTypeInstr(){
        final int number = this.Binomial(2, 0.3333);
        this.typeInstr = number;
        if (number == 0){
            return "READ";
        }else if (number == 1){
            return "CALC";
        }else{
            return "WRITE";
        }
    }
    /**
     * 
     * @return
     */
    private String newData(){
        final Random r = new Random();
        final int data1 = r.nextInt(65535);
        final int data2 = r.nextInt(65535);
        
        String dataStr1 = Integer.toHexString(data1);
        String dataStr2 = Integer.toHexString(data2);
        if (dataStr1.length() == 1){
            return "000"+dataStr1+dataStr2;
        }else if (dataStr1.length() == 2){
            return "00"+dataStr1+dataStr2;
        }else if (dataStr1.length() == 3){
            return "0"+dataStr1+dataStr2;
        }else{
            return dataStr1+dataStr2;
        }
    }

    /**
     * 
     * @return New instruction 
     */
    public String newInstruction(final String processorId, final String coreId){
        this.instruction = processorId+";"+coreId+";"+this.newTypeInstr();
        if (typeInstr == 0){
            this.instruction += ";"+this.newMemDir();
            return this.instruction;
        }else if (typeInstr == 2){
            this.instruction += ";"+this.newMemDir()+";"+this.newData();
            return this.instruction;
        }else{
            return this.instruction;
        }
    }
}