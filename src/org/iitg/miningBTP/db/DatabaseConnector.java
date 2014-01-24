package org.iitg.miningBTP.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

public class DatabaseConnector {

	private static final String DATABASE_FILE_NAME = Environment
			.getExternalStorageDirectory().getPath()
			+ "/MobileProfilerDatabase/mobileclassifier.db";

	private Connection connection;

	private SQLiteDatabase sqLiteDatabase;

	public DatabaseConnector() {
		openDBConnection();
	}

	public static String getDatabaseFileName() {
		return DATABASE_FILE_NAME;
	}

	public Connection getConnection() {
		return connection;
	}

	public void openDBConnection() {
		sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
				DATABASE_FILE_NAME, null);
	}

	public void closeDBConnection() {
		sqLiteDatabase.close();
	}

	public TermDistributionDao getTermDistribution(String term, int classId) {
		String query = "SELECT * from termdistribution where feature='" + term
				+ "' AND classId=" + classId + ";";

		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		TermDistributionDao termDistributionDao = null;
		if (cursor != null && cursor.moveToFirst()) {
			int a = cursor
					.getInt(cursor.getColumnIndex(cursor.getColumnName(2)));
			termDistributionDao = new TermDistributionDao(term, classId, a);
		}
		cursor.close();
		return termDistributionDao;
	}

	public Map<Integer, TermDistributionDao> getAllTermDistribution(String term) {
		String query = "SELECT * from termdistribution where feature='" + term
				+ "';";

		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		Map<Integer, TermDistributionDao> termDistributionDaos = new HashMap<Integer, TermDistributionDao>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int classId = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(1)));
				int a = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(2)));
				TermDistributionDao termDistributionDao = new TermDistributionDao(term, classId, a);
				termDistributionDaos.put(termDistributionDao.getClassId(),termDistributionDao);
			} while (cursor.moveToNext());
		}
		return termDistributionDaos;
	}

	public Map<String, Map<Integer, TermDistributionDao>> getAllTokensDistribution(
			ArrayList<String> tokens) {
		if (tokens.size() == 0) {
			return null;
		}
		String term = tokens.get(0), previousTerm = tokens.get(0);
		String query = "SELECT * from termdistribution where feature='" + term
				+ "'";
		for (int i = 1; i < tokens.size(); i++) {
			term = tokens.get(i);
			if (term.equals(previousTerm)) {
				continue;
			}
			previousTerm = term;
			query += " OR feature='" + term + "'";
		}
		query += ";";
		Map<String, Map<Integer, TermDistributionDao>> termDistributionMap = new HashMap<String, Map<Integer, TermDistributionDao>>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String token = cursor.getString(cursor.getColumnIndex(cursor
						.getColumnName(0)));
				int classId = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(1)));
				int a = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(2)));
				TermDistributionDao termDistributionDao = new TermDistributionDao(
						token, classId, a);
				if (termDistributionMap.containsKey(token)) {
					termDistributionMap.get(token).put(
							termDistributionDao.getClassId(),
							termDistributionDao);
				} else {
					Map<Integer, TermDistributionDao> termDistributionDaos = new HashMap<Integer, TermDistributionDao>();
					termDistributionMap.put(token, termDistributionDaos);
					termDistributionMap.get(token).put(
							termDistributionDao.getClassId(),
							termDistributionDao);
				}
			} while (cursor.moveToNext());
		}
		return termDistributionMap;
	}

	public Boolean isTermFeature(String term) {
		String query = "SELECT * from featurelist where feature='" + term
				+ "';";
		Boolean isTermFeature = false;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			isTermFeature = true;
		}
		cursor.close();
		return isTermFeature;
	}

	public int getNumberOfClasses() {
		String query = "SELECT Count(className) from classmapping;";
		int numberOfClasses = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			numberOfClasses = cursor.getInt(0);
		}
		cursor.close();
		return numberOfClasses;
	}

	public int getTotalNumberOfDocuments() {
		String query = "SELECT SUM(numberOfDocs) from classcontents;";
		int totalNumberOfDocs = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			totalNumberOfDocs = cursor.getInt(0);
		}
		cursor.close();
		return totalNumberOfDocs;
	}

	public ArrayList<Integer> getNumberOfDocuments(int startingClassId,
			int endingClassId) {
		String query = "SELECT numberOfDocs from classcontents where classId>="
				+ startingClassId + " AND classId<=" + endingClassId + ";";
		ArrayList<Integer> classContents = new ArrayList<Integer>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				classContents.add(cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(0))));
			} while (cursor.moveToNext());
		}
		cursor.close();
		return classContents;
	}

	public String getClassName(int classId) {
		String query = "SELECT className from classmapping where classId="
				+ classId + ";";
		String className = "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			className = cursor.getString(0);
		}
		cursor.close();
		return className;
	}

	public ArrayList<String> getTermsList() {
		String query = "SELECT distinct(feature) from termdistribution;";
		ArrayList<String> termsList = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				termsList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return termsList;
	}

	public void deleteFeatures() {
		String query = "Delete from featurelist";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Important Note: Inserting stuff one by one is highly inefficient and is
	 * slow as fuck. Why ? Multiple reads and writes. So, what do we do now ?
	 * Write a single query to do multiple inserts. http://goo.gl/6zVjeQ for
	 * reference. I'm doing 450 inserts in one query because apparently, Sqlite
	 * cuts you off at 500.
	 * 
	 * @param features
	 */
	public void insertFeatures(ArrayList<String> features) {
		String query = "";
		for (int i = 0; i < features.size(); i++) {
			if (i % 450 == 0) {
				sqLiteDatabase.execSQL(query);
				query = "INSERT INTO `featurelist` SELECT '" + features.get(i)
						+ "' AS 'feature' ";
			} else {
				query += "UNION SELECT '" + features.get(i) + "' ";
			}
		}
		sqLiteDatabase.execSQL(query);
	}

	public Boolean isTermPresentInDataBase(String term) {
		String query = "SELECT * from termdistribution where feature='" + term
				+ "';";
		Boolean isTermPrsentInDataBase = false;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			isTermPrsentInDataBase = true;
		}
		cursor.close();
		return isTermPrsentInDataBase;
	}

	public void updateClassContents(int classId) {
		String query = "Update `classcontents` SET numberOfDocs = numberOfDocs + 1 Where classId="
				+ classId + ";";
		sqLiteDatabase.execSQL(query);
	}

	public void updateTermDistribution(ArrayList<String> tokens, int classId) {
		int numberOfClasses = getNumberOfClasses();
		ArrayList<String> oldTerms = new ArrayList<String>();
		ArrayList<String> newTerms = new ArrayList<String>();
		for (String term : tokens) {
			if (!isTermPresentInDataBase(term)) {
				newTerms.add(term);
			} else {
				oldTerms.add(term);
			}
		}
		updateTermInfo(oldTerms, classId);
		insertTermInfo(newTerms, classId, numberOfClasses);
	}

	public void insertTermInfo(ArrayList<String> newTerms, int classId,
			int numberOfClasses) {
		String query = "";
		for (int i = 0; i < newTerms.size(); i++) {
			if (i % 450 == 0) {
				sqLiteDatabase.execSQL(query);
				query = "INSERT INTO `termdistribution` Select '"
						+ newTerms.get(i) + "' AS `feature`, " + classId
						+ " AS `classId`, 1 AS `A`";
			} else {
				query += "UNION SELECT '" + newTerms.get(i) + "'," + classId
						+ ",1 ";
			}
		}
		sqLiteDatabase.execSQL(query);
	}

	public void updateTermInfo(ArrayList<String> oldTerms, int classId) {
		int numOfTerms = oldTerms.size();
		int iterator = 0;
		while(iterator<numOfTerms){
			String query = "Update termdistribution SET `A` = `A` + 1 Where (feature='"
					+ oldTerms.get(iterator) + "' ";
			iterator++;
			while(iterator%990!=0 && iterator<numOfTerms){
				query += " OR feature='" + oldTerms.get(iterator) + "'";
				iterator++;
			}
			query += ") and classId=" + classId + ";";
			sqLiteDatabase.execSQL(query);			
		}
	}

	public void insertActivityIntoDB(ArrayList<ActivityDao> activityDaos) {
		String query = "";
		for (int i = 0; i < activityDaos.size(); i++) {
			if (i % 450 == 0) {
				if(!query.equals("")){
					sqLiteDatabase.execSQL(query);	
				}
				query = "INSERT INTO `activities`( `activityType`, `activityInfo`, `timeStamp`, `assignedClass`) Select '"
						+ activityDaos.get(i).getActivityType()
						+ "' AS `activityType`, '"
						+ activityDaos.get(i).getActivityInfo()
						+ "' AS `activityInfo`, '"
						+ activityDaos.get(i).getTimeStamp()
						+ "' AS `timeStamp`, '"
						+ activityDaos.get(i).getAssignedClass()
						+ "' AS `assignedClass`";
			} else {
				query += "UNION SELECT '"
						+ activityDaos.get(i).getActivityType() + "','"
						+ activityDaos.get(i).getActivityInfo().replace("'","''") + "','"
						+ activityDaos.get(i).getTimeStamp() + "','"
						+ activityDaos.get(i).getAssignedClass() + "'  ";
			}
		}
		Log.i("SQL Query :",query);
		sqLiteDatabase.execSQL(query);
	}
	
	public String getMaxActivityTimeStamp(){
		String query = "SELECT MAX(timeStamp) from activities;";
		String maxTimeStamp = "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			maxTimeStamp = cursor.getString(0);
		}
		cursor.close();
		if(maxTimeStamp==null){
			maxTimeStamp = "0";
		}
		return maxTimeStamp;
	}
	
	public ArrayList<ActivityDao> getUnclassifiedActivities(){
		String query = "SELECT `activityId`, `activityType`, `activityInfo`, `timeStamp`, `assignedClass` from activities where assignedClass='Not Assigned'";
		ArrayList<ActivityDao> activityDaos = new ArrayList<ActivityDao>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int activityId = cursor.getInt(0);
				String activityType = cursor.getString(1);
				String activityInfo = cursor.getString(2);
				String timeStamp = cursor.getString(3);
				String assignedClass = cursor.getString(4);
				ActivityDao activityDao = new ActivityDao(activityId, activityType, activityInfo, timeStamp, assignedClass);
				activityDaos.add(activityDao);
			} while (cursor.moveToNext());
		}
		return activityDaos;
	}
	
	public void updateActivities(ArrayList<ActivityDao> activityDaos){
		if(activityDaos.size()==0){
			return;
		}
		String query = "UPDATE `activities` SET `assignedClass` = CASE activityId ";
		String queryPartTwo = "";
		for(ActivityDao activityDao : activityDaos){
			query += " When " + activityDao.getActivityId() + " THEN '"+activityDao.getAssignedClass()+"'";
			queryPartTwo += activityDao.getActivityId() + ",";
		}
		query += " END WHERE activityId in (" + queryPartTwo + ");";
		query = query.replace(",);", ");");
		sqLiteDatabase.execSQL(query);
	}

}
