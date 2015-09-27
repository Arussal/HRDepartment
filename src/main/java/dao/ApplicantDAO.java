package dao;

import java.util.List;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.Applicant;

public class ApplicantDAO {


	public ApplicantDAO(Properties properties) {
		//LogConfig.loadLogConfig(properties);
	}

		
	public Applicant createApplicant(Applicant applicant) throws PersistException{
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(applicant);
		transaction.commit();
		Applicant createdApplicant = getApplicantByID(id);
		session.close();
		
		return createdApplicant;
	}
		
	
	public Applicant getApplicantByID(Integer id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Applicant applicant = session.get(Applicant.class, new Integer(id));
		session.close();
		
		return applicant;
	}
	
	public Applicant getApplicantByLogin(String login) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("FROM Applicant a where a.login =:login")
				.setString("login", login);
		
		@SuppressWarnings("unchecked")
		List<Applicant> list = query.list();
		if (list.size() != 1) {
			throw new PersistException(list.size() + " persists with login " + login);
		}
		return list.get(0);
	}
	
	
	public void updateApplicant(Applicant applicant) throws PersistException {
		
		if (applicant.getId() == null) {
			throw new PersistException("applicant not persisted yet");
		}
		
		Applicant persistedApplicant = getApplicantByID(applicant.getId());
		if (persistedApplicant == null) {
			throw new PersistException("no applicant with " + applicant.getId() + " in db");
		}
				
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction t = session.beginTransaction();
		//String updateHQL = "update Applicant a set password=:password where id=:id";
		Query query = session.createQuery("update Applicant a set a.password='newPassword' where a.id='1'");
		//query.setParameter("password", "newPassword");
		//query.setParameter("id", 1);
		//preparedApplicantData(query, applicant);
		//System.out.println(updateHQL); 							/////
		try {
			int count = query.executeUpdate();
			if (1 != count) {
				throw new PersistException("updated " + count + " applicant records");
			}
		} finally {
			t.commit();
			session.close();
		}
	}
	
	private void preparedApplicantData(Query query, Applicant applicant) {

		query.setParameter("password", applicant.getPassword());
		query.setParameter("id", applicant.getId());
	}
	
	private String updateQuery() {
		return "update Applicant set password=:password";
	}
		
//      check if there Manager persist
//		if (null == applicant) {
//			throw new IllegalArgumentException();
//		}
//		if (null == applicant.getId()) {
//			log.warn("Applicant with id " + applicant.getId() + " is not persist");
//			throw new PersistException("Applicant does not persist yet");
//		}
//		Applicant selectedApplicant = this.getApplicantByLogin(applicant.getLogin());
//		if (null == selectedApplicant) {
//			log.warn("Applicant with id " + applicant.getId() + " is not persist");
//			throw new PersistException("Applicant does not persist yet");
//		}	
//		
//		Connection connection = null;
//		PreparedStatement pStatement = null;
//		String updateSql = getUpdateQuery() + " WHERE login = " + "'" + applicant.getLogin() + "'";
//		
//		try {
//			connection = daoFactory.createConnection();
//			pStatement = connection.prepareStatement(updateSql);
//			preparedStatementForInsert(applicant, pStatement);
//			int count = pStatement.executeUpdate();
//			if (1 != count) {
//				log.error("Updated " + count + " Applicant entries");
//				throw new PersistException("Updated " + count + " Applicant entries");
//			}			
//		} catch (SQLException e) {
//			log.error("can't update Applicant entry");
//			throw new PersistException("can't update Applicant entry");
//		} finally {
//			if (null != pStatement) {
//				try {
//					pStatement.close();						
//				} catch (SQLException se) {
//					se.printStackTrace();
//				}
//			}
//			if (null != connection) {
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
	
	
	
	public void deleteApplicant(Applicant applicant) throws PersistException {
		
//		//check if there Manager persist
//		if (null == applicant) {
//			throw new IllegalArgumentException();
//		}
//		if (null == applicant.getId()) {
//			log.warn("Applicant with id " + applicant.getId() + " is not persist");
//			throw new PersistException("Applicant does not persist yet");
//		}
//		Applicant selectedManager = this.getApplicantByLogin(applicant.getLogin());
//		if (null == selectedManager) {
//			log.warn("Applicant with id " + applicant.getId() + " is not persist");
//			throw new PersistException("Applicant does not persist yet");
//		}
//		
//		Connection connection = null;
//		Statement statement = null;
//		String deleteSql = getDeleteQuery() + " WHERE login = '" + applicant.getLogin() + "'";
//		
//		try { 							
//			connection = daoFactory.createConnection();
//			statement = connection.createStatement();
//			int count = statement.executeUpdate(deleteSql);
//			if (1 != count) {
//				log.error("Deleted " + count + " Applicant entries");
//				throw new PersistException("Deleted " + count + " Applicant entries");
//			}			
//		} catch (SQLException e) {
//			log.error("can't delete Applicant entry");
//			throw new PersistException("can't delete Applicant entry");
//		} finally {
//			if (null != statement) {
//				try {
//					statement.close();						
//				} catch (SQLException se) {
//					se.printStackTrace();
//				}
//			}
//			if (null != connection) {
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}
	
	
	public List<Applicant> getAllApplicants() throws PersistException {
		
//		Connection connection = null;
//		Statement statement = null;
//		ResultSet rs = null;
//		List<Applicant> applicants = new ArrayList<Applicant>();
//		
//		try {
//			String selectSql = getSelectQuery();
//			connection = daoFactory.createConnection();
//			statement = connection.createStatement();
//			rs = statement.executeQuery(selectSql);
//			List<Applicant> parsedList = parseResultSet(rs);
//			if (parsedList != null) {
//				applicants = parsedList;
//			}
//			if (log.isTraceEnabled()) {
//				log.trace("get all applicants");
//			}
//		} catch (SQLException e) {
//			log.error("cant get all applicants");
//			throw new PersistException();
//		} finally {
//			if (null != rs) {
//				try {
//					rs.close();
//				} catch (SQLException se) {
//					se.printStackTrace();
//				}
//			}
//			if (null != statement) {
//				try {
//					statement.close();						
//				} catch (SQLException se) {
//					se.printStackTrace();
//				}
//			}
//			if (null != connection) {
//				try {
//					connection.close();
//				} catch (SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}		
//		return applicants;
		return null;
	}


	
	


	
	


}