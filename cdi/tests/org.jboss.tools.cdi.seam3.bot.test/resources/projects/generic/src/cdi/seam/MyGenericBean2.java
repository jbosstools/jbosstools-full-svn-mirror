package cdi.seam;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.jboss.solder.bean.generic.Generic;
import org.jboss.solder.bean.generic.GenericConfiguration;

@GenericConfiguration(MyGenericType.class)
public class MyGenericBean2 {
	@Inject
	@Generic
	MyConfiguration config;

	@Inject
	@Generic
	MyBean c;
	
	@Inject
	void setMyBean(@Generic MyBean parameter1) {}

	@Inject
	@Generic
	MyBean2 c2;
		
	@Inject
	@Generic
	MyBean3 c3;	
	
	@Produces
	MyBean2 createMySecondBean() {
		return new MyBean2("");
	}
	
}
