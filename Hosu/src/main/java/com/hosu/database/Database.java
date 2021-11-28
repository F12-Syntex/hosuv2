package com.hosu.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syntex.manga.models.QueriedEntity;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

public class Database {
	
	/*
	 * variables.
	 */
	public final ExecutorService threadpool = Executors.newFixedThreadPool(3);

	private final String host;
	private final String port;
	private final String database;
	private final String username;
	private final String password;
	
	private Connection connection;
	
	public List<RequestQueryResults> cashedQueryResults;
	private List<RequestQueryResults> cashedMangaData;
	
	public SortedSet<String> mangaQueries = new TreeSet<>();
	public SortedSet<String> liked = new TreeSet<>();
	
	private File hosu;
	
	private boolean localDB = true;
	
	//HAnDHJN2eErh4qKI
	
	public Database() {
		
		/* default values ( for testing ) */
			this.host = "localhost";
			this.port = "3306";
			this.database = "hosu";
			this.username = "root";
			this.password = "";
		
		
		/* 
		this.host = "gh1girns9tg7.ap-southeast-2.psdb.cloud";
		this.port = "3306";
		this.database = "hosu";
		this.username = "vmgh16j6gcbh";
		this.password = "pscale_pw_8v5l6ddD7DC3P1OipNblwoXQQ5q-Df8jiXEJC9qzjZM";
		*/
			
		this.cashedQueryResults = new ArrayList<>();
		
		
		//make the file if it does not exist.
		File file = new File(System.getProperty("user.home"), "Hosu");
		this.hosu = new File(file, "hosu.db");
		
		if(!hosu.exists()) {
			file.mkdirs();
			try {
				hosu.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	public void getSuggestions() {
		
	}
	
	/*
	 * connect to the mysql server, then register the database
	 * 
	 * @author syntex
	 * @throws SQLException if connection to the database cannot be established.
	 */
	public void connect() {
		//threadpool.execute(() -> {
			if(!this.isConnected()) {
				try {

					Class.forName("com.mysql.jdbc.Driver"); 
					Class.forName("org.sqlite.JDBC");
					
					if(this.localDB) {
						this.connection = DriverManager.getConnection("jdbc:sqlite:" + this.hosu);
					}else {
						this.connection = DriverManager.getConnection("jdbc:mysql://" + this.getHost() + ":"
								  + this.getPort() + "/"
								  + this.getDatabase() + "?useSSL=true", 
								    this.getUsername(),
								  	this.getPassword());	
					}
					
					this.setup();
					System.out.println("Connection to database has been established.");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		//});
	}

	/*
	 * disconnect and update the database
	 */
	public void disconnect() {
		try {
			this.connection.close();
			System.out.println("Connection to database has been closed.");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Check MySql connection.
	 */
	public boolean isConnected() {
		return connection != null;
	}
	
	/*
	 * initialise the database
	 */
	public void setup() throws Exception {
		
		//create preparedstatemnet var
		PreparedStatement statement;
		
		/*
		 * create table of key "key" which is simply a counter.
		 * query string
		 * source string
		 * creation TINYTEXT
		 * primary key "key"
		 */
		statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS QUERIES(COUNTER INTEGER AUTO_INCREMENT, SEARCH TINYTEXT, SCRAPPER TINYTEXT, CREATION TINYTEXT, PRIMARY KEY(COUNTER));");
		statement.executeUpdate();
		statement.close();
		
		/*
		 * create the MangaQuery table.
		 */
		statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS QUERIEDMANGA(COUNTER INTEGER AUTO_INCREMENT, IMAGE TINYTEXT, ALT TINYTEXT, SCRAPPER TINYTEXT, URL TINYTEXT, SEARCH TINYTEXT, PRIMARY KEY(COUNTER));");
		statement.executeUpdate();
		statement.close();
		
		/*
		 * create manga table
		 */
		statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS MANGA(COUNTER INTEGER AUTO_INCREMENT, SEARCH TINYTEXT, PRIMARY KEY(COUNTER));");
		statement.executeUpdate();
		statement.close();
		
		/*
		 * create liked table
		 */
		statement = this.connection.prepareStatement("CREATE TABLE IF NOT EXISTS LIKED(COUNTER INTEGER AUTO_INCREMENT, NAME TINYTEXT, PRIMARY KEY(COUNTER));");
		statement.executeUpdate();
		statement.close();
		
		//System.out.println("Created tables, loading sample query request.");
		//RequestQueryResults results = MangaAPI.search("", Domain.MANGA_STREAM).requestQueryResults().call();
		//this.logQueryRequest(results);
		
		this.setCashedQueryResults(this.requestQueryResults().call());
		
		//cashe the suggestions.
		ResultSet query = this.connection.createStatement().executeQuery("SELECT * FROM MANGA");
		
		//get query attributes.
		while(query.next()) {
	        String search = query.getString("SEARCH");
	        this.mangaQueries.add(search);
		}
		
		//cashe the liked.
		ResultSet likedQuery = this.connection.createStatement().executeQuery("SELECT * FROM LIKED");
		
		//get query attributes.
		while(likedQuery.next()) {
	        String search = likedQuery.getString("NAME");
	        this.liked.add(search);
		}
		
	}
	
	public void writeManga(List<String> contents) {
		for(String i : contents) {
			PreparedStatement statement;
			
			try {
			
				//System.out.println("INSERT INTO MANGA (SEARCH) VALUES(\"" + i + "\");");
				statement = this.connection.prepareStatement("INSERT INTO MANGA (SEARCH) VALUES(\"" + i + "\");");
				statement.executeUpdate();
				statement.close();
			
			} catch (SQLException e) {
				// TODO Auto-generated catch blockO
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * add a liked manga.
	 */
	public void addLiked(String contents) {
		PreparedStatement statement;
		
		try {
		
			//System.out.println("INSERT INTO MANGA (SEARCH) VALUES(\"" + i + "\");");
			statement = this.connection.prepareStatement("INSERT INTO LIKED (NAME) VALUES(\"" + contents + "\");");
			statement.executeUpdate();
			statement.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch blockO
			e.printStackTrace();
		}
	}
	
	/*
	 * remove a liked manga.
	 */
	public void removeLiked(String contents) {
		PreparedStatement statement;
		
		try {
		
			//System.out.println("INSERT INTO MANGA (SEARCH) VALUES(\"" + i + "\");");
			statement = this.connection.prepareStatement("DELETE from LIKED where NAME = \"" + contents + "\"; ");
			statement.executeUpdate();
			statement.close();
		
		} catch (SQLException e) {
			// TODO Auto-generated catch blockO
			e.printStackTrace();
		}
	}
	
	/*
	 * append queried results to database.
	 */
	public void logQueryRequest(RequestQueryResults result) {
		
		this.cashedQueryResults.add(result);
		
		threadpool.execute(() -> {
			
			try {
				
				//create preparedstatemnet var
				PreparedStatement statement;
				
				/*
				 * append query data.
				 */
				
				System.out.println("Appending results to database.");
				statement = this.connection.prepareStatement("INSERT INTO QUERIES (SEARCH, SCRAPPER, CREATION) VALUES(\"" + result.getQuery() + "\", \"" + result.getDomain().name() + "\", \"" + result.getCreation() + "\");");
				statement.executeUpdate();
				statement.close();
				
				System.out.println("Appending chapters to database.");
				
				//delete chapters if results is not empty.
				if(result.getMangas().size() > 0) {
					System.out.println("DELETE FROM `queriedmanga` WHERE SEARCH = \"" + result.getQuery() + "\" AND SCRAPPER = \"" + result.getDomain().name() + "\";");
					statement = this.connection.prepareStatement("DELETE FROM `queriedmanga` WHERE SEARCH = \"" + result.getQuery() + "\" AND SCRAPPER = \"" + result.getDomain().name() + "\";");
					statement.executeUpdate();
					statement.close();
				}
				
				for(QueriedEntity i : result.getMangas()) {
					//write the data
					System.out.println("INSERT INTO queriedmanga (IMAGE, ALT, SCRAPPER, URL, SEARCH) VALUES(\"" + i.getImage() + "\", \"" + i.getAlt() + "\", \"" + i.getDomain().name() + "\", \"" + i.getUrl() + "\", \"" + i.query + "\");");
					statement = this.connection.prepareStatement("INSERT INTO queriedmanga (IMAGE, ALT, SCRAPPER, URL, SEARCH) VALUES(\"" + i.getImage() + "\", \"" + i.getAlt() + "\", \"" + i.getDomain().name() + "\", \"" + i.getUrl() + "\", \"" + i.query + "\");");
					statement.executeUpdate();
					statement.close();
				}
				
				System.out.println("Log complete.");
				
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
		});
		
	}
	
	/*
	 * retrieve all query data from the database
	 */
	public Callable<List<RequestQueryResults>> requestQueryResults() {
		
		return () -> {
			
			List<RequestQueryResults> data = new ArrayList<>();
			
			try {
			
				//create preparedstatemnet var
				ResultSet query = this.connection.createStatement().executeQuery("SELECT * FROM queries");
				
				//get query attributes.
				while(query.next()) {
			        String search = query.getString("SEARCH");
			        String scrapper = query.getString("SCRAPPER");
			        String creation = query.getString("CREATION");
			        Source source = Domain.fromDomain(Domain.valueOf(scrapper).CLASS_PATH, search);
			        
			        ResultSet mangaQuery = this.connection.createStatement().executeQuery("SELECT * FROM queriedmanga where SEARCH = \"" + search + "\" AND SCRAPPER = \"" + scrapper + "\";");

					List<QueriedEntity> mangas = new ArrayList<>();
					
					//get mangas
					while (mangaQuery.next()){
				        String image = mangaQuery.getString("IMAGE");
				        String alt = mangaQuery.getString("ALT");
				        String url = mangaQuery.getString("URL");
				        
				        QueriedEntity manga = new QueriedEntity(image, alt, source, url);
				        mangas.add(manga);
				      }
					
					RequestQueryResults results = new RequestQueryResults(source, search, mangas, creation);
					data.add(results);
				}
				
				query.close();
			
			}catch (SQLException e) {
				e.printStackTrace();
			}
			
			return data;
			
		};
		
		
	}
	
	public String getHost() {
		return host;
	}


	public String getPort() {
		return port;
	}


	public String getDatabase() {
		return database;
	}


	public String getUsername() {
		return username;
	}


	public String getPassword() {
		return password;
	}

	public List<RequestQueryResults> getCashedQueryResults() {
		return cashedQueryResults;
	}

	public void setCashedQueryResults(List<RequestQueryResults> cashedQueryResults) {
		this.cashedQueryResults = cashedQueryResults;
	}
	
	public RequestQueryResults getLatestQueryFile(Source source) {
		
		for(RequestQueryResults i : this.getCashedQueryResults()) {
			if(i.getQuery().equalsIgnoreCase(source.query) && i.getDomain() == Domain.fromClass(source.getClass())) {
				return i;
			}
		}
		
		return null;
	}

}
