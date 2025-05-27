package curioso.engine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import curioso.engine.data.ServiceConfig;
import curioso.engine.data.ServiceYAMLConfig;
import curioso.engine.monitor.MonitorManager;
import curioso.util.CuriosoUtils;
import jakarta.annotation.PostConstruct;

@Component
public class Bootstrap {
	
	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	
	@Value("${service.files.prefix}")
	private String serviceFilesPrefix;
	
	@Value("${service.files.base-path:}")
    private String basePath;
	
	@Autowired
	private MonitorManager monitorManager;
	
	@PostConstruct
	private void init() throws IOException {
		loadFiles();
		loadServices();
		startServices();
	}

	private void startServices() {
		Global.services.stream().forEach(service -> {
			monitorManager.addMonitor(service);
		});
		
	}

	private void loadServices() {
		
		for (Path configFile : Global.serviceFiles) {
			
			logger.debug("Processing {}", configFile.toAbsolutePath());
			try {
				
				ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
				mapper.findAndRegisterModules();
				
				ServiceYAMLConfig configService = mapper.readValue(configFile.toFile(), ServiceYAMLConfig.class);
				
				if (configService.getServices() != null && configService.getServices().isEmpty()) {
					logger.warn("There are no services for {}.", configService.getName());
				}
				
				for (ServiceConfig service : configService.getServices()) {
					if (validateMandatoryServiceData(service)) {
						Global.services.add(service);
						logger.info("Added {}", service.getName());
					}
				}
				
			} catch (IOException e) {
				logger.error("Ops, an error occurred when processing {}", configFile.toAbsolutePath());
			}

		}
		
	}

	/**
	 * Validate the mandatory data for a service.
	 *  
	 * @param service the service.
	 * @return true if all mandatory fields were defined.
	 */
	private boolean validateMandatoryServiceData(ServiceConfig service) {
		return CuriosoUtils.allNotEmpty(service.getName(), service.getUrl());
	}

	/**
	 * Load the files from the disk.
	 * 
	 * @throws IOException
	 */
	private void loadFiles() throws IOException {
		
		Path directory = StringUtils.hasText(basePath) 
	            ? Paths.get(basePath).toAbsolutePath() 
	            : Paths.get("").toAbsolutePath();
		
		logger.info("Scanning for YAML files with prefix '{}' in directory: {}", serviceFilesPrefix, directory);
		
		List<Path> foundFiles = new LinkedList<Path>();
		
		try (Stream<Path> paths = Files.list(directory)) {
            foundFiles = paths
            		.filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = path.getFileName().toString();
                        return fileName.startsWith(serviceFilesPrefix) && fileName.toLowerCase().endsWith(".yaml");
                    })
                    .sorted()
                    .peek(path -> logger.debug("Found a service file: {}", path))
                    .collect(Collectors.toList());
        }
		
		if (foundFiles.isEmpty()) {
			String errorMsg = String.format("No service configuration files found with prefix '%s' and extension '%s' in directory: %s", serviceFilesPrefix, ".yaml", directory);;
			logger.error(errorMsg);
			throw new ApplicationContextException(errorMsg);
		}
		
		Global.serviceFiles.addAll(foundFiles);
		logger.info("Loaded {} service configuration file(s).", foundFiles.size());
		
	}

}