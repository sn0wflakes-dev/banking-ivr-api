package aji.intern.restapi.utils;

import aji.intern.restapi.dto.SoapFaultResponse;
import jakarta.xml.soap.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XmlParserUtilTest {

    @Test
    void shouldParseSoapFaultDetail() throws SOAPException {
        SOAPMessage soapMessages = MessageFactory.newInstance().createMessage();
        SOAPBody soapBody = soapMessages.getSOAPBody();
        SOAPFault soapFault = soapBody.addFault();

        Detail detail = soapFault.addDetail();
        var faultRes = detail.addChildElement("ResponseHeader");

        faultRes.addChildElement("responseCode")
                .addTextNode("04");

        faultRes.addChildElement("messageId")
                .addTextNode("MSG-123");

        faultRes.addChildElement("errorOrigin")
                .addTextNode("ws");

        faultRes.addChildElement("responseMessage")
                .addTextNode("Customer not found");

        SoapFaultResponse soapFaultResponse = XmlParserUtil.xmlFaultParser(detail);

        Assertions.assertNotNull(soapFaultResponse);
        Assertions.assertEquals("04", soapFaultResponse.getResponseCode());
        Assertions.assertEquals("MSG-123", soapFaultResponse.getMessageId());
        Assertions.assertEquals("ws", soapFaultResponse.getErrorOrigin());
        Assertions.assertEquals("Customer not found", soapFaultResponse.getResponseMessage());
    }

    @Test
    void shouldReturnDefaultFaultDetailWhenFail() throws SOAPException {
        SOAPMessage soapMessages = MessageFactory.newInstance().createMessage();
        SOAPBody soapBody = soapMessages.getSOAPBody();
        SOAPFault soapFault = soapBody.addFault();

        Detail detail = soapFault.addDetail();

        SoapFaultResponse soapFaultResponse = XmlParserUtil.xmlFaultParser(detail);

        Assertions.assertNotNull(soapFaultResponse);
        Assertions.assertEquals("N/A", soapFaultResponse.getResponseCode());
        Assertions.assertEquals("N/A", soapFaultResponse.getMessageId());
        Assertions.assertEquals("N/A", soapFaultResponse.getErrorOrigin());
        Assertions.assertEquals("N/A", soapFaultResponse.getResponseMessage());
    }
}
