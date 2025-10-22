package egovframework.theimc.operation.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AiReportMapper {

	 /**
	  * 운행행태 - AI + 리포트 목록
	  * @param ownrNm 운수회사
	  * @param year 보고서연
	  * @return 검색된 리포트 목록
	  */
	List<EgovMap> selectAiReportList(HashMap<String, Object> requestMap) throws Exception;

	 /**
	  * 운행행태 - AI + 리포트 상세 데이터
	  * @param anlsAiRptSn 보고서 순번
	  * @return 검색된 리포트 상세 데이터
	  */
	List<EgovMap> selectAiReportResult(HashMap<String, Object> requestMap) throws Exception;

}
