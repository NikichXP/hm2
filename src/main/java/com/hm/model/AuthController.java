package com.hm.model;

import com.hm.AppLoader;
import com.hm.entity.AuthToken;
import com.hm.entity.User;
import com.hm.repo.*;
import com.mongodb.DuplicateKeyException;
import lombok.val;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

import static com.hm.manualdb.ConnectionHandler.db;

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

	private HashMap<Class, MongoRepository> repos = new HashMap<>();
	private static Map<String, AuthToken> cachedTokens = new HashMap<>();
	/** Objects classtype given as default is User, this is queries to finalize type */
	private static Map<String, Thread> queuedQueries = new HashMap<>();

	public AuthController() {
	}

	/** creates session, starts lookup of entity in DB */
	public AuthToken auth (String login, String pass) {
		pass = UserUtils.encryptPass(login, pass);
		User user = userRepo.findByMailAndPass(login, pass);
		if (user == null) {
			return null;
		}
		val ret = new AuthToken(user);
		add(ret);
		startEntityLookUp(user, ret);
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

	public Map<String, AuthToken> getCachedTokens() {
		return cachedTokens;
	}

	public <T extends User> T getLoggedToken (String sessionId, Class<T> clazz) {
		val token = get(sessionId);
		if (token == null) {
			return null;
		}
		User user = token.getUser();
		if (clazz.isInstance(user)) {
			return clazz.cast(user);
		} else if (user != null) {
			if (user.getEntityClassName().equals(clazz.getSimpleName())) {
				try {
					queuedQueries.get(user.getMail()).join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				return getLoggedToken(sessionId, clazz);
			}
		}
		return null;
	}

	public <T extends User> T getEntity (String id, T c) { //FIXME Security issue: test-only
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

	public <T extends User> T getEntity (String id, Class<T> clazz) { //FIXME Security issue: test-only
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

	public void cleanup () {
		db().getCollection("authToken")
				.deleteMany(Document.parse("{'timeout': {$lt : "+System.currentTimeMillis()+"}}"));
		cachedTokens.keySet().forEach(key -> {
			if (cachedTokens.get(key).getTimeout() < System.currentTimeMillis()) {
				cachedTokens.remove(key);
			}
		});
	}

	private void startEntityLookUp(User user, AuthToken ret) {
		Thread entityLookup = null;
		switch (ret.getUser().getEntityClassName().toLowerCase()) {
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
	}

	public AuthToken get (@NotNull String token) {
		AuthToken ret = cachedTokens.get(token);
		if (ret != null) {
			return ret;
		}
		ret = authRepo.getToken(token);
		if (ret != null) {
			cachedTokens.put(ret.getSessionID(), ret);
			startEntityLookUp(ret.getUser(), ret);
			try {
				queuedQueries.get(ret.getUser().getMail()).join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return ret; //can be null!
	}

	public void add (AuthToken token) {
		cachedTokens.put(token.getSessionID(), token);
		try {
			authRepo.save(token);
		}catch (DuplicateKeyException ex) { //TODO Delete in release
			System.err.println("Warning! -- duplicate key, fix token generation bug");
		}
	}

	public void delete (String token) {
		cachedTokens.remove(token);
		authRepo.delete(token);
	}

}
