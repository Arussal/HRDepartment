/**
 * 
 */
package dao;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.util.HibernateUtil;
import dao.exceptions.PersistException;
import domain.ApplicationForm;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class ApplicationFormDAO {
	
	private static Logger log = Logger.getLogger(ApplicationFormDAO.class);
	
	private String title = "applicationForm";
		
	public ApplicationFormDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	public ApplicationForm createApplicationForm(ApplicationForm appForm) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(appForm);
		transaction.commit();
		ApplicationForm createdAppForm = getApplicationFormById(id);
		session.close();
		
		return createdAppForm;
	}
		
	
	public ApplicationForm getApplicationFormById(int id) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		ApplicationForm appForm = session.get(ApplicationForm.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + appForm.getId());

		return appForm;
	}

	
	public void updateApplicationForm(ApplicationForm appForm) throws PersistException {
		 
		boolean applicantPersisted = isApplicationFormPersisted(appForm);
		
		if (applicantPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			String updateHQL = getUpdateQuery();
			Query query = session.createQuery(updateHQL);
			prepareInsertData(query, appForm);
			try {
				int count = query.executeUpdate();
				if (1 != count) {
					throw new PersistException("updated " + count + " " + title + " records");
				}
			} finally {
				t.commit();
				session.close();
			}
			log.info("update " + title + " with id " + appForm.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}


	public void deleteApplicationForm(ApplicationForm af) throws PersistException {
		
		boolean appFormPersisted = isApplicationFormPersisted(af);
		
		if (appFormPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(af);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + af.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}


	public List<ApplicationForm> getAllApplicationForms() throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<ApplicationForm> appForms = query.list();
		session.close();
		log.info("get all " + title + "s");
		
		return appForms;
	}


	private String getUpdateQuery() {
		return "update ApplicationForm af set af.date=:date, af.age=:age, af.education=:education, \n"
				+ "af.requirements=:requirements, af.post=:post, af.salary=:salary, \n"
				+ "af.workExpirience=:workExpirience where af.id=:id";
	}
	

	private String getSelectQuery() {
		return "from ApplicationForm af";
	}
	
	
	private boolean isApplicationFormPersisted(ApplicationForm appForm) throws PersistException {
		
		if (appForm.getId() == null) {
			return false;
		}	
		ApplicationForm persistedAppForm = getApplicationFormById(appForm.getId());
		if (persistedAppForm == null) {
			return false;
			
		}
		return true;
	}
	
	
	private void prepareInsertData(Query query, ApplicationForm appForm) {

		query.setParameter("date", appForm.getDate());
		query.setParameter("age", appForm.getAge());
		query.setParameter("education", appForm.getEducation());
		query.setParameter("requirements", appForm.getRequirements());
		query.setParameter("post", appForm.getPost());
		query.setParameter("salary", appForm.getSalary());
		query.setParameter("workExpirience", appForm.getWorkExpirience());
		query.setParameter("id", appForm.getId());
	}

}
