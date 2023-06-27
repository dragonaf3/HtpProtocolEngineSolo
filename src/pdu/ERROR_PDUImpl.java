package pdu;

public class ERROR_PDUImpl implements ERROR_PDU {
    private final String dateiname;
    private final byte fehlercode;
    private final String errormeldung;

    public ERROR_PDUImpl(String dateiname, byte fehlercode, String errormeldung) {
        this.dateiname = dateiname;
        this.fehlercode = fehlercode;
        this.errormeldung = errormeldung;
    }

    @Override
    public String getDateiname() {
        return this.dateiname;
    }

    @Override
    public byte getFehlercode() {
        return this.fehlercode;
    }

    @Override
    public String getErrormeldung() {
        return this.errormeldung;
    }
}
