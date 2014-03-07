package org.iitg.mobileprofiler.mobilecore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.iitg.mobileprofiler.core.Classifier;
import org.iitg.mobileprofiler.core.TextParser;
import org.iitg.mobileprofiler.db.ActivityDao;
import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class ClassifierService extends Service {

	private Timer timer;

	public String classifyText(String inputText) {
		TextParser textParser = new TextParser();
		ArrayList<String> tokens = textParser.tokenizeString(inputText, true);

		DatabaseConnector databaseConnector = new DatabaseConnector();
		Classifier classifier = new Classifier(databaseConnector);
		int classId = classifier.classifyDoc(tokens);
		String assignedClass = "N/A";
		if(classId != -1){
			assignedClass = databaseConnector.getClassName(classId);	
		}
		Log.i("Classifier : ", "I've finished classifying.");
		Log.i("Class is : ", assignedClass);

		databaseConnector.updateClassContents(classId, true);
		Log.i("Classifier : ", "I've finished updating class counts.");

		databaseConnector.updateTermDistribution(textParser.getAllTokens(inputText, true), classId,true);
		Log.i("Classifier : ", "I've finished updating term distribution.");

		databaseConnector.closeDBConnection();

		return assignedClass;
	}

	public String classifyUrl(String pageURL){
		try{
			URL url = new URL(pageURL);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					"202.141.80.22", 3128));
			Authenticator authenticator = new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() {
					return (new PasswordAuthentication("b.revanth",
							"batman9903".toCharArray()));
				}
			};
			Authenticator.setDefault(authenticator);
			URLConnection urlConnection = url.openConnection(proxy);
			urlConnection.connect();

			String line = null;
			StringBuffer webPageBuffer = new StringBuffer();
			BufferedReader inputReader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			while ((line = inputReader.readLine()) != null) {
				webPageBuffer.append(line);
			}

			Document document = Jsoup.parse(String.valueOf(webPageBuffer), "UTF-8");
			Elements title = document.select("title");
			Elements body = document.select("body");

			Log.i("Now Classifying ", pageURL);
			String assignedClass = classifyText(title.text() + "\n" + body.text());
			return assignedClass;
		}
		catch (IOException e){
			Log.i("Error:", e.toString());
			e.printStackTrace();
		}
		return "N/A";
	}

	/**
	 * This is the timer task that runs periodically.
	 */
	private TimerTask classifyData = new TimerTask() {
		@Override
		public void run() {
			DatabaseConnector databaseConnector = new DatabaseConnector();
			ArrayList<ActivityDao> activityDaos = databaseConnector.getUnclassifiedActivities();
			for(ActivityDao activityDao : activityDaos){
				String url = activityDao.getActivityInfo().trim();
				activityDao.setAssignedClass(classifyUrl(url));
			}
			databaseConnector.updateActivities(activityDaos);
			databaseConnector.closeDBConnection();
			stopService(new Intent(getBaseContext(), ClassifierService.class));
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Classifier Service Started", Toast.LENGTH_LONG)
				.show();
		timer = new Timer("classifyDataTimer");
		timer.schedule(classifyData, 1000L, 50000000 * 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Classifier Service Destroyed", Toast.LENGTH_LONG)
				.show();
	}

}
