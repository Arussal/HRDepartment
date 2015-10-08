package dao;

import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import dao.exceptions.PersistException;
import dao.util.HibernateUtil;
import domain.Skill;
import domain.SkillApplicantCV;
import domain.util.LogConfig;

public class SkillDAO {

	private static Logger log = Logger.getLogger(SkillDAO.class);
	
	private String title = "Skill";
	
	public SkillDAO(Properties properties) {
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
		SkillApplicantCV skill = session.get(SkillApplicantCV.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + skill.getId());
 
		return skill;
	}
	
	
	
	public Skill getSkillByTitle(String title) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery() + " where s.skill=:skill";
		Query query = session.createQuery(selectQuery).setString("skill", title);
		
		@SuppressWarnings("unchecked")
		List<Skill> list = query.list();
		session.close();
		if (list.size() != 1) {
			log.error(list.size() + title + " persists with skill " + title);
			throw new PersistException(list.size() + " persists with skill " + title);		
		}
		log.info("get " + title + " with skill " + list.get(0));
		
		return list.get(0);
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
	
	
	public void deleteSkill(Skill skill) throws PersistException {
		
		boolean skillPersisted = isSkillPersisted(skill);
		
		if (skillPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(skill);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + skill.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
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
		String sql = "from Skill s";
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
