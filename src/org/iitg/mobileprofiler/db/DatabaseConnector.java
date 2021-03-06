package org.iitg.mobileprofiler.db;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

/**
 * This is the main class used to interact with the database. A class with
 * several methods in it. Contains methods for all database queries that will be
 * used.
 * 
 * @author RB
 * 
 */
public class DatabaseConnector {

	private static final String DATABASE_FILE_NAME = Environment
			.getExternalStorageDirectory().getPath()
			+ "/MobileProfilerDatabase/mobileclassifier.db";

	private Connection connection;

	private SQLiteDatabase sqLiteDatabase;

	/**
	 * Basic constructor. Opens a database connection.
	 */
	public DatabaseConnector() {
		openDBConnection();
	}

	public static String getDatabaseFileName() {
		return DATABASE_FILE_NAME;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Tries to open a DB connection with the sqlite database.
	 */
	public void openDBConnection() {
		sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(
				DATABASE_FILE_NAME, null);
	}

	/**
	 * Does what it says. Calling this function is crucial. Don't want any open
	 * connections lingering around !
	 */
	public void closeDBConnection() {
		sqLiteDatabase.close();
	}

	/**
	 * Given a word and classId, this function will return a TermDistributionDao
	 * object which will also contain the 'A' value.
	 * 
	 * @param term
	 * @param classId
	 * @param isUserDataTable
	 * @return
	 */
	public TermDistributionDao getTermDistribution(String term, int classId,
			boolean isUserDataTable) {
		String query = "SELECT * from "
				+ (isUserDataTable ? "userdataterms" : "termdistribution")
				+ " where term='" + term + "' AND classId=" + classId + ";";

		TermDistributionDao termDistributionDao = null;

		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			int a = cursor
					.getInt(cursor.getColumnIndex(cursor.getColumnName(2)));
			termDistributionDao = new TermDistributionDao(term, classId, a);
		}
		cursor.close();
		return termDistributionDao;
	}

	/**
	 * Given a term, this function will return a Map between classId and
	 * termDistributionDao objects. Useful function if you need the distribution
	 * of a term across all classes.
	 * 
	 * @param term
	 * @param isUserDataTable
	 * @return
	 */
	public Map<Integer, TermDistributionDao> getAllTermDistribution(
			String term, boolean isUserDataTable) {
		String query = "SELECT * from "
				+ (isUserDataTable ? "userdataterms" : "termdistribution")
				+ " where term='" + term + "';";

		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		Map<Integer, TermDistributionDao> termDistributionDaos = new HashMap<Integer, TermDistributionDao>();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int classId = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(1)));
				int a = cursor.getInt(cursor.getColumnIndex(cursor
						.getColumnName(2)));
				TermDistributionDao termDistributionDao = new TermDistributionDao(
						term, classId, a);
				termDistributionDaos.put(termDistributionDao.getClassId(),
						termDistributionDao);
			} while (cursor.moveToNext());
		}
		return termDistributionDaos;
	}

	/**
	 * Similar to the previous function. Given a set of tokens, a Map is
	 * returned that will contain info of the termDistribution of each term
	 * present in the ArrayList.
	 * 
	 * @param tokens
	 * @param isUserDataTable
	 * @return
	 */
	public Map<String, Map<Integer, TermDistributionDao>> getAllTokensDistribution(
			ArrayList<String> tokens, boolean isUserDataTable) {
		if (tokens.size() == 0) {
			return null;
		}
		String term = tokens.get(0), previousTerm = tokens.get(0);
		String query = "SELECT * from "
				+ (isUserDataTable ? "userdataterms" : "termdistribution")
				+ " where term='" + term + "'";
		for (int i = 1; i < tokens.size(); i++) {
			term = tokens.get(i);
			if (term.equals(previousTerm)) {
				continue;
			}
			previousTerm = term;
			query += " OR term='" + term + "'";
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

	/**
	 * Given a set of words, this function returns all those terms that are a
	 * part of our feature set.
	 * 
	 * @param tokens
	 * @return
	 */
	public ArrayList<String> getFeaturesFromTokensList(ArrayList<String> tokens) {
		if (tokens.size() == 0) {
			return null;
		}
		String term = tokens.get(0), previousTerm = tokens.get(0);
		String query = "SELECT * from featurelist where feature='" + term + "'";
		for (int i = 1; i < tokens.size(); i++) {
			term = tokens.get(i);
			if (term.equals(previousTerm)) {
				continue;
			}
			previousTerm = term;
			query += " OR feature='" + term + "'";
		}
		query += ";";
		ArrayList<String> features = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String token = cursor.getString(cursor.getColumnIndex(cursor
						.getColumnName(0)));
				features.add(token);
			} while (cursor.moveToNext());
		}
		return features;
	}

	/**
	 * Returns a list of all the features used by the classifier.
	 * 
	 * @return
	 */
	public ArrayList<String> getAllFeaturesList() {
		String query = "SELECT * from featurelist;";
		ArrayList<String> features = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String token = cursor.getString(cursor.getColumnIndex(cursor
						.getColumnName(0)));
				features.add(token);
			} while (cursor.moveToNext());
		}
		return features;
	}

	/**
	 * Tells if a given term is a feature or not.
	 * 
	 * @param term
	 * @return
	 */
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

	/**
	 * Queries the database and gets the total number of classes used.
	 * 
	 * @return
	 */
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

	/**
	 * Queries the database and gets the total number of documents that have
	 * been classified till now.
	 * 
	 * @param isUserDataTable
	 * @return
	 */
	public int getTotalNumberOfDocuments(boolean isUserDataTable) {
		String query = "SELECT SUM(numberOfDocs) from "
				+ (isUserDataTable ? "userdataclasscontents" : "classcontents")
				+ ";";
		int totalNumberOfDocs = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			totalNumberOfDocs = cursor.getInt(0);
		}
		cursor.close();
		return totalNumberOfDocs;
	}

	/**
	 * Returns the number of docs. that belong the classIds between two numbers.
	 * 
	 * @param startingClassId
	 * @param endingClassId
	 * @param isUserDataTable
	 * @return
	 */
	public ArrayList<Integer> getNumberOfDocuments(int startingClassId,
			int endingClassId, boolean isUserDataTable) {
		String query = "SELECT numberOfDocs from "
				+ (isUserDataTable ? "userdataclasscontents" : "classcontents")
				+ " where classId>=" + startingClassId + " AND classId<="
				+ endingClassId + ";";
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

	/**
	 * Given a classId, this function returns the name of the class.
	 * 
	 * @param classId
	 * @return
	 */
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

	/**
	 * Returns the list of all terms present in the database.
	 * 
	 * @param isUserDataTable
	 * @return
	 */
	public ArrayList<String> getTermsList(boolean isUserDataTable) {
		String query = "SELECT distinct(term) from "
				+ (isUserDataTable ? "userdataterms" : "termdistribution")
				+ ";";
		ArrayList<String> termsList = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				termsList.add(cursor.getString(0));
			} while (cursor.moveToNext());
		}
		return termsList;
	}

	/**
	 * Clears the features table and removes all entries.
	 */
	public void deleteFeatures() {
		String query = "Delete from featurelist";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Deletes all those entries where A=0. Those are useless and redundant.
	 * NOTE: Thus function is obsolete.
	 */
	public void deleteEmptyDistributions() {
		String query = "Delete from termdistribution where A=0;";
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

	/**
	 * Given a term and classId, this functions tells you if there's a
	 * <term,classId,A> mapping
	 * 
	 * @param term
	 * @param classId
	 * @param isUserDataTable
	 * @return
	 */
	public Boolean isTermPresentInTermDistribution(String term, int classId,
			boolean isUserDataTable) {
		String query = "SELECT * from "
				+ (isUserDataTable ? "userdataterms" : "termdistribution")
				+ " where term='" + term + "' and classId = " + classId + ";";
		Boolean isTermPresent = false;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			isTermPresent = true;
		}
		cursor.close();
		return isTermPresent;
	}

	/**
	 * Given a classId, the class contents for that classId are incremented.
	 * 
	 * @param classId
	 */
	public void updateClassContents(int classId, boolean isUserDataTable) {
		String query = "Update `"
				+ (isUserDataTable ? "userdataclasscontents" : "classcontents")
				+ "` SET numberOfDocs = numberOfDocs + 1 Where classId="
				+ classId + ";";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Given a set of terms, the termDistribution table is updated. Old terms
	 * are updated and new terms are added.
	 * 
	 * @param tokens
	 * @param classId
	 */
	public void updateTermDistribution(ArrayList<String> tokens, int classId,
			boolean isUserDataTable) {
		ArrayList<String> oldTerms = new ArrayList<String>();
		ArrayList<String> newTerms = new ArrayList<String>();
		for (String term : tokens) {
			if (!isTermPresentInTermDistribution(term, classId, isUserDataTable)) {
				newTerms.add(term);
			} else {
				oldTerms.add(term);
			}
		}
		updateTermInfo(oldTerms, classId, isUserDataTable);
		insertTermInfo(newTerms, classId, isUserDataTable);
	}

	/**
	 * Given a couple of terms and a classId, this function adds the
	 * <term,classId,A> mapping
	 * 
	 * @param newTerms
	 * @param classId
	 * @param isUserDataTable
	 */
	public void insertTermInfo(ArrayList<String> newTerms, int classId,
			boolean isUserDataTable) {
		String query = "";
		for (int i = 0; i < newTerms.size(); i++) {
			if (i % 450 == 0) {
				sqLiteDatabase.execSQL(query);
				query = "INSERT INTO `"
						+ (isUserDataTable ? "userdataterms"
								: "termdistribution") + "` Select '"
						+ newTerms.get(i) + "' AS `term`, " + classId
						+ " AS `classId`, 1 AS `A`";
			} else {
				query += "UNION SELECT '" + newTerms.get(i) + "'," + classId
						+ ",1 ";
			}
		}
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Increments the count of <term,classId,A> mappings.
	 * 
	 * @param oldTerms
	 * @param classId
	 */
	public void updateTermInfo(ArrayList<String> oldTerms, int classId,
			boolean isUserDataTable) {
		int numOfTerms = oldTerms.size();
		int iterator = 0;
		while (iterator < numOfTerms) {
			String query = "Update "
					+ (isUserDataTable ? "userdataterms" : "termdistribution")
					+ " SET `A` = `A` + 1 Where (term='"
					+ oldTerms.get(iterator) + "' ";
			iterator++;
			while (iterator % 950 != 0 && iterator < numOfTerms) {
				query += " OR term='" + oldTerms.get(iterator) + "'";
				iterator++;
			}
			query += ") and classId=" + classId + ";";
			sqLiteDatabase.execSQL(query);
		}
	}

	/*************************************************************************
	 * Methods that work on the activities table follow
	 **************************************************************************/

	/**
	 * Basic function that inserts an activityDao into the Database.
	 * 
	 * @param activityDaos
	 */
	public void insertActivityIntoDB(ArrayList<ActivityDao> activityDaos) {
		String query = "";
		for (int i = 0; i < activityDaos.size(); i++) {
			if (i % 450 == 0) {
				if (!query.equals("")) {
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
						+ activityDaos.get(i).getActivityType()
						+ "','"
						+ activityDaos.get(i).getActivityInfo()
								.replace("'", "''") + "','"
						+ activityDaos.get(i).getTimeStamp() + "','"
						+ activityDaos.get(i).getAssignedClass() + "'  ";
			}
		}
		Log.i("SQL Query :", query);
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * This function tells you the timeStamp of the last performed activity.
	 * Useful while inserting activities to the table. You don't insert
	 * something with a higher timestamp than this.
	 * 
	 * @return
	 */
	public String getMaxActivityTimeStamp() {
		String query = "SELECT MAX(timeStamp) from activities;";
		String maxTimeStamp = "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			maxTimeStamp = cursor.getString(0);
		}
		cursor.close();
		if (maxTimeStamp == null) {
			maxTimeStamp = "0";
		}
		return maxTimeStamp;
	}

	/**
	 * Gets an ArrayList of activityDaos which have not been assigned a class
	 * yet.
	 * 
	 * @return
	 */
	public ArrayList<ActivityDao> getUnclassifiedActivities() {
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
				ActivityDao activityDao = new ActivityDao(activityId,
						activityType, activityInfo, timeStamp, assignedClass);
				activityDaos.add(activityDao);
			} while (cursor.moveToNext());
		}
		return activityDaos;
	}

	/**
	 * Updates the className field of the activities table.
	 * 
	 * @param activityDaos
	 */
	public void updateActivities(ArrayList<ActivityDao> activityDaos) {
		if (activityDaos.size() == 0) {
			return;
		}
		String query = "UPDATE `activities` SET `assignedClass` = CASE activityId ";
		String queryPartTwo = "";
		for (ActivityDao activityDao : activityDaos) {
			query += " When " + activityDao.getActivityId() + " THEN '"
					+ activityDao.getAssignedClass() + "'";
			queryPartTwo += activityDao.getActivityId() + ",";
		}
		query += " END WHERE activityId in (" + queryPartTwo + ");";
		query = query.replace(",);", ");");
		sqLiteDatabase.execSQL(query);
	}

	/*************************************************************************
	 * Methods that work on the messages tables follow
	 **************************************************************************/

	/**
	 * Basic question that adds a question asked by our user to the
	 * questionmessages table.
	 * 
	 * @param question
	 */
	public void addQuestion(String question, String className) {
		String query = "INSERT INTO `questionmessages`(`question`,`className`) VALUES(\""
				+ question + "\",\"" + className + "\");";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * 
	 * When an answer is received, we store it here.
	 * 
	 * @param questionId
	 * @param answer
	 * @param similarity
	 */
	public void addAnswer(int questionId, int answer, Double similarity) {
		String query = "INSERT INTO `answermessages`(`questionId`,`answer`,`similarity`) VALUES("
				+ questionId + "," + answer + "," + similarity + ");";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Get the questionId of the last asked question.
	 * 
	 * @return
	 */
	public int getMaxQuestionId() {
		String query = "SELECT MAX(questionId) from questionmessages;";
		int questionId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			questionId = cursor.getInt(0);
		}
		cursor.close();
		return questionId;
	}

	/**
	 * Given a questionId, I'll return the weighted answer
	 * 
	 * @return
	 */
	public Double getWeightedAnswer(int questionId) {
		String query = "Select * from `answermessages` WHERE questionId = "
				+ questionId + ";";
		Double totalSimilarity = 0.0;
		Double weightedAnswer = 0.0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int answer = cursor.getInt(2);
				Double similarity = cursor.getDouble(3);
				weightedAnswer += (answer * similarity);
				totalSimilarity += similarity;
			} while (cursor.moveToNext());
			if (totalSimilarity == 0.0) {
				weightedAnswer = 0.0;
			} else {
				weightedAnswer = weightedAnswer / totalSimilarity;
			}
		}
		return weightedAnswer;
	}

	/**
	 * Given a questionId, the number of replies is returned.
	 * 
	 * @param questionId
	 * @return
	 */
	public int getNumberOfReplies(int questionId) {
		String query = "SELECT COUNT(answerId) from `answermessages` WHERE questionId = "
				+ questionId + ";";
		int numberOfAnswers = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			numberOfAnswers = cursor.getInt(0);
		}
		cursor.close();
		return numberOfAnswers;
	}
	
	/**
	 * Given a questionId, this will return the question. Null, if not.
	 * 
	 * @param questionId
	 * @return
	 */
	public String getQuestion(int questionId) {
		String query = "Select question from `questionmessages` WHERE questionId = "
				+ questionId + ";";
		String question = null;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			question = cursor.getString(0);
		}
		cursor.close();
		return question;
	}
	
	/**
	 * Get the className from Question ID
	 * 
	 * @return
	 */
	public String getQuestionClassName(int questionId) {
		String query = "SELECT className from questionmessages where questionId = "+questionId+";";
		String className = "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			className = cursor.getString(0);
		}
		cursor.close();
		return className;
	}

	
	/*************************************************************************
	 * Queries that run on the response Class table follow.
	 **************************************************************************/

	/**
	 * This is used to insert a className into the response class table.
	 * Standard query.
	 * @param className
	 */
	public void insertResponseClass(String className) {
		String query = "INSERT INTO responseclasses (`className`) VALUES ('"
				+ className + "');";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Given a classId, the className is returned.
	 * @param classId
	 * @return
	 */
	public String getResponseClassName(int classId) {
		String query = "SELECT className FROM `responseclasses` WHERE `classId` = "
						+ classId + "";
		String className = "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			className = cursor.getString(0);
		}
		cursor.close();
		return className;
	}
	
	/**
	 * Gets the number of various response Classes present.
	 * @return
	 */
	public int getNumberOfResponseClasses(){
		String query = "SELECT Count(className) from responseclasses;";
		int numberOfClasses = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			numberOfClasses = cursor.getInt(0);
		}
		cursor.close();
		return numberOfClasses;
	}
	
	/**
	 * Given a className, the classId is returned.
	 * @param classId
	 * @return
	 */
	public int getResponseClassId(String className) {
		String query = "SELECT classId FROM `responseclasses` WHERE `className` = \""
						+ className + "\";";
		int classId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			classId = cursor.getInt(0);
		}
		cursor.close();
		return classId;
	}
	
	/*************************************************************************
	 * Queries that run on the response table follow.
	 **************************************************************************/

	/**
	 * Given response data, this query inserts it into the table.
	 * 
	 * @param userId
	 * @param question
	 * @param answer
	 * @param className
	 */
	public void insertResponse(String userId, String question, int answer,
			int classId) {
		String query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`classId`)"
				+ "VALUES ('"
				+ userId
				+ "', '"
				+ question
				+ "', '"
				+ answer
				+ "', '" + classId + "');";
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Given an arraylist of response daos, this function will insert them into
	 * the DB.
	 * 
	 * @param responseDaos
	 */
	public void insertResponses(ArrayList<ResponseDao> responseDaos) {
		String query = "";
		for (int i = 0; i < responseDaos.size(); i++) {
			if (i % 450 == 0) {
				if (!query.equals("")) {
					sqLiteDatabase.execSQL(query);
				}
				query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`className`) Select '"
						+ responseDaos.get(i).getUserId()
						+ "' AS `userId`, '"
						+ responseDaos.get(i).getQuestion()
						+ "' AS `question`, '"
						+ responseDaos.get(i).getAnswer()
						+ "' AS `answer`, '"
						+ responseDaos.get(i).getClassId()
						+ "' AS `classId`";
			} else {
				query += "UNION SELECT '" + responseDaos.get(i).getUserId()
						+ "','" + responseDaos.get(i).getQuestion() + "','"
						+ responseDaos.get(i).getAnswer() + "','"
						+ responseDaos.get(i).getClassId() + "'  ";
			}
		}
		sqLiteDatabase.execSQL(query);
	}

	/**
	 * Gets the max responseId present in the table (we remove the user's Id
	 * from consideration)
	 * 
	 * @return
	 */
	public int getMaxResponseId(String blacklistUserId) {
		String query = "SELECT MAX(responseId) from responses WHERE userId!=\""
				+ blacklistUserId + "\";";
		int responseId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			responseId = cursor.getInt(0);
		}
		cursor.close();
		return responseId;
	}

	/**
	 * Given a response Dao, this function returns the maximum responseId.
	 * @param responseDao
	 * @return
	 */
	public int getResponseIdGivenDao(ResponseDao responseDao) {
		String query = "SELECT MAX(responseId) from responses WHERE userId=\""
				+ responseDao.getUserId() + "\" AND question=\""
				+ responseDao.getQuestion() + "\"" + " AND answer="
				+ responseDao.getAnswer() + " AND classId="
				+ responseDao.getClassId() + ";";
		int responseId = 0;
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			responseId = cursor.getInt(0);
		}
		cursor.close();
		return responseId;
	}
	
	/**
	 * Given a startingId and an endingId, this query will return an ArrayList
	 * of Responses.
	 * 
	 * @param startingId
	 * @param endingId
	 * @return
	 */
	public ArrayList<ResponseDao> getResponses(int startingId, int endingId) {
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		String query = "Select * from `responses` where responseId>="
				+ startingId + " AND responseId<=" + endingId + "";
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String userId = cursor.getString(1);
				String question = cursor.getString(2);
				int answer = cursor.getInt(3);
				int classId = cursor.getInt(4);
				responseDaos.add(new ResponseDao(userId, question, answer,
						classId));
			} while (cursor.moveToNext());
		}
		return responseDaos;
	}
	

	/**
	 * Given a question, this function returns all responses.
	 * @param question
	 * @return 
	 */
	public ArrayList<ResponseDao> getAnswersOfQuestion(String question){
		String query = "Select * from `responses` Where question='"+question+"';";
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String userId = cursor.getString(1);
				int answer = cursor.getInt(3);
				int classId = cursor.getInt(4);
				responseDaos.add(new ResponseDao(userId, question, answer,
						classId));
			} while (cursor.moveToNext());
		}
		return responseDaos;
	}
	
	/**
	 * Returns a list of unique questions in repo.
	 * @return
	 */
	public ArrayList<String> getQuestionsList(){
		String query = "Select distinct(question) from `responses`;";
		ArrayList<String> questions = new ArrayList<String>();
		Cursor cursor = sqLiteDatabase.rawQuery(query, null);
		if (cursor != null && cursor.moveToFirst()) {
			do {
				String question = cursor.getString(0);
				questions.add(question);
			} while (cursor.moveToNext());
		}
		return questions;
	}
	
}
