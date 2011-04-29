/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.cdi.internal.core.impl.definition;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.SourceRange;
import org.jboss.tools.cdi.core.IParametedType;
import org.jboss.tools.cdi.core.IRootDefinitionContext;
import org.jboss.tools.cdi.internal.core.impl.ParametedType;
import org.jboss.tools.common.util.FileUtil;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class AbstractTypeDefinition extends AbstractMemberDefinition {
	protected String qualifiedName;
	protected IType type;
	protected ParametedType parametedType = null;

	protected String content = null;

	protected boolean isVetoed = false;
	
	public AbstractTypeDefinition() {}

	public void veto() {
		isVetoed = true;
	}

	public void unveto() {
		isVetoed = false;
	}

	public boolean isVetoed() {
		return isVetoed;
	}

	public AbstractTypeDefinition getTypeDefinition() {
		return this;
	}

	public String getQualifiedName() {
		return qualifiedName;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type, IRootDefinitionContext context) {
		super.setAnnotatable(type, type, context);
	}

	@Override
	protected void init(IType contextType, IRootDefinitionContext context) throws CoreException {
		this.type = contextType;
		super.init(contextType, context);
		qualifiedName = getType().getFullyQualifiedName();
		parametedType = context.getProject().getDelegate().getNature().getTypeFactory().newParametedType(type);
		if(type != null && !type.isBinary()) {
			parametedType.setPositionProvider(new PositionProviderImpl());
			parametedType.getInheritedTypes();
		}
	}

	public IParametedType getParametedType() {
		return parametedType;
	}

	public Set<IParametedType> getInheritedTypes() {
		return parametedType == null ? new HashSet<IParametedType>() : parametedType.getInheritedTypes();
	}

	public Set<IParametedType> getAllTypes() {
		return parametedType == null ? new HashSet<IParametedType>() : parametedType.getAllTypes();
	}

	public String getContent() {
		if(type == null || type.isBinary()) return null;
		if(content == null && resource instanceof IFile && resource.getName().endsWith(".java")) {
			content = FileUtil.getContentFromEditorOrFile((IFile)resource);
		}
		return content;
	}
	
	class PositionProviderImpl implements ParametedType.PositionProvider {
		Map<String, ISourceRange> map = null;

		void init() throws CoreException {
			map = new HashMap<String, ISourceRange>();
			getContent();
			if(content == null) return;

			//Any disagreement between content and range means that content is obsolete
			//and this build is obsolete (type was updated wile old build is proceeding).
			//New build is pending, and now we only have to go smoothly around inconsistencies.
			ISourceRange r = type.getNameRange();
			if(r == null) return;
			int b = r.getOffset() + r.getLength();
			if(b < 0) return;
			int e = content.indexOf('{', b);
			if(e < 0) e = content.length();
			if(e < b) return;

			String sup = content.substring(b, e);
			String sc = type.getSuperclassName();
			if(sc != null) checkRange(b, sup, sc);
			String[] is = type.getSuperInterfaceNames();
			if(is != null) for (int i = 0; i < is.length; i++) checkRange(b, sup, is[i]);
		}
	
		void checkRange(int offset, String sup, String sc) {
			int k = sup.indexOf(sc);
			if(k >= 0) {
				map.put(sc, new SourceRange(offset + k, sc.length()));
			}			
		}

		public ISourceRange getRange(String superTypeName) {
			if(map == null) {
				try {
					init();
				} catch (CoreException e) {
					//ignore
				}
			}
			
			return map.get(superTypeName);
		}
		
	}

}
