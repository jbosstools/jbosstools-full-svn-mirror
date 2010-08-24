/*-- 

 $Id: BuilderErrorHandler.java,v 1.12 2004/02/06 09:28:31 jhunter Exp $

 Copyright (C) 2000-2004 Jason Hunter & Brett McLaughlin.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "JDOM" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact <request_AT_jdom_DOT_org>.
 
 4. Products derived from this software may not be called "JDOM", nor
    may "JDOM" appear in their name, without prior written permission
    from the JDOM Project Management <request_AT_jdom_DOT_org>.
 
 In addition, we request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      JDOM Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos 
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE JDOM AUTHORS OR THE PROJECT
 CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the JDOM Project and was originally 
 created by Jason Hunter <jhunter_AT_jdom_DOT_org> and
 Brett McLaughlin <brett_AT_jdom_DOT_org>.  For more information
 on the JDOM Project, please see <http://www.jdom.org/>.
 
 */

package org.jdom.input;

import org.xml.sax.*;

/**
 * The standard JDOM error handler implementation.
 * 
 * @author  Jason Hunter
 * @version $Revision: 1.12 $, $Date: 2004/02/06 09:28:31 $
 */

public class BuilderErrorHandler implements ErrorHandler {

    private static final String CVS_ID = 
      "@(#) $RCSfile: BuilderErrorHandler.java,v $ $Revision: 1.12 $ $Date: 2004/02/06 09:28:31 $ $Name: jdom_1_0 $";

    /**
     * This method is called when a warning has occurred; this indicates
     * that while no XML rules were broken, something appears to be
     * incorrect or missing.
     * The implementation of this method here is a "no op".
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws SAXException when things go wrong
     */
    public void warning(SAXParseException exception) throws SAXException {
        // nothing
    }

    /**
     * This method is called in response to an error that has occurred; 
     * this indicates that a rule was broken, typically in validation, but 
     * that parsing could reasonably continue.
     * The implementation of this method here is to rethrow the exception.
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws SAXException when things go wrong
     */
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    /**
     * This method is called in response to a fatal error; this indicates that
     * a rule has been broken that makes continued parsing either impossible
     * or an almost certain waste of time.
     * The implementation of this method here is to rethrow the exception.
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws SAXException when things go wrong
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }
}
