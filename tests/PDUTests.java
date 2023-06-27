import htp.HTPSerializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pdu.*;

import java.io.*;

public class PDUTests {
    @Test
    void test() {
        System.out.println("hi");
    }

    @Test
    void getPDUTest() throws IOException {
        HTPSerializer serializer = new HTPSerializer();
        GET_PDU getPdu = new GET_PDUImpl("Test");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeGET_PDU(getPdu, baos);

        byte[] sendBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(sendBytes);

        // read header
        serializer.readProtocolName(bais);
        serializer.readPDUType(bais);

        GET_PDU recaivedGET_PDU = serializer.deSerializeGET_PDU(bais);
        Assertions.assertEquals(getPdu.getDateiname(), recaivedGET_PDU.getDateiname());
    }

    @Test
    void putPDUTest() throws IOException {
        HTPSerializer serializer = new HTPSerializer();
        PUT_PDU putPdu = new PUT_PDUImpl("Test",2 , new byte[]{1, 0});

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializePUT_PDU(putPdu, baos);

        byte[] sendBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(sendBytes);

        // read header
        serializer.readProtocolName(bais);
        serializer.readPDUType(bais);

        PUT_PDU recaivedPUT_PDU = serializer.deSerializePUT_PDU(bais);
        Assertions.assertEquals(putPdu.getDateiname(), recaivedPUT_PDU.getDateiname());
        Assertions.assertEquals(putPdu.getAnzahlDerBytes(), recaivedPUT_PDU.getAnzahlDerBytes());
        Assertions.assertArrayEquals(putPdu.getDateiInhalt(), recaivedPUT_PDU.getDateiInhalt());
    }

    @Test
    void errorPDUTest() throws IOException {
        HTPSerializer serializer = new HTPSerializer();
        ERROR_PDU errorPdu = new ERROR_PDUImpl("Test", ERROR_PDU.FEHLERCODE_1, ERROR_PDU.ERRORMELDUNG_1);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializer.serializeERROR_PDU(errorPdu, baos);

        byte[] sendBytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(sendBytes);

        // read header
        serializer.readProtocolName(bais);
        serializer.readPDUType(bais);

        ERROR_PDU recaivedERROR_PDU = serializer.deSerializeERROR_PDU(bais);
        Assertions.assertEquals(errorPdu.getDateiname(), recaivedERROR_PDU.getDateiname());
        Assertions.assertEquals(errorPdu.getFehlercode(), recaivedERROR_PDU.getFehlercode());
        Assertions.assertEquals(errorPdu.getErrormeldung(), recaivedERROR_PDU.getErrormeldung());
    }
}
