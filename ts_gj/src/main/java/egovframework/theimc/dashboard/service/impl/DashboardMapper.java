package egovframework.theimc.dashboard.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface DashboardMapper {

	/**
	 * 대시보드 - 금일 운행현황 - 현재 운행 차량
	 */
	EgovMap selectOprVhclCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 차량 실시간 위치
	 */
	List<EgovMap> selectLatestGpsCoordinates(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 금일 운행현황 - 현재 운행 현황
	 */
	EgovMap selectOprVhclStatusCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 운행현황 추이
	 */
	List<EgovMap> selectOprStatusTrend(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 대시보드 - 차량 정보- 현재 운행 현황
	 */
	List<EgovMap> selectVhclOprInfo(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 대시보드 - 서비스 이용현황
	 */
	List<EgovMap> selectServiceUsageStatusByUsers(HashMap<String, Object> requestMap) throws Exception;
}
