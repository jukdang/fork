package egovframework.theimc.util;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Component;

/**
 * 문자열 유틸리티 클래스
 */
@Component
public class StringUtils {

    /**
     * Retrieves a string value from the map for the given key. 
     * If the key does not exist or the value is null, it returns null.
     *
     * @param map the map containing the key-value pairs
     * @param key the key whose associated value is to be returned
     * @return the string value associated with the key, or null if the key does not exist or value is null
     */
	//request.get('item')이 null이면 null를 반환. null이 아니면 String을 반환
    public static String getRequestString(Map<String, Object> map, String key) {
        return MapUtils.getString(map, key, null);
    }
    
    /**
     * Object 타입의 값을 안전하게 암호화하는 메서드
     * 다양한 데이터 타입을 처리하고 암호화함
     *
     * @param value 암호화할 값 (Integer, String 등 다양한 타입 가능)
     * @return 암호화된 문자열
     */
    public static String encryptValue(Object value) {
        if (value == null) {
            return "";
        }
        
        // 어떤 타입이든 String으로 변환
        String strValue = String.valueOf(value);
        
        // 암호화 수행
        return CryptoUtils.encrypt(strValue);
    }
    
    /**
     * EgovMap에서 특정 키의 값을 가져와 암호화하고 다시 설정하는 메서드
     *
     * @param map 처리할 EgovMap
     * @param key 암호화할 값의 키
     * @return 암호화가 성공했는지 여부
     */
    public static boolean encryptMapValue(EgovMap map, String key) {
        if (map == null || key == null || !map.containsKey(key)) {
            return false;
        }
        
        // 값 가져오기
        Object value = map.get(key);
        
        // 암호화
        String encrypted = encryptValue(value);
        
        // 다시 맵에 저장
        map.put(key, encrypted);
        
        return true;
    }
    
    /**
     * List<EgovMap>에서 모든 항목의 특정 키 값을 암호화하는 메서드
     *
     * @param mapList 처리할 EgovMap 리스트
     * @param key 암호화할 값의 키
     * @return 성공적으로 암호화된 항목 수
     */
    public static int encryptMapListValues(List<EgovMap> mapList, String key) {
        if (mapList == null || key == null) {
            return 0;
        }
        int count = 0;
        for (EgovMap map : mapList) {
            if (encryptMapValue(map, key)) {
                count++;
            }
        }
        
        return count;
    }
}