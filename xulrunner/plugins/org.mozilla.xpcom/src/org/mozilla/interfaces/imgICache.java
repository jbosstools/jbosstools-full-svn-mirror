/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/modules/libpr0n/public/imgICache.idl
 */

package org.mozilla.interfaces;

/**
 * imgICache interface
 *
 * @author Stuart Parmenter <pavlov@netscape.com>
 * @version 0.1
 * @see imagelib2
 */
public interface imgICache extends nsISupports {

  String IMGICACHE_IID =
    "{f1b74aae-5661-4753-a21c-66dd644afebc}";

  /**
   * Evict images from the cache.
   *
   * @param chrome If TRUE,  evict only chrome images.
   *               If FALSE, evict everything except chrome images.
   */
  void clearCache(boolean chrome);

  /**
   * Evict images from the cache.
   *
   * @param uri The URI to remove.
   * @return NS_OK if \a uri was removed from the cache.
   *         NS_ERROR_NOT_AVAILABLE if \a uri was unable to be removed from the cache.
   */
  void removeEntry(nsIURI uri);

  /**
   * Find Properties
   * Used to get properties such as 'type' and 'content-disposition'
   * 'type' is a nsISupportsCString containing the images' mime type such as 'image/png'
   * 'content-disposition' will be a nsISupportsCString containing the header
   * If you call this before any data has been loaded from a URI, it will succeed,
   * but come back empty.
   *
   * @param uri The URI to look up.
   * @returns NULL if the URL was not found in the cache
   */
  nsIProperties findEntryProperties(nsIURI uri);

}