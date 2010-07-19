/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/parser/xml/public/nsISAXDTDHandler.idl
 */

package org.mozilla.interfaces;

/**
 * Receive notification of basic DTD-related events.
 *
 * If a SAX application needs information about notations and
 * unparsed entities, then the application implements this interface
 * and registers an instance with the SAX parser using the parser's
 * setDTDHandler method. The parser uses the instance to report
 * notation and unparsed entity declarations to the application.
 *
 * Note that this interface includes only those DTD events that the
 * XML recommendation requires processors to report: notation and
 * unparsed entity declarations.
 *
 * The SAX parser may report these events in any order, regardless
 * of the order in which the notations and unparsed entities were
 * declared; however, all DTD events must be reported after the
 * document handler's startDocument event, and before the first
 * startElement event. (If the LexicalHandler is used, these events
 * must also be reported before the endDTD event.)
 */
public interface nsISAXDTDHandler extends nsISupports {

  String NS_ISAXDTDHANDLER_IID =
    "{4d01f225-6cc5-11da-be43-001422106990}";

  /**
   * Receive notification of a notation declaration event.
   *
   * It is up to the application to record the notation for later
   * reference, if necessary; notations may appear as attribute values
   * and in unparsed entity declarations, and are sometime used with
   * processing instruction target names.
   *
   * At least one of publicId and systemId must be non-null. If a
   * system identifier is present, and it is a URL, the SAX parser
   * must resolve it fully before passing it to the application
   * through this event.
   *
   * There is no guarantee that the notation declaration will be
   * reported before any unparsed entities that use it.
   *
   * @param name The notation name.
   * @param publicId The notation's public identifier, or null if none was
   *                  given.
   * @param systemId The notation's system identifier, or null if none was
   *                  given.
   */
  void notationDecl(String name, String publicId, String systemId);

  /**
   * Receive notification of an unparsed entity declaration event.
   *
   * Note that the notation name corresponds to a notation reported
   * by the notationDecl event. It is up to the application to record
   * the entity for later reference, if necessary; unparsed entities
   * may appear as attribute values.
   *
   * If the system identifier is a URL, the parser must resolve it
   * fully before passing it to the application.
   *
   * @param name The unparsed entity's name.
   * @param publicId The entity's public identifier, or null if none was
   *                  given.
   * @param systemId The entity's system identifier, or null if none was
   *                  given.
   * @param notationName The name of the associated notation.
   */
  void unparsedEntityDecl(String name, String publicId, String systemId, String notationName);

}