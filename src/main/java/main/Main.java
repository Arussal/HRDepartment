package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import dao.ApplicantDAO;
import dao.exceptions.PersistException;
import domain.Applicant;


public class Main {

	public static void main(String[] args) {
			
		Properties properties = new Properties();
		File file = new File("src/main/resources/log4j.properties");
		System.out.println(file.getAbsolutePath());
		try {
			properties.load(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ApplicantDAO ahDao = new ApplicantDAO(properties);
		
//		Applicant applicant = new Applicant();
//		applicant.setLogin("Arussal");
//		applicant.setName("Ruslan");
//		applicant.setPassword("Password");
//		Applicant newApplicant = null;
//		try {
//			newApplicant = ahDao.createApplicant(applicant);
//		} catch (PersistException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
		Applicant applicant = null;
		try {
			applicant = ahDao.getApplicantByLogin("Arussal");
		} catch (PersistException e) {
			e.printStackTrace();
		}
		System.out.println(applicant.getId());

	}

}
