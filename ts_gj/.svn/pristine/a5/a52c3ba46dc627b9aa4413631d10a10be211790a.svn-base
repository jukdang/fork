package egovframework.theimc.cmmn.influx.web;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.QueryApi;
import com.influxdb.exceptions.InfluxException;
import com.influxdb.query.FluxTable;

import egovframework.theimc.cmmn.influx.util.InfluxDbClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/influx")
public class InfluxQueryController {

    @Autowired
    private InfluxDbClient influxDbClient;
    
    private final DateTimeFormatter SEOUL_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("Asia/Seoul")); // 서울 시간대

    @RequestMapping("/getSensorHistory")
    public ModelAndView getSensorHistory(@RequestParam HashMap<String, Object> requestMap) {

        ModelAndView mav = new ModelAndView("jsonView");

        String measurements = requestMap.get("influx_table").toString();
        String influxcolumn = requestMap.get("influx_column").toString();

        String startDateTime = requestMap.get("start_date_time").toString();
        String endDateTime = requestMap.get("end_date_time").toString();

        String startTime = Instant.parse(startDateTime).toString();
        String stopTime = Instant.parse(endDateTime).toString();

        String bucket = influxDbClient.bucket;
        String org = influxDbClient.org;
        String url = "http://" + influxDbClient.host + ":" + influxDbClient.port;
        String token = influxDbClient.token;

        InfluxDBClient client = null;

        try {
            client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
            QueryApi queryApi = client.getQueryApi();

            String query;
            if (!influxcolumn.equals("All")) {
                String[] parts = influxcolumn.split(",");
                StringBuilder columnsBuilder = new StringBuilder();
                for (int i = 0; i < parts.length; i++) {
                    String column = parts[i].trim(); // 🔹 앞뒤 공백 제거
                    columnsBuilder.append("\"").append(column).append("\"");
                    if (i < parts.length - 1) columnsBuilder.append(",");
                }
                query = String.format(
                        "from(bucket:\"%s\") |> range(start: %s, stop: %s)" +
                        " |> filter(fn: (r) => r._measurement == \"%s\")" +
                        " |> pivot(rowKey:[\"_time\"], columnKey:[\"_field\"], valueColumn:\"_value\")" +
                        " |> sort(columns:[\"_time\"], desc:false)" +
                        " |> keep(columns:[%s])",
                        bucket, startTime, stopTime, measurements, columnsBuilder.toString()
                );
            } else {
                query = String.format(
                        "from(bucket:\"%s\") |> range(start: %s, stop: %s)" +
                        " |> filter(fn: (r) => r._measurement == \"%s\")" +
                        " |> pivot(rowKey:[\"_time\"], columnKey:[\"_field\"], valueColumn:\"_value\")" +
                        " |> sort(columns:[\"_time\"], desc:false)",
                        bucket, startTime, stopTime, measurements
                );
            }

            List<FluxTable> tables = queryApi.query(query);

            // JSON 직렬화를 위해 테이블/레코드를 Map으로 변환
            List<HashMap<String, Object>> resultList = tables.stream()
            	    .flatMap(t -> t.getRecords().stream())
            	    .map(r -> {
            	    	HashMap<String, Object> row = new HashMap<>();
            	        
            	        // Null 체크 및 시간대 변환
            	        Instant timeInstant = r.getTime();
            	        if (timeInstant != null) {
            	            ZonedDateTime seoulTime = timeInstant.atZone(ZoneId.of("Asia/Seoul"));
            	            row.put("time", SEOUL_TIME_FORMATTER.format(seoulTime));
            	        } else {
            	            row.put("time", null);
            	        }
            	        
            	        // values 추가 (null 체크)
            	        Map<String, Object> values = r.getValues();
            	        if (values != null) {
            	            values.forEach((key, value) -> {
            	                // _time은 이미 time으로 처리했으므로 제외
            	                if (!"_time".equals(key) && !"result".equals(key) && !"table".equals(key)) {
            	                    row.put(key, value);
            	                }
            	            });
            	        }
            	        
            	        return row;
            	    })
            	    .collect(Collectors.toList()); // .toList()는 Java 16+에서만 사용 가능

            mav.addObject("result", resultList);

        } catch (InfluxException e) {
	    	if (log.isErrorEnabled()) {
	    		log.error("[ERROR] InfluxQueryController getSensorHistory InfluxException : " + e);
			}
        } catch (Exception e) {
	    	if (log.isErrorEnabled()) {
	    		log.error("[ERROR] InfluxQueryController getSensorHistory Exception : " + e);
			}
        } finally {
            if (client != null) client.close();
        }

        return mav;
    }
    
    /**
     * 여러 measurement의 최근 GPS 좌표 조회 (수정된 버전)
     * @param requestMap - mtIdArry: "measurement1,measurement2,measurement3" 형태의 문자열
     * @return ModelAndView - 각 measurement별 최근 GPS 좌표 데이터
     */
    @RequestMapping("/getLatestGpsCoordinates")
    public ModelAndView getLatestGpsCoordinates(@RequestParam HashMap<String, Object> requestMap) {
        
        ModelAndView mav = new ModelAndView("jsonView");
        
        // mtIdArry 파라미터에서 measurement 목록 추출
        String mtIdArryStr = requestMap.get("mtIdArry").toString();
        String[] measurements = mtIdArryStr.split(",");
        
        String bucket = influxDbClient.bucket;
        String org = influxDbClient.org;
        String url = "http://" + influxDbClient.host + ":" + influxDbClient.port;
        String token = influxDbClient.token;
        
        InfluxDBClient client = null;
        
        try {
            client = InfluxDBClientFactory.create(url, token.toCharArray(), org, bucket);
            QueryApi queryApi = client.getQueryApi();
            
            // 여러 measurement의 최근 GPS 데이터를 한 번에 조회하는 Flux 쿼리
            StringBuilder measurementFilter = new StringBuilder();
            for (int i = 0; i < measurements.length; i++) {
                String measurement = measurements[i].trim();
                measurementFilter.append("r._measurement == \"").append(measurement).append("\"");
                if (i < measurements.length - 1) {
                    measurementFilter.append(" or ");
                }
            }
            
            // 수정된 쿼리: 필드명 대소문자 수정 및 그룹화 방식 변경
            String query = String.format(
                "from(bucket:\"%s\")" +
                " |> range(start: -24h)" +  // 최근 24시간 내 데이터 검색
                " |> filter(fn: (r) => %s)" +  // measurement 필터
                " |> filter(fn: (r) => r._field == \"GPS_longitude\" or r._field == \"GPS_Latitude\")" +  // GPS 필드만 필터 (L 대문자)
                " |> group(columns: [\"_measurement\", \"_field\"])" +  // measurement와 field별로 그룹화
                " |> last()" +  // 각 그룹의 최근 데이터
                " |> group(columns: [\"_measurement\"])" +  // measurement별로 다시 그룹화
                " |> pivot(rowKey:[\"_time\", \"_measurement\"], columnKey:[\"_field\"], valueColumn:\"_value\")" +  // 피벗으로 longitude, latitude 컬럼화
                " |> sort(columns:[\"_measurement\"], desc:false)",
                bucket, measurementFilter.toString()
            );
            
            if (log.isDebugEnabled()) {
                log.debug("GPS Query: " + query);
            }
            
            List<FluxTable> tables = queryApi.query(query);
            
            // 결과를 measurement별로 정리
            List<HashMap<String, Object>> resultList = tables.stream()
                .flatMap(t -> t.getRecords().stream())
                .map(r -> {
                    HashMap<String, Object> row = new HashMap<>();
                    
                    // measurement 정보
                    row.put("measurement", r.getMeasurement());
                    
                    // 시간 정보 (서울 시간대로 변환)
                    Instant timeInstant = r.getTime();
                    if (timeInstant != null) {
                        ZonedDateTime seoulTime = timeInstant.atZone(ZoneId.of("Asia/Seoul"));
                        row.put("time", SEOUL_TIME_FORMATTER.format(seoulTime));
                    } else {
                        row.put("time", null);
                    }
                    
                    // GPS 좌표 정보 (필드명 대소문자 수정)
                    Map<String, Object> values = r.getValues();
                    if (values != null) {
                        row.put("GPS_longitude", values.get("GPS_longitude"));
                        row.put("GPS_Latitude", values.get("GPS_Latitude"));  // 대문자 L
                        
                        // 추가적인 태그 정보가 있다면 포함
                        values.forEach((key, value) -> {
                            if (!key.startsWith("_") && !"result".equals(key) && !"table".equals(key) 
                                && !"GPS_longitude".equals(key) && !"GPS_Latitude".equals(key)) {
                                row.put(key, value);
                            }
                        });
                    }
                    
                    return row;
                })
                .collect(Collectors.toList());
            
            mav.addObject("result", resultList);
            mav.addObject("totalCount", resultList.size());
            
            if (log.isDebugEnabled()) {
                log.debug("Retrieved GPS coordinates for {} measurements", resultList.size());
            }
            
        } catch (InfluxException e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] InfluxQueryController getLatestGpsCoordinates InfluxException : ", e);
            }
            mav.addObject("error", "InfluxDB 조회 중 오류가 발생했습니다: " + e.getMessage());
        } catch (Exception e) {
            if (log.isErrorEnabled()) {
                log.error("[ERROR] InfluxQueryController getLatestGpsCoordinates Exception : ", e);
            }
            mav.addObject("error", "GPS 좌표 조회 중 오류가 발생했습니다: " + e.getMessage());
        } finally {
            if (client != null) client.close();
        }
        
        return mav;
    }
}
