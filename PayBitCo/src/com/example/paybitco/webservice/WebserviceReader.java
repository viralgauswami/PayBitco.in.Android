package com.example.paybitco.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebserviceReader {
	
	StringBuilder builder;
	JSONObject resObj;
	public JSONObject sendRequest(String url)
	{
		try {
			String postUrl = url;
			HttpClient client = (HttpClient)getNewHttpClient();
			HttpPost request = new HttpPost(postUrl);
			HttpResponse response = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String line;
			builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			Log.d("Response", builder.toString());
			resObj = new JSONObject(builder.toString());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("ERROR", e.toString());
		}
		return resObj;
	}
	public JSONObject sendRequest(String url, JSONObject obj)
	{
		try {
			String postUrl = url;
			StringEntity se = new StringEntity(obj.toString());
			HttpClient client = (HttpClient)getNewHttpClient();
			HttpPost httppost = new HttpPost(postUrl);
			httppost.setEntity(se);
			httppost.setHeader("Accept", "application/json");
			httppost.setHeader("Content-type", "application/json");
			HttpResponse response = client.execute(httppost);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));
			String line;
			builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}
			Log.d("Response", builder.toString());
			resObj = new JSONObject(builder.toString());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("ERROR", e.toString());
		}
		return resObj;
	}
	public Bitmap loadImage(String url)
	{
		Bitmap image=null;
		try {
			String postUrl = url;
			Log.d("Image URL", postUrl);
			HttpClient client = (HttpClient)getNewHttpClient();
			HttpPost httppost = new HttpPost(postUrl);
			HttpResponse response = client.execute(httppost);
			// Convert images from stream to bitmap object
			image = BitmapFactory.decodeStream(response.getEntity().getContent());
		} catch (Exception e) {
			// TODO: handle exception
			Log.d("ERROR", e.toString());
		}
		return image;
	}
	public HttpClient getNewHttpClient() {
	    try {
	        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
	        trustStore.load(null, null);

	        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
	        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

	        HttpParams params = new BasicHttpParams();
	        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

	        SchemeRegistry registry = new SchemeRegistry();
	        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	        registry.register(new Scheme("https", sf, 443));

	        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

	        return new DefaultHttpClient(ccm, params);
	    } catch (Exception e) {
	        return new DefaultHttpClient();
	    }
	}
}
