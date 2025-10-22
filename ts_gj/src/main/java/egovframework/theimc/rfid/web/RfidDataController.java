package egovframework.theimc.rfid.web;

import egovframework.theimc.rfid.service.RfidDataApi;
import egovframework.theimc.util.StatusMsg;
import lombok.extern.slf4j.Slf4j;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.sql.SQLException;

@RestController
@RequestMapping("/api/rfid")
@Slf4j
public class RfidDataController {
    
    @Autowired
    private  RfidDataApi rfidDataApi;
    
    @PostMapping("/tag")
    @CrossOrigin(origins = "*")  // CORS 허용
    public ResponseEntity<StatusMsg> handleRfidTag(HttpServletRequest request) {
        try {
            // Request body 읽기
            String body = getRequestBody(request);
            log.info("Received RFID TAG data: {}", body);
            
            // 빈 body 체크
            if (body == null || body.trim().isEmpty()) {
                log.warn("Empty request body");
                return StatusMsg.fail("request body is empty");
            }
            
            // body 데이터 파싱: {topic}-----{payload} 형식
            String[] bodyParts = body.split("-----");
            if (bodyParts.length != 2) {
                log.warn("Invalid body format: {}", body);
                return StatusMsg.fail("rfid tag body invalid!");
            }
            
            // topic 파싱: DRIVER/AT_ID/MT_ID
            String topic = bodyParts[0];
            if (topic == null || topic.trim().isEmpty()) {
                log.warn("Empty topic");
                return StatusMsg.fail("topic is empty");
            }
            
            String[] topicParts = topic.split("/");
            if (topicParts.length != 3) {
                log.warn("Invalid topic format: {}", topic);
                return StatusMsg.fail("topic format invalid!");
            }
            
            String mtId = topicParts[2];    // MT_ID만 사용 (topic 저장 안함)
            
            // payload 파싱: RFID_CARD_ID,TAG_DT
            String payload = bodyParts[1];
            if (payload == null || payload.trim().isEmpty()) {
                log.warn("Empty payload");
                return StatusMsg.fail("payload is empty");
            }
            
            String[] payloadParts = payload.split(",");
            if (payloadParts.length != 2) {
                log.warn("Invalid payload format: {}", payload);
                return StatusMsg.fail("payload invalid");
            }
            
            String rfidNo = payloadParts[0];    // RFID 번호
            String tagDt = payloadParts[1];     // 태그 일시
            
            log.info("Parsed data - MT_ID: {}, RFID_NO: {}, TAG_DT: {}", 
                    mtId, rfidNo, tagDt);
            
            // 데이터 유효성 검증
            if (mtId == null || mtId.trim().isEmpty()) {
                log.warn("MT_ID is empty");
                return StatusMsg.fail("MT_ID is required");
            }
            
            if (rfidNo == null || rfidNo.trim().isEmpty()) {
                log.warn("RFID_NO is empty");
                return StatusMsg.fail("RFID_NO is required");
            }
            
            if (tagDt == null || tagDt.trim().isEmpty()) {
                log.warn("TAG_DT is empty");
                return StatusMsg.fail("TAG_DT is required");
            }
            
            // TAG_DT 형식 검증 (YYYYMMDDHHMMSS - 14자리)
            if (!tagDt.matches("\\d{14}")) {
                log.warn("Invalid TAG_DT format: {}", tagDt);
                return StatusMsg.fail("TAG_DT format invalid (required: YYYYMMDDHHMMSS)");
            }
            
            // 데이터베이스 저장
            rfidDataApi.saveRfidData(mtId, rfidNo, tagDt);
            EgovMap responseData = new EgovMap();
            responseData.put("mt_id", mtId);
            responseData.put("rfid_no", rfidNo);
            responseData.put("tag_dt", tagDt);
            return StatusMsg.ok(responseData, "RFID 운행 데이터 처리 완료");
            
        } catch (IOException e) {
            log.error("Request body 읽기 중 오류 발생: {}", e.getMessage(), e);
            return StatusMsg.error("Request body 읽기 실패", e);
            
        } catch (DateTimeParseException e) {
            log.error("날짜 형식 파싱 오류: {}", e.getMessage(), e);
            return StatusMsg.fail("날짜 형식이 올바르지 않습니다");
            
        } catch (IllegalArgumentException e) {
            log.error("잘못된 매개변수: {}", e.getMessage(), e);
            return StatusMsg.fail("입력 데이터가 올바르지 않습니다: " + e.getMessage());
            
        } catch (RuntimeException e) {
            log.error("런타임 오류 발생: {}", e.getMessage(), e);
            return StatusMsg.error("처리 중 오류가 발생했습니다", e);
            
        } catch (Exception e) {
            log.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
            return StatusMsg.error("RFID 운행 데이터 처리 중 예상치 못한 오류가 발생했습니다", e);
        }
    }
    
    /**
     * Request Body 읽기
     */
    private String getRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        
        try (BufferedReader reader = request.getReader()) {
            if (reader == null) {
                throw new IOException("Request reader is null");
            }
            
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        } catch (IOException e) {
            log.error("Request body 읽기 실패: {}", e.getMessage());
            throw new IOException("Failed to read request body", e);
        }
        
        return body.toString();
    }
}