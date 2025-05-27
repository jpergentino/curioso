package curioso.engine.monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import curioso.engine.data.ServiceConfig;
import jakarta.annotation.PostConstruct;

@Component
public class MonitorManager {
	
	@Value("${monitor.thread.pool.size:10}")
	private int poolSize;
	
	private ExecutorService executorService;
	
	@PostConstruct
	private void init() {
		this.executorService = Executors.newFixedThreadPool(poolSize >= 0 ? poolSize : Runtime.getRuntime().availableProcessors());
	}
	
	public void addMonitor(ServiceConfig service) {
		Assert.notNull(service, "Service is mandatory to start monitoring.");
		Assert.hasText(service.getName(), "Service name is mandatory");
		Assert.hasText(service.getUrl(), "Service url is mandatory for the service "+ service.getName());
		
		Thread threadMonitor = new Thread(new ThreadMonitor(service), "Monitor-"+ service.getName());
		executorService.execute(threadMonitor);
	}

}