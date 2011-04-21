/**
 * 
 */
package org.jboss.tools.smooks.model.command;

import junit.framework.TestCase;

import org.jboss.tools.smooks.model.csv.CSVReader;
import org.jboss.tools.smooks.model.javabean.Bean;
import org.jboss.tools.smooks.model.javabean.Wiring;

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
		
		assertSame(reader.getFields(), "name,address,age");
		assertSame(reader.getRecordElementName(), "person");
		assertSame(reader.getRootElementName(), "people");
		assertSame(reader.getIndent(),true);
		
		
		
		command1.undo();
		command2.undo();
		command3.undo();
		command4.undo();
		
		assertSame(reader.getFields(), null);
		assertSame(reader.getRecordElementName(), null);
		assertSame(reader.getRootElementName(),null);
//		assertSame(reader.getIndent(),true);
		
		command1.redo();
		command2.redo();
		command3.redo();
		command4.redo();
		
		assertSame(reader.getFields(), "name,address,age");
		assertSame(reader.getRecordElementName(), "person");
		assertSame(reader.getRootElementName(), "people");
		assertSame(reader.getIndent(),true);
		
//		reader.setFields("name,address,age");
//		reader.setRootElementName("people");
//		reader.setRecordElementName("person");
//		reader.setIndent(true);
		
	}
	
	public void testAddCommand(){
		Bean people = new Bean();
		Wiring w = new Wiring();
		
		assertTrue(people.getWireBindings().isEmpty());
		
		AddCommand c = new AddCommand(people, w, "wireBindings");
		
		c.execute();
		
		assertTrue(people.getWireBindings().get(0) == w);
		
		c.undo();
		
		assertTrue(people.getWireBindings().isEmpty());
		
		c.redo();
		
		assertTrue(people.getWireBindings().get(0) == w);
	}
	
	public void testRemoveCommand(){
		Bean people = new Bean();
		Wiring w = new Wiring();
		AddCommand c = new AddCommand(people, w, "wireBindings");
		c.execute();
		
		assertTrue(people.getWireBindings().get(0) == w);
		RemoveCommand rc = new RemoveCommand(people, w, "wireBindings");
		rc.execute();
		assertTrue(people.getWireBindings().isEmpty());
		
		rc.undo();
		assertTrue(people.getWireBindings().get(0) == w);
		
		rc.redo();
		assertTrue(people.getWireBindings().isEmpty());
	}
	
	public void testUnSetCommand(){
		Bean people = new Bean();
		SetCommand c1 = new SetCommand(people, "people", "beanId");
		SetCommand c2 = new SetCommand(people, "java.util.ArrayList", "beanClass");
		
		CompositeCommand cc = new CompositeCommand();
		cc.appendCommand(c1);
		cc.appendCommand(c2);
		cc.execute();
		assertTrue(people.getBeanId().equals("people"));
		assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		UnSetCommand us = new UnSetCommand(people, "beanId");
		UnSetCommand us1 = new UnSetCommand(people, "beanClass");
		
		us.execute();
		us1.execute();
		assertTrue(people.getBeanId() == null);
		assertTrue(people.getBeanClass() == null);
		
		us.undo();
		us1.undo();
		assertTrue(people.getBeanId().equals("people"));
		assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		us.redo();
		us1.redo();
		assertTrue(people.getBeanId() == null);
		assertTrue(people.getBeanClass() == null);
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
		assertTrue(people.getWireBindings().get(0) == w);
		assertTrue(people.getBeanId().equals("people"));
		assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
		
		cc.undo();
		assertTrue(people.getWireBindings().isEmpty());
		assertTrue(people.getBeanId() == null);
		assertTrue(people.getBeanClass() == null);
		
		cc.redo();
		assertTrue(people.getWireBindings().get(0) == w);
		assertTrue(people.getBeanId().equals("people"));
		assertTrue(people.getBeanClass().equals("java.util.ArrayList"));
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
		
		assertTrue(people.getWireBindings().isEmpty());
		
		SetCommand c1 = new SetCommand(people, "people", "beanId");
		AddCommand c = new AddCommand(people, w, "wireBindings");
		
		
		assertTrue(!stack.canRedo()&&!stack.canUndo());
		stack.execute(c);
		
		
		assertTrue(people.getWireBindings().get(0) == w);
		assertTrue(!stack.canRedo());
		stack.undo();
		stack.execute(c1);
		stack.undo();
		assertTrue(people.getBeanId()==null);
		
	}
}
