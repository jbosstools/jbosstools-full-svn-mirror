package org.jboss.tools.smooks.graphical.actions;

import org.jboss.tools.smooks.configuration.SmooksConstants;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.model.graphics.ext.TaskType;

public class AddTaskNodeAction extends AbstractProcessGraphAction {

	protected String taskID = null;

	public AddTaskNodeAction(String taskID, String text, ISmooksModelProvider provider) {
		super(text, provider);
		this.taskID = taskID;
	}

	@Override
	protected void init() {
		// TODO Auto-generated method stub
		super.init();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
	}

	@Override
	public void update() {
		this.setEnabled(false);
		if (this.getCurrentSelectedTask() != null && this.getCurrentSelectedTask().size() == 1) {
			TaskType currentTask = this.getCurrentSelectedTask().get(0);
			String taskID = currentTask.getId();
			if (taskID != null) {
				if (taskID.equals(SmooksConstants.TASK_ID_INPUT) || taskID.equals(SmooksConstants.TASK_ID_JAVA_MAPPING))
					this.setEnabled(true);
			}
		}
	}

}
