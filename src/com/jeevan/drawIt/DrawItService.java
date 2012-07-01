package com.jeevan.drawIt;

import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

public class DrawItService extends Service implements IOCallback {
	private SocketIO socket;
	private Messenger messenger = null;
	private JSONObject jsonObject;

	
	
	public void sendMessage (String event, JSONObject jsonObject2) {
		socket.emit(event, jsonObject2);
	}
	
	
	
	public void addMessenger (Messenger messenger) {
		this.messenger = messenger;
	}
	
	public void connectWebsocket () {
		socket = new SocketIO();
		try {
			socket.connect(
					"http://ec2-46-137-155-119.eu-west-1.compute.amazonaws.com:80",
					this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

	@Override
	public void onCreate() {
		jsonObject = new JSONObject();
		super.onCreate();
	}

	public class LocalBinder extends Binder {
		DrawItService getService() {
			return DrawItService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	@Override
	public void onDestroy() {
		socket.disconnect();
		super.onDestroy();
	}

	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
			Log.d("2","Server said:" + json.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		System.out.println("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occured");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		System.out.println("Connection terminated.");
	}

	@Override
	public void onConnect() {
		System.out.println("Connection established");
		socket.emit("joinroom", "Gult");
	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		Log.d("dd", "Server triggered event '" + event + "'");
	}

}
