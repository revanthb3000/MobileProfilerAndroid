package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;
import java.util.Date;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.p2p.peer.UserNodePeer;
import org.iitg.mobileprofiler.p2p.tools.UtilityFunctions;

import com.iitg.mobileprofiler.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	String TAG ="Main Act";

	ListView listView;
	
	public static UserNodePeer userNodePeer;
	
	private static final int PEER_PORT_NUMBER = 5689;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isTablet =isTablet(this);
		
		if(isTablet==true){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		setContentView(R.layout.activity_main);
		
		
		Intent intent = getIntent();
		String ipAddress = intent.getStringExtra("ip");
		String portNumber = intent.getStringExtra("port");
		
		Toast.makeText(this, "IP:"+ipAddress+"\n port:"+portNumber, Toast.LENGTH_SHORT).show();
		
		try{
			String phoneName = Build.ID + Build.MODEL + (new Date()).getTime();
			DatabaseConnector databaseConnector = new DatabaseConnector();
			ArrayList<Integer> userClassContents = databaseConnector.getNumberOfDocuments(0, databaseConnector.getNumberOfClasses(), true);
			databaseConnector.closeDBConnection();
			userNodePeer = new UserNodePeer(UtilityFunctions.getHexDigest(phoneName), phoneName, PEER_PORT_NUMBER , userClassContents, ipAddress + ":" + portNumber, null, 0);
			userNodePeer.joinToBootstrapPeer();	
		}
		catch(Exception e){
			e.printStackTrace();
			Log.e("MainActivityError", "Error in getting classcontents/connecting to bootstrap");
		}
		
	}
	
	public static boolean isTablet(Context context) {
	    return (context.getResources().getConfiguration().screenLayout
	            & Configuration.SCREENLAYOUT_SIZE_MASK)
	            >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public void MenuClick(View v){
		Intent intent = null;
		
		switch(v.getId()){
		
		case R.id.ll_btn_a1:
			Log.v(TAG,"Profiler Service Started");
			startService(new Intent(getBaseContext(), MobileProfilerService.class));
			break;
			
		case R.id.ll_btn_a2:
			Log.v(TAG,"Classifier Service Started");
			startService(new Intent(getBaseContext(), ClassifierService.class));
			break;
			
		case R.id.ll_btn_a3:
			Log.v(TAG,"Feature Computation Service Started");
			startService(new Intent(getBaseContext(), FeatureComputationService.class));
			break;
			
		case R.id.ll_btn_b1:
			Log.v(TAG,"Ask a question activity started.");
			intent = new Intent(this, AskQuestionActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ll_btn_b2:
			Log.v(TAG,"Getting feedback for questions asked.");
			intent = new Intent(this, GetFeedbackActivity.class);
			startActivity(intent);
			break;
			
		case R.id.ll_btn_b3:
			Log.v(TAG,"Answering questions");
			intent = new Intent(this, GiveFeedbackActivity.class);
			startActivity(intent);
			break;
			
		case R.id.btn_c1:
			Log.v(TAG,"Updating Repo");
			userNodePeer.updateRepo();
			break;
			
		case R.id.btn_c2:
			Log.v(TAG,"Checking Response Repo.");
			intent = new Intent(this, DisplayResponseRepoActivity.class);
			startActivity(intent);
			break;
		}
	}
}
