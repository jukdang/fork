package egovframework.theimc.vehicle.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * 차량 정비 - 정기검사 이력 매퍼 클래스
 */
@Mapper
public interface InspectionHistoryMapper {
	 /**
	  * 정기검사 이력 - 목록 조회
	  */
	List<EgovMap> selectInspectionHist(HashMap<String, Object> requestMap) throws Exception;
	 /**
	  * 정기검사 이력 - 목록 건수
	  */
	int selectInspectionHistCnt(HashMap<String, Object> requestMap) throws Exception;
	/**
	  * 정기검사 이력 - 상세 고장코드 조회
	  */
	List<EgovMap> selectInspectionHistBkdnResult(HashMap<String, Object> requestMap) throws Exception;
	/**
	  * 정기검사 이력 - 상세 점검이력 조회
	  */
	List<EgovMap> selectInspectionHistChckResult(HashMap<String, Object> requestMap) throws Exception;
	
}
