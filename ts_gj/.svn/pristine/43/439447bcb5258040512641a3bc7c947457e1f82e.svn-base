package egovframework.theimc.mngr.web;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.mngr.service.MngrConnectorService;
import egovframework.theimc.util.CommonExcelDownload;
import egovframework.theimc.util.StatusMsg;

/**
 * 관리자 관련 요청을 처리하는 컨트롤러
 * 
 * <p>관리자 요청을 처리하는 컨트롤러입니다.</p>
 * 
 * @author  시스템 관리자
 * @version 1.0
 * @since   2025.05.01
 * @see     egovframework.theimc.mbr.service.MbrService
 * @see     egovframework.theimc.login.service.LoginService
 */
@Controller
@RequestMapping(value="/mngr")
public class MngrConnectorController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private MngrConnectorService mngrConnectorService;
	
	/**
	 * 단말기 현황
	 */
	@RequestMapping(value = "/connectorStatus")
	public String connectorStatus(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "단말기 현황");
	    pageInfo.put("pageMenuName", "status");
	    pageInfo.put("pageSubMenuName", "unitStatus");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/connector/connectorStatus";
	}
	
	/**
	 * 단말기 현황 - 목록 조회
	 */
	@RequestMapping(value = "/getConnectorStatusList")
	public ResponseEntity<StatusMsg> getConnectorStatusList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrConnectorService.selectConnectorStatusList(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}

	/**
	 * 프롬프트 현황 - 데이터 다운로드
	 */
	@RequestMapping(value = "/connectorStatusDownload", method = RequestMethod.POST)
	@ResponseBody
	public void connectorStatusDownload(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) 
	        throws Exception {
	    
	    // 전체 데이터 조회를 위해 searchType을 download로 설정
	    requestMap.put("searchType", "download");
	    authHelper.addAuthToMap(requestMap);
	    
	    // 데이터 조회
	    EgovMap resultMap = mngrConnectorService.selectConnectorStatusList(requestMap);
	    List<EgovMap> dataList = (List<EgovMap>) resultMap.get("list");
	    
	    // 컬럼 매핑 설정 (DB컬럼명 -> 한글명)
	    Map<String, String> columnMapping = new LinkedHashMap<>();
	    columnMapping.put("regNo", "차량번호");          
	    columnMapping.put("ownrNm", "운수회사");           
	    columnMapping.put("cntrEndDt", "마지막 데이터 수신");         
	    
	    // 컬럼 너비 설정
	    Map<String, Integer> columnWidths = new HashMap<>();
	    columnWidths.put("regNo", 3000);    
	    columnWidths.put("ownrNm", 3000);     
	    columnWidths.put("cntrEndDt", 4000);  
	    
	    // Excel 생성 및 다운로드
	    CommonExcelDownload excelUtil = new CommonExcelDownload();
	    excelUtil.generateExcelDownload(
	        dataList,           // 데이터
	        columnMapping,      // 컬럼 매핑
	        columnWidths,       // 컬럼 너비
	        "단말기현황",     // 시트명
	        "단말기현황",      // 파일명
	        response           // 응답 객체
	    );
	}
}