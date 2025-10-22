package egovframework.theimc.operation.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DrivingLogMapper {

	 /**
	  * 운행 일지 - 목록 조회
	  * @param ownrNm 운수회사
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @param oprtrNm 운전원명
	  * @return 검색된 운행 일지 목록
	  */
	List<EgovMap> selectDrivingLogList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운행 일지 - 목록 갯수
	  * @param ownrNm 운수회사
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @param oprtrNm 운전원명
	  * @return 검색된 운행 일지 목록 갯수
	  */
	int selectDrivingLogListCnt(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 운행 일지 - 상세
	  * @param oprHstrySn 운행이력순번
	  * @return 검색된 운행 일지 상세 데이터
	  */
	EgovMap selectDrivingLogDetail(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 운행 일지 - 상세 목록
	  * @param oprHstrySnList 운행이력순번 리스트
	  * @return 검색된 운행 일지 상세 데이터
	  */
	List<EgovMap> selectDrivingLogDetailList(HashMap<String, Object> requestMap) throws Exception;
	
}
