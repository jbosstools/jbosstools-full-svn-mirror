package org.jboss.tools.deltacloud.core;

public class DeltaCloudException extends Exception {

	private static final long serialVersionUID = 1L;

	public DeltaCloudException(String message, Throwable clause)
	{
		super(message, clause);
	}
	
	public DeltaCloudException(Throwable clause)
	{
		super(clause);
	}
	
	public DeltaCloudException(String message)
	{
		super(message);
	}
	
}
