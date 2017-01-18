package com.hm.model;

import com.hm.AppLoader;
import com.hm.entity.AuthToken;
import com.hm.entity.User;
import com.hm.repo.*;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AuthController {

	@Autowired
	private AuthRepository authRepo;
	@Autowired
	private ModeratorRepository moderatorRepo;
	@Autowired
	private WorkerRepository workerRepo;
	@Autowired
	private ClientRepository clientRepo;
	@Autowired
	private UserRepository userRepo;

	public AuthController() {
	}

	private static Map<String, AuthToken> cachedTokens = new HashMap<>();
	private static Map<String, Thread> queuedQueries = new HashMap<>();

	public AuthToken auth (String login, String pass) {
		User user = userRepo.findByMailAndPass(login, pass);
		if (user == null) {
			return null;
		}
		val ret = new AuthToken(user);
		cachedTokens.put(ret.getSessionID(), ret);
		Thread entityLookup = null;
		switch (user.getEntityClassName().toLowerCase()) {
			case "moderator":
				entityLookup = new Thread(() -> {
					User u = moderatorRepo.findOne(user.getId());
					if (u != null) {
						cachedTokens.get(ret.getSessionID()).setUser(u);
					}
				});
				break;
			case "client":
				entityLookup = new Thread(() -> {
					User u = clientRepo.findOne(user.getId());
					if (u != null) {
						cachedTokens.get(ret.getSessionID()).setUser(u);
					}
				});
				break;
			case "worker":
				entityLookup = new Thread(() -> {
					User u = workerRepo.findOne(user.getId());
					if (u != null) {
						cachedTokens.get(ret.getSessionID()).setUser(u);
					}
				});
				break;
			default:
				entityLookup = new Thread();
		}
		entityLookup.start();
		queuedQueries.put(user.getMail(), entityLookup);
		return ret;
	}

	public User getUser(@NotNull String token) {
		AuthToken au = get(token);
		if (au.getTimeout() > System.currentTimeMillis()) {
			return au.getUser();
		} else {
			cleanup();
			return null;
		}
	}

	private HashMap<Class, MongoRepository> repos = new HashMap<>();

	public <T extends User> T getLoggedToken (String sessionId, Class<T> clazz) {
		User user = cachedTokens.get(sessionId).getUser();
		if (clazz.isInstance(user)) {
			return clazz.cast(user);
		} else if (user != null) {
			if (user.getEntityClassName().equals(clazz.getSimpleName())) {
				try {
					queuedQueries.get(user.getMail()).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return clazz.cast(cachedTokens.get(sessionId).getUser());
			}
		}
		return null;
	}

	public <T extends User> T getEntity (String id, T c) { //TODO delete/make private after tests
		MongoRepository repo = repos.get(c.getClass());
		if (repo == null) {
			repo = (MongoRepository) AppLoader.ctx.getBean(c.getClass().getSimpleName().toLowerCase() + "Repository");
			repos.put(c.getClass(), repo);
		}
		try {
			Object o = repo.findOne(id);
			return (T) o;
		} catch (Exception e) {
			return null;
		}
	}

	public <T extends User> T getEntity (String id, Class<T> clazz) {
		MongoRepository repo = repos.get(clazz);
		if (repo == null) {
			repo = (MongoRepository) AppLoader.ctx.getBean(clazz.getSimpleName().toLowerCase() + "Repository");
			repos.put(clazz, repo);
		}
		try {
			Object o = repo.findOne(id);
			return (T) o;
		} catch (Exception e) {
			return null;
		}
	}

	public List<AuthToken> cleanup () {
		List<AuthToken> list = new ArrayList<>();
		authRepo.findByTimeoutLessThan(System.currentTimeMillis()).forEach(e -> {
			delete(e.getSessionID());
			list.add(e);
		});
		return list;
	}

	public AuthToken get (@NotNull String token) {
		AuthToken ret = cachedTokens.get(token);
		if (ret != null) {
			return ret;
		}
		ret = authRepo.findOne(token);
		return ret; //can be null!
	}

	public void add (AuthToken token) {
		authRepo.save(token);
		cachedTokens.put(token.getSessionID(), token);
	}

	public void delete (String token) {
		cachedTokens.remove(token);
		authRepo.delete(token);
	}

}
