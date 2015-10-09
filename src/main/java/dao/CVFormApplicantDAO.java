/**
 * 
 */
package dao;

import java.util.List;
import java.util.Map;
import java.util.Properties;

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

	public CVFormApplicant createCVForm(CVFormApplicant cv) {
		
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


	public List<CVFormApplicant> getCVFormByEducation(String education) {

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


	public List<CVFormApplicant> geyCVFormByDesiredSalary(int desiredSalary) {

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
	
	
	
	public List<CVFormApplicant> getCVFormByName(String name) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		Criterion crExpirience = Restrictions.eq("name", name);
		crit.add(crExpirience);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with name " + name + ", amount = " + list.size());
 
		return list;
	}

	
	public List<CVFormApplicant> getCVForm(Map<String, List<String>> queries) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormApplicant.class);
		addCriteria(crit, queries);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience different parameters, amount = " + list.size());
 
		return list;
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

	
	public void updateCVForm(CVFormApplicant cv) throws PersistException {

		boolean candidatePersisted = isCVFormPersisted(cv);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(cv);
			t.commit();
			session.close();
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


	public List<CVFormApplicant> getAllCVForms() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<CVFormApplicant> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}
	
	
	private String getSelectQuery() {
		String sql = "from CVFormApplicant cv";
		return sql;
	}
	
	
	private boolean isCVFormPersisted(CVFormApplicant cv) {
		
		if (cv.getId() == null) {
			return false;
		}	
		CVFormApplicant persistedCVFormApplicant = getCVFormById(cv.getId());
		if (persistedCVFormApplicant == null) {
			return false;
		}
		
		return true;
	}
}
