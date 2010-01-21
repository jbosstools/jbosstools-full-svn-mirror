/*
 * ModeShape (http://www.modeshape.org)
 * See the COPYRIGHT.txt file distributed with this work for information
 * regarding copyright ownership.
 *
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * See the AUTHORS.txt file in the distribution for a full listing of
 * individual contributors.
 */
package org.jboss.tools.modeshape.rest;

import static org.jboss.tools.modeshape.rest.IUiConstants.PLUGIN_ID;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.modeshape.common.util.CheckArg;
import org.modeshape.web.jcr.rest.client.Status;
import org.modeshape.web.jcr.rest.client.Status.Severity;

public final class Utils {

    // ===========================================================================================================================
    // Class Methods
    // ===========================================================================================================================

    /**
     * @param tokens the tokens being combined into one value (never <code>null</code>)
     * @param delimiter the character inserted to separate each token
     * @return the tokens separated by the delimiter
     */
    public static String combineTokens( String[] tokens,
                                        char delimiter ) {
        CheckArg.isNotNull(tokens, "tokens");
        StringBuilder value = new StringBuilder();

        for (String token : tokens) {
            value.append(token).append(delimiter);
        }

        return value.toString();
    }

    /**
     * Sizes the shell to the minimum of it's current size or the width and height display percentages.
     * 
     * @param shell the shell being resized (if necessary) and located
     * @param widthPercentage a number between 1 and 100 indicating a percentage of the screen size (defaults to 50 if bad value)
     * @param heightPercentage a number between 1 and 100 indicating a percentage of the screen size (defaults to 50 if bad value)
     */
    public static void centerAndSizeShellRelativeToDisplay( Shell shell,
                                                            int widthPercentage,
                                                            int heightPercentage ) {
        if ((widthPercentage < 1) || (widthPercentage > 100)) {
            widthPercentage = 50;
        }

        if ((heightPercentage < 1) || (heightPercentage > 100)) {
            heightPercentage = 50;
        }

        // size
        Rectangle shellBounds = shell.getBounds();
        Rectangle displayBounds = shell.getDisplay().getClientArea();
        int scaledWidth = displayBounds.width * widthPercentage / 100;
        int scaledHeight = displayBounds.height * heightPercentage / 100;
        shell.setSize(Math.min(scaledWidth, shellBounds.width), Math.min(scaledHeight, shellBounds.height));
        Point size = shell.getSize();

        // center
        int excessX = displayBounds.width - size.x;
        int excessY = displayBounds.height - size.y;
        int x = displayBounds.x + (excessX / 2);
        int y = displayBounds.y + (excessY / 2);

        shell.setLocation(x, y);
    }

    /**
     * Converts the non-Eclipse status severity to an Eclipse severity level. An {@link Status.Severity#UNKNOWN unknown status} is
     * converted to {@link IStatus#CANCEL cancel}.
     * 
     * @param severity the eclipse status severity level
     * @return the converted severity level (never <code>null</code>)
     * @see IStatus
     */
    public static int convertSeverity( Severity severity ) {
        if (severity == Severity.OK) return IStatus.OK;
        if (severity == Severity.ERROR) return IStatus.ERROR;
        if (severity == Severity.WARNING) return IStatus.WARNING;
        if (severity == Severity.INFO) return IStatus.INFO;
        return IStatus.CANCEL;
    }

    /**
     * Converts the Eclipse status severity level to a non-Eclipse severity.
     * 
     * @param severity the eclipse status severity level
     * @return the converted severity level (never <code>null</code>)
     * @see IStatus
     */
    public static Severity convertSeverity( int severity ) {
        if (severity == IStatus.OK) return Severity.OK;
        if (severity == IStatus.ERROR) return Severity.ERROR;
        if (severity == IStatus.WARNING) return Severity.WARNING;
        if (severity == IStatus.INFO) return Severity.INFO;
        return Severity.UNKNOWN;
    }

    /**
     * @param status the status being converted (never <code>null</code>)
     * @return the Eclipse status object (never <code>null</code>)
     */
    public static IStatus convert( Status status ) {
        CheckArg.isNotNull(status, "status");
        return new org.eclipse.core.runtime.Status(convertSeverity(status.getSeverity()), PLUGIN_ID, status.getMessage(),
                                                   status.getException());
    }

    /**
     * The OK status does not have an image.
     * 
     * @param status the status whose image is being requested (never <code>null</code>)
     * @return the image or <code>null</code> if no associated image for the status severity
     */
    public static Image getImage( Status status ) {
        CheckArg.isNotNull(status, "status");
        String imageId = null;

        if (status.isError()) {
            imageId = ISharedImages.IMG_OBJS_ERROR_TSK;
        } else if (status.isInfo()) {
            imageId = ISharedImages.IMG_OBJS_INFO_TSK;
        } else if (status.isWarning()) {
            imageId = ISharedImages.IMG_OBJS_WARN_TSK;
        }

        if (imageId != null) {
            return Activator.getDefault().getSharedImage(imageId);
        }

        return null;
    }

    /**
     * @param string the string whose tokens are being requested (may be <code>null</code>)
     * @param delimiters the delimiters that separate the tokens (never <code>null</code>)
     * @param removeDuplicates a flag indicating if duplicate tokens should be removed
     * @return the tokens (never <code>null</code>)
     */
    public static String[] getTokens( String string,
                                      String delimiters,
                                      boolean removeDuplicates ) {
        CheckArg.isNotNull(delimiters, "delimiters");

        if (string == null) {
            return new String[0];
        }

        Collection<String> tokens = removeDuplicates ? new TreeSet<String>() : new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(string, delimiters);

        while (st.hasMoreTokens()) {
            tokens.add(st.nextToken());
        }

        return tokens.toArray(new String[tokens.size()]);
    }

    /**
     * The image can be used to decorate an existing image.
     * 
     * @param status the status whose image overlay is being requested (never <code>null</code>)
     * @return the image descriptor or <code>null</code> if none found for the status severity
     */
    public static ImageDescriptor getOverlayImage( Status status ) {
        CheckArg.isNotNull(status, "status");
        String imageId = null;

        if (status.isError()) {
            imageId = IUiConstants.ERROR_OVERLAY_IMAGE;
        }

        if (imageId != null) {
            return Activator.getDefault().getImageDescriptor(imageId);
        }

        return null;
    }

    /**
     * @param password the password being validated
     * @return a validation status (never <code>null</code>)
     */
    public static Status isPasswordValid( String password ) {
        return Status.OK_STATUS;
    }

    /**
     * This does not verify that a server with the same primary field values doesn't already exist in the server registry.
     * 
     * @param url the URL being validated
     * @param user the user being validated
     * @param password the password being validated
     * @return a validation status (never <code>null</code>)
     */
    public static Status isServerValid( String url,
                                        String user,
                                        String password ) {
        Status status = isUrlValid(url);

        if (!status.isError()) {
            status = isUserValid(user);

            if (!status.isError()) {
                status = isPasswordValid(password);
            }
        }

        return status;
    }

    /**
     * @param url the URL being validated
     * @return a validation status (never <code>null</code>)
     */
    public static Status isUrlValid( String url ) {
        if ((url == null) || (url.length() == 0)) {
            return new Status(Severity.ERROR, RestClientI18n.serverEmptyUrlMsg.text(), null);
        }

        try {
            new URL(url);
        } catch (Exception e) {
            return new Status(Severity.ERROR, RestClientI18n.serverInvalidUrlMsg.text(url), e);
        }

        return Status.OK_STATUS;
    }

    /**
     * @param user the user being validated
     * @return a validation status (never <code>null</code>)
     */
    public static Status isUserValid( String user ) {
        if ((user == null) || (user.length() == 0)) {
            return new Status(Severity.ERROR, RestClientI18n.serverEmptyUserMsg.text(), null);
        }

        return Status.OK_STATUS;
    }

    // ===========================================================================================================================
    // Constructors
    // ===========================================================================================================================

    /**
     * Don't allow construction.
     */
    public Utils() {
        // nothing to do
    }

}
