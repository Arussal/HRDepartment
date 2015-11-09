package ui.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateCheckEditor extends PropertyEditorSupport {

	@Override
	public void setAsText(String enteredDate) {
		
    	
		if (enteredDate.equals("")) {
			setValue(null);
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Date date = null;
			try {
				date = dateFormat.parse(enteredDate);
			} catch (ParseException e) {
				setValue(null);
			}
			setValue(date);
		}
	}
}

