package hello;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessamentoDispatchHelper {
	private static final Logger logger = LoggerFactory.getLogger(ProcessamentoDispatchHelper.class);

	
	public static String processar() {

		try {
			URL wsdl = new URL("http://localhost:8080/ws/countries.wsdl");

			String ns = "http://www.baeldung.com/springsoap/gen";
			String svcName = "CountriesPortService";
			QName svcQName = new QName(ns, svcName);
			Service service = Service.create(wsdl, svcQName);
			String portName = "CountriesPortSoap11";
			QName portQName = new QName(ns, portName);

			Dispatch<Source> dispatch = service.createDispatch(portQName, Source.class, Service.Mode.MESSAGE);
			String payload = fileToString("request.xml");
			StreamSource request = new StreamSource(new StringReader(payload));
			logger.debug(">>>dispatchPayload: Invoking...");

			Source bookResponse = dispatch.invoke(request);
			Transformer trans = TransformerFactory.newInstance().newTransformer();
			StreamResult dom2 = new StreamResult(new StringWriter());
			trans.transform(bookResponse, dom2);

			String xml = dom2.getWriter().toString();
			return xml;
		} catch (Exception e) {
			logger.error("Erro de cath...: " + e.getMessage());
		}
		return null;

	}

	public static String fileToString(String arquivo) {
		try {
			return new String(Files.readAllBytes(Paths.get(arquivo)));
		} catch (IOException e) {
			return "";
		}
	}
}