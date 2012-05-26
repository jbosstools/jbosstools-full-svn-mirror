package org.jboss.tools.portlet.ui.bot.task.wait;

public enum TaskDuration {
	SHORT(1 * 1000), NORMAL(10 * 1000), LONG(1 * 60 * 1000), VERY_LONG(10 * 60 * 1000);
	
	private long timeout;
	
	private TaskDuration(long timeout) {
		this.timeout = timeout;
	}
	
	public long getTimeout() {
		return timeout;
	}
}