package egovframework.theimc.mngr.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * 배차일지 관한 데이터처리 매퍼 클래스
 */
@Mapper
public interface MngrScheduleMapper {

	/**
	  * RFID 관리 - 등록 
	  */
	int insertFile(HashMap<String, Object> requestMap);

	/**
	 * 배차일지 관리 - 배차일지 목록
	 */
	List<EgovMap> selectScheduleList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 배차일지 관리 - 배차일지 목록 건수
	 */
	int selectScheduleListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 배차일지 관리 - 배차일지 파일 정보
	 */
	EgovMap selectScheduleFileInfo(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 배차일지 관리 - 배차일지 삭제할 파일 정보
	 */
	List<EgovMap> selectScheduleFileInfoList(HashMap<String, Object> requestMap);
	
	/**
	 * 배차일지 관리 - 배차일지 파일 정보 삭제
	 */
	int deleteScheduleFiles(HashMap<String, Object> requestMap);
	
}
