import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ProtocolEngineTests {

    @Test
    public void scenarioGetFile() throws IOException {
        String filename = "C:\\Users\\drago\\Documents\\ProjekteJava\\ProtocolEngines\\tests\\htpProtocolEngine\\First\\1.docx";

        /////// A ==== > GetFile (test.txt) ===> B /////////////////////////////////////////////

        // step 1: A ===> ByteArrayOutputStream
        // no input
        ByteArrayInputStream isA = new ByteArrayInputStream(new byte[0]);
        // output goes here
        ByteArrayOutputStream osA = new ByteArrayOutputStream();

        HTPSerializer serializerSideA = new HTPSerializer();
        HTPProtocolEngine engineA = new HTPProtocolEngineImpl(isA, osA, serializerSideA);

        engineA.getFile(filename);

        // step 2: ByteArrayOutputStream ===> B
        // and output was produced - get it...
        byte[] serializedDataA = osA.toByteArray();

        // .. input that output to other engine
        ByteArrayInputStream isB = new ByteArrayInputStream(serializedDataA);
        ByteArrayOutputStream osB = new ByteArrayOutputStream();

        HTPSerializer serializerSideB = new HTPSerializer();
        HTPProtocolEngine engineB = new HTPProtocolEngineImpl(isB, osB, serializerSideB);

        /////// B ==== > putFile () ===> A //////////////////////////////////////////
        // run it - let engine read from input stream
        engineB.readFromInputStream();

        // step 1: B ===> ByteArrayOutputStream
        // output expected - serialized putPDU

        byte[] outputB = osB.toByteArray();
        Assertions.assertTrue(outputB.length > 0);

        // step 1: ByteArrayOutputStream ===> A
        isA = new ByteArrayInputStream(outputB);
        // output goes here
        osA = new ByteArrayOutputStream();

        engineA = new HTPProtocolEngineImpl(isA, osB, serializerSideA);
        //run it
        engineA.readFromInputStream();

        // possible tests?


    }
}