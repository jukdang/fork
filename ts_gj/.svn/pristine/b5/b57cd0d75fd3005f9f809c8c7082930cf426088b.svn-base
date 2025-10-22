package egovframework.theimc.util;

public class UserAgentUtils {
    /**
     * User-Agent 정보를 기반으로 OS 정보 감지
     */
	public static String detectOsInfo(String userAgent) {
        if (userAgent == null) {
            return "UNKNOWN";
        }
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("windows")) {
            return "Windows";
        } else if (userAgent.contains("mac os x")) {
            return "Mac OS X";
        } else if (userAgent.contains("linux")) {
            return userAgent.contains("android") ? "Android" : "Linux";
        } else if (userAgent.contains("iphone") || userAgent.contains("ipad") || userAgent.contains("ipod")) {
            return "iOS";
        } else {
            return "Other";
        }
    }
    
    /**
     * User-Agent 정보를 기반으로 브라우저 정보 감지
     */
    public static String detectBrwsrInfo(String userAgent) {
        if (userAgent == null) {
            return "UNKNOWN";
        }

        userAgent = userAgent.toLowerCase();

        if (userAgent.contains("edg/")) {
            return "Edge";
        } else if (userAgent.contains("opr") || userAgent.contains("opera")) {
            return "Opera";
        } else if (userAgent.contains("chrome") && !userAgent.contains("edg/") && !userAgent.contains("opr")) {
            return "Chrome";
        } else if (userAgent.contains("safari") && !userAgent.contains("chrome")) {
            return "Safari";
        } else if (userAgent.contains("firefox")) {
            return "Firefox";
        } else if (userAgent.contains("msie") || userAgent.contains("trident")) {
            return "Internet Explorer";
        } else {
            return "Other";
        }
    }
    
    /**
     * User-Agent 정보를 기반으로 디바이스 타입 감지
     */
    public static String detectDeviceType(String userAgent) {
        if (userAgent == null) {
            return "UNKNOWN";
        }
        
        userAgent = userAgent.toLowerCase();
        
        if (userAgent.contains("mobile") || userAgent.contains("android") || 
            userAgent.contains("iphone") || userAgent.contains("ipod")) {
            return "MOBILE";
        } else if (userAgent.contains("ipad") || userAgent.contains("tablet")) {
            return "TABLET";
        } else {
            return "PC";
        }
    }
}
