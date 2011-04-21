package org.jboss.tools.smooks.model.command;

import java.util.EventObject;

public class SmooksCommandStackEvent extends EventObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2132661251732718650L;
	
	private final ICommand command;
	private final int detail;

	public SmooksCommandStackEvent(SmooksCommandStack smooksCommandStack,
			ICommand command, int state) {
		super(smooksCommandStack);
		this.command = command;
		this.detail = state;
	}

	
	public ICommand getCommand() {
		return command;
	}

	/**
	 * Returns <code>true</code> if this event is fired prior to the stack
	 * changing.
	 * 
	 * @return <code>true</code> if pre-change event
	 * @since 3.2
	 */
	public final boolean isPreChangeEvent() {
		return 0 != (getDetail() & SmooksCommandStack.PRE_MASK);
	}

	/**
	 * Returns <code>true</code> if this event is fired after the stack having
	 * changed.
	 * 
	 * @return <code>true</code> if post-change event
	 * @since 3.2
	 */
	public final boolean isPostChangeEvent() {
		return 0 != (getDetail() & SmooksCommandStack.POST_MASK);
	}

	/**
	 * Returns an integer identifying the type of event which has occurred.
	 * 
	 * @since 3.1
	 * @return the detail of the event
	 */
	public int getDetail() {
		return detail;
	}

}
