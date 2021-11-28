package com.syntex.manga.queries;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.hosu.application.HosuClient;
import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

public class RequestQueryResults implements ICashable{

	private String query; 
	private int queries;
	private List<QueriedEntity> mangas;
	private String source;
	private String creation;
	
	public RequestQueryResults(Source source, String query, List<QueriedEntity> mangas) {
		this.query = query;
		this.mangas = mangas;
		this.queries = mangas.size();
		
		this.source = Domain.fromClass(source.getClass()).name();
	
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");  
	    Date date = new Date(); 
		this.creation = formatter.format(date);

		this.cashe();
	}
	
	public RequestQueryResults(Source source, String query, List<QueriedEntity> mangas, String creation) {
		this.query = query;
		this.mangas = mangas;
		this.queries = mangas.size();
		
		this.source = Domain.fromClass(source.getClass()).name();
		this.creation = creation;
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

	public List<QueriedEntity> getMangas() {
		return mangas;
	}

	public void setMangas(List<QueriedEntity> mangas) {
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
		HosuClient.database.logQueryRequest(this);
	}
	
}
