package egovframework.theimc.sample.web;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.egovframe.rte.psl.dataaccess.util.EgovMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import egovframework.theimc.sample.service.TheimcSampleService;
import egovframework.theimc.util.StatusMsg;

@RequestMapping(value = "/theimcSample")
@Controller
public class TheimcSampleController {

	@Autowired
	TheimcSampleService theimcSampleService;
	
	@ResponseBody
	@RequestMapping(value = "/selectSampleList")
	public ResponseEntity<StatusMsg> selectSampleList(@RequestParam HashMap<String, Object> requestMap) throws Exception {
			
			EgovMap resultList = theimcSampleService.selectSampleList(requestMap);
			//try catch로 하는게 정석인데 현 템플릿에는 aop에서 catch를 하기때문에 exception이 발생하지 않는 문제가 있음.
			if (resultList != null && !resultList.isEmpty()) {
				return StatusMsg.ok(resultList);
			}else {
				return StatusMsg.fail("조회 결과가 없습니다.");
			}
	}
	
	@GetMapping(value = "/sampleList")
 	public String sampleList(@RequestParam HashMap<String, Object> requestMap, ModelMap model, HttpServletRequest request) throws Exception {
		return "/sample/sampleList";
	}
}
