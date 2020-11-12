/**
 * project: ShockVx
 *  module: Stickershock Android App for cold chain tracking.
 *  author: Velvetwire, llc
 *    file: NetworkScreen.java
 *
 *  Internet connections and REST calls
 *
 * (c) Copyright 2020 Velvetwire, LLC. All rights reserved.
 */
package com.ice.stickershock_shockvx.network;

import android.app.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


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



public class NetworkScreen extends Activity  {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.venue);
        makeRestCall( );
    }


    public void onResume() {
        super.onResume();

    }

    public void setBookmark(String id) {

    }


    private void makeRestCall ( ) {

        String url = "http://www.velvetwire.com";
        NetworkInterface ni = new NetworkInterface();
        ni.execute(url);
    }

    public class NetworkInterface extends AsyncTask<String, Void, String> {
        public String server_response;


        @Override
	    protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    Log.v("CatalogClient", server_response);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
	    protected void onPostExecute(String s) {

            super.onPostExecute(s);

            Log.e("Response", "" + server_response);
            doVenueJSONDecode(server_response);
        }


	// Converting InputStream to String

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }



        protected void doVenueJSONDecode(String Content) {
            JSONObject jsonResponse;
            String id;
            String name;
            String address;
            String catName;
            String latitude;
            String longitude;
            String phone;
            String url = "";
            try {

                JSONObject jsonObject = new JSONObject(Content);
                JSONObject response = jsonObject.getJSONObject("response");
                JSONArray venues = response.getJSONArray("venues");
                /*********** Process each JSON Node ************/

                for (int i = 0; i < venues.length(); i++) {
                    /****** Get Object for each JSON node.***********/

		    JSONObject v = venues.getJSONObject(i);
		    catName = null;
                    /******* Fetch node values **********/
                    id = v.optString("id");
                    name = v.optString("name");
                    JSONObject location = v.getJSONObject("location");
                    address = location.optString("address");
                    latitude = location.optString("lat");
                    longitude = location.optString("lng");
                    phone     = location.optString("city") + " " +
                                location.optString("state") + " " +
                                location.optString("formattedPhone");
                    JSONObject contact = v.getJSONObject("contact");
                    phone = contact.optString("formattedPhone");
                    JSONArray categories = v.getJSONArray("categories");

                    for (int j = 0; j < categories.length(); j++) {
                        JSONObject cat = categories.getJSONObject(j);
                        catName = cat.optString("name");
                        JSONObject icon = cat.getJSONObject("icon");
                        url = icon.optString("prefix") + "100" + icon.optString(
										"suffix");
                    }
                    Log.e("id", "" + id + "  " + name + "  " + address + " " + catName + " " + url + " " + latitude + " " + longitude + " " + phone);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    // gets characters after last slash in url, usually filename
    private String getFileName(String url)  {
        String buffer = "";
        for (String token : url.split("/"))
	    {
		buffer = token;
	    }
        return buffer;
    }


}


