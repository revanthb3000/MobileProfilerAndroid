package org.iitg.mobileprofiler.mobilecore;

import java.util.ArrayList;
import java.util.Date;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.p2p.peer.UserNodePeer;
import org.iitg.mobileprofiler.p2p.tools.UtilityFunctions;

import com.iitg.mobileprofiler.Feedback;
import com.iitg.mobileprofiler.QnADisplayActivity;
import com.iitg.mobileprofiler.QueryActivity;
import com.iitg.mobileprofiler.Questions;
import com.iitg.mobileprofiler.R;

import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MobileProfierMainActivity extends Activity {
	
	public static UserNodePeer userNodePeer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		String phoneName = Build.ID + Build.MODEL + (new Date()).getTime();
		DatabaseConnector databaseConnector = new DatabaseConnector();
		ArrayList<Integer> userClassContents = databaseConnector.getNumberOfDocuments(0, databaseConnector.getNumberOfClasses(), true);
		databaseConnector.closeDBConnection();
		userNodePeer = new UserNodePeer(UtilityFunctions.getHexDigest(phoneName), phoneName, 5689, userClassContents, "192.168.1.3:5080", null, 0);
		userNodePeer.joinToBootstrapPeer();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void startMobileProfilerService(View view) {
		startService(new Intent(getBaseContext(), MobileProfilerService.class));
	} 
	
	public void startClassifierService(View view) {
		startService(new Intent(getBaseContext(), ClassifierService.class));
	}
	
	public void startFeatureComputationService(View view) {
		startService(new Intent(getBaseContext(), FeatureComputationService.class));
	}
	
	public void experimentalCollection(View view){
		startService(new Intent(getBaseContext(), ExperimentalService.class));
	}
	
	public void stopAllServices(View view){
		stopService(new Intent(getBaseContext(), MobileProfilerService.class));
		stopService(new Intent(getBaseContext(), ClassifierService.class));
		stopService(new Intent(getBaseContext(), FeatureComputationService.class));
		stopService(new Intent(getBaseContext(), ExperimentalService.class));
	}
	
	public void startAskQuestionActivity(View view){
		Intent intent = new Intent(this, QueryActivity.class);
		startActivity(intent);
	}
	
	public void getFeedback(View view){
		Intent intent = new Intent(this, QnADisplayActivity.class);
		startActivity(intent);
	}
	
	public void setFeedback(View view){
		Intent intent = new Intent(this, Feedback.class);
		startActivity(intent);
	}
	
	public void getQuestions(View view){
		Intent intent = new Intent(this, Questions.class);
		intent.putExtra("msg", "I am here!!");
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}