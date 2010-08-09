/*
 * (c) Copyright IBM Corp 2001, 2005 
 */

package javax.wsdl.extensions.http;

import javax.wsdl.extensions.*;

/**
 * @author Matthew J. Duftler (duftler@us.ibm.com)
 */
public interface HTTPAddress extends ExtensibilityElement, java.io.Serializable
{
  /**
   * Set the location URI for this HTTP address.
   *
   * @param locationURI the desired location URI
   */
  public void setLocationURI(String locationURI);

  /**
   * Get the location URI for this HTTP address.
   */
  public String getLocationURI();
}