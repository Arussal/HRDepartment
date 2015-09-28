/**
 * 
 */
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.ApplicationForm;
import domain.Candidate;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CandidateDAO {

	private static Logger log = Logger.getLogger(CandidateDAO.class);
	
	private String title = "candidate";
	private Properties properties;
	
	public CandidateDAO(Properties properties) {
		this.properties = properties;
		LogConfig.loadLogConfig(properties);
	}
	
	
	public Candidate createCandidate(Candidate candidate) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(candidate);
		transaction.commit();
		Candidate createdCandidate = getCandidateById(id);
		session.close();
		
		return createdCandidate;
	}

	
	public Candidate getCandidateById(int id) throws PersistException {
				
		Session session = HibernateUtil.getSessionFactory().openSession();
		Candidate candidate = session.get(Candidate.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + candidate.getId());
 
		return candidate;
	}

	
	public Set<Candidate> getCandidatesByPost(String post)
			throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Set<Candidate> candidates = null;
		
		try {
			log.trace("get Candidates with post " + post);
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			candidates = parseResultSet(rs);
			if (candidates.size() < 1) {
				log.warn("no Candidates with post " + post);
				throw new PersistException("No Candidates with post = " + post);
			}
		} catch (SQLException e) {
			log.error("can't get Candidates with post " + post);
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
		
		return candidates;
	}

	
	public Set<Candidate> getCandidatesByWorkExpirience(int workExpirience)
			throws PersistException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Set<Candidate> candidates = null;
		
		try {
			log.trace("get Candidates with workExpirience " + workExpirience);
			String sqlSelect = getSelectQuery() + " WHERE work_expirience = " + workExpirience;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			candidates = parseResultSet(rs);
			if (candidates.size() < 1) {
				log.warn("no Candidates with workExpirience " + workExpirience);
				throw new PersistException("No Candidates with workExpirience = " + workExpirience);
			}
		} catch (SQLException e) {
			log.error("can't get Candidates with workExpirience " + workExpirience);
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
		
		return candidates;
	}

	
	public void updateCandidate(Candidate candidate) throws PersistException {
		
		boolean candidatePersisted = isCandidatePersisted(candidate);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			String updateHQL = getUpdateQuery();
			Query query = session.createQuery(updateHQL);
			prepareInsertData(query, candidate);
			try {
				int count = query.executeUpdate();
				if (1 != count) {
					throw new PersistException("updated " + count + " " + title + " records");
				}
			} finally {
				t.commit();
				session.close();
			}
			log.info("update " + title + " with id " + candidate.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}
	

	public void deleteCandidate(Candidate candidate) throws PersistException {
	
		boolean appFormPersisted = isCandidatePersisted(candidate);
		
		if (appFormPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(candidate);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + candidate.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}


	public Set<Candidate> getAllCandidates() throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Candidate> candidatesList = query.list();
		session.close();
		log.info("get all " + title + "s");
		Set<Candidate> candidates = new HashSet<Candidate>(candidatesList);

		return candidates;
	}
	
	
	public Set<Candidate> getCandidates(Map<String, List<String>> queries) 
			throws PersistException {

		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Set<Candidate> candidates = null;
		
		try {
			log.trace("get Candidates with different query parameters");
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
			candidates = parseResultSet(rs);
			if (candidates.size() < 1) {
				log.warn("no Candidates with different query parameters");
			}
		} catch (SQLException e) {
			log.error("can't get Candidates with different query parameters");
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
		
		return candidates;
		
	}
	

	private String getSelectQuery() {
		return "from Candidate c";
	}
	
	
	private String getUpdateQuery() {
		return "update ApplicationForm af set af.date=:date, af.age=:age, af.education=:education, \n"
				+ "af.requirements=:requirements, af.post=:post, af.salary=:salary, \n"
				+ "af.workExpirience=:workExpirience where af.id=:id";
	}


	private String getUpdateQuery() {
		String sql = "UPDATE candidate SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, skills = ?, work_expirience = ?";
		return sql;
	}
		
	private boolean isCandidatePersisted(Candidate candidate) throws PersistException {
		
		if (candidate.getId() == null) {
			return false;
		}	
		Candidate persistedCandidate = getCandidateById(candidate.getId());
		if (persistedCandidate == null) {
			return false;
		}
		
		return true;
	}
	
	
	private void prepareInsertData(Query query, ApplicationForm appForm) {

		query.setParameter("date", appForm.getDate());
		query.setParameter("age", appForm.getAge());
		query.setParameter("education", appForm.getEducation());
		query.setParameter("requirements", appForm.getRequirements());
		query.setParameter("post", appForm.getPost());
		query.setParameter("salary", appForm.getSalary());
		query.setParameter("workExpirience", appForm.getWorkExpirience());
		query.setParameter("id", appForm.getId());
	}
	
	
}
