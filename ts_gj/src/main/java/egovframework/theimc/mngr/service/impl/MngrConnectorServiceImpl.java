package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.mngr.service.MngrConnectorService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class MngrConnectorServiceImpl extends EgovAbstractServiceImpl implements MngrConnectorService {
	
	
	@Autowired
    private MngrConnectorMapper mngrConnectorMapper;
	
	 /**
	  * 단말기 현황 - 목록 조회
	  */
	public EgovMap selectConnectorStatusList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = mngrConnectorMapper.selectConnectorStatusList(requestMap);
	    int totCnt = mngrConnectorMapper.selectConnectorStatusListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
}
