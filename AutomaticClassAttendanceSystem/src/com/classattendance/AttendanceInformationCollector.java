package com.classattendance;


import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.mongodb.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
//import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
//import com.mongodb.util.JSON;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import javax.ws.rs.core.MediaType;

@Path("/class/section/")
public class AttendanceInformationCollector {
	
	@Path("{sid}")
	@POST
	//\@Produces("application/json")
	@Consumes("application/json")
	public Response doPost(InputStream incomingData) {
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			System.out.println("json str: "+ result1);
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			//Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) parser.parse(result1);
			
			System.out.println("json Obj: "+ jsonObject);
         // To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("Geolookup: Connect to database successfully");
	         //boolean auth = db.authenticate(myUserName, myPassword);
	         //System.out.println("Authentication: "+auth);
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	        Date curDate = new Date();
	        String attendanceDate = sdf.format(curDate);
	        DBCollection collection = db.getCollection(attendanceDate);
	        System.out.println("Creating BasicDBObject for Student POST...");
	     	BasicDBObject document = new BasicDBObject();
	     	
	     	BasicDBObject studentInfo = (BasicDBObject) AutomaticClassAttendanceSystem.getDataFromDB();
	     	
	     	String first = studentInfo.getString("first");
	     	String last = studentInfo.getString("last");
	     	//String sid = studentInfo.getString("sid");
	     	String sid = (String) jsonObject.get("sid");
	     	String timeIN =  (String) jsonObject.get("timeIN");
	     	String phoneId = (String) jsonObject.get("phoneId");
	     	String location = (String) jsonObject.get("location");

	        document.put("first", first);
	        document.put("last", last);
	        document.put("sid", sid);
	     	document.put("timeIN", timeIN);
	     	document.put("timeOUT", "");
	     	document.put("phoneId", phoneId);
	     	document.put("location", location);
	        collection.insert(document);

	     	DBCursor cursorDoc = collection.find();
	     	 //outStr = collection.toString();
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
	     	//collection.remove(new BasicDBObject());
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }


	
 
		String result = "@Produces(\"application/json\") Output: \n\nF to C Converter Output: \n\n";
		//return Response.status(200).entity(result).build();
		//return result;
		return Response.status(201).entity(result).build();

	}
	
	public String getIncomingData(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		//JSONParser parser = new JSONParser();
		try {
			//InputStreamReader in = new InputStreamReader(incomingData);
			//BufferedReader in = new BufferedReader(inStream);
			//Object obj = parser.parse(new InputStreamReader(incomingData));
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			//JSONObject jsonObject = (JSONObject) obj;

			//String name = (String) jsonObject.get("jsonData");
			//System.out.println("FIRST: " +name);
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println("Line - " +line);
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + strBuilder.toString());
 
		// return HTTP response 200 in case of success
		//return Response.status(200).entity(crunchifyBuilder.toString()).build();
		return strBuilder.toString();
	}

}
