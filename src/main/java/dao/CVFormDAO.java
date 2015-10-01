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
import domain.CVForm;
import domain.Candidate;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class CVFormDAO {

	private static Logger log = Logger.getLogger(CVFormDAO.class);
	
	private String title = "CVForm";
	
	public CVFormDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}

	public CVForm createCVForm(CVForm cv) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(cv);
		transaction.commit();
		CVForm createdCV = getCVFormById(id);
		session.close();
		
		return createdCV;
	}

	
	public CVForm getCVFormById(int id) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		CVForm cv = session.get(CVForm.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + cv.getId());
 
		return cv;
	}

	
	public List<CVForm> getCVFormByPost(String post) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVForm.class);
		Criterion crPost = Restrictions.eq("post", post);
		crit.add(crPost);
		@SuppressWarnings("unchecked")
		List<CVForm> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + post + ", amount = " + list.size());
 
		return list;
	}


	public List<CVForm> getCVFormByWorkExpirience(int workExpirience) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVForm.class);
		Criterion crExpirience = Restrictions.eq("workExpirience", workExpirience);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVForm> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience " + workExpirience + ", amount = " + list.size());
 
		return list;
	}


	public List<CVForm> getCVFormByEducation(String education)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVForm.class);
		Criterion crExpirience = Restrictions.eq("education", education);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVForm> list = crit.list();
		session.close();
		log.info("get " + title + " with education " + education + ", amount = " + list.size());
 
		return list;
	}


	public List<CVForm> geyCVFormByDesiredSalary(int desiredSalary)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(CVForm.class);
		Criterion crExpirience = Restrictions.eq("desiredSalary", desiredSalary);
		crit.add(crExpirience);
		@SuppressWarnings("unchecked")
		List<CVForm> list = crit.list();
		session.close();
		log.info("get " + title + " with desiredSalary " + desiredSalary + ", amount = " + list.size());
 
		return list;
	}

	
	public List<CVForm> getCVForm(Map<String, List<String>> queries) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Candidate.class);
		addCriteria(crit, queries);
		@SuppressWarnings("unchecked")
		List<CVForm> list = crit.list();
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

	
	public void updateCVForm(CVForm cv) throws PersistException {

		boolean candidatePersisted = isCVFormPersisted(cv);
		
		if (candidatePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			String updateHQL = getUpdateQuery();
			Query query = session.createQuery(updateHQL);
			prepareInsertData(query, cv);
			try {
				int count = query.executeUpdate();
				if (1 != count) {
					throw new PersistException("updated " + count + " " + title + " records");
				}
			} finally {
				t.commit();
				session.close();
			}
			log.info("update " + title + " with id " + cv.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
		
	}


	public void deleteCVForm(CVForm cv) throws PersistException {
				
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


	public List<CVForm> getAllCVForms() throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<CVForm> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}

	
	private String getUpdateQuery() {
		return "update CVForm cv set cv.name=:name, cv.age=:age, cv.education=:education, \n"
				+ "cv.email=:email, cv.phone=:phone, cv.workExpirience=:workExpirience, \n"
				+ "cv.skills=:skills, cv.desiredSalary=:desiredSalary, \n"
				+ "cv.additionalInfo=:additionalInfo where cv.id=:id";
	}
	
	
	private String getSelectQuery() {
		String sql = "from CVForm cv";
		return sql;
	}
	
	
	private boolean isCVFormPersisted(CVForm cv) throws PersistException {
		
		if (cv.getId() == null) {
			return false;
		}	
		CVForm persistedCVForm = getCVFormById(cv.getId());
		if (persistedCVForm == null) {
			return false;
		}
		
		return true;
	}
	
	
	private void prepareInsertData(Query query, CVForm cv) {

		query.setParameter("name", cv.getName());
		query.setParameter("age", cv.getAge());
		query.setParameter("education", cv.getEducation());
		query.setParameter("email", cv.getEmail());
		query.setParameter("phone", cv.getPhone());
		query.setParameter("skills", cv.getSkills());
		query.setParameter("workExpirience", cv.getWorkExpirience());
		query.setParameter("desiredSalary", cv.getDesiredSalary());
		query.setParameter("additionalInfo", cv.getAdditionalInfo());
		query.setParameter("id", cv.getId());
	}

}
