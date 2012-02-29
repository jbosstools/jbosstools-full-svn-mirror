/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.cnd.CndElement.NotationType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Abstract;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Autocreated;
import org.jboss.tools.modeshape.jcr.cnd.attributes.DefaultType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.DefaultValues;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Mandatory;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Mixin;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Multiple;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NoFullText;
import org.jboss.tools.modeshape.jcr.cnd.attributes.NoQueryOrder;
import org.jboss.tools.modeshape.jcr.cnd.attributes.OnParentVersion;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Orderable;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PrimaryItem;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PropertyType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Protected;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators.QueryOperator;
import org.jboss.tools.modeshape.jcr.cnd.attributes.Queryable;
import org.jboss.tools.modeshape.jcr.cnd.attributes.RequiredTypes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.SameNameSiblings;
import org.jboss.tools.modeshape.jcr.cnd.attributes.SuperTypes;
import org.jboss.tools.modeshape.jcr.cnd.attributes.ValueConstraints;
import org.modeshape.common.annotation.NotThreadSafe;
import org.modeshape.common.text.ParsingException;
import org.modeshape.common.text.Position;
import org.modeshape.common.text.TokenStream;
import org.modeshape.common.text.TokenStream.Tokenizer;
import org.modeshape.common.util.IoUtil;

/**
 * A class that imports the node types contained in a JCR Compact Node Definition (CND) file into {@link NodeTypeDefinition}
 * instances.
 */
@NotThreadSafe
public class CndImporter {

    protected final boolean jcr170;

    public CndImporter() {
        this.jcr170 = false;
    }

    public CndImporter( boolean compatibleWithPreJcr2 ) {
        this.jcr170 = compatibleWithPreJcr2;
    }

    /**
     * Import the CND content from the supplied stream, placing the content into the importer's destination.
     * 
     * @param stream the stream containing the CND content
     * @param problems where any problems encountered during import should be reported
     * @param resourceName a logical name for the resource name to be used when reporting problems; may be null if there is no
     *            useful name
     * @throws IOException if there is a problem reading from the supplied stream
     */
    public CompactNodeTypeDefinition importFrom( InputStream stream,
                                                 Collection<Throwable> problems,
                                                 String resourceName ) throws IOException {
        return importFrom(IoUtil.read(stream), problems, resourceName);
    }

    /**
     * Import the CND content from the supplied stream, placing the content into the importer's destination.
     * 
     * @param file the file containing the CND content
     * @param problems where any problems encountered during import should be reported
     * @throws IOException if there is a problem reading from the supplied stream
     */
    public CompactNodeTypeDefinition importFrom( File file,
                                                 Collection<Throwable> problems ) throws IOException {
        return importFrom(IoUtil.read(file), problems, file.getCanonicalPath());
    }

    /**
     * Import the CND content from the supplied stream, placing the content into the importer's destination.
     * 
     * @param content the string containing the CND content
     * @param problems where any problems encountered during import should be reported
     * @param resourceName a logical name for the resource name to be used when reporting problems; may be null if there is no
     *            useful name
     */
    public CompactNodeTypeDefinition importFrom( String content,
                                                 Collection<Throwable> problems,
                                                 String resourceName ) {
        try {
            return parse(content);
        } catch (RuntimeException e) {
            problems.add(e);
        }

        return null;
    }

    /**
     * Parse the CND content.
     * 
     * @param content the content (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    public CompactNodeTypeDefinition parse( String content ) {
        Utils.isNotNull(content, "content is null"); //$NON-NLS-1$

        CompactNodeTypeDefinition cnd = new CompactNodeTypeDefinition();
        Tokenizer tokenizer = new CndTokenizer(false, true);
        TokenStream tokens = new TokenStream(content, tokenizer, false);
        tokens.start();

        while (tokens.hasNext()) {
            // Keep reading while we can recognize one of the two types of statements ...
            if (tokens.matches(NamespaceMapping.NOTATION_PREFIX, TokenStream.ANY_VALUE, NamespaceMapping.NOTATION_DELIMITER,
                               TokenStream.ANY_VALUE, NamespaceMapping.NOTATION_SUFFIX)) {
                parseNamespaceMapping(tokens, cnd);
            } else if (tokens.matches(NodeTypeDefinition.NAME_NOTATION_PREFIX, TokenStream.ANY_VALUE,
                                      NodeTypeDefinition.NAME_NOTATION_SUFFIX)) {
                parseNodeTypeDefinition(tokens, cnd);
            } else {
                Position position = tokens.previousPosition();
                Object[] args = new Object[] { tokens.consume(), position.getLine(), position.getColumn() };
                throw new ParsingException(position, NLS.bind(Messages.expectedNamespaceOrNodeDefinition, args));
            }
        }

        return cnd;
    }

    /**
     * Parse the namespace mapping statement that is next on the token stream.
     * 
     * @param tokens the tokens containing the namespace statement (cannot be <code>null</code>)
     * @param cnd the CND object representing the CND file being parsed (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseNamespaceMapping( TokenStream tokens,
                                        CompactNodeTypeDefinition cnd ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (cnd != null) : "cnd is null"; //$NON-NLS-1$

        tokens.consume(NamespaceMapping.NOTATION_PREFIX);
        String prefix = removeQuotes(tokens.consume());

        tokens.consume(NamespaceMapping.NOTATION_DELIMITER);
        String uri = removeQuotes(tokens.consume());

        tokens.consume(NamespaceMapping.NOTATION_SUFFIX);
        cnd.addNamespaceMapping(new NamespaceMapping(prefix, uri));
    }

    /**
     * Parse the node type definition that is next on the token stream.
     * 
     * @param tokens the tokens containing the node type definition (cannot be <code>null</code>)
     * @param cnd the CND object representing the CND file being parsed (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseNodeTypeDefinition( TokenStream tokens,
                                          CompactNodeTypeDefinition cnd ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (cnd != null) : "cnd is null"; //$NON-NLS-1$

        NodeTypeDefinition nodeType = new NodeTypeDefinition();

        // name
        String name = parseNodeTypeName(tokens);

        if (!Utils.isEmpty(name)) {
            nodeType.setName(name);
        }

        // supertypes
        List<String> superTypes = parseSupertypes(tokens);

        if (!superTypes.isEmpty()) {
            for (String superType : superTypes) {
                nodeType.addSuperType(superType);
            }
        }
        // Read the node type options (and vendor extensions) ...
        parseNodeTypeAttributes(tokens, nodeType);

        // Parse property and child node definitions ...
        parsePropertyOrChildNodeDefinitions(tokens, nodeType);

        cnd.addNodeTypeDefinition(nodeType);
    }

    /**
     * Parse a node type name that appears next on the token stream.
     * 
     * @param tokens the tokens containing the node type name (cannot be <code>null</code>)
     * @return the node type name
     * @throws ParsingException if there is a problem parsing the content
     */
    private String parseNodeTypeName( TokenStream tokens ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$

        tokens.consume(NodeTypeDefinition.NAME_NOTATION_PREFIX);
        String name = parseName(tokens);
        tokens.consume(NodeTypeDefinition.NAME_NOTATION_SUFFIX);
        return name;
    }

    /**
     * Parse an optional list of supertypes if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the supertype names (cannot be <code>null</code>)
     * @return the list of supertype names (cannot be <code>null</code> but can be empty)
     * @throws ParsingException if there is a problem parsing the content
     */
    private List<String> parseSupertypes( TokenStream tokens ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$

        if (tokens.canConsume(SuperTypes.NOTATION_PREFIX)) {
            // There is at least one supertype ...
            return parseNameList(tokens);
        }

        return Collections.emptyList();
    }

    /**
     * Parse a list of strings, separated by commas. Any quotes surrounding the strings are removed.
     * 
     * @param tokens the tokens containing the comma-separated strings (cannot be <code>null</code>)
     * @return the list of string values (cannot be <code>null</code> but can be empty)
     * @throws ParsingException if there is a problem parsing the content
     */
    private List<String> parseStringList( TokenStream tokens ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$

        List<String> strings = new ArrayList<String>();

        if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
            // This list is variant ...
            strings.add(AttributeState.VARIANT_STRING);
        } else {
            // Read names until we see a ','
            do {
                strings.add(removeQuotes(tokens.consume()));
            } while (tokens.canConsume(','));
        }

        return strings;
    }

    /**
     * Parse a list of names, separated by commas. Any quotes surrounding the names are removed.
     * 
     * @param tokens the tokens containing the comma-separated strings (cannot be <code>null</code>)
     * @return the list of string values (cannot be <code>null</code> but can be empty)
     * @throws ParsingException if there is a problem parsing the content
     */
    private List<String> parseNameList( TokenStream tokens ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$

        List<String> names = new ArrayList<String>();

        if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
            names.add(AttributeState.VARIANT_STRING);
        } else {
            // Read names until we see a ','
            do {
                names.add(parseName(tokens));
            } while (tokens.canConsume(','));
        }

        return names;
    }

    /**
     * Parse the options for the node types, including whether the node type is orderable, a mixin, abstract, whether it supports
     * querying, and which property/child node (if any) is the primary item for the node type.
     * 
     * @param tokens the tokens containing the comma-separated strings (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type being created; may not be null
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseNodeTypeAttributes( TokenStream tokens,
                                          NodeTypeDefinition nodeTypeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (nodeTypeDefn != null) : "nodeTypeDefn is null"; //$NON-NLS-1$

        while (true) {
            // Keep reading while we see a valid option ...
            if (tokens.canConsumeAnyOf(Orderable.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                       Orderable.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                       Orderable.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {

                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.ORDERABLE, Value.VARIANT);
                } else {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.ORDERABLE, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Mixin.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Mixin.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Mixin.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.MIXIN, Value.VARIANT);
                } else {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.MIXIN, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Abstract.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Abstract.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Abstract.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.ABSTRACT, Value.VARIANT);
                } else {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.ABSTRACT, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Queryable.NO_QUERY_NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Queryable.NO_QUERY_NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Queryable.NO_QUERY_NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.QUERYABLE, Value.IS_NOT);
            } else if (tokens.canConsumeAnyOf(Queryable.QUERY_NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Queryable.QUERY_NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Queryable.QUERY_NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.QUERYABLE, Value.IS);
            } else if (tokens.canConsumeAnyOf(PrimaryItem.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              PrimaryItem.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    nodeTypeDefn.changeState(NodeTypeDefinition.PropertyName.PRIMARY_ITEM, Value.VARIANT);
                } else {
                    nodeTypeDefn.setPrimaryItemName(removeQuotes(tokens.consume()));
                }
            } else {
                // No more valid options on the stream, so stop ...
                break;
            }
        }
    }

    /**
     * Parse a node type's property or child node definitions that appear next on the token stream.
     * 
     * @param tokens the tokens containing the definitions (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type being created (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parsePropertyOrChildNodeDefinitions( TokenStream tokens,
                                                      NodeTypeDefinition nodeTypeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (nodeTypeDefn != null) : "nodeTypeDefn is null"; //$NON-NLS-1$

        while (true) {
            // Keep reading while we see a property definition or child node definition ...
            if (tokens.matches(PropertyDefinition.NOTATION_PREFIX)) {
                parsePropertyDefinition(tokens, nodeTypeDefn);
            } else if (tokens.matches(ChildNodeDefinition.NOTATION_PREFIX)) {
                parseChildNodeDefinition(tokens, nodeTypeDefn);
            } else {
                // The next token does not signal either one of these, so stop ...
                break;
            }
        }
    }

    /**
     * Parse a node type's property definition from the next tokens on the stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type definition (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parsePropertyDefinition( TokenStream tokens,
                                          NodeTypeDefinition nodeTypeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (nodeTypeDefn != null) : "nodeTypeDefn is null"; //$NON-NLS-1$

        tokens.consume(PropertyDefinition.NOTATION_PREFIX);
        PropertyDefinition propDefn = new PropertyDefinition();

        // name
        String name = parseName(tokens);

        if (!Utils.isEmpty(name)) {
            propDefn.setName(name);
        }

        // required type
        parsePropertyType(tokens, propDefn);

        // Parse the default values ...
        parseDefaultValues(tokens, propDefn);

        // Parse the property attributes (and vendor extensions) ...
        parsePropertyAttributes(tokens, nodeTypeDefn, propDefn);

        // Parse the property constraints ...
        parseValueConstraints(tokens, propDefn);

        nodeTypeDefn.addPropertyDefinition(propDefn);
    }

    /**
     * Parse the property type, if a valid one appears next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param propDefn the property definition (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parsePropertyType( TokenStream tokens,
                                    PropertyDefinition propDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (propDefn != null) : "propDefn is null"; //$NON-NLS-1$

        if (tokens.canConsume(PropertyType.NOTATION_PREFIX)) {
            // Parse the (optional) property type ...
            if (tokens.matchesAnyOf(Utils.toUpperCase(PropertyType.validValues()))) {
                String propertyType = tokens.consume();
                propDefn.setType(PropertyType.find(propertyType));
            }

            tokens.consume(PropertyType.NOTATION_SUFFIX);
        }
    }

    /**
     * Parse the property definition's default values, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param propDefn the property definition whose default values are being parsed (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseDefaultValues( TokenStream tokens,
                                     PropertyDefinition propDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (propDefn != null) : "propDefn is null"; //$NON-NLS-1$

        if (tokens.canConsume(DefaultValues.NOTATION_PREFIX)) {
            List<String> values = parseStringList(tokens);

            if (!values.isEmpty()) {
                if ((values.size() == 1) && AttributeState.VARIANT_STRING.equals(values.get(0))) {
                    propDefn.changeState(PropertyDefinition.PropertyName.DEFAULT_VALUES, Value.VARIANT);
                } else {
                    for (String value : values) {
                        propDefn.addDefaultValue(value);
                    }
                }
            }
        }
    }

    /**
     * Parse the property definition's value constraints, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param propDefn the property definition whose value constraints are being parsed (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseValueConstraints( TokenStream tokens,
                                        PropertyDefinition propDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (propDefn != null) : "propDefn is null"; //$NON-NLS-1$

        if (tokens.canConsume(ValueConstraints.NOTATION_PREFIX)) {
            List<String> constraints = parseStringList(tokens);

            if (!constraints.isEmpty()) {
                if ((constraints.size() == 1) && AttributeState.VARIANT_STRING.equals(constraints.get(0))) {
                    propDefn.changeState(PropertyDefinition.PropertyName.VALUE_CONSTRAINTS, Value.VARIANT);
                } else {
                    for (String constraint : constraints) {
                        propDefn.addValueConstraint(constraint);
                    }
                }
            }
        }
    }

    /**
     * Parse the property definition's attributes, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the attributes (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type definition of the property definition (cannot be <code>null</code>)
     * @param propDefn the property definition whose attributes are being parsed (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parsePropertyAttributes( TokenStream tokens,
                                          NodeTypeDefinition nodeTypeDefn,
                                          PropertyDefinition propDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (propDefn != null) : "propDefn is null"; //$NON-NLS-1$

        while (true) {
            if (tokens.canConsumeAnyOf(Autocreated.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                       Autocreated.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                       Autocreated.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.AUTOCREATED, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.AUTOCREATED, AttributeState.Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Mandatory.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Mandatory.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Mandatory.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.MANDATORY, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.MANDATORY, AttributeState.Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Protected.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Protected.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Protected.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.PROTECTED, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.PROTECTED, AttributeState.Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Multiple.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Multiple.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Multiple.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.MULTIPLE, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.MULTIPLE, AttributeState.Value.IS);
                }
            } else if (tokens.matchesAnyOf(Utils.toUpperCase(OnParentVersion.toArray()))) {
                String opv = tokens.consume();
                propDefn.setOnParentVersion(opv);
            } else if (tokens.canConsumeAnyOf(NoFullText.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              NoFullText.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              NoFullText.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.NO_FULL_TEXT, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.NO_FULL_TEXT, AttributeState.Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(NoQueryOrder.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              NoQueryOrder.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              NoQueryOrder.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    propDefn.changeState(PropertyDefinition.PropertyName.NO_QUERY_ORDER, Value.VARIANT);
                } else {
                    propDefn.changeState(PropertyDefinition.PropertyName.NO_QUERY_ORDER, AttributeState.Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(QueryOperators.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              QueryOperators.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              QueryOperators.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                parseQueryOperators(tokens, propDefn);
            } else if (tokens.canConsumeAnyOf("PRIMARY", "PRI", "!")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // these keywords are no longer property attributes in JCR 2.0
                if (!this.jcr170) {
                    Position pos = tokens.previousPosition();
                    int line = pos.getLine();
                    int column = pos.getColumn();
                    throw new ParsingException(tokens.previousPosition(), NLS.bind(Messages.primaryKeywordNotValidInJcr2CndFormat,
                                                                                   line, column));
                }

                // JCR 170: this child node is considered the primary item ...
                nodeTypeDefn.setPrimaryItemName(propDefn.getName());
            } else {
                break;
            }
        }
    }

    /**
     * Parse the property definition's query operators, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param propDefn the property definition whose query operators are being set (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseQueryOperators( TokenStream tokens,
                                      PropertyDefinition propDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (propDefn != null) : "propDefn is null"; //$NON-NLS-1$

        if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
            propDefn.changeState(PropertyDefinition.PropertyName.QUERY_OPS, Value.VARIANT);
        } else {
            // The query operators are expected to be enclosed in a single quote, so therefore will be a single token ...
            String operatorList = removeQuotes(tokens.consume());
            List<String> operators = new ArrayList<String>();

            // Now split this string on ',' ...
            for (String operatorValue : operatorList.split(",")) { //$NON-NLS-1$
                QueryOperator operator = QueryOperator.find(operatorValue.trim());

                if (operator != null) {
                    operators.add(operatorValue);
                } else {
                    throw new ParsingException(tokens.previousPosition(), NLS.bind(Messages.expectedValidQueryOperator, operator));
                }
            }

            if (!operators.isEmpty()) {
                propDefn.setAvailableQueryOperators(operators.toArray(new String[operators.size()]));
            }
        }
    }

    /**
     * Parse a node type's child node definition from the next tokens on the stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type being created (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseChildNodeDefinition( TokenStream tokens,
                                           NodeTypeDefinition nodeTypeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (nodeTypeDefn != null) : "nodeTypeDefn is null"; //$NON-NLS-1$

        tokens.consume(ChildNodeDefinition.NOTATION_PREFIX);
        ChildNodeDefinition childNodeDefn = new ChildNodeDefinition();

        // name
        String name = parseName(tokens);

        if (!Utils.isEmpty(name)) {
            childNodeDefn.setName(name);
        }

        // required types
        parseRequiredPrimaryTypes(tokens, childNodeDefn);

        // default types
        parseDefaultType(tokens, childNodeDefn.getDefaultType());

        // attributes
        parseNodeAttributes(tokens, nodeTypeDefn, childNodeDefn);

        // add child node definition
        nodeTypeDefn.addChildNodeDefinition(childNodeDefn);
    }

    /**
     * Parse the child node definition's list of required primary types, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param childNodeDefn the child node definition (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseRequiredPrimaryTypes( TokenStream tokens,
                                            ChildNodeDefinition childNodeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (childNodeDefn != null) : "childNodeDefn is null"; //$NON-NLS-1$

        if (tokens.canConsume(RequiredTypes.NOTATION_PREFIX)) {
            List<String> requiredTypeNames = parseNameList(tokens);

            if (!requiredTypeNames.isEmpty()) {
                if ((requiredTypeNames.size() == 1) && AttributeState.VARIANT_STRING.equals(requiredTypeNames.get(0))) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.REQUIRED_TYPES, Value.VARIANT);
                } else {
                    for (String requiredTypeName : requiredTypeNames) {
                        childNodeDefn.addRequiredType(requiredTypeName);
                    }
                }
            }

            tokens.consume(RequiredTypes.NOTATION_SUFFIX);
        }
    }

    /**
     * Parse the child node definition's default type, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the definition (cannot be <code>null</code>)
     * @param defaultType the default type (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseDefaultType( TokenStream tokens,
                                   DefaultType defaultType ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (defaultType != null) : "defaultType is null"; //$NON-NLS-1$

        if (tokens.canConsume(DefaultType.NOTATION)) {
            if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                defaultType.set(Value.VARIANT);
            } else {
                String typeName = parseName(tokens);

                if (!Utils.isEmpty(typeName)) {
                    defaultType.setDefaultType(typeName);
                }
            }
        }
    }

    /**
     * Parse the child node definition's attributes, if they appear next on the token stream.
     * 
     * @param tokens the tokens containing the attributes (cannot be <code>null</code>)
     * @param nodeTypeDefn the node type being created (cannot be <code>null</code>)
     * @param childDefn the child node definition (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private void parseNodeAttributes( TokenStream tokens,
                                      NodeTypeDefinition nodeTypeDefn,
                                      ChildNodeDefinition childNodeDefn ) {
        assert (tokens != null) : "tokens is null"; //$NON-NLS-1$
        assert (nodeTypeDefn != null) : "nodeTypeDefn is null"; //$NON-NLS-1$
        assert (childNodeDefn != null) : "childNodeDefn is null"; //$NON-NLS-1$

        while (true) {
            if (tokens.canConsumeAnyOf(Autocreated.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                       Autocreated.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                       Autocreated.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.AUTOCREATED, Value.VARIANT);
                } else {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.AUTOCREATED, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Mandatory.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Mandatory.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Mandatory.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.MANDATORY, Value.VARIANT);
                } else {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.MANDATORY, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(Protected.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              Protected.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              Protected.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.PROTECTED, Value.VARIANT);
                } else {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.PROTECTED, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf(SameNameSiblings.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              SameNameSiblings.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              SameNameSiblings.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.SAME_NAME_SIBLINGS, Value.VARIANT);
                } else {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.SAME_NAME_SIBLINGS, Value.IS);
                }
            } else if (tokens.canConsumeAnyOf("MULTIPLE", "MUL", "*")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // these keywords are no longer node attributes in JCR 2.0
                if (!this.jcr170) {
                    Position pos = tokens.previousPosition();
                    int line = pos.getLine();
                    int column = pos.getColumn();
                    throw new ParsingException(tokens.previousPosition(), NLS.bind(Messages.multipleKeywordNotValidInJcr2CndFormat,
                                                                                   line, column));
                }

                if (tokens.canConsume(AttributeState.VARIANT_CHAR)) {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.SAME_NAME_SIBLINGS, Value.VARIANT);
                } else {
                    childNodeDefn.changeState(ChildNodeDefinition.PropertyName.SAME_NAME_SIBLINGS, Value.IS);
                }
            } else if (tokens.matchesAnyOf(Utils.toUpperCase(OnParentVersion.toArray()))) {
                String opv = tokens.consume();
                childNodeDefn.setOnParentVersion(opv);
            } else if (tokens.canConsumeAnyOf("PRIMARYITEM", "PRIMARY", "PRI", "!")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                if (!this.jcr170) {
                    Position pos = tokens.previousPosition();
                    int line = pos.getLine();
                    int column = pos.getColumn();
                    throw new ParsingException(tokens.previousPosition(), NLS.bind(Messages.primaryKeywordNotValidInJcr2CndFormat,
                                                                                   line, column));
                }

                // Then this child node is considered the primary item ...
                nodeTypeDefn.setPrimaryItemName(childNodeDefn.getName());
            } else if (tokens.canConsumeAnyOf(PrimaryItem.NOTATION[NotationType.LONG_INDEX].toUpperCase(),
                                              PrimaryItem.NOTATION[NotationType.COMPRESSED_INDEX].toUpperCase(),
                                              PrimaryItem.NOTATION[NotationType.COMPACT_INDEX].toUpperCase())) {
                // Then this child node is considered the primary item ...
                nodeTypeDefn.setPrimaryItemName(childNodeDefn.getName());
            } else {
                break;
            }
        }
    }

    /**
     * Parse the name that is expected to be next on the token stream.
     * 
     * @param tokens the tokens containing the name (cannot be <code>null</code>)
     * @return the name (cannot be <code>null</code>)
     * @throws ParsingException if there is a problem parsing the content
     */
    private String parseName( TokenStream tokens ) {
        String value = tokens.consume();
        return removeQuotes(value);
    }

    private final String removeQuotes( String text ) {
        // Remove leading and trailing quotes, if there are any ...
        return text.replaceFirst("^['\"]+", "").replaceAll("['\"]+$", ""); //$NON-NLS-1$ //$NON-NLS-2$//$NON-NLS-3$ //$NON-NLS-4$
    }

}
