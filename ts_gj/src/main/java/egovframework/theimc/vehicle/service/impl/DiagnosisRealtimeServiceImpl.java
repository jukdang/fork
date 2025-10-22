package egovframework.theimc.vehicle.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import egovframework.theimc.vehicle.service.DiagnosisRealtimeService;
import com.fasterxml.jackson.core.type.TypeReference;
/**
 * 차량 정비 - AI 실시간 진단 서비스 클래스
 */
@Service
public class DiagnosisRealtimeServiceImpl extends EgovAbstractServiceImpl implements DiagnosisRealtimeService{
	
	@Autowired
    private DiagnosisRealtimeMapper diagnosisRealtimeMapper;
	
	 /**
	  * AI 실시간 진단 차량 상태 목록
	  */
	@Override
	public EgovMap selectVehicleSttsList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = diagnosisRealtimeMapper.selectVehicleSttsList(requestMap);
	    ObjectMapper objectMapper = new ObjectMapper();
	    for (EgovMap vehicle : list) {
	        String ChckListStr = (String) vehicle.get("chckList");
	        
	        if (ChckListStr != null && !ChckListStr.trim().isEmpty()) {
                // JSON 문자열을 List<Map> 객체로 변환
                List<Map<String, Object>> chckList = objectMapper.readValue(
                	ChckListStr, 
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                vehicle.put("chckList", chckList);
	        } else {
	            vehicle.put("chckList", null);
	        }
	        
        	String bkdnCdListStr = (String) vehicle.get("bkdnCdList");
	        
	        if (bkdnCdListStr != null && !bkdnCdListStr.trim().isEmpty()) {
                // JSON 문자열을 List<Map> 객체로 변환
                List<Map<String, Object>> bkdnCdList = objectMapper.readValue(
                    bkdnCdListStr, 
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                vehicle.put("bkdnCdList", bkdnCdList);
	        } else {
	            vehicle.put("bkdnCdList", null);
	        }
	    }
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	 /**
	  * AI 실시간 진단 차량 점검 목록
	  */
	@Override
	public EgovMap selectVehicleChkList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = diagnosisRealtimeMapper.selectVehicleChkList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	 /**
	  * AI 실시간 진단 차량 고장 목록
	  */
	@Override
	public EgovMap selectVehicleBkdnList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = diagnosisRealtimeMapper.selectVehicleBkdnList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}

	 /**
	  * AI 실시간 진단 차량 점검 지침서
	  */
	@Override
	public EgovMap selectChckManual(HashMap<String, Object> requestMap) throws Exception {
	    EgovMap result = diagnosisRealtimeMapper.selectChckManual(requestMap);
	    return result;
	}
	
	 /**
	  * AI 실시간 진단 차량 고장 지침서
	  */
	@Override
	public EgovMap selectBkDnManual(HashMap<String, Object> requestMap) throws Exception {
		EgovMap result = diagnosisRealtimeMapper.selectBkDnManual(requestMap);
	    return result;
	}
	
	 /**
	  * AI 실시간 진단 - 알림 정보 조회
	  */
	@Override
	public EgovMap selectVehicleChckAlarmInfo(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = diagnosisRealtimeMapper.selectVehicleChckAlarmInfo(requestMap);
		ObjectMapper objectMapper = new ObjectMapper();
	    for (EgovMap vehicle : list) {
	        String ChckListStr = (String) vehicle.get("chckList");
	        
	        if (ChckListStr != null && !ChckListStr.trim().isEmpty()) {
                // JSON 문자열을 List<Map> 객체로 변환
                List<Map<String, Object>> chckList = objectMapper.readValue(
                	ChckListStr, 
                    new TypeReference<List<Map<String, Object>>>() {}
                );
                vehicle.put("chckList", chckList);
	        } else {
	            vehicle.put("chckList", null);
	        }
	    }
		EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
}
