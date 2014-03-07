package org.iitg.mobileprofiler.mobilecore;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.iitg.mobileprofiler.db.ActivityDao;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Browser;
import android.util.Log;
import android.widget.Toast;

public class ExperimentalService extends Service {
	private Timer timer;

	/**
	 * This function saves BrowserHistory.
	 * 
	 * @return
	 */
	public void getBrowserHistory() {
		Log.i("Experimental Something", " new ");
		ArrayList<ActivityDao> activityDaos = new ArrayList<ActivityDao>();
		String[] proj = new String[] { Browser.BookmarkColumns.TITLE,
				Browser.BookmarkColumns.URL, Browser.BookmarkColumns.DATE };
		String sel = Browser.BookmarkColumns.BOOKMARK + " = 0";
		Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI, proj,
				sel, null, null);
		mCur.moveToFirst();
		String title = "";
		String url = "";
		String timeOfAccess = "";
		if (mCur.moveToFirst() && mCur.getCount() > 0) {
			boolean cont = true;
			while (mCur.isAfterLast() == false && cont) {
				title = mCur.getString(mCur
						.getColumnIndex(Browser.BookmarkColumns.TITLE));
				url = mCur.getString(mCur
						.getColumnIndex(Browser.BookmarkColumns.URL));
				timeOfAccess = mCur.getString(mCur
						.getColumnIndex(Browser.BookmarkColumns.DATE));

				Log.i("Browsing History : ", title + " - " + url + " - "
						+ timeOfAccess);
				activityDaos.add(new ActivityDao(0, "Web", url, timeOfAccess,
						"Not Assigned"));
				mCur.moveToNext();
			}
		}
		try{
			FileWriter fileWriter = new FileWriter(Environment.getExternalStorageDirectory().getPath() + "/experimentalOutput.txt");
			for(ActivityDao activityDao : activityDaos){
				fileWriter.write(activityDao.getActivityId() + " ||| " + activityDao.getActivityType() + " ||| " + activityDao.getActivityInfo() + " ||| " + activityDao.getTimeStamp() + "\n");
			}
			fileWriter.close();	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * This is the timer task that runs periodically.
	 */
	private TimerTask writeUserDataToFile = new TimerTask() {
		@Override
		public void run() {
			getBrowserHistory();
			stopService(new Intent(getBaseContext(),
					ExperimentalService.class));
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Experimental Service Started", Toast.LENGTH_LONG)
				.show();
		timer = new Timer("writeUserDataToFileTimer");
		timer.schedule(writeUserDataToFile, 1000L, 50000000 * 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Experimental Service Destroyed", Toast.LENGTH_LONG)
				.show();
	}
}
