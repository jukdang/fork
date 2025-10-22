package egovframework.theimc.vehicle.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface InspectionHistoryService {
	/**
  	* 정기검사 이력 - 목록 조회
  	*/
	EgovMap selectInspectionHist(HashMap<String, Object> requestMap) throws Exception;
	/**
  	* 정기검사 이력 - 상세 조회
  	*/
	EgovMap selectInspectionHistResult(HashMap<String, Object> requestMap) throws Exception;
}