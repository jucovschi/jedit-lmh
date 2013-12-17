package info.kwarc.sally.jedit.stex;

import info.kwarc.sally.jedit.mmt.ResultInstance;
import info.kwarc.sally.jedit.mmt.ResultListWrapper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class MMT {
	String mmturl;
	String charset = "UTF-8";
	XmlMapper xmlMapper = new XmlMapper();
	
	public MMT(String mmturl) {
		this.mmturl = mmturl;
	}

	public String sendMessage(String body) {
		URLConnection connection;
		try {
			connection = new URL(mmturl+"/:query").openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "text/plain");
			OutputStream output = connection.getOutputStream();
			try {
				output.write(body.getBytes(charset));
			} finally {
				try { output.close(); } catch (IOException logOrIgnore) {}
			}
			StringWriter writer = new StringWriter();
			IOUtils.copy(connection.getInputStream(), writer, charset);
			return writer.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	List<String> flattenTheory(String uri) {
		String result = sendMessage(String.format("<related><literal uri='%s'/>  <sequence><transitive><toobject relation='Includes'/></transitive><toobject relation='Declares'/></sequence></related>", uri));
		ResultListWrapper res = null;
		try {
			res = xmlMapper.readValue(result, ResultListWrapper.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> results = new ArrayList<String>();
		for (ResultInstance ri: res.getResult()) {
			results.add(ri.getPath());
		}
		return results;
	}
	
	public String getPresentation(String uri) {
		URLConnection connection;
		try {
			String url = "/:mmt?get%20"+uri+"?%20%20present%20http://cds.omdoc.org/styles/omdoc/mathml.omdoc?html5%20respond";
			connection = new URL(mmturl+url).openConnection();
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "text/plain");
			StringWriter writer = new StringWriter();
			IOUtils.copy(connection.getInputStream(), writer, charset);
			return writer.toString();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	List<String> getTheoryConcepts(String uri) {
		String result = sendMessage(String.format("<related><literal uri='%s'/><toobject relation='Declares'/></related>", uri));
		ResultListWrapper res = null;
		try {
			res = xmlMapper.readValue(result, ResultListWrapper.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<String> results = new ArrayList<String>();
		for (ResultInstance ri: res.getResult()) {
			results.add(ri.getPath());
		}
		return results;
	}
	
	List<String> getTheories() {
		String result = sendMessage("<uris concept='theory'/>");
		System.out.println(result);
		return null;
	}
	
	public static void main(String[] args) {
		MMT mmt = new MMT("http://localhost:8081");
		System.out.println(mmt.getPresentation("http://mathhub.info/smglom/smglom/fibonaccinumbers.omdoc?fibonaccinumbers"));
		for (String s : mmt.flattenTheory("http://mathhub.info/smglom/smglom/fibonaccinumbers.omdoc?fibonaccinumbers")) {
			System.out.println(s);
		};
		mmt.getTheories();
	}
}
