package dao;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.Applicant;
import domain.util.LogConfig;

public class ApplicantDAO {

	private static Logger log = Logger.getLogger(ApplicantDAO.class);
	
	public ApplicantDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}

		
	public Applicant createApplicant(Applicant applicant) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(applicant);
		transaction.commit();
		Applicant createdApplicant = getApplicantByID(id);
		session.close();
		return createdApplicant;
	}
		
	
	public Applicant getApplicantByID(Integer id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Applicant applicant = session.get(Applicant.class, new Integer(id));
		session.close();
		log.info("get applicant with id " + applicant.getId());
		
		return applicant;
	}
	
	
	public Applicant getApplicantByLogin(String login) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery("FROM Applicant a where a.login =:login")
				.setString("login", login);
		
		@SuppressWarnings("unchecked")
		List<Applicant> list = query.list();
		session.close();
		if (list.size() != 1) {
			log.error(list.size() + " applicant persists with login " + login);
			throw new PersistException(list.size() + " persists with login " + login);		
		}
		log.info("get applicant with login " + list.get(0));
		
		return list.get(0);
	}
	
	
	public List<Applicant> getAllApplicants() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = "from Applicant a";
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Applicant> applicants = query.list();
		session.close();
		log.info("get all applicants");
		
		return applicants;
	}

	
	public void updateApplicant(Applicant applicant) throws PersistException {
		
		boolean applicantPersisted = isApplicantPersisted(applicant);
		
		if (applicantPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(applicant);
			t.commit();
			session.close();
		}
	}
	
	
	public void deleteApplicant(Applicant applicant) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		session.delete(applicant);
		transaction.commit();
		log.info("delete applicant with id " + applicant.getId());

	}
	
		
	private boolean isApplicantPersisted(Applicant applicant) throws PersistException {
		
		if (applicant.getId() == null) {
			return false;
		}	
		Applicant persistedApplicant = getApplicantByID(applicant.getId());
		if (persistedApplicant == null) {
			return false;
			
		}
		return true;
	}		
}