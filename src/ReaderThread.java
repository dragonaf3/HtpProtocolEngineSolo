import java.io.IOException;

class ReaderThread implements Runnable {
    private HTPProtocolEngineImpl protocolMachine;

    public ReaderThread(HTPProtocolEngineImpl protocolMachine) {
        this.protocolMachine = protocolMachine;
    }

    @Override
    public void run() {
        try {
            // Implementieren Sie den Algorithmus zum Lesen von InputStream
            protocolMachine.readFromInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
