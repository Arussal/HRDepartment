/**
 * 
 */
package com.mentat.nine.dao;

import java.util.List;

import com.mentat.nine.domain.CVForm;

/**
 * @author Ruslan
 *
 */
public interface CVFormDAO {
	
	public CVForm createCVForm();
	
	public CVForm updateCVForm();
	
	public CVForm selectCVForm(String name);
	
	public List<CVForm> selectAllCVFormsToThePost(String post);
	
	public void deleteCVForm(String name);
}
