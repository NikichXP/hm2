package com.hm.dao;

import com.google.gson.Gson;
import com.hm.entity.User;
import org.bson.Document;

import static com.hm.dao.db.ConnectionHandler.db;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class UserDAO {

	private static final String users = CollectionNames.user.toString();
	private static Gson gson = new Gson();

	public static void createUser(User u) {
		db().getCollection("users").insertOne(Document.parse(gson.toJson(u)));
	}

	public static User getUser (String login, String pass) {
		System.out.println(gson.toJson(db().getCollection(users).find(and(eq("mail", login), eq("pass", pass))).first()));
		try {
			return gson.fromJson(db().getCollection(users).find(and(eq("mail", login), eq("pass", pass))).first().toJson(), User.class);
		} catch (Exception e) {
			try {
				return gson.fromJson(db().getCollection(users).find(and(eq("phone", login), eq("pass", pass))).first().toJson(), User.class);
			} catch (Exception e2) {
				return null;
			}
		}
	}


}
