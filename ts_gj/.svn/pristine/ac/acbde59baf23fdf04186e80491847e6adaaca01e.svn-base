package egovframework.theimc.mngr.service;

import java.util.HashMap;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.web.multipart.MultipartFile;

public interface MngrScheduleService {
	/**
	 * 파일 저장
	 */
	int saveFile(MultipartFile file, HashMap<String, Object> requestMap);

	/**
	 * 배차일지 관리 - 배차일지 목록
	 */
	EgovMap selectScheduleList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 배차일지 관리 - 배차일지 파일 정보
	 */
	public EgovMap getScheduleFileInfo(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 배차일지 관리 - 배차일지 파일 삭제
	 */
	void deleteScheduleFiles(HashMap<String, Object> requestMap);
}
