package egovframework.theimc.mbr.service;

import java.util.HashMap;
import java.util.List;

import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;

/**
 * 회원 관리 서비스 인터페이스
 * 
 * <p>회원 등록, 수정, 중복 체크, 목록 조회 등의 기능을 제공하는 서비스 인터페이스</p>
 *
 * @author  시스템 관리자
 * @version 1.0
 * @since   2025.05.01
 */
public interface MbrService {
	
	/**
	 * 회원 등록 (관리자용)
	 * 
	 * <p>관리자가 회원 정보를 등록하는 기능</p>
	 * 
	 * @param mbrVO 등록할 회원 정보
	 * @return 등록된 행 수 (성공: 1, 실패: 0)
	 */
	int insertMbr(MbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 등록 (사용자용)
	 * 
	 * <p>사용자가 직접 회원가입을 통해 정보를 등록하는 기능</p>
	 * 
	 * @param mbrVO 등록할 회원 정보
	 * @return 등록된 행 수 (성공: 1, 실패: 0)
	 */
	int insertMbrSelf(MbrVO mbrVO) throws Exception;
	
	/**
	 * 아이디 중복 체크
	 * 
	 * <p>회원가입 시 입력한 아이디의 중복 여부를 체크하는 기능</p>
	 * 
	 * @param mbrVO 중복 체크할 회원 정보 (아이디)
	 * @return 중복 여부 (중복: 1, 미중복: 0)
	 */
	int chkMbrId(MbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 정보 수정 (관리자용)
	 * 
	 * <p>관리자가 회원 정보를 수정하는 기능</p>
	 * 
	 * @param mbrVO 수정할 회원 정보
	 * @return 수정된 행 수 (성공: 1, 실패: 0)
	 */
	int updateMbr(ModifyMbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 정보 수정 (사용자용)
	 * 
	 * <p>사용자가 자신의 정보를 수정하는 기능</p>
	 * 
	 * @param mbrVO 수정할 회원 정보
	 * @return 수정된 행 수 (성공: 1, 실패: 0)
	 */
	int updateMbrSelf(ModifyMbrVO mbrVO) throws Exception;
	
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
	public int deleteMbrArray(MbrVO mbrVO) throws Exception;
	/**
	 * 회원 목록 조회
	 * 
	 * <p>전체 회원 목록을 조회하는 기능</p>
	 * 
	 * @return 회원 목록
	 */
	List<MbrVO> selectMbrList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 전체 회원 수 조회
	 * 
	 * <p>검색 조건에 맞는 전체 회원 수를 조회하는 기능</p>
	 * 
	 * @param requestMap 검색 조건을 담은 맵
	 * @return 전체 회원 수
	 */
	int selectMbrTotalCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 회원 상세 정보 조회
	 * 
	 * <p>특정 회원의 상세 정보를 조회하는 기능</p>
	 * 
	 * @param mbrId 조회할 회원 아이디
	 * @return 회원 상세 정보
	 */
	MbrVO selectMbr(MbrVO mbrVO) throws Exception;
}