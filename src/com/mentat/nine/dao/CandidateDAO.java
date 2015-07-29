/**
 * 
 */
package com.mentat.nine.dao;

import java.util.Set;

import com.mentat.nine.dao.exceptions.DAOException;
import com.mentat.nine.domain.Candidate;

/**
 * @author Ruslan
 *
 */
public interface CandidateDAO {
	
	public Candidate createCandidate() throws DAOException;
 
	public Candidate readCandidateByPost(String post) throws DAOException;
	
	public Candidate readCandidateByWorkExpirience(int workExpirience) throws DAOException;
	
	public void updateCandidate(Candidate candidate) throws DAOException;
		
	public void deleteCandidate(Candidate candidate) throws DAOException;
	
	public Set<Candidate> getAllCandidates() throws DAOException;
}
