/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.ast.ClassExpression;

/**
 * @author Rob Stryker
 */
public class JDTInterfaceIntroduction extends InterfaceIntroduction{

	
	public JDTInterfaceIntroduction() {
		super("test", "test2", new String[] { "interface1", "interface2"});
		getMixins().add(new Mixin("mixinClass", 
				new String[] {"mixininterface1", "mixinInterface2"}, 
				"new whatever(this)", false));
	}
	
	public void setClassExpression(String expr) {
		this.classExpr = new ClassExpression(expr);
	}
	
	public void setInterfaces( String[] s ) {
		this.interfaces = s;
	}
	
	

}
