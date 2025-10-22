var chart;
var root;
var globalVehicleData = {};
var vehicleUpdateInterval; // 차량 정보 업데이트 인터벌 변수
var vhclInfoAreaUpdateInterval; // 차량 상세 정보 업데이트 인터벌 변수
var existingPointSeries = []; // 기존 포인트 시리즈들을 저장할 배열
let mapVhclCircles = [];

// 차량 정보를 전역변수에 저장하는 함수
function storeVehicleDataGlobally(mergedVehicleData) {
    globalVehicleData = {};
    
    mergedVehicleData.forEach(vehicle => {
        globalVehicleData[vehicle.vhclSn] = vehicle;
    });
    
    //console.log('전역 차량 데이터 저장 완료:', globalVehicleData);
}

// vhclSn으로 차량 정보를 조회하는 함수
function getVehicleInfoByVhclSn(vhclSn) {
	
    const vehicleInfo = globalVehicleData[vhclSn];
    
    if (vehicleInfo) {
		const fuelNm = {
		    FU06: "CNG",
		    FU02: "경유",
		    FU05: "수소",
		    FU04: "전기"
		};
		
        //console.log('=== 차량 정보 조회 결과 ===');
        //console.log('차량일련번호(vhclSn):', vehicleInfo.vhclSn);
        //console.log('차량번호(regNo):', vehicleInfo.regNo);
        //console.log('운영사명(oprtrNm):', vehicleInfo.oprtrNm);
        //console.log('차량상태(vhclStts):', vehicleInfo.vhclStts);
        //console.log('점검상태(chckStts):', vehicleInfo.chckStts, '(' + getVehicleStatusText(vehicleInfo.chckStts) + ')');
        //console.log('차량종류(vhclType):', vehicleInfo.vhclType);
        //console.log(vehicleInfo)
        //console.log(vehicleInfo.regNo)
        //console.log('==========================');
        //기본정보
        $("#vhclInfoMdlInfo").text(vehicleInfo.mdlNm + ' ' + vehicleInfo.mdlYr);	///차량모델, 연식
        $("#vhclInfoRegNo").text(vhclMasked(vehicleInfo.regNo));	//차량번호
        $("#fuelNm").text(fuelNm[vehicleInfo.fuelCd]);	//차량연료
        $("#vhclInfoOprDstnc").text(formatNumber(vehicleInfo.oprDstnc)); //주행거리
        $("#vhclInfoFuelEconom").text(formatNumber(vehicleInfo.fuelEconom));	//평균연비
        $("#vhclInfoCb").text(formatNumber(vehicleInfo.cb));	//총 탄소배출
        $("#vhclInfoIdleTm").text(formatNumber(vehicleInfo.idleTm));	//총 공회전
        
        //진단정보
        let chckListHtml = ``;
		const chckList = JSON.parse(vehicleInfo.chckList).map(item => item["chck_nm"]);
		const uniqueChckList = [...new Set(chckList)];
		
		if (uniqueChckList.length > 0) {
		    const firstItem = uniqueChckList[0];
		    const remainingCount = uniqueChckList.length - 1;
		    const displayText = remainingCount > 0 
		        ? `${firstItem} 외 ${remainingCount}개` 
		        : firstItem;
		    chckListHtml = `<b class="status_item"><i class="fa-solid fa-triangle-exclamation"></i> ${displayText}</b>점검필요`;
		} else {
		    chckListHtml = " - ";
		}
		
		$("#bkdnCdListCnt").text(formatNumber(JSON.parse(vehicleInfo.bkdnCdList).length));
		$("#chckList").html(`${chckListHtml}`);
        
        //운행행태
        const dgrTypes = {
		    ovrSpdCnt: "과속",
		    sudDclrCnt: "급감속",
		    sudAcclCnt: "급가속",
		    sudStopCnt: "급정지",
		    sudStrtCnt: "급출발"
		};
		let dangers = "";
        // 0보다 큰 것만 텍스트로 추출
		let dgrList = Object.keys(dgrTypes)
		    .filter(key => vehicleInfo[key] > 0)
		    .map(key => dgrTypes[key]);
		
		// , 로 이어붙이기
		dangers = dgrList.join(", ") == "" ? "-" : dgrList.join(", ");
		let dgrListCnt = formatNumber(vehicleInfo.ovrSpdCnt + vehicleInfo.sudAcclCnt + vehicleInfo.sudDclrCnt + vehicleInfo.sudStopCnt + vehicleInfo.sudStrtCnt);
        $("#dgrListCnt").text(dgrListCnt == "" ? "0" : dgrListCnt);
        $("#dgrList").text(`${dangers}`);
    
    	let circle = getCircleByVhclSn(vhclSn);
		highlightCircle(circle);
        
        return vehicleInfo;
    } else {
        console.warn('차량 정보를 찾을 수 없습니다. vhclSn:', vhclSn);
        return null;
    }
}

function setMap() {
    am5.ready(function() {
        root = am5.Root.new("mapChartDiv");
        root.setThemes([am5themes_Animated.new(root)]);
        
        chart = root.container.children.push(
            am5map.MapChart.new(root, {
                projection: am5map.geoMercator(),
                width: am5.percent(100),
                height: am5.percent(100),
                x: am5.percent(0),
                y: am5.percent(0)
            })
        );
        
        var polygonSeries = chart.series.push(
            am5map.MapPolygonSeries.new(root, {
                fill: am5.color("#0f0f28"),
                fillOpacity: 0.5,
                geoJSON: am5geodata_gwngjuLow,
            })
        );

        chart.appear(1000, 100);
        
        // 지도 초기화 완료 후 차량 정보 로딩 및 주기적 업데이트 시작
        //console.log('지도 초기화 완료. 차량 정보 로딩 시작...');
        getVhclInfo(); // 최초 차량 정보 로딩
        //startVehicleInfoUpdate(); // 주기적 업데이트 시작
    });
}

// 차량 상태에 따른 색상 반환 함수
function getVehicleStatusColor(chckStts) {
    switch(chckStts) {
        case "0":
            return "#00B050"; // 초록색 - 정상
        case "1":
            return "#e4af0f"; // 주황색 - 점검
        case "2":
            return "#ef1515"; // 빨간색 - 고장
        default:
            return "#808080"; // 회색 - 알 수 없음
    }
}

// 차량 상태에 따른 텍스트 반환 함수
function getVehicleStatusText(chckStts) {
    switch(chckStts) {
        case "0":
            return "정상";
        case "1":
            return "점검";
        case "2":
            return "고장";
        default:
            return "알 수 없음";
    }
}

// 기존 차량 마커들을 제거하는 함수
function clearVehicleMarkers() {
    if (existingPointSeries.length > 0) {
        //console.log('기존 마커 제거 중...');
        existingPointSeries.forEach(series => {
            if (series && !series.isDisposed()) {
                chart.series.removeValue(series);
                series.dispose();
            }
        });
        existingPointSeries = [];
        //console.log('기존 마커 제거 완료');
    }
}

// 차량 위치를 지도에 표시하는 함수
function displayVehicleLocations() {
    if (!vhclLocationData || !vhclData) {
        //console.log('지도 또는 데이터가 준비되지 않았습니다.');
        return;
    }

    //console.log('차량 위치 업데이트 시작...');
    //console.log('차량 위치 데이터:', vhclLocationData);
    //console.log('차량 정보 데이터:', vhclData);

    // 기존 마커들 제거
    clearVehicleMarkers();
    
    // 기존 circle 초기화
    mapVhclCircles = [];

    // 상태별로 차량 데이터 그룹화
    const vehiclesByStatus = {
        "0": [], // 정상
        "1": [], // 점검
        "2": [], // 고장
        "other": [] // 기타
    };
    
    let existLatLngList = [];
	let noneLatLngList = [];
    // vhclLocationData와 vhclData 매칭하여 상태별로 분류
    vhclLocationData.forEach((location) => {
        //const vehicle = vhclData.data.mergedVehicleData.find(v => v.mtId === location.measurement);
        
        if (location.latitude && location.longitude) {
            const vehicleData = {
                geometry: {
                    type: "Point",
                    coordinates: [location.longitude, location.latitude]
                },
                vhclSn: location.vhclSn,
                regNo: location.regNo,
                statusText: getVehicleStatusText(location.chckStts),
                time: location.time,
                oprtrNm: location.oprtrNm,
                chckStts: location.chckStts
            };
			
            if (vehiclesByStatus[location.chckStts]) {
                vehiclesByStatus[location.chckStts].push(vehicleData);
            } else {
                vehiclesByStatus["other"].push(vehicleData);
            }
            existLatLngList.push(location)
        }else {
			noneLatLngList.push(location)
		}
    });
    
    // 각 상태별로 별도의 Point Series 생성
    Object.keys(vehiclesByStatus).forEach(status => {
        const vehicles = vehiclesByStatus[status];
        if (vehicles.length > 0) {
            const color = getVehicleStatusColor(status);
            
            const statusPointSeries = chart.series.push(
                am5map.MapPointSeries.new(root, {})
            );

            // 생성된 시리즈를 배열에 저장 (나중에 제거하기 위함)
            existingPointSeries.push(statusPointSeries);

            // 방법 2: 더 명시적으로 툴팁 비활성화
			statusPointSeries.bullets.push(function(root, series, dataItem) {
			    const oprtrNm = dataItem.dataContext.oprtrNm || "";
    			const isFirst = series.dataItems.indexOf(dataItem) === 0; // 첫 번째 circle 체크
			
			    var circleConfig = {
			        radius: 6,
			        fill: am5.color(color),
			        strokeWidth: 2,
			        cursorOverStyle: "pointer"
			    };
			
			    // oprtrNm이 있을 때만 tooltipText 추가
			    if (oprtrNm && oprtrNm.trim() !== "") {
			        circleConfig.tooltipText = oprtrMasked(oprtrNm);
			    }
			
			    var circle = am5.Circle.new(root, circleConfig);
			    
				mapVhclCircles.push(circle);
				
			    // 첫 번째 circle이면 바로 highlightCircle 호출
			    if (isFirst) {
			        highlightCircle(circle);
				    getVehicleInfoByVhclSn(dataItem.dataContext.vhclSn);
			    }
				
			    // 클릭 이벤트
			    circle.events.on("click", function(ev) {
			        stopvhclInfoAreaUpdate();
			        const dataItem = ev.target.dataItem;
			        if (dataItem && dataItem.dataContext) {
			            const vhclSn = dataItem.dataContext.vhclSn;
			            console.log('클릭한 마커 차량 일련번호:', vhclSn);
			            getVehicleInfoByVhclSn(vhclSn);
			            highlightCircle(ev.target);
		            }
			        
			    });
			
			    // oprtrNm이 있을 때만 커스텀 툴팁 스타일 적용
			    if (oprtrNm && oprtrNm.trim() !== "") {
			        var tooltip = am5.Tooltip.new(root, {
			            getFillFromSprite: false,
			            autoTextColor: false,
			            fill: am5.color("#ffffff"),
			            fillOpacity: 1,
			            stroke: am5.color("#333333"),
			            strokeWidth: 1,
			            strokeOpacity: 1,
			            pointerOrientation: "down",
			            paddingTop: 8,
			            paddingBottom: 8,
			            paddingLeft: 10,
			            paddingRight: 10
			        });
			
			        tooltip.label.setAll({
			            fill: am5.color("#333333"),
			            fontSize: 18,
			            fontWeight: "normal",
			            oversizedBehavior: "wrap"
			        });
			
			        tooltip.get("background").setAll({
			            fill: am5.color("#ffffff"),
			            fillOpacity: 1,
			            stroke: am5.color("#333333"),
			            strokeWidth: 1
			        });
			
			        circle.set("tooltip", tooltip);
			    }
			
			    return am5.Bullet.new(root, {
			        sprite: circle
			    });
			});
            
            statusPointSeries.data.setAll(vehicles);
        }
    });

    const totalVehicles = Object.values(vehiclesByStatus).reduce((sum, arr) => sum + arr.length, 0);
    console.log(`총 ${totalVehicles} / ${noneLatLngList.length + existLatLngList.length} 대, 위/경도가 누락 ${noneLatLngList.length}대`);
    console.log(`총 ${totalVehicles}대의 차량 마커 마커 업데이트 완료`);
}

function highlightCircle(circle) {
    if (!circle) return;

	// 모든 마커 초기화
    mapVhclCircles.forEach(c => c.setAll({
		radius: 6, 
		fillOpacity: 1, 
		fill: c.get("fill"),
		strokeWidth: 0,     // 테두리 제거
		strokeOpacity: 0    // 테두리 제거
	}));

	// 기존 애니메이션 초기화
    circle.setAll({ 
        radius: 6, 
        fillOpacity: 1,
        strokeWidth: 1,      // 강조 테두리 두께
        stroke: am5.color(0xffffff), // 강조 테두리 색상 (하얀색 예시)
        strokeOpacity: 1
    });

    // 반복 애니메이션 (radius 확장/축소)
    circle.animate({
        key: "radius",
        to: 12,
        duration: 500,
        easing: am5.ease.out(am5.ease.cubic),
        loops: Infinity,
        direction: "alternate"
    });

    // 선택 시 fill 색도 변경 가능
    circle.animate({
        key: "fill",
        duration: 500,
        loops: Infinity,
        direction: "alternate"
    });

    // 앞으로 가져오기
    if (circle.parent) {
        circle.parent.children.moveValue(circle, circle.parent.children.length - 1);
    }
}

function getCircleByVhclSn(vhclSn) {
    if (!mapVhclCircles || mapVhclCircles.length === 0) return null;

    return mapVhclCircles.find(circle => {
        return circle.dataItem &&
               circle.dataItem.dataContext &&
               circle.dataItem.dataContext.vhclSn === vhclSn;
    }) || null;
}

// GPS 좌표 NMEA 포맷을 십진도로 변환하는 함수
function convertNmeaToDecimal(nmeaCoord) {
    if (!nmeaCoord || nmeaCoord === 0) return null;
    const degrees = Math.floor(nmeaCoord / 100);
    const minutes = nmeaCoord % 100;
    return degrees + (minutes / 60);
}

// getVhclInfo 함수 - 차량 정보 및 위치 조회
function getVhclInfo(){
    var ownrNm = $("#ownrSelect").val();
    
    //console.log('차량 정보 업데이트 중... 시간:', new Date().toLocaleTimeString());
    
    customFetch({
        url: '/dashboard/getVhclInfo',
        method: 'POST',
        params: {
            ownrNm : ownrNm
        },
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        loadingIds: 'mapChartDiv|vhclInfoArea',
        onSuccess: (response) => {
            //console.log('getVhclInfo:', response)
            vhclData = mergeVehicleData(response);
            //console.log('차량 데이터 업데이트:', vhclData);
            
            storeVehicleDataGlobally(vhclData.data.mergedVehicleData);
            
            // vhclStts가 "1"인 차량들의 mtId 수집
            const activeMtIds = vhclData.data.vhclSttsInfo
                .filter(vehicle => vehicle.vhclStts === "1")
                .map(vehicle => vehicle.mtId);
            
            //console.log('운행중인 차량 mtId:', activeMtIds);
            
            if (activeMtIds.length > 0) {
                
                customFetchJson('/dashboard/getLatestGpsCoordinates', 
				    { activeMtIds: activeMtIds }, 
				    {
				        loadingIds: 'mapChartDiv|vhclInfoArea',
				        onSuccess: (gpsResponse) => {
				            //console.log('GPS 응답 업데이트:', gpsResponse);
				            
				            vhclLocationData = gpsResponse.data.list.map(item => ({
				                vhclSn: item.vhclSn,
				                regNo: item.regNo,
				                oprtrNm: item.oprtrNm,
				                chckStts: item.chckStts,
				                measurement: item.mtId,
				                time: item.mdfcnDt,
				                latitude: item.lat,
				                longitude: item.lng
				            }));
							//vehicleIndex = vhclLocationData[0].vhclSn;
				            //console.log('변환된 위치 데이터:', vhclLocationData);
				            startvhclInfoAreaUpdate()
				            displayVehicleLocations();
				            
				        },
				        onError: (error) => {
				            console.error('GPS 조회 에러:', error);
				        }
				    }
				);
            } else {
                console.log('운행 중인 차량이 없습니다.');
                vhclLocationData = [];
                displayVehicleLocations();
            }
        },
        onError: (error) => {
            console.error('차량 데이터 조회 에러:', error);
        }
    });
}

// 5초마다 차량 상세 정보(우측 하단) 업데이트하는 함수
function startvhclInfoAreaUpdate() {
    // 기존 인터벌이 있다면 제거
    if (vhclInfoAreaUpdateInterval) {
		vehicleIndex = 0;
        clearInterval(vhclInfoAreaUpdateInterval);
    }
    
    //console.log('차량 정보 주기적 업데이트 시작 (5초 간격)');
    
    //5초마다 차량 정보 업데이트
    vhclInfoAreaUpdateInterval = setInterval(function() {
        //console.log('--- 차량 상세 정보 주기적 업데이트 실행 ---');
		
        getVehicleInfoByVhclSn(vhclLocationData[vehicleIndex].vhclSn);
    
	    if(vehicleIndex < vhclLocationData.length - 1) {
	    	vehicleIndex++;
		}else {
			vehicleIndex = 0;
		}
        
    }, 1000*5); // 5초 = 50000ms
}

// 5초마다 차량 상세 정보(우측 하단) 업데이트하는 함수 중지 함수
function stopvhclInfoAreaUpdate() {
    if (vhclInfoAreaUpdateInterval) {
        clearInterval(vhclInfoAreaUpdateInterval);
        vhclInfoAreaUpdateInterval = null;
        //console.log('차량 정보 주기적 업데이트 중지');
    }
}

// 10초마다 차량 정보 업데이트하는 함수
function startVehicleInfoUpdate() {
    // 기존 인터벌이 있다면 제거
    if (vehicleUpdateInterval) {
        clearInterval(vehicleUpdateInterval);
    }
    
    //console.log('차량 정보 주기적 업데이트 시작 (10초 간격)');
    
    //10초마다 차량 정보 업데이트
    //vehicleUpdateInterval = setInterval(function() {
    //    console.log('--- 차량 정보 주기적 업데이트 실행 ---');
    //    //getVhclInfo();
    //}, 1000*60); // 10초 = 10000ms
}

// 차량 정보 업데이트 중지 함수
function stopVehicleInfoUpdate() {
    if (vehicleUpdateInterval) {
        clearInterval(vehicleUpdateInterval);
        vehicleUpdateInterval = null;
        //console.log('차량 정보 주기적 업데이트 중지');
    }
}

// 페이지를 떠날 때 인터벌 정리
window.addEventListener('beforeunload', function() {
    stopVehicleInfoUpdate();
});

// 차량 데이터 병합 함수
function mergeVehicleData(data) {
    const { vhclInfo, vhclSttsInfo, vhclOprInfo } = data.data;
    
    const mergedData = vhclInfo.map(vhclItem => {
        const vhclSn = vhclItem.vhclSn;
        
        const statusInfo = vhclSttsInfo.find(item => item.vhclSn === vhclSn) || {};
        const operationInfo = vhclOprInfo.find(item => item.vhclSn === vhclSn) || {};
        
        return {
            ...vhclItem,
            ...Object.fromEntries(
                Object.entries(statusInfo).filter(([key]) => key !== 'vhclSn')
            ),
            ...Object.fromEntries(
                Object.entries(operationInfo).filter(([key]) => key !== 'vhclSn')
            )
        };
    });
    
    return {
        ...data,
        data: {
            ...data.data,
            mergedVehicleData: mergedData
        }
    };
}