import java.io.*;

public class HTPProtocolEngineImpl implements HTPProtocolEngine {
    private final InputStream is;
    private final OutputStream os;
    private final HTPSerializer serializer;

    public HTPProtocolEngineImpl(InputStream is, OutputStream os, HTPSerializer serializer) {
        this.is = is;
        this.os = os;
        this.serializer = serializer;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                     called from application                                //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void getFile(String filenname) throws IOException {
        GET_PDUImpl getPdu = new GET_PDUImpl(filenname);

        // send PDU
        this.serializer.serializeGET_PDU(getPdu, this.os);
    }

    @Override
    public void putFile(String filenname, int anzahlDerByte, byte[] dateiInhalt) throws IOException {
        PUT_PDUImpl putPdu = new PUT_PDUImpl(filenname, anzahlDerByte, dateiInhalt);

        // send PDU
        this.serializer.serializePUT_PDU(putPdu, this.os);
    }

    @Override
    public void readFromInputStream() throws IOException {
        DataInputStream dais = new DataInputStream(this.is);

        // read header
        HtpPDU_Type pduType = this.serializer.readProtocolHeader(dais);

        switch (pduType) {
            case GET -> {
                GET_PDU getPDU = this.serializer.deSerializeGET_PDU(this.is);

                // successfully deserialized pdu - process it
                this.handleGET_PDU(getPDU);
            }
            case PUT -> {
                PUT_PDU putPDU = this.serializer.deSerializePUT_PDU(this.is);

                // successfully deserialized pdu - process it
                this.handlePUT_PDU(putPDU);
            }
            case ERROR -> {
                ERROR_PDU errorPDU = this.serializer.deSerializeERROR_PDU(this.is);

                // successfully deserialized pdu - process it
                this.handleERROR_PDU(errorPDU);
            }
            default -> {
                throw new IOException("Unknown PDU type: " + pduType);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    //                                    protocol actions                                    //
    ////////////////////////////////////////////////////////////////////////////////////////////

    private void handleGET_PDU(GET_PDU getPdu) throws IOException {
        File file = new File(getPdu.getDateiname());

        if (file.isFile()) {

            try {

                FileInputStream fis = new FileInputStream(file);
                byte[] fileContent = fis.readAllBytes();
                fis.close();

                this.putFile(getPdu.getDateiname(), fileContent.length, fileContent);

            } catch (IOException e) {

                // Erstelle und sende ERROR-PDU
                ERROR_PDU errorPdu = new ERROR_PDUImpl(getPdu.getDateiname(), ERROR_PDU.FEHLERCODE_2, ERROR_PDU.ERRORMELDUNG_2);
                this.serializer.serializeERROR_PDU(errorPdu, this.os);
            }


        } else {

            // Erstelle und sende ERROR-PDU
            ERROR_PDU errorPdu = new ERROR_PDUImpl(getPdu.getDateiname(), ERROR_PDU.FEHLERCODE_1, ERROR_PDU.ERRORMELDUNG_1);
            this.serializer.serializeERROR_PDU(errorPdu, this.os);

        }
    }

    private void handlePUT_PDU(PUT_PDU putPdu) throws IOException {
        String dateiname = putPdu.getDateiname();
        byte[] dateiInhalt = putPdu.getDateiInhalt();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(dateiname);
            fos.write(dateiInhalt);
            fos.flush();
            System.out.println("Datei erfolgreich geschrieben: " + dateiname);

        } catch (IOException e) {

            System.err.println(ERROR_PDU.ERRORMELDUNG_2);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.err.println(ERROR_PDU.ERRORMELDUNG_F);
                }
            }
        }
    }

    private void handleERROR_PDU(ERROR_PDU errorPdu) {
        System.out.println("ERROR-PDU empfangen: \n "
                + "Dateiname: " + errorPdu.getDateiname() + "\n" +
                "Fehlercode: " + errorPdu.getFehlercode() + "\n" +
                "Fehlermeldung: " + errorPdu.getErrormeldung());

    }
}

