package egovframework.theimc.vehicle.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.pagination.PageInfoUtil;
import egovframework.theimc.vehicle.service.DiagnosisHistoryService;
/**
 * 차량 정비 - 진단 내역 서비스 클래스
 */
@Service
public class DiagnosisHistoryServiceImpl extends EgovAbstractServiceImpl implements DiagnosisHistoryService{
	@Autowired
    private DiagnosisHistoryMapper diagnosisHistoryMapper;
	@Autowired
    private VehicleMapper vehiclMapper;
	/** 
	  * 진단 내역 - 목록 조회
	  */
	@Override
	public EgovMap selectDiagnosisHist(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 16);
		
		List<EgovMap> list = vehiclMapper.selectVehicleHist(requestMap);
	    int totCnt = vehiclMapper.selectVehicleHistCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 16);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}

	/** 
	  * 진단 내역 - 상세 조회
	  */
	@Override
	public EgovMap selectDiagnosisHistResult(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> bkdnList = diagnosisHistoryMapper.selectDiagnosisHistBkdnResult(requestMap);
		List<EgovMap> chckList = diagnosisHistoryMapper.selectDiagnosisHistChckResult(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("bkdnList", bkdnList);
	    resultMap.put("chckList", chckList);
	    return resultMap;
	}
}
