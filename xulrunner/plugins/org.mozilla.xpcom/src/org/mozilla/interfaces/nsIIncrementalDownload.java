/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/netwerk/base/public/nsIIncrementalDownload.idl
 */

package org.mozilla.interfaces;

/**
 * An incremental download object attempts to fetch a file piecemeal over time
 * in an effort to minimize network bandwidth usage.
 *
 * Canceling a background download does not cause the file on disk to be
 * deleted.
 */
public interface nsIIncrementalDownload extends nsIRequest {

  String NS_IINCREMENTALDOWNLOAD_IID =
    "{6687823f-56c4-461d-93a1-7f6cb7dfbfba}";

  /**
   * Initialize the incremental download object.  If the destination file
   * already exists, then only the remaining portion of the file will be
   * fetched.
   *
   * NOTE: The downloader will create the destination file if it does not
   * already exist.  It will create the file with the permissions 0600 if
   * needed.  To affect the permissions of the file, consumers of this
   * interface may create an empty file at the specified destination prior to
   * starting the incremental download.
   *
   * NOTE: Since this class may create a temporary file at the specified
   * destination, it is advisable for the consumer of this interface to specify
   * a file name for the destination that would not tempt the user into
   * double-clicking it.  For example, it might be wise to append a file
   * extension like ".part" to the end of the destination to protect users from
   * accidentally running "blah.exe" before it is a complete file.
   *
   * @param uri
   *        The URI to fetch.
   * @param destination
   *        The location where the file is to be stored.
   * @param chunkSize
   *        The size of the chunks to fetch.  A non-positive value results in
   *        the default chunk size being used.
   * @param intervalInSeconds
   *        The amount of time to wait between fetching chunks.  Pass a
   *        negative to use the default interval, or 0 to fetch the remaining
   *        part of the file in one chunk.
   */
  void init(nsIURI uri, nsIFile destination, int chunkSize, int intervalInSeconds);

  /**
   * The URI being fetched.
   */
  nsIURI getURI();

  /**
   * The URI being fetched after any redirects have been followed.  This
   * attribute is set just prior to calling OnStartRequest on the observer
   * passed to the start method.
   */
  nsIURI getFinalURI();

  /**
   * The file where the download is being written.
   */
  nsIFile getDestination();

  /**
   * The total number of bytes for the requested file.  This attribute is set
   * just prior to calling OnStartRequest on the observer passed to the start
   * method.
   *
   * This attribute has a value of -1 if the total size is unknown.
   */
  long getTotalSize();

  /**
   * The current number of bytes downloaded so far.  This attribute is set just
   * prior to calling OnStartRequest on the observer passed to the start
   * method.
   *
   * This attribute has a value of -1 if the current size is unknown.
   */
  long getCurrentSize();

  /**
   * Start the incremental download.
   *
   * @param observer
   *        An observer to be notified of various events.  OnStartRequest is
   *        called when finalURI and totalSize have been determined or when an
   *        error occurs.  OnStopRequest is called when the file is completely
   *        downloaded or when an error occurs.  If this object implements
   *        nsIProgressEventSink, then its OnProgress method will be called as
   *        data is written to the destination file.  If this object implements
   *        nsIInterfaceRequestor, then it will be assigned as the underlying
   *        channel's notification callbacks, which allows it to provide a
   *        nsIAuthPrompt implementation if needed by the channel, for example.
   * @param ctxt
   *        User defined object forwarded to the observer's methods.
   */
  void start(nsIRequestObserver observer, nsISupports ctxt);

}