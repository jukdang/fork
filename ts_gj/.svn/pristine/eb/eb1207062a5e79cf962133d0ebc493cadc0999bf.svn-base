package egovframework.theimc.mngr.web;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.mngr.service.MngrStatService;
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
public class MngrStatController {
	
	@Autowired
	private AuthHelper authHelper;
	
	@Autowired
	private MngrStatService mngrStatService;
	
	/**
	    /**
     * 이용현황 통계
     * @return 이용현황 통계 페이지
     */
    @GetMapping(value = "/svcUseStat")
    public String svcUseStat(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
    	
		EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "통계 및 현황 모니터링");
    	pageInfo.put("pageMenuName", "stat");
    	pageInfo.put("pageSubMenuName", "svcUseStat");
		
		model.addAttribute("pageInfo", pageInfo);
        return "/mngr/stat/svcUseStat";
    }
    
    /**
     * 일별 접속 사용자 수 조회
     * 
     * <p>특정 날짜에 접속한 사용자 수를 조회하는 기능</p>
     * 
     * @param requestMap 조회 조건이 담긴 맵(검색 날짜 등)
     * @param model 화면에 전달할 모델 객체
     * @param request HTTP 요청 객체
     * @return jsonView 이름의 뷰
     * @throws Exception 처리 중 예외 발생 시
     */
    @PostMapping("/api/connUserCnt")
    public String connUserCnt(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
    	
    	List<EgovMap> resultList = mngrStatService.selectConnUserCnt(requestMap);
		model.addAttribute("data", resultList);
    	
    	return "jsonView";
    }
    
    /**
     * 일별 최대 동시 접속자 수 조회
     * 
     * <p>특정 날짜의 최대 동시 접속자 수를 조회하는 기능</p>
     * 
     * @param requestMap 조회 조건이 담긴 맵(검색 날짜 등)
     * @param model 화면에 전달할 모델 객체
     * @param request HTTP 요청 객체
     * @return jsonView 이름의 뷰
     * @throws Exception 처리 중 예외 발생 시
     */
    @PostMapping("/api/maxConcurrentUserCnt")
    public String maxConcurrentUserCnt(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
        
        List<EgovMap> resultList = mngrStatService.selectMaxConcurrentUserCnt(requestMap);
        model.addAttribute("data", resultList);
        
        return "jsonView";
    }
    
	/**
	 * 이용 통계
	 */
	@RequestMapping(value = "/usageStatsUsage")
	public String usageStatsUsage(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "이용 통계");
	    pageInfo.put("pageMenuName", "usageStats");
	    pageInfo.put("pageSubMenuName", "usageStatsUsage");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/useStat/usageStatsUsage";
	}
	
	/**
	 * 이용 통계 - 로그인 현황
	 */
	@RequestMapping(value = "/getLoginStat")
	public ResponseEntity<StatusMsg> getRfidTagList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrStatService.selectLoginStat(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 이용 통계 - 전체 접속 현황
	 */
	@RequestMapping(value = "/getTotalLoginStat")
	public ResponseEntity<StatusMsg> getTotalLoginStat(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrStatService.selectTotalLoginStat(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 이용 통계 - 메뉴별 접속현황
	 */
	@RequestMapping(value = "/getMenuUseStat")
	public ResponseEntity<StatusMsg> getMenuUseStat(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrStatService.selectMenuUseStat(requestMap);
		//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
		if (resultList != null && !resultList.isEmpty()) {
			return StatusMsg.ok(resultList);
		}else {
			return StatusMsg.fail("조회 결과가 없습니다.");
		}
	}
	
	/**
	 * 변화량 통계
	 */
	@RequestMapping(value = "/usageStatsVariation")
	public String usageStatsVariation(@RequestParam HashMap<String, Object> requestMap, ModelMap model) throws Exception {
		authHelper.addAuthToMapAndModel(requestMap, model);
		EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "변화량 통계");
	    pageInfo.put("pageMenuName", "usageStats");
	    pageInfo.put("pageSubMenuName", "usageStatsVariation");
	    
	    model.addAttribute("pageInfo", pageInfo);
	    return "/mngr/useStat/usageStatsVariation";
	}
	
	/**
	 * 변화량 통계 - 전체 변화량
	 */
	@RequestMapping(value = "/getVariationStat")
	public ResponseEntity<StatusMsg> getVariationStat(@RequestParam HashMap<String, Object> requestMap) throws Exception {
		authHelper.addAuthToMap(requestMap);
		EgovMap resultList = mngrStatService.selectVariationStat(requestMap);
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
	@RequestMapping(value = "/variationStatDownload", method = RequestMethod.POST)
	@ResponseBody
	public void variationStatDownload(@RequestParam HashMap<String, Object> requestMap, HttpServletResponse response) 
	        throws Exception {
	    
	    authHelper.addAuthToMap(requestMap);
	    
	    // 데이터 조회
	    EgovMap resultMap = mngrStatService.selectVariationStat(requestMap);
	    List<EgovMap> dataList = (List<EgovMap>) resultMap.get("list");
	    
	    // 컬럼 매핑 설정 (DB컬럼명 -> 한글명)
	    Map<String, String> columnMapping = new LinkedHashMap<>();
	    columnMapping.put("anlsDt", "일자");          
	    columnMapping.put("cb", "총 탄소 배출량(kg)");          
	    columnMapping.put("idleTm", "총 공회전 시간(분)");           
	    columnMapping.put("avgFu06", "평균 연비(km/m³)");          
	    columnMapping.put("avgFu04", "평균 전비(km/kWh)");            
	    columnMapping.put("avgFu05", "평균 수소전비(km/kWh)");         
	    columnMapping.put("oprDstnc", "총 주행거리(km)");             
	    
	    // 컬럼 너비 설정
	    Map<String, Integer> columnWidths = new HashMap<>();
	    columnWidths.put("anlsDt", 3000);    // 소속
	    columnWidths.put("cb", 3000);     // ID
	    columnWidths.put("idleTm", 3000);   // 질문 (넓게)
	    columnWidths.put("avgFu06", 3000);    // 답변 (가장 넓게)
	    columnWidths.put("avgFu04", 3000);       // 평가
	    columnWidths.put("avgFu05", 3000);     // 등록일시
	    columnWidths.put("oprDstnc", 3000);     // 등록일시
	    
	    // Excel 생성 및 다운로드
	    CommonExcelDownload excelUtil = new CommonExcelDownload();
	    excelUtil.generateExcelDownload(
	        dataList,           // 데이터
	        columnMapping,      // 컬럼 매핑
	        columnWidths,       // 컬럼 너비
	        "변화량통계",     // 시트명
	        "변화량통계",      // 파일명
	        response           // 응답 객체
	    );
	}
}