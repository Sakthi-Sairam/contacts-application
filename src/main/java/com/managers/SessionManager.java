package com.managers;

import com.models.Session;
import com.models.User;
import com.dao.SessionDao;
import com.dao.UserDao;
import com.exceptions.DaoException;

import java.sql.SQLException;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionManager {
    private static final Logger LOGGER = Logger.getLogger(SessionManager.class.getName());
    
    public static final int TIMEOUT_MINUTES = 30;
    private static final int MAX_HASHMAP_SIZE = 5;

    // Thread-safe synchronized map for sessions
    public static final Map<String, Session> sessionMap = Collections.synchronizedMap(new LinkedHashMap<>(5, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<String, Session> eldest) {
            return size() > MAX_HASHMAP_SIZE;
        }
    });
    
    // Thread-safe map for users
    public static final Map<Integer, User> userMap = Collections.synchronizedMap(new LinkedHashMap<>(5, 0.75f, true) {
        private static final long serialVersionUID = 1L;

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, User> eldest) {
            return size() > MAX_HASHMAP_SIZE;
        }
    });
    
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final List<Session> sessionsToUpdate = Collections.synchronizedList(new ArrayList<>());

    public static void startScheduler() {
        LOGGER.info("Starting session management scheduler");
        scheduler.scheduleAtFixedRate(() -> {
            try {
                LOGGER.fine("Running scheduled session cleanup");
                cleanExpiredSessions(TIMEOUT_MINUTES);
                updateSessionsInDB();
                cleanExpiredSessionsFromDB(TIMEOUT_MINUTES);
                
                synchronized (userMap) {
                    userMap.clear();
                }
                
//                LOGGER.fine("Session cleanup completed");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error during scheduled session cleanup", e);
            }
        }, TIMEOUT_MINUTES, TIMEOUT_MINUTES, TimeUnit.MINUTES);
    }

    public static Session getSession(String sessionId) {
        LOGGER.fine("Retrieving session: " + sessionId);
        Session result = sessionMap.get(sessionId);
        try {
			if (result == null) {
			    result = SessionDao.getSession(sessionId);
			    if (result != null) {
			        LOGGER.info("Session retrieved from database: " + sessionId);
			        sessionMap.put(sessionId, result);
			    }
			}
			return result;
		} catch (DaoException e) {
			e.printStackTrace();
			return null;
		}
    }

    public static void updateSession(String sessionId, int userId) {
        LOGGER.fine("Updating session: " + sessionId + " for user: " + userId);
        Session session = sessionMap.get(sessionId);

        if (session != null) {
            // Update last accessed time
            session.setLastAccessedTime(System.currentTimeMillis());
        } else {
            // Create new session
            session = new Session(userId);
            sessionMap.put(sessionId, session);
//            LOGGER.info("New session created: " + sessionId);
        }

        // Add to list for DB update
        sessionsToUpdate.add(session);
    }

    public static void addSession(Session session) {
        int userId = session.getUserId();
//        LOGGER.info("Adding session for user: " + userId);
        sessionMap.put(session.getSessionId(), session);
        try {
			userMap.putIfAbsent(userId, UserDao.getUserById(userId));
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public static void removeSession(String sessionId) {
        Session session = sessionMap.get(sessionId);
        if (session != null) {
            int userId = session.getUserId();
            sessionMap.remove(sessionId);
            userMap.remove(userId);
//            LOGGER.info("Session removed for user: " + userId + ", session: " + sessionId);
        }
    }

    public static void cleanExpiredSessions(int timeoutMinutes) {
        long now = System.currentTimeMillis();
//        LOGGER.fine("Cleaning expired sessions");

        synchronized (sessionMap) {
            Iterator<Map.Entry<String, Session>> iterator = sessionMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Session> entry = iterator.next();
                Session session = entry.getValue();
                
                if (session.getLastAccessedTime() + (timeoutMinutes * 60000L) < now) {
                    String sessionId = entry.getKey();
                    LOGGER.info("Removing expired session: " + sessionId);
                    iterator.remove();
                    
                    try {
                        SessionDao.deleteSessionById(sessionId);
                    } catch (Exception e) {
                        LOGGER.log(Level.WARNING, "Error deleting expired session from DB", e);
                    }
                }
            }
        }
    }

    public static void cleanExpiredSessionsFromDB(int timeoutMinutes) throws DaoException {
        long expirationTime = System.currentTimeMillis() - (timeoutMinutes * 60000L);
//        LOGGER.fine("Cleaning expired sessions from database");
        SessionDao.deleteExpiredSessions(expirationTime);
    }

    public static void updateSessionsInDB() {
    	System.out.println(sessionsToUpdate);
        if (!sessionsToUpdate.isEmpty()) {
            try {
//                LOGGER.info("Batch updating " + sessionsToUpdate.size() + " sessions");
                SessionDao.batchUpdateSessions(new ArrayList<>(sessionsToUpdate));
                
                // Remove processed sessions from map
                synchronized (sessionMap) {
                    sessionsToUpdate.forEach(session -> sessionMap.remove(session.getSessionId()));
                }
                
                sessionsToUpdate.clear();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error batch updating sessions", e);
            }
        }
    }

    public static void initialize() {
        LOGGER.info("SessionManager initialized");
    }

    public static void shutdown() {
        LOGGER.info("Shutting down SessionManager");
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        LOGGER.info("SessionManager shutdown complete");
    } 

    public static User getUser(int userId) {
        return userMap.get(userId);
    }
    public static void setUser(User user) {
    	userMap.put(user.getUserId(), user);
    }
}