package egovframework.theimc.login.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;


public interface LoginSessionService {
    
    /**
     * 세션 정보 저장
     * @param paramMap 세션 정보가 담긴 Map
     * @return 저장된 세션의 일련번호
     */
    int insertMbrSession(HashMap<String, Object> paramMap);
    
    /**
     * 세션 종료 처리
     * @param sessionId 세션 ID
     * @return 처리 결과 (성공: 1, 실패: 0)
     */
    int updateSessionEnd(HashMap<String, Object> paramMap);
    
    /**
     * 세션 마지막 활동 시간 업데이트
     * @param sessionId 세션 ID
     * @return 처리 결과 (성공: 1, 실패: 0)
     */
    int updateLastActivityTime(String sessionId);
    
    /**
     * 회원 ID로 활성 세션 조회
     * @param mbrId 회원 ID
     * @return 활성 세션 정보
     */
    EgovMap getActiveSessionByMbrId(String mbrId);
    
    /**
     * 세션 ID로 세션 정보 조회
     * @param sessionId 세션 ID
     * @return 세션 정보
     */
    EgovMap getSessionById(String sessionId);
    
    /**
     * 세션 일련번호로 세션 종료 처리
     * @param sn 세션 일련번호
     * @return 처리 결과 (성공: 1, 실패: 0)
     */
    int updateSessionEndBySn(HashMap<String, Object> paramMap);

    /**
     * 지정된 시간(분) 이상 활동이 없는 활성 세션 찾기
     * @param minutes 비활성 시간(분)
     * @return 비활성 세션 목록
     */
    List<EgovMap> findInactiveSessions(int minutes);
    
    /**
     * 지정된 시간(분) 이상 활동이 없는 활성 세션 찾기 - remember-me
     * @param minutes 비활성 시간(분)
     * @return 비활성 세션 목록
     */
    List<EgovMap> findSessionsWithoutRememberMe(int minutes);
    
    /**
     * Remember-me 토큰 체크 없이 세션 종료 처리 (명시적 로그아웃 시 사용)
     */
    int updateSessionEndWithoutRememberMeCheck(HashMap<String, Object> paramMap);

    /**
     * 세션 종료 유형과 함께 세션 종료 처리
     * @param sn 세션 일련번호
     * @param endType 종료 유형 (O: 로그아웃, T: 타임아웃, B: 브라우저 종료)
     * @return 처리 결과
     */
    int updateSessionEndWithType(HashMap<String, Object> paramMap);

}