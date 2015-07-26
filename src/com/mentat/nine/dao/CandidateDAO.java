/**
 * 
 */
package com.mentat.nine.dao;

import java.util.Set;

import com.mentat.nine.domain.Candidate;

/**
 * @author Ruslan
 *
 */
public interface CandidateDAO {
	
	public Candidate createCandidate(String name, int age, String education, String email, String phone, 
			String post, String skills, int workExpirience);
 
	public Candidate selectCandidate(String name);
	
	public Set<Candidate> selectAllCandidatesToThePost(String post);
	
	public void deleteCandidate(String name);
}
