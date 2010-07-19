/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/uriloader/base/nsIURILoader.idl
 */

package org.mozilla.interfaces;

/**
 * The uri dispatcher is responsible for taking uri's, determining
 * the content and routing the opened url to the correct content 
 * handler. 
 *
 * When you encounter a url you want to open, you typically call 
 * openURI, passing it the content listener for the window the uri is 
 * originating from. The uri dispatcher opens the url to discover the 
 * content type. It then gives the content listener first crack at 
 * handling the content. If it doesn't want it, the dispatcher tries
 * to hand it off one of the registered content listeners. This allows
 * running applications the chance to jump in and handle the content.
 *
 * If that also fails, then the uri dispatcher goes to the registry
 * looking for the preferred content handler for the content type
 * of the uri. The content handler may create an app instance
 * or it may hand the contents off to a platform specific plugin
 * or helper app. Or it may hand the url off to an OS registered 
 * application. 
 */
public interface nsIURILoader extends nsISupports {

  String NS_IURILOADER_IID =
    "{2f7e8051-f1c9-4bcc-8584-9cfd5849e343}";

  /**
   * @name Flags for opening URIs.
   */
/**
   * Should the content be displayed in a container that prefers the
   * content-type, or will any container do.
   */
  long IS_CONTENT_PREFERRED = 1L;

  /**
   * If this flag is set, only the listener of the specified window context will
   * be considered for content handling; if it refuses the load, an error will
   * be indicated.
   */
  long DONT_RETARGET = 2L;

  /**
   * As applications such as messenger and the browser are instantiated,
   * they register content listener's with the uri dispatcher corresponding
   * to content windows within that application. 
   *
   * Note to self: we may want to optimize things a bit more by requiring
   * the content types the registered content listener cares about.
   *
   * @param aContentListener
   *        The listener to register. This listener must implement
   *        nsISupportsWeakReference.
   *
   * @see the nsIURILoader class description
   */
  void registerContentListener(nsIURIContentListener aContentListener);

  void unRegisterContentListener(nsIURIContentListener aContentListener);

  /**
   * OpenURI requires the following parameters.....
   * @param aChannel
   *        The channel that should be opened. This must not be asyncOpen'd yet!
   *        If a loadgroup is set on the channel, it will get replaced with a
   *        different one.
   * @param aIsContentPreferred
   *        Should the content be displayed in a container that prefers the
   *        content-type, or will any container do.
   * @param aWindowContext
   *        If you are running the url from a doc shell or a web shell, this is
   *        your window context. If you have a content listener you want to
   *        give first crack to, the uri loader needs to be able to get it
   *        from the window context. We will also be using the window context
   *        to get at the progress event sink interface.
   *        <b>Must not be null!</b>
   */
  void openURI(nsIChannel aChannel, boolean aIsContentPreferred, nsIInterfaceRequestor aWindowContext);

  /**
   * Loads data from a channel. This differs from openURI in that the channel
   * may already be opened, and that it returns a stream listener into which the
   * caller should pump data. The caller is responsible for opening the channel
   * and pumping the channel's data into the returned stream listener.
   *
   * Note: If the channel already has a loadgroup, it will be replaced with the
   * window context's load group, or null if the context doesn't have one.
   *
   * If the window context's nsIURIContentListener refuses the load immediately
   * (e.g. in nsIURIContentListener::onStartURIOpen), this method will return
   * NS_ERROR_WONT_HANDLE_CONTENT. At that point, the caller should probably
   * cancel the channel if it's already open (this method will not cancel the
   * channel).
   *
   * If flags include DONT_RETARGET, and the content listener refuses the load
   * during onStartRequest (e.g. in canHandleContent/isPreferred), then the
   * returned stream listener's onStartRequest method will return
   * NS_ERROR_WONT_HANDLE_CONTENT.
   *
   * @param aChannel
   *        The channel that should be loaded. The channel may already be
   *        opened. It must not be closed (i.e. this must be called before the
   *        channel calls onStopRequest on its stream listener).
   * @param aFlags
   *        Combination (bitwise OR) of the flags specified above. 0 indicates
   *        default handling.
   * @param aWindowContext
   *        If you are running the url from a doc shell or a web shell, this is
   *        your window context. If you have a content listener you want to
   *        give first crack to, the uri loader needs to be able to get it
   *        from the window context. We will also be using the window context
   *        to get at the progress event sink interface.
   *        <b>Must not be null!</b>
   */
  nsIStreamListener openChannel(nsIChannel aChannel, long aFlags, nsIInterfaceRequestor aWindowContext);

  /**
   * Stops an in progress load
   */
  void stop(nsISupports aLoadCookie);

}