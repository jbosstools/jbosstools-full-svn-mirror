package org.jboss.tools.vpe.editor;

public class ActiveEditorSwitcher {
	public static final int ACTIVE_EDITOR_CANNOT = 0;
	public static final int ACTIVE_EDITOR_NONE = 1;
	public static final int ACTIVE_EDITOR_SOURCE = 2;
	public static final int ACTIVE_EDITOR_VISUAL = 3;

	private int type = ACTIVE_EDITOR_CANNOT;
	private VpeController vpeController;
	private VpeEditorPart editPart;
	
	public ActiveEditorSwitcher(VpeController vpeController, VpeEditorPart editPart) {
		this.vpeController = vpeController;
		this.editPart = editPart;
	}

	public void initActiveEditor() {
		type = ACTIVE_EDITOR_NONE;
	}

	public void destroyActiveEditor() {
		type = ACTIVE_EDITOR_CANNOT;
	}

	public boolean startActiveEditor(int newType) {
		if (type != ACTIVE_EDITOR_NONE || type == ACTIVE_EDITOR_NONE && newType == ACTIVE_EDITOR_SOURCE
			&& editPart.getVisualMode() == VpeEditorPart.SOURCE_MODE) {
			return false;
		} else {
			type = newType;
			return true;
		}
	}

	public void stopActiveEditor() {
		vpeController.onRefresh();
		type = ACTIVE_EDITOR_NONE;
	}
}
