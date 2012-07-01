package com.jeevan.drawIt;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;



public class DrawItActivity extends Activity {
	
	private DrawItService mBoundService;
	final Messenger activityMessenger = new Messenger(new IncomingHandler());
 
	class IncomingHandler extends Handler {
	    @Override
	    public void handleMessage(Message msg) {
	                super.handleMessage(msg);
	    }
	}
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
//        startService(new Intent(DrawItActivity.this, DrawItService.class));
//        doBindService();
//         DrawView drawView =(DrawView) findViewById(R.id.DrawView);
//         OnTouchListener l = new OnTouchListener() {
//
//        	    @Override
//        	    public boolean onTouch(View v, MotionEvent event) {
//        	    	
//        	    	
//        	                        return true;
//        	    }
//        	    };
//         
//         
//         drawView.setOnTouchListener(l);
    }
    
    @Override
	protected void onDestroy() {
		stopService(new Intent(this, DrawItService.class));
		super.onDestroy();
	}
    
    
    private ServiceConnection mConnection = new ServiceConnection() {
    	
        public void onServiceConnected(ComponentName className, IBinder service) {
            mBoundService = ((DrawItService.LocalBinder)service).getService();
            mBoundService.connectWebsocket();
           // ((DrawView) findViewById(R.id.DrawView)).activityMessenger
            Log.d("J", "Binded successfully!");
            mBoundService.addMessenger(activityMessenger);
        }

        public void onServiceDisconnected(ComponentName className) {
            mBoundService = null;
        }
    };
    
    void doBindService() {
        bindService(new Intent(DrawItActivity.this, 
                DrawItService.class), mConnection, Context.BIND_AUTO_CREATE);
     
    }
    
    void doUnbindService() {
    	unbindService(mConnection);
        }
    
    

    
 
    
  
}