package egovframework.theimc.mbr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;
import egovframework.theimc.mbr.service.MbrService;
import egovframework.theimc.util.CryptoUtils;

/**
 * 회원 관리 서비스 구현 클래스
 * 
 * <p>회원 관리 서비스 인터페이스(MbrService)의 구현체로, 
 * 회원 등록, 수정, 중복 체크, 목록 조회 등의 비즈니스 로직을 처리합니다.</p>
 *
 * @author  시스템 관리자
 * @version 1.0
 * @since   2025.05.01
 * @see     egovframework.theimc.mbr.service.MbrService
 */
@Service("MbrService")
public class MbrServiceImpl extends EgovAbstractServiceImpl implements MbrService {
    
    /**
     * 회원 매퍼
     */
    @Autowired
    private MbrMapper mbrMapper;
    
    /**
     * 비밀번호 암호화 인코더
     */
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    /**
     * 회원 등록 (관리자용) 구현
     * 
     * <p>관리자가 회원 정보를 등록합니다. 
     * 비밀번호는 보안을 위해 암호화하여 저장합니다.</p>
     * 
     * @param MbrVO 등록할 회원 정보
     * @return 등록된 행 수 (성공: 1, 실패: 0)
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int insertMbr(MbrVO MbrVO) throws Exception {
        // 비밀번호 암호화
        MbrVO.setMbrPswd(passwordEncoder.encode(MbrVO.getMbrPswd()));
        MbrVO.setMbrNm(CryptoUtils.encrypt(MbrVO.getMbrNm()));
        MbrVO.setMbrEmail(CryptoUtils.encrypt(MbrVO.getMbrEmail()));
        MbrVO.setMbrTelNo(CryptoUtils.encrypt(MbrVO.getMbrTelNo()));
        
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("currentUserId", MbrVO.getCurrentUserId());
        requestMap.put("mbrId", MbrVO.getMbrId());
        requestMap.put("mbrRole", MbrVO.getMbrRole());
        
        mbrMapper.insertMbrRoleHstry(requestMap);
        return mbrMapper.insertMbr(MbrVO);
    }
    
    /**
     * 회원 등록 (사용자 가입용) 구현
     * 
     * <p>사용자가 직접 회원가입을 통해 정보를 등록합니다.
     * 비밀번호는 보안을 위해 암호화하여 저장합니다.</p>
     * 
     * @param MbrVO 등록할 회원 정보
     * @return 등록된 행 수 (성공: 1, 실패: 0)
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int insertMbrSelf(MbrVO MbrVO) throws Exception {
        // 비밀번호 암호화
        MbrVO.setMbrPswd(passwordEncoder.encode(MbrVO.getMbrPswd()));
        MbrVO.setMbrNm(CryptoUtils.encrypt(MbrVO.getMbrNm()));
        MbrVO.setMbrEmail(CryptoUtils.encrypt(MbrVO.getMbrEmail()));
        MbrVO.setMbrTelNo(CryptoUtils.encrypt(MbrVO.getMbrTelNo()));
        return mbrMapper.insertMbrSelf(MbrVO);
    }
    
    /**
     * 아이디 중복 체크 구현
     * 
     * <p>회원가입 시 입력한 아이디의 중복 여부를 확인합니다.</p>
     * 
     * @param MbrVO 중복 체크할 회원 정보 (아이디)
     * @return 중복 여부 (중복: 1, 미중복: 0)
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int chkMbrId(MbrVO MbrVO) throws Exception {
        return mbrMapper.chkMbrId(MbrVO);
    }
    
    /**
     * 회원 정보 수정 (관리자용) 구현
     * 
     * <p>관리자가 회원 정보를 수정합니다.</p>
     * 
     * @param MbrVO 수정할 회원 정보
     * @return 수정된 행 수 (성공: 1, 실패: 0)
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int updateMbr(ModifyMbrVO MbrVO) throws Exception {
    	MbrVO.setMbrNm(CryptoUtils.encrypt(MbrVO.getMbrNm()));
        MbrVO.setMbrEmail(CryptoUtils.encrypt(MbrVO.getMbrEmail()));
        MbrVO.setMbrTelNo(CryptoUtils.encrypt(MbrVO.getMbrTelNo()));
        
        HashMap<String, Object> requestMap = new HashMap<String, Object>();
        requestMap.put("currentUserId", MbrVO.getCurrentUserId());
        requestMap.put("mbrId", MbrVO.getMbrId());
        requestMap.put("mbrRole", MbrVO.getMbrRole());
        
        mbrMapper.insertMbrRoleHstry(requestMap);
        return mbrMapper.updateMbr(MbrVO);
    }
    
    /**
     * 회원 정보 수정 (사용자용) 구현
     * 
     * <p>사용자가 자신의 정보를 수정합니다.</p>
     * 
     * @param MbrVO 수정할 회원 정보
     * @return 수정된 행 수 (성공: 1, 실패: 0)
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int updateMbrSelf(ModifyMbrVO MbrVO) throws Exception {
    	MbrVO.setMbrNm(CryptoUtils.encrypt(MbrVO.getMbrNm()));
        MbrVO.setMbrEmail(CryptoUtils.encrypt(MbrVO.getMbrEmail()));
        MbrVO.setMbrTelNo(CryptoUtils.encrypt(MbrVO.getMbrTelNo()));
        return mbrMapper.updateMbrSelf(MbrVO);
    }
    
    /**
     * 회원 삭제 (배열 직접 사용) 구현
     * 
     * <p>선택된 여러 회원을 배열로 받아서 일괄 삭제합니다.
     * 트랜잭션 처리로 모든 삭제가 성공하거나 모두 실패합니다.</p>
     * 
     * @param snArray 삭제할 회원들의 일련번호 배열
     * @return 삭제된 총 행 수
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int deleteMbrArray(MbrVO MbrVO) throws Exception {
        // 선택된 sn이 없는 경우
        if (MbrVO.getSelectedSns() == null || MbrVO.getSelectedSns().length == 0) {
            return 0;
        }
        
        // 암호화된 sn들을 복호화하여 새 배열에 저장
        String[] decryptedSns = new String[MbrVO.getSelectedSns().length];
        for (int i = 0; i < MbrVO.getSelectedSns().length; i++) {
            decryptedSns[i] = CryptoUtils.decrypt(MbrVO.getSelectedSns()[i]);
        }
        
        // 복호화된 sn 배열을 MbrVO에 다시 설정
        MbrVO.setSelectedSns(decryptedSns);
        
        // MyBatis에서 단일 SQL로 일괄 삭제 처리
        return mbrMapper.deleteMbrArray(MbrVO);
    }
    
    /**
     * 회원 목록 조회 구현
     * 
     * <p>전체 회원 목록을 조회합니다.</p>
     * 
     * @return 회원 목록
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public List<MbrVO> selectMbrList(HashMap<String, Object> requestMap) throws Exception {
        List<MbrVO> mbrList = mbrMapper.selectMbrList(requestMap);

        for (MbrVO mbr : mbrList) {
        	// 암호화 처리
        	mbr.setSn(CryptoUtils.encrypt(mbr.getSn()));
            // 복호화 처리
            mbr.setMbrNm(CryptoUtils.decrypt(mbr.getMbrNm()));
            mbr.setMbrEmail(CryptoUtils.decrypt(mbr.getMbrEmail()));
            mbr.setMbrTelNo(CryptoUtils.decrypt(mbr.getMbrTelNo()));
        }

        return mbrList;
    }
    
    /**
     * 전체 회원 수 조회 구현
     *
     * <p>검색 조건에 맞는 전체 회원 수를 조회합니다.</p>
     * 
     * @param requestMap 검색 조건을 담은 맵
     * @return 전체 회원 수
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public int selectMbrTotalCnt(HashMap<String, Object> requestMap) throws Exception {
        return mbrMapper.selectMbrTotalCnt(requestMap);
    }
    
    /**
     * 회원 상세 정보 조회 구현
     * 
     * <p>특정 회원의 상세 정보를 조회합니다.</p>
     * 
     * @param mbrId 조회할 회원 아이디
     * @return 회원 상세 정보
     * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
     */
    @Override
    public MbrVO selectMbr(MbrVO mbrVO) throws Exception {
    	mbrVO.setSn(CryptoUtils.decrypt(mbrVO.getSn()));
        MbrVO mbr = mbrMapper.selectMbr(mbrVO);

        if (mbr != null) {
        	// 암호화 처리
        	mbr.setSn(CryptoUtils.encrypt(mbr.getSn()));
            // 복호화 처리
            mbr.setMbrNm(CryptoUtils.decrypt(mbr.getMbrNm()));
            mbr.setMbrEmail(CryptoUtils.decrypt(mbr.getMbrEmail()));
            mbr.setMbrTelNo(CryptoUtils.decrypt(mbr.getMbrTelNo()));
        }

        return mbr;
    }
}