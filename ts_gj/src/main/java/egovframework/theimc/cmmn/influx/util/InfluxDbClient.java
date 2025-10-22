package egovframework.theimc.cmmn.influx.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

@Controller
public class InfluxDbClient {
	
	@Value("${influxdb.host}")
	public String host;
	
	@Value("${influxdb.port}")
	public String port;
	
	@Value("${influxdb.token}")
	public String token;
	
	@Value("${influxdb.org}")
	public String org;
	
	@Value("${influxdb.bucket}")
	public String bucket;
	
}
