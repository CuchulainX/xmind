package io_wintermute_discovery.osgi;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class HelloActivator implements BundleActivator { 

public void start(BundleContext context) throws Exception { 
	System.out.println( "Hello JCG, Welcome to OSGi World!!" ); 
} 

/* 
* (non-Javadoc) 
* 
* @see 
* org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext) 
*/ 
public void stop(BundleContext context) throws Exception { 
	System.out.println( "Goodbye JCG" ); 
} 

} 
