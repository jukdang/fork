package egovframework.theimc.vehicle.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * sample에 관한 데이터처리 매퍼 클래스
 *
 * @author  표준프레임워크센터
 * @since 2014.01.24
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *          수정일          수정자           수정내용
 *  ----------------    ------------    ---------------------------
 *   2014.01.24        표준프레임워크센터          최초 생성
 *
 * </pre>
 */
@Mapper
public interface RepairAssistMapper {

	 /**
	  * 채팅방 생성
	  */
	int insertChatList(HashMap<String, Object> requestMap) throws Exception;

	/**
  	* 채팅방 조회
  	*/
	EgovMap selectChatInfo(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅방 내용 조회
  	*/
	List<EgovMap> selectChatCnList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 저장
  	*/
	int insertChatContent(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 피드백 수정
  	*/
	int updateChatCnFeedBack(HashMap<String, Object> requestMap) throws Exception;
	
	/**
  	* 채팅 내용 목록 조회
  	*/
	List<EgovMap> selectChatPromptList(HashMap<String, Object> requestMap) throws Exception;

	/**
  	* 채팅 내용 목록 건수 조회
  	*/
	int selectChatPromptListCnt(HashMap<String, Object> requestMap) throws Exception;

	/**
  	* 일별 건수 조회
  	*/
	List<EgovMap> selectChatPromptLineList(HashMap<String, Object> requestMap) throws Exception;
}
