package info.kwarc.sally.jedit.lucene;

import info.kwarc.sally.jedit.stex.MMT;

import java.io.File;
import java.io.IOException;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;



public class MMTIndexer {
	MMT mmt;

	public MMTIndexer(MMT mmt) {
		this.mmt = mmt;
	}

	public static void main(String[] args) {
		String urlString = "http://localhost:8983/solr";
		SolrServer solr = new HttpSolrServer(urlString);

		try {
			solr.deleteByQuery( "*:*" );
		} catch (SolrServerException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		MMT mmt = new MMT("http://localhost:8081");
		File root = new File("/home/costea/kwarc/localmh/MathHub/smglom/smglom/source");
		int max = 20;
		for (File f :  root.listFiles()) {
			if (max == 0)
				break;
			if (f.getName().endsWith(".omdoc")) {
			//	max--;
				String name = f.getName();
				String modName = name.substring(0, name.length()-6);
				String url = String.format("http://mathhub.info/smglom/smglom/%s.omdoc?%s", modName, modName);

				SolrInputDocument doc = new SolrInputDocument();

				doc.addField( "id", url, 1.0f );
				doc.addField( "name", modName, 1.0f );
				doc.addField( "description", mmt.getPresentation(url));

				try {
					solr.add(doc);
				} catch (SolrServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		try {
			UpdateResponse res =  solr.commit();
			System.out.println(res.getRequestUrl());
			System.out.println(res);
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
