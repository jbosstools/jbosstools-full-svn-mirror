/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import junit.framework.TestCase;

import org.jboss.tools.smooks.model.csv.CSVReader;
import org.jboss.tools.smooks.model.javabean.Bean;
import org.jboss.tools.smooks.model.javabean.Wiring;
import org.junit.Assert;

/**
 * @author Dart
 * 
 */
public class CommandTest extends TestCase {
	public void testSetCommand() {
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
	
	public void testAddCommand(){
		Bean people = new Bean();
		Wiring w = new Wiring();
		
		Assert.assertTrue(people.getWireBindings().isEmpty());
		
		AddCommand c = new AddCommand(people, w, "wireBindings");
		
		c.execute();
		
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		
		c.undo();
		
		Assert.assertTrue(people.getWireBindings().isEmpty());
		
		c.redo();
		
		Assert.assertTrue(people.getWireBindings().get(0) == w);
	}
	
	public void testRemoveCommand(){
		Bean people = new Bean();
		Wiring w = new Wiring();
		AddCommand c = new AddCommand(people, w, "wireBindings");
		c.execute();
		
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		RemoveCommand rc = new RemoveCommand(people, w, "wireBindings");
		rc.execute();
		Assert.assertTrue(people.getWireBindings().isEmpty());
		
		rc.undo();
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		
		rc.redo();
		Assert.assertTrue(people.getWireBindings().isEmpty());
	}
	
	public void testUnSetCommand(){
		Bean people = new Bean();
		SetCommand c1 = new SetCommand(people, "people", "beanId");
		SetCommand c2 = new SetCommand(people, "java.util.ArrayList", "beanClass");
		
		CompositeCommand cc = new CompositeCommand();
		cc.appendCommand(c1);
		cc.appendCommand(c2);
		cc.execute();
		Assert.assertTrue(people.getBeanId().equals("people"));
		Assert.assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		UnSetCommand us = new UnSetCommand(people, "beanId");
		UnSetCommand us1 = new UnSetCommand(people, "beanClass");
		
		us.execute();
		us1.execute();
		Assert.assertTrue(people.getBeanId() == null);
		Assert.assertTrue(people.getBeanClass() == null);
		
		us.undo();
		us1.undo();
		Assert.assertTrue(people.getBeanId().equals("people"));
		Assert.assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		us.redo();
		us1.redo();
		Assert.assertTrue(people.getBeanId() == null);
		Assert.assertTrue(people.getBeanClass() == null);
	}
	
	public void testCompositeCommand(){
		Bean people = new Bean();
		Wiring w = new Wiring();
		AddCommand c = new AddCommand(people, w, "wireBindings");
		SetCommand c1 = new SetCommand(people, "people", "beanId");
		SetCommand c2 = new SetCommand(people, "java.util.ArrayList", "beanClass");
		
		CompositeCommand cc = new CompositeCommand();
		cc.appendCommand(c);
		cc.appendCommand(c1);
		cc.appendCommand(c2);
		
		cc.execute();
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		Assert.assertTrue(people.getBeanId().equals("people"));
		Assert.assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		cc.undo();
		Assert.assertTrue(people.getWireBindings().isEmpty());
		Assert.assertTrue(people.getBeanId() == null);
		Assert.assertTrue(people.getBeanClass() == null);
		
		cc.redo();
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		Assert.assertTrue(people.getBeanId().equals("people"));
		Assert.assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
	}
	
	public void testCommandStatck(){
		SmooksCommandStack stack = new SmooksCommandStack();
		ISmooksCommandStackChangeListener listener = new ISmooksCommandStackChangeListener() {
			
			public void stackChanged(SmooksCommandStackEvent event) {
				if(event.getDetail() == SmooksCommandStack.PRE_EXECUTE){
					System.out.println("pre execute " + event.getCommand().getCommandLabel());
				}
				if(event.getDetail() == SmooksCommandStack.POST_EXECUTE){
					System.out.println("post execute " + event.getCommand().getCommandLabel());
				}
				if(event.getDetail() == SmooksCommandStack.PRE_REDO){
					System.out.println("pre redo " + event.getCommand().getCommandLabel());
				}
				if(event.getDetail() == SmooksCommandStack.POST_REDO){
					System.out.println("post redo " + event.getCommand().getCommandLabel());
				}
				if(event.getDetail() == SmooksCommandStack.PRE_UNDO){
					System.out.println("pre undo " + event.getCommand().getCommandLabel());
				}
				if(event.getDetail() == SmooksCommandStack.POST_UNDO){
					System.out.println("post undo " + event.getCommand().getCommandLabel());
				}
			}
		};
		
		stack.addCommandStackEventListener(listener);
		
		Bean people = new Bean();
		Wiring w = new Wiring();
		
		Assert.assertTrue(people.getWireBindings().isEmpty());
		
		SetCommand c1 = new SetCommand(people, "people", "beanId");
		AddCommand c = new AddCommand(people, w, "wireBindings");
		
		
		Assert.assertTrue(!stack.canRedo()&&!stack.canUndo());
		stack.execute(c);
		
		
		Assert.assertTrue(people.getWireBindings().get(0) == w);
		Assert.assertTrue(!stack.canRedo());
		stack.undo();
		stack.execute(c1);
		stack.undo();
		Assert.assertTrue(people.getBeanId()==null);
		
	}
}
