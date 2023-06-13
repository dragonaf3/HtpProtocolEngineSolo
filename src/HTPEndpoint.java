import java.io.*;
import java.net.*;

public class HTPEndpoint {
    public static void main(String[] args) {
        if (args.length == 1) {
            int port = Integer.parseInt(args[0]);
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                Socket clientSocket = serverSocket.accept();

                HTPSerializer serializer = new HTPSerializer();
                HTPProtocolEngineImpl protocolMachine = new HTPProtocolEngineImpl(clientSocket.getInputStream(), clientSocket.getOutputStream(), serializer);

                ReaderThread readerThread = new ReaderThread(protocolMachine);
                Thread thread = new Thread(readerThread);
                thread.start();

                // Serverseite, deshalb auskommentieren
                //protocolMachine.getFile("example.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (args.length == 2) {
            int port = Integer.parseInt(args[0]);
            String hostname = args[1];
            try {
                Socket socket = new Socket(hostname, port);

                HTPSerializer serializer = new HTPSerializer();
                HTPProtocolEngineImpl protocolMachine = new HTPProtocolEngineImpl(socket.getInputStream(), socket.getOutputStream(), serializer);

                // Liest Input Stream
                ReaderThread readerThread = new ReaderThread(protocolMachine);
                Thread threadInputstream = new Thread(readerThread);
                threadInputstream.start();

                // Fragt welche Funktion verwendet werde soll
                UserInterfaceThread userInterfaceThread = new UserInterfaceThread(protocolMachine);
                Thread threadUser = new Thread(userInterfaceThread);
                threadUser.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

