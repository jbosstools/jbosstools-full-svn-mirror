/*
 * Created on Jan 11, 2005
 */
package org.jboss.ide.eclipse.jdt.aop.core.pointcut;

import java.io.StringReader;

import javassist.NotFoundException;

import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.JavaModelUtil;
import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.MatcherHelper;
import org.jboss.aop.pointcut.Pointcut;
import org.jboss.aop.pointcut.ast.ASTAll;
import org.jboss.aop.pointcut.ast.ASTAttribute;
import org.jboss.aop.pointcut.ast.ASTConstructor;
import org.jboss.aop.pointcut.ast.ASTField;
import org.jboss.aop.pointcut.ast.ASTFieldExecution;
import org.jboss.aop.pointcut.ast.ASTHas;
import org.jboss.aop.pointcut.ast.ASTHasField;
import org.jboss.aop.pointcut.ast.ASTMethod;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ClassExpression;
import org.jboss.aop.pointcut.ast.Node;

import com.thoughtworks.qdox.JavaDocBuilder;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;

/**
 * @author Marshall
 */
public class JDTFieldMatcher extends MatcherHelper {

	protected IField jdtField;
	protected ASTStart start;
	
	public JDTFieldMatcher (IField field, ASTStart start) throws NotFoundException
	{
		super (start, AspectManager.instance());
		this.jdtField = field;
		this.start = start;
	}
	
	protected Boolean resolvePointcut(Pointcut p) {
		throw new RuntimeException("SHOULD NOT BE CALLED");
	}

	public Object visit(ASTField node, Object data) {

		try {
			if (node.getAttributes().size() > 0)
			{
				for (int i = 0; i < node.getAttributes().size(); i++)
				{
					ASTAttribute attr = (ASTAttribute) node.getAttributes().get(i);
					if (! JDTPointcutUtil.matchModifiers(attr, jdtField.getFlags()));
				}
			}
			
			ClassExpression type = node.getType();
			
			String typeSig = jdtField.getTypeSignature();
			IType fieldType = JavaModelUtil.findType(jdtField.getJavaProject(), typeSig);
			if (fieldType == null)
			{
				if (! JDTPointcutUtil.matchesClassExprPrimitive(type, typeSig)) return Boolean.FALSE;
			}
			else {
				if (! JDTPointcutUtil.matchesClassExpr(type, fieldType)) return Boolean.FALSE;
			}
			if (! JDTPointcutUtil.matchesClassExpr(node.getClazz(), jdtField.getDeclaringType())) return Boolean.FALSE;
			
		
		
			if (node.getFieldIdentifier().isAnnotation())
			{
				//TODO implement annotation code here
				JavaField qDoxField = QDoxMatcher.matchField(jdtField);
				// qDoxField will be null if there's an exception, such as a compile exception.
				//System.out.println("QDOXFIELD: " + qDoxField);
				if( qDoxField == null ) return Boolean.FALSE;
				DocletTag[] tags = qDoxField.getTags();
				for( int k = 0; k < tags.length; k++ ) {
					if( node.getFieldIdentifier().getOriginal().equals(tags[k].getName())) 
						return Boolean.TRUE;
				}
		        return Boolean.FALSE;

			}
			else
			{
				if (node.getFieldIdentifier().matches(jdtField.getElementName()))
				{
					return Boolean.TRUE;
				}
			}
			
			return Boolean.FALSE;
		} catch (JavaModelException e) {
			return Boolean.FALSE;
		}
	}
	
	public Object visit(ASTFieldExecution node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, null);
	}
	
	public Object visit(ASTAll node, Object data) {
		if (node.getClazz().isAnnotation())
		{
			JavaField qDoxField = QDoxMatcher.matchField(jdtField);
			// qDoxField will be null if there's an exception, such as a compile exception.
			if( qDoxField == null ) return Boolean.FALSE;
			DocletTag[] tags = qDoxField.getTags();
			for( int k = 0; k < tags.length; k++ ) {
				if( node.getClasseExpression().equals(tags[k].getName()))
					return Boolean.TRUE;
			}
	        return Boolean.FALSE;
		}
		else if (node.getClazz().isInstanceOf())
		{
			if (! JDTPointcutUtil.subtypeOf(jdtField.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		else if (node.getClazz().isTypedef())
		{
			if (! JDTPointcutUtil.matchesTypedef(jdtField.getDeclaringType(), node.getClazz())) return Boolean.FALSE;
		}
		else if (! node.getClazz().matches(jdtField.getDeclaringType().getFullyQualifiedName()))
		{
			return Boolean.FALSE;
		}
		
		return Boolean.TRUE;
	}
	
	public Object visit(ASTHasField node, Object data) {
		ASTField field = (ASTField) node.jjtGetChild(0);
		
		return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), field));
	}
	
	public Object visit(ASTHas node, Object data) {
		Node n = node.jjtGetChild(0);
		if (n instanceof ASTMethod)
		{
			return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), (ASTMethod) n));
		}
		else
		{
			return new Boolean(JDTPointcutUtil.has(jdtField.getDeclaringType(), (ASTConstructor) n));
		}
	}
	


}
