
package org.example.www.helloworld;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.1-b03-
 * Generated source version: 2.0
 * 
 */
@WebServiceClient(name = "HelloWorldService", targetNamespace = "http://www.example.org/HelloWorld", wsdlLocation = "http://localhost:8080/JavaFirstTestProject/HelloWorld?wsdl")
public class HelloWorldService
    extends Service
{

    private final static URL HELLOWORLDSERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            url = new URL("http://localhost:8080/JavaFirstTestProject/HelloWorld?wsdl");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HELLOWORLDSERVICE_WSDL_LOCATION = url;
    }

    public HelloWorldService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public HelloWorldService() {
        super(HELLOWORLDSERVICE_WSDL_LOCATION, new QName("http://www.example.org/HelloWorld", "HelloWorldService"));
    }

    /**
     * 
     * @return
     *     returns HelloWorld
     */
    @WebEndpoint(name = "HelloWorldPort")
    public HelloWorld getHelloWorldPort() {
        return (HelloWorld)super.getPort(new QName("http://www.example.org/HelloWorld", "HelloWorldPort"), HelloWorld.class);
    }

}
