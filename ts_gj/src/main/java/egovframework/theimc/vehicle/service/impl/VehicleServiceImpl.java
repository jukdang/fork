package egovframework.theimc.vehicle.service.impl;

import java.util.HashMap;
import java.util.List;

import org.egovframe.rte.fdl.cmmn.EgovAbstractServiceImpl;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.theimc.vehicle.service.VehicleService;

@Service
public class VehicleServiceImpl extends EgovAbstractServiceImpl implements VehicleService{
	
	@Autowired
    private VehicleMapper vehicleMapper;
	
	 /**
	  * 차량번호로 차량 목록 검색
	  */
	@Override
	public EgovMap selectRegNoList(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = vehicleMapper.selectRegNoList(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
	    return resultMap;
	}
	
	 /**
	  * 차량아이디로 차량 정보 조회
	  */
	@Override
	public EgovMap selectVehicleInfo(HashMap<String, Object> requestMap) throws Exception {
	    EgovMap result = vehicleMapper.selectVehicleInfo(requestMap);
	    return result;
	}
	  
	/**
	  * 차량유형으로 센서 검색
	  */
	@Override
	public EgovMap selectSensorInfo(HashMap<String, Object> requestMap) throws Exception {
	    List<EgovMap> list = vehicleMapper.selectSensorInfo(requestMap);
	    EgovMap resultMap = new EgovMap();
	    resultMap.put("list", list);
		return resultMap;
	}
}
