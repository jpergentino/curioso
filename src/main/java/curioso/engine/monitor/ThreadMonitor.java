package curioso.engine.monitor;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import curioso.engine.data.ServiceConfig;

public class ThreadMonitor implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(ThreadMonitor.class);
	
	private ServiceConfig serviceConfig;

	private boolean keepMonitoring = true;

	private HttpClient client;

	private HttpRequest request;

	public ThreadMonitor(ServiceConfig service) {
		this.serviceConfig = service;
		
		prepareHttpRequest();
	}

	@Override
	public void run() {
		
		logger.info("Monitoring {}", serviceConfig.getName());
		
		while (keepMonitoring) {
			logger.debug("Trying to reach {}: {}...", serviceConfig.getName(), serviceConfig.getUrl());
			
			logger.trace(logRequest(request));
			
			try {
				HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
				logger.debug("Response Code: {}: {}", serviceConfig.getName(), response.statusCode());
				logger.trace("Response Body: {}: {}", serviceConfig.getName(), response.body());
				Thread.sleep(serviceConfig.getInterval() * 1000);
			} catch (InterruptedException e) {
				logger.error("Error when waiting next ping on {}", serviceConfig.getName());
			} catch (IOException e) {
				logger.error("Error when sending request to {}", serviceConfig.getUrl());
			}
		}
		
	}
	
	private String logRequest(HttpRequest request) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== HTTP Request ==="+ System.lineSeparator());
        sb.append(request.method()).append(" ").append(request.uri()).append(System.lineSeparator());

        sb.append("Headers:"+ System.lineSeparator());
        request.headers().map().forEach((key, values) -> {
            sb.append("  ").append(key).append(": ");
            sb.append(String.join(", ", values)).append("\n");
        });
        
        sb.append("Body:"+ System.lineSeparator());
        sb.append(serviceConfig.getBody());

        return sb.toString();
	}
	
	private void prepareHttpRequest() {
		client = HttpClient.newBuilder()
				.connectTimeout(Duration.ofSeconds(serviceConfig.getMaxTimeout() > 0 ? serviceConfig.getMaxTimeout() : 60))
				.followRedirects(Redirect.ALWAYS)
				.build();
		
		HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(serviceConfig.getUrl()));
		
		switch (serviceConfig.getMethod().toUpperCase()) {
		case "GET":
			requestBuilder.GET();
			break;
		case "POST":
			requestBuilder.POST(createBodyPublisher(serviceConfig));
			break;
		case "PUT":
			requestBuilder.PUT(createBodyPublisher(serviceConfig));
			break;
		case "DELETE":
			requestBuilder.DELETE();
			break;
		case "HEAD":
			requestBuilder.method("HEAD", HttpRequest.BodyPublishers.noBody());
			break;
		case "PATCH":
			requestBuilder.method("PATCH", createBodyPublisher(serviceConfig));
			break;
		default:
			requestBuilder.GET();
		}
		
		if (serviceConfig.getHeaders() != null) {
			serviceConfig.getHeaders().entrySet().stream()
			.forEach(entry -> requestBuilder.header(entry.getKey(), entry.getValue()));
		}
		
		request = requestBuilder.build();
	}

	private static HttpRequest.BodyPublisher createBodyPublisher(ServiceConfig serviceConfig) {
        return Optional.ofNullable(serviceConfig.getBody())
                .map(HttpRequest.BodyPublishers::ofString)
                .orElse(HttpRequest.BodyPublishers.noBody());
    }

}