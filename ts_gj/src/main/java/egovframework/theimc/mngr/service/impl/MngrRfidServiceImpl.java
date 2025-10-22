package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.mngr.service.MngrRfidService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class MngrRfidServiceImpl extends EgovAbstractServiceImpl implements MngrRfidService{
	
	@Autowired
    private MngrRfidMapper rfidMngrMapper;
	
	 /**
	  * 운전자 RFID 정보 목록
	  */
	public EgovMap selectOperatorRfidList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = rfidMngrMapper.selectOperatorRfidList(requestMap);
	    int totCnt = rfidMngrMapper.selectOperatorRfidListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
	
	 /**
	  * RFID 중복 검사
	  */
	public EgovMap chkRfidNo(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> resultList = rfidMngrMapper.chkRfidNo(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", resultList);
	    return resultMap;
	}
	
	 /**
	  * RFID 관리 - 등록 
	  */
	public int insertRfidOprtr(HashMap<String, Object> requestMap) throws Exception {
		String ownrSelect = requestMap.get("ownrSelect").toString();
	    String oprtrPrefix;
	    
	    // 운수회사 한글명을 이니셜 대문자로 변환
	    switch (ownrSelect) {
	        case "대원버스":
	            oprtrPrefix = "DW";
	            break;
	        case "대진운수":
	            oprtrPrefix = "DJ";
	            break;
	        case "대창운수":
	            oprtrPrefix = "DC";
	            break;
	        case "동화운수":
	            oprtrPrefix = "DH";
	            break;
	        case "라정시내":
	            oprtrPrefix = "LJ";
	            break;
	        case "삼아교통":
	            oprtrPrefix = "SA";
	            break;
	        case "삼원운수":
	            oprtrPrefix = "SW";
	            break;
	        case "세영운수":
	            oprtrPrefix = "SY";
	            break;
	        case "을로운수":
	            oprtrPrefix = "EL";
	            break;
	        case "현대교통":
	            oprtrPrefix = "HD";
	            break;
	        default:
	            throw new Exception("유효하지 않은 운수회사입니다.");
	    }
	    
	    // 마지막 순번 조회
	    String lastSeq = rfidMngrMapper.getLastOprtrSeq(oprtrPrefix);

		 // 다음 순번 생성 (null이면 1, 있으면 +1)
		 int nextSeq = (lastSeq == null || lastSeq.isEmpty()) ? 1 : Integer.parseInt(lastSeq) + 1;
		 String seqStr = String.format("%04d", nextSeq);
	
		 // 운전원 ID 생성 (예: DW0001)
		 String oprtrId = oprtrPrefix + seqStr;
	    
	    requestMap.put("oprtrId", oprtrId);
	    
	    int result = rfidMngrMapper.insertRfidOprtr(requestMap);
	    return result;
	}
	
	 /**
	  * RFID 관리 - 운전자 RFID 정보
	  */
	public EgovMap selectOperatorRfidResult(HashMap<String, Object> requestMap) throws Exception {
		EgovMap result = rfidMngrMapper.selectOperatorRfidResult(requestMap);
	    return result;
	}
	
	 /**
	  * RFID 관리 - 수정 
	  */
	public int updateRfidOprtr(HashMap<String, Object> requestMap) throws Exception {
	    int result = rfidMngrMapper.updateRfidOprtr(requestMap);
	    return result;
	}

	 /**
	  * RFID 태그 기록 - 운전자 목록
	  */
	public EgovMap selectRfidTagOperatorList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = rfidMngrMapper.selectRfidTagOperatorList(requestMap);
	    int totCnt = rfidMngrMapper.selectRfidTagOperatorListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}

	 /**
	  * RFID 태그 기록 - 태그 목록
	  */
	public EgovMap selectRfidTagList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = rfidMngrMapper.selectRfidTagList(requestMap);
	    int totCnt = rfidMngrMapper.selectRfidTagListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
	 /**
	  * RFID 관리 - 삭제 
	  */
	public int deleteOperatorRfidList(HashMap<String, Object> requestMap) throws Exception {
	    int result = rfidMngrMapper.deleteOperatorRfidList(requestMap);
	    return result;
	}
}
