/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/toolkit/components/downloads/public/nsIDownloadManager.idl
 */

package org.mozilla.interfaces;

public interface nsIDownloadManager extends nsISupports {

  String NS_IDOWNLOADMANAGER_IID =
    "{bacca1ac-1b01-4a6f-9e91-c2ead1f7d2c0}";

  /**
   * Download type for generic file download.
   */
  short DOWNLOAD_TYPE_DOWNLOAD = 0;

  /**
   * Download state for uninitialized download object.
   */
  short DOWNLOAD_NOTSTARTED = -1;

  /**
   * Download is currently transferring data.
   */
  short DOWNLOAD_DOWNLOADING = 0;

  /**
   * Download completed including any processing of the target
   * file.  (completed)
   */
  short DOWNLOAD_FINISHED = 1;

  /**
   * Transfer failed due to error. (completed)
   */
  short DOWNLOAD_FAILED = 2;

  /**
   * Download was canceled by the user. (completed)
   */
  short DOWNLOAD_CANCELED = 3;

  /**
   * Transfer was paused by the user.
   */
  short DOWNLOAD_PAUSED = 4;

  /**
   * Download is active but data has not yet been received.
   */
  short DOWNLOAD_QUEUED = 5;

  /**
   * Transfer request was blocked by parental controls proxies. (completed)
   */
  short DOWNLOAD_BLOCKED_PARENTAL = 6;

  /**
   * Transferred download is being scanned by virus scanners.
   */
  short DOWNLOAD_SCANNING = 7;

  /**
   * A virus was detected in the download. The target will most likely
   * no longer exist. (completed)
   */
  short DOWNLOAD_DIRTY = 8;

  /**
   * Win specific: Request was blocked by zone policy settings.
   * (see bug #416683) (completed)
   */
  short DOWNLOAD_BLOCKED_POLICY = 9;

  /**
   * Creates an nsIDownload and adds it to be managed by the download manager.
   *
   * @param aSource The source URI of the transfer. Must not be null.
   *
   * @param aTarget The target URI of the transfer. Must not be null.
   *
   * @param aDisplayName The user-readable description of the transfer.
   *                     Can be empty.
   *
   * @param aMIMEInfo The MIME info associated with the target,
   *                  including MIME type and helper app when appropriate.
   *                  This parameter is optional.
   *
   * @param startTime Time when the download started
   *
   * @param aTempFile The location of a temporary file; i.e. a file in which
   *                  the received data will be stored, but which is not
   *                  equal to the target file. (will be moved to the real
   *                  target by the caller, when the download is finished)
   *                  May be null.
   *
   * @param aCancelable An object that can be used to abort the download.
   *                    Must not be null.
   *
   * @return The newly created download item with the passed-in properties.
   *
   * @note This does not actually start a download.  If you want to add and
   *       start a download, you need to create an nsIWebBrowserPersist, pass it
   *       as the aCancelable object, call this method, set the progressListener
   *       as the returned download object, then call saveURI.
   */
  nsIDownload addDownload(short aDownloadType, nsIURI aSource, nsIURI aTarget, String aDisplayName, nsIMIMEInfo aMIMEInfo, double aStartTime, nsILocalFile aTempFile, nsICancelable aCancelable);

  /**
   * Retrieves a download managed by the download manager.  This can be one that
   * is in progress, or one that has completed in the past and is stored in the
   * database.
   *
   * @param aID The unique ID of the download.
   * @return The download with the specified ID.
   * @throws NS_ERROR_NOT_AVAILABLE if the download is not in the database.
   */
  nsIDownload getDownload(long aID);

  /**
   * Cancels the download with the specified ID if it's currently in-progress.
   * This calls cancel(NS_BINDING_ABORTED) on the nsICancelable provided by the
   * download.
   *
   * @param aID The unique ID of the download.
   * @throws NS_ERROR_FAILURE if the download is not in-progress.
   */
  void cancelDownload(long aID);

  /**
   * Removes the download with the specified id if it's not currently
   * in-progress.  Whereas cancelDownload simply cancels the transfer, but
   * retains information about it, removeDownload removes all knowledge of it.
   *
   * Also notifies observers of the "download-manager-remove-download" topic
   * with the download id as the subject to allow any DM consumers to react to
   * the removal.
   *
   * @param aID The unique ID of the download.
   * @throws NS_ERROR_FAILURE if the download is active.
   */
  void removeDownload(long aID);

  /**
   * Removes all inactive downloads that were started inclusively within the
   * specified time frame.
   *
   * @param aBeginTime
   *        The start time to remove downloads by in microseconds.
   * @param aEndTime
   *        The end time to remove downloads by in microseconds.
   */
  void removeDownloadsByTimeframe(long aBeginTime, long aEndTime);

  /**
   * Pause the specified download.
   *
   * @param aID The unique ID of the download.
   * @throws NS_ERROR_FAILURE if the download is not in-progress.
   */
  void pauseDownload(long aID);

  /**
   * Resume the specified download.
   *
   * @param aID The unique ID of the download.
   * @throws NS_ERROR_FAILURE if the download is not in-progress.
   */
  void resumeDownload(long aID);

  /**
   * Retries a failed download.
   *
   * @param aID The unique ID of the download.
   * @throws NS_ERROR_NOT_AVAILALE if the download id is not known.
   * @throws NS_ERROR_FAILURE if the download is not in the following states:
   *           nsIDownloadManager::DOWNLOAD_CANCELED
   *           nsIDownloadManager::DOWNLOAD_FAILED
   */
  void retryDownload(long aID);

  /**
   * The database connection to the downloads database.
   */
  mozIStorageConnection getDBConnection();

  /** 
   * Whether or not there are downloads that can be cleaned up (removed)
   * i.e. downloads that have completed, have failed or have been canceled.
   */
  boolean getCanCleanUp();

  /** 
   * Removes completed, failed, and canceled downloads from the list.
   *
   * Also notifies observers of the "download-manager-remove-download" topic
   * with a null subject to allow any DM consumers to react to the removals.
   */
  void cleanUp();

  /** 
   * The number of files currently being downloaded.
   */
  int getActiveDownloadCount();

  /**
   * An enumeration of active nsIDownloads
   */
  nsISimpleEnumerator getActiveDownloads();

  /**
   * Adds a listener from the download manager.
   */
  void addListener(nsIDownloadProgressListener aListener);

  /**
   * Removes a listener from the download manager.
   */
  void removeListener(nsIDownloadProgressListener aListener);

  /**
   * Returns the platform default downloads directory.
   */
  nsILocalFile getDefaultDownloadsDirectory();

  /**
   * Returns the user configured downloads directory. 
   * The path is dependent on two user configurable prefs
   * set in preferences:
   *
   * browser.download.folderList
   *   Indicates the location users wish to save downloaded 
   *   files too.  
   *   Values: 
   *     0 - The desktop is the default download location. 
   *     1 - The system's downloads folder is the default download location. 
   *     2 - The default download location is elsewhere as specified in  
   *         browser.download.dir. If invalid, userDownloadsDirectory
   *         will fallback on defaultDownloadsDirectory.
   *
   * browser.download.dir - 
   *   A local path the user may have selected at some point 
   *   where downloaded files are saved. The use of which is
   *   enabled when folderList equals 2. 
   */
  nsILocalFile getUserDownloadsDirectory();

}