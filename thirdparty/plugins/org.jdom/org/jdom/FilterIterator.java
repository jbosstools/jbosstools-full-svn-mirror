/*--

 $Id: FilterIterator.java,v 1.5 2004/08/31 19:36:12 jhunter Exp $

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

package org.jdom;

import java.util.*;
import org.jdom.filter.*;

/**
 * Traverse a parent's children that match the supplied filter.
 *
 * @author Bradley S. Huffman
 * @version $Revision: 1.5 $, $Date: 2004/08/31 19:36:12 $
 */
class FilterIterator implements Iterator {

    private Iterator iterator;
    private Filter filter;
    private Object nextObject;

    private static final String CVS_ID =
            "@(#) $RCSfile: FilterIterator.java,v $ $Revision: 1.5 $ $Date: 2004/08/31 19:36:12 $ $Name: jdom_1_0 $";

    public FilterIterator(Iterator iterator, Filter filter) {
        if ((iterator == null) || (filter == null)) {
            throw new IllegalArgumentException("null parameter");
        }
        this.iterator = iterator;
        this.filter = filter;
    }

    public boolean hasNext() {
        if (nextObject != null) {
            return true;
        }

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (filter.matches(obj)) {
                nextObject = obj;
                return true;
            }
        }
        return false;
    }

    public Object next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        Object obj = nextObject;
        nextObject = null;
        return obj;
    }

    public void remove() {
        // XXX Could cause probs for sure if hasNext() is
        // called before the remove(), although that's unlikely.
        iterator.remove();
    }
}
