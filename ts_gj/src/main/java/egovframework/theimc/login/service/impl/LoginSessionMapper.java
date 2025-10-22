package egovframework.theimc.login.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;


@Mapper
public interface LoginSessionMapper {
    
    int insertMbrSession(HashMap<String, Object> paramMap);
    
    int insertConcurrentUsersCnt(HashMap<String, Object> paramMap);
    
    int updateSessionEnd(HashMap<String, Object> paramMap);
    
    int updateLastActivityTime(String sessionId);
    
    EgovMap getActiveSessionByMbrId(String mbrId);
    
    EgovMap getSessionById(String sessionId);
    
    int updateSessionEndBySn(HashMap<String, Object> paramMap);
    
    List<EgovMap> findInactiveSessions(int minutes);
    
    List<EgovMap> findSessionsWithoutRememberMe(int minutes);
    int updateSessionEndWithType(HashMap<String, Object> paramMap);
    
    EgovMap getSessionBySn(int sn);
    
    int updateSessionEndWithoutRememberMeCheck(HashMap<String, Object> paramMap);
}