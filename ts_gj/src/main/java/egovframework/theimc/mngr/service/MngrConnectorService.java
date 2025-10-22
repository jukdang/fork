package egovframework.theimc.mngr.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;

public interface MngrConnectorService {
	/**
	 * 단말기 현황 - 목록 조회
	 */
	EgovMap selectConnectorStatusList(HashMap<String, Object> requestMap) throws Exception;
}
