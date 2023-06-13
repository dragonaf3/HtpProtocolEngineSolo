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

                // Hier können Sie eine beliebige Methode Ihrer Protokollmaschine aufrufen
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
                ReaderThread readerThread = new ReaderThread(protocolMachine);
                Thread thread = new Thread(readerThread);
                thread.start();

                // Hier können Sie eine beliebige Methode Ihrer Protokollmaschine aufrufen
                protocolMachine.getFile("123.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

