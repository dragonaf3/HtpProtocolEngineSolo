import java.io.*;

public class HTPSerializer {
    public static final String PROTOCOL_NAME = "HTP1.0";
    public static final int GET_PDU_BYTE = 1;
    public static final int PUT_PDU_BYTE = 2;
    public static final int ERROR_PDU_BYTE = 3;

    String readProtocolName(InputStream is) throws IOException {
        DataInputStream daos = new DataInputStream(is);
        return daos.readUTF();
    }

    int readPDUType(InputStream is) throws IOException {
        return is.read();
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //                                  protocol header                                 //
    //////////////////////////////////////////////////////////////////////////////////////

    void sendProtocolHeader(OutputStream os, HtpPDU_Type pduType) throws IOException {
        DataOutputStream daos = new DataOutputStream(os);

        daos.writeUTF(PROTOCOL_NAME);

        switch (pduType) {
            case GET -> os.write(GET_PDU_BYTE);
            case PUT -> os.write(PUT_PDU_BYTE);
            case ERROR -> os.write(ERROR_PDU_BYTE);
            default -> throw new IOException("unknown pdu type byte:" + pduType);
        }
    }

    HtpPDU_Type readProtocolHeader(DataInputStream dais) throws IOException {
        String protocolString = dais.readUTF();

        if (!protocolString.equalsIgnoreCase(PROTOCOL_NAME)) {
            throw new IOException("wrong protocol on stream - give up");
        }

        byte pduType = (byte) dais.read();
        switch (pduType) {
            case GET_PDU_BYTE -> {
                return HtpPDU_Type.GET;
            }
            case PUT_PDU_BYTE -> {
                return HtpPDU_Type.PUT;
            }
            case ERROR_PDU_BYTE -> {
                return HtpPDU_Type.ERROR;
            }
            default -> throw new IOException("unknown pdu type byte:" + pduType);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //                                      GETPDU                                      //
    //////////////////////////////////////////////////////////////////////////////////////

    void serializeGET_PDU(GET_PDU getPdu, OutputStream os) throws IOException {
        DataOutputStream daos = new DataOutputStream(os);

        // sende header
        this.sendProtocolHeader(os, HtpPDU_Type.GET);

        // schreibe Dateiname
        daos.writeUTF(getPdu.getDateiname());
    }

    GET_PDU deSerializeGET_PDU(InputStream is) throws IOException {
        DataInputStream dais = new DataInputStream(is);

        // Header bereits gelesen

        // lese Dateiname
        String dateiname = dais.readUTF();

        return new GET_PDUImpl(dateiname);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //                                   PUT_PDU                                        //
    //////////////////////////////////////////////////////////////////////////////////////

    void serializePUT_PDU(PUT_PDU putPdu, OutputStream os) throws IOException {
        DataOutputStream daos = new DataOutputStream(os);

        // sende Header
        this.sendProtocolHeader(os, HtpPDU_Type.PUT);

        // schreibe Dateiname
        daos.writeUTF(putPdu.getDateiname());
        // schreibe Anzahl der Bytes
        daos.writeInt(putPdu.getAnzahlDerBytes());
        // schreibe Fileinhalt
        daos.write(putPdu.getDateiInhalt());
    }

    PUT_PDU deSerializePUT_PDU(InputStream is) throws IOException {
        DataInputStream dais = new DataInputStream(is);

        // Header bereits gelesen

        // lese Dateiname
        String dateiname = dais.readUTF();
        // lese Anzahl der Bytes
        int anzahlDerBytes = dais.readInt();
        // lese Fileinhalt
        byte[] dateiInhalt = new byte[anzahlDerBytes];
        dais.readFully(dateiInhalt);

        return new PUT_PDUImpl(dateiname, anzahlDerBytes, dateiInhalt);
    }

    //////////////////////////////////////////////////////////////////////////////////////
    //                                   ERROR_PDU                                      //
    //////////////////////////////////////////////////////////////////////////////////////

    void serializeERROR_PDU(ERROR_PDU errorPdu, OutputStream os) throws IOException {
        DataOutputStream daos = new DataOutputStream(os);

        // sende Header
        this.sendProtocolHeader(os, HtpPDU_Type.ERROR);

        // schreibe Dateiname
        daos.writeUTF(errorPdu.getDateiname());
        // schreibe Fehlercode
        daos.writeByte(errorPdu.getFehlercode());
        // schreibe Errormeldung
        daos.writeUTF(errorPdu.getErrormeldung());
    }

    ERROR_PDU deSerializeERROR_PDU(InputStream is) throws IOException {
        DataInputStream dais = new DataInputStream(is);

        // Header bereits gelesen

        // lese Dateiname
        String dateiname = dais.readUTF();
        // lese Fehlercode
        byte fehlercode = dais.readByte();
        // lese Errormeldung
        String errormeldung = dais.readUTF();

        return new ERROR_PDUImpl(dateiname, fehlercode, errormeldung);
    }

}
