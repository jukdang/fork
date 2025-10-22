package egovframework.theimc.mngr.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface MngrRfidService {

	 /**
	  * 운전자 RFID 정보 목록
	  */
	EgovMap selectOperatorRfidList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 중복 검사
	  */
	EgovMap chkRfidNo(HashMap<String, Object> requestMap) throws Exception;
	
	 /**
	  * RFID 관리 - 등록 
	  */
	int insertRfidOprtr(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 관리 - 운전자 RFID 정보
	  */
	EgovMap selectOperatorRfidResult(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 관리 - 수정 
	  */
	int updateRfidOprtr(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 태그 기록 - 운전자 목록
	  */
	EgovMap selectRfidTagOperatorList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 태그 기록 - 태그 목록
	  */
	EgovMap selectRfidTagList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * RFID 태그 기록 - 삭제
	  */
	int deleteOperatorRfidList(HashMap<String, Object> requestMap) throws Exception;
}