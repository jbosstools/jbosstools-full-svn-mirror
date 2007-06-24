/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.hibernate.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.jboss.tools.hibernate.core.exception.ExceptionHandler;
import org.jboss.tools.hibernate.internal.core.util.ClassUtils;
import org.jboss.tools.hibernate.internal.core.util.ScanProject;
import org.jboss.tools.hibernate.internal.core.util.StringUtils;


/**
 * @author Nick - mailto:n.belaevski@exadel.com
 * created: 26.09.2005
 * 
 */
public class CodeRendererService implements ICodeRendererService {

    public static final String esGenerated = "@es_generated";
    public static final String autoGenerated = StringUtils.EOL+StringUtils.EOL+"/** auto generated"+StringUtils.EOL+"* "+esGenerated+StringUtils.EOL+"*/";

    private static abstract class ImportsResolver
    {
        abstract String[][] resolve(String className) throws JavaModelException;
        abstract IPackageFragment getPackage() throws CoreException;
        abstract ICompilationUnit getUnit();
    }
    
    static class CUImportsResolver extends ImportsResolver
    {
        ICompilationUnit base = null;
        
        CUImportsResolver(ICompilationUnit base)
        {
            this.base = base;
        }

        private String[][] format(String packageName, String className)
        {
            String[][] result = null;
            result = new String[1][];
            result[0] = new String[2];
            result[0][0] = packageName;
            result[0][1] = className;
            
            return result;
        }
        
        String[][] resolve(String className) throws JavaModelException {
            if (className == null)
                return null;
                
            String[][] result = null;
            
            IImportDeclaration[] imports = base.getImports();
            for (int i = 0; i < imports.length && result == null; i++) {
                IImportDeclaration declaration = imports[i];
                
                if (!declaration.isOnDemand())
                {
                    if (className.equals(Signature.getSimpleName(declaration.getElementName())))
                    {
                        result = format(Signature.getQualifier(declaration.getElementName()),
                                className);
                    }
                }
                else
                {
                    String packageName = Signature.getQualifier(declaration.getElementName());
                    IJavaProject project = base.getJavaProject();
                    if (project == null)
                        continue;
                    
                    try {
                        if (ScanProject.findClass(className,packageName,project.getProject()) != null)
                        {
                            result = format(Signature.getQualifier(declaration.getElementName()),
                                    className);
                        }
                    } catch (CoreException e) {
                        ExceptionHandler.logThrowableError(e,e.getMessage());
                    }
                }
            }
            
            return result;
        }

        IPackageFragment getPackage() throws CoreException {
            return ClassUtils.getPackageFragment(base);
        }

        ICompilationUnit getUnit() {
            return base;
        }
    }

    static class TypeImportsResolver extends ImportsResolver
    {
        IType base = null;
        
        TypeImportsResolver(IType base)
        {
            this.base = base;
        }

        String[][] resolve(String className) throws JavaModelException {
            return base.resolveType(className, null);
        }

        IPackageFragment getPackage() {
            return base.getPackageFragment();
        }

        ICompilationUnit getUnit() {
            return base.getCompilationUnit();
        }
    }
    
    protected static IMethod getMethod(IType type, String name, String[] params) throws CoreException
    {
        if (type == null || !type.exists() || name == null)
            return null;
        
        IMethod[] methods = type.getMethods();
        if (methods != null)
        {
            for (int i = 0; i < methods.length; i++) {
                IMethod method = methods[i];
                
                if (name.equals(method.getElementName()))
                {
                    String[] methodParams = method.getParameterTypes();
                    if (params == null && methodParams.length == 0)
                        return method;
                        
                    if (params == null || params.length != methodParams.length)
                        continue;
                    
                    boolean validMethod = true;
                    
                    for (int j = 0; j < methodParams.length && validMethod; j++) {
                        if (!ClassUtils.getQualifiedNameFromSignature(type, methodParams[j]).equals(params[j]))
                        {
                            validMethod = false;
                        }
                    }
                    
                    if (validMethod)
                        return method;
                }
            }
        }
        
        
        
        return null;
    }
    
    private static boolean processImport(String packageName, String className, ImportsResolver resolver) throws CoreException
    {
        boolean hasNoPackageConflicts = false;
        boolean importProcessed = false;
        
        String[][] resolved = resolver.resolve(className);
        if (resolved == null || resolved.length == 0 ||
                ( resolved[0][0].equals(packageName) && resolved[0][1].equals(className)))
        {
            hasNoPackageConflicts = true;
        }
        
        ICompilationUnit unit = resolver.getUnit();
        
        if (hasNoPackageConflicts && unit != null)
        {
            importProcessed = true;
            
            if (!packageName.equals("java.lang") && 
                    !packageName.equals(resolver.getPackage().getElementName()) 
                    && !packageName.equals("") && (resolved == null || resolved.length == 0))
            {
                String importString = packageName + "." + ClassUtils.getHolderClassName(className);
                unit.createImport(importString,null,null);
                synchronized (unit) {
                    unit.reconcile(ICompilationUnit.NO_AST, true, null, null);
                }
            }
        }
        return importProcessed;
    }
  
    protected String importTypeName(String type, ImportsResolver resolver) throws CoreException
    {
        String typeName = type;
        int arrayDepth = Signature.getArrayCount(type);
        if (arrayDepth != 0)
        {
            typeName = Signature.getElementType(type);
        }
        
        String packageName = Signature.getQualifier(typeName);
        
        String className;
        boolean isPrimitive = ClassUtils.isPrimitiveType(typeName);
        if (isPrimitive)
            className = typeName;
        else
            className = Signature.getSimpleName(typeName).replace('$','.');
        
        boolean useFQName = true;
        if (isPrimitive || processImport(packageName,className,resolver))
            useFQName = false;

        if (useFQName && packageName != null && packageName.length() != 0)
            className = packageName + "." + className;
        
        for (int i = 0; i < arrayDepth; i++) {
            className += "[]";
        }
        
        return className;
    }
    
    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createCompilationUnit(java.lang.String, org.eclipse.jdt.core.IPackageFragment)
     */
    public ICompilationUnit createCompilationUnit(String compilationUnitName, IPackageFragment fragment) throws CoreException {
        if (compilationUnitName == null || fragment == null)
            return null;

        String packageName = fragment.getElementName();
        StringBuffer cuText = new StringBuffer();
        cuText.append("/* Auto generated file */");
        cuText.append(StringUtils.EOL);
        cuText.append(StringUtils.EOL);
        if (packageName != null && packageName.length() != 0)
        {
            cuText.append("package ");
            cuText.append(packageName);
            cuText.append(";");
            cuText.append(StringUtils.EOL);
        }
        //akuzmin 26.07.2005          
//        cuText.append(" public class ");
//        cuText.append(className);
//        cuText.append("{");
//        cuText.append(EOL);
//        cuText.append("}");
//        cuText.append(EOL);
        
        ICompilationUnit unit = fragment.createCompilationUnit(compilationUnitName+".java",cuText.toString(),true,null);
        if (unit != null)
            ClassUtils.formatMember(null,unit);
        
        return unit;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createType(java.lang.String, java.lang.String, java.lang.String[], boolean, )
     */
    public IType createType(ICompilationUnit unit, String typeName, String baseTypeName, String[] implementingInterfaceNames, boolean isStatic) throws CoreException {
        if (typeName == null || unit == null)
            return null;
        
        if (unit == null || !unit.exists())
            return null;

        IType result = ScanProject.findClassInCU(unit,typeName);
        
        boolean createSerialVersionId = false;
        
        if (result == null || !result.exists())
        {
            int index = typeName.indexOf('$');
            
            String holderTypeName = typeName;
            String className = typeName;
            
            IType innerTypeHolder = null;
            
            ImportsResolver resolver;
            
            if (index != -1)
            {
                holderTypeName = typeName.substring(0, index);
                className = typeName.substring(index+1);
                innerTypeHolder = ScanProject.findClassInCU(unit,holderTypeName);
                if (innerTypeHolder == null)
                    return null;
                
                resolver = new TypeImportsResolver(innerTypeHolder);
            }
            else
            {
                resolver = new CUImportsResolver(unit);
            }

            StringBuffer body = new StringBuffer("public ");
            if (isStatic)
            {
                body.append("static ");
            }
            
            body.append("class "+className);

            if (baseTypeName != null)
            {
                body.append(" extends " + importTypeName(baseTypeName,resolver));
            }
            
            if (implementingInterfaceNames != null && implementingInterfaceNames.length != 0)
            {
                body.append(" implements ");
                for (int i = 0; i < implementingInterfaceNames.length; i++) {
                    String ifaceName = importTypeName(implementingInterfaceNames[i],resolver);
                    
                    if (Serializable.class.getName().equals(implementingInterfaceNames[i]))
                        createSerialVersionId = true;
                    
                    body.append(ifaceName);
                    
                    if (i < implementingInterfaceNames.length - 1)
                        body.append(", ");
                }
            }
            
            body.append("{}");
            
            if (innerTypeHolder != null)
            {
                result = innerTypeHolder.createType(body.toString(), null, false, null);
            }
            else
            {
                result = unit.createType(body.toString(), null, false, null);
            }
        }

        if (result != null && result.exists())
        {
            if (createSerialVersionId)
                ClassUtils.createSerialVersion(result);
            createMethodStubs(result);
            formatMember(result);
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createField(org.eclipse.jdt.core.IType, org.jboss.tools.hibernate.core.ICodeRendererService.PropertyInfoStructure)
     */
    public IField createField(IType inType, PropertyInfoStructure fieldInfo) throws CoreException {
        if (inType == null || fieldInfo == null)
            return null;
        
        boolean isIface = inType.isInterface();
        String className = importTypeName(fieldInfo.propertyTypeName, new TypeImportsResolver(inType));
        
        IField field = inType.getField(fieldInfo.propertyName);
        
        String scopeModifier;
        scopeModifier = ( isIface ? "public" : "private");
        
        if (!field.exists())
            field = inType.createField(autoGenerated + scopeModifier + " " + className + " " + fieldInfo.propertyName + ";",
                null,false,null);
        
        formatMember(field);

        return field;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createGetter(org.eclipse.jdt.core.IType, org.jboss.tools.hibernate.core.ICodeRendererService.PropertyInfoStructure)
     */
    public IMethod createGetter(IType inType, PropertyInfoStructure getterInfo) throws CoreException {
        if (inType == null || getterInfo == null)
            return null;
        
        String getterMethodName = "boolean".equals(getterInfo.propertyTypeName) ? "is" : "get";
        String className = importTypeName(getterInfo.propertyTypeName, new TypeImportsResolver(inType));
        boolean isIface = inType.isInterface();
        
        //aluzmin 25.08.2005        
        String[] params = {};
        IMethod getter = getMethod(inType, getterMethodName+StringUtils.beanCapitalize(getterInfo.propertyName),params);
        
        
        if (getter == null || !getter.exists()){
            StringBuffer getterBody = new StringBuffer();
            getterBody.append(autoGenerated);
            getterBody.append("public "+ className +" "+ getterMethodName + StringUtils.beanCapitalize(getterInfo.propertyName) + "()");
            if (!isIface)
                getterBody.append(StringUtils.EOL+"{\t return this." + getterInfo.propertyName + ";\t}"+StringUtils.EOL);
            else
                getterBody.append(";"+StringUtils.EOL);
            
            getter = inType.createMethod(getterBody.toString(),null,false,null);
        }
        formatMember(getter);
        
        return getter;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createSetter(org.eclipse.jdt.core.IType, org.jboss.tools.hibernate.core.ICodeRendererService.PropertyInfoStructure)
     */
    public IMethod createSetter(IType inType, PropertyInfoStructure setterInfo) throws CoreException {
        if (inType == null || setterInfo == null)
            return null;
        
        boolean isIface = inType.isInterface();
        
        //aluzmin 25.08.2005        
        
        String[] params = new String[]{setterInfo.propertyTypeName};
        String className = importTypeName(setterInfo.propertyTypeName, new TypeImportsResolver(inType));
        
        IMethod setter = getMethod(inType, "set"+StringUtils.beanCapitalize(setterInfo.propertyName),params);
            
        if (setter == null || !setter.exists()){
            StringBuffer setterBody = new StringBuffer();
            setterBody.append(autoGenerated);
            setterBody.append("public void set" + StringUtils.beanCapitalize(setterInfo.propertyName) + "(" + className + " value" + ")");
            if (!isIface)
                setterBody.append(StringUtils.EOL+"{\t this." + setterInfo.propertyName + " = value;" + "\t}"+StringUtils.EOL);
            else
                setterBody.append(";"+StringUtils.EOL);
            
                setter = inType.createMethod(setterBody.toString(),null,false,null);
        }
        
        formatMember(setter);
        
        return setter;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createConstructor(org.eclipse.jdt.core.IType)
     */
    public IMethod createConstructor(IType enclosingType) throws CoreException {
        return createConstructor(enclosingType,null);
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createConstructor(org.eclipse.jdt.core.IType, org.jboss.tools.hibernate.core.ICodeRendererService.PropertyInfoStructure[])
     */
    public IMethod createConstructor(IType enclosingType, PropertyInfoStructure[] parameters) throws CoreException {
        final boolean force = false;
        
        if (enclosingType == null)
            return null;
        
        IMethod[] methods = enclosingType.getMethods();
        IMethod firstMethod = null;
        if (methods != null && methods.length != 0)
            firstMethod = methods[0];

        String[] properties = null;
        PropertyInfoStructure[] params = parameters;
        if (parameters != null)
        {
            properties = new String[parameters.length];
            for (int i = 0; i < properties.length; i++) {
                properties[i] = parameters[i].propertyTypeName;
            }
        }
        
        IMethod ctor = getMethod(enclosingType,enclosingType.getElementName(),properties);

        if (ctor != null && ctor.exists() && ClassUtils.isESGenerated(ctor))
        {
            ctor.delete(true, null);
        }

        if (ctor == null || !ctor.exists())
        {
            String[] names = null;
            String[] types = null;
            
            if (parameters != null)
            {
                names = new String[parameters.length];
                types = new String[parameters.length];
                
                for (int i = 0; i < params.length; i++) {
                    names[i] = parameters[i].propertyName;
                    types[i] = importTypeName(parameters[i].propertyTypeName,new TypeImportsResolver(enclosingType));
                }
            }
            ctor = enclosingType.createMethod(ClassUtils.generateDefaultCtorBody(enclosingType, names, types, "public"),
                    firstMethod,force,null);
            formatMember(ctor);
        }
        
        return ctor;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#formatMember(org.eclipse.jdt.core.IMember)
     */
    public void formatMember(IMember member) throws CoreException {
        if (member == null || member.getCompilationUnit() == null)
            return;
        
        ISourceRange range = null;
        ICompilationUnit unit = member.getCompilationUnit();
        if (member != null)
            range = member.getSourceRange();
        
        int start = 0;
        int length = 0;
        if (range != null)
        {
            start = range.getOffset();
            length = range.getLength();
        }
        Document doc = new Document(unit.getBuffer().getContents());
        CodeFormatter cf = ToolFactory.createCodeFormatter(null);
        TextEdit te = cf.format(CodeFormatter.K_COMPILATION_UNIT,doc.get(),start,
                length,0,null);
        
        try {
            if (te != null)
                te.apply(doc,TextEdit.UPDATE_REGIONS);
        } catch (MalformedTreeException e) {
            ExceptionHandler.logThrowableError(e, null);
        } catch (BadLocationException e) {
            ExceptionHandler.logThrowableError(e, null);
        }
        String newSource = doc.get();
        // update of the compilation unit
        unit.getBuffer().setContents(newSource);
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createEquals(org.eclipse.jdt.core.IType, java.lang.String[])
     */
    public IMethod createEquals(IType inType, String[] fieldNames) throws CoreException {
        if (inType == null || fieldNames == null)
            return null;
        
        IMethod result = null;
        
        StringBuffer equalsBody = new StringBuffer();

        String typename = inType.getTypeQualifiedName().replaceAll("\\$",".");
        equalsBody.append(autoGenerated);
        equalsBody.append("public boolean equals("+importTypeName(Object.class.getName(), new TypeImportsResolver(inType))+" value)"); // yan 20051103
        equalsBody.append(StringUtils.EOL+"{\t "+StringUtils.EOL);
        if (fieldNames.length>0)
        {
            // added by Nick 28.09.2005
            String eqBuilderName = importTypeName(EqualsBuilder.class.getName(), new TypeImportsResolver(inType));
            // by Nick
            
            equalsBody.append("if (this == value) {  return true; }\t"+StringUtils.EOL);            
            equalsBody.append("if (value instanceof "+ typename +"== false) return false;\t"+StringUtils.EOL);
            equalsBody.append(typename +" rhs = ("+ typename +") value;\t"+StringUtils.EOL);
//          equalsBody.append("  return new EqualsBuilder() .appendSuper(super.equals(value))\t"+EOL);
            equalsBody.append("  return new "+eqBuilderName/*EqualsBuilder*/+"()\t"+StringUtils.EOL);
            
            for(int i=0;i<fieldNames.length;i++)
            {
                equalsBody.append(".append(" + fieldNames[i] + ",rhs."+ fieldNames[i] +")\t"+StringUtils.EOL);
            }
                equalsBody.append(".isEquals();\t}"+StringUtils.EOL);
        }
        else
        {
            equalsBody.append("//TODO Implement equals() using Business key equality.\t"+StringUtils.EOL); 
            equalsBody.append("return super.equals(value);\t"+StringUtils.EOL+"}"+StringUtils.EOL); 
        }

//        if (propertyNames.length>0)
//        {
//            inType.getCompilationUnit().createImport(EqualsBuilder.class.getName(),null,null);
//        }           
        
        IMethod equals = getMethod(inType,"equals",new String[]{Object.class.getName()});
        
        if (equals != null && equals.exists() && ClassUtils.isESGenerated(equals))
        {
            equals.delete(true, null);
        }

        if (equals == null || !equals.exists())
        {
            result = inType.createMethod(equalsBody.toString(),null,true,null);
            formatMember(result);
        }
        else
        {
            result = equals;
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createHashCode(org.eclipse.jdt.core.IType, java.lang.String[])
     */
    public IMethod createHashCode(IType inType, String[] fieldNames) throws CoreException {
        if (inType == null || fieldNames == null)
            return null;
        
        IMethod result = null;
        StringBuffer hashBody = new StringBuffer();

        hashBody.append(autoGenerated);
        hashBody.append("public int hashCode()");
        hashBody.append(StringUtils.EOL+"{\t "+StringUtils.EOL);
        if (fieldNames.length>0)
        {
            // added by Nick 28.09.2005
            String hashBuilderName = importTypeName(HashCodeBuilder.class.getName(), new TypeImportsResolver(inType));
            // by Nick
//          hashBody.append("return  new HashCodeBuilder(17, 37) .appendSuper(1)\t"+EOL);
            hashBody.append("return new "+hashBuilderName/*HashCodeBuilder*/+"(17, 37)\t"+StringUtils.EOL);
            
            for(int i=0;i<fieldNames.length;i++)
            {
                hashBody.append(".append(" + fieldNames[i] + ")\t"+StringUtils.EOL);
            }
            hashBody.append(".toHashCode(); \t}"+StringUtils.EOL);
        }
        else
        {
            hashBody.append("//TODO Implement hashCode() using Business key equality.\t"+StringUtils.EOL); 
            hashBody.append("return super.hashCode();\t"+StringUtils.EOL+"}"+StringUtils.EOL);  
        }
//        hashBody.append("int hash = 1;\t"+EOL);
//        for(int i=0;i<compproperties.length;i++)
//        {
//          hashBody.append("hash = 31* hash + " + compproperties[i] + ".hashCode();" + "\t}"+EOL);
//        }
//        hashBody.append("return hash; \t}"+EOL);
        
        
//        if (propertyNames.length>0)
//        {
//            inType.getCompilationUnit().createImport(HashCodeBuilder.class.getName(),null,null);
//        }           
        IMethod hashCode = getMethod(inType, "hashCode",new String[]{});
        
        if (hashCode != null && hashCode.exists() && ClassUtils.isESGenerated(hashCode))
        {
            hashCode.delete(true, null);
        }

        if (hashCode == null || !hashCode.exists())
        {
            result = inType.createMethod(hashBody.toString(),null,true,null);
            formatMember(result);
        }
        else
        {
            result = hashCode;
        }

        return result;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createToString(org.eclipse.jdt.core.IType, java.lang.String[])
     */
    public IMethod createToString(IType inType, String[] fieldNames) throws CoreException {
        if (inType == null || fieldNames == null)
            return null;
        
        IMethod result = null;
        StringBuffer toStringBody = new StringBuffer();

        toStringBody.append(autoGenerated);
        toStringBody.append("public String toString()");
        toStringBody.append(StringUtils.EOL+"{\t "+StringUtils.EOL);
        if (fieldNames.length>0)
        {
//          hashBody.append("return  new HashCodeBuilder(17, 37) .appendSuper(1)\t"+EOL);
            // added by Nick 28.09.2005
            String toStringBuilderName = importTypeName(ToStringBuilder.class.getName(), new TypeImportsResolver(inType));
            // by Nick
            toStringBody.append("return new "+toStringBuilderName/*ToStringBuilder*/+"(this)\t"+StringUtils.EOL);
            
            for(int i=0;i<fieldNames.length;i++)
            {
                toStringBody.append(".append(\"" + fieldNames[i] + "\", " + fieldNames[i] + ")\t"+StringUtils.EOL);
            }
            toStringBody.append(".toString(); \t}"+StringUtils.EOL);
        }
        else
        {
            toStringBody.append("//TODO Implement toString().\t"+StringUtils.EOL); 
            toStringBody.append("return super.toString();\t"+StringUtils.EOL+"}"+StringUtils.EOL);  
        }
//        hashBody.append("int hash = 1;\t"+EOL);
//        for(int i=0;i<compproperties.length;i++)
//        {
//          hashBody.append("hash = 31* hash + " + compproperties[i] + ".hashCode();" + "\t}"+EOL);
//        }
//        hashBody.append("return hash; \t}"+EOL);
        
        
//        if (propertyNames.length>0)
//        {
//            inType.getCompilationUnit().createImport(ToStringBuilder.class.getName(),null,null);
//        }           

        IMethod toString = getMethod(inType, "toString",new String[]{});
        
        if (toString != null && toString.exists() && ClassUtils.isESGenerated(toString))
        {
            toString.delete(true, null);
        }
        
        if (toString == null || !toString.exists())
        {
            result = inType.createMethod(toStringBody.toString(),null,true,null);
            formatMember(result);
        }
        else
        {
            result = toString;
        }
        
        return result;
    }

    public IPackageFragment[] getOrCreatePackage(IProject project, String packageName) throws CoreException {
        if (project == null || packageName == null || !project.hasNature(JavaCore.NATURE_ID))
            return null;
        
        IJavaProject jProject = JavaCore.create(project);
        IPackageFragmentRoot[] roots = jProject.getAllPackageFragmentRoots();
        IPackageFragment[] result = ScanProject.findAllPackageFragments(packageName,project);
        
        List<IPackageFragment> resultList = new ArrayList<IPackageFragment>();
        if (result != null) {
            for (int i = 0; i < result.length; i++) {
                IPackageFragment fragment = result[i];
                
                if (fragment.getKind() == IPackageFragmentRoot.K_SOURCE)
                    resultList.add(fragment);
            }
            
            result = (IPackageFragment[]) resultList.toArray(new IPackageFragment[0]);
        }
        
        
        for (int i = 0; i < roots.length && (result == null || result.length == 0); i++) {
            IPackageFragmentRoot root = roots[i];
            if (root.getKind() == IPackageFragmentRoot.K_SOURCE)
            {
                result = new IPackageFragment[1];
                result[0] = root.createPackageFragment(packageName,false,null);
            }
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see org.jboss.tools.hibernate.core.ICodeRendererService#createMethodStubs(org.eclipse.jdt.core.IType)
     */
    public void createMethodStubs(IType type) throws CoreException {
        if (type != null)
            ClassUtils.createMethods(type);
    }
    
}
