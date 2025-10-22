package egovframework.theimc.rfid.service.impl;

import egovframework.theimc.rfid.vo.RfidDataVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface RfidDataMapper {
    
    /**
     * RFID 운행 데이터 저장
     */
    int insertRfidData(RfidDataVO rfidDataVO);
    
    /**
     * MT_ID로 RFID 데이터 조회
     */
    List<RfidDataVO> selectByMtId(String mtId);
    
    /**
     * RFID 번호로 최근 데이터 조회
     */
    List<RfidDataVO> selectByRfidNo(String rfidNo);
}