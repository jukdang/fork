package egovframework.theimc.mngr.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import egovframework.theimc.config.aop.BusinessException;
import egovframework.theimc.config.aop.CommonException;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.mngr.service.MngrScheduleService;
import egovframework.theimc.util.StatusMsg;
import lombok.extern.slf4j.Slf4j;

/**
 * 관리자
 */
@Slf4j
@Controller
@RequestMapping(value="/mngr")
public class MngrScheduleController {
	
	@Autowired
	private AuthHelper authHelper;
	
    @Autowired
    private MngrScheduleService mngrScheduleService;
	
	/**
	 * 배차일지 관리
	 */
	@RequestMapping(value = "/scheduleManage")
	public String scheduleManage(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "배차일지 관리");
	    pageInfo.put("pageMenuName", "schedule");
	    pageInfo.put("pageSubMenuName", "scheduleManage");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/schedule/scheduleManage";
	}
	
	/**
	 * 배차일지 등록
	 */
	@RequestMapping(value = "/scheduleInsertForm")
	public String scheduleInsertForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "배차일지 등록");
	    pageInfo.put("pageMenuName", "schedule");
	    pageInfo.put("pageSubMenuName", "scheduleRegister");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/schedule/scheduleInsertForm";
	}

	/**
	 * 배차일지 등록 - 파일 업로드
	 */
    @PostMapping("/uploadFile")
    @ResponseBody
    public Map<String, Object> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam HashMap<String, Object> requestMap, ModelMap model) {
        Map<String, Object> result = new HashMap<>();
        authHelper.addAuthToMapAndModel(requestMap, model);
        try {
            if (file.isEmpty()) {
                result.put("success", false);
                result.put("message", "파일이 선택되지 않았습니다.");
                return result;
            }
            
            // 파일 저장
            int fileSn = mngrScheduleService.saveFile(file, requestMap);
            
            result.put("success", true);
            result.put("message", "파일이 성공적으로 등록되었습니다.");
            result.put("fileSn", fileSn);
            
        } catch (CommonException e) {
        	result.put("success", false);
            result.put("message", "파일 등록 중 오류가 발생했습니다.");
	    } catch (Exception e) {
        	if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "MngrScheduleServiceController", "deleteScheduleFiles", "Exception", e);
            }
        	result.put("success", false);
            result.put("message", "파일 등록 중 오류가 발생했습니다.");
        }
        
        return result;
    }
    
	
    /**
	 * 배차일지 관리 - 배차일지 목록
	 */
	@RequestMapping(value = "/getScheduleList")
	public ResponseEntity<StatusMsg> getRfidTagOperatorList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrScheduleService.selectScheduleList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 배차일지 관리 - 배차일지 파일 정보
	 */
	@RequestMapping(value = "/downloadScheduleFile", method = RequestMethod.GET)
	public void downloadScheduleFile(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) throws Exception {
		
		EgovMap fileInfo = mngrScheduleService.getScheduleFileInfo(requestMap);
	    
	    if(fileInfo != null) {
	        String filePath = fileInfo.get("filePath").toString();
	        String orgFileNm = fileInfo.get("orgFileNm").toString();
	        
	        File file = new File(filePath);
	        
	        if(file.exists()) {
	            response.setContentType("application/octet-stream");
	            response.setHeader("Content-Disposition", "attachment; filename=\"" + 
	                             URLEncoder.encode(orgFileNm, "UTF-8") + "\"");
	            response.setContentLength((int) file.length());
	            
	            try (FileInputStream fis = new FileInputStream(file);
	                 OutputStream os = response.getOutputStream()) {
	                
	                byte[] buffer = new byte[1024];
	                int bytesRead;
	                while ((bytesRead = fis.read(buffer)) != -1) {
	                    os.write(buffer, 0, bytesRead);
	                }
	                os.flush();
	            }
	        }
	    }
	}
	
	/**
	* 배차일지 관리 - 배차일지 파일 삭제
	*/
	@RequestMapping(value = "/deleteScheduleFiles", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<StatusMsg> deleteScheduleFiles(@RequestParam HashMap<String, Object> requestMap) {
	    try {
	        // fileSns 파라미터 처리 (콤마로 구분된 문자열을 List로 변환)
	        String fileSnsStr = (String) requestMap.get("fileSns");
	        if (fileSnsStr != null && !fileSnsStr.trim().isEmpty()) {
	            String[] fileSnsArray = fileSnsStr.split(",");
	            List<String> fileSns = Arrays.asList(fileSnsArray);
	            requestMap.put("fileSns", fileSns);
	            
	            mngrScheduleService.deleteScheduleFiles(requestMap);
	            return StatusMsg.ok("삭제가 완료되었습니다.");
	        } else {
	            return StatusMsg.fail("삭제할 파일이 선택되지 않았습니다.");
	        }
	    } catch (CommonException e) {
	    	throw new BusinessException("파일 삭제 중 예상치 못한 오류가 발생했습니다.");
	    } catch (Exception e) {
        	if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "MngrScheduleServiceController", "deleteScheduleFiles", "Exception", e);
            }
            throw new BusinessException("파일 삭제 중 예상치 못한 오류가 발생했습니다.");
        }
	}
}