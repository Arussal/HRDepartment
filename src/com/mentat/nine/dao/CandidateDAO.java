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
	
	public Candidate createCandidate(String name, int age, String education, String email, 
			String phone, String post, String skills, int workExpirience) throws DAOException;
 
	public Candidate selectCandidateByPost(String post) throws DAOException;
	
	public Candidate selectCandidateByWorkExpirience(int workExpirience) throws DAOException;
	
	public Set<Candidate> selectAllCandidatesToThePost(String post) throws DAOException;
	
	public void deleteCandidate(String name) throws DAOException;
}
