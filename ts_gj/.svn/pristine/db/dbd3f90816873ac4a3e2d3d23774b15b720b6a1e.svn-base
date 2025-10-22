package egovframework.theimc.dashboard.service;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface DashboardService {
	/**
	 * 대시보드 - 금일 운행현황 - 현재 운행 차량
	 */
	EgovMap selectOprVhclCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 차량 실시간 위치
	 */
	EgovMap selectLatestGpsCoordinates(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 운행현황 추이
	 */
	EgovMap selectOprStatusTrend(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 대시보드 - 차량 정보
	 */
	EgovMap selectVhclInfo(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 대시보드 - 서비스 이용현황
	 */
	EgovMap selectServiceUsageStatusByUsers(HashMap<String, Object> requestMap) throws Exception;
}
