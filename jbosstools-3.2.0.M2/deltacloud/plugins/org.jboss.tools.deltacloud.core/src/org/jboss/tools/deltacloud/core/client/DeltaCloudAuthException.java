package org.jboss.tools.deltacloud.core.client;

public class DeltaCloudAuthException extends DeltaCloudClientException {

	private static final long serialVersionUID = 1L;
	
	public DeltaCloudAuthException(String message, Throwable clause)
	{
		super(message, clause);
	}
	
	public DeltaCloudAuthException(Throwable clause)
	{
		super(clause);
	}
	
	public DeltaCloudAuthException(String message)
	{
		super(message);
	}
	
}
