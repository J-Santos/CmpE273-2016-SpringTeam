package com.classattendance;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import com.mongodb.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
 
@Path("/class")
public class AutomaticClassAttendanceSystem {
	
	@Path("student")
	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public Response getAllStudents() {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("studentColl");
			
			DBCursor cursor = collection.find();
			
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
			outStr = "[";
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
				if(cursor.hasNext()){
					outStr += ",";
				}
			}
			int endIndex = outStr.length()-1;
			outStr.substring(0, endIndex);
			outStr += "]";
			System.out.println("Match: " + outStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("student/{sid}")
	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public Response getStudentData(@PathParam("sid") String sid) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("studentColl");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sid", sid);
			DBCursor cursor = collection.find(whereQuery);
			
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
	
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
			}
			System.out.println("Match: " + outStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("student/device/{deviceId}")
	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public Response getStudentByDeviceId(@PathParam("deviceId") String deviceId) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection("studentColl");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("device_id", deviceId);
			DBCursor cursor = collection.find(whereQuery);
			
			if(cursor.count() == 0){
				return Response.status(404).entity("").build();
			}
	
			while(cursor.hasNext()) {
				outStr += cursor.next().toString();
			}
			System.out.println("Match: " + outStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("student/register-student/{sid}")
	@GET
	@Consumes("application/json")
	@Produces("application/json")
	public Response doPostRegistration(InputStream incomingData) {
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) obj;
			
			System.out.println("json Obj: "+ jsonObject);
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("Geolookup: Connect to database successfully");

	        DBCollection collection = db.getCollection("studentColl");
	        System.out.println("Creating BasicDBObject for Student POST...");
	     	BasicDBObject document = new BasicDBObject(); 
	     	
	     	String first = (String) jsonObject.get("first");
	     	String last = (String) jsonObject.get("last");
	     	String sid = (String) jsonObject.get("sid");
	     	String email = (String) jsonObject.get("email");
	     	String device_id = (String) jsonObject.get("device_id");

	        document.put("first", first);
	        document.put("last", last);
	        document.put("sid", sid);
	        document.put("email", email);
	        document.put("device_id", device_id);
	        collection.insert(document);

	     	DBCursor cursorDoc = collection.find();
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
 
	@Path("student/register/{sid}")
	@POST
	@Consumes("application/json")
	@Produces("application/json")
	public Response doPost(InputStream incomingData) {
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";
		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) obj;
			
			System.out.println("json Obj: "+ jsonObject);
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("Geolookup: Connect to database successfully");

	        DBCollection collection = db.getCollection("studentColl");
	        System.out.println("Creating BasicDBObject for Student POST...");
	     	BasicDBObject document = new BasicDBObject(); 
	     	
	     	String first = (String) jsonObject.get("first");
	     	String last = (String) jsonObject.get("last");
	     	String sid = (String) jsonObject.get("sid");
	     	String email = (String) jsonObject.get("email");
	     	String device_id = (String) jsonObject.get("device_id");

	        document.put("first", first);
	        document.put("last", last);
	        document.put("sid", sid);
	        document.put("email", email);
	        document.put("device_id", device_id);
	        collection.insert(document);

	     	DBCursor cursorDoc = collection.find();
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		
		return Response.status(201).entity(outStr).build();
	}
	
	@Path("student/{sid}")
	@DELETE
	@Consumes("application/json")
	public Response deleteRecord(@PathParam("sid") String sid) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB Delete: Connect to database successfully");
	        DBCollection collection = db.getCollection("studentColl");
			
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sid", sid);
			collection.remove(whereQuery);
			outStr = "Record SID: " + sid + " was deleted succesfully.";
			System.out.println(outStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Response.status(204).entity(outStr).build();
	}
	
	@Path("student/{sid}")
	@PUT
	@Consumes("application/json")
	public Response doPut(InputStream incomingData) {
		String dataIncoming = getIncomingData(incomingData);
		String result1;
		String outStr = "";

		try {
			result1 = java.net.URLDecoder.decode(dataIncoming, "UTF-8");
			result1 = result1.substring(9);
			System.out.println("json str: "+ result1);
			
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(result1);
			JSONObject jsonObject = (JSONObject) obj;
			
			System.out.println("json Obj: "+ jsonObject);
			// To connect to mongodb server
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB: Connect to database successfully");

	        DBCollection collection = db.getCollection("studentColl");
	        System.out.println("Updating BasicDBObject for student PUT...");
	     	BasicDBObject document = new BasicDBObject();		
            
            String first = (String) jsonObject.get("first");
	     	String last = (String) jsonObject.get("last");
	     	String sid = (String) jsonObject.get("sid");
	     	String email = (String) jsonObject.get("email");
	     	String device_id = (String) jsonObject.get("device_id");
            
	     	document.put("first", first);
	        document.put("last", last);
	        document.put("sid", sid);
	        document.put("email", email);
	        document.put("device_id", device_id);
	        
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("sid", sid);
			collection.remove(whereQuery);
	        
			collection.insert(document);

	     	DBCursor cursorDoc = collection.find(whereQuery);
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     		//System.out.println(outStr);
	     	}
	     	System.out.println(outStr);
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }

		return Response.status(201).entity("").build();

	}
	
	public static DBObject getDataFromDB(String device_id) {
		MongoClient mongoClient;
		
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");

	        DBCollection collection = db.getCollection("studentColl");
	        BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("device_id", device_id);
			DBCursor cursor = collection.find(whereQuery);
			DBObject updateDocument = null;
			updateDocument = cursor.next();
			System.out.println("Match: " +updateDocument);
			return updateDocument;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public String getIncomingData(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));

			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println("Line - " +line);
				strBuilder.append(line);
			}
		} catch (Exception e) {
			System.out.println("Error Parsing: - ");
		}
		System.out.println("Data Received: " + strBuilder.toString());

		return strBuilder.toString();
	}

	
	public String getPrettyJson(String jsonStr){
		ObjectMapper mapper = new ObjectMapper();
		String prettyJson ="";
		try {
			Object jsonTest = mapper.readValue(jsonStr, Object.class);
			prettyJson = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonTest);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return prettyJson;
	}

	
	public String getJsonData(String url) {
		String jsonStr ="";
		  try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet getRequest = new HttpGet(
				url);
			getRequest.addHeader("accept", "application/json");

			HttpResponse response = httpClient.execute(getRequest);

			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
				   + response.getStatusLine().getStatusCode());
			}

			BufferedReader br = new BufferedReader(
	                         new InputStreamReader((response.getEntity().getContent())));

			String output;
			
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				jsonStr += output;
				System.out.println(output);
			}

			httpClient.getConnectionManager().shutdown();

		  } catch (ClientProtocolException e) {
		
			e.printStackTrace();

		  } catch (IOException e) {
		
			e.printStackTrace();
		  }
		  
		  return jsonStr;

	}

}