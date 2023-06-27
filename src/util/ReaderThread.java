package util;

import htp.HTPProtocolEngineImpl;

import java.io.IOException;

public class ReaderThread implements Runnable {
    private HTPProtocolEngineImpl protocolMachine;

    public ReaderThread(HTPProtocolEngineImpl protocolMachine) {
        this.protocolMachine = protocolMachine;
    }

    /**
     * Die Methode run() wird vom Thread ausgeführt, sobald dieser gestartet wird.
     * Sie liest die Daten vom InputStream und übergibt sie an die Methode
     * readFromInputStream() der Klasse htp.HTPProtocolEngineImpl.
     */
    @Override
    public void run() {
        try {
            protocolMachine.readFromInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
