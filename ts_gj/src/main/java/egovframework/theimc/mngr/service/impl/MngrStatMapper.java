package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface MngrStatMapper {
	/**
	 * 일별 접속 사용자 수 조회
	 * 
	 * <p>특정 날짜 또는 기간 동안 접속한 사용자 수를 일별로 조회하는 기능</p>
	 * 
	 * @param requestMap 조회 조건이 담긴 맵(검색 날짜 또는 기간)
	 * @return 일별 접속 사용자 수가 포함된 List<EgovMap> 객체
	 * @throws Exception 처리 중 예외 발생 시
	 */
	List<EgovMap> selectConnUserCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 일별 최대 동시 접속자 수 조회
	 * 
	 * <p>특정 날짜 또는 기간 동안 최대 동시 접속자 수를 일별로 조회하는 기능</p>
	 * 
	 * @param requestMap 조회 조건이 담긴 맵(검색 날짜 또는 기간)
	 * @return 일별 접속 사용자 수가 포함된 List<EgovMap> 객체
	 * @throws Exception 처리 중 예외 발생 시
	 */
	List<EgovMap> selectMaxConcurrentUserCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 이용 통계 - 로그인 현황(운수회사별)
	 */
	List<EgovMap> selectOwnrLoginStat(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 이용 통계 - 로그인 현황 - 순위(접속 많은순, 최근 접속순)
	 */
	List<EgovMap> selectLoginRankStat(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 이용 통계 - 전체 접속 현황
	 */
	List<EgovMap> selectTotalLoginStat(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 이용 통계 - 메뉴별 접속현황
	 */
	List<EgovMap> selectMenuUseStat(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 변화량 통계 - 전체 변화량
	 */
	List<EgovMap> selectVariationStat(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 개인정보 접속 로그 저장
	 */
	int insertStatPrivacyAccess(HashMap<String, Object> requestMap) throws Exception;
}
