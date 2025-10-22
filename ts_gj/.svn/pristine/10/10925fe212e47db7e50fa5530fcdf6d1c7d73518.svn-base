package egovframework.theimc.mbr.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.psl.dataaccess.mapper.Mapper;

import egovframework.theimc.login.vo.MbrVO;
import egovframework.theimc.login.vo.ModifyMbrVO;

/**
 * 회원 정보 데이터 접근 매퍼 인터페이스
 * 
 * <p>회원 정보의 등록, 수정, 중복 체크, 목록 조회 등 데이터베이스 작업을 처리하는 매퍼 인터페이스입니다.
 * MyBatis와 연동하여 SQL 쿼리를 실행합니다.</p>
 *
 * @author  시스템 관리자
 * @version 1.0
 * @since   2025.05.01
 * @see     egovframework.theimc.mbr.service.impl.MbrServiceImpl
 */
@Mapper
public interface MbrMapper {
	
	/**
	 * 회원 등록 (관리자용)
	 * 
	 * <p>관리자가 등록한 회원 정보를 데이터베이스에 저장합니다.</p>
	 * 
	 * @param mbrVO 등록할 회원 정보
	 * @return 등록된 행 수 (성공: 1, 실패: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int insertMbr(MbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 등록 (사용자 가입용)
	 * 
	 * <p>사용자 회원가입을 통해 입력된 회원 정보를 데이터베이스에 저장합니다.
	 * 관리자용 등록과는 다른 권한 설정 및 추가 정보가 처리될 수 있습니다.</p>
	 * 
	 * @param mbrVO 등록할 회원 정보
	 * @return 등록된 행 수 (성공: 1, 실패: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int insertMbrSelf(MbrVO mbrVO) throws Exception;
	
	/**
	 * 아이디 중복 체크
	 * 
	 * <p>입력된 회원 아이디가 데이터베이스에 이미 존재하는지 확인합니다.</p>
	 * 
	 * @param mbrVO 중복 체크할 회원 정보 (아이디)
	 * @return 중복 여부 (중복: 1, 미중복: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int chkMbrId(MbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 정보 수정 (관리자용)
	 * 
	 * <p>관리자가 수정한 회원 정보를 데이터베이스에 업데이트합니다.
	 * 모든 회원 정보 필드를 수정할 수 있는 권한을 가집니다.</p>
	 * 
	 * @param mbrVO 수정할 회원 정보
	 * @return 수정된 행 수 (성공: 1, 실패: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int updateMbr(ModifyMbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 정보 수정 (사용자용)
	 * 
	 * <p>사용자가 수정한 자신의 정보를 데이터베이스에 업데이트합니다.
	 * 일반 사용자는 제한된 필드만 수정할 수 있습니다.</p>
	 * 
	 * @param mbrVO 수정할 회원 정보
	 * @return 수정된 행 수 (성공: 1, 실패: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int updateMbrSelf(ModifyMbrVO mbrVO) throws Exception;
	
	/**
	 * 회원 일괄 삭제 (단일 SQL)
	 * 
	 * @param MbrVO 삭제할 회원들의 sn 배열이 담긴 VO
	 * @return 삭제된 행 수
	 * @throws Exception
	 */
	public int deleteMbrArray(MbrVO MbrVO) throws Exception;
	
	/**
	 * 회원 목록 조회
	 * 
	 * <p>전체 회원 목록을 데이터베이스에서 조회합니다.</p>
	 * 
	 * @return 회원 목록
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	List<MbrVO> selectMbrList(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 전체 회원 수 조회
	 * 
	 * <p>검색 조건에 맞는 전체 회원 수를 데이터베이스에서 조회합니다.</p>
	 * 
	 * @param requestMap 검색 조건을 담은 맵
	 * @return 전체 회원 수
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int selectMbrTotalCnt(HashMap<String, Object> requestMap) throws Exception;
	
	/**
	 * 회원 상세 정보 조회
	 * 
	 * <p>특정 회원의 상세 정보를 데이터베이스에서 조회합니다.</p>
	 * 
	 * @param mbrId 조회할 회원 아이디
	 * @return 회원 상세 정보
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	MbrVO selectMbr(MbrVO MbrVO) throws Exception;

	/**
	 * 회원 등록 (관리자용)
	 * 
	 * <p>관리자가 등록한 회원 정보를 데이터베이스에 저장합니다.</p>
	 * 
	 * @param mbrVO 등록할 회원 정보
	 * @return 등록된 행 수 (성공: 1, 실패: 0)
	 * @throws Exception 데이터베이스 처리 중 발생할 수 있는 예외
	 */
	int insertMbrRoleHstry(HashMap<String, Object> requestMap) throws Exception;
}