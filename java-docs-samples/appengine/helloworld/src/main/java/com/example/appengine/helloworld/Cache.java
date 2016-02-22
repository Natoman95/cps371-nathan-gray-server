/**
* A singleton class that stores data about requests that have been made in a
* static HashMap
*/

package com.example.appengine.helloworld;

import java.util.HashMap;
import org.json.JSONObject;

public class Cache {

  // Stores the requests by zip code
  private static HashMap<String, Request> requests = new HashMap<String, Request>();

  // The Cache instance
  private static Cache cache = new Cache();

  private Cache() {}

  /**
  * Gets the instance of the Cache
  */
  public static Cache getInstance() {
    return cache;
  }

  /**
  * An inner class for storing information about a request
  */
  private class Request {
    private long time; // The time the request was cached
    private JSONObject response; // The JSON data from forecast.io (truncated)

    public Request(long time, JSONObject response) {
      this.time = time;
      this.response = response;
    }

    public long getTime() {
      return time;
    }

    public JSONObject getResponse() {
      return response;
    }
  }

  /**
  * Adds a new request to the HashMap
  */
  public void addRequest(String zip, JSONObject response) {
    if (requests.containsKey(zip)) { // If the zip code is cached, remove it
      requests.remove(zip);
    }
    long currentTime = new Long(System.currentTimeMillis()); // Get the time
    Request request = new Request(currentTime, response); // Create Request
    requests.put(zip, request); // Cache Request
  }

  /**
  * Returns cached data (if it exists) for a zip code
  */
  public JSONObject getRequest(String zip) {
    if (requests.containsKey(zip)) { // If the zip code is cached
      long timeOfRequest = new Long(requests.get(zip).getTime());
      long currentTime = new Long(System.currentTimeMillis());
      long elapsedTime = new Long(currentTime - timeOfRequest);
      long fiveMinutes = new Long(300000);
      // If it has been cached for longer than five minutes then remove it
      // and return null
      if (elapsedTime >= fiveMinutes) {
        requests.remove(zip);
        return null;
      }
      // Else return the cached data
      else {
        return requests.get(zip).getResponse();
      }
    } // If it isn't cached, return null
    return null;
  }
}
