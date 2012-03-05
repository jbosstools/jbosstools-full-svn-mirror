/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.jboss.tools.modeshape.jcr.cnd;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.util.NLS;
import org.jboss.tools.modeshape.jcr.Messages;
import org.jboss.tools.modeshape.jcr.MultiValidationStatus;
import org.jboss.tools.modeshape.jcr.Utils;
import org.jboss.tools.modeshape.jcr.ValidationStatus;
import org.jboss.tools.modeshape.jcr.cnd.attributes.AttributeState.Value;
import org.jboss.tools.modeshape.jcr.cnd.attributes.PropertyType;
import org.jboss.tools.modeshape.jcr.cnd.attributes.QueryOperators.QueryOperator;

/**
 * Used to validate values stored in a CND.
 */
public final class CndValidator {

    /**
     * @param value the value being checked (can be <code>null</code> or empty)
     * @param propertyType the property type of the property definition the value is for (cannot be <code>null</code>)
     * @param propertyName the name to use to identify the property definition (cannot be <code>null</code> empty)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus isValid( final String value,
                                            final PropertyType propertyType,
                                            final String propertyName ) {
        Utils.isNotNull(propertyType, "propertyType"); //$NON-NLS-1$

        if (Utils.isEmpty(value)) {
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyValue, propertyName));
        }

        try {
            if (PropertyType.STRING == propertyType) {
                return ValidationStatus.OK_STATUS;
            }

            if (PropertyType.BINARY == propertyType) {
                // TODO implement BINARY validation
            } else if (PropertyType.BOOLEAN == propertyType) {
                if (!value.equalsIgnoreCase(Boolean.TRUE.toString()) && !value.equalsIgnoreCase(Boolean.FALSE.toString())) {
                    return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidPropertyValueForType, new Object[] { value,
                            PropertyType.BOOLEAN, propertyName }));
                }
            } else if (PropertyType.DATE == propertyType) {
                try {
                    Date.valueOf(value);
                } catch (final Exception e) {
                    return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidPropertyValueForType, new Object[] { value,
                            PropertyType.DATE, propertyName }));
                }
            } else if (PropertyType.DECIMAL == propertyType) {
                try {
                    new BigDecimal(value);
                } catch (final Exception e) {
                    return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidPropertyValueForType, value,
                                                                        PropertyType.DECIMAL));
                }
            } else if (PropertyType.DOUBLE == propertyType) {
                try {
                    Double.parseDouble(value);
                } catch (final Exception e) {
                    return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidPropertyValueForType, value,
                                                                        PropertyType.DOUBLE));
                }
            } else if (PropertyType.LONG == propertyType) {
                try {
                    Long.parseLong(value);
                } catch (final Exception e) {
                    return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidPropertyValueForType, value,
                                                                        PropertyType.LONG));
                }
            } else if (PropertyType.NAME == propertyType) {
                return validateLocalName(value, propertyName);
            } else if (PropertyType.PATH == propertyType) {
                // TODO implement PATH validation
            } else if (PropertyType.REFERENCE == propertyType) {
                // TODO implement REFERENCE validation
            } else if (PropertyType.UNDEFINED == propertyType) {
                // TODO implement UNDEFINED validation
            } else if (PropertyType.URI == propertyType) {
                return validateUri(value, propertyName);
            } else if (PropertyType.WEAKREFERENCE == propertyType) {
                // TODO implement WEAKREFERENCE validation
            }

            return ValidationStatus.OK_STATUS;
        } catch (final Exception e) {
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.errorValidatingPropertyValueForType, new Object[] { value,
                    propertyType, propertyName }));
        }
    }

    /**
     * @param value the value being checked (can be <code>null</code> or empty)
     * @param propertyType the property type of the property definition the value is for (cannot be <code>null</code>)
     * @param propertyName the name to use to identify the property definition (cannot be <code>null</code> empty)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void isValid( final String value,
                                final PropertyType propertyType,
                                final String propertyName,
                                final MultiValidationStatus status ) {
        final ValidationStatus newStatus = isValid(value, propertyType, propertyName);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param childNodeDefinition the child node definition being validated (cannot be <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateChildNodeDefinition( final ChildNodeDefinition childNodeDefinition ) {
        Utils.isNotNull(childNodeDefinition, "childNodeDefinition"); //$NON-NLS-1$

        /**
         * <pre>
         *     ERROR - Empty or invalid child node definition name
         *     ERROR - Invalid required type name
         *     ERROR - Duplicate required type name
         *     ERROR - Cannot have explicit required types when required types is marked as a variant
         *     ERROR - Invalid default type name
         *     ERROR - Cannot have explicit default type when default type is marked as a variant
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();
        String childNodeName = childNodeDefinition.getName();

        if (Utils.isEmpty(childNodeName)) {
            childNodeName = Messages.missingName;
        }

        { // name
          // ERROR - Empty or invalid child node definition name
            validateLocalName(childNodeDefinition.getName(), Messages.childDefinitionName, status);
        }

        { // required types
            final String[] requiredTypeNames = childNodeDefinition.getRequiredPrimaryTypeNames();

            if (Utils.isEmpty(requiredTypeNames)) {
                if (childNodeDefinition.getState(ChildNodeDefinition.PropertyName.REQUIRED_TYPES) == Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyRequiredTypes, childNodeName)));
                }
            } else {
                final List<String> names = new ArrayList<String>(requiredTypeNames.length);

                for (final String requiredTypeName : requiredTypeNames) {
                    // ERROR - Invalid required type name
                    validateLocalName(requiredTypeName, Messages.requiredTypeName, status);

                    if (!Utils.isEmpty(requiredTypeName)) {
                        // ERROR - Duplicate required type name
                        if (names.contains(requiredTypeName)) {
                            status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateRequiredType, childNodeName,
                                                                                    requiredTypeName)));
                        } else {
                            names.add(requiredTypeName);
                        }
                    }
                }

                // ERROR - Cannot have explicit required types when required types is marked as a variant
                if (childNodeDefinition.getState(ChildNodeDefinition.PropertyName.REQUIRED_TYPES) != Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.requiredTypesExistButMarkedAsVariant,
                                                                            childNodeName)));
                }
            }
        }

        { // default type
            final String defaultType = childNodeDefinition.getDefaultPrimaryTypeName();

            if (childNodeDefinition.getState(ChildNodeDefinition.PropertyName.DEFAULT_TYPE) == Value.IS) {
                // ERROR - Invalid default type name
                validateLocalName(defaultType, Messages.defaultTypeName, status);
            } else if (!Utils.isEmpty(defaultType)) {
                // ERROR - Cannot have explicit default type when default type is marked as a variant
                status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.defaultTypeExistsButMarkedAsVariant, childNodeName)));
            }
        }

        return status;
    }

    /**
     * @param childNodeDefinition the child node definition being validated (cannot be <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateChildNodeDefinition( final ChildNodeDefinition childNodeDefinition,
                                                    final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateChildNodeDefinition(childNodeDefinition);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param cnd the CND being validated (cannot be <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateCnd( final CompactNodeTypeDefinition cnd ) {
        Utils.isNotNull(cnd, "cnd"); //$NON-NLS-1$

        /**
         * <pre>
         *     WARNING - No namespace declarations or node type definitions exist
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();
        boolean noNamespaceMappings = false;
        boolean noNodeTypeDefinitions = false;

        { // namespace mappings
            final List<NamespaceMapping> namespaceMappings = cnd.getNamespaceMappings();

            if (Utils.isEmpty(namespaceMappings)) {
                noNamespaceMappings = true;
            } else {
                validateNamespaceMappings(namespaceMappings, status);
            }
        }

        { // node type definitions
            final List<NodeTypeDefinition> nodeTypeDefinitions = cnd.getNodeTypeDefinitions();

            if (Utils.isEmpty(nodeTypeDefinitions)) {
                noNodeTypeDefinitions = true;
            } else {
                validateNodeTypeDefinitions(nodeTypeDefinitions, status);
            }
        }

        // WARNING - No namespace declarations or node type definitions exist
        if (noNamespaceMappings && noNodeTypeDefinitions) {
            status.add(ValidationStatus.createWarningMessage(Messages.cndHasNoNamespacesOrNodeTypeDefinitions));
        }

        return status;
    }

    /**
     * @param cnd the CND being validated (cannot be <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateCnd( final CompactNodeTypeDefinition cnd,
                                    final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateCnd(cnd);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param localName the local name being validated (cannot be <code>null</code>)
     * @param propertyName the name to use in the validation message (cannot be <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateLocalName( final String localName,
                                                      final String propertyName ) {
        Utils.verifyIsNotEmpty(propertyName, propertyName);

        if (Utils.isEmpty(localName)) {
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyLocalName, propertyName));
        }

        // ERROR Local name cannot be self or parent
        if (localName.equals(".") || localName.equals("..")) { //$NON-NLS-1$ //$NON-NLS-2$
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.localNameEqualToSelfOrParent, propertyName));
        }

        for (final char c : localName.toCharArray()) {
            switch (c) {
            case '/':
            case ':':
            case '[':
            case ']':
            case '|':
            case '*':
                // ERROR invalid character
                return ValidationStatus.createErrorMessage(NLS.bind(Messages.localNameHasInvalidCharacters, propertyName));
            default:
                continue;
            }
        }

        return ValidationStatus.OK_STATUS;
    }

    /**
     * @param localName the local name being validated (cannot be <code>null</code>)
     * @param propertyName the name to use in the validation message (cannot be <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateLocalName( final String localName,
                                          final String propertyName,
                                          final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateLocalName(localName, propertyName);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param namespaceMapping the namespace mapping being validated (cannot be <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateNamespaceMapping( final NamespaceMapping namespaceMapping ) {
        Utils.isNotNull(namespaceMapping, "namespaceMapping"); //$NON-NLS-1$

        /**
         * <pre>
         *     ERROR - Empty or invalid prefix
         *     ERROR - Empty or invalid URI
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();

        // ERROR - Empty or invalid prefix
        validateLocalName(namespaceMapping.getPrefix(), Messages.namespacePrefix, status);

        // ERROR - Empty or invalid URI
        validateLocalName(namespaceMapping.getUri(), Messages.namespaceUri, status);

        return status;
    }

    /**
     * @param namespaceMapping the namespace mapping being validated (cannot be <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateNamespaceMapping( final NamespaceMapping namespaceMapping,
                                                 final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateNamespaceMapping(namespaceMapping);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param namespaceMappings the collection of namespace mappings being validated (can be <code>null</code> or empty)
     * @return the status (never <code>null</code>)
     */
    public static MultiValidationStatus validateNamespaceMappings( final List<NamespaceMapping> namespaceMappings ) {
        /**
         * <pre>
         *     ERROR - Duplicate namespace mapping prefix
         *     ERROR - Duplicate namespace mapping URI
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();

        // OK not to have namespaces
        if (Utils.isEmpty(namespaceMappings)) {
            return status;
        }

        final List<String> prefixes = new ArrayList<String>(namespaceMappings.size());
        final List<String> uris = new ArrayList<String>(namespaceMappings.size());

        for (final NamespaceMapping namespaceMapping : namespaceMappings) {
            validateNamespaceMapping(namespaceMapping, status);

            { // ERROR - Duplicate namespace mapping prefix
                final String prefix = namespaceMapping.getPrefix();

                if (!Utils.isEmpty(prefix)) {
                    if (prefixes.contains(prefix)) {
                        status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateNamespacePrefix, prefix)));
                    } else {
                        prefixes.add(prefix);
                    }
                }
            }

            { // ERROR - Duplicate namespace mapping URI
                final String uri = namespaceMapping.getUri();

                if (!Utils.isEmpty(uri)) {
                    if (uris.contains(uri)) {
                        status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateNamespaceUri, uri)));
                    } else {
                        uris.add(uri);
                    }
                }
            }
        }

        return status;
    }

    /**
     * @param namespaceMappings the collection of namespace mappings being validated (can be <code>null</code> or empty)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateNamespaceMappings( final List<NamespaceMapping> namespaceMappings,
                                                  final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateNamespaceMappings(namespaceMappings);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param nodeTypeDefinition the node type definition being validated (cannot be <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateNodeTypeDefinition( final NodeTypeDefinition nodeTypeDefinition ) {
        Utils.isNotNull(nodeTypeDefinition, "nodeTypeDefinition"); //$NON-NLS-1$

        /**
         * <pre>
         *     WARNING - No property definitions or child node definitions exist
         *     ERROR - Empty or invalid node type definition name
         *     ERROR - Duplicate super type name
         *     ERROR - Invalid super type name
         *     ERROR - Cannot have explicit super types when super types is marked as a variant
         *     ERROR - Empty or invalid primary item name
         *     ERROR - Cannot have a primary item name when the primary item node type attribute is marked as a variant
         *     ERROR - Duplicate property definition names
         *     ERROR - Duplicate child node definition names
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();
        String nodeTypeDefinitionName = nodeTypeDefinition.getName();

        if (Utils.isEmpty(nodeTypeDefinitionName)) {
            nodeTypeDefinitionName = Messages.missingName;
        }

        { // name
          // ERROR - Empty or invalid node type definition name
            validateLocalName(nodeTypeDefinition.getName(), Messages.nodeTypeDefinitionName, status);
        }

        { // super types
            final String[] superTypeNames = nodeTypeDefinition.getDeclaredSupertypeNames();

            if (Utils.isEmpty(superTypeNames)) {
                if (nodeTypeDefinition.getState(NodeTypeDefinition.PropertyName.SUPERTYPES) == Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.emptySuperTypes, nodeTypeDefinitionName)));
                }
            } else {
                final List<String> names = new ArrayList<String>(superTypeNames.length);

                for (final String superTypeName : superTypeNames) {
                    // ERROR - Invalid super type name
                    validateLocalName(superTypeName, Messages.superTypeName, status);

                    if (!Utils.isEmpty(superTypeName)) {
                        // ERROR - Duplicate super type name
                        if (names.contains(superTypeName)) {
                            status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateSuperType,
                                                                                    nodeTypeDefinitionName, superTypeName)));
                        } else {
                            names.add(superTypeName);
                        }
                    }
                }

                // ERROR - Cannot have explicit super types when super types is marked as a variant
                if (nodeTypeDefinition.getState(NodeTypeDefinition.PropertyName.SUPERTYPES) != Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.superTypesExistButMarkedAsVariant,
                                                                            nodeTypeDefinitionName)));
                }
            }
        }

        { // primary item
            final String primaryItemName = nodeTypeDefinition.getPrimaryItemName();

            if (nodeTypeDefinition.getState(NodeTypeDefinition.PropertyName.PRIMARY_ITEM) == Value.IS) {
                // ERROR - Empty or invalid primary item name
                validateLocalName(primaryItemName, Messages.primaryItemName, status);
            } else if (!Utils.isEmpty(primaryItemName)) {
                // ERROR Cannot have a primary item name when the primary item node type attribute is marked as a variant
                status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.primaryItemExistsButMarkedAsVariant,
                                                                        nodeTypeDefinitionName)));
            }
        }

        boolean noPropertyDefinitions = false;
        boolean noChildNodeDefinitions = false;

        { // property definitions
            final List<PropertyDefinition> propertyDefinitions = nodeTypeDefinition.getPropertyDefinitions();

            if (Utils.isEmpty(propertyDefinitions)) {
                noPropertyDefinitions = true;
            } else {
                final List<String> propNames = new ArrayList<String>(propertyDefinitions.size());

                for (final PropertyDefinition propertyDefn : propertyDefinitions) {
                    validatePropertyDefinition(propertyDefn, status);

                    { // ERROR - Duplicate property definition names
                        final String propName = propertyDefn.getName();

                        if (!Utils.isEmpty(propName)) {
                            if (propNames.contains(propName)) {
                                status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicatePropertyDefinitionName,
                                                                                        nodeTypeDefinitionName, propName)));
                            } else {
                                propNames.add(propName);
                            }
                        }
                    }
                }
            }
        }

        { // child node definitions
            final List<ChildNodeDefinition> childNodeDefinitions = nodeTypeDefinition.getChildNodeDefinitions();

            if (Utils.isEmpty(childNodeDefinitions)) {
                noChildNodeDefinitions = true;
            } else {
                final List<String> childNodeNames = new ArrayList<String>(childNodeDefinitions.size());

                for (final ChildNodeDefinition childNodeDefn : childNodeDefinitions) {
                    validateChildNodeDefinition(childNodeDefn, status);

                    { // ERROR - Duplicate child node definition names
                        final String childNodeName = childNodeDefn.getName();

                        if (!Utils.isEmpty(childNodeName)) {
                            if (childNodeNames.contains(childNodeName)) {
                                status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateChildNodeDefinitionName,
                                                                                        nodeTypeDefinitionName, childNodeName)));
                            } else {
                                childNodeNames.add(childNodeName);
                            }
                        }
                    }
                }
            }
        }

        // WARNING - No property definitions or child node definitions exist
        if (noPropertyDefinitions && noChildNodeDefinitions) {
            status.add(ValidationStatus.createWarningMessage(Messages.nodeTypeDefinitionHasNoPropertyDefinitionsOrChildNodeDefinitions));
        }

        return status;
    }

    /**
     * @param nodeTypeDefinition the node type definition being validated (cannot be <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateNodeTypeDefinition( final NodeTypeDefinition nodeTypeDefinition,
                                                   final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateNodeTypeDefinition(nodeTypeDefinition);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param nodeTypeDefinitions the collection of namespace mappings to validate (can be <code>null</code> or empty)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateNodeTypeDefinitions( final List<NodeTypeDefinition> nodeTypeDefinitions ) {
        /**
         * <pre>
         *     ERROR - Duplicate node type definition names
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();

        // OK not to have node type definitions
        if (Utils.isEmpty(nodeTypeDefinitions)) {
            return status;
        }

        final List<String> names = new ArrayList<String>(nodeTypeDefinitions.size());

        for (final NodeTypeDefinition nodeTypeDefinition : nodeTypeDefinitions) {
            validateNodeTypeDefinition(nodeTypeDefinition, status);

            { // ERROR - Duplicate node type definition names
                final String name = nodeTypeDefinition.getName();

                if (!Utils.isEmpty(name)) {
                    if (names.contains(name)) {
                        status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateNodeTypeDefinitionName, name)));
                    } else {
                        names.add(name);
                    }
                }
            }
        }

        return status;
    }

    /**
     * @param nodeTypeDefinitions the collection of namespace mappings to validate (can be <code>null</code> or empty)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateNodeTypeDefinitions( final List<NodeTypeDefinition> nodeTypeDefinitions,
                                                    final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateNodeTypeDefinitions(nodeTypeDefinitions);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param propertyDefinition the property definition being validated (never <code>null</code>)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validatePropertyDefinition( final PropertyDefinition propertyDefinition ) {
        Utils.isNotNull(propertyDefinition, "propertyDefinition"); //$NON-NLS-1$

        /**
         * <pre>
         *     ERROR - Empty or invalid property definition name
         *     ERROR - Invalid property type
         *     ERROR - Cannot have multiple default values when the property definition is single-valued
         *     ERROR - Default value is not valid for the property definition type
         *     ERROR - Duplicate default value
         *     ERROR - Cannot have explicit default values when default values is marked as a variant
         *     ERROR - Invalid value constraint
         *     ERROR - Duplicate value constraint
         *     ERROR - Cannot have explicit value constraints when value constraints is marked as a variant
         *     ERROR - Invalid query operator
         *     ERROR - Duplicate query operator
         *     ERROR - Cannot have explicit query operators when query operators is marked as a variant
         * </pre>
         */

        final MultiValidationStatus status = new MultiValidationStatus();
        String propertyDefinitionName = propertyDefinition.getName();

        if (Utils.isEmpty(propertyDefinitionName)) {
            propertyDefinitionName = Messages.missingName;
        }

        { // name
          // ERROR - Empty or invalid property definition name
            validateLocalName(propertyDefinition.getName(), Messages.propertyDefinitionName, status);
        }

        { // property type
          // ERROR - Invalid property type
          // no validation needed since type is an enum
        }

        { // default values
            final List<String> defaultValues = propertyDefinition.getDefaultValuesAsStrings();

            if (Utils.isEmpty(defaultValues)) {
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.DEFAULT_VALUES) == Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyDefaultValues, propertyDefinitionName)));
                }
            } else {
                // ERROR - Cannot have multiple default values when the property definition is single-valued
                if ((defaultValues.size() > 1)
                        && (propertyDefinition.getState(PropertyDefinition.PropertyName.MULTIPLE) == Value.IS_NOT)) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.multipleDefaultValuesForSingleValuedProperty,
                                                                            propertyDefinitionName)));
                }

                final List<String> values = new ArrayList<String>(defaultValues.size());

                for (final String defaultValue : defaultValues) {
                    // ERROR - Default value is not valid for the property definition type
                    isValid(defaultValue, propertyDefinition.getType(), Messages.defaultValue, status);

                    if (!Utils.isEmpty(defaultValue)) {
                        // ERROR - Duplicate default value
                        if (values.contains(defaultValue)) {
                            status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateDefaultValue,
                                                                                    propertyDefinitionName, defaultValue)));
                        } else {
                            values.add(defaultValue);
                        }
                    }
                }

                // ERROR - Cannot have explicit default values when default values is marked as a variant
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.DEFAULT_VALUES) != Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.defaultValuesExistButMarkedAsVariant,
                                                                            propertyDefinition)));
                }
            }
        }

        { // value constraints
            final String[] valueConstraints = propertyDefinition.getValueConstraints();

            if (Utils.isEmpty(valueConstraints)) {
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.VALUE_CONSTRAINTS) == Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyValueConstraints, propertyDefinitionName)));
                }
            } else {
                final List<String> constraints = new ArrayList<String>(valueConstraints.length);

                for (final String constraint : valueConstraints) {
                    // ERROR - Invalid value constraint
                    validateValueConstraint(constraint, status);

                    if (!Utils.isEmpty(constraint)) {
                        // ERROR - Duplicate value constraint
                        if (constraints.contains(constraint)) {
                            status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateValueConstraint,
                                                                                    propertyDefinitionName, constraint)));
                        } else {
                            constraints.add(constraint);
                        }
                    }
                }

                // ERROR - Cannot have explicit value constraints when value constraints is marked as a variant
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.VALUE_CONSTRAINTS) != Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.valueConstraintsExistButMarkedAsVariant,
                                                                            propertyDefinition)));
                }
            }
        }

        { // query operators
            final String[] queryOperators = propertyDefinition.getAvailableQueryOperators();

            if (Utils.isEmpty(queryOperators)) {
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.QUERY_OPS) == Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyQueryOperators, propertyDefinitionName)));
                }
            } else {
                final List<String> operators = new ArrayList<String>(queryOperators.length);

                for (final String operator : queryOperators) {
                    // ERROR - Invalid query operator
                    validateQueryOperator(operator, status);

                    if (!Utils.isEmpty(operator)) {
                        // ERROR - Duplicate query operator
                        if (operators.contains(operator)) {
                            status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.duplicateQueryOperator,
                                                                                    propertyDefinitionName, operator)));
                        } else {
                            operators.add(operator);
                        }
                    }
                }

                // ERROR - Cannot have explicit query operators when query operators is marked as a variant
                if (propertyDefinition.getState(PropertyDefinition.PropertyName.QUERY_OPS) != Value.IS) {
                    status.add(ValidationStatus.createErrorMessage(NLS.bind(Messages.queryOperatorsExistButMarkedAsVariant,
                                                                            propertyDefinition)));
                }
            }
        }

        return status;
    }

    /**
     * @param propertyDefinition the property definition being validated (never <code>null</code>)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validatePropertyDefinition( final PropertyDefinition propertyDefinition,
                                                   final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validatePropertyDefinition(propertyDefinition);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param operator the query operator being validated (can be <code>null</code> or empty)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateQueryOperator( final String operator ) {
        Utils.verifyIsNotEmpty(operator, "operator"); //$NON-NLS-1$

        try {
            QueryOperator.find(operator);
        } catch (final Exception e) {
            return ValidationStatus.createErrorMessage(Messages.invalidQueryOperator);
        }

        return ValidationStatus.OK_STATUS;
    }

    /**
     * @param operator the query operator being validated (can be <code>null</code> or empty)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateQueryOperator( final String operator,
                                              final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateQueryOperator(operator);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * @param uri the URI being checked (can be <code>null</code> or empty)
     * @param propertyName the name to use to identify the URI (cannot be <code>null</code> empty)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateUri( final String uri,
                                                final String propertyName ) {
        Utils.verifyIsNotEmpty(propertyName, "propertyName"); //$NON-NLS-1$

        if (Utils.isEmpty(uri) || uri.contains(" ")) { //$NON-NLS-1$
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.emptyValue, propertyName));
        }

        try {
            URI.create(uri);
        } catch (final Exception e) {
            return ValidationStatus.createErrorMessage(NLS.bind(Messages.invalidUri, propertyName));
        }

        return ValidationStatus.OK_STATUS;
    }

    /**
     * @param constraint the value constraint being validated (cannot be <code>null</code> or empty)
     * @return the status (never <code>null</code>)
     */
    public static ValidationStatus validateValueConstraint( final String constraint ) {
        Utils.verifyIsNotEmpty(constraint, "constraint"); //$NON-NLS-1$

        // TODO implement
        return null;
    }

    /**
     * @param constraint the value constraint being validated (cannot be <code>null</code> or empty)
     * @param status the status to add the new status to (never <code>null</code>)
     */
    public static void validateValueConstraint( final String constraint,
                                                final MultiValidationStatus status ) {
        final ValidationStatus newStatus = validateValueConstraint(constraint);

        if (!newStatus.isOk()) {
            status.add(newStatus);
        }
    }

    /**
     * Don't allow construction.
     */
    private CndValidator() {
        // nothing to do
    }

}
