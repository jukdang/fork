#!/bin/bash

# 로그 파일 경로
LOG_FILE="./logs/ts_gj_sys.log"

# 애플리케이션 프로세스를 찾기 위한 문자열
PROCESS_STRING="ts_gj-exe.jar"

# 로그 파일에 내용을 추가하면서 콘솔에 출력하는 함수
log_and_echo() {
    local message="$1"
    echo "$message"
    echo "$message" >> "$LOG_FILE"
}

# 로그 파일에서 애플리케이션 PID를 추출
APP_PID=$(pgrep -f "$PROCESS_STRING")

if [ -z "$APP_PID" ]; then
    log_and_echo "애플리케이션을 찾을 수 없습니다."
else
    log_and_echo "애플리케이션을 중지합니다. PID: $APP_PID"
    kill -9 "$APP_PID"
    # 애플리케이션이 완전히 종료될 때까지 기다림
    while [ -e "/proc/$APP_PID" ]; do
        sleep 1
    done
    log_and_echo "애플리케이션이 성공적으로 중지되었습니다."
fi

