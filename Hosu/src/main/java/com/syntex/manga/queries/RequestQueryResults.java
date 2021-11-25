package com.syntex.manga.queries;

import java.lang.reflect.Modifier;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.syntex.manga.cashe.CasheHelper;
import com.syntex.manga.cashe.CashingPool;
import com.syntex.manga.cashe.ICashable;
import com.syntex.manga.models.QueriedManga;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

public class RequestQueryResults implements ICashable{

	private String query; 
	private int queries;
	private List<QueriedManga> mangas;
	private String source;
	private String creation;
	
	public RequestQueryResults(Source source, String query, List<QueriedManga> mangas) {
		this.query = query;
		this.mangas = mangas;
		this.queries = mangas.size();
		
		this.source = Domain.fromClass(source.getClass()).name();
	
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");  
	    Date date = new Date(); 
		this.creation = formatter.format(date);

		this.cashe();
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public int getQueries() {
		return queries;
	}

	public void setQueries(int queries) {
		this.queries = queries;
	}

	public List<QueriedManga> getMangas() {
		return mangas;
	}

	public void setMangas(List<QueriedManga> mangas) {
		this.mangas = mangas;
	}

	public Source getSource() {
		return Domain.fromDomain(Domain.valueOf(this.source).getSource(), this.query);
	}
	
	public Domain getDomain() {
		return Domain.valueOf(this.source);
	}
	
	public String getCreation() {
		return creation;
	}

	public void setCreation(String creation) {
		this.creation = creation;
	}

	@Override
	public void cashe() {
		
		CashingPool.pool.execute(() -> {

			//cashe the request instance.
			Gson gson = new GsonBuilder().excludeFieldsWithModifiers(Modifier.ABSTRACT)
							             .create();
		
			String json = gson.toJson(this);
			
			CasheHelper.makeQueryFileAndWrite(query, json);

			//List<RequestQueryResults> results = CasheHelper.deserialiseQuriedCasheFile(query, RequestQueryResults.class, this.getDomain());
			//for(RequestQueryResults result : results) {
			//	System.out.println(result.creation);	
		    //}
			
		});
		
	}
	
}
