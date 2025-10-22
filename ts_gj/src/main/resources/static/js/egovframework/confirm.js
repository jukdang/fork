function customConfirm(message, callback) {
    // 대화 상자를 HTML 요소로 생성(동적)
    var confirmBox = document.createElement('div');
    confirmBox.setAttribute('class', 'confirm-box');
    confirmBox.innerHTML = 
    `<div class="pop_wrap" id="popWrap">
        <div class="pop_cont pop_layer">
            <div class="pop_head">
                <span>알림</span>
            </div>
            <div class="pop_body">
                <div id="" class="pop_alert">${message}</div>
                <div class="tac">
                    <button id="cancel" class="btn mid grey mr8">취소</button>
                    <button id="confirm" class="btn mid blue">확인</button>
                </div>
            </div>
        </div>
    </div>`;
    
    // HTML 요소를 body 요소의 하위 요소로 추가합니다.
    document.body.appendChild(confirmBox);
    
    return new Promise(function(resolve) {
        var popWrap = $("#popWrap");
        var popLayer = $(".pop_cont.pop_layer");
        
        // 확인 버튼을 클릭했을 때 이벤트
        $("#confirm").click(function() {
            setTimeout(function() {
                resolve(true);
                confirmBox.remove();
            }, 100);
        });
        
        // 취소 버튼을 클릭했을 때 이벤트
        $("#cancel").click(function() {
            setTimeout(function() {
                resolve(false);
                confirmBox.remove();
            }, 100);
        });
        
        // pop_cont pop_layer 바깥을 클릭했을 때 이벤트
        popWrap.click(function(event) {
            if (!$(event.target).closest(popLayer).length) {
	                setTimeout(function() {
	                resolve(false);
	                confirmBox.remove();
	            }, 100);
            }
        });

		$(document).on('keydown', function(event) {
            if (event.key === "Enter" && $('.confirm-box').length > 0) {
                // confirmBox가 활성화된 상태에서만 동작
                $("#confirm").click(); // 확인 버튼 클릭 이벤트 트리거
            }
        });
    });
}