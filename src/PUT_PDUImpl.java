public class PUT_PDUImpl implements PUT_PDU {

    private final String dateiname;
    private final int anzahlDerBytes;
    private final byte[] dateiInhalt;

    public PUT_PDUImpl(String dateiname, int anzahlDerBytes, byte[] dateiInhalt) {
        this.dateiname = dateiname;
        this.anzahlDerBytes = anzahlDerBytes;
        this.dateiInhalt = dateiInhalt;
    }

    @Override
    public String getDateiname() {
        return this.dateiname;
    }

    @Override
    public int getAnzahlDerBytes() {
        return this.anzahlDerBytes;
    }

    @Override
    public byte[] getDateiInhalt() {
        return this.dateiInhalt;
    }
}
