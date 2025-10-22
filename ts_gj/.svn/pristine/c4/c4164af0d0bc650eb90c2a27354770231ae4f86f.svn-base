package egovframework.theimc.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MaskingUtils {
    
	@Value("${svc.ownrMaskedYn}")
    private String ownrMaskedYn;
	
	@Value("${svc.vhclMaskedYn}")
    private String vhclMaskedYn;
	
	@Value("${svc.oprtrMaskedYn}")
    private String oprtrMaskedYn;
    
    // 방법 1: 설정값 자동 적용
    public String maskVehicleRegNo(String regNo) {
        boolean isMasked = "true".equals(vhclMaskedYn);
        
        if (!isMasked || regNo == null || regNo.length() <= 5) {
            return regNo != null ? regNo : "";
        }
        
        String visible = regNo.substring(0, 5);
        String masked = "*".repeat(regNo.length() - 5);
        return visible + masked;
    }
    
    // 운전자 이름 마스킹 (첫 글자만 보이고 나머지는 *)
    public String maskDriverName(String name) {
        boolean isMasked = "true".equals(oprtrMaskedYn);
        
        if (!isMasked || name == null || name.length() <= 1) {
            return name != null ? name : "";
        }
        
        String visible = name.substring(0, 1);
        String masked = "*".repeat(name.length() - 1);
        return visible + masked;
    }
    
    // 운수회사 이름 마스킹 (앞 두글자 **)
    public String maskOwnerName(String name) {
        boolean isMasked = "true".equals(ownrMaskedYn);

        if (!isMasked || name == null || name.length() <= 2) {
            return name != null ? name : "";
        }

        // 앞 두 글자를 *로, 나머지는 그대로
        String masked = "*".repeat(2);
        String visible = name.substring(2);
        return masked + visible;
    }
}