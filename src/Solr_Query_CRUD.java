import java.io.IOException;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class Solr_Query_CRUD {
	
	//1.Install Solr
	//2.Run Solr
	//3.Create Core
	//4.Add Sample data
	
	//{"id":"001","name":"Ram","age":53,"Designation":"Manager","Location":"Hyderabad"}
	//{"id":"002","name":"Robert","age":43,"Designation":"SR.Programmer","Location":"Chennai"}
	//{"id":"003","name":"Rahim","age":25,"Designation":"JR.Programmer","Location":"Delhi"}
	
	
	public static String SERVER_URL = "http://localhost:8983/solr/";
	public static String CORE_NAME = "core_name";
	public static SolrClient client = new HttpSolrClient.Builder( SERVER_URL+CORE_NAME ).build();
	
	public static void main(String[] args) {
		System.out.println("=====================================================================");
		System.out.println("Data Inserted : "+addData());
		System.out.println("=====================================================================");
		System.out.println("Data Updated : "+updateData());
		System.out.println("=====================================================================");
		System.out.println("Data Deleted : "+deleteData());
		System.out.println("=====================================================================");
		System.out.println("Data All Data : "+getAllData());
		System.out.println("=====================================================================");
		System.out.println("Data Get Some Data : "+getSomeData());
		System.out.println("=====================================================================");
	}
	
	public static boolean addData(){
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "004");
		doc.addField("name", "Rutvik");
		doc.addField("age", 27);
		doc.addField("Designation", "Java Developer");
		doc.addField("Location", "Ahmedabad");
		try {
			System.out.println( "RST : "+client.add(doc).toString() );
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean updateData(){
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", "004");
		doc.addField("name", "Ankit");
		doc.addField("age", 27);
		doc.addField("Designation", "Java Developer");
		doc.addField("Location", "Ahmedabad");
		
		UpdateRequest ur = new UpdateRequest();
		ur.setAction(UpdateRequest.ACTION.COMMIT, false, false);
		ur.add(doc);
		
		System.out.println( doc.hasChildDocuments() );
		System.out.println( doc.getFieldNames().toString() );
		System.out.println( doc.isEmpty() );
		
		try {
			UpdateResponse rsp = ur.process(client);
			System.out.println( "RST : "+rsp.toString() );
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true; 
	}
	public static boolean deleteData(){
		try {
			System.out.println( "RST : "+client.deleteByQuery("id:002").toString() );
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true; 
	}
	public static boolean getAllData() {
		SolrQuery query = new SolrQuery();
		query.addSort("id", ORDER.desc);
		query.setQuery("*:*");
		query.addField("*");
		try {
			QueryResponse qr = client.query(query);
			SolrDocumentList list =  qr.getResults();
			System.out.println(list);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				System.out.println("RST : "+list.get(i));
			}
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public static boolean getSomeData() {
		//https://lucene.apache.org/solr/guide/6_6/common-query-parameters.html
		//https://lucene.apache.org/solr/guide/6_6/function-queries.html
		
		SolrQuery query = new SolrQuery();
		query.setQuery("*:*");									// all record
		query.setFilterQueries("age:[18 TO 28]");				// Range Query									// age:[18 TO *]
		query.setFilterQueries("(+name:A* +name:(*ki*)) OR (+Location:Ah* +Location:(Ahmedabad))");		// boolean query		//AND, OR, NOT
		query.setFilterQueries("name:\"Ank*\"");				// Phrase Query									// where query
		query.setFilterQueries("name:Ank*");					// Basic Queries								// like query
		
		query.addSort("id", ORDER.desc);						// order descending by id						// ORDER.desc/ORDER.desc
		query.addField("id,name,age,Designation,Location");		// all field of json							// *
		query.setRows(1);										// get row number only
		
		System.out.println("-->"+query.getQuery());
		for (String j : query.getFilterQueries()) {
			System.out.println("-->"+j);
		}
		try {
			QueryResponse qr = client.query(query);
			SolrDocumentList list =  qr.getResults();
			System.out.println(list);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				System.out.println("RST : "+list.get(i));
			}
			client.commit();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
