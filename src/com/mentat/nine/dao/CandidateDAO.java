/**
 * 
 */
package com.mentat.nine.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;



import com.mentat.nine.dao.exceptions.PersistException;
import com.mentat.nine.dao.util.Closer;
import com.mentat.nine.dao.util.DAOFactory;
import com.mentat.nine.domain.Candidate;

/**
 * @author Ruslan
 *
 */
public class CandidateDAO {
	
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
				String sqlSelect = getSelectQuery() + " WHERE id = " + candidate.getId();
				connection = daoFactory.createConnection();
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				Set<Candidate> candidates = parseResultSet(rs);
				if (candidates.size() != 0) {
					throw new PersistException("Candidate is already persist, id " + candidate.getId());
				} 
			}catch (SQLException e) {
				throw new PersistException(" can't check Candidate with id " + candidate.getId());
			}finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(statement);
			}
			
			// create new Candidate persist
			try {
				String sqlCreate = getCreateQuery();
				pStatement = connection.prepareStatement(sqlCreate, Statement.RETURN_GENERATED_KEYS);
				prepareStatementForInsert(pStatement, candidate);
				int count = pStatement.executeUpdate();
				if (1 == count) {
					rs = pStatement.getGeneratedKeys();
					rs.next();
					id = rs.getInt("id"); 
				} else {
					throw new PersistException("Candidate hasn't been created");
				}
			}catch (SQLException e) {
				throw new PersistException(" can't create Candidate with id " + id);
			} finally {
				Closer.closeResultSet(rs);
				Closer.closeStatement(pStatement);
			}
			
			//return the last entity
			try {
				String sqlSelect = getSelectQuery() + " WHERE id = " + id;
				statement = connection.createStatement();
				rs = statement.executeQuery(sqlSelect);
				Set<Candidate> candidates = parseResultSet(rs);
				if (null == candidates || candidates.size() != 1) {
					throw new PersistException("Created more than one Candidate with id = " + id);
				}
				for (Candidate cand : candidates) {
					createdCandidate = cand;	
				}
				createdCandidate.setId(id);
			}catch (SQLException e) {
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
			String sqlSelect = getSelectQuery() + " WHERE id = " + id;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			candidates = parseResultSet(rs);
			if (candidates.size() > 1) {
				throw new PersistException("Get more than one Candidate with id = " + id);
			} else if (candidates.size() < 1) {
				throw new PersistException("No Candidate with id = " + id);
			}
			for (Candidate cand : candidates) {
				candidate = cand;	
			}
		} catch (SQLException e) {
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
			String sqlSelect = getSelectQuery() + " WHERE post = " + post;
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			candidates = parseResultSet(rs);
			if (candidates.size() < 1) {
				throw new PersistException("No Candidates with post = " + post);
			}
		} catch (SQLException e) {
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
		
		String sqlSelect = getSelectQuery() + "WHERE work_expirience = " + workExpirience;
		
		try {
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			candidates = parseResultSet(rs);
			if (candidates.size() < 1) {
				throw new PersistException("No Candidates with workExpirience = " + workExpirience);
			}
		} catch (SQLException e) {
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
		
		// check if there is Candidate entity
		if (null == candidate.getId()) {
			throw new PersistException("Candidate does not persist yet");
		}
		
		// update Candidate entity
		String sqlUpdate = getUpdateQuery() + "WHERE id = " + candidate.getId();
		try {
			connection = daoFactory.createConnection();
			pStatement = connection.prepareStatement(sqlUpdate, Statement.RETURN_GENERATED_KEYS);
			prepareStatementForInsert(pStatement, candidate);
			int count = pStatement.executeUpdate();
			if (count > 1) {
				throw new PersistException("Updated more than one Candidate");
			} else if (count < 1) {
				throw new PersistException("No one Candidate was updated");
			}
		} catch (SQLException e) {
			throw new PersistException();
		} finally {
			Closer.close(rs, pStatement, connection);
		}
	}
	

	public void deleteCandidate(Candidate candidate) throws PersistException {
	
		Connection connection = null;
		Statement statement = null;
		
		// check if there is Candidate entity
		if (null == candidate.getId()) {
			throw new PersistException("Candidate does not persist");
		}
		
		// delete Candidate entity
		String sqlDelete = getDeleteQuery() + " WHERE id = " + candidate.getId();
		try {
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			int count = statement.executeUpdate(sqlDelete);
			if (1 != count) {
				throw new PersistException("On delete modify more then 1 record: " + count);
			}
		} catch (SQLException e) {
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
			connection = daoFactory.createConnection();
			statement = connection.createStatement();
			rs = statement.executeQuery(sqlSelect);
			candidates = parseResultSet(rs);
			if (null == candidates || 0 == candidates.size()) {
				throw new PersistException(" there are not Candidate entities");
			}
		} catch (SQLException e) {
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
				candidates.add(candidate);
			}
		} catch (SQLException e) {
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
			throw new PersistException();
		}	
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
