package egovframework.theimc.vehicle.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

/**
 * 차량 정비 - AI 실시간 진단 서비스 인터페이스
 */
public interface RepairAssistService {

	/**
  	* 채팅방 생성
  	*/
	EgovMap insertChatList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅방 조회
  	*/
	EgovMap selectChatInfo(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 저장
  	*/
	EgovMap insertChatContent(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 피드백 수정
  	*/
	EgovMap updateChatCnFeedBack(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 목록 조회
  	*/
	EgovMap selectChatPromptList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 일별 건수 조회
  	*/
	EgovMap selectChatPromptLineList(HashMap<String, Object> requestMap) throws Exception;
}