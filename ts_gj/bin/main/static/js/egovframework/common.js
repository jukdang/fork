/**
 * 값이 null, undefined, 빈 문자열('')일 경우 기본값으로 대체
 * @param {any} value - 검사할 값
 * @param {any} defaultValue - 대체할 기본값
 * @returns {any} value가 유효하면 그대로, 아니면 defaultValue 반환
 */
function defaultIfEmptyOrNull(value, defaultValue) {
  if (value === null || value === undefined || value === '') {
    return defaultValue;
  }
  return value;
}

// HTML 특수문자 이스케이프 (XSS 방지용)
function escapeHtml(text) {
    if (!text) return '';
    return text
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

//숫자 3자리수 콤바 처리
function formatNumber(input) {
    let num = Number(input);
	
    // 숫자가 아니면 "-"
    if (isNaN(num)) return "";

    // 소수점 2자리까지만 버림
    num = Math.floor(num * 100) / 100;

    // 소수점 끝 0은 자동 제거됨
    return num.toLocaleString('en-US');
}

//모바일 체크
function isMobile() {
    return /Mobi|Android|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent);
}

function vhclMasked(regNo) {
	let maskedYn = $("#vhclMaskedYn").val() === "true";
    if (!maskedYn || !regNo) {
        return regNo || '';
    }

    // 앞 두 글자만 남기고 나머지는 * 처리
    const visible = regNo.slice(0, 5);
    const masked = '*'.repeat(regNo.length - 5);
    return visible + masked;
}

function oprtrMasked(oprtrNm) {
    let maskedYn = $("#oprtrMaskedYn").val() === "true";
    if (!maskedYn || !oprtrNm) {
        return oprtrNm || '';
    }

    const firstChar = oprtrNm.charAt(0); // 성
    const masked = '*'.repeat(oprtrNm.length - 1);
    return firstChar + masked;
}

function ownrMasked(ownrNm) {
    let maskedYn = $("#ownrMaskedYn").val() === "true";
    if (!maskedYn || !ownrNm || ownrNm.length <= 2) {
        return ownrNm || '';
    }

    const masked = '**'; // 앞 두글자를 **로 처리
    const lastChars = ownrNm.substring(2); // 뒤 글자들
    return masked + lastChars;
}


function displayOrDash(value) {
  if (value === null || value === undefined) return "-";
  if (typeof value === "string" && value.trim() === "") return "-";
  if (Array.isArray(value) && value.length === 0) return "-";
  if (typeof value === "object" && Object.keys(value).length === 0) return "-";
  return value;
}

function initDateValidation(startDateId, endDateId) {
    const startInput = document.getElementById(startDateId);
    const endInput = document.getElementById(endDateId);
    
    function validateDates() {
        if (startInput.value && endInput.value && startInput.value > endInput.value) {
            alert('시작일은 종료일보다 늦을 수 없습니다.');
            startInput.value = endInput.value;
        }
        
        // 날짜 제한 설정
        if (endInput.value) startInput.max = endInput.value;
        if (startInput.value) endInput.min = startInput.value;
    }
    
    startInput.addEventListener('change', validateDates);
    endInput.addEventListener('change', validateDates);
}

function initDateValidation(startDateId, endDateId) {
    const startInput = document.getElementById(startDateId);
    const endInput = document.getElementById(endDateId);
    
    if (!startInput || !endInput) {
        return; // 요소가 없으면 종료
    }
    
    function validateDates() {
        if (startInput.value && endInput.value && startInput.value > endInput.value) {
            alert('시작일은 종료일보다 늦을 수 없습니다.');
            startInput.value = endInput.value;
        }
        
        // 날짜 제한 설정
        if (endInput.value) startInput.max = endInput.value;
        if (startInput.value) endInput.min = startInput.value;
    }
    
    startInput.addEventListener('change', validateDates);
    endInput.addEventListener('change', validateDates);
}

// DOM이 준비되면 자동으로 날짜 유효성 검사 초기화
document.addEventListener('DOMContentLoaded', function() {
    // 일반적인 날짜 input ID들에 대해 자동으로 유효성 검사 설정
    const dateValidationPairs = [
        { start: 'startDt', end: 'endDt' },
        { start: 'startDate', end: 'endDate' },
        { start: 'searchStartDt', end: 'searchEndDt' },
        { start: 'reportStartDt', end: 'reportEndDt' }
        // 필요한 다른 날짜 쌍들을 여기에 추가
    ];
    
    dateValidationPairs.forEach(pair => {
        initDateValidation(pair.start, pair.end);
    });
});
