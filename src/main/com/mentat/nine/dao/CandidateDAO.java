/**
 * 
 */
package main.com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import main.com.mentat.nine.dao.exceptions.PersistException;
import main.com.mentat.nine.dao.util.Closer;
import main.com.mentat.nine.dao.util.DAOFactory;
import main.com.mentat.nine.domain.Candidate;
import main.com.mentat.nine.domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CandidateDAO {
	
	static {
		LogConfig.loadLogConfig();
	}
	private static Logger log = Logger.getLogger(CandidateDAO.class);
	
	private DAOFactory daoFactory = null;
	
	public CandidateDAO() throws PersistException{
		daoFactory = DAOFactory.getFactory();
	}

	
	public Candidate createCandidate(Candidate candidate) throws PersistException {
		
		Candidate createdCandidate = null;
		Connection connection = null;
		Statement statement = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		int id = 0;

		try {
			
			//check if this Candidate does not persist
			try {
				
				if(log.isTraceEnabled()) {
					log.trace("try to check if Candidate with id " + candidate.getId() + " exists");
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + candidate.getId();
				connection = daoFactory.createConnection();
				log.trace("connection created");
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resultset got");
				Set<Candidate> candidates = parseResultSet(rs);
				if (candidates.size() != 0) {
					log.warn("Candidate is already persist, id " + candidate.getId());
					throw new PersistException("Candidate is already persist, id " + candidate.getId());
				} 
				if(log.isTraceEnabled()){
					log.trace("Candidate with id " + id + " is absent");
				}
			}catch (SQLException e) {
				log.error(" can't check Candidate by id");
				throw new PersistException(" can't check Candidate with id " + candidate.getId());
			}finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new Candidate persist
			try {
				log.trace("try to create new entity Candidate");
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				log.trace("pStatement created");
				prepareStatementForInsert(pStatement, candidate);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					log.trace("generated pStatement keys");
					rs.next();
					id = rs.getInt("id"); 
				} else {
					log.error("new entity Candidate not created");
					throw new PersistException("Candidate hasn't been created");
				}
				log.info("new entity Candidate created, id " + id);
			}catch (SQLException e) {
				log.error("new entity Candidate not created");
				throw new PersistException(" can't create Candidate with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				if(log.isTraceEnabled()) {
					log.trace("try to get Candidate entity with id " + id);
				}
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				log.trace("statement created");
				rs = statement.executeQuery(sqlSelect);
				log.trace("resulset got");
				Set<Candidate> candidates = parseResultSet(rs);
				if (null == candidates || candidates.size() != 1) {
					log.warn("more than one Candidate with id " + id);
					throw new PersistException("Created more than one Candidate with id = " + id);
				}
				for (Candidate cand : candidates) {
					createdCandidate = cand;	
				}
				log.info("return new Candidate with id " + id);
				createdCandidate.setId(id);
			} catch (SQLException e) {
				log.error(" can't return new Candidate with id " + id);
				throw new PersistException(" can't return new Candidate with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
		} finally {
			Closer.closeConnection(connection);
		}
		
		return createdCandidate;
	}

	
	public Candidate getCandidateById(int id) throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		Set<Candidate> candidates = null;
		
		Candidate candidate = new Candidate();

		try {
			log.trace("get Candidate with id " + id);
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("resultset got");
			candidates = parseResultSet(rs);
			if (candidates.size() > 1) {
				log.warn("more than one Candidate with id " + id);
				throw new PersistException("Get more than one Candidate with id = " + id);
			} else if (candidates.size() < 1) {
				log.warn("no Candidate with id " + id);
				throw new PersistException("No Candidate with id = " + id);
			}
			for (Candidate cand : candidates) {
				candidate = cand;	
			}
			if (log.isTraceEnabled()) {
				log.trace("get Candidate with id " + id);
			}
		} catch (SQLException e) {
			log.error("can't get Candidate with id " + id);
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
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
			Closer.close(rs, statement, connection);
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
			Closer.close(rs, statement, connection);
		}
		
		return candidates;
	}

	
	public void updateCandidate(Candidate candidate) throws PersistException {
		
		Connection connection = null;
		PreparedStatement pStatement = null;
		ResultSet rs = null;
		
		// check if there is CVForm entity
		if (null == candidate) {
			throw new IllegalArgumentException();
		}
		if (null == candidate.getId()) {
			log.warn("Candidate with id " + candidate.getId() + " is not persist");
			throw new PersistException("Candidate does not persist yet");
		}
		Candidate selectedCandidate = this.getCandidateById(candidate.getId());
		if (null == selectedCandidate) {
			log.warn("Candidate with id " + candidate.getId() + " is not persist");
			throw new PersistException("Candidate does not persist yet");
		}	
		
		// update Candidate entity
		String sqlUpdate = getUpdateQuery() + " WHERE id = " + candidate.getId();
		try {
			if(log.isTraceEnabled()) {
				log.trace("try to update Candidate with id " + candidate.getId()); 
			}
			connection = daoFactory.createConnection();
			log.trace("create connection");
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			log.trace("create pStatement");
			prepareStatementForInsert(pStatement, candidate);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				log.warn("update more then one Candidate");
				throw new PersistException("Updated more than one Candidate: " + count);
			} else if (count < 1) {
				log.warn("no Candidate update");
				throw new PersistException("No one Candidate was updated");
			}
		} catch (SQLException e) {
			log.error("can't update Candidate");
			throw new PersistException();
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}
	

	public void deleteCandidate(Candidate candidate) throws PersistException {
	
		Connection connection = null;
		Statement statement = null;
		
		// check if there is CVForm entity
		if (null == candidate) {
			throw new IllegalArgumentException();
		}
		if (null == candidate.getId()) {
			log.warn("Candidate with id " + candidate.getId() + " is not persist");
			throw new PersistException("Candidate does not persist yet");
		}
		Candidate selectedCandidate = this.getCandidateById(candidate.getId());
		if (null == selectedCandidate) {
			log.warn("Candidate with id " + candidate.getId() + " is not persist");
			throw new PersistException("Candidate does not persist yet");
		}	
		
		// delete Candidate entity
		String sqlDelete = getDeleteQuery() + " WHERE id = " + candidate.getId();
		try {
			if (log.isTraceEnabled()) {
				log.trace("try to delete Candidate with id " + candidate.getId());
			}
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				log.warn("delete more than one entity");
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
			log.error("can't delete Candidate");
			throw new PersistException();
		} finally {
			Closer.closeStatement(statement);
			Closer.closeConnection(connection);
		}
	}


	public Set<Candidate> getAllCandidates() throws PersistException {
		
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		
		Set<Candidate> candidates = null;
		String sqlSelect = getSelectQuery();
		
		try { 
			log.trace("get all Candidates");
			connection = daoFactory.createConnection();
			log.trace("create connection");
			statement = connection.createStatement();
			log.trace("create statement");
			rs = statement.executeQuery(sqlSelect);
			log.trace("get resultset");
			candidates = parseResultSet(rs);
			if (null == candidates || 0 == candidates.size()) {
				log.warn("no one Candidate persist");
				throw new PersistException(" there are not Candidate entities");
			}
		} catch (SQLException e) {
			log.error("can't get all Candidates");
			throw new PersistException();
		} finally {
			Closer.close(rs, statement, connection);
		}
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
			Closer.close(rs, statement, connection);
		}
		
		return candidates;
		
	}


	private String getCreateQuery() {
		String sql = "INSERT INTO candidate (name, age, education, email, phone, \n"
				+ "post, skills, work_expirience) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		return sql;
	}


	private String getUpdateQuery() {
		String sql = "UPDATE candidate SET name = ?, age = ?, education = ?, \n"
				+ "email = ?, phone = ?, skills = ?, work_expirience = ?";
		return sql;
	}
	

	private String getSelectQuery() {
		String sql = "SELECT * FROM candidate";
		return sql;
	}
	
	
	private String getDeleteQuery() {
		String sql = "DELETE FROM candidate";
		return sql;
	}
	
	private Set<Candidate> parseResultSet(ResultSet rs) throws PersistException {
		Set<Candidate> candidates = new HashSet<Candidate>();

		try {
			String skill = "";
			while (rs.next()) {
				Candidate candidate = new Candidate();
				candidate.setId(rs.getInt("id"));
				candidate.setName(rs.getString("name"));
				candidate.setAge(rs.getInt("age"));
				candidate.setEducation(rs.getString("education"));
				candidate.setEmail(rs.getString("email"));
				candidate.setPhone(rs.getString("phone"));
				candidate.setPost(rs.getString("post"));
				skill = rs.getString("skills");
				candidate.setWorkExpirience(rs.getInt("work_expirience"));
				String[] skillArray = skill.split(";");
				Set<String> skills = new HashSet<String>(Arrays.asList(skillArray));
				candidate.setSkills(skills);
				log.trace("parsed Candidate entity");
				candidates.add(candidate);
			}
		} catch (SQLException e) {
			log.error("can't parse query results");
			throw new PersistException();
		}
		return candidates;
	}
	
	private void prepareStatementForInsert(PreparedStatement statement,
			Candidate candidate) throws PersistException {
		String skills = convertString(candidate.getSkills());
		try {
			statement.setString(1, candidate.getName());
			statement.setInt(2, candidate.getAge());
			statement.setString(3, candidate.getEducation());
			statement.setString(4, candidate.getEmail());
			statement.setString(5, candidate.getPhone());
			statement.setString(6, candidate.getPost());
			statement.setString(7, skills);
			statement.setInt(8, candidate.getWorkExpirience());
		} catch (SQLException e) {
			log.error("can't create arguments for pStatement");
			throw new PersistException();
		}	
		log.trace("create arguments for pStatement");
	}
	
	private String convertString(Set<String> set) {
		StringBuilder sb = new StringBuilder();
		for (String s : set) {
			sb.append(s);
			sb.append(";");
		}
		return sb.toString();
	}

}
