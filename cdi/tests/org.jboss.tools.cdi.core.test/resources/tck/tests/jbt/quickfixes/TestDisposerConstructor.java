package org.jboss.jsr299.tck.tests.jbt.quickfixes;

import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

@Named("sss")
public class TestDisposerConstructor {
	@Produces
	public String produce(){
		return "test";
	}
	
	
	@Inject
	public TestDisposerConstructor(@Disposes String aaa){
		
	}
}
