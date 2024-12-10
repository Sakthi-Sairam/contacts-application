package com.managers;

import com.models.Session;
import com.models.User;
import com.Dao.SessionDao;
import com.Dao.userDao;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SessionManager {
	private static int MAX_HASHMAP_SIZE = 5;
    public static final Map<String, Session> sessionMap = Collections.synchronizedMap( new LinkedHashMap<>(5,0.75f,true){
    	private static final long serialVersionUID = 1L;

		@Override
        protected boolean removeEldestEntry(Map.Entry<String, Session> eldest)
        {
            return size() > MAX_HASHMAP_SIZE;
        }
    });
    
    public static final HashMap<Integer, User> userMap = new LinkedHashMap<>(5,0.75f,true){
    	private static final long serialVersionUID = 1L;

		@Override
        protected boolean removeEldestEntry(Map.Entry<Integer, User> eldest)
        {
            return size() > MAX_HASHMAP_SIZE;
        }
    };
    
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private static final List<Session> sessionsToUpdate = new ArrayList<>();
    private static final int TIMEOUT_MINUTES = 5; 


    public static void startScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            try {
            	System.out.println("before delete "+sessionMap);
                cleanExpiredSessions(TIMEOUT_MINUTES);
                cleanExpiredSessionsFromDB(TIMEOUT_MINUTES);
                updateSessionsInDB();
//                sessionMap.clear();
                userMap.clear();
                System.out.println("after delete "+sessionMap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 5, 30, TimeUnit.SECONDS);
    }

    // Get the session based on sessionId from the map
    public static Session getSession(String sessionId) {
        Session result =  sessionMap.get(sessionId);
        if(result == null) {
        	result = SessionDao.getSession(sessionId);
        	if(result != null) sessionMap.put(sessionId, result);
        	
        }
        return result;
    }

    // Add or update the session in the sessionMap and database
    public static void updateSession(String sessionId, int userId) {
        Session session = sessionMap.get(sessionId);

        if (session != null) {
            // If session exists, update lastAccessedTime
            session.setLastAccessedTime(System.currentTimeMillis());
        } else {
            // If session does not exist, create a new one
            session = new Session(userId);
            sessionMap.put(sessionId, session); // Add new session to the map
        }

        // Add to list for DB update
        sessionsToUpdate.add(session);
    }

    
    public static void addSession(Session session) {
    	int userId = session.getUserId();
        sessionMap.put(session.getSessionId(), session);
        userMap.putIfAbsent(userId,userDao.getUserById(userId));
    }

    
    public static void removeSession(String sessionId) {
    	int userId = sessionMap.get(sessionId).getUserId();
        sessionMap.remove(sessionId);
        userMap.remove(userId);
        System.out.println(userId+" removed");
    }

    // Clean up sessions that have been inactive for more than 30 minutes
    public static void cleanExpiredSessions(int TIMEOUT_MINUTES) {
        long now = System.currentTimeMillis();

        sessionMap.forEach((sessionId, session) -> {
            if (session.getLastAccessedTime()+(TIMEOUT_MINUTES*60000)<now) {
            	System.out.println(sessionId+" removed");
                sessionMap.remove(sessionId); // Remove from session map
                try {
                    SessionDao.deleteSessionById(sessionId); // Clean from DB
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public static void cleanExpiredSessionsFromDB(int TIMEOUT_MINUTES) throws SQLException {
        long now = System.currentTimeMillis();
        SessionDao.deleteExpiredSessions(now - TIMEOUT_MINUTES*60000 );
    }

    // Batch update sessions in the database
    private static void updateSessionsInDB() {
        if (!sessionsToUpdate.isEmpty()) {
            try {
                SessionDao.batchUpdateSessions(new ArrayList<>(sessionsToUpdate));
                for(Session s : sessionsToUpdate) sessionMap.remove(s.getSessionId());
                sessionsToUpdate.clear(); 
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void initialize() {
        // Initialize session-related resources here
        System.out.println("SessionManager initialized");
    }

    public static void shutdown() {
        // Clean up resources here
        scheduler.shutdown(); // stop the scheduler
        System.out.println("SessionManager shutdown complete");
    }

	public static User getUser(int userId) {
		return userMap.get(userId);
	}
}
