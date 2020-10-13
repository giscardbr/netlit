package br.com.netlit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import br.com.netlit.accounts.domain.account.general.verification.MailClient;
import br.com.netlit.accounts.domain.event.EmailEvent;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class LogMessagetHandler implements SOAPHandler<SOAPMessageContext> {

	private String req;
	private String res;
	private String error;

	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private MailClient mailClient;

//	public LogMessagetHandler(
//			MailClient mailClient,
//			final ApplicationEventPublisher publisher) {
//		this.mailClient = mailClient;
//		this.publisher = publisher;
//	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		Boolean outboundProperty = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (outboundProperty.booleanValue()) {
			System.out.println("\nOutbound message:");
			try {
				SOAPMessage msg = context.getMessage();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				msg.writeTo(out);
				req = getSOAPMessageAsString(msg);
				msg.writeTo(System.out);
			} catch (SOAPException ex) {
				log.error(ex);
			} catch (IOException ex) {
				log.error(ex);
			}
		} else {
			System.out.println("\nInbound message:");
			try {
				SOAPMessage msg = context.getMessage();
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				msg.writeTo(out);
				res = getSOAPMessageAsString(msg);
				msg.writeTo(System.out);
			} catch (SOAPException ex) {
				log.error(ex);
			} catch (IOException ex) {
				log.error(ex);
			}
		}

		return true;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		System.out.println("Client : handleFault()......");
		try {
			SOAPMessage msg = context.getMessage();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			msg.writeTo(out);
			error = getSOAPMessageAsString(msg);
			msg.writeTo(System.out);
		} catch (SOAPException ex) {
			log.error(ex);
		} catch (IOException ex) {
			log.error(ex);
		}
		return true;
	}

	@Override
	public void close(MessageContext context) {
		System.out.println("Client : close()......");
		
		publisher.publishEvent(
				new EmailEvent(
						this, 
						String.format("REQUEST \n\n%s\n\nRESPONSE\n\n%s\n\nERROR\n\n%s", req, res, error), 
						null, 
						new String[] { "giscard.ti+protheus@gmail.com", "digodecastro+prothues@gmail.com" }, 
						(error != null ? "ERROR" : "SUCCESS") + " CALL PROTHEUS",
						false));
		
//		mailClient.sendEmail((error != null ? "ERROR" : "SUCCESS") + " CALL PROTHEUS",
//				new String[] { "giscard.ti+protheus@gmail.com", "digodecastro+prothues@gmail.com" },
//				String.format("REQUEST \n\n%s\n\nRESPONSE\n\n%s\n\nERROR\n\n%s", req, res, error),
//				false);
	}

	@Override
	public Set<QName> getHeaders() {
		System.out.println("Client : getHeaders()......");
		return null;
	}

	public static String getSOAPMessageAsString(SOAPMessage soapMessage) {
		try {

			TransformerFactory tff = TransformerFactory.newInstance();
			Transformer tf = tff.newTransformer();

			// Set formatting

			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

			Source sc = soapMessage.getSOAPPart().getContent();

			ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			StreamResult result = new StreamResult(streamOut);
			tf.transform(sc, result);

			String strMessage = streamOut.toString();
			return strMessage;
		} catch (Exception e) {
			System.out.println("Exception in getSOAPMessageAsString " + e.getMessage());
			return null;
		}

	}

}
