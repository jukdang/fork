package egovframework.theimc.vehicle.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.pagination.PageInfoUtil;
import egovframework.theimc.vehicle.service.RepairAssistService;

@Service
public class RepairAssistServiceImpl extends EgovAbstractServiceImpl implements RepairAssistService{
	
	@Autowired
    private RepairAssistMapper repairAssistMapper;
	
	/**
	 * 채팅방 생성
	 */
	@Override
	public EgovMap insertChatList(HashMap<String, Object> requestMap) throws Exception {
	    int result = repairAssistMapper.insertChatList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", requestMap);
	    resultMap.put("result", result);
	    return resultMap;
	}
	
	/**
	 * 채팅방 조회
	 */
	@Override
	public EgovMap selectChatInfo(HashMap<String, Object> requestMap) throws Exception {
		EgovMap chatInfo = repairAssistMapper.selectChatInfo(requestMap);
		List<EgovMap> chatCnList = repairAssistMapper.selectChatCnList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("chatInfo", chatInfo);
	    resultMap.put("chatCnList", chatCnList);
	    return resultMap;
	}
	
	/**
	 * 채팅 내용 저장
	 */
	@Override
	public EgovMap insertChatContent(HashMap<String, Object> requestMap) throws Exception {
	    int result = repairAssistMapper.insertChatContent(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", requestMap);
	    resultMap.put("result", result);
	    return resultMap;
	}

	/**
	 * 채팅 내용 피드백 수정
	 */
	@Override
	public EgovMap updateChatCnFeedBack(HashMap<String, Object> requestMap) throws Exception {
	    int result = repairAssistMapper.updateChatCnFeedBack(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", requestMap);
	    resultMap.put("result", result);
	    return resultMap;
	}
	
	/*
	 * 채팅 내용 목록 조회
	 */
	@Override
	public EgovMap selectChatPromptList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = repairAssistMapper.selectChatPromptList(requestMap);
	    int totCnt = repairAssistMapper.selectChatPromptListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	/*
	 * 일별 건수 조회
	 */
	@Override
	public EgovMap selectChatPromptLineList(HashMap<String, Object> requestMap) throws Exception {
		List<EgovMap> list = repairAssistMapper.selectChatPromptLineList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
}
