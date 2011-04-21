/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.test.xml;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.XSDAttributeDeclaration;
import org.eclipse.xsd.XSDAttributeUse;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDParticle;
import org.eclipse.xsd.XSDParticleContent;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.XSDTypeDefinition;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

/**
 * @author Dart Peng
 * 
 * @CreateTime Jul 24, 2008
 */
public class XMLStructuredModelBuilderTest {
	public static void main(String[] args) throws IOException {
		Resource resource = new XSDResourceFactoryImpl().createResource(URI
				.createFileURI("/root/Public/smooks_1_0.xsd"));
		resource.load(Collections.EMPTY_MAP);
		XSDSchema schema = (XSDSchema) resource.getContents().get(0);
		List elements = schema.getElementDeclarations();
		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			XSDElementDeclaration elementDec = (XSDElementDeclaration) iterator
					.next();
			// if (elementDec.isAbstract())
			// continue;
			// System.out.println(elementDec.isAbstract());
			XSDTypeDefinition td = elementDec.getType();
			XSDSimpleTypeDefinition st = td.getSimpleType();
			System.out.println("Element Name :" + elementDec.getAliasName());
			// XSDComplexTypeDefinition ct = td.getComplexType();
			if (st != null) {
				System.out.println("\tSimple Type :" + st.toString());
			}
			if (td instanceof XSDComplexTypeDefinition) {
				XSDComplexTypeDefinition ctype = (XSDComplexTypeDefinition) td;

				XSDParticle xsdparticle = td.getComplexType();
				if (xsdparticle != null) {
					XSDParticleContent term = xsdparticle.getContent();//.getTerm
					// ();
					if (term instanceof XSDModelGroup) {
						List list = ((XSDModelGroup) term).getParticles();
						for (Iterator iterator2 = list.iterator(); iterator2
								.hasNext();) {
							XSDParticle xp = (XSDParticle) iterator2.next();
							XSDParticleContent content = xp.getContent();
							if (content instanceof XSDElementDeclaration) {
								XSDElementDeclaration child = (XSDElementDeclaration) content;
								String refStr = "";
								if (child.isElementDeclarationReference()) {
									child = child
											.getResolvedElementDeclaration();
									refStr = "Reference";
								}
								XSDTypeDefinition childType = child
										.getTypeDefinition();

								System.out.println("\t" + refStr
										+ "Element Name: "
										+ child.getAliasName() + " - "
										+ childType);

							}
						}
					}
				}
				List attributeContents = ctype.getAttributeContents();
				for (Iterator iterator2 = attributeContents.iterator(); iterator2
						.hasNext();) {
					XSDAttributeUse attributeUse = (XSDAttributeUse) iterator2
							.next();
					XSDAttributeDeclaration attribute = attributeUse
							.getAttributeDeclaration();
					System.out.println("\tAttributes   :"
							+ attribute.getAliasName() + " - "
							+ attribute.getTypeDefinition().getQName());
				}
				continue;
			} else {
				// XSDSimpleTypeDefinition stype = td.getSimpleType();
				// System.out.println("Simple Type :" +stype.toString());
			}
			System.out.println();
		}
	}
	// private static final String SCHEMA_NAMESPACE =
	// "http://www.w3.org/2001/XMLSchema";
	// private static String destDir;
	//
	// public static void main(String[] args) throws IOException {
	// // need to do this in order to have Eclipse's XSD 'resource'
	// // support work,
	// Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(
	// "xsd", new XSDResourceFactoryImpl());
	//
	// String filename = args[0];
	// if (args.length > 1) {
	// destDir = args[1];
	// } else {
	// destDir = ".";
	// }
	// XSDSchema mainSchema = loadSchema(filename);
	// processSchema(mainSchema);
	// }
	//
	// private static XSDSchema loadSchema(String filename) throws IOException {
	// ResourceSet resourceSet = new ResourceSetImpl();
	// XSDResourceImpl resource = (XSDResourceImpl) resourceSet
	// .createResource(URI.createURI("*.xsd"));
	// resource.setURI(URI.createFileURI(filename));
	// resource.load(resourceSet.getLoadOptions());
	// XSDSchema mainSchema = resource.getSchema();
	// return mainSchema;
	// }
	//
	// private static void processSchema(XSDSchema schema) throws IOException {
	// processSchemaCreateTypes(schema);
	// processSchemaCreateUnmarshaler(schema);
	// }
	//
	// private static void processSchemaCreateTypes(XSDSchema schema)
	// throws IOException {
	// List types = schema.getTypeDefinitions();
	// for (Iterator i = types.iterator(); i.hasNext();) {
	// XSDTypeDefinition typeDef = (XSDTypeDefinition) i.next();
	// if (typeDef instanceof XSDComplexTypeDefinition) {
	// processComplexType((XSDComplexTypeDefinition) typeDef);
	// }
	// }
	// }
	//
	// /**
	// * Handle a top-level complex type definition by creating an AS class.
	// */
	// private static void processComplexType(XSDComplexTypeDefinition typeDef)
	// throws IOException {
	// ASSourceFactory fact = new ASSourceFactory();
	// CompilationUnit unit = fact.newClass(typeName(typeDef));
	// ASClassType clazz = (ASClassType) unit.getType();
	// processComplexTypeBaseType(typeDef, clazz);
	// processComplexTypeAnnotation(typeDef, clazz);
	// processAllComplexTypeAttributes(typeDef, clazz);
	// processAllComplexTypeElements(typeDef, clazz);
	// fact.write(destDir, unit);
	// }
	//
	// /**
	// * Super-type handling
	// */
	// private static void processComplexTypeBaseType(
	// XSDComplexTypeDefinition typeDef, ASClassType clazz) {
	// XSDTypeDefinition baseType = typeDef.getBaseType();
	// if (!isXSDAnyType(baseType)) {
	// clazz.setSuperclass(typeName(baseType));
	// }
	// }
	//
	// /**
	// * turn attrubutes defined by the complexType into class properties
	// */
	// private static void processAllComplexTypeAttributes(
	// XSDComplexTypeDefinition typeDef, ASClassType clazz) {
	// List attrs = typeDef.getAttributeContents();
	// for (Iterator i = attrs.iterator(); i.hasNext();) {
	// XSDAttributeGroupContent attrContent = (XSDAttributeGroupContent) i
	// .next();
	// if (attrContent instanceof XSDAttributeUse) {
	// processComplexTypeAttribute((XSDAttributeUse) attrContent,
	// clazz);
	// }
	// }
	// }
	//
	// /**
	// * turn this particular attribute into a property on the given AS class
	// */
	// private static void processComplexTypeAttribute(XSDAttributeUse attrUse,
	// ASClassType clazz) {
	// XSDAttributeDeclaration attrDecl = attrUse.getAttributeDeclaration();
	// ASField field = clazz.newField(fieldName(attrDecl), Visibility.PUBLIC,
	// typeName(attrDecl.getTypeDefinition()));
	// String doc = findDocumentation(attrDecl.getAnnotation());
	// if (doc != null) {
	// field.setDocComment(doc);
	// }
	// }
	//
	// /**
	// * create a field name based on the given attribute declaration
	// */
	// private static String fieldName(XSDAttributeDeclaration attrDecl) {
	// // TODO: name sanitization etc.
	// return attrDecl.getName();
	// }
	//
	// /**
	// * add any annotation on the given complexType as the documentation
	// comment
	// * for the given AS class.
	// */
	// private static void processComplexTypeAnnotation(
	// XSDComplexTypeDefinition typeDef, ASClassType clazz) {
	// String doc = findDocumentation(typeDef.getAnnotation());
	// if (doc != null) {
	// clazz.setDocComment("\n" + doc + "\n");
	// }
	// }
	//
	// /**
	// * attempt to extract simple text from the documentation element of the
	// * given annotation.
	// */
	// private static String findDocumentation(XSDAnnotation annotation) {
	// if (annotation != null) {
	// List docs = annotation.getUserInformation();
	// for (Iterator i = docs.iterator(); i.hasNext();) {
	// // maybe we can do better..?
	// Element doc = (Element) i.next();
	// return preProcessComment(doc.getTextContent());
	// }
	// }
	// return null;
	// }
	//
	// /**
	// * Strip initial whitespace from all lines in the given string, and return
	// a
	// * string which starts each line with a single space character, ready to
	// go
	// * into a javadoc comment.
	// */
	// private static String preProcessComment(String text) {
	// return text.replaceFirst("\\A\\s*", " ").replaceAll("([\n\r])\\s+",
	// "$1 ");
	// }
	//
	// /**
	// * returns the name of the AS type that holds data for the given component
	// * of the schema
	// */
	// private static String typeName(XSDNamedComponent named) {
	// if (named instanceof XSDSimpleTypeDefinition) {
	// XSDSimpleTypeDefinition simpleType = (XSDSimpleTypeDefinition) named;
	// return lookupTypeName(simpleType);
	// }
	// if (isXSDAnyType(named)) {
	// return "XML";
	// }
	// String pkgName = toPackageName(named.getTargetNamespace());
	// return pkgName + "." + named.getName();
	// }
	//
	// private static boolean isXSDAnyType(XSDNamedComponent named) {
	// return "anyType".equals(named.getName())
	// && named.getTargetNamespace().equals(SCHEMA_NAMESPACE);
	// }
	//
	// private static String toPackageName(String targetNamespace) {
	// URI uri = URI.createURI(targetNamespace);
	// String name = reverseJoin(sanitize(uri.host().split("\\.")), ".");
	// if (uri.hasPath()) {
	// String path = uri.path().replaceAll("/+", "/").replaceFirst("\\A/",
	// "");
	// name = name + "." + join(sanitize(path.split("/")), ".");
	// }
	// return name;
	// }
	//
	// private static String[] sanitize(String[] strings) {
	// for (int i = 0; i < strings.length; i++) {
	// strings[i] = sanitize(strings[i]);
	// }
	// return strings;
	// }
	//
	// private static String sanitize(String string) {
	// StringBuffer result = new StringBuffer();
	// if (!Character.isJavaIdentifierStart(string.charAt(0))
	// && Character.isJavaIdentifierPart(string.charAt(0))) {
	// // e.g. if this fragment starts with a number, prefix
	// // it with an underscore to create a valid identifier
	// result.append("_");
	// }
	// for (int i = 0; i < string.length(); i++) {
	// char c = string.charAt(i);
	// if (Character.isJavaIdentifierPart(c)) {
	// result.append(c);
	// } else {
	// result.append("_");
	// }
	// }
	// return result.toString();
	// }
	//
	// private static String reverseJoin(String[] strings, String delimiter) {
	// StringBuffer result = new StringBuffer();
	// for (int i = strings.length - 1; i >= 0; i--) {
	// result.append(strings[i]);
	// if (i > 0) {
	// result.append(delimiter);
	// }
	// }
	// return result.toString();
	// }
	//
	// private static String join(String[] strings, String delimiter) {
	// StringBuffer result = new StringBuffer();
	// for (int i = 0; i < strings.length; i++) {
	// if (i > 0) {
	// result.append(delimiter);
	// }
	// result.append(strings[i]);
	// }
	// return result.toString();
	// }
	//
	// /**
	// * tries to map XML Schema standard types to AS types
	// */
	// private static String lookupTypeName(XSDSimpleTypeDefinition simpleType)
	// {
	// if (simpleType == null) {
	// return null;
	// }
	// if (simpleType.getTargetNamespace().equals(SCHEMA_NAMESPACE)) {
	// if (simpleType.getName().equals("string")) {
	// return "String";
	// }
	// if (simpleType.getName().equals("int")) {
	// return "Number";
	// }
	// if (simpleType.getName().equals("float")) {
	// return "Number";
	// }
	// System.err.println("Unhandled type " + simpleType.getURI());
	// return null;
	// }
	// return lookupTypeName(simpleType.getBaseTypeDefinition());
	// }
	//
	// /**
	// * adds all elements declared by the given complexType as properties to
	// the
	// * given AS class
	// */
	// private static void processAllComplexTypeElements(
	// XSDComplexTypeDefinition typeDef, ASClassType clazz) {
	// XSDComplexTypeContent complexContent = typeDef.getContent();
	// if (complexContent instanceof XSDParticle) {
	// XSDParticle particle = (XSDParticle) complexContent;
	// XSDParticleContent particleContent = particle.getContent();
	// if (particleContent instanceof XSDModelGroup) {
	// XSDModelGroup modelGroup = (XSDModelGroup) particleContent;
	// if (modelGroup.getCompositor().equals(
	// XSDCompositor.SEQUENCE_LITERAL)) {
	// processComplexTypeSequence(typeDef, modelGroup, clazz);
	// }
	// }
	// }
	// }
	//
	// /**
	// * auxiliary function used by processAllComplexTypeElements() to handle
	// the
	// * xs:sequence within an xs:complexType
	// */
	// private static void processComplexTypeSequence(
	// XSDComplexTypeDefinition typeDef, XSDModelGroup modelGroup,
	// ASClassType clazz) {
	// List particles = modelGroup.getParticles();
	// for (Iterator i = particles.iterator(); i.hasNext();) {
	// XSDParticle part = (XSDParticle) i.next();
	// XSDParticleContent partContent = part.getContent();
	// if (partContent instanceof XSDElementDeclaration) {
	// processComplexTypeElementDeclaration(part,
	// (XSDElementDeclaration) partContent, clazz);
	// }
	// }
	// }
	//
	// /**
	// * handles an xs:element within the xs:sequence of an xs:complexType by
	// * adding a property to the given AS class.
	// */
	// private static void processComplexTypeElementDeclaration(XSDParticle
	// part,
	// XSDElementDeclaration decl, ASClassType clazz) {
	// if (decl.isElementDeclarationReference()) {
	// decl = decl.getResolvedElementDeclaration();
	// }
	// XSDElementDeclaration listElement = getElementIfContainerForList(decl);
	// String typeName = null;
	// String doc = findDocumentation(decl.getAnnotation());
	// if (listElement != null) {
	// typeName = "Array";
	// if (doc == null) {
	// doc = "";
	// } else {
	// doc += "\n\n";
	// }
	// doc += " Elements of type {@link "
	// + typeName(listElement.getType()) + "}";
	// } else if (isMultiplyOccuring(part)) {
	// typeName = "Array";
	// if (doc == null) {
	// doc = "";
	// } else {
	// doc += "\n\n";
	// }
	// if (decl.getType() != null) {
	// doc += "Elements of type {@link " + typeName(decl.getType())
	// + "}\n";
	// }
	// doc += "minOccurs " + describeMultiplicity(part.getMinOccurs())
	// + ", maxOccurs "
	// + describeMultiplicity(part.getMaxOccurs());
	// } else if (decl.getType() != null) {
	// typeName = typeName(decl.getType());
	// }
	// if (typeName == null) {
	// System.err.println("no AS type resulted from: " + decl.getType());
	// }
	// ASField field = clazz.newField(fieldName(decl), Visibility.PUBLIC,
	// typeName);
	// if (doc != null) {
	// field.setDocComment(doc);
	// }
	// }
	//
	// /**
	// * returns the name of the AS field which should hold values of the given
	// * element declaration.
	// */
	// private static String fieldName(XSDElementDeclaration decl) {
	// // TODO: sanitise value, etc.
	// return decl.getName();
	// }
	//
	// /**
	// * If the given element appears to be implementing a list-container design
	// * pattern then return the definition of the element repeated in the list
	// */
	// private static XSDElementDeclaration getElementIfContainerForList(
	// XSDElementDeclaration decl) {
	// XSDTypeDefinition typeDef = decl.getAnonymousTypeDefinition();
	// if (typeDef == null) {
	// return null;
	// }
	// // look for the definition pattern which allows documents like:
	// // ...<eggs><egg/><egg/><egg/></eggs>...
	// // so that we can add a 'eggs' array to the defining class,
	// // rather than creating a useless 'Eggs' class which just holds
	// // the array.
	// // TODO: check for attrs on container,
	// XSDParticle ctype = typeDef.getComplexType();
	// XSDParticleContent particleContent = ctype.getContent();
	// if (particleContent instanceof XSDModelGroup) {
	// XSDModelGroup modelGroup = (XSDModelGroup) particleContent;
	// if (modelGroup.getCompositor().equals(
	// XSDCompositor.SEQUENCE_LITERAL)) {
	// List particles = modelGroup.getParticles();
	// if (particles.size() == 1) {
	// XSDParticle part = (XSDParticle) particles.get(0);
	// XSDParticleContent partContent = part.getContent();
	// if (partContent instanceof XSDElementDeclaration) {
	// if (isMultiplyOccuring(part)) {
	// return (XSDElementDeclaration) partContent;
	// }
	// }
	// }
	// }
	// }
	//
	// return null;
	// }
	//
	// /**
	// * returns true if the given particle's maxOccurs attribute is either
	// * 'unbounded', or an integer greater than 1.
	// */
	// private static boolean isMultiplyOccuring(XSDParticle part) {
	// int max = part.getMaxOccurs();
	// return max == -1 || max > 1; // -1 : unbounded
	// }
	//
	// /**
	// * returns a text string with either the value "unbounded" if the given
	// * integer is -1, or the decimal representation of the given integer
	// * otherwise.
	// */
	// private static String describeMultiplicity(int occurs) {
	// if (occurs == -1) {
	// return "unbounded";
	// } else {
	// return String.valueOf(occurs);
	// }
	// }
	//
	// // ---- marshaling / unmarshaling ----
	//
	// private static void processSchemaCreateUnmarshaler(XSDSchema schema)
	// throws IOException {
	// ASSourceFactory fact = new ASSourceFactory();
	// CompilationUnit unit = fact.newClass(toPackageName(schema
	// .getTargetNamespace())
	// + ".Unmarshaler");
	// ASClassType clazz = (ASClassType) unit.getType();
	// List types = schema.getTypeDefinitions();
	// for (Iterator i = types.iterator(); i.hasNext();) {
	// XSDTypeDefinition typeDef = (XSDTypeDefinition) i.next();
	// if (typeDef instanceof XSDComplexTypeDefinition) {
	// XSDComplexTypeDefinition ctype = (XSDComplexTypeDefinition) typeDef;
	// String typeName = typeName(ctype);
	// ASMethod meth = clazz.newMethod(unmarshalerMethodFor(ctype),
	// Visibility.PUBLIC, typeName(ctype));
	// meth.addParam("thisElement", "XML");
	// meth.addStmt("var _result:" + typeName + " = new " + typeName
	// + "()");
	// processComplexTypeBaseTypeUnmarshal(ctype, meth);
	// processAllComplexTypeAttributesUnmarshal(ctype, meth);
	// processAllComplexTypeElementsUnmarshal(ctype, meth);
	// meth.addStmt("return _result");
	// }
	// }
	// fact.write(destDir, unit);
	// }
	//
	// private static void processComplexTypeBaseTypeUnmarshal(
	// XSDComplexTypeDefinition ctype, ASMethod meth) {
	// XSDTypeDefinition baseType = ctype.getBaseType();
	// if (!isXSDAnyType(baseType)) {
	// // TODO: handle base class initialisation
	// }
	// }
	//
	// private static void processAllComplexTypeAttributesUnmarshal(
	// XSDComplexTypeDefinition ctype, ASMethod meth) {
	// List attrs = ctype.getAttributeContents();
	// for (Iterator i = attrs.iterator(); i.hasNext();) {
	// XSDAttributeGroupContent attrContent = (XSDAttributeGroupContent) i
	// .next();
	// if (attrContent instanceof XSDAttributeUse) {
	// processComplexTypeAttributeUnmarshal(
	// (XSDAttributeUse) attrContent, meth);
	// }
	// }
	// }
	//
	// private static void processComplexTypeAttributeUnmarshal(
	// XSDAttributeUse attrUse, ASMethod meth) {
	// XSDAttributeDeclaration attrDecl = attrUse.getAttributeDeclaration();
	// String accessExpr = "thisElement." + attrAccess(attrDecl);
	// accessExpr = doBasicTypeCoercion(accessExpr, attrDecl.getType());
	// meth.addStmt("_result." + fieldName(attrDecl) + " = " + accessExpr);
	// }
	//
	// private static String doBasicTypeCoercion(String expr,
	// XSDTypeDefinition type) {
	// if (typeIsA(type, SCHEMA_NAMESPACE, "string")) {
	// return expr;
	// }
	// if (typeIsA(type, SCHEMA_NAMESPACE, "int")
	// || typeIsA(type, SCHEMA_NAMESPACE, "float")) {
	// return "Number(" + expr + ")";
	// }
	// System.err.println("Unable to produce type convertion expression for "
	// + type.getURI());
	// return expr + " /* <-- didn't know how to convert */";
	// }
	//
	// private static boolean typeIsA(XSDTypeDefinition type, String namespace,
	// String name) {
	// return (type.getName().equals(name) && type.getTargetNamespace()
	// .equals(namespace))
	// || XSDSchemaQueryTools.isTypeDerivedFrom(type, namespace, name);
	// }
	//
	// private static String attrAccess(XSDAttributeDeclaration attrDecl) {
	// if (isValidIdent(attrDecl.getName())) {
	// return "@" + attrDecl.getName();
	// }
	// return "@[" + ASSourceFactory.str(attrDecl.getName()) + "]";
	// }
	//
	// private static boolean isValidIdent(String name) {
	// if (!Character.isJavaIdentifierStart(name.charAt(0))) {
	// return false;
	// }
	// for (int i = 1; i < name.length(); i++) {
	// if (!Character.isJavaIdentifierPart(name.charAt(0))) {
	// return false;
	// }
	// }
	// return true;
	// }
	//
	// private static void processAllComplexTypeElementsUnmarshal(
	// XSDComplexTypeDefinition typeDef, ASMethod meth) {
	// XSDComplexTypeContent complexContent = typeDef.getContent();
	// if (complexContent instanceof XSDParticle) {
	// XSDParticle particle = (XSDParticle) complexContent;
	// XSDParticleContent particleContent = particle.getContent();
	// if (particleContent instanceof XSDModelGroup) {
	// XSDModelGroup modelGroup = (XSDModelGroup) particleContent;
	// if (modelGroup.getCompositor().equals(
	// XSDCompositor.SEQUENCE_LITERAL)) {
	// processComplexTypeSequenceUnmarshal(typeDef, modelGroup,
	// meth);
	// }
	// }
	// }
	// }
	//
	// private static void processComplexTypeSequenceUnmarshal(
	// XSDComplexTypeDefinition typeDef, XSDModelGroup modelGroup,
	// ASMethod meth) {
	// List particles = modelGroup.getParticles();
	// if (!particles.isEmpty()) {
	// // meth.addComment(" process child elements,");
	// meth.addStmt("var _children:XMLList = thisElement.children()");
	// meth.addStmt("var _seq:int = 0");
	// }
	// for (Iterator i = particles.iterator(); i.hasNext();) {
	// StatementContainer block = meth;
	// XSDParticle part = (XSDParticle) i.next();
	// XSDParticleContent partContent = part.getContent();
	// if (partContent instanceof XSDElementDeclaration) {
	// XSDElementDeclaration elementDecl = (XSDElementDeclaration) partContent;
	// if (elementDecl.isElementDeclarationReference()) {
	// elementDecl = elementDecl.getResolvedElementDeclaration();
	// }
	// if (isOptional(part)) {
	// block = meth.newIf("_children[_seq].name()==new QName("
	// + ASSourceFactory.str(elementDecl
	// .getTargetNamespace()) + ", "
	// + ASSourceFactory.str(elementDecl.getName()));
	// }
	// processComplexTypeSequenceElementDeclarationUnmarshal(part,
	// elementDecl, block);
	// } else {
	// meth.addStmt("_seq++");
	// }
	// }
	// }
	//
	// private static boolean isOptional(XSDParticle part) {
	// return part.getMinOccurs() == 0 && part.getMaxOccurs() == 1;
	// }
	//
	// private static void
	// processComplexTypeSequenceElementDeclarationUnmarshal(
	// XSDParticle part, XSDElementDeclaration decl,
	// StatementContainer block) {
	// String accessExpr = "_children[_seq++]";
	// XSDTypeDefinition typeDef = decl.getType();
	// String fieldAccess = "_result." + fieldName(decl);
	// if (typeDef instanceof XSDSimpleTypeDefinition) {
	// accessExpr = doBasicTypeCoercion(accessExpr + ".text()", typeDef);
	// block.addStmt(fieldAccess + " = " + accessExpr);
	// } else {
	// XSDElementDeclaration listElement = getElementIfContainerForList(decl);
	// if (listElement == null) {
	// String unmarshaled = unmarshaledFrom(typeDef, accessExpr);
	// if (isMultiplyOccuring(part)) {
	// block.addStmt(fieldAccess + " = new Array()");
	// block = block
	// .newWhile("_seq<_children.length() && _children[_seq].name()==new QName("
	// + ASSourceFactory.str(decl
	// .getTargetNamespace())
	// + ", "
	// + ASSourceFactory.str(decl.getName()) + ")");
	// block.addStmt(fieldAccess + ".push(" + unmarshaled + ")");
	// } else {
	// block.addStmt(fieldAccess + " = " + unmarshaled);
	// }
	// } else {
	// block.addStmt(fieldAccess + " = new Array()");
	// StatementContainer loop = block.newForEachIn("var _child:XML",
	// accessExpr + ".elements()");
	// String unmarshaled = unmarshaledFrom(listElement.getType(),
	// "_child");
	// loop.addStmt(fieldAccess + ".push(" + unmarshaled + ")");
	// }
	// }
	// }
	//
	// private static String unmarshaledFrom(XSDTypeDefinition typeDef, String
	// expr) {
	// if (isXSDAnyType(typeDef)) {
	// // result is just the XML node
	// return expr;
	// }
	// return unmarshalerMethodFor(typeDef) + "(" + expr + ")";
	// }
	//
	// private static String unmarshalerMethodFor(XSDTypeDefinition typeDef) {
	// return "unmarshal" + typeDef.getName();
	// }
	// }
}
