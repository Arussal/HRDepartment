package main;

import java.util.Properties;

import dao.ApplicantDAO;
import dao.exceptions.PersistException;
import domain.Applicant;


public class Main {

	public static void main(String[] args) {
			
		Properties properties = new Properties();
		ApplicantDAO ahDao = new ApplicantDAO(properties);
		
		Applicant applicant = null;
		try {
			applicant = ahDao.getApplicantByLogin("Arussal");
		} catch (PersistException e) {
			e.printStackTrace();
		}
		applicant.setPassword("newPassword");
		try {
			ahDao.updateApplicant(applicant);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
