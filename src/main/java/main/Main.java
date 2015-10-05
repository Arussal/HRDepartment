package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import dao.ManagerDAO;
import dao.exceptions.PersistException;
import domain.Manager;


public class Main {

	public static void main(String[] args) {
			
		Properties properties = new Properties();
		File file = new File("src/main/resources/log4j.properties");
		try {
			properties.load(new FileReader(file));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		ManagerDAO manDao = new ManagerDAO(properties);
		try {
			List<Manager> managers = manDao.getAllManagers();
			System.out.println(managers);
		} catch (PersistException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

}
