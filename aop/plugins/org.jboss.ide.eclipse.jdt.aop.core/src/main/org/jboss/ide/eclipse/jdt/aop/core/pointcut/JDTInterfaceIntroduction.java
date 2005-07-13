/**
 * 
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import java.io.StringReader;
import java.util.Arrays;

import org.eclipse.jdt.core.IType;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.aop.pointcut.ast.TypeExpressionParser;
import org.jboss.ide.eclipse.jdt.aop.core.matchers.JDTTypeMatcher;

/**
 * @author Rob Stryker
 */
public class JDTInterfaceIntroduction extends InterfaceIntroduction{

	
	public JDTInterfaceIntroduction() {
		super("", "", new String[] {});
	}
	
	public void setClassExpression(String expr) {
		try {
			this.classExpr = new ClassExpression(expr);
			this.name = expr;
		} catch( Throwable e ) {
		}
	}
	
	public void setName( String s ) {
		this.name = s;
	}

	public void setInterfaces( String[] s ) {
		this.interfaces = s;
	}
	
	public void setInterfaces(String s) {
		if( s != null ) 
			setInterfaces(s.split(", "));
	}
	
	public void addInterface( String s ) {
		String[] currentInterfaces = getInterfaces();
		String[] newInterfaces = new String[currentInterfaces.length + 1];
		System.arraycopy(currentInterfaces, 0, newInterfaces, 0, currentInterfaces.length);
		newInterfaces[newInterfaces.length-1] = s;
		this.interfaces = newInterfaces;
	}
	
	public void removeInterface( String s ) {
		if( !Arrays.asList(getInterfaces()).contains(s)) return;
		String[] currentInterfaces = getInterfaces();
		String[] newInterfaces = new String[currentInterfaces.length - 1];
		int curIndex = 0;
		int newIndex = 0;
		for( curIndex = 0; curIndex < currentInterfaces.length; curIndex++ ) {
			if( !currentInterfaces[curIndex].equals(s)) {
				newInterfaces[newIndex++] = currentInterfaces[curIndex];
			}
		}
		this.interfaces = newInterfaces;
		
	}
	
	public String getInterfacesAsString() {
		String[] inter = getInterfaces();
		String retval = "";
		for( int i = 0; i < inter.length; i++ ) {
			retval += inter[i];
			if( i != inter.length - 1 ) {
				retval += ", ";
			}
		}
		return retval;
	}
	
	public String getMixinInterfacesAsString(Mixin mixin) {
		String[] inter = mixin.getInterfaces();
		String retval = "";
		for( int i = 0; i < inter.length; i++ ) {
			if(!inter[i].equals("")) {
				retval += inter[i];
				if( i != inter.length - 1 ) {
					retval += ", ";
				}
			}
		}
		return retval;
	}
	
	public boolean matches (IType type)
	{
		try {
			if( ast == null ) {
				ast = new TypeExpressionParser(new StringReader(this.classExpr.getOriginal())).Start();
			}
		} catch( Throwable throwable ) {
			return false;
		}
		
		JDTTypeMatcher matcher = new JDTTypeMatcher(type);
		return ((Boolean) ast.jjtAccept(matcher, null)).booleanValue();
	}
	
	/**
	 * This class is only used temporarily. I would have liked
	 * to have had it extend the Mixin class, but those fields are
	 * final, so to do so is impossible.
	 */
	public static class JDTMixin {
	      protected String classname;
	      protected String[] interfaces;
	      protected String construction;
	      protected boolean trans;
	      
	      public JDTMixin() {
	    	  this.classname = "";
	    	  this.interfaces = new String[] { };
	    	  this.construction = "";
	    	  this.trans = true;
	      }

	      public JDTMixin(Mixin mix) {
	    	  this.classname = mix.getClassName();
	    	  this.interfaces = mix.getInterfaces();
	    	  this.construction = mix.getConstruction();
	    	  this.trans = mix.isTransient();
	      }
	      public String getClassname() {
				return classname;
	      }
	      public void setClassname(String classname) {
	    	  this.classname = classname;
	      }	
	      
	      public String getConstruction() {
				return construction;
		  }

	      public void setConstruction(String construction) {
				this.construction = construction;
	      }
	      public String[] getInterfaces() {
				return interfaces;
	      }
	      public void setInterfaces(String[] interfaces) {
				this.interfaces = interfaces;
	      }
	      public boolean isTrans() {
				return trans;
	      }
	      public void setTrans(boolean trans) {
				this.trans = trans;
	      }
			
	      public Mixin toMixin() {
				return new Mixin(classname, interfaces, construction, trans);
	      }
			
	      public void addInterface( String s ) {
				String[] currentInterfaces = getInterfaces();
				String[] newInterfaces = new String[currentInterfaces.length + 1];
				System.arraycopy(currentInterfaces, 0, newInterfaces, 0, currentInterfaces.length);
				newInterfaces[newInterfaces.length-1] = s;
				this.interfaces = newInterfaces;
	      }
			
	      public void removeInterface( String s ) {
				if( !Arrays.asList(getInterfaces()).contains(s)) return;
				String[] currentInterfaces = getInterfaces();
				String[] newInterfaces = new String[currentInterfaces.length - 1];
				int curIndex = 0;
				int newIndex = 0;
				for( curIndex = 0; curIndex < currentInterfaces.length; curIndex++ ) {
					if( !currentInterfaces[curIndex].equals(s)) {
						newInterfaces[newIndex++] = currentInterfaces[curIndex];
					}
				}
				this.interfaces = newInterfaces;
				
	      }

	}

}
