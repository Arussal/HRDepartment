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
import domain.CVFormManager;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CVFormManagerDAO {

	private static Logger log = Logger.getLogger(CVFormManagerDAO.class);
	
	private String title = "CVForm";
	
	public CVFormManagerDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}

	public CVFormManager createCVForm(CVFormManager cv) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(cv);
		transaction.commit();
		CVFormManager createdCV = getCVFormById(id);
		session.close();
		
		return createdCV;
	}

	
	public CVFormManager getCVFormById(int id) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		CVFormManager cv = session.get(CVFormManager.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + cv.getId());
 
		return cv;
	}

	
	public List<CVFormManager> getCVFormByPost(String post) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormManager.class);
		Criterion crPost = Restrictions.eq("post", post);
		crit.add(crPost);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + post + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormManager> getCVFormByWorkExpirience(int workExpirience) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormManager.class);
		Criterion crExpirience = Restrictions.eq("workExpirience", workExpirience);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience " + workExpirience + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormManager> getCVFormByEducation(String education)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormManager.class);
		Criterion crExpirience = Restrictions.eq("education", education);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = crit.list();
		session.close();
		log.info("get " + title + " with education " + education + ", amount = " + list.size());
 
		return list;
	}


	public List<CVFormManager> getCVFormByDesiredSalary(int desiredSalary)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormManager.class);
		Criterion crExpirience = Restrictions.eq("desiredSalary", desiredSalary);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = crit.list();
		session.close();
		log.info("get " + title + " with desiredSalary " + desiredSalary + ", amount = " + list.size());
 
		return list;
	}

	
	public List<CVFormManager> getCVForm(Map<String, List<String>> queries) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVFormManager.class);
		addCriteria(crit, queries);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = crit.list();
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
		Object correctTypeParameter = setCorrectTypeOfParameter(params.get(0), params.get(2));
		Criterion criterion;
		if (params.get(1).equals("<=")) {
			criterion = Restrictions.le(field, correctTypeParameter);	
		} else if (params.get(1).equals("<")) {
			criterion = Restrictions.lt(field, correctTypeParameter);	
		} else if (params.get(1).equals(">=")) {
			criterion = Restrictions.ge(field, correctTypeParameter);
		} else if (params.get(1).equals(">")) {
			criterion = Restrictions.gt(field, correctTypeParameter);
		} else {
			criterion = Restrictions.eq(field, correctTypeParameter);	 
		}
		return criterion;
	}

	
	public Object setCorrectTypeOfParameter(String incomeParameter, String type) {
		if (type.equals("integer")) {
			return Integer.valueOf(incomeParameter);
		} else {
			return incomeParameter;
		}
	}
	
	public void updateCVForm(CVFormManager cv) throws PersistException {

		boolean candidatePersisted = isCVFormPersisted(cv);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(cv);
			t.commit();
			session.close();
		}
	}


	public void deleteCVForm(CVFormManager cv) throws PersistException {
				
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


	public List<CVFormManager> getAllCVForms() throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<CVFormManager> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}
	
	
	private String getSelectQuery() {
		String sql = "from CVFormManager cv";
		return sql;
	}
	
	
	private boolean isCVFormPersisted(CVFormManager cv) throws PersistException {
		
		if (cv.getId() == null) {
			return false;
		}	
		CVFormManager persistedCVForm = getCVFormById(cv.getId());
		if (persistedCVForm == null) {
			return false;
		}
		
		return true;
	}
}
