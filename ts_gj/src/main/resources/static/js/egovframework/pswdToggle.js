/**
 * 비밀번호 표시/숨김 기능
 * 
 * 이 스크립트는 비밀번호 입력 필드 옆에 있는 아이콘을 클릭하면
 * 비밀번호가 보이거나 숨겨지는 기능을 제공합니다.
 * 
 * 사용법:
 * 1. 이 스크립트를 포함시킨 후 다음 형식으로 HTML을 구성하세요:
 * 
 * <div class="pass_input">
 *   <input type="password" id="password">
 *   <span class="icon_view">
 *     <i class="fa-regular fa-eye" style="display: none;"></i>
 *     <i class="fa-regular fa-eye-slash"></i>
 *   </span>
 * </div>
 * 
 * 2. jQuery가 포함되어 있어야 합니다.
 * 
 * @requires jQuery
 * @author 사용자
 * @version 1.0
 */

/**
 * 페이지 로드 후 비밀번호 표시/숨김 기능 초기화
 */
$(document).ready(function() {
    initPasswordToggle();
});

/**
 * 비밀번호 표시/숨김 기능 초기화
 */
function initPasswordToggle() {
    // 비밀번호 표시/숨김 토글
    $(".icon_view").click(function() {
        var inputField = $(this).siblings('input');
        var eyeOpen = $(this).find('.fa-eye');
        var eyeClosed = $(this).find('.fa-eye-slash');
        
        if (inputField.attr('type') === 'password') {
            inputField.attr('type', 'text');
            eyeOpen.show();
            eyeClosed.hide();
        } else {
            inputField.attr('type', 'password');
            eyeOpen.hide();
            eyeClosed.show();
        }
    });
    
    // 비밀번호 표시/숨김 아이콘에 마우스 오버 효과 추가
    $(".icon_view").hover(function() {
        $(this).css('cursor', 'pointer');
    });
}

/**
 * 동적으로 생성된 비밀번호 필드에 토글 기능 적용
 * 
 * @param {string} containerSelector - 비밀번호 필드가 포함된 컨테이너의 선택자
 */
function applyPasswordToggle(containerSelector) {
    $(containerSelector).find(".icon_view").off('click').click(function() {
        var inputField = $(this).siblings('input');
        var eyeOpen = $(this).find('.fa-eye');
        var eyeClosed = $(this).find('.fa-eye-slash');
        
        if (inputField.attr('type') === 'password') {
            inputField.attr('type', 'text');
            eyeOpen.show();
            eyeClosed.hide();
        } else {
            inputField.attr('type', 'password');
            eyeOpen.hide();
            eyeClosed.show();
        }
    });
    
    $(containerSelector).find(".icon_view").hover(function() {
        $(this).css('cursor', 'pointer');
    });
}