# cps371-nathan-gray-server
Serves and caches requests for weather data from the application stored in cps371-nathan-gray-weather

# Server Address
http://cps-371-weather-app-server.appspot.com/

Requests made to the server must have a querystring of this structure: "zip=01984".

# API
Contains only the necessary data in a similar structure to the JSON returned by forecast.io.
Uses both a "currently" and a "daily" array with items of similar name and format.

{
  "currently": {
    "icon": "cloudy",
    "humidity": 0.64,
    "dewPoint": 27.73,
    "temperature": 38.99
  },
  "daily": [
    {
      "summary": "Partly cloudy overnight.",
      "maxTemp": 39.6,
      "icon": "partly-cloudy-night",
      "time": 1456117200,
      "minTemp": 26.66
    },
    ...
    ...
    ...
  ]
}

# Caching Strategy
The cache is organized in a singleton class called Cache.java

Requests sent to forecast.io are stored in a static HashMap indexed by zip code.

Each "Request" in the HashMap contains the JSON data that the app needs to display and also the time of the request.

Each new request sent to the server is checked by zip code with the data in the cache.
If there has been a weather request for the same area within the last five minutes, the server simply returns that data.
Otherwise it fetches new data and replaces the request in the HashMap.
