package org.jboss.tools.deltacloud.core.client;

public enum HttpStatusRange {
	CLIENT_ERROR(400, 499), SERVER_ERROR(500, 599);

	private int start;
	private int stop;

	HttpStatusRange(int start, int stop) {
		this.start = start;
		this.stop = stop;
	}

	public boolean isInRange(int statusCode) {
		return statusCode >= start
				&& statusCode <= stop;
	}
}
