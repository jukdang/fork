package egovframework.theimc.vehicle.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * 차량 정비 - AI 실시간 진단 매퍼 클래스
 */
@Mapper
public interface DiagnosisRealtimeMapper {

	 /**
	  * AI 실시간 진단 차량 상태 목록
	  */
	List<EgovMap> selectVehicleSttsList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * AI 실시간 진단 차량 점검 목록
	  */
	List<EgovMap> selectVehicleChkList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * AI 실시간 진단 차량 고장 목록
	  */
	List<EgovMap> selectVehicleBkdnList(HashMap<String, Object> requestMap) throws Exception;
	
	 /**
	  * AI 실시간 진단 차량 점검 지침서
	  */
	EgovMap selectChckManual(HashMap<String, Object> requestMap) throws Exception;
	
	 /**
	  * AI 실시간 진단 차량 고장 지침서
	  */
	EgovMap selectBkDnManual(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * AI 실시간 진단 - 알림 정보 조회
	  */
	List<EgovMap> selectVehicleChckAlarmInfo(HashMap<String, Object> requestMap) throws Exception;
	
}
