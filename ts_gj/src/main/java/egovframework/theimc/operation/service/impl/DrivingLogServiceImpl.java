package egovframework.theimc.operation.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.operation.service.DrivingLogService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class DrivingLogServiceImpl extends EgovAbstractServiceImpl implements DrivingLogService{
	
	@Autowired
    private DrivingLogMapper drivingLogMapper;
	
	 /**
	  * 운행 일지 - 목록 조회
	  */
	@Override
	public EgovMap selectDrivingLogList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = drivingLogMapper.selectDrivingLogList(requestMap);
	    int totCnt = drivingLogMapper.selectDrivingLogListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
	/**
	  * 운행 일지 - 상세
	  */
	@Override
	public EgovMap selectDrivingLogDetail(HashMap<String, Object> requestMap) throws Exception {
	    EgovMap result = drivingLogMapper.selectDrivingLogDetail(requestMap);
		return result;
	}

	/**
  	* - 운행 일지 - 상세 목록
  	*/
	@Override
	public EgovMap selectDrivingLogDetailList(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = drivingLogMapper.selectDrivingLogDetailList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
}
