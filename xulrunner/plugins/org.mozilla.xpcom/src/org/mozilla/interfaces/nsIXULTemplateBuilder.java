/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/content/xul/templates/public/nsIXULTemplateBuilder.idl
 */

package org.mozilla.interfaces;

/**
 * A template builder, given an input source of data, a template, and a
 * reference point, generates a list of results from the input, and copies
 * part of the template for each result. Templates may generate content
 * recursively, using the same template, but with the previous iteration's
 * results as the reference point. As an example, for an XML datasource the
 * initial reference point would be a specific node in the DOM tree and a
 * template might generate a list of all child nodes. For the next iteration,
 * those children would be used to generate output for their child nodes and
 * so forth.
 *
 * A template builder is attached to a single DOM node; this node is called
 * the root node and is expected to contain a XUL template element as a direct
 * child. Different template builders may be specialized in the manner in
 * which they generate and display the resulting content from the template.
 *
 * The structure of a template is as follows:
 *
 * <rootnode datasources="" ref="">
 *   <template>
 *     <queryset>
 *       <query>
 *       </query>
 *       <rule>
 *         <conditions>...</conditions>
 *         <bindings>...</bindings>
 *         <action>...</action>
 *       </rule>
 *     </queryset>
 *   </template>
 * </rootnode>
 *
 * The datasources attribute on the root node is used to identify the source
 * of data to be used. The ref attribute is used to specify the reference
 * point for the query. Currently, the datasource will either be an
 * nsIRDFDataSource or a DOM node. In the future, other datasource types may
 * be used.
 *
 * The <queryset> element contains a single query and one or more <rule>
 * elements. There may be more than one <queryset> if multiple queries are
 * desired, and this element is optional if only one query is needed -- in
 * that case the <query> and <rule>s are allowed to be children of the
 * <template> node
 *
 * The contents of the query are processed by a separate component called a
 * query processor. This query processor is expected to use this query to
 * generate results when asked by the template builder. The template builder
 * then generates output for each result based on the <rule> elements.
 *
 * This allows the query processor to be specific to a particular kind of
 * input data or query syntax, while the template builder remains independent
 * of the kind of data being used. Due to this, the query processor will be
 * supplied with the datasource and query which the template builder handles
 * in an opaque way, while the query processor handles these more
 * specifically.
 *
 * Results implement the nsIXULTemplateResult interface and may be identified
 * by an id which must be unique within a given set of query results.
 *
 * Each query may be accompanied by one or more <rule> elements. These rules
 * are evaluated by the template builder for each result produced by the
 * query. A rule consists of conditions that cause a rule to be either
 * accepted or rejected. The condition syntax allows for common conditional
 * handling; additional filtering may be applied by adding a custom filter
 * to a rule with the builder's addRuleFilter method.
 *
 * If a result passes a rule's conditions, this is considered a match, and the
 * content within the rule's <action> body is inserted as a sibling of the
 * <template>, assuming the template builder creates real DOM content. Only
 * one rule will match a result. For a tree builder, for example, the content
 * within the action body is used to create the tree rows instead. A matching
 * result must have its ruleMatched method called. When a result no longer
 * matches, the result's hasBeenRemoved method must be called.
 *
 * Optionally, the rule may have a <bindings> section which may be used to
 * define additional variables to be used within an action body. Each of these
 * declared bindings must be supplied to the query processor via its
 * addBinding method. The bindings are evaluated after a rule has matched.
 *
 * Templates may generate content recursively, using the previous iteration's
 * results as reference point to invoke the same queries. Since the reference
 * point is different, different output will typically be generated.
 *
 * The reference point nsIXULTemplateResult object for the first iteration is
 * determined by calling the query processor's translateRef method using the
 * value of the root node's ref attribute. This object may be retrieved later
 * via the builder's rootResult property.
 *
 * For convenience, each reference point as well as all results implement the
 * nsIXULTemplateResult interface, allowing the result objects from each
 * iteration to be used directly as the reference points for the next
 * iteration.
 *
 * When using multiple queries, each may generate results with the same id.
 * More than one of these results may match one of the rules in their
 * respective queries, however only the result for the earliest matching query
 * in the template becomes the active match and generates output. The
 * addResult, removeResult, replaceResult and resultBindingChanged methods may
 * be called by the query processor to indicate that the set of valid results
 * has changed, such that a different query may match. If a different match
 * would become active, the content for the existing match is removed and the
 * content for the new match is generated. A query processor is not required
 * to provide any support for updating results after they have been generated.
 *
 * See http://wiki.mozilla.org/XUL:Templates_Plan for details about templates.
 */
public interface nsIXULTemplateBuilder extends nsISupports {

  String NS_IXULTEMPLATEBUILDER_IID =
    "{a583b676-5b02-4f9c-a0c9-cb850cb99818}";

  /**
     * The root node in the DOM to which this builder is attached.
     */
  nsIDOMElement getRoot();

  /**
     * The opaque datasource object that is used for the template. This object
     * is created by the getDataSource method of the query processor. May be
     * null if the datasource has not been loaded yet. Set this attribute to
     * use a different datasource and rebuild the template.
     *
     * For an RDF datasource, this will be the same as the database. For XML
     * this will be the nsIDOMNode for the datasource document or node for
     * an inline reference (such as #name). Other query processors may use
     * other types for the datasource.
     */
  nsISupports getDatasource();

  /**
     * The opaque datasource object that is used for the template. This object
     * is created by the getDataSource method of the query processor. May be
     * null if the datasource has not been loaded yet. Set this attribute to
     * use a different datasource and rebuild the template.
     *
     * For an RDF datasource, this will be the same as the database. For XML
     * this will be the nsIDOMNode for the datasource document or node for
     * an inline reference (such as #name). Other query processors may use
     * other types for the datasource.
     */
  void setDatasource(nsISupports aDatasource);

  /**
     * The composite datasource that the template builder observes
     * and uses to create content. This is used only for RDF queries and is
     * maintained for backwards compatibility. It will be the same object as
     * the datasource property. For non-RDF queries, it will always be null.
     */
  nsIRDFCompositeDataSource getDatabase();

  /**
     * The virtual result representing the starting reference point,
     * determined by calling the query processor's translateRef method
     * with the root node's ref attribute as an argument.
     */
  nsIXULTemplateResult getRootResult();

  /**
     * Force the template builder to rebuild its content. All existing content
     * will be removed first. The query processor's done() method will be
     * invoked during cleanup, followed by its initializeForBuilding method
     * when the content is to be regenerated.
     * 
     */
  void rebuild();

  /**
     * Reload any of our RDF datasources that support nsIRDFRemoteDatasource. 
     *
     * @note This is a temporary hack so that remote-XUL authors can
     *       reload remote datasources. When RDF becomes remote-scriptable,
     *       this will no longer be necessary.
     */
  void refresh();

  /**
     * Inform the template builder that a new result is available. The builder
     * will add this result to the set of results. The query node that the
     * new result applies to must be specified using the aQueryNode parameter.
     *
     * The builder will apply the rules associated with the query to the new
     * result, unless a result with the same id from an earlier query
     * supersedes it, and the result's RuleMatched method will be called if it
     * matches.
     *
     * @param aResult the result to add
     * @param aQueryNode the query that the result applies to
     *
     * @throws NS_ERROR_NULL_POINTER if aResult or aQueryNode are null
     */
  void addResult(nsIXULTemplateResult aResult, nsIDOMNode aQueryNode);

  /**
     * Inform the template builder that a result no longer applies. The builder
     * will call the remove content generated for the result, if any. If a different
     * query would then match instead, it will become the active match. This
     * method will have no effect if the result isn't known to the builder.
     *
     * @param aResult the result to remove
     *
     * @throws NS_ERROR_NULL_POINTER if aResult is null
     */
  void removeResult(nsIXULTemplateResult aResult);

  /**
     * Inform the template builder that one result should be replaced with
     * another. Both the old result (aOldResult) and the new result
     * (aNewResult) must have the same id. The query node that the new result
     * applies to must be specified using the aQueryNode parameter.
     *
     * This method is expected to have the same effect as calling both
     * removeResult for the old result and addResult for the new result.
     *
     * @param aOldResult the old result
     * @param aNewResult the new result
     * @param aQueryNode the query that the new result applies to
     *
     * @throws NS_ERROR_NULL_POINTER if either argument is null, or
     *         NS_ERROR_INVALID_ARG if the ids don't match
     */
  void replaceResult(nsIXULTemplateResult aOldResult, nsIXULTemplateResult aNewResult, nsIDOMNode aQueryNode);

  /**
     * Inform the template builder that one or more of the optional bindings
     * for a result has changed. In this case, the rules are not reapplied as
     * it is expected that the same rule will still apply. The builder will
     * resynchronize any variables that are referenced in the action body.
     *
     * @param aResult the result to change
     *
     * @throws NS_ERROR_NULL_POINTER if aResult is null
     */
  void resultBindingChanged(nsIXULTemplateResult aResult);

  /**
     * Return the result for a given id. Only one such result is returned and
     * is always the result with that id associated with the active match.
     * This method will return null is there is no result for the id.
     *
     * @param aId the id to return the result for
     */
  nsIXULTemplateResult getResultForId(String aId);

  /**
     * Retrieve the result corresponding to a generated element, or null is
     * there isn't one.
     *
     * @param aContent element to result the result of
     */
  nsIXULTemplateResult getResultForContent(nsIDOMElement aElement);

  /**
     * Returns true if the node has content generated for it. This method is
     * intended to be called only by the RDF query processor. If aTag is set,
     * the content must have a tag name that matches aTag. aTag may be ignored
     * for builders that don't generate real DOM content.
     *
     * @param aNode node to check
     * @param aTag tag that must match
     */
  boolean hasGeneratedContent(nsIRDFResource aNode, nsIAtom aTag);

  /**
     * Adds a rule filter for a given rule, which may be used for specialized
     * rule filtering. Any existing filter on the rule is removed. The default
     * conditions specified inside the <rule> tag are applied before the
     * rule filter is applied, meaning that the filter may be used to further
     * filter out results but not reaccept results that have already been
     * rejected.
     *
     * @param aRule the rule to apply the filter to
     * @param aFilter the filter to add
     */
  void addRuleFilter(nsIDOMNode aRule, nsIXULTemplateRuleFilter aFilter);

  /**
     * Add a listener to this template builder. The template builder
     * holds a strong reference to the listener.
     */
  void addListener(nsIXULBuilderListener aListener);

  /**
     * Remove a listener from this template builder.
     */
  void removeListener(nsIXULBuilderListener aListener);

}