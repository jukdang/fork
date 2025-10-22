package egovframework.theimc.login.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.login.service.LoginSessionService;

@Service("loginSessionService")
public class LoginSessionServiceImpl implements LoginSessionService {

	@Autowired
    private LoginSessionMapper loginSessionMapper;
    
    @Override
    public int insertMbrSession(HashMap<String, Object> paramMap) {
    		loginSessionMapper.insertConcurrentUsersCnt(paramMap);
        return loginSessionMapper.insertMbrSession(paramMap);
    }
    
    @Override
    public int updateSessionEnd(HashMap<String, Object> paramMap) {
        return loginSessionMapper.updateSessionEnd(paramMap);
    }
    
    @Override
    public int updateLastActivityTime(String sessionId) {
        return loginSessionMapper.updateLastActivityTime(sessionId);
    }
    
    @Override
    public EgovMap getActiveSessionByMbrId(String mbrId) {
        return loginSessionMapper.getActiveSessionByMbrId(mbrId);
    }
    
    @Override
    public EgovMap getSessionById(String sessionId) {
        return loginSessionMapper.getSessionById(sessionId);
    }
    
    @Override
    public int updateSessionEndBySn(HashMap<String, Object> paramMap) {
        return loginSessionMapper.updateSessionEndBySn(paramMap);
    }
    
    @Override
    public List<EgovMap> findInactiveSessions(int minutes) {
        return loginSessionMapper.findInactiveSessions(minutes);
    }

    @Override
    public List<EgovMap> findSessionsWithoutRememberMe(int minutes) {
        return loginSessionMapper.findSessionsWithoutRememberMe(minutes);
    }
    
    @Override
    public int updateSessionEndWithType(HashMap<String, Object> paramMap) {
        return loginSessionMapper.updateSessionEndWithType(paramMap);
    }

    @Override
    public int updateSessionEndWithoutRememberMeCheck(HashMap<String, Object> paramMap) {
        return loginSessionMapper.updateSessionEndWithoutRememberMeCheck(paramMap);
    }
}