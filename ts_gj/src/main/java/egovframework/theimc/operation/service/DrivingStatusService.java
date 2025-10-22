package egovframework.theimc.operation.service;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface DrivingStatusService {

	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 목록
	  */
	EgovMap selectRteList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 목록
	  */
	EgovMap selectDrivingHistList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 상세 목록
	  */
	EgovMap selectRouteHistDetailList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * 운행현황판 - 위험 운행 지도 - 위험운행좌표
	  */
	EgovMap selectDgrCrdList(HashMap<String, Object> requestMap) throws Exception;
	
 
}