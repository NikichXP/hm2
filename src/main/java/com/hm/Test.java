package com.hm;

import org.bson.Document;

import static com.hm.manualdb.ConnectionHandler.db;

public class Test {

	public static void main(String[] args) {
		db().getCollection("category").updateOne(Document.parse("{'groups.name': 'Фотограф'}"),
				Document.parse("{$set : {'groups.$.executors' : 1}}"));
	}
}
