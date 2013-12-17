package info.kwarc.sally.jedit.lucene;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.util.NamedList;

public class LuceneQuery {

	

	public static void main(String[] args) {
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);
		SolrQuery query = new SolrQuery();
		query.setQuery("group" );
		try {
			QueryResponse rsp = solr.query( query );
			NamedList<Object> result = rsp.getResponse();
			SolrDocumentList o = (SolrDocumentList) result.get("response");
			for (SolrDocument doc : o) {
				System.out.println(doc.getFieldValue("id"));
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
