package org.jboss.tools.internal.deltacloud.test;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.junit.Test;

public class DeltacloudIntegrationTest {

	@Test
	public void runDeltaCloud() {
		ScriptEngineManager factory = new ScriptEngineManager();
		ScriptEngine engine = factory.getEngineByName("jruby");
		try {
			engine.eval("puts('Hello')");
			engine.eval("gem install rake");
		} catch (ScriptException exception) {
			exception.printStackTrace();
		}
	}

}
