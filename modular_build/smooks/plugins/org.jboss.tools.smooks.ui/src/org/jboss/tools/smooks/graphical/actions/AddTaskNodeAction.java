package org.jboss.tools.smooks.graphical.actions;

import org.eclipse.ui.IEditorPart;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.graphical.editors.TaskTypeManager;
import org.jboss.tools.smooks.graphical.editors.process.TaskType;

public class AddTaskNodeAction extends AbstractProcessGraphAction {
	
	protected String taskID = null;
	
	
	protected TaskTypeRules rules = new TaskTypeRules();

	public AddTaskNodeAction(String taskID, String text, ISmooksModelProvider provider, IEditorPart editorPart) {
		super(text, provider,editorPart);
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
				if (taskID.equals(TaskTypeManager.TASK_ID_INPUT) || taskID.equals(TaskTypeManager.TASK_ID_JAVA_MAPPING))
					this.setEnabled(true);
			}
		}
	}

}
