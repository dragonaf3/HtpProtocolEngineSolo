package pdu;

public class GET_PDUImpl implements GET_PDU {
    private final String dateiname;

    public GET_PDUImpl(String dateiname) {
        this.dateiname = dateiname;
    }

    @Override
    public String getDateiname() {
        return this.dateiname;
    }
}
