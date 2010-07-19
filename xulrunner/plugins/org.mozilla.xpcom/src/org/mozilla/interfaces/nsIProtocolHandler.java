/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/netwerk/base/public/nsIProtocolHandler.idl
 */

package org.mozilla.interfaces;

/**
 * nsIProtocolHandler
 *
 * @status FROZEN
 */
public interface nsIProtocolHandler extends nsISupports {

  String NS_IPROTOCOLHANDLER_IID =
    "{15fd6940-8ea7-11d3-93ad-00104ba0fd40}";

  /**
     * The scheme of this protocol (e.g., "file").
     */
  String getScheme();

  /** 
     * The default port is the port that this protocol normally uses.
     * If a port does not make sense for the protocol (e.g., "about:")
     * then -1 will be returned.
     */
  int getDefaultPort();

  /**
     * Returns the protocol specific flags (see flag definitions below).  
     */
  long getProtocolFlags();

  /**
     * Makes a URI object that is suitable for loading by this protocol,
     * where the URI string is given as an UTF-8 string.  The caller may
     * provide the charset from which the URI string originated, so that
     * the URI string can be translated back to that charset (if necessary)
     * before communicating with, for example, the origin server of the URI
     * string.  (Many servers do not support UTF-8 IRIs at the present time,
     * so we must be careful about tracking the native charset of the origin
     * server.)
     *
     * @param aSpec          - the URI string in UTF-8 encoding. depending
     *                         on the protocol implementation, unicode character
     *                         sequences may or may not be %xx escaped.
     * @param aOriginCharset - the charset of the document from which this URI
     *                         string originated.  this corresponds to the
     *                         charset that should be used when communicating
     *                         this URI to an origin server, for example.  if
     *                         null, then UTF-8 encoding is assumed (i.e.,
     *                         no charset transformation from aSpec).
     * @param aBaseURI       - if null, aSpec must specify an absolute URI.
     *                         otherwise, aSpec may be resolved relative
     *                         to aBaseURI, depending on the protocol. 
     *                         If the protocol has no concept of relative 
     *                         URI aBaseURI will simply be ignored.
     */
  nsIURI newURI(String aSpec, String aOriginCharset, nsIURI aBaseURI);

  /**
     * Constructs a new channel from the given URI for this protocol handler. 
     */
  nsIChannel newChannel(nsIURI aURI);

  /**
     * Allows a protocol to override blacklisted ports.
     *
     * This method will be called when there is an attempt to connect to a port 
     * that is blacklisted.  For example, for most protocols, port 25 (Simple Mail
     * Transfer) is banned.  When a URI containing this "known-to-do-bad-things" 
     * port number is encountered, this function will be called to ask if the 
     * protocol handler wants to override the ban.  
     */
  boolean allowPort(int port, String scheme);

  /**************************************************************************
     * Constants for the protocol flags (the first is the default mask, the
     * others are deviations):
     *
     * NOTE: Implementation must ignore any flags they do not understand.
     */
/**
     * standard full URI with authority component and concept of relative
     * URIs (http, ftp, ...)
     */
  long URI_STD = 0L;

  /**
     * no concept of relative URIs (about, javascript, finger, ...)
     */
  long URI_NORELATIVE = 1L;

  /**
     * no authority component (file, ...)
     */
  long URI_NOAUTH = 2L;

  /**
     * The URIs for this protocol have no inherent security context, so
     * documents loaded via this protocol should inherit the security context
     * from the document that loads them.
     */
  long URI_INHERITS_SECURITY_CONTEXT = 16L;

  /**
     * "Automatic" loads that would replace the document (e.g. <meta> refresh,
     * certain types of XLinks, possibly other loads that the application
     * decides are not user triggered) are not allowed if the originating (NOT
     * the target) URI has this protocol flag.  Note that the decision as to
     * what constitutes an "automatic" load is made externally, by the caller
     * of nsIScriptSecurityManager::CheckLoadURI.  See documentation for that
     * method for more information.
     *
     * A typical protocol that might want to set this flag is a protocol that
     * shows highly untrusted content in a viewing area that the user expects
     * to have a lot of control over, such as an e-mail reader.
     */
  long URI_FORBIDS_AUTOMATIC_DOCUMENT_REPLACEMENT = 32L;

  /**
     * +-------------------------------------------------------------------+
     * |                                                                   |
     * |  ALL PROTOCOL HANDLERS MUST SET ONE OF THE FOLLOWING FOUR FLAGS.  |
     * |                                                                   |
     * +-------------------------------------------------------------------+
     *
     * These flags are used to determine who is allowed to load URIs for this
     * protocol.  Note that if a URI is nested, only the flags for the
     * innermost URI matter.  See nsINestedURI.
     *
     * If none of these four flags are set, the URI must be treated as if it
     * had the URI_LOADABLE_BY_ANYONE flag set, for compatibility with protocol
     * handlers written against Gecko 1.8 or earlier.  In this case, there may
     * be run-time warning messages indicating that a "default insecure"
     * assumption is being made.  At some point in the futures (Mozilla 2.0,
     * most likely), these warnings will become errors.
     */
/**
     * The URIs for this protocol can be loaded by anyone.  For example, any
     * website should be allowed to trigger a load of a URI for this protocol.
     * Web-safe protocols like "http" should set this flag.
     */
  long URI_LOADABLE_BY_ANYONE = 64L;

  /**
     * The URIs for this protocol are UNSAFE if loaded by untrusted (web)
     * content and may only be loaded by privileged code (for example, code
     * which has the system principal).  Various internal protocols should set
     * this flag.
     */
  long URI_DANGEROUS_TO_LOAD = 128L;

  /**
     * The URIs for this protocol point to resources that are part of the
     * application's user interface.  There are cases when such resources may
     * be made accessible to untrusted content such as web pages, so this is
     * less restrictive than URI_DANGEROUS_TO_LOAD but more restrictive than
     * URI_LOADABLE_BY_ANYONE.  See the documentation for
     * nsIScriptSecurityManager::CheckLoadURI.
     */
  long URI_IS_UI_RESOURCE = 256L;

  /**
     * Loading of URIs for this protocol from other origins should only be
     * allowed if those origins should have access to the local filesystem.
     * It's up to the application to decide what origins should have such
     * access.  Protocols like "file" that point to local data should set this
     * flag.
     */
  long URI_IS_LOCAL_FILE = 512L;

  /**
     * Loading channels from this protocol has side-effects that make
     * it unsuitable for saving to a local file.
     */
  long URI_NON_PERSISTABLE = 1024L;

  /**
     * Channels using this protocol never call OnDataAvailable
     * on the listener passed to AsyncOpen and they therefore
     * do not return any data that we can use.
     */
  long URI_DOES_NOT_RETURN_DATA = 2048L;

  /**
     * URIs for this protocol are considered to be local resources.  This could
     * be a local file (URI_IS_LOCAL_FILE), a UI resource (URI_IS_UI_RESOURCE),
     * or something else that would not hit the network.
     */
  long URI_IS_LOCAL_RESOURCE = 4096L;

  /**
     * URIs for this protocol execute script when they are opened.
     */
  long URI_OPENING_EXECUTES_SCRIPT = 8192L;

  /**
     * This protocol handler can be proxied via a proxy (socks or http)
     * (e.g., irc, smtp, http, etc.).  If the protocol supports transparent
     * proxying, the handler should implement nsIProxiedProtocolHandler.
     *
     * If it supports only HTTP proxying, then it need not support
     * nsIProxiedProtocolHandler, but should instead set the ALLOWS_PROXY_HTTP
     * flag (see below).
     *
     * @see nsIProxiedProtocolHandler
     */
  long ALLOWS_PROXY = 4L;

  /**
     * This protocol handler can be proxied using a http proxy (e.g., http,
     * ftp, etc.).  nsIIOService::newChannelFromURI will feed URIs from this
     * protocol handler to the HTTP protocol handler instead.  This flag is
     * ignored if ALLOWS_PROXY is not set.
     */
  long ALLOWS_PROXY_HTTP = 8L;

}