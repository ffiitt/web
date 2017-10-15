package com.example.project;



import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import android.os.Handler;
import android.os.Message;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class test extends Activity{
	private String responseMsg = "";
	Handler handler1;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		final Button login_button = (Button)findViewById(R.id.login);
		login_button.setOnClickListener(new login_listen());
		handler1 = new Handler(){
			@Override
			public void handleMessage(Message msg){
				if(msg.obj!=null){
					Toast.makeText(getApplication(), responseMsg,Toast.LENGTH_SHORT).show();
				}
			}
		};
		if(responseMsg.equals("success")){
			Intent intent = new Intent(test.this,my.class);//test其实是login
			startActivity(intent);
			finish();
		}else{
			System.out.println("nonoo");
		}
	}
	class login_listen implements OnClickListener{
		@Override
		public void onClick(View v){
			Thread loginThread = new Thread(new LoginThread());
			loginThread.start();
//			Thread loginThread1 = new Thread(new login());
//			loginThread1.start();
		}
	}
	public boolean loginserver(){
		boolean loginValidate = false;
		final EditText user = (EditText)findViewById(R.id.username);
		final EditText pass = (EditText)findViewById(R.id.password);
		String username = user.getText().toString();
		String password = pass.getText().toString();
		System.out.println(username+" "+password);
		String url = "http://10.0.2.2:8080/web/FirstServlet";
		url = url + "?" + "username=" + username + "&password=" + password;
		HttpClient client = new DefaultHttpClient();
    	HttpGet request  = new HttpGet(url);
    	try{   		
    		HttpResponse response = client.execute(request);
    		if(response.getStatusLine().getStatusCode()==200){
    			loginValidate = true;
    			responseMsg = EntityUtils.toString(response.getEntity());	
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return loginValidate;
	}
	class LoginThread implements Runnable{
		public void run(){
			System.out.println(2222);
			boolean loginValidate = loginserver();
			Message m = handler1.obtainMessage();
			m.obj = responseMsg;
			handler1.sendMessage(m);
			if(responseMsg.equals("success")){
				Intent intent = new Intent(test.this,my.class);//test其实是login
				startActivity(intent);
				finish();
			}else{
				System.out.println("nonoo");
			}
		}
	}
	public void sendjson(){
		String url = "http://10.0.2.2:8080/web/FirstServlet";
		HttpPost post = new HttpPost(url);
		final EditText user = (EditText)findViewById(R.id.username);
		final EditText pass = (EditText)findViewById(R.id.password);
		String muser = user.getText().toString();
		String mpass = pass.getText().toString();
		try{
			JSONObject json1 = new JSONObject();
			Object username = muser;
			json1.put("username", username);
			Object pwd = mpass;
			json1.put("password", pwd);
			//System.out.print(json1.toString());
			StringEntity se = new StringEntity(json1.toString());
			se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			post.setEntity(se);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(post);
			HttpEntity entity = response.getEntity();
			InputStream inputStream = entity.getContent();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			String s;
			StringBuffer result = new StringBuffer("");
			while((s=reader.readLine()) != null){
				result.append(s);
			}
			reader.close();
			JSONObject json = new JSONObject(result.toString());
			String name = json.getString("login");
			System.out.println(name);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	class login implements Runnable{
		public void run(){
			sendjson();
		}
	}

}
