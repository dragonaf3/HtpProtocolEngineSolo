import java.io.*;
import java.net.*;

public class HTPEndpoint {
    public static void main(String[] args) {
        if (args.length != 1 && args.length != 2) {
            System.out.println("Verwendung: HTPEndpoint <Port> [<Hostname>]");
            return;
        }

        int port = Integer.parseInt(args[0]);
        startServer(port);

        if (args.length == 2) {
            String hostname = args[1];
            connectToServer(hostname, port);
        }
    }

    private static void startServer(int port) {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server gestartet auf Port: " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();

                HTPProtocolEngineImpl protocolEngine = createProtocolMachine(clientSocket);
                startReader(protocolEngine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void connectToServer(String hostname, int port) {
        try {
            Socket socket = new Socket(hostname, port);

            HTPProtocolEngineImpl protocolEngine = createProtocolMachine(socket);
            startReader(protocolEngine);

            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            boolean continueExecution = true;

            while (continueExecution) {
                System.out.println("Bitte wählen Sie eine Methode aus:");
                System.out.println("1. getFile");
                System.out.println("2. putFile");
                System.out.println("3. Beenden");

                String input = reader.readLine();

                System.out.print("\033[H\033[2J"); // Terminal löschen
                System.out.flush(); // Puffer leeren

                switch (input) {
                    case "1" -> {
                        System.out.println("Bitte geben Sie den Dateinamen ein:");
                        String filename = reader.readLine();
                        protocolEngine.getFile(filename);

                        System.out.print("\033[H\033[2J"); // Terminal löschen
                        System.out.flush(); // Puffer leeren

                        // Kurze Verzögerung, um auf die Error PDU zu warten
                        Thread.sleep(500); // 500 Millisekunden (0,5 Sekunden)
                    }
                    case "2" -> {
                        System.out.println("Bitte geben Sie den Dateinamen ein:");
                        String putFilename = reader.readLine();

                        protocolEngine.putFile(putFilename);
                    }
                    case "3" -> continueExecution = false;
                    default -> System.out.println("Ungültige Eingabe. Bitte wählen Sie erneut.");
                }

                System.out.println("\nVielen Dank für die Anfrage");

                continueExecution = false;

            }

            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void startReader(HTPProtocolEngineImpl protocolMachine) {
        ReaderThread readerThread = new ReaderThread(protocolMachine);
        Thread thread = new Thread(readerThread);
        thread.start();
    }

    private static HTPProtocolEngineImpl createProtocolMachine(Socket socket) throws IOException {
        HTPSerializer serializer = new HTPSerializer();
        HTPProtocolEngineImpl protocolMachine = new HTPProtocolEngineImpl(socket.getInputStream(), socket.getOutputStream(), serializer);
        return protocolMachine;
    }
}

