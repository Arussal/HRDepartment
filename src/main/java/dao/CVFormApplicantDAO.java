/**
 * 
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.CVFormApplicant;
import domain.Candidate;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CVFormApplicantDAO {

	private static Logger log = Logger.getLogger(CVFormApplicantDAO.class);
	
	private String title = "CVFormApplicant";
	
	public CVFormApplicantDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}

	public CVFormApplicant createCVForm(CVFormApplicant cv) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(cv);
		transaction.commit();
		CVFormApplicant createdCV = getCVFormById(id);
		session.close();
		
		return createdCV;
	}

	
	public CVFormApplicant getCVFormById(int id) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		CVFormApplicant cv = session.get(CVFormApplicant.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + cv.getId());
 
		return cv;
	}

	
	public List<CVFormApplicant> getCVFormByPost(String post) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		Criterion crPost = Restrictions.eq("post", post);
		crit.add(crPost);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + post + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormApplicant> getCVFormByWorkExpirience(int workExpirience) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		Criterion crExpirience = Restrictions.eq("workExpirience", workExpirience);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience " + workExpirience + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormApplicant> getCVFormByEducation(String education)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		Criterion crExpirience = Restrictions.eq("education", education);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with education " + education + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormApplicant> geyCVFormByDesiredSalary(int desiredSalary)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		Criterion crExpirience = Restrictions.eq("desiredSalary", desiredSalary);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with desiredSalary " + desiredSalary + ", amount = " + list.size());
 
		return list;
	}

	
	public List<CVFormApplicant> getCVForm(Map<String, List<String>> queries) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		List<CVFormApplicant> cvForms = new ArrayList<CVFormApplicant>();
		
		try {
			log.trace("get CVForms with different query parameters");
			StringBuilder selectBuilder = new StringBuilder();
			String selectSql = "";
			String selectPhrase = getSelectQuery();
			selectBuilder.append(selectPhrase);
			if (queries.size() != 0) {
				selectBuilder.append(" WHERE ");
				for (String key : queries.keySet()) {
					selectBuilder.append(key);
					if (queries.get(key).get(0) == null) {
						selectBuilder.append(" is null");
						selectSql = selectBuilder.toString();
					} else {
						selectBuilder.append(queries.get(key).get(1)+"'");
						selectBuilder.append(queries.get(key).get(0));
						selectBuilder.append("'");
						selectBuilder.append(" AND ");
						selectSql = selectBuilder.substring(0, selectBuilder.length()-5);
					}
				}
			} else {
				selectSql = selectBuilder.toString();
			}
			
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(selectSql);
			log.trace("resultset got");
			cvForms = parseResultSet(rs);
			if (cvForms.size() < 1) {
				log.warn("no CVForms with different query parameters");
			}
		} catch (SQLException e) {
			log.error("can't get CVForms with different query parameters");
			throw new PersistException();
		} finally {
			if (null != rs) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (null != statement) {
				try {
					statement.close();						
				} catch (SQLException se) {
					se.printStackTrace();
				}
			}
			if (null != connection) {
				try {
					connection.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
		return cvForms;
	}

	
	public void updateCVForm(CVFormApplicant cv) throws PersistException {

		boolean candidatePersisted = isCVFormPersisted(cv);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			String updateHQL = getUpdateQuery();
			Query query = session.createQuery(updateHQL);
			prepareInsertData(query, cv);
			try {
				int count = query.executeUpdate();
				if (1 != count) {
					throw new PersistException("updated " + count + " " + title + " records");
				}
			} finally {
				t.commit();
				session.close();
			}
			log.info("update " + title + " with id " + cv.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
		
	}


	public void deleteCVForm(CVFormApplicant cv) throws PersistException {
				
		boolean CVFormPersisted = isCVFormPersisted(cv);
		
		if (CVFormPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(cv);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + cv.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}


	public List<CVFormApplicant> getAllCVForms() throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}

	
	private String getUpdateQuery() {
		return "update CVFormApplicant cv set cv.name=:name, cv.age=:age, cv.education=:education, \n"
				+ "cv.email=:email, cv.phone=:phone, cv.workExpirience=:workExpirience, \n"
				+ "cv.skills=:skills, cv.desiredSalary=:desiredSalary, \n"
				+ "cv.additionalInfo=:additionalInfo, cv.sendStatus=:sendStatus where cv.id=:id";
	}
	
	
	private String getSelectQuery() {
		String sql = "from CVFormApplicant cv";
		return sql;
	}
	
	
	private boolean isCVFormPersisted(CVFormApplicant cv) throws PersistException {
		
		if (cv.getId() == null) {
			return false;
		}	
		CVFormApplicant persistedCVFormApplicant = getCVFormById(cv.getId());
		if (persistedCVFormApplicant == null) {
			return false;
		}
		
		return true;
	}
	
	
	private void prepareInsertData(Query query, CVFormApplicant cv) {

		query.setParameter("name", cv.getName());
		query.setParameter("age", cv.getAge());
		query.setParameter("education", cv.getEducation());
		query.setParameter("email", cv.getEmail());
		query.setParameter("phone", cv.getPhone());
		query.setParameter("skills", cv.getSkills());
		query.setParameter("workExpirience", cv.getWorkExpirience());
		query.setParameter("desiredSalary", cv.getDesiredSalary());
		query.setParameter("additionalInfo", cv.getAdditionalInfo());
		query.setParameter("sendStatus", cv.getSendStatus());
		query.setParameter("id", cv.getId());
	}

}
