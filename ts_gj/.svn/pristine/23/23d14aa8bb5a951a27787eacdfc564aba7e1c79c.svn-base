package egovframework.theimc.operation.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.operation.service.AiReportService;
import egovframework.theimc.pagination.PageInfoUtil;

@Service
public class AiReportServiceImpl extends EgovAbstractServiceImpl implements AiReportService{
	
	@Autowired
    private AiReportMapper aiReportMapper;

	@Override
	public EgovMap selectAiReportList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = aiReportMapper.selectAiReportList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}

	@Override
	public EgovMap selectAiReportResult(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = aiReportMapper.selectAiReportResult(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	
}
