package com.example.paybitco;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paybitco.webservice.WebserviceReader;

public class SignupActivity extends Activity {

	Context mContext;
	WebserviceReader wr;
	JSONObject country,countryInfo;
	String cityName,countryName,countryCode,zipCode,phoneCode,currencyCode,addUSR,addBTC;
	EditText edtCode,edtPhoneNo;
	TextView tvLocation,tvWelcome;
	TelephonyManager tm; 
	String number;
	ImageView ivFlag;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		mContext = this;
		wr = new WebserviceReader();
		
		//UI Elements
		ivFlag = (ImageView)findViewById(R.id.ivFlag);
		
		tvWelcome = (TextView)findViewById(R.id.tvWelcome); 
				
		edtCode = (EditText)findViewById(R.id.edtCode);
		edtPhoneNo = (EditText)findViewById(R.id.edtPhoneNo);
		
		tvLocation = (TextView)findViewById(R.id.tvLocation);
		
		country = new JSONObject();
		countryInfo = new JSONObject();
		
		tm = (TelephonyManager)getSystemService(mContext.TELEPHONY_SERVICE);
		number = tm.getLine1Number();
		Log.d("Mobile No ", number);
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if(isNetworkAvailable())
		{
			new SignupTask().execute(CommonObject.baseUrl+"users/signup/json");
		}
		else
		{
			Toast.makeText(mContext, "Connection is not available",Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup, menu);
		return true;
	}
	@SuppressLint("DefaultLocale")
	class SignupTask extends AsyncTask<String, Void, JSONObject>
	{
		ProgressDialog loading=new ProgressDialog(mContext);
		Bitmap flag;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			loading.setMessage("\t\t\tLoading...");
			loading.show();
		}
		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONObject obj = wr.sendRequest(params[0]);
			
			try {
				country = new JSONObject(obj.getString("Country"));
				addUSR = obj.getString("addUSR");
				addBTC = obj.getString("addBTC");
				countryInfo = new JSONObject(obj.getString("countryInfo"));
				countryName = country.getString("countryName");
				countryCode = country.getString("countryCode");
				countryCode = countryCode.toLowerCase();
				cityName = country.getString("cityName");
				zipCode = country.getString("zipCode");
				phoneCode = countryInfo.getString("Phone");
				currencyCode = countryInfo.getString("CurrencyCode"); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			flag = wr.loadImage(CommonObject.baseUrl+"img/Flags/"+countryCode+".gif");
			return obj;
		}
		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			loading.cancel();
			tvWelcome.setVisibility(View.VISIBLE);
			ivFlag.setImageBitmap(flag);
			tvWelcome.setText("Once you confirm, we will transfer "+currencyCode+" "+addUSR+" equivalent to "+addBTC+" BTC to your account.");
			edtCode.setText(phoneCode);
			edtPhoneNo.setText(number);
			tvLocation.setText(cityName+" "+zipCode+", "+countryName);
		}
	}
	//Check network is available or not 
    private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
}
