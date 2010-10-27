package org.jboss.tools.internal.deltacloud.test.core.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;

import org.jruby.Ruby;
import org.jruby.RubyInstanceConfig;
import org.jruby.javasupport.JavaEmbedUtils;

public class DeltacloudIntegrationTest {

	public void runDeltaCloud() throws IOException {

		RubyInstanceConfig config = new RubyInstanceConfig();
		Ruby ruby = JavaEmbedUtils.initialize(Collections.EMPTY_LIST, config);
		try {
			ruby.executeScript(readScript("/bootstrap-deltacloud.rb"), "/bootstrap-deltacloud.rb");
		} finally {
			JavaEmbedUtils.terminate(ruby);
		}
	}

	private String readScript(String script) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(script)));
		StringWriter writer = new StringWriter();
		while (reader.ready())
			new PrintWriter(writer).println(reader.readLine());
		return writer.toString();
	}
}
