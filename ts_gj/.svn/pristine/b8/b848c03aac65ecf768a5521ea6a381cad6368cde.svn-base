package egovframework.theimc.vehicle.service.impl;
import org.egovframe.rte.psl.dataaccess.mapper.Mapper;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import java.util.HashMap;
import java.util.List;

/**
 * sample에 관한 데이터처리 매퍼 클래스
 *
 * @author  표준프레임워크센터
 * @since 2014.01.24
 * @version 1.0
 * @see <pre>
 *  == 개정이력(Modification Information) ==
 *
 *          수정일          수정자           수정내용
 *  ----------------    ------------    ---------------------------
 *   2014.01.24        표준프레임워크센터          최초 생성
 *
 * </pre>
 */
@Mapper
public interface VehicleMapper {

	 /**
	  * 차량번호로 차량 목록 검색
	  */
	List<EgovMap> selectRegNoList(HashMap<String, Object> requestMap) throws Exception;
	 
	 /**
	  * 차량아이디로 차량 정보 조회
	  */
	EgovMap selectVehicleInfo(HashMap<String, Object> requestMap) throws Exception;

	/**
	  * 차량유형으로 센서 검색
	  */
	List<EgovMap> selectSensorInfo(HashMap<String, Object> requestMap) throws Exception;
	 
	/**
	  * 차량 목록 조회
	  */
	List<EgovMap> selectVehicleHist(HashMap<String, Object> requestMap) throws Exception;
	 /**
	  * 차량 목록 건수
	  */
	int selectVehicleHistCnt(HashMap<String, Object> requestMap) throws Exception;
}
