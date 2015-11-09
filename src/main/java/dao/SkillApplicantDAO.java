package dao;

import java.util.List;
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
import domain.Skill;
import domain.SkillApplicantCV;
import domain.util.LogConfig;

public class SkillApplicantDAO {

	private static Logger log = Logger.getLogger(SkillApplicantDAO.class);
	
	private String title = "Skill";
	
	public SkillApplicantDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	public Skill createSkill(Skill skill) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(skill);
		transaction.commit();
		SkillApplicantCV createdSkill = getSkillById(id);
		session.close();
		
		return createdSkill;
	}
	
	
	public SkillApplicantCV getSkillById(Integer id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillApplicantCV.class);
		Criterion crExpirience = Restrictions.eq("id", id);
		crit.add(crExpirience);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<SkillApplicantCV> list = crit.list();
		session.close();
		log.info("get " + title + " with id " + id);
 
		return list.get(0);
	}
	
	public List<SkillApplicantCV> getSkillsByApplicantCV(CVFormApplicant cvApplicant) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillApplicantCV.class);
		Criterion crExpirience = Restrictions.eq("cvApplicant", cvApplicant);
		crit.add(crExpirience);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<SkillApplicantCV> list = crit.list();
		session.close();
		log.info("get " + title + " with id_cvapp " + cvApplicant);
 
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SkillApplicantCV> getSkillByTitle(String skill) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillApplicantCV.class);
		Criterion crExpirience = Restrictions.eq("skill", skill);
		crit.add(crExpirience);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<SkillApplicantCV> list = crit.list();
		session.close();
		log.info("get " + title + " with skill " + skill + ", amount = " + list.size());
 
		return list;
	}
	
	
	public void updateSkill(Skill skill) {
		
		boolean skillPersisted = isSkillPersisted(skill);
		
		if (skillPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(skill);
			t.commit();
			session.close();
		}
	}
	
	
	public void deleteSkill(Skill skill) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.delete(skill);
			transaction.commit();
		} finally {
			session.close();
		}
			log.info("delete " + title + " with id " + skill.getId());
	}
	
	
	public List<Skill> getAllSkills() {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Skill> list = query.list();
		session.close();
		log.info("get all " + title + "s");

		return list;
	}

	
	
	private String getSelectQuery() {
		String sql = "from SkillApplicantCV s";
		return sql;
	}

	
	private boolean isSkillPersisted(Skill skill)  {
		
		if (skill.getId() == null) {
			return false;
		}	
		Skill persistedSkill = getSkillById(skill.getId());
		if (persistedSkill == null) {
			return false;
		}
		
		return true;
	}
}
