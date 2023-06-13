import java.io.IOException;

public interface HTPProtocolEngine {

    /**
     * App bittet die Protokoll-Engine von einer anderen Maschine eine File zu holen.
     *
     * @param filenname Name der Datei
     */
    void getFile(String filenname) throws IOException;

    /**
     * App bittet die Protokoll-Engine ein File auf einer anderen Maschine zu schreiben.
     *
     * @param filenname Name der Datei
     */
    void putFile(String filenname, int anzahlDerByte, byte[] dateiInhalt) throws IOException;

    void readFromInputStream() throws IOException;
}
