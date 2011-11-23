package org.jboss.tools.portlet.ui.bot.test.task.wizard;

import java.util.ArrayList;
import java.util.List;

import org.jboss.tools.portlet.ui.bot.test.task.CompositeSWTTask;
import org.jboss.tools.ui.bot.ext.SWTBotFactory;

/**
 * 
 * Provides the functionality of navigating between wizard pages. The data
 * should fill every wizard page itself.  
 * 
 * @author ljelinko
 *
 */
public class WizardFillingTask extends CompositeSWTTask<WizardPageFillingTask>{

	private List<WizardPageFillingTask> wizardPages;
	
	public WizardFillingTask() {
		super();
		wizardPages = new ArrayList<WizardPageFillingTask>();
	}
	
	@Override
	public void perform() {
		super.setTasks(wizardPages);
		super.perform();
		getBot().button("Finish").click();
		
		SWTBotFactory.getUtil().waitForAll();
	}
	
	public void addWizardPage(WizardPageFillingTask task){
		wizardPages.add(task);
	}
	
	public void addAllWizardPages(List<WizardPageFillingTask> tasks){
		wizardPages.addAll(tasks);
	}
	
	@Override
	protected void beforeTask(WizardPageFillingTask task) {
		if (!isFirst(task)){
			getBot().button("Next >").click();
		}
	}
	
	private boolean isFirst(WizardPageFillingTask task){
		return getTasks().indexOf(task) == 0;
	}
}
