#!/bin/bash

# 애플리케이션 실행 명령어
APP_JAR="ts_gj-exe.jar"

# 활성화할 프로파일
PROFILE="prod"

# 로그 파일 경로
LOG_FILE="./logs/ts_gj_sys.log"

# 업로드 war파일과 실행 war파일명이 같으면 업로드시 어플리케이션이 오류가 나서 war파일명을 서로 다르게 한다.
# 업로드가 완료 되었는지 확인하고 실행파일을 백업 후 업로드 파일명을 실행파일명으로 이름 변경.
if [ -f "./ts_gj.jar" ]; then
    # war 파일 백업
    mv ./ts_gj-exe.jar ./ts_gj-exe_bak.jar
    mv ./ts_gj.jar ./ts_gj-exe.jar
    echo "War Backup SUCCESS"
fi

# 애플리케이션 실행
nohup java -jar $APP_JAR --spring.profiles.active=$PROFILE >> $LOG_FILE 2>&1 &

# 로그 확인을 위한 tail
tail -n 0 -f $LOG_FILE | while IFS= read -r line; do
    echo "$line"
    # 로그에서 실패 메시지를 찾음
    echo "$line" | grep -q "FAILED"
    if [ $? -eq 0 ]; then
        echo "Failure detected: $line"
        break
    fi

    # 로그에서 성공 메시지를 찾음
    echo "$line" | grep -q "Tomcat started on port"
    if [ $? -eq 0 ]; then
        echo "Success detected: $line"
        break
    fi
done
