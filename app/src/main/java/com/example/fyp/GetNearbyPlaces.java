package com.example.fyp;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

//class to get nearby hospitals, pharmacies, GP  and display on map for user
public class GetNearbyPlaces extends AsyncTask<Object, String, String> {

    GoogleMap map;
    String url;
    InputStream is;
    BufferedReader bufferedReader;
    StringBuilder stringBuilder;
    String data;


    @Override
    protected String doInBackground(Object... objects) {
        //define object parameters
        map =(GoogleMap) objects[0];
        url = (String) objects[1];

//requesting API response
        try {
            URL mUrl = new URL(url);
            //open url connection
            HttpURLConnection urlConnection = (HttpURLConnection) mUrl.openConnection();
            urlConnection.connect();
            is = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line = "";
            //initialise string builder
            stringBuilder = new StringBuilder();
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line);

            }
            //turn data into string
            data = stringBuilder.toString();

        } catch (MalformedURLException e) {
            e.printStackTrace();
            //io exception for open connection
        } catch(IOException e){
            e.printStackTrace();
        }

        return data;
    }

    //parse the data
    @Override
    protected void onPostExecute(String s) {
        //string s will contain json parsed data
        try { //parsing data retrieved result is name of array in postman
            JSONObject parentObject = new JSONObject(s);
            JSONArray result = parentObject.getJSONArray("results");

            for(int i=0; i<result.length();i++){
                JSONObject jsonObject = result.getJSONObject(i);
                JSONObject location = jsonObject.getJSONObject("geometry").getJSONObject("location");
                String latitude = location.getString("lat");
                String longitude = location.getString("lng");

                JSONObject nameObj = result.getJSONObject(i);
                String nameHosp = nameObj.getString("name");
                String vicinity = nameObj.getString("vicinity");

                LatLng latLng = new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(vicinity);
                markerOptions.title(nameHosp);
                markerOptions.position(latLng);

                map.addMarker(markerOptions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
