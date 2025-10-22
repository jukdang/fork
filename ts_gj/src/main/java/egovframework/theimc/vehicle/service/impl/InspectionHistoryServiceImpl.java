package egovframework.theimc.vehicle.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.pagination.PageInfoUtil;
import egovframework.theimc.vehicle.service.InspectionHistoryService;
/**
 * 차량 정비 - 정기검사 이력 서비스 클래스
 */
@Service
public class InspectionHistoryServiceImpl extends EgovAbstractServiceImpl implements InspectionHistoryService{
	@Autowired
    private VehicleMapper vehiclMapper;
	@Autowired
    private InspectionHistoryMapper InspectionHistoryMapper;
	/** 
	  * 정기검사 이력 - 목록 조회
	  */
	@Override
	public EgovMap selectInspectionHist(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = vehiclMapper.selectVehicleHist(requestMap);
	    int totCnt = vehiclMapper.selectVehicleHistCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}

	/** 
	  * 정기검사 이력 - 상세 조회
	  */
	@Override
	public EgovMap selectInspectionHistResult(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> bkdnList = InspectionHistoryMapper.selectInspectionHistBkdnResult(requestMap);
		List<EgovMap> chckList = InspectionHistoryMapper.selectInspectionHistChckResult(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("bkdnList", bkdnList);
	    resultMap.put("chckList", chckList);
	    return resultMap;
	}
}
