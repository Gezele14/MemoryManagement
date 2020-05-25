package app;
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
        final int nummber = this.Binomial(16, 0.625);
        final String memDir = Integer.toBinaryString(nummber);

        if (memDir.length() == 3){
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

    private String newData(){
        final Random r = new Random();
        final int data = r.nextInt(65535);
        
        return Integer.toHexString(data);
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