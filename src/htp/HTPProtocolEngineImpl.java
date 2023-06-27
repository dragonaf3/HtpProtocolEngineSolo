package htp;

import pdu.*;

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
    public void putFile(String filenname) throws IOException {
        File file = new File(filenname);

        if (file.isFile()) {

            try {

                FileInputStream fis = new FileInputStream(filenname);
                byte[] fileContent = fis.readAllBytes();
                fis.close();

                PUT_PDU putPdu = new PUT_PDUImpl(filenname, fileContent.length, fileContent);
                // send PDU
                this.serializer.serializePUT_PDU(putPdu, this.os);

            } catch (IOException e) {

                System.out.println("Fehler beim Lesen der Datei: " + e.getMessage());
            }


        } else {

            System.out.println("Datei existiert nicht oder ist keine normale Datei oder Zugriff verweigert");

        }
    }

    @Override
    public void readFromInputStream() throws IOException {
        DataInputStream dais = new DataInputStream(this.is);

        // Lese Protokoll-Header
        HtpPDU_Type pduType = this.serializer.readProtocolHeader(dais);

        switch (pduType) {
            case GET -> {
                GET_PDU getPDU = this.serializer.deSerializeGET_PDU(this.is);

                // Erfolgreich deserialisierte pdu - verarbeite es
                this.handleGET_PDU(getPDU);
            }
            case PUT -> {
                PUT_PDU putPDU = this.serializer.deSerializePUT_PDU(this.is);

                // Erfolgreich deserialisierte pdu - verarbeite es
                this.handlePUT_PDU(putPDU);
            }
            case ERROR -> {
                ERROR_PDU errorPDU = this.serializer.deSerializeERROR_PDU(this.is);

                // Erfolgreich deserialisierte pdu - verarbeite es
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

    /**
     * Verarbeitet eine GET-PDU
     *
     * @param getPdu GET-PDU
     */
    private void handleGET_PDU(GET_PDU getPdu) throws IOException {
        File file = new File(getPdu.getDateiname());

        if (file.isFile()) {

            try {

                FileInputStream fis = new FileInputStream(file);
                byte[] fileContent = fis.readAllBytes();
                fis.close();

                PUT_PDU putPdu = new PUT_PDUImpl(getPdu.getDateiname(), fileContent.length, fileContent);
                this.serializer.serializePUT_PDU(putPdu, this.os);

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

    /**
     * Verarbeitet eine PUT-PDU
     *
     * @param putPdu PUT-PDU
     */
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

            System.out.println("Fehler beim Schreiben der Datei: " + dateiname);

        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    System.out.println("Fehler beim Schließen der Datei: " + dateiname);
                }
            }
        }
    }

    /**
     * Verarbeitet eine ERROR-PDU
     *
     * @param errorPdu ERROR-PDU
     */
    private void handleERROR_PDU(ERROR_PDU errorPdu) {
        System.out.println("ERROR-PDU empfangen: \n" +
                "Dateiname: " + errorPdu.getDateiname() + "\n" +
                "Fehlercode: " + errorPdu.getFehlercode() + "\n" +
                "Fehlermeldung: " + errorPdu.getErrormeldung());

    }
}

