/*
	Milyn - Copyright (C) 2006

	This library is free software; you can redistribute it and/or
	modify it under the terms of the GNU Lesser General Public
	License (version 2.1) as published by the Free Software
	Foundation.

	This library is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

	See the GNU Lesser General Public License for more details:
	http://www.gnu.org/licenses/lgpl.txt
*/
package example.editojava;

import org.milyn.*;
import org.milyn.container.*;
import org.milyn.event.report.*;
import org.milyn.io.*;
import org.xml.sax.*;

import javax.xml.transform.stream.*;
import java.io.*;
import java.util.*;

/**
 * Simple example main class.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class EDItoJavaMain {

    private static byte[] messageIn = readInputMessage();

    private final Smooks smooks;

    protected EDItoJavaMain() throws IOException, SAXException {
        // Instantiate Smooks with the config...
        smooks = new Smooks("example/editojava/smooks-config.xml");
    }

    protected org.milyn.payload.JavaResult runSmooksTransform(ExecutionContext executionContext) throws IOException, SAXException, SmooksException {
    	
    	Locale defaultLocale = Locale.getDefault();
    	Locale.setDefault(new Locale("en", "IE"));
    	
        org.milyn.payload.JavaResult javaResult = new org.milyn.payload.JavaResult();
        
        // Configure the execution context to generate a report...
        executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

        // Filter the input message to the outputWriter, using the execution context...
        smooks.filter(new StreamSource(new ByteArrayInputStream(messageIn)), javaResult, executionContext);

        Locale.setDefault(defaultLocale);
        
        return javaResult;
    }

    public static void main(String[] args) throws IOException, SAXException, SmooksException {
        System.out.println("\n\n==============Message In==============");
        System.out.println(new String(messageIn));
        System.out.println("======================================\n");

        pause("The EDI input stream can be seen above.  Press 'enter' to see how this stream is transformed the Order Object graph...");

        EDItoJavaMain smooksMain = new EDItoJavaMain();
        ExecutionContext executionContext = smooksMain.smooks.createExecutionContext();
        org.milyn.payload.JavaResult result = smooksMain.runSmooksTransform(executionContext);


        System.out.println("\n==============EDI as Java Object Graph=============");
        System.out.println(result.getBean("order"));
        System.out.println("======================================\n\n");

        pause("And that's it!  Press 'enter' to finish...");
    }

    private static byte[] readInputMessage() {
        try {
            return StreamUtils.readStream(EDItoJavaMain.class.getResourceAsStream("input-message.edi"));
        } catch (IOException e) {
            e.printStackTrace();
            return "<no-message/>".getBytes();
        }
    }

    private static void pause(String message) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("> " + message);
            in.readLine();
        } catch (IOException e) {
        }
        System.out.println("\n");
    }

    public org.milyn.payload.JavaResult runSmooksTransform() throws IOException, SAXException {
        ExecutionContext executionContext = smooks.createExecutionContext();
        return runSmooksTransform(executionContext);
    }
}
