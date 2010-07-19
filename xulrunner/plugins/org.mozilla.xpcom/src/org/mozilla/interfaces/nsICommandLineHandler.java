/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/toolkit/components/commandlines/public/nsICommandLineHandler.idl
 */

package org.mozilla.interfaces;

/**
 * Handles arguments on the command line of an XUL application.
 *
 * Each handler is registered in the category "command-line-handler".
 * The entries in this category are read in alphabetical order, and each
 * category value is treated as a service contractid implementing this
 * interface.
 *
 * By convention, handler with ordinary priority should begin with "m".
 *
 * Example:
 * Category             Entry          Value
 * command-line-handler b-jsdebug      @mozilla.org/venkman/clh;1
 * command-line-handler c-extensions   @mozilla.org/extension-manager/clh;1
 * command-line-handler m-edit         @mozilla.org/composer/clh;1
 * command-line-handler m-irc          @mozilla.org/chatzilla/clh;1
 * command-line-handler y-final        @mozilla.org/browser/clh-final;1
 *
 * @status UNDER_REVIEW This interface is intended to be frozen, but it isn't
 *                      frozen yet. Be careful!
 * @note What do we do about localizing helpInfo? Do we make each handler do it,
 *       or provide a generic solution of some sort? Don't freeze this interface
 *       without thinking about this!
 */
public interface nsICommandLineHandler extends nsISupports {

  String NS_ICOMMANDLINEHANDLER_IID =
    "{d4b123df-51ee-48b1-a663-002180e60d3b}";

  /**
   * Process a command line. If this handler finds arguments that it
   * understands, it should perform the appropriate actions (such as opening
   * a window), and remove the arguments from the command-line array.
   *
   * @throw NS_ERROR_ABORT to immediately cease command-line handling
   *        (if this is STATE_INITIAL_LAUNCH, quits the app).
   *        All other exceptions are silently ignored.
   */
  void handle(nsICommandLine aCommandLine);

  /**
   * When the app is launched with the -help argument, this attribute
   * is retrieved and displayed to the user (on stdout). The text should
   * have embedded newlines which wrap at 76 columns, and should include
   * a newline at the end. By convention, the right column which contains flag
   * descriptions begins at the 24th character.
   */
  String getHelpInfo();

}