package egovframework.theimc.admin.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.egovframe.rte.fdl.property.EgovPropertyService;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;
import egovframework.theimc.mbr.service.MbrService;

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
public class MngrController {
	
	@Autowired
	private AuthHelper authHelper;
	
	/**
	 * 회원 서비스
	 */
	@Autowired
	private MbrService mbrService;
    
	/**
	 * 비밀번호 암호화 인코더
	 */
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EgovPropertyService propertiesService;
	
	
	@Value("${file.upload-dir}")
    private String uploadDir;
	
	@GetMapping("/")
	public String main(Model model) throws Exception {
		
	    EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "메인 페이지");
    	pageInfo.put("mainTitle", "메인 제목");
    	
    	model.addAttribute("pageInfo", pageInfo);
    	
		return "/mngr/main/main";
	}
	
	/**
	 * 회원 목록 페이지 이동
	 * 
	 * <p>회원 목록을 조회하는 페이지로 이동합니다.
	 * 페이징 처리된 회원 목록을 조회하여 모델에 추가합니다.</p>
	 * 
	 * @param requestMap 요청 파라미터를 담은 맵
	 * @param model 뷰에 전달할 데이터를 담는 모델 객체
	 * @param request HTTP 요청 객체
	 * @param message 리다이렉트 시 전달된 메시지
	 * @return 회원 목록 템플릿 페이지 경로
	 * @throws Exception 회원 목록 조회 중 발생할 수 있는 예외
	 */
	@RequestMapping("/mbr/mbrList")
	public String mbrList(@RequestParam HashMap<String, Object> requestMap, ModelMap model, 
	                     HttpServletRequest request, @ModelAttribute("message") String message) throws Exception {
	    
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "회원 관리");
	    pageInfo.put("pageMenuName", "account");
	    pageInfo.put("pageSubMenuName", "accountManage");
	    
        // HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
        
	    // 검색 조건 처리
	    if (!requestMap.containsKey("searchType") || requestMap.get("searchType") == null || "".equals(requestMap.get("searchType"))) {
	        requestMap.put("searchType", "mbrId"); // 기본값은 '아이디'
	    }

	    if (!requestMap.containsKey("searchRole") || requestMap.get("searchRole") == null || "".equals(requestMap.get("searchRole"))) {
	        requestMap.put("searchRole", ""); // 기본값은 빈 문자열로 설정 (권한이 없으면 전체 검색)
	    }
	    
	    // 페이징 설정
	    int pageUnit = propertiesService.getInt("pageUnit");
	    int pageSize = propertiesService.getInt("pageSize");
	    int pageIndex = 1;
	    
	    requestMap.put("pageUnit", pageUnit);
	    requestMap.put("pageSize", pageSize);
	    
	    // 페이징 정보 설정
	    PaginationInfo paginationInfo = new PaginationInfo();
	    paginationInfo.setCurrentPageNo(pageIndex);
	    paginationInfo.setRecordCountPerPage(pageUnit);
	    paginationInfo.setPageSize(pageSize);
	    
	    // 페이지 인덱스가 요청에 포함된 경우 페이징 정보 업데이트
	    if (requestMap.containsKey("pageIndex")) {
	        pageIndex = Integer.parseInt(requestMap.get("pageIndex").toString());
	        paginationInfo.setCurrentPageNo(pageIndex);
	    }
	    
	    // 페이징을 위한 인덱스 설정
	    requestMap.put("firstRecordIndex", paginationInfo.getFirstRecordIndex());
	    requestMap.put("lastRecordIndex", paginationInfo.getLastRecordIndex());
	    requestMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
	    
	    // 회원 목록 조회
	    List<MbrVO> mbrList = mbrService.selectMbrList(requestMap);
	    // 전체 회원 수 설정
	    paginationInfo.setTotalRecordCount(mbrService.selectMbrTotalCnt(requestMap));
	    
	    // 모델에 데이터 추가
	    model.addAttribute("param", requestMap);
	    model.addAttribute("pageInfo", pageInfo);
	    model.addAttribute("mbrList", mbrList);
	    model.addAttribute("paginationInfo", paginationInfo);
	    model.addAttribute("message", message);
	    
	    return "/mngr/mbr/mbrList";
	}
    
	/**
     * 회원 등록 폼 페이지 이동 (관리자용)
     * 
     * <p>회원 등록 폼이 있는 페이지로 이동합니다. 빈 회원 VO 객체를 모델에 추가합니다.</p>
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원등록 템플릿 페이지 경로
     */
    @GetMapping(value = "/mbr/mbrInsertForm")
    public String mbrInsertForm(ModelMap model) {
    	
    	HashMap<String, Object> requestMap = new HashMap<>();
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(requestMap, model);
    	
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "회원 등록");
	    pageInfo.put("pageMenuName", "account");
	    pageInfo.put("pageSubMenuName", "accountManage");
    	
    	// pageInfo
		model.addAttribute("pageInfo", pageInfo);
    	model.addAttribute("mbrVO", new MbrVO());
        return "/mngr/mbr/mbrInsertForm";
    }
    
    /**
     * 회원 등록 처리 (관리자용)
     * 
     * <p>회원 등록 폼에서 제출된 데이터를 처리하고 회원을 등록합니다.
     * 유효성 검사를 통과하지 못한 경우 오류 메시지와 함께 폼으로 되돌립니다.</p>
     * 
     * @param mbrVO 등록할 회원 정보
     * @param errors 유효성 검증 결과
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param redirectAttributes 리다이렉트 시 전달할 데이터
     * @return 성공 시 메인 페이지로 리다이렉트, 실패 시 회원 등록 폼
     * @throws Exception 회원 등록 중 발생할 수 있는 예외
     */
    @PostMapping(value = "/mbr/mbrInsert")
    public String mbrInsert(@Valid MbrVO mbrVO, Errors errors, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(mbrVO, model);
    	if (errors.hasErrors()) {
    		/* 회원등록 실패시 입력 데이터 값을 유지 */            
    		model.addAttribute("mbrVO", mbrVO);             
    		/* 유효성 통과 못한 필드와 메시지를 핸들링 */            
    		Map<String, String> validatorResult = validateHandling(errors);            
    		for (String key : validatorResult.keySet()) {                
    			model.addAttribute(key, validatorResult.get(key));            
    			}           
    		/* 회원가입 페이지로 다시 리턴 */           
    		return "/mngr/mbr/mbrInsertForm";         
		}
        if(mbrVO.getDataFilter() == "self") {
        	mbrVO.setOwnrNm(mbrVO.getCurrentOwnrNm());
        }
    	int mbrIdCnt = mbrService.insertMbr(mbrVO);
    	
    	if(mbrIdCnt > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보가 성공적으로 등록되었습니다.");
            return "redirect:/mngr/mbr/mbrList";
    	}else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    		return "/mngr/mbr/mbrInsertForm";         
    	}
    	
    }

    /**
     * 유효성 검사 오류 메시지 처리
     * 
     * <p>Bean Validation 결과로 발생한 오류 메시지를 모델에 추가하기 좋은 형태로 변환합니다.
     * 각 필드별 오류 메시지를 'valid_필드명' 형식의 키로 맵에 저장합니다.</p>
     * 
     * @param errors 유효성 검증 결과
     * @return 필드별 오류 메시지를 담은 맵
     */
    public Map<String, String> validateHandling(Errors errors) {        
    	Map<String, String> validatorResult = new HashMap<>();         
    	for (FieldError error : errors.getFieldErrors()) {            
    		String validKeyName = String.format("valid_%s", error.getField());            
    		validatorResult.put(validKeyName, error.getDefaultMessage());        
		}        
		return validatorResult;    
	}
    
    /**
     * 회원정보 수정 페이지 이동 (관리자용)
     * 
     * <p>회원정보 수정 페이지로 이동합니다. 현재 로그인한 사용자의 정보를 모델에 추가합니다.</p>
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원정보 수정 템플릿 페이지 경로
     * @throws Exception 사용자 정보 조회 중 발생할 수 있는 예외
     */
    @PostMapping("/mbr/mbrUpdateForm")
    public String mbrUpdateForm(MbrVO mbrVO, ModelMap model) throws Exception {
    	
	    EgovMap pageInfo = new EgovMap();
	    pageInfo.put("pageTitle", "회원 정보 수정");
	    pageInfo.put("pageMenuName", "account");
	    pageInfo.put("pageSubMenuName", "accountManage");
    	
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(mbrVO, model);
    	
        MbrVO result = mbrService.selectMbr(mbrVO);
        if (result == null) {
            return "redirect:/mngr/mbr/mbrList";
        }
        model.addAttribute("mbrVO", result);
    	model.addAttribute("pageInfo", pageInfo);
        return "/mngr/mbr/mbrUpdateForm";
    }
    
    /**
     * 회원정보 수정 처리 (관리자용)
     * 
     * <p>회원정보 수정 폼에서 제출된 데이터를 처리하고 회원 정보를 업데이트합니다.
     * 비밀번호 변경이 요청된 경우 현재 비밀번호 확인 후 새 비밀번호로 변경합니다.</p>
     * 
     * @param mbrVO 수정할 회원 정보
     * @param errors 유효성 검증 결과
     * @param currentPswd 현재 비밀번호
     * @param newPswd 새 비밀번호
     * @param confirmPswd 새 비밀번호 확인
     * @param redirectAttributes 리다이렉트 시 전달할 데이터
     * @return 성공 시 메인 페이지로 리다이렉트, 실패 시 회원정보 수정 폼
     * @throws Exception 회원정보 수정 중 발생할 수 있는 예외
     */
    @PostMapping("/mbr/mbrUpdate")
    public String mbrUpdate(@Valid ModifyMbrVO mbrVO,//MbrVO mbrVO, 
    					  Errors errors, 
                          @RequestParam(required = false) String newPswd,
                          @RequestParam(required = false) String confirmPswd,
                          RedirectAttributes redirectAttributes, ModelMap model) throws Exception {

    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "회원정보 수정 처리 (관리자용)");
    	
    	// pageInfo
		model.addAttribute("pageInfo", pageInfo);
        
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(mbrVO, model);
    	
        // 1. 검증 실패 처리
        if (errors.hasErrors()) {
        	String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("mbrVO", mbrVO);
            model.addAttribute("errorMessage", errorMessage);
            return "/mngr/mbr/mbrUpdateForm"; // 검증 실패 시 다시 폼으로
        }
    	
        // 비밀번호 변경 처리
        if (newPswd != null && !newPswd.isEmpty()) {
            // 새 비밀번호와 확인 비밀번호 일치 여부 확인
            if (!newPswd.equals(confirmPswd)) {
                model.addAttribute("errorMessage", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                model.addAttribute("mbrVO", mbrVO);
                return "/mngr/mbr/mbrUpdateForm";
            }
            
            // 새 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(newPswd);
            mbrVO.setMbrPswd(encodedPassword);
        } else {
            // 비밀번호 변경을 원하지 않는 경우, 기존 비밀번호 유지
            mbrVO.setMbrPswd("");
        }
        
        // 회원정보 수정
        int result = mbrService.updateMbr(mbrVO);
        
        if (result > 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "회원정보가 성공적으로 수정되었습니다.");
            return "redirect:/mngr/mbr/mbrList";
        } else {
        	model.addAttribute("errorMessage", "회원정보 수정에 실패했습니다.");
            model.addAttribute("mbrVO", mbrVO);
            return "/mngr/mbr/mbrUpdateForm";
        }
    }
    
    /**
     * 회원정보 삭제 처리 (관리자용)
     * 
     * <p>회원정보 수정 폼에서 제출된 데이터를 처리하고 회원 정보를 업데이트합니다.
     * 비밀번호 변경이 요청된 경우 현재 비밀번호 확인 후 새 비밀번호로 변경합니다.</p>
     * 
     * @param mbrVO 삭제할 회원 정보
     * @throws Exception 회원정보 수정 중 발생할 수 있는 예외
     */
    @PostMapping("/mbr/mbrDelete")
    public String mbrDelete(MbrVO mbrVO, RedirectAttributes redirectAttributes) throws Exception {
        
    	// 선택된 회원이 없는 경우
        if (mbrVO.getSelectedSns() == null || mbrVO.getSelectedSns().length == 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "삭제할 회원을 선택해주세요.");
            return "redirect:/mngr/mbr/mbrList";
        }
        
        // 배열을 List로 변환
        int deletedCount = mbrService.deleteMbrArray(mbrVO);
        
        if (deletedCount > 0) {
            redirectAttributes.addFlashAttribute("message", 
                deletedCount + "명의 회원이 성공적으로 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "회원 삭제에 실패했습니다.");
        }
        
        return "redirect:/mngr/mbr/mbrList";
    }
    
    /**
     * 시스템 모니터링
     * @return 게시물 수정 페이지
     */
    @GetMapping(value = "/stat/sysMonitor")
    public String sysMonitor(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
    	
		EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "통계 및 현황 모니터링");
    	pageInfo.put("pageMenuName", "stat");
    	pageInfo.put("pageSubMenuName", "sysMonitor");
		
		model.addAttribute("pageInfo", pageInfo);
        return "/mngr/stat/sysMonitor";
    }
}