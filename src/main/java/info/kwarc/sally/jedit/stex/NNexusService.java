package info.kwarc.sally.jedit.stex;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;

public class NNexusService {
	String url;
	String charset = "UTF-8";
	
	public NNexusService(String url) {
		this.url = url;
	}

	public NNexusLinks[]  getAnnotations(String body) {
		URLConnection connection;
		try {
			connection = new URL(url+"/linkentry").openConnection();
			connection.setDoOutput(true); // Triggers POST.
			connection.setRequestProperty("Accept-Charset", charset);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
			String query = String.format("annotation=json&embed=0&body=%s", 
					URLEncoder.encode(body, charset) 
					);
			OutputStream output = connection.getOutputStream();
			try {
				output.write(query.getBytes(charset));
			} finally {
				try { output.close(); } catch (IOException logOrIgnore) {}
			}
			InputStream response = connection.getInputStream();
			ObjectMapper mapper  = new ObjectMapper();
			NNexusStatus res = 	mapper.readValue(response, NNexusStatus.class);
			System.out.println(res.payload);
			NNexusLinks[] links= mapper.readValue(res.getPayload(), NNexusLinks[].class);
			return links;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new NNexusLinks[0];
	}

	public static void main(String[] args) {
		new NNexusService("http://127.0.0.1:3000").getAnnotations("Injective functions are fun! Also quadratic functions!");
	}
}
