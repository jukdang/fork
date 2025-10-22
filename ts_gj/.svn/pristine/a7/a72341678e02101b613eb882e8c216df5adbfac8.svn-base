package egovframework.theimc.login.service.impl;

import java.util.HashMap;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.theimc.login.vo.MbrVO;

@Mapper
public interface LoginMapper {
    // 유저 정보
    MbrVO findByUsername(String username) throws Exception;
    
    // 로그인 실패 건수
    int getLoginFailCount(String param) throws Exception;
    
    // 로그인 실패 건수 초기화
    void updateLoginFailCount(HashMap<String, Object> paramMap) throws Exception;
    
    // 로그인 통계 생성
    int insertLoginStat(HashMap<String, Object> requestMap) throws Exception;
    
    // 회원 비밀번호 조회
    String getMbrPswd(String mbrId) throws Exception;
    
    // 회원 비밀번호 업데이트
    void updateMbrPswd(HashMap<String, Object> paramMap) throws Exception;
}