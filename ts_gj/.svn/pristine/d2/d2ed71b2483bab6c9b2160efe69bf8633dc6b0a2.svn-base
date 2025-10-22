package egovframework.theimc.rfid.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import egovframework.theimc.rfid.service.impl.RfidDataMapper;
import egovframework.theimc.rfid.vo.RfidDataVO;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RfidDataApi{
    
    @Autowired
    private RfidDataMapper rfidDataMapper;
    
    /**
     * RFID 운행 데이터 저장
     */
    public void saveRfidData(String mtId, String rfidNo, String tagDtStr) {
        try {
            // TAG_DT 문자열을 LocalDateTime으로 변환 (YYYYMMDDHHMMSS -> LocalDateTime)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime tagDt = LocalDateTime.parse(tagDtStr, formatter);
            
            RfidDataVO rfidDataVO = new RfidDataVO();
            rfidDataVO.setMtId(mtId);
            rfidDataVO.setRfidNo(rfidNo);
            rfidDataVO.setTagDt(tagDt);
            
            int result = rfidDataMapper.insertRfidData(rfidDataVO);
            
            if (result > 0) {
                log.info("RFID 운행 데이터 저장 완료 - MT_ID: {}, RFID_NO: {}, TAG_DT: {}", 
                        mtId, rfidNo, tagDtStr);
            } else {
                throw new IllegalArgumentException("RFID 운행 데이터 저장 실패");
            }
            
        } catch (IllegalArgumentException e) {
	        if (log.isErrorEnabled()) {
	            log.error("[ERROR] RfidDataApi saveRfidData IllegalArgumentException : " + e);
	        }
        } catch (Exception e) {
	        if (log.isErrorEnabled()) {
	            log.error("[ERROR] RfidDataApi saveRfidData Exception : " + e);
	        }
        }
    }
}