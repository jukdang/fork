/**
 * 로딩 오버레이를 표시하거나 숨기는 함수
 * @param {string} targetId - 로딩 오버레이를 표시할 대상 요소의 ID
 * @param {boolean} show - true면 로딩 표시, false면 숨김
 */
function toggleLoading(targetId, show) {
    const targetElement = document.getElementById(targetId);
    if (!targetElement) {
        console.warn(`ID가 '${targetId}'인 요소를 찾을 수 없습니다.`);
        return;
    }
    
    // 로딩 오버레이 ID 생성
    const loadingId = `${targetId}-loading-overlay`;
    
    // 기존 로딩 오버레이 확인
    let loadingOverlay = document.getElementById(loadingId);
    
    // show가 true이고 로딩 오버레이가 없는 경우, 생성
    if (show) {
        if (!loadingOverlay) {
            // 대상 요소의 position 스타일 확인
            const targetPosition = window.getComputedStyle(targetElement).position;
            if (targetPosition === 'static') {
                targetElement.style.position = 'relative';
            }
            
            // 로딩 오버레이 생성
            loadingOverlay = document.createElement('div');
            loadingOverlay.id = loadingId;
            loadingOverlay.className = 'loading-overlay';
            
            // 스피너 컨테이너 및 스피너 생성
            const spinnerContainer = document.createElement('div');
            spinnerContainer.className = 'spinner-container';
            
            const spinner = document.createElement('div');
            spinner.className = 'loading-spinner';
            
            spinnerContainer.appendChild(spinner);
            loadingOverlay.appendChild(spinnerContainer);
            
            // 로딩 오버레이 스타일 설정
            loadingOverlay.style.position = 'absolute';
            loadingOverlay.style.top = '0';
            loadingOverlay.style.left = '0';
            loadingOverlay.style.width = '100%';
            loadingOverlay.style.height = '100%';
            loadingOverlay.style.backgroundColor = 'rgba(200, 200, 200, 0.7)'; // 투명한 회색
            loadingOverlay.style.display = 'flex';
            loadingOverlay.style.justifyContent = 'center';
            loadingOverlay.style.alignItems = 'center';
            loadingOverlay.style.zIndex = '1000';
            loadingOverlay.style.borderRadius = '8px'; // 모서리 둥글게 (필요한 경우)
            
            // 스피너 컨테이너 스타일
            spinnerContainer.style.display = 'flex';
            spinnerContainer.style.justifyContent = 'center';
            spinnerContainer.style.alignItems = 'center';
            
            // 로딩 스피너 스타일 설정
            spinner.style.width = '30px';
            spinner.style.height = '30px';
            spinner.style.border = '3px solid transparent';
            spinner.style.borderTop = '3px solid #3498db'; // 파란색 테두리
            spinner.style.borderRight = '3px solid #3498db';
            spinner.style.borderRadius = '50%';
            spinner.style.animation = 'spin 1s linear infinite';
            
            // 애니메이션 키프레임 추가 (한 번만 추가)
            if (!document.getElementById('loading-spinner-keyframes')) {
                const keyframes = document.createElement('style');
                keyframes.id = 'loading-spinner-keyframes';
                keyframes.innerHTML = `
                    @keyframes spin {
                        0% { transform: rotate(0deg); }
                        100% { transform: rotate(360deg); }
                    }
                `;
                document.head.appendChild(keyframes);
            }
            
            // 대상 요소에 로딩 오버레이 추가
            targetElement.appendChild(loadingOverlay);
        } else {
            // 이미 있는 경우 표시
            loadingOverlay.style.display = 'flex';
        }
    } else if (loadingOverlay) {
        // show가 false이고 로딩 오버레이가 있는 경우, 숨김
        loadingOverlay.style.display = 'none';
    }
    
    return loadingOverlay; // 생성된 오버레이 반환 (선택적으로 사용 가능)
}

/**
 * 다중 로딩 오버레이를 표시하거나 숨기는 함수
 * @param {string} targetIds - 로딩 오버레이를 표시할 대상 요소의 ID들
 * @param {boolean} show - true면 로딩 표시, false면 숨김
 */
function toggleLoadings(targetIds, show) {
    // 여러 ID 지원: | 로 구분
    const ids = targetIds.split('|').map(id => id.trim()).filter(Boolean);

    ids.forEach(targetId => {
        const targetElement = document.getElementById(targetId);
        if (!targetElement) {
            console.warn(`ID가 '${targetId}'인 요소를 찾을 수 없습니다.`);
            return;
        }

        // 로딩 오버레이 ID 생성
        const loadingId = `${targetId}-loading-overlay`;

        // 기존 로딩 오버레이 확인
        let loadingOverlay = document.getElementById(loadingId);

        if (show) {
            if (!loadingOverlay) {
                const targetPosition = window.getComputedStyle(targetElement).position;
                if (targetPosition === 'static') {
                    targetElement.style.position = 'relative';
                }

                loadingOverlay = document.createElement('div');
                loadingOverlay.id = loadingId;
                loadingOverlay.className = 'loading-overlay';

                const spinnerContainer = document.createElement('div');
                spinnerContainer.className = 'spinner-container';

                const spinner = document.createElement('div');
                spinner.className = 'loading-spinner';

                spinnerContainer.appendChild(spinner);
                loadingOverlay.appendChild(spinnerContainer);

                // 스타일 설정
                loadingOverlay.style.position = 'absolute';
                loadingOverlay.style.top = '0';
                loadingOverlay.style.left = '0';
                loadingOverlay.style.width = '100%';
                loadingOverlay.style.height = '100%';
                loadingOverlay.style.backgroundColor = 'rgba(200, 200, 200, 0.7)';
                loadingOverlay.style.display = 'flex';
                loadingOverlay.style.justifyContent = 'center';
                loadingOverlay.style.alignItems = 'center';
                loadingOverlay.style.zIndex = '1000';
                loadingOverlay.style.borderRadius = '8px';

                spinnerContainer.style.display = 'flex';
                spinnerContainer.style.justifyContent = 'center';
                spinnerContainer.style.alignItems = 'center';

                spinner.style.width = '30px';
                spinner.style.height = '30px';
                spinner.style.border = '3px solid transparent';
                spinner.style.borderTop = '3px solid #3498db';
                spinner.style.borderRight = '3px solid #3498db';
                spinner.style.borderRadius = '50%';
                spinner.style.animation = 'spin 1s linear infinite';

                if (!document.getElementById('loading-spinner-keyframes')) {
                    const keyframes = document.createElement('style');
                    keyframes.id = 'loading-spinner-keyframes';
                    keyframes.innerHTML = `
                        @keyframes spin {
                            0% { transform: rotate(0deg); }
                            100% { transform: rotate(360deg); }
                        }
                    `;
                    document.head.appendChild(keyframes);
                }

                targetElement.appendChild(loadingOverlay);
            } else {
                loadingOverlay.style.display = 'flex';
            }
        } else if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }
    });
}

/**
* 로봇 애니메이션 로딩 오버레이를 표시하거나 숨기는 함수
* @param {string} targetId - 로딩 오버레이를 표시할 대상 요소의 ID
* @param {boolean} show - true면 로딩 표시, false면 숨김
*/
function toggleAiLoading(targetId, show) {
    const targetElement = document.getElementById(targetId);
    if (!targetElement) {
        console.warn(`ID가 '${targetId}'인 요소를 찾을 수 없습니다.`);
        return;
    }

    // 로딩 오버레이 ID 생성
    const loadingId = `${targetId}-loading-overlay`;

    // 기존 로딩 오버레이 확인
    let loadingOverlay = document.getElementById(loadingId);

    // show가 true이고 로딩 오버레이가 없는 경우, 생성
    if (show) {
        if (!loadingOverlay) {
            // 대상 요소의 position 스타일 확인
            const targetPosition = window.getComputedStyle(targetElement).position;
            if (targetPosition === 'static') {
                targetElement.style.position = 'relative';
            }

            // 로딩 오버레이 생성
            loadingOverlay = document.createElement('div');
            loadingOverlay.id = loadingId;
            loadingOverlay.className = 'loading-overlay';

            // 로봇 컨테이너 생성
            const robotContainer = document.createElement('div');
            robotContainer.className = 'robot-container';

            // 로봇 생성
            const robot = document.createElement('div');
            robot.className = 'robot';

            // 로봇 머리
            const robotHead = document.createElement('div');
            robotHead.className = 'robot-head';

            // 로봇 눈 (2개)
            const leftEye = document.createElement('div');
            leftEye.className = 'robot-eye left-eye';
            const rightEye = document.createElement('div');
            rightEye.className = 'robot-eye right-eye';

            // 로봇 안테나
            const antenna = document.createElement('div');
            antenna.className = 'robot-antenna';
            const antennaBall = document.createElement('div');
            antennaBall.className = 'antenna-ball';
            antenna.appendChild(antennaBall);

            // 로봇 몸통
            const robotBody = document.createElement('div');
            robotBody.className = 'robot-body';

            // 로봇 팔 (2개)
            const leftArm = document.createElement('div');
            leftArm.className = 'robot-arm left-arm';
            const rightArm = document.createElement('div');
            rightArm.className = 'robot-arm right-arm';

            // 로봇 다리 (2개)
            const leftLeg = document.createElement('div');
            leftLeg.className = 'robot-leg left-leg';
            const rightLeg = document.createElement('div');
            rightLeg.className = 'robot-leg right-leg';

            // 로딩 바 컨테이너
            const loadingBarContainer = document.createElement('div');
            loadingBarContainer.className = 'loading-bar-container';
            
            // 로딩 바 배경
            const loadingBarBg = document.createElement('div');
            loadingBarBg.className = 'loading-bar-bg';
            
            // 로딩 바 진행률
            const loadingBar = document.createElement('div');
            loadingBar.className = 'loading-bar';
            
            // 로딩 바 반짝이 효과
            const loadingBarShine = document.createElement('div');
            loadingBarShine.className = 'loading-bar-shine';
            
            // 로딩 텍스트 (작게)
            const loadingText = document.createElement('div');
            loadingText.className = 'loading-text';
            loadingText.textContent = '처리 중...';

            // 요소들 조립
            robotHead.appendChild(leftEye);
            robotHead.appendChild(rightEye);
            robotHead.appendChild(antenna);

            robot.appendChild(robotHead);
            robot.appendChild(robotBody);
            robot.appendChild(leftArm);
            robot.appendChild(rightArm);
            robot.appendChild(leftLeg);
            robot.appendChild(rightLeg);

            loadingBar.appendChild(loadingBarShine);
            loadingBarBg.appendChild(loadingBar);
            loadingBarContainer.appendChild(loadingBarBg);
            loadingBarContainer.appendChild(loadingText);

            robotContainer.appendChild(robot);
            robotContainer.appendChild(loadingBarContainer);

            loadingOverlay.appendChild(robotContainer);

            // 로딩 오버레이 기본 스타일 설정
            loadingOverlay.style.position = 'absolute';
            loadingOverlay.style.top = '0';
            loadingOverlay.style.left = '0';
            loadingOverlay.style.width = '100%';
            loadingOverlay.style.height = '100%';
            loadingOverlay.style.backgroundColor = 'rgba(200, 200, 200, 0.7)';
            loadingOverlay.style.display = 'flex';
            loadingOverlay.style.justifyContent = 'center';
            loadingOverlay.style.alignItems = 'center';
            loadingOverlay.style.zIndex = '1000';
            loadingOverlay.style.borderRadius = '8px';

            // 로봇 애니메이션 CSS 추가 (한 번만 추가)
            if (!document.getElementById('robot-loading-keyframes')) {
                const keyframes = document.createElement('style');
                keyframes.id = 'robot-loading-keyframes';
                keyframes.innerHTML = `
                    .robot-container {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        gap: 30px;
                    }

                    .robot {
                        position: relative;
                        animation: robotBounce 2s ease-in-out infinite;
                    }

                    .robot-head {
                        width: 40px;
                        height: 30px;
                        background: linear-gradient(135deg, #e3f2fd, #bbdefb);
                        border: 2px solid #2196f3;
                        border-radius: 8px 8px 15px 15px;
                        position: relative;
                        margin: 0 auto 5px;
                        box-shadow: 0 2px 8px rgba(33, 150, 243, 0.3);
                    }

                    .robot-eye {
                        width: 6px;
                        height: 6px;
                        background: #2196f3;
                        border-radius: 50%;
                        position: absolute;
                        top: 8px;
                        animation: robotBlink 3s ease-in-out infinite;
                    }

                    .left-eye { left: 10px; }
                    .right-eye { right: 10px; }

                    .robot-antenna {
                        width: 2px;
                        height: 12px;
                        background: #757575;
                        position: absolute;
                        top: -12px;
                        left: 50%;
                        transform: translateX(-50%);
                        animation: antennaWiggle 1.5s ease-in-out infinite;
                    }

                    .antenna-ball {
                        width: 4px;
                        height: 4px;
                        background: #ff5722;
                        border-radius: 50%;
                        position: absolute;
                        top: -2px;
                        left: 50%;
                        transform: translateX(-50%);
                        animation: antennaBallGlow 1s ease-in-out infinite alternate;
                    }

                    .robot-body {
                        width: 50px;
                        height: 35px;
                        background: linear-gradient(135deg, #e8f5e8, #c8e6c9);
                        border: 2px solid #4caf50;
                        border-radius: 12px;
                        position: relative;
                        margin: 0 auto;
                        box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
                    }

                    .robot-body::before {
                        content: '';
                        width: 8px;
                        height: 8px;
                        background: #4caf50;
                        border-radius: 50%;
                        position: absolute;
                        top: 50%;
                        left: 50%;
                        transform: translate(-50%, -50%);
                        animation: bodyLight 2s ease-in-out infinite;
                    }

                    .robot-arm {
                        width: 15px;
                        height: 4px;
                        background: #9e9e9e;
                        border-radius: 2px;
                        position: absolute;
                        top: 45px;
                    }

                    .left-arm {
                        left: -10px;
                        animation: leftArmWave 2s ease-in-out infinite;
                        transform-origin: right center;
                    }

                    .right-arm {
                        right: -10px;
                        animation: rightArmWave 2s ease-in-out infinite;
                        transform-origin: left center;
                    }

                    .robot-leg {
                        width: 6px;
                        height: 20px;
                        background: #9e9e9e;
                        border-radius: 3px;
                        position: absolute;
                        top: 75px;
                    }

                    .left-leg {
                        left: 17px;
                        animation: leftLegWalk 1s ease-in-out infinite;
                    }

                    .right-leg {
                        right: 17px;
                        animation: rightLegWalk 1s ease-in-out infinite;
                    }

                    .loading-bar-container {
                        display: flex;
                        flex-direction: column;
                        align-items: center;
                        gap: 8px;
                        width: 100px;
                    }

                    .loading-bar-bg {
                        width: 100%;
                        height: 8px;
                        background: rgba(255, 255, 255, 0.3);
                        border-radius: 4px;
                        overflow: hidden;
                        border: 1px solid rgba(255, 255, 255, 0.5);
                        position: relative;
                        box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.2);
                    }

                    .loading-bar {
                        height: 100%;
                        background: linear-gradient(90deg, #4caf50, #8bc34a, #4caf50);
                        background-size: 200% 100%;
                        border-radius: 3px;
                        position: relative;
                        overflow: hidden;
                        animation: loadingProgress 3s ease-in-out infinite, 
                                   gradientShift 2s linear infinite;
                    }

                    .loading-bar-shine {
                        position: absolute;
                        top: 0;
                        left: -100%;
                        width: 100%;
                        height: 100%;
                        background: linear-gradient(90deg, 
                            transparent, 
                            rgba(255, 255, 255, 0.6), 
                            transparent);
                        animation: barShine 2s ease-in-out infinite;
                    }

                    .loading-text {
                        font-family: Arial, sans-serif;
                        font-size: 11px;
                        color: #666;
                        font-weight: 600;
                        animation: textPulse 2s ease-in-out infinite;
                        letter-spacing: 1px;
                        text-transform: uppercase;
                    }

                    /* 애니메이션 키프레임 */
                    @keyframes robotBounce {
                        0%, 100% { transform: translateY(0px); }
                        50% { transform: translateY(-10px); }
                    }

                    @keyframes robotBlink {
                        0%, 90%, 100% { opacity: 1; }
                        95% { opacity: 0; }
                    }

                    @keyframes antennaWiggle {
                        0%, 100% { transform: translateX(-50%) rotate(0deg); }
                        25% { transform: translateX(-50%) rotate(-5deg); }
                        75% { transform: translateX(-50%) rotate(5deg); }
                    }

                    @keyframes antennaBallGlow {
                        0% { background: #ff5722; box-shadow: 0 0 5px #ff5722; }
                        100% { background: #ff9800; box-shadow: 0 0 10px #ff9800; }
                    }

                    @keyframes bodyLight {
                        0%, 100% { opacity: 0.6; transform: translate(-50%, -50%) scale(1); }
                        50% { opacity: 1; transform: translate(-50%, -50%) scale(1.2); }
                    }

                    @keyframes leftArmWave {
                        0%, 100% { transform: rotate(0deg); }
                        50% { transform: rotate(-20deg); }
                    }

                    @keyframes rightArmWave {
                        0%, 100% { transform: rotate(0deg); }
                        50% { transform: rotate(20deg); }
                    }

                    @keyframes leftLegWalk {
                        0%, 100% { transform: translateX(0px) rotate(0deg); }
                        50% { transform: translateX(2px) rotate(5deg); }
                    }

                    @keyframes rightLegWalk {
                        0%, 100% { transform: translateX(0px) rotate(0deg); }
                        50% { transform: translateX(-2px) rotate(-5deg); }
                    }

                    @keyframes loadingProgress {
                        0% { width: 0%; }
                        50% { width: 80%; }
                        100% { width: 100%; }
                    }

                    @keyframes gradientShift {
                        0% { background-position: 0% 0%; }
                        100% { background-position: 200% 0%; }
                    }

                    @keyframes barShine {
                        0% { left: -100%; }
                        50% { left: 100%; }
                        100% { left: 100%; }
                    }

                    @keyframes textPulse {
                        0%, 100% { opacity: 0.7; }
                        50% { opacity: 1; }
                    }
                `;
                document.head.appendChild(keyframes);
            }

            // 대상 요소에 로딩 오버레이 추가
            targetElement.appendChild(loadingOverlay);
        } else {
            // 이미 있는 경우 표시
            loadingOverlay.style.display = 'flex';
        }
    } else if (loadingOverlay) {
        // show가 false이고 로딩 오버레이가 있는 경우, 숨김
        loadingOverlay.style.display = 'none';
    }

    return loadingOverlay; // 생성된 오버레이 반환
}