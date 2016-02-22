/**
* Sends a request to a website
*/

package com.example.appengine.helloworld;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

public class RequestSender {

  /**
  * Gets a json object from a website and returns it
  */
  public JSONObject getJSONResponse(String urlString) {
    String inString = "";
    JSONObject inJson = new JSONObject(); // A json object to store the data

    try {
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "application/json");
      conn.connect();

      if (conn.getResponseCode() != 200) {
        throw new RuntimeException("Failed : HTTP error code : "
                                   + conn.getResponseCode());
      }

      BufferedReader br = new BufferedReader(new InputStreamReader(
                                               (conn.getInputStream())));
      String line =  "";
      // Read the response line by line and add it to a string
      while ((line = br.readLine()) != null) {
        inString += line;
      }
      inJson = new JSONObject(inString); // Convert string to json

    } catch (MalformedURLException e) {
      e.printStackTrace();
      inString = "{ 'error': 'MalformedURLException' }";
    } catch (IOException e) {
      e.printStackTrace();
      inString = "{ 'error': 'IOException' }";
    }
    return inJson;
  }
}
