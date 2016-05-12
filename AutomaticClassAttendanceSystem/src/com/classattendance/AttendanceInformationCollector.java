package com.classattendance;


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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


@Path("/class/section")
public class AttendanceInformationCollector {
	
	@Path("devices")
	@GET
	@Produces("application/json")
	public Response getAllDevices() {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection(getCurrentDate());
			
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
	
	@Path("devices/in-class")
	@GET
	@Produces("application/json")
	public Response getAllDeviceInClass() {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection(getCurrentDate());
	        BasicDBObject whereQuery = new BasicDBObject().append("checked_in", "true");
			
			DBCursor cursor = collection.find(whereQuery);
			
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
	
	@Path("devices/not-in-class")
	@GET
	@Produces("application/json")
	public Response getAllDeviceNotInClass() {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection(getCurrentDate());
	        BasicDBObject whereQuery = new BasicDBObject().append("checked_in", "false");
			
			DBCursor cursor = collection.find(whereQuery);
			
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
        // Now connect to your databases
		
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	@Path("{deviceId}")
	@GET
	@Produces("application/json")
	public Response getDeviceData(@PathParam("deviceId") String deviceId) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("UsersDB READ: Connect to database successfully");
	        DBCollection collection = db.getCollection(getCurrentDate());
	        
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
	
	@Path("{device_id}")
	@POST
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
			JSONObject jsonObject = (JSONObject) parser.parse(result1);
			
			System.out.println("json Obj: "+ jsonObject);
	        MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
				
	         // Now connect to your databases
	        DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("Geolookup: Connect to database successfully");
	        DBCollection collection = db.getCollection(getCurrentDate());
	        

	        System.out.println("Creating BasicDBObject for Student POST...");
	     	//BasicDBObject document = new BasicDBObject();
	     	BasicDBObject timeDocument = new BasicDBObject();
	     	
	     	String device_id = (String) jsonObject.get("device_id");
	     	String timeIn =  (String) jsonObject.get("timeIn");
	     	String firstTimeIn =  (String) jsonObject.get("firstTimeIn");
	     	BasicDBObject studentInfo = (BasicDBObject) AutomaticClassAttendanceSystem.getDataFromDB(device_id);
	     	String first = studentInfo.getString("first");
			String last = studentInfo.getString("last");
			String email = studentInfo.getString("email");
			String sid = studentInfo.getString("sid");
			BasicDBObject whereQuery = new BasicDBObject();
			whereQuery.put("device_id", device_id);
			DBCursor cursor = collection.find(whereQuery);
			
			if(cursor.count() != 0){
				return Response.status(200).entity("").build();
			}
		
			timeDocument.put("first", first);
	     	timeDocument.put("last", last);
	     	timeDocument.put("email", email);
	     	timeDocument.put("sid", sid);
	     	
	     	
	     	timeDocument.put("device_id", device_id);
	     	timeDocument.put("timeIn", timeIn);
	     	timeDocument.put("firstTimeIn", firstTimeIn);
	     	timeDocument.put("timeOut", "");
	     	timeDocument.put("checked_in", "true");
	     	collection.insert(timeDocument);
	     	
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
		return Response.status(201).entity("").build();

	}
	
	@Path("{device_id}/timeIn")
	@PUT
	@Consumes("application/json")
	public Response doPutTimeIn(InputStream incomingData) {
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
	        System.out.println("HubDB: Connect to database successfully");
	       
	        DBCollection collection = db.getCollection(getCurrentDate());
	        System.out.println("Creating BasicDBObject for PUT timeIn...");
	        
	        BasicDBObject checkInDocument = new BasicDBObject();
	     	BasicDBObject timeDocument = new BasicDBObject();

	     	String device_id = (String) jsonObject.get("device_id");
	     	String timeIn =  (String) jsonObject.get("timeIn");
	     	
	     	timeDocument.append("$set", new BasicDBObject().append("timeIn", timeIn));
	    	checkInDocument.append("$set", new BasicDBObject().append("checked_in", "true"));
	    			
	    	BasicDBObject whereQuery = new BasicDBObject().append("device_id", device_id);

	    	collection.update(whereQuery, timeDocument);
	    	collection.update(whereQuery, checkInDocument);
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
	
	@Path("{device_id}/timeOut")
	@PUT
	@Consumes("application/json")
	public Response doPutTimeOut(InputStream incomingData) {
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
	        System.out.println("HubDB: Connect to database successfully");

	        DBCollection collection = db.getCollection(getCurrentDate());
	        System.out.println("Creating BasicDBObject for PUT timeOut...");
	        
	        BasicDBObject checkOutDocument = new BasicDBObject();
	     	BasicDBObject timeDocument = new BasicDBObject();

	     	String device_id = (String) jsonObject.get("device_id");
	     	String timeOut =  (String) jsonObject.get("timeOut");
	     	
	     	timeDocument.append("$set", new BasicDBObject().append("timeOut", timeOut));
	     	checkOutDocument.append("$set", new BasicDBObject().append("checked_in", "false"));
	    			
	    	BasicDBObject whereQuery = new BasicDBObject().append("device_id", device_id);

	    	collection.update(whereQuery, timeDocument);
	    	collection.update(whereQuery, checkOutDocument);

	    	//System.out.println("HERE");
	        outStr="";
	     	DBCursor cursorDoc = collection.find(whereQuery);
	     	while (cursorDoc.hasNext()) {
	     		outStr += cursorDoc.next().toString();
	     	}
	     	System.out.println(outStr);
            
        } catch (Exception e) {
        	System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            e.printStackTrace();
        }
		return Response.status(201).entity("").build();

	}
	@Path("{deviceId}")
	@DELETE
	@Consumes("application/json")
	public Response deleteRecord(@PathParam("deviceId") String deviceId) {
		MongoClient mongoClient;
		String outStr = "";
		try {
			mongoClient = new MongoClient( "localhost" , 27017 );
			DB db = mongoClient.getDB( "AttendanceDB" );
	        System.out.println("AttendanceDB Delete: Connect to database successfully");

	        DBCollection collection = db.getCollection(getCurrentDate());
	        
	        BasicDBObject whereQuery = new BasicDBObject();
	        
	        String temp = "Delete " + deviceId;
	        BasicDBObject newDocument = new BasicDBObject();
	    	newDocument.append("$set", new BasicDBObject().append(deviceId, temp));

	    	collection.update(whereQuery, newDocument);
	        
	        whereQuery.put(deviceId, temp);
	        collection.remove(whereQuery);

			outStr = "Device ID: " + deviceId + " was deleted succesfully.";
			System.out.println(outStr);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return Response.status(204).entity(outStr).build();
	}
	
	public String getCurrentDate(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date curDate = new Date();
        String attendanceDate = sdf.format(curDate);
        return attendanceDate;
	}
	
	@GET
	@Path("{month}/{day}/{year}")
	@Consumes("application/json")
	public Response doGet(@PathParam("year") String year, @PathParam("month") String month,
			@PathParam("day") String day) {

		String actualDate = year + month + day;
		String outStr = "";
		MongoClient mongoClient;
		try {
			mongoClient = new MongoClient("localhost", 27017);
			DB db = mongoClient.getDB("AttendanceDB");
			System.out.println("Geolookup: Connect to database successfully");
			DBCollection collection = db.getCollection(actualDate);
			DBCursor cursor = collection.find();
			while(cursor.hasNext()){			
					String jsonV = cursor.next().toString();
					outStr = outStr +  "," + jsonV;
			}
			outStr = outStr.substring(1);
			outStr = "[" + outStr + "]";
			System.out.println("Match: " + outStr);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		System.out.println(getPrettyJson(outStr));
		return Response.status(200).entity(getPrettyJson(outStr)).build();
	}
	
	public String getIncomingData(InputStream incomingData) {
		StringBuilder strBuilder = new StringBuilder();
		//JSONParser parser = new JSONParser();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(incomingData));
			String line = null;
			while ((line = in.readLine()) != null) {
				//System.out.println("Line - " +line);
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

}
