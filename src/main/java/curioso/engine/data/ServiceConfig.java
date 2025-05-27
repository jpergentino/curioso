package curioso.engine.data;

import java.io.Serializable;
import java.util.Map;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

public class ServiceConfig implements Serializable {

	private static final long serialVersionUID = 6440942913180912058L;

	/**
	 * The service name.
	 */
	private String name;

	/**
	 * The service URL.
	 */
	private String url;
	
	/**
	 * The interval (in seconds) between new service check.
	 */
	private short interval = 60;
	
	/**
	 * The request method. Accepted values: GET, POST, PUT, PATCH, DELETE. 
	 * Default value: GET
	 */
	private String method = HttpMethod.GET.name();
	
	/**
	 * The payload body data to include in the request.
	 */
	private String body;

	/**
	 * Maximum timeout, in seconds. Default value: 60.
	 */
	private short maxTimeout = 60;

	/**
	 * An online flag indicator.
	 */
	private boolean online = false;

	/**
	 * The expected HTTP code result. 
	 * Default value: 200.
	 */
	private int expectedHttpCodeResult = HttpStatus.OK.value();

	/**
	 * The expected result.
	 */
	private String expectedResult;

	/**
	 * The Regex expression to evaluate the expected result .
	 */
	private String expectedResultRegex;
	
	private Map<String, String> headers;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public short getMaxTimeout() {
		return maxTimeout;
	}

	public void setMaxTimeout(short maxTimeout) {
		this.maxTimeout = maxTimeout;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public int getExpectedHttpCodeResult() {
		return expectedHttpCodeResult;
	}

	public void setExpectedHttpCodeResult(int expectedHttpCodeResult) {
		this.expectedHttpCodeResult = expectedHttpCodeResult;
	}

	public String getExpectedResult() {
		return expectedResult;
	}

	public void setExpectedResult(String expectedResult) {
		this.expectedResult = expectedResult;
	}

	public String getExpectedResultRegex() {
		return expectedResultRegex;
	}

	public void setExpectedResultRegex(String expectedResultRegex) {
		this.expectedResultRegex = expectedResultRegex;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String payload) {
		this.body = payload;
	}

	public short getInterval() {
		return interval;
	}

	public void setInterval(short interval) {
		this.interval = interval;
	}

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}

}