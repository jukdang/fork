package egovframework.theimc.pagination;
import org.egovframe.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import java.text.MessageFormat;
public class EgovPaginationFormat {
    // Font Awesome 아이콘을 사용하는 새로운 HTML 구조로 변경
    protected String firstPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\" class=\"first {2}\"><i class=\"fa fa-angle-double-left\"></i></a>";
    protected String previousPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\" class=\"pre {2}\"><i class=\"fa fa-angle-left\"></i></a>";
    protected String currentPageLabel = "<a class=\"pageNoClass on\">{0}</a>";
    protected String otherPageLabel = "<a class=\"pageNoClass\" onclick=\"{0}({1}); return false;\">{2}</a>";
    protected String nextPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\" class=\"next {2}\"><i class=\"fa fa-angle-right\"></i></a>";
    protected String lastPageLabel = "<a href=\"#\" onclick=\"{0}({1}); return false;\" class=\"last {2}\"><i class=\"fa fa-angle-double-right\"></i></a>";
    
    public String paginationFormat(PaginationInfo paginationInfo, String jsFunction) {
        StringBuffer stringBuffer = new StringBuffer();
        
        // 페이지네이션 컨테이너 시작
        stringBuffer.append("<div class=\"paging\" id=\"paging\">");
        stringBuffer.append("<div class=\"wr_page\">");
        stringBuffer.append("<span class=\"num\">");
        
        int firstPageNo = paginationInfo.getFirstPageNo();
        int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList();
        int totalPageCount = paginationInfo.getTotalPageCount();
        int pageSize = paginationInfo.getPageSize();
        int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList();
        int currentPageNo = paginationInfo.getCurrentPageNo();
        int lastPageNo = paginationInfo.getLastPageNo();
        
        // 첫 페이지와 이전 페이지 버튼 생성
        String firstPageDisabled = (currentPageNo <= 1) ? "disable" : "";
        String prevPageDisabled = (firstPageNoOnPageList <= 1) ? "disable" : "";
        
        // 첫 페이지 버튼
        stringBuffer.append(MessageFormat.format(firstPageLabel, 
            new Object[]{jsFunction, Integer.toString(firstPageNo), firstPageDisabled}));
        
        // 이전 페이지 버튼
        if (firstPageNoOnPageList > pageSize) {
            stringBuffer.append(MessageFormat.format(previousPageLabel, 
                new Object[]{jsFunction, Integer.toString(firstPageNoOnPageList-1), prevPageDisabled}));
        } else {
            stringBuffer.append(MessageFormat.format(previousPageLabel, 
                new Object[]{jsFunction, Integer.toString(firstPageNo), prevPageDisabled}));
        }
        
        // 페이지 번호 부분 시작
        stringBuffer.append("<span class=\"num\">");
        
        // 페이지 번호 버튼 생성
        for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
            if (i == currentPageNo) {
                stringBuffer.append(MessageFormat.format(currentPageLabel, new Object[]{Integer.toString(i)}));
            } else {
                stringBuffer.append(MessageFormat.format(otherPageLabel, new Object[]{jsFunction, Integer.toString(i), Integer.toString(i)}));
            }
        }
        
        stringBuffer.append("</span>");
        
        // 다음 페이지와 마지막 페이지 버튼 생성
        String nextPageDisabled = (lastPageNoOnPageList >= totalPageCount) ? "disable" : "";
        String lastPageDisabled = (currentPageNo >= totalPageCount) ? "disable" : "";
        
        // 다음 페이지 버튼
        if (lastPageNoOnPageList < totalPageCount) {
            stringBuffer.append(MessageFormat.format(nextPageLabel, 
                new Object[]{jsFunction, Integer.toString(firstPageNoOnPageList+pageSize), nextPageDisabled}));
        } else {
            stringBuffer.append(MessageFormat.format(nextPageLabel, 
                new Object[]{jsFunction, Integer.toString(lastPageNo), nextPageDisabled}));
        }
        
        // 마지막 페이지 버튼
        stringBuffer.append(MessageFormat.format(lastPageLabel, 
            new Object[]{jsFunction, Integer.toString(lastPageNo), lastPageDisabled}));
        
        // 페이지네이션 컨테이너 종료
        stringBuffer.append("</span>");
        stringBuffer.append("</div>");
        stringBuffer.append("</div>");
        
        return stringBuffer.toString();
    }
}