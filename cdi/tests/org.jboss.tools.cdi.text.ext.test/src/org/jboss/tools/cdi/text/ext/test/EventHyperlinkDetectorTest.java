package org.jboss.tools.cdi.text.ext.test;

import java.util.ArrayList;

import org.eclipse.jface.text.Region;
import org.jboss.tools.cdi.text.ext.hyperlink.EventAndObserverMethodHyperlinkDetector;

public class EventHyperlinkDetectorTest extends HyperlinkDetectorTest {

	public void testEventHyperlinkDetector() throws Exception {
		ArrayList<Region> regionList = new ArrayList<Region>();
		regionList.add(new Region(959, 6));
		regionList.add(new Region(967, 16));
		regionList.add(new Region(985, 11));
		regionList.add(new Region(1006, 6));
		regionList.add(new Region(1014, 3));
		regionList.add(new Region(1019, 34));
		regionList.add(new Region(1055, 42));
		regionList.add(new Region(1107, 6));
		regionList.add(new Region(1115, 34));
		regionList.add(new Region(1151, 36));
		regionList.add(new Region(1188, 3));
		regionList.add(new Region(1235, 11));
		regionList.add(new Region(1334, 42));

		checkRegions("JavaSource/org/jboss/jsr299/tck/tests/event/bindingTypes/EventEmitter.java", regionList, new EventAndObserverMethodHyperlinkDetector());
	}

}