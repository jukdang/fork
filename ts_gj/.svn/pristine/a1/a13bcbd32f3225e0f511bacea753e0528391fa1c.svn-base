package egovframework.theimc.mngr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

@Mapper
public interface MngrConnectorMapper {

	/**
	 * 이용 통계 - 메뉴별 접속현황
	 */
	List<EgovMap> selectConnectorStatusList(HashMap<String, Object> requestMap) throws Exception;

	/**
	 * 변화량 통계 - 전체 변화량
	 */
	int selectConnectorStatusListCnt(HashMap<String, Object> requestMap) throws Exception;
}
