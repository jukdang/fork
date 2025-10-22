package egovframework.theimc.operation.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.operation.service.DrivingStatusService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class DrivingStatusServiceImpl extends EgovAbstractServiceImpl implements DrivingStatusService{

	
	@Autowired
    private DrivingStatusMapper drivingStatusMapper;

	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 목록
	  */
	@Override
	public EgovMap selectRteList(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = drivingStatusMapper.selectRteList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 목록
	  */
	@Override
	public EgovMap selectDrivingHistList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> coord = drivingStatusMapper.selectRteCoord(requestMap);
		List<EgovMap> statistics = drivingStatusMapper.selectDrivingHistStatistics(requestMap);
		List<EgovMap> list = drivingStatusMapper.selectDrivingHistList(requestMap);
	    int totCnt = drivingStatusMapper.selectDrivingHistListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("coord", coord);
	    resultMap.put("statistics", statistics);
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
	/**
	  * 운행현황판 - 노선 운행 현황 - 노선 운행 상세 목록
	  */
	@Override
	public EgovMap selectRouteHistDetailList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = drivingStatusMapper.selectRouteHistDetailList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}

	/**
	  * 운행현황판 - 위험 운행 지도 - 위험운행좌표
	  */
	@Override
	public EgovMap selectDgrCrdList(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = drivingStatusMapper.selectDgrCrdList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
}
