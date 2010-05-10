/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.ui.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Tester class for JAX-RS services
 * @author bfitzpat
 *
 */
public class JAXRSTester {
	
	// Result message to pass back
	private String resultBody;
	
	// HTTP headers going in and out
	private Map<?, ?> requestHeaders = null;
	private Map<?, ?> resultHeaders = null;
	
	// utility constant
	private String EMPTY_STRING = ""; //$NON-NLS-1$

	/**
	 * Constructor
	 */
	public JAXRSTester() {
		// empty
	}

	/**
	 * Return the result message 
	 * @return String
	 */
	public String getResultBody() {
		return this.resultBody;
	}

	/**
	 * Return the result HTTP headers
	 * @return Map
	 */
	public Map<?, ?> getResultHeaders() {
		return resultHeaders;
	}
	
	/**
	 * Return the request HTTP headers
	 * @return Map
	 */
	public Map<?, ?> getRequestHeaders() {
		return requestHeaders;
	}

	/*
	 * Start building the web query. Append parameters to URL
	 */
	private String buildWebQuery(Map<String, String> parameters) throws Exception {
		if (!parameters.isEmpty()) {
	        StringBuilder sb = new StringBuilder();
	        for (Map.Entry<String, String> entry : parameters.entrySet()) {
	            String key = URLEncoder.encode(entry.getKey(), "UTF-8"); //$NON-NLS-1$
	            String value = URLEncoder.encode(entry.getValue(), "UTF-8"); //$NON-NLS-1$
	            sb.append(key).append("=").append(value).append("&"); //$NON-NLS-1$ //$NON-NLS-2$
	        }
	        return sb.toString().substring(0, sb.length() - 1);
		}
		return EMPTY_STRING;
    }

    /**
     * Simplest call for JAX-RS
     * @param address
     * @param parameters
     * @param headers
     * @throws Exception
     */
    public void doTest (String address, Map<String, String> parameters, Map<String, String> headers) throws Exception {
        doTest(address, parameters, headers, "GET", null, null, 0); //$NON-NLS-1$
    }

    /**
     * Call a JAX-RS service
     * @param address
     * @param parameters
     * @param headers
     * @param methodType
     * @param requestBody
     * @throws Exception
     */
    public void doTest (String address, Map<String, String> parameters, Map<String, String> headers, String methodType, String requestBody) throws Exception {
    	doTest (address, parameters, headers, methodType, requestBody, null, 0);
    }

    /**
     * Call a JAX-RS service
     * @param address
     * @param parameters
     * @param headers
     * @param methodType
     * @param requestBody
     * @param proxy
     * @param port
     * @throws Exception
     */
    public void doTest(String address, Map<String, String> parameters, Map<String, String> headers, String methodType, String requestBody, String proxy, String port) throws Exception {
        doTest(address, parameters, headers, methodType, requestBody, proxy, Integer.parseInt(port));
    }

    /**
     * Call a JAX-RS service
     * @param address
     * @param parameters
     * @param headers
     * @param methodType
     * @param requestBody
     * @param proxy
     * @param port
     * @throws Exception
     */
    public void doTest(String address, Map<String, String> parameters, Map<String, String> headers, String methodType, String requestBody, String proxy, int port) throws Exception {

    	// handle the proxy
        Proxy proxyObject = null;
        if (proxy != null && proxy.length() > 0 && port > 0) {
            InetSocketAddress proxyAddress = new InetSocketAddress(proxy, port);
            proxyObject = new Proxy(Proxy.Type.HTTP, proxyAddress);
        }

        // clear the returned results
        resultBody = EMPTY_STRING;
        
        // get the parms string
        String query = buildWebQuery(parameters);

        // build the complete URL
        URL url = null;
        if (query != null) {
        	// add the ? if there are parameters
            if (!address.endsWith("?") && methodType.equalsIgnoreCase("GET") ) {  //$NON-NLS-1$//$NON-NLS-2$
            	address = address + "?"; //$NON-NLS-1$
            }
        	// add parms to the url if we have some
        	url = new URL(address + query);
        } else {
        	url = new URL(address);
        }

        // make connection
        HttpURLConnection httpurlc = null;
        if (proxyObject == null) {
            httpurlc = (HttpURLConnection) url.openConnection();
        } else {
        	// if have proxy, pass it along
            httpurlc = (HttpURLConnection) url.openConnection(proxyObject);
        }
        
        // since we are expecting output back, set to true
       	httpurlc.setDoOutput(true);
        
        // not sure what this does - may be used for authentication?
        httpurlc.setAllowUserInteraction(false);
        
        // set whether this is a GET or POST
        httpurlc.setRequestMethod(methodType);
        
        // if we have headers to add
        if (headers != null && !headers.isEmpty()) {
        	Iterator<?> iter = headers.entrySet().iterator();
        	while (iter.hasNext()) {
        		Entry<?, ?> entry = (Entry<?, ?>)iter.next();
        		if (entry.getKey() != null && entry.getKey() instanceof String)
        			httpurlc.addRequestProperty((String) entry.getKey(), (String) entry.getValue());
        	}
        }
        
        requestHeaders = httpurlc.getRequestProperties();
        
        // CONNECT!
        httpurlc.connect();

        // If we are doing a POST and we have some request body to pass along, do it
        if (requestBody != null && ( methodType.equalsIgnoreCase("POST")  //$NON-NLS-1$
        		|| methodType.equalsIgnoreCase("PUT"))) { //$NON-NLS-1$
        	requestBody = WSTestUtils.stripNLsFromXML(requestBody);
        	OutputStreamWriter out = new OutputStreamWriter(httpurlc.getOutputStream());
        	out.write(requestBody);
        	out.close();
        }

        // retrieve result and put string results into the response
        InputStream is = (InputStream) httpurlc.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));//$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");//$NON-NLS-1$
        }
        br.close();
        resultBody = sb.toString();
        
        resultHeaders = httpurlc.getHeaderFields();
        
        // disconnect explicitly (may not be necessary)
        httpurlc.disconnect();
    }	
}
