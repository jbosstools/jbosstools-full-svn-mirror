package cdi.test.search3;

import javax.enterprise.event.Event;
import javax.inject.Inject;

import cdi.test.search1.Base1;
import cdi.test.search2.Base2;

public class Base3 extends Base2 {
	@Inject
	Event<Base1> event3;
}
