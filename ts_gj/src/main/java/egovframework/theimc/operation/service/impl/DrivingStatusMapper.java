package egovframework.theimc.operation.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface DrivingStatusMapper {

	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 목록
	  * @return 노선 목록
	  */
	List<EgovMap> selectRteList(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 좌표
	  * @param rteIn 노선아이디
	  * @return 노선 좌표
	  */
	List<EgovMap> selectRteCoord(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 운행현황판 - 노선 운행 현황 - 통계데이터
	  * @param ownrNm 운수회사
	  * @param rteIn 노선아이디
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @return 운행횟수, 주행거리, 평균연비, 탄소배출량
	  */
	List<EgovMap> selectDrivingHistStatistics(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 목록
	  * @param ownrNm 운수회사
	  * @param rteIn 노선아이디
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @return 검색된 노선 운행 목록
	  */
	List<EgovMap> selectDrivingHistList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 목록 갯수
	  * @param ownrNm 운수회사
	  * @param rteIn 노선아이디
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @return 검색된 노선 운행 목록 갯수
	  */
	int selectDrivingHistListCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 상세 목록
	  * @param ownrNm 운수회사
	  * @param rteIn 노선아이디
	  * @param startDate 시작일
	  * @param endDate 종료일
	  * @return 검색된 노선 운행 상세 목록
	  */
	List<EgovMap> selectRouteHistDetailList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	  * 운행현황판 - 위험 운행 지도 - 위험운행좌표
	  * @param ownrNm 운수회사
	  * @param ocrnDt 발생일시
	  * @return 검색된 위험운행좌표 목록
	  */
	List<EgovMap> selectDgrCrdList(HashMap<String, Object> requestMap) throws Exception;
}
