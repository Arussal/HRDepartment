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

	@Override
	public Candidate createCandidate() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Candidate readCandidateByPost(String post) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Candidate readCandidateByWorkExpirience(int workExpirience)
			throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCandidate(Candidate candidate) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteCandidate(Candidate candidate) throws DAOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<Candidate> getAllCandidates() throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
