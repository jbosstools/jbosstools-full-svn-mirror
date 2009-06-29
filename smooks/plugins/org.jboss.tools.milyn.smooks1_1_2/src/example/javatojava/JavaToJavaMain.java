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
package example.javatojava;

import example.srcmodel.*;
import example.trgmodel.*;
import org.milyn.*;
import org.milyn.container.*;
import org.milyn.event.report.*;
import org.milyn.payload.*;
import org.xml.sax.*;

import java.io.*;

/**
 * Simple example main class.
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class JavaToJavaMain {

    /**
     * Run the transform for the request or response.
     * @param srcOrder The input Java Object.
     * @return The transformed Java Object XML.
     */
    protected LineOrder runSmooksTransform(Order srcOrder) throws IOException, SAXException {
        Smooks smooks = new Smooks("example/javatojava/smooks-config.xml");
        ExecutionContext executionContext = smooks.createExecutionContext();

        // Transform the source Order to the target LineOrder via a
        // JavaSource and JavaResult instance...
        JavaSource source = new JavaSource(srcOrder);
        JavaResult result = new JavaResult();

        // Configure the execution context to generate a report...
        executionContext.setEventListener(new HtmlReportGenerator("target/report/report.html"));

        smooks.filter(source, result, executionContext);

        return (LineOrder) result.getBean("lineOrder");
    }

    public static void main(String[] args) throws IOException, SAXException, SmooksException {
        JavaToJavaMain smooksMain = new JavaToJavaMain();
        Order order = new Order();
        LineOrder lineOrder;

        pause("Press 'enter' to display the input Java Order message...");
        System.out.println("\n");
        System.out.println(order);
        System.out.println("\n\n");

        System.out.println("This needs to be transformed to another Java Object.");
        pause("Press 'enter' to display the transformed Java Object...");
        lineOrder = smooksMain.runSmooksTransform(order);
        System.out.println("\n");
        System.out.println(lineOrder);
        System.out.println("\n\n");

        pause("And that's it!");
        System.out.println("\n\n");
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
}