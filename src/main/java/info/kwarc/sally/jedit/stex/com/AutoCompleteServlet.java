package info.kwarc.sally.jedit.stex.com;

import info.kwarc.sally.jedit.stex.AutocompleteRequest;
import info.kwarc.sally.jedit.stex.LMH;
import info.kwarc.sally.jedit.stex.MMT;
import info.kwarc.sally.jedit.stex.STeXAutoComplete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

public class AutoCompleteServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	MMT mmt;
	LMH lmh;
	STeXAutoComplete auto ;

	public AutoCompleteServlet() {
		this.mmt = new MMT("http://localhost:8181");
		this.lmh = new LMH("/home/costea/kwarc/localmh");
		this.auto = new STeXAutoComplete(mmt, lmh);	
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		System.out.println("Getting");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}
		System.out.println(json);
		ObjectMapper mapper = new ObjectMapper();
		AutocompleteRequest auto_request = mapper.readValue(json, AutocompleteRequest.class);
		
		List<String> results = auto.autocomplete(auto_request.getContent(), auto_request.getLine(), auto_request.getCol());
		resp.getOutputStream().write(mapper.writeValueAsString(results).getBytes());
	}
}
