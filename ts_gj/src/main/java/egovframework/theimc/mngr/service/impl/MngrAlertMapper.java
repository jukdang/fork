package egovframework.theimc.mngr.service.impl;
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
public interface MngrAlertMapper {

	/**
	  * 알림 수신관리 - 수신자 등록
	  */
	int insertRecipient(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 수신관리 - 점검 알림 건수 조회
	  */
	int selectChckRcptnYCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 수신관리 - AI 리포트 건수 조회
	  */
	int selectAiRptRcptnYCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 수신관리 - 수신자 목록
	  */
	List<EgovMap> selectRecipientList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  *  알림 수신관리 - 수신자 삭제
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
	List<EgovMap> selectAlertReceptionHstoryList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  *  알림 수신관리 - 수신자 건수
	  */
	int selectAlertReceptionHstoryListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 알림 내역 - 수신 이력 등록
	  */
	int insertRecipientHstry(HashMap<String, Object> requestMap) throws Exception;
	
}
