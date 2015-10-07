/**
 * 
 */
package dao;

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
import domain.Candidate;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CandidateDAO {

	private static Logger log = Logger.getLogger(CandidateDAO.class);
	
	private String title = "candidate";
	
	public CandidateDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	
	public Candidate createCandidate(Candidate candidate) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(candidate);
		transaction.commit();
		Candidate createdCandidate = getCandidateById(id);
		session.close();
		
		return createdCandidate;
	}

	
	public Candidate getCandidateById(int id) {
				
		Session session = HibernateUtil.getSessionFactory().openSession();
		Candidate candidate = session.get(Candidate.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + candidate.getId());
 
		return candidate;
	}

	
	public Set<Candidate> getCandidatesByPost(String post) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Candidate.class);
		Criterion crPost = Restrictions.eq("post", post);
		crit.add(crPost);
		@SuppressWarnings("unchecked")
		List<Candidate> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + post + ", amount = " + list.size());
 
		return new HashSet<Candidate>(list);
	}

	
	public Set<Candidate> getCandidatesByWorkExpirience(int workExpirience) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Candidate.class);
		Criterion crExpirience = Restrictions.eq("workExpirience", workExpirience);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<Candidate> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience " + workExpirience + ", amount = " + list.size());
 
		return new HashSet<Candidate>(list);
	}

	
	public void updateCandidate(Candidate candidate) throws PersistException {
		
		boolean candidatePersisted = isCandidatePersisted(candidate);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(candidate);
			t.commit();
			session.close();
		}
	}
	

	public void deleteCandidate(Candidate candidate) throws PersistException {
	
		boolean candidatePersisted = isCandidatePersisted(candidate);
		
		if (candidatePersisted) {
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


	public Set<Candidate> getAllCandidates() {
		
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

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Candidate.class);
		addCriteria(crit, queries);
		@SuppressWarnings("unchecked")
		List<Candidate> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience different parameters, amount = " + list.size());
 
		return new HashSet<Candidate>(list);
	}
		

	private void addCriteria(Criteria crit, Map<String, List<String>> queries) {
		
		for (String key : queries.keySet()) {
			Criterion cron = makeCriterion(key, queries.get(key));
			crit.add(cron);
		}
	}


	private Criterion makeCriterion(String field, List<String> params) {
		Criterion criterion;
		if (params.get(1).equals("<=")) {
			criterion = Restrictions.le(field, params.get(0));	
		} else if (params.get(1).equals("<")) {
			criterion = Restrictions.lt(field, params.get(0));	
		} else if (params.get(1).equals(">=")) {
			criterion = Restrictions.ge(field, params.get(0));
		} else if (params.get(1).equals(">")) {
			criterion = Restrictions.gt(field, params.get(0));
		} else {
			criterion = Restrictions.eq(field, params.get(0));	 
		}
		return criterion;
	}


	private String getSelectQuery() {
		return "from Candidate c";
	}

		
	private boolean isCandidatePersisted(Candidate candidate) {
		
		if (candidate.getId() == null) {
			return false;
		}	
		Candidate persistedCandidate = getCandidateById(candidate.getId());
		if (persistedCandidate == null) {
			return false;
		}
		
		return true;
	}
}
