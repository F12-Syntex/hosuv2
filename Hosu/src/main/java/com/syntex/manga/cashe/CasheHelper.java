package com.syntex.manga.cashe;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.syntex.manga.api.MangaAPI;
import com.syntex.manga.queries.RequestQueryResults;
import com.syntex.manga.sources.Domain;
import com.syntex.manga.sources.Source;

public class CasheHelper {

	public static File makeQueryFileAndWrite(String name, String text) {
		try {
			
			File file = CasheHelper.getAndMakeQueryFile(name);

			
			//format the json text
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			JsonElement el = JsonParser.parseString(text);
			text = gson.toJson(el);
			
			//now add data
			PrintWriter writer = new PrintWriter(file);
			writer.print(text);
			writer.close();
			
			return file;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static File getQueryFile(String name) {
		try {
			
			
			name = name.replaceAll("[-+.^:,]","").replace(" ", "").toLowerCase();
			
			File cashe = new File(MangaAPI.getDownloader().home, "Cashe");
			File queries = new File(cashe, "Queries");
			File queryFolder = new File(queries, name);

			
			return queryFolder;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static List<RequestQueryResults> deserialiseQuriedCasheFile(Source source) {
		return deserialiseQuriedCasheFile(source.query, Domain.fromClass(source.getClass()));
	}
	
	public static File getLatestQueriedFile(Source source) {
		return getLatestQueriedFile(source.query, Domain.fromClass(source.getClass()));
	}
	

	public static File getLatestQueriedFile(String folder, Domain domain) {
		
		folder = folder.replaceAll("[-+.^:,]","").replace(" ", "").toLowerCase();
		
		try {
			
			File cashe = new File(MangaAPI.getDownloader().home, "Cashe");
			File queries = new File(cashe, "Queries");
			File query = new File(queries, folder);
			
			Map<RequestQueryResults, File> files = new HashMap<>();
			
			if(!query.exists()) {
				return null;
			}
			
			for(File file : query.listFiles()) {
				
				if(file.isDirectory()) continue;
				
				StringBuilder json = new StringBuilder();
				BufferedReader br = new BufferedReader(new FileReader(file));
				br.lines().forEach(json::append);
				
				Object data = new Gson().fromJson(json.toString(), RequestQueryResults.class);	
				RequestQueryResults result = (RequestQueryResults) data;

				if(result.getDomain() == domain) {
					files.put(result, file);
				}
				
				br.close();
			}
	
			
			List<RequestQueryResults> test = new ArrayList<>();
			
			for(RequestQueryResults key : files.keySet()) {
				test.add(key);
			}
			
			Collections.sort(test, new Comparator<RequestQueryResults>() {
			  @Override
			  public int compare(RequestQueryResults o1, RequestQueryResults o2) {
				try {
					
				    Date o1Date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").parse(o1.getCreation());  
				    Date o2Date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").parse(o2.getCreation());  
					
				    return o2Date.compareTo(o1Date);
				}catch (Exception e) {
					e.printStackTrace();
				}
				  
				return 0;  
			  }
			});
			
			return files.get(test.get(0));
			
			
		}catch (NullPointerException e) {e.printStackTrace();} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<RequestQueryResults> deserialiseQuriedCasheFile(String folder, Domain domain) {
		
		folder = folder.replaceAll("[-+.^:,]","").replace(" ", "").toLowerCase();
		
		try {
			
			File cashe = new File(MangaAPI.getDownloader().home, "Cashe");
			File queries = new File(cashe, "Queries");
			File query = new File(queries, folder);
			
			List<RequestQueryResults> files = new ArrayList<>();
			
			if(!query.exists()) {
				return null;
			}
			
			for(File file : query.listFiles()) {
				
				if(file.isDirectory()) continue;
				
				StringBuilder json = new StringBuilder();
				BufferedReader br = new BufferedReader(new FileReader(file));
				br.lines().forEach(json::append);
				
				Object data = new Gson().fromJson(json.toString(), RequestQueryResults.class);	
				RequestQueryResults result = (RequestQueryResults) data;

				if(result.getDomain() == domain) {
					files.add(result);
				}
				
				br.close();
			}
	
			Collections.sort(files, new Comparator<RequestQueryResults>() {
			  @Override
			  public int compare(RequestQueryResults o1, RequestQueryResults o2) {
				try {
					
				    Date o1Date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").parse(o1.getCreation());  
				    Date o2Date = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss").parse(o2.getCreation());  
					
				    return o2Date.compareTo(o1Date);
				}catch (Exception e) {
					e.printStackTrace();
				}
				  
				return 0;  
			  }
			});
			
			return files;
			
		}catch (NullPointerException e) {e.printStackTrace();} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static File getAndMakeQueryFile(String name) {
		
		name = name.replaceAll("[-+.^:,]","").replace(" ", "").toLowerCase();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");  
	    Date date = new Date(); 
		String time = formatter.format(date);
		
		File cashe = new File(MangaAPI.getDownloader().home, "Cashe");
		File queries = new File(cashe, "Queries");
		File queryFolder = new File(queries, name);
		
		//System.out.println(queryFolder.getAbsolutePath() + " name: " + name);
		
		File file = new File(queryFolder, time + ".json");
		
		if(!queryFolder.exists()) {
			queryFolder.mkdirs();
		}
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	
	public static File getAndMakeMangaFile(String name) {
		
		name = name.replaceAll("[-+.^:,]","").replace(" ", "").toLowerCase();
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH-mm-ss");  
	    Date date = new Date(); 
		String time = formatter.format(date);
		
		File cashe = new File(MangaAPI.getDownloader().home, "Cashe");
		File queries = new File(cashe, "Mangas");
		File queryFolder = new File(queries, name);
		
		//System.out.println(queryFolder.getAbsolutePath() + " name: " + name);
		
		File file = new File(queryFolder, time + ".json");
		
		if(!queryFolder.exists()) {
			queryFolder.mkdirs();
		}
		
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return file;
	}
	

}
