package app;

public class App {
    public static void main(String[] args) throws Exception {
        Cpu cpu0 = new Cpu("Processor0", "P0");
        Cpu cpu1 = new Cpu("Processor1", "P1");

        cpu0.start();
        cpu1.start();
    }
}