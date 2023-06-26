public interface ERROR_PDU {
    public static final byte FEHLERCODE_1 = 0x01;
    public static final byte FEHLERCODE_2 = 0x02;
    public static final byte FEHLERCODE_F = (byte) 0xFF;
    public static final String ERRORMELDUNG_1 = "Datei existiert nicht oder ist keine normale Datei oder Zugriff verweigert";
    public static final String ERRORMELDUNG_2 = "Fehler beim Schreiben der Datei";
    public static final String ERRORMELDUNG_F = "Fehler beim Schließen des Streams";




    /**
     * Gibt Dateiname zurueck
     *
     * @return gibt Dateiname
     */
    String getDateiname();

    /**
     * Gibt Fehlercode zurueck
     *
     * @return gibt Fehlercode
     */
    byte getFehlercode();

    /**
     * Gibt Errormeldung zurueck
     *
     * @return gibt Errormeldung
     */
    String getErrormeldung();
}
