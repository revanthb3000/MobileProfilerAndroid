package org.iitg.mobileprofiler.mobilecore;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.iitg.mobileprofiler.db.ActivityDao;
import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.helpers.EmailManager;

import android.app.Service;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Browser;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import android.provider.CallLog;

public class MobileProfilerService extends Service {

	private Timer timer;

	/**
	 * This function saves BrowserHistory.
	 * 
	 * @return
	 */
	public void getBrowserHistory() {
		Log.i("Something", " new ");
		DatabaseConnector databaseConnector = new DatabaseConnector();
		BigInteger maxTimeStamp = new BigInteger(
				databaseConnector.getMaxActivityTimeStamp());
		Log.i("Updates: ", "Max Timestamp is " + maxTimeStamp);
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

				Log.i("Browsing History : ",  title + " - " + url + " - " + timeOfAccess);
				if (new BigInteger(timeOfAccess).compareTo(maxTimeStamp) >= 0) {
					activityDaos.add(new ActivityDao(0, "Web", url, timeOfAccess, "Not Assigned"));
				}
				mCur.moveToNext();
			}
		}
		databaseConnector.insertActivityIntoDB(activityDaos);
		databaseConnector.closeDBConnection();
	}

	/**
	 * This function checks email
	 * 
	 * @return
	 */
	public String getEmails() {
		EmailManager emailManager = new EmailManager();
		String data = "";
		try {
			Message[] emails = emailManager.getMails();
			Log.i("Emails are : ", " these: " + emails);
			for (int i = 0; i < emails.length; i++) {
				data += emails[i].toString();
				Log.i("Email: ", emails[i].toString());
				Log.i("Subject: ", emails[i].getSubject());
			}
		} catch (MessagingException e) {
			Log.e("Email error : ", e.toString());
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * This function retrieves the list of apps installed.
	 * 
	 * @return
	 */
	public String getInstalledAppsList() {
		String data = "";
		PackageManager pm = getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {
			Log.d("APPS INSTALLED : ", "Installed package :"
					+ packageInfo.packageName);
			data = data + "\n" + packageInfo.packageName;
		}
		return data;
	}

	/**
	 * Retrieves all contacts.
	 * 
	 * @return
	 */
	public String getContacts() {
		String contacts = "";
		Cursor phones = getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
				null, null);

		while (phones.moveToNext()) {
			String Name = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			String Number = phones
					.getString(phones
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			contacts = contacts + " name-" + Name + " number" + Number;
		}
		return contacts;

	}

	/**
	 * This function retrieves Call details.
	 * 
	 * @return
	 */
	public String getCallDetails() {
		StringBuffer sb = new StringBuffer();
		Cursor managedCursor = getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null, null);
		int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
		sb.append("Call Details :");
		while (managedCursor.moveToNext()) {
			String phNumber = managedCursor.getString(number);
			String callType = managedCursor.getString(type);
			String callDate = managedCursor.getString(date);
			// Date callDayTime = new Date(Long.valueOf(callDate));
			String callDuration = managedCursor.getString(duration);
			String dir = null;
			int dircode = Integer.parseInt(callType);
			switch (dircode) {
			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;

			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;

			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			sb.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
					+ dir + " \nCall Date:--- " + callDate
					+ " \nCall duration in sec :--- " + callDuration);
			sb.append("\n----------------------------------");
		}
		managedCursor.close();
		return sb.toString();
	}

	/**
	 * This is the timer task that runs periodically.
	 */
	private TimerTask collectUserProfileData = new TimerTask() {
		@Override
		public void run() {
			getBrowserHistory();
			String totalData = getCallDetails()
					+ "\n" + getContacts()
					// + "\n" + getEmails()
					+ "\n" + getInstalledAppsList();
			totalData = totalData.replace("\n", "\n\n");
			stopService(new Intent(getBaseContext(), MobileProfilerService.class));
		}
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Toast.makeText(this, "Profiler Service Started", Toast.LENGTH_LONG)
				.show();
		timer = new Timer("collectUserProfileDataTimer");
		timer.schedule(collectUserProfileData, 1000L,50000000 * 1000L);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Toast.makeText(this, "Profiler Service Destroyed", Toast.LENGTH_LONG)
				.show();
	}

}
