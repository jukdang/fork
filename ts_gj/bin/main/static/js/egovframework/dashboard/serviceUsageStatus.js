//대시보드 - 서비스 이용현황
function getServiceUsageStatusByUsers() {
	
	const startDate = dayjs().subtract(6, "day").format("YYYY-MM-DD");
	const endDate = dayjs().format("YYYY-MM-DD");
	const ownrNm = $("#ownrSelect").val();
	const params = {
		startDate: startDate,
		endDate: endDate,
		ownrNm: ownrNm
    };
	
	customFetch({
        url: '/dashboard/getServiceUsageStatusByUsers',
        method: 'POST',
        params: params,
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        loadingId: 'serviceUsageStatusArea',
        onSuccess: (response) => {
            //console.log('getServiceUsageStatusByUsers:', response)
            let data = response.data.list;
        	makeCustomVerticalLineChart("serviceUsageStatusChartDiv", data, 'date');
        	let todayData = data.filter(item => item.date == dayjs().format("MM/DD"))[0];
        	$("#adminUseCnt").text(todayData["관리자"])
        	$("#mechanicUseCnt").text(todayData["정비사"])
        },
        onError: (error) => {
            console.error('차량 데이터 조회 에러:', error);
        }
    });
}

function makeCustomVerticalLineChart(id, data, xKey = 'time') {
    
    // 기존 차트가 있으면 제거
    if (globalChartInstances[id]) {
        globalChartInstances[id].root.dispose();
        delete globalChartInstances[id];
    }
    
    // 추가 안전장치: registry에서도 확인
    if (am5.registry.rootElements[id]) {
        am5.registry.rootElements[id].dispose();
    }
    // Root 생성
    let root = am5.Root.new(id);

    let chart = root.container.children.push(am5xy.XYChart.new(root, {
        panX: false,
        panY: false,
        paddingLeft: 0,
        layout: root.verticalLayout
    }));

    // 데이터 변환 및 시리즈 정보 수집
    let processedData = [];
    let seriesInfo = new Set();

    data.forEach(item => {
        let dataPoint = { category: item[xKey] };

        // time을 제외한 나머지 속성들을 시리즈로 추가
        Object.keys(item).forEach(key => {
            if (key !== xKey) {
                dataPoint[key] = parseFloat(item[key]) || 0;
                seriesInfo.add(key);
            }
        });

        processedData.push(dataPoint);
    });

    // X축 (카테고리축) 생성
    let xAxis = chart.xAxes.push(am5xy.CategoryAxis.new(root, {
        categoryField: "category",
        renderer: am5xy.AxisRendererX.new(root, {
            minGridDistance: 50
        }),
        tooltip: am5.Tooltip.new(root, {})
    }));

    // X축 라벨 흰색, 폰트 크기 증가
    xAxis.get("renderer").labels.template.setAll({
        fontSize: 14,
        fill: am5.color("#FFFFFF"),
		fontFamily: "Pretendard",
        paddingTop: 8
    });

    xAxis.get("renderer").setAll({
        cellStartLocation: 0.3, // 기본은 0
        cellEndLocation: 0.7    // 기본은 1
    });

	xAxis.get("renderer").grid.template.setAll({
        visible: false,
    });

    let colorIndex = 0;
    let seriesArray = [];

	let yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
        renderer: am5xy.AxisRendererY.new(root, {})
    }));

    // Y축 라벨 흰색, 폰트 크기 증가
    yAxis.get("renderer").labels.template.setAll({
        fill: am5.color("#FFFFFF"),
		fontFamily: "Pretendard",
        fontSize: 14
    });
    
    // Y축 그리드 제거
    yAxis.get("renderer").grid.template.setAll({
        visible: true,
        stroke: am5.color("#FFFFFF")
    });

	//전역 컬러 재정의   [정비사 #FF928A   관리자 #75A0FE]
	let colors = [
	    am5.color("#FF928A"),
	    am5.color("#75A0FE")
	];

    // 각 센서별로 Y축과 시리즈 생성
    seriesInfo.forEach(sensorKey => {
        // 현재 색상을 변수에 저장 (클로저 문제 해결)
        let currentColor = colors[colorIndex % colors.length];
        
        // 각 시리즈마다 별도의 Y축 생성
        /*let yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        // Y축 라벨 흰색, 폰트 크기 증가
        yAxis.get("renderer").labels.template.setAll({
            fill: am5.color("#FFFFFF"),
            fontSize: 14
        });
        
        // Y축 그리드 제거
        yAxis.get("renderer").grid.template.setAll({
            visible: true,
            stroke: am5.color("#FFFFFF")
        });*/

        let series = chart.series.push(am5xy.LineSeries.new(root, {
            name: sensorKey,
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: sensorKey,
            categoryXField: "category",
            tooltip: am5.Tooltip.new(root, {
                labelText: "{name}: {valueY}"
            })
        }));
        
        // 시리즈의 fill과 stroke 속성 설정 (범례 색상을 위해 필요)
        series.set("fill", currentColor);
        series.set("stroke", currentColor);

        seriesArray.push(series);
        colorIndex++;
        
    });

    // X축에 데이터 설정
    xAxis.data.setAll(processedData);

    // 각 시리즈에 데이터 설정
    seriesArray.forEach(series => {
        series.data.setAll(processedData);
    });

    // 커서 추가
    let cursor = chart.set("cursor", am5xy.XYCursor.new(root, {
        behavior: "zoomX"
    }));
    cursor.lineY.set("visible", false);

    // 차트를 전역 변수에 저장
    globalChartInstances[id] = {
        chart: chart,
        xAxis: xAxis,
        root: root,
        processedData: processedData
    };
}