package curioso.engine;

import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

import curioso.engine.data.ServiceConfig;

/**
 * Global class providing global attributes to be consumed by the application. 
 */
public class Global {
	
	public static List<Path> serviceFiles = new LinkedList<Path>();
	
	public static List<ServiceConfig> services = new LinkedList<ServiceConfig>();

}