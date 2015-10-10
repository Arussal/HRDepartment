/**
 * 
 */
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
import domain.Department;
import domain.util.LogConfig;

/**
 * @author Ruslan
 *
 */
public class DepartmentDAO{

	private static Logger log = Logger.getLogger(DepartmentDAO.class);
	
	private String title = "Department";
	
	public DepartmentDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}

	public Department createDepartment(Department department) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(department);
		transaction.commit();
		Department createdDepartment = getDepartmentById(id);
		session.close();
		
		return createdDepartment;
	}

	public Department getDepartmentById(int id) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Department department = session.get(Department.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + department.getId());
 
		return department;
	}
	
	public Department getDepartmentByName(String name) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Department.class);
		Criterion crName = Restrictions.eq("name", name);
		crit.add(crName);
		@SuppressWarnings("unchecked")
		List<Department> list = crit.list();
		session.close();
		log.info("get " + title + " with name " + name + ", amount = " + list.size());
 
		return list.get(0);
	}


	public List<Department> getDepartmentsByHead(String head) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Candidate.class);
		Criterion crHead = Restrictions.eq("head", head);
		crit.add(crHead);
		@SuppressWarnings("unchecked")
		List<Department> list = crit.list();
		session.close();
		log.info("get " + title + " with head " + head + ", amount = " + list.size());
 
		return list;
	}


	public void updateDepartment(Department department) throws PersistException {
		
		boolean departmentPersisted = isDepartmentPersisted(department);
		
		if (departmentPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(department);
			t.commit();
			session.close();	
		}
	}

	public void deleteDepartment(Department department) throws PersistException {
		
		boolean departmentPersisted= isDepartmentPersisted(department);
		
		if (departmentPersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(department);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + department.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}


	public List<Department> getAllDepartments() {

		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Department> departments = query.list();
		session.close();
		log.info("get all " + title + "s");

		return departments;	
	}

	
	private String getSelectQuery() {
		String sql = "from Department d";
		return sql;
	}
	
	
	private boolean isDepartmentPersisted(Department department) {
		
		if (department.getId() == null) {
			return false;
		}	
		Department persistedDepartment= getDepartmentById(department.getId());
		if (persistedDepartment == null) {
			return false;
		}
		
		return true;
	}
}
