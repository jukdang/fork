/**
 * 날짜 범위를 설정하는 함수
 * @param {string} startDateId - 시작 날짜 input 요소의 ID
 * @param {string} endDateId - 종료 날짜 input 요소의 ID
 * @param {number} daysBefore - 오늘로부터 며칠 전을 시작 날짜로 설정할지 (기본값: 7)
 */
function setDateRange(startDateId, endDateId, daysBefore = 7) {
    // 오늘 날짜 가져오기
    const today = new Date();
    
    // 지정된 일수 전 날짜 계산
    const beforeDate = new Date();
    beforeDate.setDate(today.getDate() - daysBefore);
    
    // 날짜 형식 변환 (YYYY-MM-DD)
    const formatDate = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };
    
    // 시작 날짜와 종료 날짜 설정
    const startDateInput = document.getElementById(startDateId);
    const endDateInput = document.getElementById(endDateId);
    
    if (startDateInput) {
        startDateInput.value = formatDate(beforeDate);
    } else {
        console.warn(`ID가 '${startDateId}'인 요소를 찾을 수 없습니다.`);
    }
    
    if (endDateInput) {
        endDateInput.value = formatDate(today);
    } else {
        console.warn(`ID가 '${endDateId}'인 요소를 찾을 수 없습니다.`);
    }
    
    return {
        startDate: formatDate(beforeDate),
        endDate: formatDate(today)
    };
}

/**
 * 현재 날짜 범위 값을 반환하는 함수
 * @param {string} startDateId - 시작 날짜 input 요소의 ID
 * @param {string} endDateId - 종료 날짜 input 요소의 ID
 * @returns {Object} 시작 날짜와 종료 날짜를 포함하는 객체
 */
function getDateRange(startDateId, endDateId) {
    const startDateInput = document.getElementById(startDateId);
    const endDateInput = document.getElementById(endDateId);
    
    if (!startDateInput || !endDateInput) {
        console.warn('날짜 입력 요소를 찾을 수 없습니다.');
        return null;
    }
    
    return {
        startDate: startDateInput.value,
        endDate: endDateInput.value
    };
}