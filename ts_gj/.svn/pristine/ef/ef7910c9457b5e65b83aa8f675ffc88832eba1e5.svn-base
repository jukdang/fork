package egovframework.theimc.login.vo;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MbrVO {

    private String sn;
    private String encryptSn;
    
    @NotBlank(message = "아이디는 필수 입력값입니다")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요")
    private String mbrId;
    
    @NotBlank(message = "비밀번호는 필수 입력값입니다")
    @Size(min = 8, max = 100, message = "비밀번호는 8자 이상이어야 합니다")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$", 
             message = "비밀번호는 영문자, 숫자, 특수문자를 포함해야 합니다")
    private String mbrPswd;
    
    @NotBlank(message = "이름은 필수 입력값입니다")
    @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요")
    private String mbrNm;
    
    //@NotBlank(message = "연락처는 필수 입력값입니다")
    @Pattern(regexp = "^\\d{1,11}$", message = "연락처는 숫자만 입력하며 최대 11자리까지 가능합니다")
    private String mbrTelNo;
    
    //@NotBlank(message = "이메일은 필수 입력값입니다")
    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String mbrEmail;
    
    private String mbrRole;

    private String ownrNm;
    private String prvcIpAddr;
    
    private String utztnTrmsAgreYn;
    private String prvcClctAgreYn;
    private String lastActDt;
    
    private String[] selectedSns;
    
    private String currentUserId;         // 현재 로그인한 사용자 ID
    private String currentOwnrNm;
    private String currentUserRole;       // 현재 로그인한 사용자 권한
    private String dataFilter;            // 데이터 필터 (self/all)
    private String authOwnrInfo;
    private List<String> authOwnrList;
    
    // Spring Security에서 필요한 메서드
    public String getMbrId() {
        return this.mbrId;
    }

    public String getOwnrNm() {
        return this.ownrNm;
    }
    
    public String getMbrPswd() {
        return this.mbrPswd;
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(this.mbrRole));
    }
}