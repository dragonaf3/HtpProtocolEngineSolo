import java.io.IOException;
import java.util.Scanner;

public class UserInterfaceThread implements Runnable {

    private HTPProtocolEngineImpl protocolMachine;

    public UserInterfaceThread(HTPProtocolEngineImpl protocolMachine) {
        this.protocolMachine = protocolMachine;
    }

    @Override
    public void run() {
        try {
            // Algorithmen verwenden der get und put Methode
            System.out.println("1 für get \n 2 für put");
            System.out.println();

            switch (readFromSystemInInt()) {
                case 1 -> {
                    System.out.println("Dateiname eingeben: \n");
                    protocolMachine.getFile(readFromSystemInString());
                }
                case 2 -> {
                    System.out.println("Welche Datei möchten Sie schicken? \n");
                    protocolMachine.putFile(readFromSystemInString());
                    System.exit(0);
                }
                default -> {
                    System.out.println("Falsche Eingabe! Versuch es nochmal :D\n");
                    System.exit(0);
                }
            }

            System.out.println();

        } catch (
                IOException e) {
            e.printStackTrace();
        }

    }

    public int readFromSystemInInt() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public String readFromSystemInString() {
        Scanner scanner = new Scanner(System.in);
        return scanner.next();
    }
}
