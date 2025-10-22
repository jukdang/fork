package egovframework.theimc.login.service.impl;

import java.util.HashMap;
import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.login.service.LoginService;
import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.util.CryptoUtils;

@Service("loginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;
    
    // 유저 정보
    @Override
    public MbrVO findByUsername(String username) throws Exception {
    	MbrVO mbr = loginMapper.findByUsername(username);

        if (mbr != null) {
            mbr.setMbrNm(CryptoUtils.decrypt(mbr.getMbrNm()));
            mbr.setMbrEmail(CryptoUtils.decrypt(mbr.getMbrEmail()));
            mbr.setMbrTelNo(CryptoUtils.decrypt(mbr.getMbrTelNo()));
        }

        return mbr;
    }
    
    // 로그인 실패 건수
    @Override
    public int getLoginFailCount(String param) throws Exception {
        return loginMapper.getLoginFailCount(param);
    }
    
    // 로그인 실패 건수 초기화
    @Override
    public void updateLoginFailCount(HashMap<String, Object> paramMap) throws Exception {
        loginMapper.updateLoginFailCount(paramMap);
    }
    
    // 로그인 통계 생성
    @Override
    public int insertLoginStat(HashMap<String, Object> requestMap) throws Exception {
        return loginMapper.insertLoginStat(requestMap);
    }
    
    // 회원 비밀번호 조회
    @Override
    public String getMbrPswd(String mbrId) throws Exception {
        return loginMapper.getMbrPswd(mbrId);
    }
    
    // 회원 비밀번호 업데이트
    @Override
    public void updateMbrPswd(HashMap<String, Object> paramMap) throws Exception {
        loginMapper.updateMbrPswd(paramMap);
    }
}