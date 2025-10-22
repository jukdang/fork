package egovframework.theimc.operation.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface DrivingLogService {

	/**
  	* - 운행 일지 - 목록 조회
  	*/
	EgovMap selectDrivingLogList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운행 일지 - 상세
	  */
	EgovMap selectDrivingLogDetail(HashMap<String, Object> requestMap) throws Exception;

	/**
  	* - 운행 일지 - 상세 목록
  	*/
	EgovMap selectDrivingLogDetailList(HashMap<String, Object> requestMap) throws Exception;
 
}