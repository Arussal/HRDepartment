package dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import domain.Employee;
import domain.util.LogConfig;

public class EmployeeDAO {

	private static Logger log = Logger.getLogger(EmployeeDAO.class);
	
	private String title = "Employee";
	
	public EmployeeDAO(Properties properties) {
		LogConfig.loadLogConfig(properties);
	}
	
	public Employee createEmployee(Employee employee) {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		Integer id = (Integer)session.save(employee);
		transaction.commit();
		Employee createdEmployee = getEmployeeById(id);
		session.close();
		
		return createdEmployee;
	}


	public Employee getEmployeeById(int id) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Employee employee = session.get(Employee.class, new Integer(id));
		session.close();
		log.info("get " + title + " with id " + employee.getId());
 
		return employee;
	}

	
	public Set<Employee> getEmployeesByEducation(String education)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		Criterion crEducation = Restrictions.eq("education", education);
		crit.add(crEducation);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + education + ", amount = " + list.size());
 
		return new HashSet<Employee>(list);
	}

	
	public Set<Employee> getEmployeesByDepartament(String departament)
			throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		Criterion crDepartment = Restrictions.eq("departament", departament);
		crit.add(crDepartment);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with departament " + departament + ", amount = " + list.size());
 
		return new HashSet<Employee>(list);
	}

	
	public Set<Employee> getEmployeesByPost(String post) throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		Criterion crPost = Restrictions.eq("post", post);
		crit.add(crPost);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with post " + post + ", amount = " + list.size());
 
		return new HashSet<Employee>(list);
	}

	
	public Set<Employee> getEmployeesBySalary(int salary)
			throws PersistException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		Criterion crSalary = Restrictions.eq("salary", salary);
		crit.add(crSalary);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with salary " + salary + ", amount = " + list.size());
 
		return new HashSet<Employee>(list);
	}

	
	public Set<Employee> getEmployeesByAge(int age) throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		Criterion crAge = Restrictions.eq("post", age);
		crit.add(crAge);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with age " + age + ", amount = " + list.size());
 
		return new HashSet<Employee>(list);
	}

	
	public void updateEmployee(Employee employee) throws PersistException {
		
		boolean employeePersisted = isEmployePersisted(employee);
		
		if (employeePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction t = session.beginTransaction();
			session.update(employee);
			t.commit();
			session.close();
		}
	}
	
	public void deleteEmployee(Employee employee) throws PersistException {
		
		boolean employeePersisted = isEmployePersisted(employee);
		
		if (employeePersisted) {
			Session session = HibernateUtil.getSessionFactory().openSession();
			Transaction transaction = session.beginTransaction();
			try {
				session.delete(employee);
				transaction.commit();
			} finally {
				session.close();
			}
			log.info("delete " + title + " with id " + employee.getId());
		} else {
			log.error(title + " not persisted yet");
			throw new PersistException(title + " not persisted yet");
		}
	}

	
	public Set<Employee> getEmployees(Map<String, List<String>> queries) throws ParseException {

		Session session = HibernateUtil.getSessionFactory().openSession();
		Criteria crit = session.createCriteria(Employee.class);
		addCriteria(crit, queries);
		@SuppressWarnings("unchecked")
		List<Employee> list = crit.list();
		session.close();
		log.info("get " + title + " with expirience different parameters, amount = " + list.size());
	
		return new HashSet<Employee>(list);
	}
			

	private void addCriteria(Criteria crit, Map<String, List<String>> queries) 
			throws ParseException {
		
		for (String key : queries.keySet()) {
			Criterion cron = makeCriterion(key, queries.get(key));
			crit.add(cron);
		}
	}


	private Criterion makeCriterion(String field, List<String> params) throws ParseException {
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

	
	public Object setCorrectTypeOfParameter(String incomeParameter, String type) 
			throws ParseException {
		if (type.equals("integer")) {
			return Integer.valueOf(incomeParameter);
		} else if (type.equals("date")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(incomeParameter);
			return date;
		} else {
			return incomeParameter;
		}
	}
	
	
	public Set<Employee> getAllEmployees() throws PersistException {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		String selectQuery = getSelectQuery();
		Query query = session.createQuery(selectQuery);
		@SuppressWarnings("unchecked")
		List<Employee> employeesList = query.list();
		session.close();
		log.info("get all " + title + "s");
		Set<Employee> employees = new HashSet<Employee>(employeesList);

		return employees;
	}
		

	private String getSelectQuery() {
		String sql = "from Employee e";
		return sql;
	}
	
		
	private boolean isEmployePersisted(Employee employee) {
		
		if (employee.getId() == null) {
			return false;
		}	
		Employee persistedEmployee = getEmployeeById(employee.getId());
		if (persistedEmployee == null) {
			return false;
		}
		
		return true;
	}
}
