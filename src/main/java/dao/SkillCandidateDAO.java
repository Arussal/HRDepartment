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
import domain.Candidate;
import domain.Skill;
import domain.SkillCandidate;
import domain.util.LogConfig;

public class SkillCandidateDAO {

	private static Logger log = Logger.getLogger(SkillCandidateDAO.class);
	
	private String title = "CandidateSkill";
	
	public SkillCandidateDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	public SkillCandidate createSkill(Skill skill) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(skill);
		transaction.commit();
		SkillCandidate createdSkill = getSkillById(id);
		session.close();
		
		return createdSkill;
	}
	
	
	public SkillCandidate getSkillById(Integer id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillCandidate.class);
		Criterion crId = Restrictions.eq("id", id);
		crit.add(crId);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<SkillCandidate> list = crit.list();
		session.close();
		log.info("get " + title + " with id " + id);
 
		return list.get(0);
	}
	
	public List<SkillCandidate> getSkillsByCandidate(Candidate candidate) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillCandidate.class);
		Criterion crCandidate = Restrictions.eq("candidate", candidate);
		crit.add(crCandidate);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		@SuppressWarnings("unchecked")
		List<SkillCandidate> list = crit.list();
		session.close();
		log.info("get " + title + " with id_cvapp " + candidate);
 
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public List<SkillCandidate> getSkillByTitle(String skill) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(SkillCandidate.class);
		Criterion crSkill = Restrictions.eq("skill", skill);
		crit.add(crSkill);
		crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<SkillCandidate> list = crit.list();
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
		String sql = "from SkillCandidate s";
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
