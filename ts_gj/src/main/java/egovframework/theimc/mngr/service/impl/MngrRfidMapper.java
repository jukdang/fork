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
public interface MngrRfidMapper {

	/**
	  * 운전자 RFID 정보 목록
	  */
	List<EgovMap> selectOperatorRfidList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운전자 RFID 정보 건수
	  */
	int selectOperatorRfidListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * RFID 중복 검사
	  */
	List<EgovMap> chkRfidNo(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * RFID 관리 - 등록 
	  */
	int insertRfidOprtr(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * RFID 관리 - 운전원 아이디 마지막 순번 조회
	  */
	String getLastOprtrSeq(String oprtrPrefix) throws Exception;
	
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
	List<EgovMap> selectRfidTagOperatorList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * RFID 태그 기록 - 운전자 목록 건수
	  */
	int selectRfidTagOperatorListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * RFID 태그 기록 - 태그 목록
	  */
	List<EgovMap> selectRfidTagList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * RFID 태그 기록 - 태그 목록 건수
	  */
	int selectRfidTagListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * RFID 태그 기록 - 삭제
	  */
	int deleteOperatorRfidList(HashMap<String, Object> requestMap) throws Exception;
	
}
