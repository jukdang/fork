package egovframework.theimc.vehicle.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * 차량 정비 - 진단 내역 매퍼 클래스
 */
@Mapper
public interface DiagnosisHistoryMapper {
	/**
	  * 진단 내역 - 상세 고장코드 조회
	  */
	List<EgovMap> selectDiagnosisHistBkdnResult(HashMap<String, Object> requestMap) throws Exception;
	/**
	  * 진단 내역 - 상세 점검이력 조회
	  */
	List<EgovMap> selectDiagnosisHistChckResult(HashMap<String, Object> requestMap) throws Exception;
	
}
