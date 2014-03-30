package org.iitg.mobileprofiler.mobilecore;

import com.iitg.mobileprofiler.Databasequestion;
import com.iitg.mobileprofiler.Feedback;
import com.iitg.mobileprofiler.R;
import com.iitg.mobileprofiler.SampleTestAcitivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MobileProfierMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
	
	public void createNewActivity(View view){
		Intent intent = new Intent(this, SampleTestAcitivity.class);
		startActivity(intent);
	}
	
	public void getFeedback(View view){
		Intent intent = new Intent(this, Databasequestion.class);
		startActivity(intent);
	}
	
	public void setFeedback(View view){
		Intent intent = new Intent(this, Feedback.class);
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}