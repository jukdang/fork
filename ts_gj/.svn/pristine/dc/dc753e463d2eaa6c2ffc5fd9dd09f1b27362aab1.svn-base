package egovframework.theimc.mngr.service;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface MngrAlertService {
	
	/**
	  * 알림 수신관리 - 수신자 목록
	  */
	EgovMap selectRecipientList(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception;
	
	/**
	  * 알림 수신관리 - 수신자 등록
	  */
	int insertRecipient(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 알림 수신관리 - 수신자 삭제
	  */
	int deleteRecipientList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 수신관리 - 수신자 조회
	  */
	EgovMap selectRecipient(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 수신관리 - 수신자 수정
	  */
	int updateRecipient(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 알림 수신관리 - 수신자 목록
	  */
	EgovMap selectAlertReceptionHstoryList(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception;
	
	/**
	  * 알림 내역 - 수신 이력 등록
	  */
	int insertRecipientHstry(HashMap<String, Object> requestMap) throws Exception;
}