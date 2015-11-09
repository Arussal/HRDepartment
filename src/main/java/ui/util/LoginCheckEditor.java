package ui.util;

import java.beans.PropertyEditorSupport;

public class LoginCheckEditor extends PropertyEditorSupport {
	
	@Override
	public void setAsText(String login) {
		if (login.contains(" ")) {
			setValue(null);
		} else {
			setValue(login);
		}
	}
}
