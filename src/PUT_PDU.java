public interface PUT_PDU {
    /**
     * @return gibt Dateiname
     */
    String getDateiname();

    /**
     * @return gibt Anzahl der Bytes
     */
    int getAnzahlDerBytes();

    /**
     * @return gibt Fileinhalt
     */
    byte[] getDateiInhalt();

}
