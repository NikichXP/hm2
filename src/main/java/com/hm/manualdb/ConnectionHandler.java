package com.hm.manualdb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class ConnectionHandler {

	private static MongoClient client = null;
	private static MongoDatabase db;

	static {
		MongoClientURI mongoClientURI = new MongoClientURI("mongodb://heroku_6hlqqwnw:amm1faupj3i2th3edirt0lpl8k@ds119568.mlab.com:19568/heroku_6hlqqwnw");
		client = new MongoClient(mongoClientURI);
		db = client.getDatabase("heroku_6hlqqwnw");
	}

	public static MongoDatabase db() {
		return db;
	}

}
