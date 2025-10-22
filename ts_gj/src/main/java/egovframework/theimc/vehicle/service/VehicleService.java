package egovframework.theimc.vehicle.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

/**
 * 차량 공통 서비스
 */
public interface VehicleService {

	 /**
	  * 차량번호로 차량 목록 검색
	  */
	EgovMap selectRegNoList(HashMap<String, Object> requestMap) throws Exception;
 
	
	 /**
	  * 차량아이디로 차량 정보 조회
	  */
	EgovMap selectVehicleInfo(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 차량유형으로 센서 검색
	  */
	EgovMap selectSensorInfo(HashMap<String, Object> requestMap) throws Exception;
}