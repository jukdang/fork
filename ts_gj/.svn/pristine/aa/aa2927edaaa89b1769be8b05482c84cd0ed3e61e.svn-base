package egovframework.theimc.sample.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.pagination.PageInfoUtil;
import egovframework.theimc.sample.service.TheimcSampleService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TheimcSampleServiceImpl extends EgovAbstractServiceImpl implements TheimcSampleService {
	
	@Autowired
    private TheimcSampleMapper theimcSampleMapper;
	
	@Override
	public EgovMap selectSampleList(HashMap<String, Object> requestMap) throws Exception {
	    
	    // 1. 페이징 파라미터 먼저 설정 (firstRecordIndex, recordCountPerPage 등이 requestMap에 추가됨)
	    int currentPageNo = PageInfoUtil.setPageParams(requestMap);
	    
	    // 2. 전체 건수 조회 (검색 조건 적용)
	    int totCnt = theimcSampleMapper.selectSampleListTotCnt(requestMap);
	    
	    // 3. 목록 조회 (이제 requestMap에 페이징 파라미터가 있음)
	    List<EgovMap> list = theimcSampleMapper.selectSampleList(requestMap);
	    
	    // 4. 완전한 PaginationInfo 객체 생성
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt);
	    
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
}
