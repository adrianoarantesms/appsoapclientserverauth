package hello;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
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
//			URL wsdl = new URL("http://localhost:8080/ws/countries.wsdl");
//			URL wsdl = new URL("https://172.27.1.40:8444/demosoapserver/ws/countries.wsdl");
			URL wsdl = new URL("https://localhost:8443/ws/countries.wsdl");
			
			//autenticacao
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(getKeyManagerFactory().getKeyManagers(), getTrustManagerFactory().getTrustManagers(), null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            //autenticacao
			String svcName = "CountriesPortService";
			String ns = "http://www.baeldung.com/springsoap/gen";

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

	public static KeyManagerFactory getKeyManagerFactory() {
		InputStream inputStream = null;
		KeyStore ts = null;
		KeyManagerFactory keyManagerFactory = null;
		try {
			ts = KeyStore.getInstance("JKS");
			inputStream = ProcessamentoDispatchHelper.class.getClassLoader().getResourceAsStream("javaclient.jks");
			ts.load(inputStream, "changeit".toCharArray());
			keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyManagerFactory.init(ts, "changeit".toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException
				| UnrecoverableKeyException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return keyManagerFactory;
	}

	public static TrustManagerFactory getTrustManagerFactory() {
		InputStream inputStream = null;
		KeyStore ts = null;
		TrustManagerFactory trustManagerFactory = null;
		try {
			ts = KeyStore.getInstance("JKS");
			inputStream = ProcessamentoDispatchHelper.class.getClassLoader().getResourceAsStream("certificate.jks");
			ts.load(inputStream, "changeit".toCharArray());
			trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			trustManagerFactory.init(ts);
		} catch (NoSuchAlgorithmException | CertificateException | KeyStoreException | IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return trustManagerFactory;
	}

	public static String fileToString(String arquivo) {
		try {
			return new String(Files.readAllBytes(Paths.get(arquivo)));
		} catch (IOException e) {
			return "";
		}
	}
}