/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import junit.framework.TestCase;

import org.jboss.tools.smooks.model.csv.CSVReader;
import org.junit.Assert;

/**
 * @author Dart
 * 
 */
public class SetCommandTest extends TestCase {
	public void testCSVReaderSet() {
		CSVReader reader = new CSVReader();
		
		SetCommand command1 = new SetCommand(reader, "name,address,age", "fields");
		SetCommand command2 = new SetCommand(reader, "people", "rootElementName");
		SetCommand command3 = new SetCommand(reader, "person", "recordElementName");
		SetCommand command4 = new SetCommand(reader, true, "indent");
		
		
		command1.execute();
		command2.execute();
		command3.execute();
		command4.execute();
		
		Assert.assertSame(reader.getFields(), "name,address,age");
		Assert.assertSame(reader.getRecordElementName(), "person");
		Assert.assertSame(reader.getRootElementName(), "people");
		Assert.assertSame(reader.getIndent(),true);
		
		
		
		command1.undo();
		command2.undo();
		command3.undo();
		command4.undo();
		
		Assert.assertSame(reader.getFields(), null);
		Assert.assertSame(reader.getRecordElementName(), null);
		Assert.assertSame(reader.getRootElementName(),null);
//		Assert.assertSame(reader.getIndent(),true);
		
		command1.redo();
		command2.redo();
		command3.redo();
		command4.redo();
		
		Assert.assertSame(reader.getFields(), "name,address,age");
		Assert.assertSame(reader.getRecordElementName(), "person");
		Assert.assertSame(reader.getRootElementName(), "people");
		Assert.assertSame(reader.getIndent(),true);
		
//		reader.setFields("name,address,age");
//		reader.setRootElementName("people");
//		reader.setRecordElementName("person");
//		reader.setIndent(true);
		
	}
}
