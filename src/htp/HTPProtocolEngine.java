package htp;

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
    void putFile(String filenname) throws IOException;

    /**
     * App bittet die Protokoll-Engine, die eingehenden Daten zu lesen und zu verarbeiten.
     */
    void readFromInputStream() throws IOException;
}
