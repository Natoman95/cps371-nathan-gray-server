/**
* Modifies JSON responses from websites in useful ways
*/

package com.example.appengine.helloworld;

import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;

public class JSONResponseManipulator {

  /**
  * Takes the json object from forecast.io and makes a new json object that
  * contains only data the app will use
  */
  public JSONObject createJSONResponse(JSONObject weatherJson) {
    // Get the currently data object from the json
    JSONObject currentlyIn = weatherJson.getJSONObject("currently");
    // Create a replacement
    JSONObject currentlyOut = new JSONObject();
    // Add the desired fields from the original to the new object
    currentlyOut.put("temperature", currentlyIn.getDouble("temperature"));
    currentlyOut.put("dewPoint", currentlyIn.getDouble("dewPoint"));
    currentlyOut.put("humidity", currentlyIn.getDouble("humidity"));
    currentlyOut.put("icon", currentlyIn.getString("icon"));

    // Get the daily data array from the json
    JSONArray dailyIn = weatherJson.getJSONObject("daily").getJSONArray("data");
    // Create a replacement
    JSONArray dailyOut = new JSONArray();
    // Loop through the items in the array
    for (int i = 0; i < dailyIn.length(); i ++) {
      JSONObject dayIn = dailyIn.getJSONObject(i); // Get the day object
      JSONObject dayOut = new JSONObject(); // Create a replacement
      // Add the desired fields to the new day object
      dayOut.put("maxTemp", dayIn.getDouble("temperatureMax"));
      dayOut.put("minTemp", dayIn.getDouble("temperatureMin"));
      dayOut.put("time", (dayIn.getInt("time")));
      dayOut.put("summary", dayIn.getString("summary"));
      dayOut.put("icon", dayIn.getString("icon"));
      // Add that object to the array
      dailyOut.put(i, dayOut);
    }

    // Create a new json object to send back to the application
    JSONObject jsonOut = new JSONObject();
    // Add the replacement objects to it
    jsonOut.put("currently", currentlyOut);
    jsonOut.put("daily", dailyOut);

    return jsonOut;
  }

  /**
  * Gets the latitude and longitude from maps.googleapis.com
  */
  public ArrayList<String> getCoordinates(JSONObject locationJson) {
    ArrayList<String> coordinates = new ArrayList();
    // Get first level JSONArray
    JSONArray results = (JSONArray) locationJson.get("results");
    // Get the object within results array
    JSONObject resultsObj = (JSONObject)results.get(0);
    // Get the object within resultsObj
    JSONObject geometry = (JSONObject)resultsObj.get("geometry");
    // Get the object within the geometry object
    JSONObject location = (JSONObject) geometry.get("location");
    // FINALLY get the latitude and longitude
    String lat = Double.toString(location.getDouble("lat"));
    String lng = Double.toString(location.getDouble("lng"));
    coordinates.add(lat);
    coordinates.add(lng);

    return coordinates;
  }
}
