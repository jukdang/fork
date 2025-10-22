/**
 * 전자정부프레임워크 페이징 JavaScript 유틸리티
 * EgovPaginationFormat과 동일한 HTML 구조를 JavaScript로 생성
 * 
 * @author 개발자
 * @since 2025.08.12
 * @version 1.0
 */

/**
 * 페이징 설정 함수
 * 
 * @param {string} paginationTagId - 페이징을 표시할 태그의 ID
 * @param {string} jsFunction - 페이지 클릭 시 호출할 JavaScript 함수명
 * @param {object} paginationInfo - PaginationInfo 객체
 * @param {object} options - 추가 옵션 (선택사항)
 */
function setPagination(paginationTagId, jsFunction, paginationInfo, options) {
    const paginationTag = $('#' + paginationTagId);
    
    // 기본 옵션 설정
    const defaultOptions = {
        useIcons: true,           // Font Awesome 아이콘 사용 여부
        showFirstLast: true,      // 처음/마지막 버튼 표시 여부
        showPrevNext: true,       // 이전/다음 버튼 표시 여부
        className: 'paging'       // CSS 클래스명
    };
    
    const config = Object.assign({}, defaultOptions, options || {});
    
    if (!paginationInfo || !paginationInfo.totalRecordCount || paginationInfo.totalRecordCount === 0) {
        paginationTag.empty();
        return;
    }
    
    // EgovPaginationFormat.paginationFormat() 메서드를 JavaScript로 구현
    const paginationHtml = createEgovPaginationHtml(paginationInfo, jsFunction, config);
    paginationTag.html(paginationHtml);
}

/**
 * EgovPaginationFormat의 paginationFormat 메서드를 JavaScript로 구현
 * 
 * @param {object} paginationInfo - PaginationInfo 객체
 * @param {string} jsFunction - JavaScript 함수명
 * @param {object} config - 설정 옵션
 * @returns {string} 페이징 HTML
 */
function createEgovPaginationHtml(paginationInfo, jsFunction, config) {
    const firstPageNo = paginationInfo.firstPageNo || 1;
    const firstPageNoOnPageList = paginationInfo.firstPageNoOnPageList || 1;
    const totalPageCount = paginationInfo.totalPageCount || 1;
    const pageSize = paginationInfo.pageSize || 10;
    const lastPageNoOnPageList = paginationInfo.lastPageNoOnPageList || 1;
    const currentPageNo = paginationInfo.currentPageNo || 1;
    const lastPageNo = paginationInfo.lastPageNo || totalPageCount;
    
    let html = '';
    
    // 페이지네이션 컨테이너 시작 (기존 EgovPaginationFormat 구조)
    html += `<div class="${config.className}" id="paging">`;
    html += '<div class="wr_page">';
    html += '<span class="num">';
    
    // 버튼 disabled 상태 확인
    const firstPageDisabled = (currentPageNo <= 1) ? 'disable' : '';
    const prevPageDisabled = (firstPageNoOnPageList <= 1) ? 'disable' : '';
    const nextPageDisabled = (lastPageNoOnPageList >= totalPageCount) ? 'disable' : '';
    const lastPageDisabled = (currentPageNo >= totalPageCount) ? 'disable' : '';
    
    // 첫 페이지 버튼 (EgovPaginationFormat 스타일)
    if (config.showFirstLast) {
        html += `<a href="#" onclick="${jsFunction}(${firstPageNo}); return false;" class="first ${firstPageDisabled}"><i class="fa fa-angle-double-left"></i></a>`;
    }
    
    // 이전 페이지 버튼 (EgovPaginationFormat 스타일)
    if (config.showPrevNext) {
        const prevPageNo = (firstPageNoOnPageList > pageSize) ? firstPageNoOnPageList - 1 : firstPageNo;
        html += `<a href="#" onclick="${jsFunction}(${prevPageNo}); return false;" class="pre ${prevPageDisabled}"><i class="fa fa-angle-left"></i></a>`;
    }
    
    // 페이지 번호 버튼들 (EgovPaginationFormat 스타일)
    for (let i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
        if (i === currentPageNo) {
            // 현재 페이지 (EgovPaginationFormat 스타일)
            html += `<a class="pageNoClass on">${i}</a>`;
        } else {
            // 다른 페이지 (EgovPaginationFormat 스타일)
            html += `<a class="pageNoClass" href="#" onclick="${jsFunction}(${i}); return false;">${i}</a>`;
        }
    }
    
    // 다음 페이지 버튼 (EgovPaginationFormat 스타일)
    if (config.showPrevNext) {
        const nextPageNo = (lastPageNoOnPageList < totalPageCount) ? firstPageNoOnPageList + pageSize : lastPageNo;
        html += `<a href="#" onclick="${jsFunction}(${nextPageNo}); return false;" class="next ${nextPageDisabled}"><i class="fa fa-angle-right"></i></a>`;
    }
    
    // 마지막 페이지 버튼 (EgovPaginationFormat 스타일)
    if (config.showFirstLast) {
        html += `<a href="#" onclick="${jsFunction}(${lastPageNo}); return false;" class="last ${lastPageDisabled}"><i class="fa fa-angle-double-right"></i></a>`;
    }
    
    // 페이지네이션 컨테이너 종료
    html += '</span>';
    html += '</div>';
    html += '</div>';
    
    return html;
}

/**
 * 페이지 버튼 생성 헬퍼 함수
 * 
 * @param {string} type - 버튼 타입 (first, pre, next, last)
 * @param {string} jsFunction - JavaScript 함수명
 * @param {number} pageNo - 페이지 번호
 * @param {string} disabled - disabled 클래스
 * @param {string} content - 버튼 내용 (아이콘 또는 텍스트)
 * @returns {string} 버튼 HTML
 */
function createPageButton(type, jsFunction, pageNo, disabled, content) {
    const disabledAttr = disabled ? 'style="pointer-events: none; opacity: 0.5;"' : '';
    return `<a href="#" onclick="${jsFunction}(${pageNo}); return false;" class="${type} ${disabled}" ${disabledAttr}>${content}</a>`;
}

/**
 * 간단한 페이징 설정 함수 (기본 옵션)
 * 
 * @param {string} paginationTagId - 페이징을 표시할 태그의 ID
 * @param {string} jsFunction - JavaScript 함수명
 * @param {object} paginationInfo - PaginationInfo 객체
 */
function setSimplePagination(paginationTagId, jsFunction, paginationInfo) {
    setPagination(paginationTagId, jsFunction, paginationInfo, {
        useIcons: false,
        showFirstLast: false
    });
}

/**
 * 페이징 정보 검증 함수
 * 
 * @param {object} paginationInfo - PaginationInfo 객체
 * @returns {boolean} 유효성 검증 결과
 */
function validatePaginationInfo(paginationInfo) {
    if (!paginationInfo) {
        console.warn('[Paging.js] PaginationInfo is null or undefined');
        return false;
    }
    
    const requiredFields = ['currentPageNo', 'recordCountPerPage', 'pageSize', 'totalRecordCount'];
    for (let field of requiredFields) {
        if (paginationInfo[field] === undefined || paginationInfo[field] === null) {
            console.warn(`[Paging.js] Required field '${field}' is missing in PaginationInfo`);
            return false;
        }
    }
    
    return true;
}

/**
 * 페이징 정보 로깅 함수 (디버깅용)
 * 
 * @param {object} paginationInfo - PaginationInfo 객체
 */
function logPaginationInfo(paginationInfo) {
    if (!paginationInfo) {
        console.log('[Paging.js] PaginationInfo is null');
        return;
    }
    
    console.log('[Paging.js] PaginationInfo:', {
        currentPageNo: paginationInfo.currentPageNo,
        totalPageCount: paginationInfo.totalPageCount,
        totalRecordCount: paginationInfo.totalRecordCount,
        recordCountPerPage: paginationInfo.recordCountPerPage,
        pageSize: paginationInfo.pageSize,
        firstPageNoOnPageList: paginationInfo.firstPageNoOnPageList,
        lastPageNoOnPageList: paginationInfo.lastPageNoOnPageList
    });
}