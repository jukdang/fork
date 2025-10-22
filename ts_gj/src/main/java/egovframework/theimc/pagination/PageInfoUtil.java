package egovframework.theimc.pagination;

import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import java.util.Map;

/**
 * 페이징 처리를 위한 유틸리티 클래스
 * 전자정부프레임워크 PaginationInfo의 반복적인 설정 작업을 간소화
 *
 * @author 개발자
 * @since 2025.08.12
 */
public class PageInfoUtil {
    
    /** 기본 페이지당 레코드 수 */
    private static final int DEFAULT_RECORD_COUNT_PER_PAGE = 10;
    
    /** 기본 페이지 리스트 크기 */
    private static final int DEFAULT_PAGE_SIZE = 10;
    
    /**
     * 페이지 정보를 설정하고 Map에 페이징 파라미터를 추가
     * 
     * @param requestMap 요청 파라미터 맵 (페이징 파라미터가 추가됨)
     * @param currentPageNo 현재 페이지 번호
     * @param totalRecordCount 전체 레코드 수
     * @return 설정된 PaginationInfo 객체
     */
    public static PaginationInfo setPageInfo(Map<String, Object> requestMap, 
                                           int currentPageNo, 
                                           int totalRecordCount) {
        return setPageInfo(requestMap, currentPageNo, totalRecordCount, 
                          DEFAULT_RECORD_COUNT_PER_PAGE, DEFAULT_PAGE_SIZE);
    }
    
    /**
     * 페이지 정보를 설정하고 Map에 페이징 파라미터를 추가 (페이지당 레코드 수 지정)
     * 
     * @param requestMap 요청 파라미터 맵
     * @param currentPageNo 현재 페이지 번호
     * @param totalRecordCount 전체 레코드 수
     * @param recordCountPerPage 페이지당 레코드 수
     * @return 설정된 PaginationInfo 객체
     */
    public static PaginationInfo setPageInfo(Map<String, Object> requestMap,
                                           int currentPageNo, 
                                           int totalRecordCount,
                                           int recordCountPerPage) {
        return setPageInfo(requestMap, currentPageNo, totalRecordCount, 
                          recordCountPerPage, DEFAULT_PAGE_SIZE);
    }
    
    /**
     * 페이지 정보를 설정하고 Map에 페이징 파라미터를 추가 (모든 파라미터 지정)
     * 
     * @param requestMap 요청 파라미터 맵
     * @param currentPageNo 현재 페이지 번호
     * @param totalRecordCount 전체 레코드 수
     * @param recordCountPerPage 페이지당 레코드 수
     * @param pageSize 페이지 리스트 크기
     * @return 설정된 PaginationInfo 객체
     */
    public static PaginationInfo setPageInfo(Map<String, Object> requestMap,
                                           int currentPageNo,
                                           int totalRecordCount,
                                           int recordCountPerPage,
                                           int pageSize) {
        
        PaginationInfo paginationInfo = new PaginationInfo();
        
        // Required Fields 설정
        paginationInfo.setCurrentPageNo(currentPageNo);
        paginationInfo.setRecordCountPerPage(recordCountPerPage);
        paginationInfo.setPageSize(pageSize);
        paginationInfo.setTotalRecordCount(totalRecordCount);
        
        // requestMap에 페이징 파라미터 추가
        requestMap.put("firstRecordIndex", paginationInfo.getFirstRecordIndex());
        requestMap.put("lastRecordIndex", paginationInfo.getLastRecordIndex());
        requestMap.put("recordCountPerPage", paginationInfo.getRecordCountPerPage());
        
        return paginationInfo;
    }
    
    /**
     * requestMap에서 pageIndex를 추출하여 페이지 정보 설정
     * 
     * @param requestMap 요청 파라미터 맵 (pageIndex 포함)
     * @param totalRecordCount 전체 레코드 수
     * @return 설정된 PaginationInfo 객체
     */
    public static PaginationInfo setPageInfoFromMap(Map<String, Object> requestMap, 
                                                  int totalRecordCount) {
        int pageIndex = 1; // 기본값
        
        if (requestMap.containsKey("pageIndex")) {
            try {
                pageIndex = Integer.parseInt(requestMap.get("pageIndex").toString());
            } catch (NumberFormatException e) {
                pageIndex = 1; // 파싱 실패 시 기본값
            }
        }
        
        return setPageInfo(requestMap, pageIndex, totalRecordCount);
    }
    
    /**
     * 페이징 파라미터만 미리 설정 (전체 건수 조회 전에 사용)
     * 
     * @param requestMap 요청 파라미터 맵
     * @return 현재 페이지 번호
     */
    public static int setPageParams(Map<String, Object> requestMap) {
        return setPageParams(requestMap, DEFAULT_RECORD_COUNT_PER_PAGE);
    }
    
    /**
     * 페이징 파라미터만 미리 설정 (전체 건수 조회 전에 사용)
     * 
     * @param requestMap 요청 파라미터 맵
     * @param recordCountPerPage 페이지당 레코드 수
     * @return 현재 페이지 번호
     */
    public static int setPageParams(Map<String, Object> requestMap, int recordCountPerPage) {
        int pageIndex = 1;
        
        // pageIndex 추출
        if (requestMap.containsKey("pageIndex")) {
            try {
                Object pageIndexObj = requestMap.get("pageIndex");
                if (pageIndexObj != null) {
                    pageIndex = Integer.parseInt(pageIndexObj.toString());
                    if (pageIndex < 1) {
                        pageIndex = 1;
                    }
                }
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        
        // 페이징에 필요한 기본 파라미터 계산 및 설정
        int firstRecordIndex = (pageIndex - 1) * recordCountPerPage;
        
        // requestMap에 반드시 추가 (기존 값 덮어쓰기)
        requestMap.put("recordCountPerPage", recordCountPerPage);
        requestMap.put("firstRecordIndex", firstRecordIndex);
        requestMap.put("firstIndex", firstRecordIndex);  // 기존 이름도 추가
        requestMap.put("lastRecordIndex", pageIndex * recordCountPerPage);
        requestMap.put("currentPageNo", pageIndex);
        requestMap.put("pageIndex", pageIndex); // 혹시 모르니 이것도 설정
        
        return pageIndex;
    }
    
    /**
     * requestMap에서 pageIndex를 추출하여 페이지 정보 설정 (페이지당 레코드 수 지정)
     * 
     * @param requestMap 요청 파라미터 맵
     * @param totalRecordCount 전체 레코드 수
     * @param recordCountPerPage 페이지당 레코드 수
     * @return 설정된 PaginationInfo 객체
     */
    public static PaginationInfo setPageInfoFromMap(Map<String, Object> requestMap,
                                                  int totalRecordCount,
                                                  int recordCountPerPage) {
        int pageIndex = 1;
        
        if (requestMap.containsKey("pageIndex")) {
            try {
                pageIndex = Integer.parseInt(requestMap.get("pageIndex").toString());
            } catch (NumberFormatException e) {
                pageIndex = 1;
            }
        }
        
        return setPageInfo(requestMap, pageIndex, totalRecordCount, recordCountPerPage);
    }
}