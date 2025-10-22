// CSRF 자동 처리를 위한 공통 스크립트
(function() {
    // 페이지가 로드되면 실행
    document.addEventListener('DOMContentLoaded', function() {
        // Ajax 요청 전에 CSRF 토큰을 자동으로 추가
        const token = $("meta[name='_csrf']").attr("content");
        const header = $("meta[name='_csrf_header']").attr("content");
        
        if (token && header) {
            // jQuery Ajax에 기본 헤더 추가
            $(document).ajaxSend(function(e, xhr, options) {
                xhr.setRequestHeader(header, token);
            });
            
            // Fetch API를 위한 원래 fetch 함수 저장
            const originalFetch = window.fetch;
            
            // Fetch API 오버라이드
            window.fetch = function(url, options = {}) {
                // 새 options 객체 생성
                const newOptions = { ...options };
                
                // headers 객체가 없으면 생성
                if (!newOptions.headers) {
                    newOptions.headers = {};
                } else if (newOptions.headers instanceof Headers) {
                    // Headers 객체인 경우 일반 객체로 변환
                    const originalHeaders = newOptions.headers;
                    newOptions.headers = {};
                    for (const [key, value] of originalHeaders.entries()) {
                        newOptions.headers[key] = value;
                    }
                }
                
                // 객체가 아닌 경우 (예: Headers 인스턴스) 객체로 변환
                if (typeof newOptions.headers !== 'object') {
                    newOptions.headers = {};
                }
                
                // CSRF 헤더 추가 (이미 있는 경우 덮어쓰지 않음)
                if (!newOptions.headers[header]) {
                    newOptions.headers[header] = token;
                }
                
                // 원래의 fetch 호출
                return originalFetch(url, newOptions);
            };
        }
    });
})();