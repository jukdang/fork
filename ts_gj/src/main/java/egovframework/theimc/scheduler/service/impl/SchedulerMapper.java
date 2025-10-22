package egovframework.theimc.scheduler.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface SchedulerMapper {
	
	/**
	  * 문자 알림 - AI 월간 보고서
	  */
	List<EgovMap> selectAiRptRcptnList() throws Exception;
	
	/**
	  * 문자 알림 - 차량점검
	  */
	List<EgovMap> selectChckRcptnList() throws Exception;

}
