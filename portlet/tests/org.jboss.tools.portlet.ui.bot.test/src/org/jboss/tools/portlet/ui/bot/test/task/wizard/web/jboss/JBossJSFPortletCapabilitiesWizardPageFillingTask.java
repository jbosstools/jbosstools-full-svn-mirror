package org.jboss.tools.portlet.ui.bot.test.task.wizard.web.jboss;

import org.jboss.tools.portlet.ui.bot.test.task.AbstractSWTTask;
import org.jboss.tools.portlet.ui.bot.test.task.wizard.WizardPageFillingTask;

/**
 * Fills the JBoss JSF portlet specific wizard page (for selection of 
 * how the portlet libraries should be configured)
 * 
 * @author Lucia Jelinkova
 *
 */
public class JBossJSFPortletCapabilitiesWizardPageFillingTask extends
		AbstractSWTTask implements WizardPageFillingTask {

	public enum Type {
		DISABLED("Disable Library Configuration"), 
		USER("User library"), 
		RUNTIME_PROVIDER("JSF Portletbridge Runtime Provider");

		private String desc;

		private Type(String s) {
			desc = s;
		}

		@Override
		public String toString() {
			return desc;
		}
	}

	private Type type;
	
	public JBossJSFPortletCapabilitiesWizardPageFillingTask(Type type) {
		super();
		this.type = type;
	}

	@Override
	public void perform() {
		getBot().comboBoxWithLabel("Type:").setSelection(type.toString());
	}
}
