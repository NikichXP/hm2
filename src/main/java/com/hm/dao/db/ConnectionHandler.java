package com.hm.dao.db;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectionHandler {

	private static MongoClient client = null;
	private static MongoDatabase db;
	static {
		MongoClientURI mongoClientURI;

		try {
			mongoClientURI = new MongoClientURI(System.getenv("MONGODB_URI"));
			client = new MongoClient(mongoClientURI);
		} catch (Exception e) { //in case if system runs on local machine
			client = new MongoClient();
		}

		db = client.getDatabase("heroku_n7tnwh09");
	}

	public static MongoDatabase db() {
		return db;
	}

}
