package egovframework.theimc.mngr.service.impl;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import egovframework.theimc.config.aop.BusinessException;
import egovframework.theimc.config.aop.CommonException;
import egovframework.theimc.mngr.service.MngrScheduleService;
import egovframework.theimc.pagination.PageInfoUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MngrScheduleServiceImpl extends EgovAbstractServiceImpl implements MngrScheduleService {
	
	@Autowired
    private MngrScheduleMapper mngrScheduleMapper;
    
    @Value("${file.upload-dir}")
    private String uploadPath;
    
    @Override
    public int saveFile(MultipartFile file, HashMap<String, Object> requestMap) {
        try {
        	
	        // 파일 정보 추출
	        String orgFileName = file.getOriginalFilename();
	        String fileExt = orgFileName.substring(orgFileName.lastIndexOf(".") + 1).toLowerCase();
	        String fileName = UUID.randomUUID().toString() + "." + fileExt;
	        String fileSize = String.valueOf(file.getSize());
	        
	        // 업로드 디렉토리 생성
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdirs();
	        }
	        
	        // 파일 저장
	        String filePath = uploadPath + fileName;
	        File saveFile = new File(filePath);
	        file.transferTo(saveFile);
	        
	        // DB 저장
	        requestMap.put("orgFileNm", orgFileName);
	        requestMap.put("orgFileNm", orgFileName);
	        requestMap.put("fileNm", fileName);
	        requestMap.put("filePath", filePath);
	        requestMap.put("fileSz", fileSize);
	        requestMap.put("fileExt", fileExt);
	        
	        mngrScheduleMapper.insertFile(requestMap);
        
        } catch (CommonException e) {
            throw new BusinessException("파일 등록 중 오류가 발생했습니다.");
        } catch (Exception e) {
        	if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "MngrScheduleServiceImpl", "saveFile", "Exception", e);
            }
            throw new BusinessException("파일 등록 중 오류가 발생했습니다.");
        }
        
        return Integer.parseInt(requestMap.get("fileSn").toString());
    }
    

    /**
	 * 배차일지 관리 - 배차일지 목록
	 */
    @Override
	public EgovMap selectScheduleList(HashMap<String, Object> requestMap) throws Exception {
		int currentPageNo = PageInfoUtil.setPageParams(requestMap, 10);
		
		List<EgovMap> list = mngrScheduleMapper.selectScheduleList(requestMap);
	    int totCnt = mngrScheduleMapper.selectScheduleListCnt(requestMap);
	    
	    EgovMap resultMap = new EgovMap();
	    PaginationInfo paginationInfo = PageInfoUtil.setPageInfo(requestMap, currentPageNo, totCnt, 10);
	    
	    resultMap.put("list", list);
	    resultMap.put("paginationInfo", paginationInfo);
	    return resultMap;
	}
	
    /**
	 * 배차일지 관리 - 배차일지 파일 정보
	 */
	@Override
	public EgovMap getScheduleFileInfo(HashMap<String, Object> requestMap) throws Exception {
	    return mngrScheduleMapper.selectScheduleFileInfo(requestMap);
	}
	
    /**
	 * 배차일지 관리 - 배차일지 파일 삭제
	 */
	@Override
	@Transactional
	public void deleteScheduleFiles(HashMap<String, Object> requestMap) {
	    try {
	        List<String> fileSns = (List<String>) requestMap.get("fileSns");
	        
	        if (fileSns == null || fileSns.isEmpty()) {
	            throw new BusinessException("삭제할 파일이 선택되지 않았습니다.");
	        }
	        
	        // 삭제하기 전 실제 파일 경로 조회
	        List<EgovMap> fileInfoList = mngrScheduleMapper.selectScheduleFileInfoList(requestMap);
	        
	        // DB에서 파일 정보 삭제
	        int deleteCount = mngrScheduleMapper.deleteScheduleFiles(requestMap);
	        
	        if (deleteCount > 0) {
	            // 실제 파일 시스템에서 파일 삭제
	            for (EgovMap fileInfo : fileInfoList) {
	                String filePath = fileInfo.get("filePath").toString();
	                
	                if (filePath != null) {
	                    File file = new File(filePath);
	                    if (file.exists() && file.isFile()) {
	                        boolean deleted = file.delete();
	                        if (!deleted) {
                                log.error("파일 삭제 실패: " + file.getAbsolutePath());
	                        }
	                    }
	                }
	            }
	        } else {
	            throw new BusinessException("삭제할 파일을 찾을 수 없습니다.");
	        }
	    } catch (CommonException e) {
	    	throw new BusinessException("파일 삭제 중 예상치 못한 오류가 발생했습니다.");
	    } catch (Exception e) {
        	if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "MngrScheduleServiceImpl", "deleteScheduleFiles", "Exception", e);
            }
            throw new BusinessException("파일 삭제 중 예상치 못한 오류가 발생했습니다.");
        }
	}
}
