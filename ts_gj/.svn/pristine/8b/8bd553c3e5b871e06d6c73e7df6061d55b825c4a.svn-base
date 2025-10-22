package egovframework.theimc.scheduler.service.impl;

import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.scheduler.service.SchedulerService;

@Service
public class SchedulerServiceImpl extends EgovAbstractServiceImpl implements SchedulerService {
	
	@Autowired
    private SchedulerMapper schedulerMapper;

	/**
	  * 문자 알림 - AI 월간 보고서
	  */
	@Override
	public EgovMap selectAiRptRcptnList() throws Exception {
		List<EgovMap> list = schedulerMapper.selectAiRptRcptnList();
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}

	/**
	  * 문자 알림 - 차량점검
	  */
	@Override
	public EgovMap selectChckRcptnList() throws Exception {
		List<EgovMap> list = schedulerMapper.selectChckRcptnList();
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	
}
