package egovframework.theimc.mbr.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import egovframework.theimc.config.login.AuthHelper;
import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;
import egovframework.theimc.mbr.service.MbrService;

/**
 * 회원 관련 요청을 처리하는 컨트롤러
 * 
 * <p>회원 등록, 정보 수정, 비밀번호 확인 등 회원 관련 웹 요청을 처리하는 컨트롤러입니다.
 * Spring Security를 활용한 인증 및 권한 처리도 함께 수행합니다.</p>
 * 
 * @author  시스템 관리자
 * @version 1.0
 * @since   2025.05.01
 * @see     egovframework.theimc.mbr.service.MbrService
 * @see     egovframework.theimc.login.service.LoginService
 */
@Controller
@RequestMapping(value="/mbr")
public class MbrController {
	
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
	
	/**
	 * 로그인 서비스
	 */
	@Autowired
	private LoginService loginService;
    
	/**
     * 회원 등록 폼 페이지전 이용약관 동의 이동 (사용자용)
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원등록 템플릿 페이지 경로
     */
    @GetMapping(value = "/mbrAgree")
    public String mbrAgree(Model model) {
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "회원 등록");
    	
    	// pageInfo
		model.addAttribute("pageInfo", pageInfo);
    	model.addAttribute("mbrVO", new MbrVO());
        return "/mbr/mbrAgree";
    }
	
	/**
     * 회원 등록 폼 페이지 이동 (사용자용)
     * 
     * <p>회원 등록 폼이 있는 페이지로 이동합니다. 빈 회원 VO 객체를 모델에 추가합니다.</p>
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원등록 템플릿 페이지 경로
     */
    @PostMapping(value = "/mbrInsertForm")
    public String mbrInsertForm(MbrVO mbrVO, Model model, RedirectAttributes redirectAttributes) {
    	
    	//개인정보 수집, 이용약관 동의 여부 체크
    	if (!"Y".equals(mbrVO.getPrvcClctAgreYn()) || !"Y".equals(mbrVO.getUtztnTrmsAgreYn())) {
    		redirectAttributes.addFlashAttribute("message", "이용약관, 개인정보 수집 및 활용에 동의 해주세요.");
            return "redirect:/mbr/mbrAgree";
        }
    	
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "회원 등록");
    	
    	// pageInfo
		model.addAttribute("pageInfo", pageInfo);
    	model.addAttribute("mbrVO", mbrVO);
        return "/mbr/mbrInsertForm";
    }

    /**
     * 회원 등록 처리 (사용자용)
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
    @PostMapping(value = "/mbrSelfInsert")
    public String mbrSelfInsert(@Valid MbrVO mbrVO, Errors errors, ModelMap model, RedirectAttributes redirectAttributes) throws Exception {
    	if (errors.hasErrors()) {
    		/* 회원가입 실패시 입력 데이터 값을 유지 */            
    		model.addAttribute("mbrVO", mbrVO);             
    		/* 유효성 통과 못한 필드와 메시지를 핸들링 */            
    		Map<String, String> validatorResult = validateHandling(errors);            
    		for (String key : validatorResult.keySet()) {                
    			model.addAttribute(key, validatorResult.get(key));            
    			}           
    		/* 회원가입 페이지로 다시 리턴 */           
    		return "/mbr/mbrInsertForm";         
		}
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(mbrVO, model);
    	int mbrIdCnt = mbrService.insertMbrSelf(mbrVO);
    	
    	if(mbrIdCnt > 0) {
    		return "/mbr/mbrResult";       
    	}else {
    		redirectAttributes.addFlashAttribute("message", "오류가 발생했습니다.");
    	}
    	
        return "redirect:/";
    }
    
 	/**
      * 회원 등록 성공 페이지 이동 (사용자용)
      * 
      * @param model 뷰에 전달할 데이터를 담는 모델 객체
      * @return 회원등록 성공 페이지 경로
      */
     @GetMapping(value = "/mbrResult")
     public String mbrResult(Model model) {
     	EgovMap pageInfo = new EgovMap();
     	pageInfo.put("pageTitle", "회원 등록 성공");
     	
     	// pageInfo
 		model.addAttribute("pageInfo", pageInfo);
     	model.addAttribute("mbrVO", new MbrVO());
         return "/mbr/mbrResult";
     }
    
    /**
     * 아이디 중복 체크
     * 
     * <p>입력된 회원 아이디가 이미 존재하는지 확인합니다.
     * 비동기 요청을 통해 실시간으로 아이디 중복 여부를 확인할 수 있습니다.</p>
     * 
     * @param mbrVO 중복 체크할 회원 정보 (아이디)
     * @param errors 유효성 검증 결과
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 중복 여부 (중복: 1, 미중복: 0)
     * @throws Exception 중복 체크 중 발생할 수 있는 예외
     */
    @PostMapping(value = "/chkMbrId")
    @ResponseBody
    public int chkMbrId(@Valid MbrVO mbrVO, Errors errors, Model model) throws Exception {
    	int mbrIdCnt = mbrService.chkMbrId(mbrVO);
        return mbrIdCnt;
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
     * 비밀번호 확인 폼 페이지 이동
     * 
     * <p>회원정보 수정 전 비밀번호를 확인하는 페이지로 이동합니다.</p>
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 비밀번호 확인 템플릿 페이지 경로
     */
    @GetMapping(value = "/chkMbrPswdForm")
    public String chkMbrPswdForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model) {
    	authHelper.addAuthToMapAndModel(requestMap, model);
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "비밀번호 확인");
    	
    	// pageInfo
    	model.addAttribute("pageInfo", pageInfo);
    	model.addAttribute("mbrVO", new MbrVO());
    	return "/mbr/chkMbrPswdForm";
    }

    /**
     * 비밀번호 확인 처리
     * 
     * <p>회원정보 수정 전 현재 비밀번호를 확인하는 기능입니다.
     * 비밀번호가 일치하면 회원정보 수정 페이지로 이동하고, 일치하지 않으면 오류 메시지를 표시합니다.</p>
     * 
     * @param mbrVO 회원정보
     * @param errors 유효성 검증 결과
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @param redirectAttributes 리다이렉트 시 전달할 데이터
     * @return 회원정보 수정 페이지 또는 비밀번호 확인 페이지
     * @throws Exception 비밀번호 확인 중 발생할 수 있는 예외
     */
    @PostMapping(value = "/chkMbrPswd")
    public String chkMbrPswd(@Valid MbrVO mbrVO, Errors errors, Model model, RedirectAttributes redirectAttributes, HttpSession session) throws Exception {
        // 현재 로그인한 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        
        // 입력받은 ID와 현재 로그인한 사용자의 ID 비교 (보안 강화)
        if (!currentUsername.equals(mbrVO.getMbrId())) {
            model.addAttribute("errorMessage", "잘못된 접근입니다.");
            return "/mbr/chkMbrPswdForm";
        }
        
        // DB에서 사용자 정보 조회
        MbrVO dbMbrVO = loginService.findByUsername(mbrVO.getMbrId());
        
        if (dbMbrVO != null) {
            // 입력받은 비밀번호와 DB에 저장된 암호화된 비밀번호 비교
            boolean pwMatch = passwordEncoder.matches(mbrVO.getMbrPswd(), dbMbrVO.getMbrPswd());
            
            if (pwMatch) {
            	// 비밀번호 일치 - 세션에 인증 완료 표시 저장
            	session.setAttribute("passwordConfirmed", true);
                // 비밀번호 일치 - 회원정보 수정 페이지로 리다이렉트
                return "redirect:/mbr/mbrUpdateForm";
            }
        }
        
        // 비밀번호 불일치 - 에러 메시지와 함께 비밀번호 확인 페이지로 이동
        redirectAttributes.addFlashAttribute("errorMessage", "비밀번호가 일치하지 않습니다.");
        return "redirect:/mbr/chkMbrPswdForm";
    }
    
    /**
     * 회원정보 수정 페이지 이동 (사용자용)
     * 
     * <p>회원정보 수정 페이지로 이동합니다. 현재 로그인한 사용자의 정보를 모델에 추가합니다.</p>
     * 
     * @param model 뷰에 전달할 데이터를 담는 모델 객체
     * @return 회원정보 수정 템플릿 페이지 경로
     * @throws Exception 사용자 정보 조회 중 발생할 수 있는 예외
     */
    @GetMapping("/mbrUpdateForm")
    public String mbrUpdateForm(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpSession session, RedirectAttributes redirectAttributes) throws Exception {
    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "회원 정보 수정");
    	authHelper.addAuthToMapAndModel(requestMap, model);
        // 세션에서 비밀번호 확인 여부 확인
        Boolean passwordConfirmed = (Boolean) session.getAttribute("passwordConfirmed");
        
        if (passwordConfirmed == null || !passwordConfirmed) {
            // 비밀번호 확인을 거치지 않은 경우 비밀번호 확인 페이지로 리다이렉트
            redirectAttributes.addFlashAttribute("errorMessage", "비밀번호 확인이 필요합니다.");
            return "redirect:/mbr/chkMbrPswdForm";
        }
    	
    	// 현재 로그인한 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        
        // DB에서 사용자 정보 조회
        MbrVO mbrVO = loginService.findByUsername(currentUsername);
        
        model.addAttribute("mbrVO", mbrVO);
    	model.addAttribute("pageInfo", pageInfo);
        return "/mbr/mbrUpdateForm";
    }
    
    /**
     * 회원정보 수정 처리 (사용자용)
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
    @PostMapping("/mbrSelfUpdate")
    public String mbrSelfUpdate(@Valid ModifyMbrVO mbrVO, Errors errors, HttpSession session, 
                          @RequestParam(required = false) String currentPswd,
                          @RequestParam(required = false) String newPswd,
                          @RequestParam(required = false) String confirmPswd,
                          RedirectAttributes redirectAttributes, ModelMap model) throws Exception {

    	EgovMap pageInfo = new EgovMap();
    	pageInfo.put("pageTitle", "회원정보 수정 처리 (사용자용)");
    	
    	// pageInfo
		model.addAttribute("pageInfo", pageInfo);
    	
        // 1. 검증 실패 처리
        if (errors.hasErrors()) {
        	String errorMessage = errors.getAllErrors().get(0).getDefaultMessage();
            model.addAttribute("mbrVO", mbrVO);
            model.addAttribute("errorMessage", errorMessage);
            return "/mbr/mbrUpdateForm"; // 검증 실패 시 다시 폼으로
        }
    	
        // 작업 완료 후 세션에서 비밀번호 확인 플래그 제거
        session.removeAttribute("passwordConfirmed");
        // 현재 로그인한 사용자 정보 가져오기
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = auth.getName();
        
        // 입력받은 ID와 현재 로그인한 사용자의 ID 비교 (보안 강화)
        if (!currentUsername.equals(mbrVO.getMbrId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 접근입니다.");
            return "redirect:/";
        }
        
        // DB에서 사용자 정보 조회
        MbrVO dbMbrVO = loginService.findByUsername(mbrVO.getMbrId());
        
        // 비밀번호 변경 처리
        if (newPswd != null && !newPswd.isEmpty()) {
            // 현재 비밀번호 확인
            if (currentPswd == null || currentPswd.isEmpty()) {
                model.addAttribute("errorMessage", "현재 비밀번호를 입력해주세요.");
                model.addAttribute("mbrVO", mbrVO);
                return "/mbr/mbrUpdateForm";
            }
            
            // 현재 비밀번호 일치 여부 확인
            boolean pwMatch = passwordEncoder.matches(currentPswd, dbMbrVO.getMbrPswd());
            if (!pwMatch) {
                model.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
                model.addAttribute("mbrVO", mbrVO);
                return "/mbr/mbrUpdateForm";
            }
            
            // 새 비밀번호와 확인 비밀번호 일치 여부 확인
            if (!newPswd.equals(confirmPswd)) {
                model.addAttribute("errorMessage", "새 비밀번호와 확인 비밀번호가 일치하지 않습니다.");
                model.addAttribute("mbrVO", mbrVO);
                return "/mbr/mbrUpdateForm";
            }
            
            // 새 비밀번호 암호화
            String encodedPassword = passwordEncoder.encode(newPswd);
            mbrVO.setMbrPswd(encodedPassword);
        } else {
            // 비밀번호 변경을 원하지 않는 경우, 기존 비밀번호 유지
            mbrVO.setMbrPswd(dbMbrVO.getMbrPswd());
        }
        
    	// HashMap에 권한 정보 추가
        authHelper.addAuthToMapAndModel(mbrVO, model);
        
        // 회원정보 수정
        int result = mbrService.updateMbrSelf(mbrVO);
        
        if (result > 0) {
            redirectAttributes.addFlashAttribute("message", "회원정보가 성공적으로 수정되었습니다.");
            return "redirect:/";
        } else {
            model.addAttribute("errorMessage", "회원정보 수정에 실패했습니다.");
            model.addAttribute("mbrVO", mbrVO);
            return "/mbr/mbrUpdateForm";
        }
    }
}