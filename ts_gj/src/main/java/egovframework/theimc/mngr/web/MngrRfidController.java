package egovframework.theimc.mngr.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.theimc.mngr.service.MngrRfidService;
import egovframework.theimc.config.aop.BusinessException;
import egovframework.theimc.config.aop.CommonException;
import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.util.CommonExcelDownload;
import egovframework.theimc.util.StatusMsg;

/**
 * 관리자
 */
@Controller
@RequestMapping(value="/mngr")
public class MngrRfidController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private MngrRfidService rfidMngrService;
	
	/**
	 * RFID 관리 페이지
	 */
	@RequestMapping(value = "/rfidManage")
	public String rfidManage(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "RFID 관리");
	    pageInfo.put("pageMenuName", "rfid");
	    pageInfo.put("pageSubMenuName", "rfidManage");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/rfid/rfidManage";
	}
	/**
	 * RFID 관리 - 운전자 RFID 정보
	 */
	@RequestMapping(value = "/getOperatorRfidList")
	public ResponseEntity<StatusMsg> getOperatorRfidList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = rfidMngrService.selectOperatorRfidList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	
	/**
	 * RFID 관리 - 등록 페이지
	 */
	@RequestMapping(value = "/rfidOprtrInsertForm")
	public String rfidOprtrInsertForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "RFID 관리 - 등록");
	    pageInfo.put("pageMenuName", "rfid");
	    pageInfo.put("pageSubMenuName", "rfidManage");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/rfid/rfidOprtrInsertForm";
	}
	
	/**
	 * RFID 관리 - 등록 페이지
	 */
	@RequestMapping(value = "/chkRfidNo")
	public ResponseEntity<StatusMsg> chkRfidNo(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = rfidMngrService.chkRfidNo(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
    /**
     *  RFID 관리 - 등록 
     */
    @PostMapping(value = "/rfidOprtrInsert")
    public String rfidOprtrInsert(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	int mbrIdCnt = rfidMngrService.insertRfidOprtr(requestMap);
    	
    	if(mbrIdCnt > 0) {
    		redirectAttributes.addFlashAttribute("message", "RFID 등록에 성공하셨습니다.");
    	}else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    	}
    	
        return "redirect:/mngr/rfidManage";
    }
	
	/**
	 * RFID 관리 - 등록 페이지
	 */
	@RequestMapping(value = "/rfidOprtrUpdateForm")
	public String rfidOprtrUpdateForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "RFID 관리 - 수정");
	    pageInfo.put("pageMenuName", "rfid");
	    pageInfo.put("pageSubMenuName", "rfidManage");
	    
	    EgovMap result = rfidMngrService.selectOperatorRfidResult(requestMap);
	    
	    model.addAttribute("pageInfo", pageInfo);
	    model.addAttribute("result", result);
	    return "/mngr/rfid/rfidOprtrUpdateForm";
	}
	
    /**
     *  RFID 관리 - 등록 
     */
    @PostMapping(value = "/rfidOprtrUpdate")
    public String rfidOprtrUpdate(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	authHelper.addAuthToMapAndModel(requestMap, model);
    	int mbrIdCnt = rfidMngrService.updateRfidOprtr(requestMap);
    	
    	if(mbrIdCnt > 0) {
    		redirectAttributes.addFlashAttribute("message", "RFID 수정에 성공하셨습니다.");
    	}else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    	}
    	
        return "redirect:/mngr/rfidManage";
    }
	
	/**
	 * RFID 태그 기록 페이지
	 */
	@RequestMapping(value = "/rfidHistory")
	public String rfidHistory(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "RFID 태그 기록");
	    pageInfo.put("pageMenuName", "rfid");
	    pageInfo.put("pageSubMenuName", "rfidHistory");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/rfid/rfidHistory";
	}
	
	/**
	 * RFID 태그 기록 - 운전자 목록
	 */
	@RequestMapping(value = "/getRfidTagOperatorList")
	public ResponseEntity<StatusMsg> getRfidTagOperatorList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = rfidMngrService.selectRfidTagOperatorList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * RFID 태그 기록 - 태그 목록
	 */
	@RequestMapping(value = "/getRfidTagList")
	public ResponseEntity<StatusMsg> getRfidTagList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = rfidMngrService.selectRfidTagList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * RFID 태그 기록 - 다운로드
	 */
	@RequestMapping(value = "/rfidTagDownload", method = RequestMethod.POST)
	@ResponseBody
	public void rfidTagDownload(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) 
	        throws Exception {
	    
	    // 전체 데이터 조회를 위해 searchType을 download로 설정
	    authHelper.addAuthToMap(requestMap);
	    requestMap.put("searchType", "download");
	    // 데이터 조회
	    EgovMap resultMap = rfidMngrService.selectRfidTagList(requestMap);
	    List<EgovMap> dataList = (List<EgovMap>) resultMap.get("list");
	    
	    // 컬럼 매핑 설정 (DB컬럼명 -> 한글명)
	    Map<String, String> columnMapping = new LinkedHashMap<>();
	    columnMapping.put("rowNum", "No");           // 순번
	    columnMapping.put("mtId", "차량ID");           // 순번
	    columnMapping.put("rfidNo", "RFID번호");          // 소속
	    columnMapping.put("tagDt", "태그일시");            // ID
	    
	    // 컬럼 너비 설정
	    Map<String, Integer> columnWidths = new HashMap<>();
	    columnWidths.put("rowNum", 2000);    // No
	    columnWidths.put("mtId", 3000);    // 소속
	    columnWidths.put("rfidNo", 3000);     // ID
	    columnWidths.put("tagDt", 3000);   // 질문 (넓게)
	    
	    // Excel 생성 및 다운로드
	    CommonExcelDownload excelUtil = new CommonExcelDownload();
	    excelUtil.generateExcelDownload(
	        dataList,           // 데이터
	        columnMapping,      // 컬럼 매핑
	        columnWidths,       // 컬럼 너비
	        "태그정보",     // 시트명
	        "태그정보",      // 파일명
	        response           // 응답 객체
	    );
	}
	
	

	/**
	 * RFID 태그 기록 - 삭제
	 */
	@RequestMapping(value = "/deleteOperatorRfidList", method = RequestMethod.POST)
	@ResponseBody
    public ResponseEntity<StatusMsg> deleteOperatorRfidList(@RequestParam HashMap<String, Object> requestMap, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	authHelper.addAuthToMapAndModel(requestMap, model);
    	int resultCnt = rfidMngrService.deleteOperatorRfidList(requestMap);
    	
		if (resultCnt > 0) {
			return StatusMsg.ok(resultCnt);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
    }
}
