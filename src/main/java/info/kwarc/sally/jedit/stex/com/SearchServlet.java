package info.kwarc.sally.jedit.stex.com;

import info.kwarc.sally.jedit.lucene.LuceneQuery;
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

public class SearchServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	LuceneQuery query;

	public SearchServlet(LuceneQuery query) {
		this.query = query;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(req.getInputStream()));
		String json = "";
		if(br != null){
			json = br.readLine();
		}
		ObjectMapper mapper = new ObjectMapper();
		AutocompleteRequest auto_request = mapper.readValue(json, AutocompleteRequest.class);
		
	}
}
