package egovframework.theimc.login.vo;

import javax.validation.constraints.AssertTrue;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModifyMbrVO {

    private String sn;
    private String encryptSn;
    
    private String mbrId;
    
    private String mbrNm;
    private String mbrPswd;
    
//    @NotBlank(message = "연락처는 필수 입력값입니다")
//    @Pattern(regexp = "^\\d{1,11}$", message = "연락처는 숫자만 입력하며 최대 11자리까지 가능합니다")
    private String mbrTelNo;
    
//    @NotBlank(message = "이메일은 필수 입력값입니다")
//    @Email(message = "올바른 이메일 형식이 아닙니다")
    private String mbrEmail;
    
    private String mbrRole;

    private String ownrNm;
    
    private String currentUserId;         // 현재 로그인한 사용자 ID
    private String currentOwnrNm;
    private String currentUserRole;       // 현재 로그인한 사용자 권한
    private String dataFilter;            // 데이터 필터 (self/all)

    @AssertTrue(message = "이름은 2자 이상 50자 이하로 입력해주세요")
    public boolean isMbrNmValid() {
        if (mbrNm == null || mbrNm.isBlank()) {
            return true; // 이름 수정 안 함 → 통과
        }
        return mbrNm.length() >= 2 && mbrNm.length() <= 50;
    }

    
    @AssertTrue(message = "비밀번호는 8이상이어야 하며 영문자, 숫자, 특수문자를 모두 포함해야 합니다")
    public boolean isMbrPswdValid() {
        if (mbrPswd == null || mbrPswd.isBlank()) {
            return true;
        }

        if (mbrPswd.length() < 8 || mbrPswd.length() > 100) {
            return false;
        }

        return mbrPswd.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$");
    }
    
}