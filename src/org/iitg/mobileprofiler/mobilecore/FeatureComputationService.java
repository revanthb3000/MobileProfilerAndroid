package org.iitg.mobileprofiler.mobilecore;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.iitg.mobileprofiler.core.Classifier;
import org.iitg.mobileprofiler.db.DatabaseConnector;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FeatureComputationService extends Service {

	private Timer timer;
	
	/**
	 * This is the timer task that runs periodically.
	 */
	private TimerTask computeFeaturesList = new TimerTask() {
		@Override
		public void run() {
			DatabaseConnector databaseConnector = new DatabaseConnector();
			Classifier classifier = new Classifier(databaseConnector);
			classifier.recomputeFeatures();
			ArrayList<String> features = databaseConnector.getAllFeaturesList();
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(Environment
						.getExternalStorageDirectory().getPath()
						+ "/MobileProfilerDatabase/features.txt");
				for (String feature : features) {
					fileWriter.write(feature + "\n");
				}
				fileWriter.close();
			} catch (IOException e) {
				Log.e("Error error : ", "FATAL ERROR");
				e.printStackTrace();
			}
			databaseConnector.closeDBConnection();
			stopService(new Intent(getBaseContext(), FeatureComputationService.class));
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Feature Computation Service has started.", Toast.LENGTH_LONG).show();
		timer = new Timer("computeFeaturesListTimer");
		timer.schedule(computeFeaturesList, 1000L,50000000 * 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Feature Computation Service Destroyed", Toast.LENGTH_LONG)
				.show();
	}

}
