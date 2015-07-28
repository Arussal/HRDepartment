/**
 * 
 */
package com.mentat.nine.dao.postgres;

import java.util.Set;

import com.mentat.nine.dao.CandidateDAO;
import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Candidate;

/**
 * @author Ruslan
 *
 */
public class PostgreSQLCandidateDAO implements CandidateDAO {

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CandidateDAO#createCandidate(java.lang.String, int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	public Candidate createCandidate(String name, int age, String education,
			String email, String phone, String post, String skills,
			int workExpirience) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CandidateDAO#selectCandidateByPost(java.lang.String)
	 */
	@Override
	public Candidate selectCandidateByPost(String post) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CandidateDAO#selectCandidateByWorkExpirience(int)
	 */
	@Override
	public Candidate selectCandidateByWorkExpirience(int workExpirience)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CandidateDAO#selectAllCandidatesToThePost(java.lang.String)
	 */
	@Override
	public Set<Candidate> selectAllCandidatesToThePost(String post)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.mentat.nine.dao.CandidateDAO#deleteCandidate(java.lang.String)
	 */
	@Override
	public void deleteCandidate(String name) throws DAOException {
		// TODO Auto-generated method stub

	}

}
