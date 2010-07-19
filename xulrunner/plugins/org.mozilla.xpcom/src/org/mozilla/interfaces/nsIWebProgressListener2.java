/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/uriloader/base/nsIWebProgressListener2.idl
 */

package org.mozilla.interfaces;

/**
 * An extended version of nsIWebProgressListener.
 */
public interface nsIWebProgressListener2 extends nsIWebProgressListener {

  String NS_IWEBPROGRESSLISTENER2_IID =
    "{dde39de0-e4e0-11da-8ad9-0800200c9a66}";

  /**
   * Notification that the progress has changed for one of the requests
   * associated with aWebProgress.  Progress totals are reset to zero when all
   * requests in aWebProgress complete (corresponding to onStateChange being
   * called with aStateFlags including the STATE_STOP and STATE_IS_WINDOW
   * flags).
   *
   * This function is identical to nsIWebProgressListener::onProgressChange,
   * except that this function supports 64-bit values.
   *
   * @param aWebProgress
   *        The nsIWebProgress instance that fired the notification.
   * @param aRequest
   *        The nsIRequest that has new progress.
   * @param aCurSelfProgress
   *        The current progress for aRequest.
   * @param aMaxSelfProgress
   *        The maximum progress for aRequest.
   * @param aCurTotalProgress
   *        The current progress for all requests associated with aWebProgress.
   * @param aMaxTotalProgress
   *        The total progress for all requests associated with aWebProgress.
   *
   * NOTE: If any progress value is unknown, then its value is replaced with -1.
   *
   * @see nsIWebProgressListener2::onProgressChange64
   */
  void onProgressChange64(nsIWebProgress aWebProgress, nsIRequest aRequest, long aCurSelfProgress, long aMaxSelfProgress, long aCurTotalProgress, long aMaxTotalProgress);

  /**
   * Notification that a refresh or redirect has been requested in aWebProgress
   * For example, via a <meta http-equiv="refresh"> or an HTTP Refresh: header
   *
   * @param aWebProgress
   *        The nsIWebProgress instance that fired the notification.
   * @param aRefreshURI
   *        The new URI that aWebProgress has requested redirecting to.
   * @param aMillis
   *        The delay (in milliseconds) before refresh.
   * @param aSameURI
   *        True if aWebProgress is requesting a refresh of the
   *        current URI.
   *        False if aWebProgress is requesting a redirection to
   *        a different URI.
   *
   * @return True if the refresh may proceed.
   *         False if the refresh should be aborted.
   */
  boolean onRefreshAttempted(nsIWebProgress aWebProgress, nsIURI aRefreshURI, int aMillis, boolean aSameURI);

}