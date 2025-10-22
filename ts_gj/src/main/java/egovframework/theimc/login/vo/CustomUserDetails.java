package egovframework.theimc.login.vo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
/**
 * Spring Security용 커스텀 UserDetails 구현체
 * 사용자 정보를 메모리에 저장하여 매번 DB 조회를 방지
 */
public class CustomUserDetails implements UserDetails {
    
    private String mbrId;        // 사용자 ID
    private String mbrPswd;      // 비밀번호  
    private String mbrNm;        // 사용자명
    private String mbrRole;      // 권한
    private String mbrSn;      // 순번
	private String ownrNm;       // 소속명
	private String prvcIpAddr;       // 개인정보접근아이피

	private List<String> authOwnrList; // 권한 소속 목록
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;
    
    // 기본 생성자
    public CustomUserDetails() {}
    
    // MbrVO를 받는 생성자
    public CustomUserDetails(MbrVO mbrVO) {
        this.mbrId = mbrVO.getMbrId();
        this.mbrPswd = mbrVO.getMbrPswd();
        this.mbrNm = mbrVO.getMbrNm();
        this.mbrRole = mbrVO.getMbrRole();
        this.mbrSn = mbrVO.getSn();
        this.prvcIpAddr = mbrVO.getPrvcIpAddr();
        this.ownrNm = mbrVO.getOwnrNm();
        this.authOwnrList = mbrVO.getAuthOwnrList();
        this.authorities = mbrVO.getAuthorities();
    }
    
    // UserDetails 인터페이스 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return mbrPswd;
    }
    
    @Override
    public String getUsername() {
        return mbrId;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    // 추가 Getter/Setter 메서드들
    
    public String getMbrId() {
        return mbrId;
    }
    
    public void setMbrId(String mbrId) {
        this.mbrId = mbrId;
    }
    
    public String getMbrPswd() {
        return mbrPswd;
    }
    
    public void setMbrPswd(String mbrPswd) {
        this.mbrPswd = mbrPswd;
    }
    
    public String getMbrNm() {
        return mbrNm;
    }
    
    public void setMbrNm(String mbrNm) {
        this.mbrNm = mbrNm;
    }
    
    public String getMbrRole() {
        return mbrRole;
    }
    
    public void setMbrRole(String mbrRole) {
        this.mbrRole = mbrRole;
    }
    
    public String getOwnrNm() {
        return ownrNm;
    }
    
    public void setOwnrNm(String ownrNm) {
        this.ownrNm = ownrNm;
    }
    
    // Getter (방어적 복사본 반환)
    public List<String> getAuthOwnrList() {
        // 원본 대신 복제본 반환
        return Collections.unmodifiableList(new ArrayList<>(authOwnrList));
    }

    // Setter (방어적 복사본 저장)
    public void setAuthOwnrList(List<String> authOwnrList) {
        if (authOwnrList == null) {
            this.authOwnrList = new ArrayList<>();
        } else {
            this.authOwnrList = new ArrayList<>(authOwnrList); // 외부 컬렉션을 내부 복제본으로 저장
        }
    }
    
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
    
    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }
    
    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }
    
    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getMbrSn() {
		return mbrSn;
	}

	public void setMbrSn(String mbrSn) {
		this.mbrSn = mbrSn;
	}

    public String getPrvcIpAddr() {
		return prvcIpAddr;
	}

	public void setPrvcIpAddr(String prvcIpAddr) {
		this.prvcIpAddr = prvcIpAddr;
	}
	
    @Override
    public String toString() {
        // 복제본 사용 (점검도구가 인식하도록 별도 변수 활용)
        List<String> safeAuthOwnrList = new ArrayList<>(authOwnrList);

        return "CustomUserDetails{" +
                "mbrId='" + mbrId + '\'' +
                ", mbrNm='" + mbrNm + '\'' +
                ", mbrRole='" + mbrRole + '\'' +
                ", ownrNm='" + ownrNm + '\'' +
                ", prvcIpAddr='" + prvcIpAddr + '\'' +
                ", authOwnrList=" + safeAuthOwnrList +
                ", enabled=" + enabled +
                '}';
    }
}