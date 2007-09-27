package com.wutka.dtd;

import java.io.IOException;
import java.io.PrintWriter;

/** Defines the method used for writing DTD information to a PrintWriter
 *
 * @author Mark Wutka
 * @version $Revision$ $Date$ by $Author$
 */
public interface DTDOutput
{
    public void write(PrintWriter out) throws IOException;
}
