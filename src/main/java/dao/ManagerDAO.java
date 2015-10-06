package dao;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.Manager;
import domain.util.LogConfig;

public class ManagerDAO {
	
	private static Logger log = Logger.getLogger(ManagerDAO.class);
	
	private String title = "Manager";
	
	public ManagerDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	public Manager createManager(Manager manager) throws PersistException{
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(manager);
		transaction.commit();
		Manager createdManager = getManagerById(id);
		session.close();
		
		return createdManager;
	}
	
	
	public Manager getManagerById(Integer id) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Manager manager = session.get(Manager.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + manager.getId());
 
		return manager;
	}
	
	
	
	public Manager getManagerByLogin(String login) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery() + " where m.login=:login";
		Query query = session.createQuery(selectQuery).setString("login", login);
		
		@SuppressWarnings("unchecked")
		List<Manager> list = query.list();
		session.close();
		if (list.size() != 1) {
			log.error(list.size() + title + " persists with login " + login);
			throw new PersistException(list.size() + " persists with login " + login);		
		}
		log.info("get " + title + " with login " + list.get(0));
		
		return list.get(0);
	}
	
	
	public void updateManager(Manager manager) throws PersistException {
		
		boolean managerPersisted = isManagerPersisted(manager);
		
		if (managerPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			String updateHQL = getUpdateQuery();
			Query query = session.createQuery(updateHQL);
			prepareInsertData(query, manager);
			try {
				int count = query.executeUpdate();
				if (1 != count) {
					throw new PersistException("updated " + count + " " + title + " records");
				}
			} finally {
				t.commit();
				session.close();
			}
			log.info("update " + title + " with id " + manager.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}
	
	
	public void deleteManager(Manager manager) throws PersistException {
		
		boolean managerPersisted = isManagerPersisted(manager);
		
		if (managerPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(manager);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + manager.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}
	
	
	public List<Manager> getAllManagers() throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Manager> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}

	
	private String getUpdateQuery() {
		return "update Manager m set m.login=:login, m.password=:password";
	}
	
	
	private String getSelectQuery() {
		String sql = "from Manager m";
		return sql;
	}

	
	private boolean isManagerPersisted(Manager manager) throws PersistException {
		
		if (manager.getId() == null) {
			return false;
		}	
		Manager persistedManager = getManagerById(manager.getId());
		if (persistedManager == null) {
			return false;
		}
		
		return true;
	}
	
	
	private void prepareInsertData(Query query, Manager manager) {

		query.setParameter("login", manager.getLogin());
		query.setParameter("password", manager.getPassword());
		query.setParameter("id", manager.getId());
	}
	

}
