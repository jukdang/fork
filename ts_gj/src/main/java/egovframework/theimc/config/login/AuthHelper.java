// 1. AuthHelper 클래스 생성 (util 패키지에)
package egovframework.theimc.config.login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import egovframework.theimc.login.vo.CustomUserDetails;
import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;

import java.util.HashMap;

@Component  // 이 어노테이션이 중요! Spring Bean으로 등록
public class AuthHelper {
    
	@Value("${svc.ownrMaskedYn}")
    private String ownrMaskedYn;
	
	@Value("${svc.vhclMaskedYn}")
    private String vhclMaskedYn;
	
	@Value("${svc.oprtrMaskedYn}")
    private String oprtrMaskedYn;
	
    /**
     * HashMap에 현재 로그인 사용자의 권한 정보를 추가
     * 
     * @param requestMap 권한 정보를 추가할 Map
     */
    public void addAuthToMap(HashMap<String, Object> requestMap) {
        CustomUserDetails currentUser = getCurrentUser();
        if (currentUser != null) {
        	requestMap.put("currentUserSn", currentUser.getMbrSn());
            requestMap.put("currentUserId", currentUser.getMbrId());
            requestMap.put("currentUserRole", currentUser.getMbrRole());
            requestMap.put("currentOwnrNm", currentUser.getOwnrNm());
            requestMap.put("authOwnrList", currentUser.getAuthOwnrList());
            
            // ADMIN이 아닌 경우 본인 데이터만 조회하도록 필터 설정
            if (!"ROLE_ADMIN".equals(currentUser.getMbrRole())) {
                requestMap.put("dataFilter", "ownr");
            } else {
                requestMap.put("dataFilter", "all");
            }
        }
    }
    
    /**
     * HashMap과 Model에 현재 로그인 사용자의 권한 정보를 모두 추가
     * 
     * @param requestMap 권한 정보를 추가할 Map
     * @param model 권한 정보를 추가할 Model
     */
    public void addAuthToMapAndModel(HashMap<String, Object> requestMap, ModelMap model) {
        CustomUserDetails currentUser = getCurrentUser();
        
        if (currentUser != null) {
            // requestMap에 추가
            requestMap.put("currentUserId", currentUser.getMbrId());
            requestMap.put("currentUserRole", currentUser.getMbrRole());
            requestMap.put("currentOwnrNm", currentUser.getOwnrNm());
            requestMap.put("authOwnrList", currentUser.getAuthOwnrList());
            requestMap.put("prvcIpAddr", currentUser.getPrvcIpAddr());
            
            // model에 추가 (화면에서 사용)
            model.addAttribute("isAdmin", "ROLE_ADMIN".equals(currentUser.getMbrRole()));
            model.addAttribute("currentMbrRole", currentUser.getMbrRole());
            model.addAttribute("currentOwnrNm", currentUser.getOwnrNm());
            model.addAttribute("currentMbrId", currentUser.getMbrId());
            model.addAttribute("authOwnrList", currentUser.getAuthOwnrList());
            model.addAttribute("prvcIpAddr", currentUser.getPrvcIpAddr());
            // ADMIN이 아닌 경우 본인 데이터만 조회하도록 필터 설정
            if (!"ROLE_ADMIN".equals(currentUser.getMbrRole())) {
                requestMap.put("dataFilter", "ownr");
                model.addAttribute("dataFilter", "ownr");
            } else {
                requestMap.put("dataFilter", "all");
                model.addAttribute("dataFilter", "all");
            }
            
        }
        model.addAttribute("ownrMaskedYn", ownrMaskedYn);
        model.addAttribute("vhclMaskedYn", vhclMaskedYn);
        model.addAttribute("oprtrMaskedYn", oprtrMaskedYn);
    }
    
    
    /**
     * HashMap과 Model에 현재 로그인 사용자의 권한 정보를 모두 추가
     * 
     * @param requestMap 권한 정보를 추가할 Map
     * @param model 권한 정보를 추가할 Model
     */
    public void addAuthToMapAndModel(MbrVO mbrVO, ModelMap model) {
        CustomUserDetails currentUser = getCurrentUser();
        
        if (currentUser != null) {
            // requestMap에 추가
        	mbrVO.setCurrentUserId(currentUser.getMbrId());
        	mbrVO.setCurrentUserRole(currentUser.getMbrRole());
        	mbrVO.setCurrentOwnrNm(currentUser.getOwnrNm());
        	mbrVO.setAuthOwnrList(currentUser.getAuthOwnrList());
        	
        	// model에 추가 (화면에서 사용)
        	model.addAttribute("isAdmin", "ROLE_ADMIN".equals(currentUser.getMbrRole()));
        	model.addAttribute("currentMbrRole", currentUser.getMbrRole());
        	model.addAttribute("currentOwnrNm", currentUser.getOwnrNm());
        	model.addAttribute("currentMbrId", currentUser.getMbrId());
        	model.addAttribute("authOwnrList", currentUser.getAuthOwnrList());
            
        	// ADMIN이 아닌 경우 본인 데이터만 조회하도록 필터 설정
            if (!"ROLE_ADMIN".equals(currentUser.getMbrRole())) {
            	mbrVO.setDataFilter("ownr");
            	model.addAttribute("dataFilter", "ownr");
            } else {
            	mbrVO.setDataFilter("all");
            	model.addAttribute("dataFilter", "all");
            }
            
        }
        model.addAttribute("ownrMaskedYn", ownrMaskedYn);
        model.addAttribute("vhclMaskedYn", vhclMaskedYn);
        model.addAttribute("oprtrMaskedYn", oprtrMaskedYn);
    }
    
    
    /**
     * HashMap과 Model에 현재 로그인 사용자의 권한 정보를 모두 추가
     * 
     * @param requestMap 권한 정보를 추가할 Map
     * @param model 권한 정보를 추가할 Model
     */
    public void addAuthToMapAndModel(ModifyMbrVO mbrVO, ModelMap model) {
        CustomUserDetails currentUser = getCurrentUser();
        
        if (currentUser != null) {
            // requestMap에 추가
        	mbrVO.setCurrentUserId(currentUser.getMbrId());
        	mbrVO.setCurrentOwnrNm(currentUser.getOwnrNm());
        	
        	// model에 추가 (화면에서 사용)
        	model.addAttribute("isAdmin", "ROLE_ADMIN".equals(currentUser.getMbrRole()));
        }
        
    	// ADMIN이 아닌 경우 본인 데이터만 조회하도록 필터 설정
        if (!"ROLE_ADMIN".equals(currentUser.getMbrRole())) {
        	mbrVO.setDataFilter("ownr");
        	model.addAttribute("dataFilter", "ownr");
        } else {
        	mbrVO.setDataFilter("all");
        	model.addAttribute("dataFilter", "all");
        }
        
        model.addAttribute("ownrMaskedYn", ownrMaskedYn);
        model.addAttribute("vhclMaskedYn", vhclMaskedYn);
        model.addAttribute("oprtrMaskedYn", oprtrMaskedYn);
    }
    
    /**
     * MbrVO에 현재 로그인 사용자의 권한 정보를 추가
     * 
     * @param mbrVO 권한 정보를 추가할 MbrVO
     */
    public void addAuthToVO(MbrVO mbrVO) {
        CustomUserDetails currentUser = getCurrentUser();
        
        if (currentUser != null) {
            // MbrVO에 권한 정보 설정
            mbrVO.setCurrentUserId(currentUser.getMbrId());
            mbrVO.setCurrentUserRole(currentUser.getMbrRole());
            mbrVO.setCurrentOwnrNm(currentUser.getOwnrNm());
            mbrVO.setAuthOwnrList(currentUser.getAuthOwnrList());
            
            // ADMIN이 아닌 경우 본인 데이터만 처리하도록 필터 설정
            if (!"ROLE_ADMIN".equals(currentUser.getMbrRole())) {
                mbrVO.setDataFilter("ownr");
            } else {
                mbrVO.setDataFilter("all");
            }
        }
    }
    
    /**
     * 현재 사용자가 ADMIN인지 체크
     * 
     * @return ADMIN이면 true, 아니면 false
     */
    public boolean isAdmin() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser != null && "ROLE_ADMIN".equals(currentUser.getMbrRole());
    }
    
    /**
     * 현재 로그인한 사용자의 CustomUserDetails를 반환 (DB 조회 없음)
     * 
     * @return 현재 사용자 CustomUserDetails
     */
    public CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        // Principal이 CustomUserDetails인 경우
        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return (CustomUserDetails) authentication.getPrincipal();
        }
        
        return null;
    }
    
    /**
     * 현재 로그인한 사용자 ID 반환
     * 
     * @return 사용자 ID
     */
    public String getCurrentUserId() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getMbrId() : null;
    }
    
    /**
     * 현재 로그인한 사용자 권한 반환
     * 
     * @return 사용자 권한
     */
    public String getCurrentUserRole() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getMbrRole() : null;
    }
    
    /**
     * 현재 로그인한 사용자의 소속명 반환
     * 
     * @return 소속명
     */
    public String getCurrentOwnrNm() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getOwnrNm() : null;
    }
    
    /**
     * 현재 로그인한 사용자명 반환
     * 
     * @return 사용자명
     */
    public String getCurrentUserName() {
        CustomUserDetails currentUser = getCurrentUser();
        return currentUser != null ? currentUser.getMbrNm() : null;
    }
    
    /**
     * Spring Security에서 현재 Authentication 객체 반환
     * 
     * @return Authentication 객체
     */
    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    
    /**
     * 로그인 여부 체크
     * 
     * @return 로그인되어 있으면 true, 아니면 false
     */
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() 
               && !"anonymousUser".equals(authentication.getPrincipal());
    }
}