/**
 * Adapted from Dr. Tuck's example by Nathan Gray 2016
 *
 * Adapted from 2 sources by Russ Tuck, 2016:
 *  - Google App Engine "helloworld" example app.
 *  - http://www.mkyong.com/webservices/jax-rs/restfull-java-client-with-java-net-url/
 */

package com.example.appengine.helloworld;

// Client side:
import java.io.IOException;
import java.io.PrintWriter;

// Server side:
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import org.json.JSONObject;
import org.json.JSONArray;
import com.example.appengine.helloworld.JSONResponseManipulator;
import com.example.appengine.helloworld.RequestSender;
import com.example.appengine.helloworld.Cache;

@SuppressWarnings("serial")
public class HelloServlet extends HttpServlet {

  /**
  * Handles all get requests
  */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
    throws IOException {
    Cache theCache = Cache.getInstance(); // Get the cache
    resp.setContentType("application/json; charset=utf-8");
    PrintWriter out = resp.getWriter();
    // Get zip code from req query String
    String zip = req.getParameter("zip");
    // If the cache contains recent data indexed under this zip code, then just
    // send the data stored in the cache
    if (theCache.getRequest(zip) != null) {
      out.write(theCache.getRequest(zip).toString());
    }
    // Otherwise find out the latitude and longitude of the zip code
    // and get data from forecast.io
    else {
      RequestSender reqSend = new RequestSender();
      JSONResponseManipulator respMan = new JSONResponseManipulator();
      // Get latitude and longitude from maps.googleapis.com
      JSONObject locationJson = reqSend.getJSONResponse(this.getLocationUrl(zip));
      ArrayList<String> coordinates = respMan.getCoordinates(locationJson);
      String lat = coordinates.get(0);
      String lng = coordinates.get(1);
      // Get json from forecast.io
      JSONObject weatherJson = reqSend.getJSONResponse(this.getWeatherUrl(lat, lng));
      // Trim forecast data and put it into a new JSONObject
      JSONObject jsonOut = respMan.createJSONResponse(weatherJson);
      theCache.addRequest(zip, jsonOut); // Add the data to theCache
      out.write(jsonOut.toString()); // Send the json
    }
  }

  /**
  * Gets the forecast.io url
  */
  private String getWeatherUrl(String lat, String lng) {
    String apiUrl = "https://api.forecast.io/forecast/";
    String apiKey = "10e3cd1e90ea7df35786186d55eca4ec";
    String urlString = apiUrl + apiKey + "/" + lat + "," + lng;

    return urlString;
  }

  /**
  * Gets the maps.googleapis url
  */
  private String getLocationUrl(String zip) {
    String apiKey = "AIzaSyAwIkHXo0cljOM5Mb97nufag5Cd8MW4kcg";
    String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?components=country:US|postal_code:";
    String urlString = apiUrl + zip + "&key=" + apiKey;

    return urlString;
  }
}
