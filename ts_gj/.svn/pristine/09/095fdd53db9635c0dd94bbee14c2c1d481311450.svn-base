package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.mngr.service.MngrAlertService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class MngrAlertServiceImpl extends EgovAbstractServiceImpl implements MngrAlertService{
	
	@Autowired
    private MngrAlertMapper mngrAlertMapper;
	
	@Autowired
    private MngrStatMapper mngrStatMapper;

	 /**
	  * 알림 수신관리 - 수신자 목록
	  */
	public EgovMap selectRecipientList(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		List<EgovMap> list = mngrAlertMapper.selectRecipientList(requestMap);
		
		requestMap.put("ipAddr", getClientIp(request));
        requestMap.put("flag", "smrd");
        requestMap.put("svcNm", "알림 내역 - 수신자 목록");
        mngrStatMapper.insertStatPrivacyAccess(requestMap);
        
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	/**
	  * 알림 수신관리 - 수신자 등록
	  */
	public int insertRecipient(HashMap<String, Object> requestMap) throws Exception {
		int result = 0;
		
		int chckRcptnYCnt = mngrAlertMapper.selectChckRcptnYCnt(requestMap);
		if(requestMap.get("chckRcptnYn") != null && 
		   "Y".equals(requestMap.get("chckRcptnYn").toString()) && 
		   chckRcptnYCnt >= 3) {
		    return 2;
		}
//		System.out.println("chckRcptnYn 값: [" + requestMap.get("chckRcptnYn") + "]");
//		System.out.println("chckRcptnYCnt 값: " + chckRcptnYCnt);
		int aiRptRcptnYCnt = mngrAlertMapper.selectAiRptRcptnYCnt(requestMap);
		if(requestMap.get("aiRptRcptnYn") != null && 
		   "Y".equals(requestMap.get("aiRptRcptnYn").toString()) && 
		   aiRptRcptnYCnt >= 3) {
		    return 2;
		}
		result = mngrAlertMapper.insertRecipient(requestMap);
	    return result;
	}
	
	/**
	  * 알림 수신관리 - 수신자 삭제
	  */
	public int deleteRecipientList(HashMap<String, Object> requestMap) throws Exception {
		int result = mngrAlertMapper.deleteRecipientList(requestMap);
	    return result;
	}
	
	/**
	  * 알림 수신관리 - 수신자 조회
	  */
	public EgovMap selectRecipient(HashMap<String, Object> requestMap) throws Exception {
		EgovMap Result = mngrAlertMapper.selectRecipient(requestMap);
	    return Result;
	}
	
	/**
	  * 알림 수신관리 - 수신자 수정
	  */
	public int updateRecipient(HashMap<String, Object> requestMap) throws Exception {
		int result = 0;
		
		int chckRcptnYCnt = mngrAlertMapper.selectChckRcptnYCnt(requestMap);
		if(requestMap.get("chckRcptnYn") != null && 
		   "Y".equals(requestMap.get("chckRcptnYn").toString()) && 
		   chckRcptnYCnt >= 3) {
		    return 2;
		}
//		System.out.println("chckRcptnYn 값: [" + requestMap.get("chckRcptnYn") + "]");
//		System.out.println("chckRcptnYCnt 값: " + chckRcptnYCnt);
		int aiRptRcptnYCnt = mngrAlertMapper.selectAiRptRcptnYCnt(requestMap);
		if(requestMap.get("aiRptRcptnYn") != null && 
		   "Y".equals(requestMap.get("aiRptRcptnYn").toString()) && 
		   aiRptRcptnYCnt >= 3) {
		    return 2;
		}
		result = mngrAlertMapper.updateRecipient(requestMap);
	    return result;
	}
	
	
	/*
	 * 알림 수신관리 - 수신자 목록
	 */
	@Override
	public EgovMap selectAlertReceptionHstoryList(HashMap<String, Object> requestMap, HttpServletRequest request) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		List<EgovMap> list = mngrAlertMapper.selectAlertReceptionHstoryList(requestMap);
	    int totCnt = mngrAlertMapper.selectAlertReceptionHstoryListCnt(requestMap);
	    
		requestMap.put("ipAddr", getClientIp(request));
        requestMap.put("flag", "smrh");
        requestMap.put("svcNm", "알림 내역 - 목록 조회");
        mngrStatMapper.insertStatPrivacyAccess(requestMap);
        
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
	/**
	  * 알림 내역 - 수신 이력 등록
	  */
	public int insertRecipientHstry(HashMap<String, Object> requestMap) throws Exception {
		int result = mngrAlertMapper.insertRecipientHstry(requestMap);
	    return result;
	}
	
	private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
