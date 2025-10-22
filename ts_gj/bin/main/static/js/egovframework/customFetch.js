/**
 * 커스텀 AJAX 요청 함수 (기본 로딩 + AI 로봇 로딩 지원)
 * @param {Object} options - 요청 옵션
 * @param {string} options.url - 요청 URL
 * @param {string} options.method - HTTP 메서드 (기본값: 'GET')
 * @param {Object} options.params - 요청 파라미터 (URLSearchParams로 변환됨)
 * @param {string} options.loadingId - 기본 스피너 로딩 표시할 요소 ID (옵션, 빈 값이면 로딩 표시 안함)
 * @param {string} options.loadingIds - 다중 기본 스피너 로딩 표시할 요소 ID 구분자(|) (옵션, 빈 값이면 로딩 표시 안함)
 * @param {string} options.aiLoadingId - AI 로봇 로딩 표시할 요소 ID (옵션, 빈 값이면 로딩 표시 안함)
 * @param {Function} options.onSuccess - 성공 시 콜백 함수 (데이터를 인자로 받음)
 * @param {Function} options.onError - 오류 시 콜백 함수 (오류를 인자로 받음)
 * @param {Function} options.onFinally - 항상 실행될 콜백 함수
 * @param {Object} options.headers - 추가 헤더 (기본 헤더에 병합됨)
 * @returns {Promise} - fetch 요청의 Promise 객체
 */
function customFetch(options) {
    const {
        url,
        method = 'GET',
        params = {},
        loadingId,
        loadingIds,
        aiLoadingId,
        onSuccess,
        onError,
        onFinally,
        headers = {},
        timeout = 5000 // 타임아웃 기본 5초
    } = options;

    // 기본 스피너 로딩 표시 시작 (loadingId가 제공되고 비어있지 않은 경우)
    if (loadingId && loadingId.trim() !== '') {
        toggleLoading(loadingId, true);
    }

    // 다중 기본 스피너 로딩 표시 시작 (loadingId가 제공되고 비어있지 않은 경우)
    if (loadingIds && loadingIds.trim() !== '') {
        toggleLoadings(loadingIds, true);
    }

    // AI 로봇 로딩 표시 시작 (aiLoadingId가 제공되고 비어있지 않은 경우)
    if (aiLoadingId && aiLoadingId.trim() !== '') {
        toggleAiLoading(aiLoadingId, true);
    }

    // 기본 헤더 설정
    const defaultHeaders = {
        'Content-Type': 'application/x-www-form-urlencoded'
    };
    
    // 사용자 지정 헤더와 병합
    const mergedHeaders = { ...defaultHeaders, ...headers };

	var contextPath = $('#contextPathHolder').attr('data-contextPath') ? $('#contextPathHolder').attr('data-contextPath') : '';
    // GET 요청일 때 URL에 쿼리스트링 추가
    let requestUrl = contextPath + url;
    let requestBody = undefined;
    
    if (method.toUpperCase() === 'GET') {
        // GET: params를 URL 쿼리스트링으로 변환
        if (Object.keys(params).length > 0) {
            const queryString = new URLSearchParams(params).toString();
            requestUrl = contextPath + url + (url.includes('?') ? '&' : '?') + queryString;
        }
    } else {
        // POST/PUT/DELETE: params를 body에 포함
        requestBody = new URLSearchParams(params);
    }

    // fetch 요청 실행
    return fetch(requestUrl, {
        method: method,
        headers: mergedHeaders,
        body: requestBody
    })
    .then(response => {
	    if (!response.ok) {
	        throw new Error(`HTTP 오류 ${response.status}: ${response.statusText}`);
	    }
	    
	    // 파일 다운로드인 경우 blob으로 처리
	    if (options.responseType === 'blob') {
	        return response.blob();
	    }
	    
	    // 기본은 JSON
	    return response.json();
	})
    .then(data => {
	    // 성공 콜백 실행
	    if (onSuccess && typeof onSuccess === 'function') {
	        onSuccess(data);  // blob인 경우 data가 blob 객체임
	    }
	    return data;
	})
    .catch(error => {
        if (error.name === 'AbortError') {
            console.log("요청 시간이 초과되었습니다. 페이지를 새로고침하고 다시 시도해주세요.");
            if (onError && typeof onError === 'function') {
                onError(new Error('요청 시간이 초과되었습니다. 페이지를 새로고침하고 다시 시도해주세요.'));
            }
        } else {
            console.error('AJAX 오류 발생:', error);
            if (onError && typeof onError === 'function') onError(error);
        }
        throw error;
    })
    .finally(() => {
        // 기본 스피너 로딩 표시 종료 (loadingId가 제공되고 비어있지 않은 경우)
        if (loadingId && loadingId.trim() !== '') {
            toggleLoading(loadingId, false);
        }
        
        // 다중 기본 스피너 로딩 표시 종료 (loadingId가 제공되고 비어있지 않은 경우)
        if (loadingIds && loadingIds.trim() !== '') {
	        toggleLoadings(loadingIds, false);
	    }

        // AI 로봇 로딩 표시 종료 (aiLoadingId가 제공되고 비어있지 않은 경우)
        if (aiLoadingId && aiLoadingId.trim() !== '') {
            toggleAiLoading(aiLoadingId, false);
        }
        
        // finally 콜백 실행
        if (onFinally && typeof onFinally === 'function') {
            onFinally();
        }
    });
}

/**
 * GET 요청 헬퍼 함수
 */
function customFetchGet(url, params, options = {}) {
    return customFetch({
        url,
        method: 'GET',
        params,
        ...options
    });
}

/**
 * POST 요청 헬퍼 함수
 */
function customFetchPost(url, params, options = {}) {
    return customFetch({
        url,
        method: 'POST',
        params,
        ...options
    });
}

/**
 * JSON 요청 헬퍼 함수 (Content-Type: application/json)
 */
function customFetchJson(url, data, options = {}) {
	
	var contextPath = $('#contextPathHolder').attr('data-contextPath') ? $('#contextPathHolder').attr('data-contextPath') : '';
	
    const jsonOptions = {
        ...options,
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        }
    };
    
    // 기본 스피너 로딩 표시 시작 (loadingId가 제공되고 비어있지 않은 경우)
    if (options.loadingId && options.loadingId.trim() !== '') {
        toggleLoading(options.loadingId, true);
    }
        
    // 다중 기본 스피너 로딩 표시 종료 (loadingId가 제공되고 비어있지 않은 경우)
    if (options.loadingIds && options.loadingIds.trim() !== '') {
        toggleLoadings(options.loadingIds, true);
    }

    // AI 로봇 로딩 표시 시작 (aiLoadingId가 제공되고 비어있지 않은 경우)
    if (options.aiLoadingId && options.aiLoadingId.trim() !== '') {
        toggleAiLoading(options.aiLoadingId, true);
    }
    
    // JSON 요청의 경우 body를 JSON 문자열로 변환
    return fetch(contextPath + url, {
        method: options.method || 'POST',
        headers: jsonOptions.headers,
        body: JSON.stringify(data)
    })
	.then(response => {
	    if (!response.ok) {
	        throw new Error(`HTTP 오류 ${response.status}: ${response.statusText}`);
	    }
	    
	    // 파일 다운로드인 경우 blob으로 처리
	    if (options.responseType === 'blob') {
	        return response.blob();
	    }
	    
	    // 기본은 JSON
	    return response.json();
	})
    .then(responseData => {
        if (options.onSuccess && typeof options.onSuccess === 'function') {
            options.onSuccess(responseData);
        }
        return responseData;
    })
    .catch(error => {
        console.error('AJAX 오류 발생:', error);
        if (options.onError && typeof options.onError === 'function') {
            options.onError(error);
        }
        throw error;
    })
    .finally(() => {
        // 기본 스피너 로딩 표시 종료 (loadingId가 제공되고 비어있지 않은 경우)
        if (options.loadingId && options.loadingId.trim() !== '') {
            toggleLoading(options.loadingId, false);
        }
        
	    // 다중 기본 스피너 로딩 표시 종료 (loadingId가 제공되고 비어있지 않은 경우)
	    if (options.loadingIds && options.loadingIds.trim() !== '') {
	        toggleLoadings(options.loadingIds, false);
	    }

        // AI 로봇 로딩 표시 종료 (aiLoadingId가 제공되고 비어있지 않은 경우)
        if (options.aiLoadingId && options.aiLoadingId.trim() !== '') {
            toggleAiLoading(options.aiLoadingId, false);
        }
        
        if (options.onFinally && typeof options.onFinally === 'function') {
            options.onFinally();
        }
    });
}