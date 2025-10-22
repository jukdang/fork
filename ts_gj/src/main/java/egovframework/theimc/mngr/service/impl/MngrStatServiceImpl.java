package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.mngr.service.MngrStatService;

@Service("MngrStatService")
public class MngrStatServiceImpl extends EgovAbstractServiceImpl implements MngrStatService  {
	
	@Autowired
    private MngrStatMapper mngrStatMapper;
	
	/**
	 * 일별 접속 사용자 수 조회
	 * 
	 * <p>특정 날짜 또는 기간 동안 접속한 사용자 수를 일별로 조회하는 기능</p>
	 * 
	 * @param requestMap 조회 조건이 담긴 맵(검색 날짜 또는 기간)
	 * @return 일별 접속 사용자 수가 포함된 List<EgovMap> 객체
	 * @throws Exception 처리 중 예외 발생 시
	 */
	@Override
	public List<EgovMap> selectConnUserCnt(HashMap<String, Object> requestMap) throws Exception {
	    return mngrStatMapper.selectConnUserCnt(requestMap);
	}
	
	/**
	 * 일별 최대 동시 접속자 수 조회
	 * 
	 * <p>특정 날짜 또는 기간 동안 최대 동시 접속자 수를 일별로 조회하는 기능</p>
	 * 
	 * @param requestMap 조회 조건이 담긴 맵(검색 날짜 또는 기간)
	 * @return 일별 접속 사용자 수가 포함된 List<EgovMap> 객체
	 * @throws Exception 처리 중 예외 발생 시
	 */
	@Override
	public List<EgovMap> selectMaxConcurrentUserCnt(HashMap<String, Object> requestMap) throws Exception {
	    return mngrStatMapper.selectMaxConcurrentUserCnt(requestMap);
	}
	
	 /**
	  * 이용 통계 - 로그인 현황
	  */
	@Override
	public EgovMap selectLoginStat(HashMap<String, Object> requestMap) throws Exception {
		
		List<EgovMap> ownrLoginlist = mngrStatMapper.selectOwnrLoginStat(requestMap);
		requestMap.put("order", "many");
		List<EgovMap> loginManyRankStat = mngrStatMapper.selectLoginRankStat(requestMap);
		requestMap.put("order", "recent");
		List<EgovMap> loginRecentStat = mngrStatMapper.selectLoginRankStat(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    
	    resultMap.put("ownrLoginlist", ownrLoginlist);
	    resultMap.put("loginManyRankStat", loginManyRankStat);
	    resultMap.put("loginRecentStat", loginRecentStat);
	    return resultMap;
	}
	
	 /**
	  * 이용 통계 - 전체 접속 현황
	  */
	@Override
	public EgovMap selectTotalLoginStat(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> connUserCnt = mngrStatMapper.selectTotalLoginStat(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("connUserCnt", connUserCnt);
	    return resultMap;
	}
	
	/**
	  * 이용 통계 - 메뉴별 접속현황
	  */
	@Override
	public EgovMap selectMenuUseStat(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> menuUseStat = mngrStatMapper.selectMenuUseStat(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("menuUseStat", menuUseStat);
	    return resultMap;
	}

	/**
	  * 변화량 통계 - 전체 변화량
	  */
	@Override
	public EgovMap selectVariationStat(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = mngrStatMapper.selectVariationStat(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	/**
	  * 개인정보 접속 로그 저장
	  */
	@Override
	public int insertStatPrivacyAccess(HashMap<String, Object> requestMap) throws Exception {
        return mngrStatMapper.insertStatPrivacyAccess(requestMap);
	}
}
