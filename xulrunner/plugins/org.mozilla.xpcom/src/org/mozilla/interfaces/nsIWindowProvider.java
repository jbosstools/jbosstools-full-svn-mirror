/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/embedding/base/nsIWindowProvider.idl
 */

package org.mozilla.interfaces;

/**
 * The nsIWindowProvider interface exists so that the window watcher's default
 * behavior of opening a new window can be easly modified.  When the window
 * watcher needs to open a new window, it will first check with the
 * nsIWindowProvider it gets from the parent window.  If there is no provider
 * or the provider does not provide a window, the window watcher will proceed
 * to actually open a new window.
 */
public interface nsIWindowProvider extends nsISupports {

  String NS_IWINDOWPROVIDER_IID =
    "{5119ac7f-81dd-4061-96a7-71f2cf5efee4}";

  /**
   * A method to request that this provider provide a window.  The window
   * returned need not to have the right name or parent set on it; setting
   * those is the caller's responsibility.  The provider can always return null
   * to have the caller create a brand-new window.
   *
   * @param aParent Must not be null.  This is the window that the caller wants
   *                to use as the parent for the new window.  Generally,
   *                nsIWindowProvider implementors can expect to be somehow
   *                related to aParent; the relationship may depend on the
   *                nsIWindowProvider implementation.
   * @param aChromeFlags The chrome flags the caller will use to create a new
   *                      window if this provider returns null.  See
   *                      nsIWebBrowserChrome for the possible values of this
   *                      field.
   * @param aPositionSpecified Whether the attempt to create a window is trying
   *                           to specify a position for the new window.
   * @param aSizeSpecified Whether the attempt to create a window is trying to
   *                       specify a size for the new window.
   * @param aURI The URI to be loaded in the new window.  The nsIWindowProvider
   *             implementation MUST NOT load this URI in the window it
   *             returns.  This URI is provided solely to help the
   *             nsIWindowProvider implementation make decisions; the caller
   *             will handle loading the URI in the window returned if
   *             provideWindow returns a window.  Note that the URI may be null
   *             if the load cannot be represented by a single URI (e.g. if
   *             the load has extra load flags, POST data, etc).
   * @param aName The name of the window being opened.  Setting the name on the
   *              return value of provideWindow will be handled by the caller;
   *              aName is provided solely to help the nsIWindowProvider
   *              implementation make decisions.
   * @param aFeatures The feature string for the window being opened.  This may
   *                  be empty.  The nsIWindowProvider implementation is
   *                  allowed to apply the feature string to the window it
   *                  returns in any way it sees fit.  See the nsIWindowWatcher
   *                  interface for details on feature strings.
   * @param aWindowIsNew [out] Whether the window being returned was just
   *                           created by the window provider implementation.
   *                           This can be used by callers to keep track of which
   *                           windows were opened by the user as opposed to
   *                           being opened programmatically.  This should be set
   *                           to false if the window being returned existed
   *                           before the provideWindow() call.  The value of this
   *                           out parameter is meaningless if provideWindow()
   *                           returns null.
   * @return A window the caller should use or null if the caller should just
   *         create a new window.  The returned window may be newly opened by
   *         the nsIWindowProvider implementation or may be a window that
   *         already existed.
   *
   * @see nsIWindowWatcher for more information on aFeatures.
   * @see nsIWebBrowserChrome for more information on aChromeFlags.
   */
  nsIDOMWindow provideWindow(nsIDOMWindow aParent, long aChromeFlags, boolean aPositionSpecified, boolean aSizeSpecified, nsIURI aURI, String aName, String aFeatures, boolean[] aWindowIsNew);

}