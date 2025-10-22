package egovframework.theimc.vehicle.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface DiagnosisRealtimeService {

	/**
  	* AI 실시간 진단 차량 상태 목록
  	*/
	EgovMap selectVehicleSttsList(HashMap<String, Object> requestMap) throws Exception;

	/**
  	* AI 실시간 진단 차량 점검 목록
  	*/
	EgovMap selectVehicleChkList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* AI 실시간 진단 차량 고장 목록
  	*/
	EgovMap selectVehicleBkdnList(HashMap<String, Object> requestMap) throws Exception;

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
	EgovMap selectVehicleChckAlarmInfo(HashMap<String, Object> requestMap) throws Exception;
}