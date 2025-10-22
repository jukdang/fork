/*
* 운행현황 추이 js
*/

$(document).ready(function() {
    // 초기 설정 - 첫 번째만 보이게
    $('.graph_area > div[id^="oprStatusTrendChartDiv"]').removeClass('show');
    $('#oprStatusTrendChartDiv1').addClass('show');
    
    const chartTitles = [
        '주행거리<em>(km)</em>',
        '평균 연비<em>(km/m³)</em>',
        '총 탄소배출량<em>(kg)</em>',
        '총 공회전시간<em>(시)</em>'
    ];
    let currentChartIndex = 0;
    const chartDivs = ['oprStatusTrendChartDiv1', 'oprStatusTrendChartDiv2', 'oprStatusTrendChartDiv3', 'oprStatusTrendChartDiv4'];

    // 차트 전환 함수
    function switchChart() {
        // 현재 차트 페이드 아웃
        $('#' + chartDivs[currentChartIndex]).removeClass('show');
        
        // 다음 인덱스로 이동
        currentChartIndex = (currentChartIndex + 1) % chartDivs.length;
        
        // 제목 변경
        $('#oprStatusTrendTitle').html(chartTitles[currentChartIndex]);
        
        // 약간의 딜레이 후 다음 차트 페이드 인
        setTimeout(() => {
            $('#' + chartDivs[currentChartIndex]).addClass('show');
        }, 200);
    }

    // 화살표 클릭 이벤트
    $('.arrow_btn').on('click', function(e) {
        e.preventDefault();
        oprStatusTrendRollingFlag = false;

        // 롤링 중지
        if (oprStatusTrendInterval) {
            clearInterval(oprStatusTrendInterval);
            oprStatusTrendInterval = null;
        }

        switchChart();
    });

    if (oprStatusTrendRollingFlag == true) {
        // 5초마다 자동 전환
        oprStatusTrendInterval = setInterval(switchChart, 5000);
    }
});

/*
* 운행현황 추이
*/
function setOprStatusTrend(){
    var ownrNm = $("#ownrSelect").val();
	
    customFetch({
        url: '/dashboard/getOprStatusTrend',
        method: 'POST',
        params: {
            ownrNm : ownrNm
        },
        loadingId : 'oprStatusTrendArea',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        onSuccess: (response) => {
        	let data = response.data.list;
        	makeCustomVerticalBarChart("oprStatusTrendChartDiv1", data.map(item => ({anlsDt: item.anlsDt,  '총 주행거리(km)': item.oprDstnc})), 'anlsDt');
        	makeCustomVerticalBarChart("oprStatusTrendChartDiv2", data.map(item => ({anlsDt: item.anlsDt,  '평균 연비(km)': item.fuelEconom})), 'anlsDt');
        	makeCustomVerticalBarChart("oprStatusTrendChartDiv3", data.map(item => ({anlsDt: item.anlsDt,  '총 탄소배출량(kg)': item.cb})), 'anlsDt');
        	makeCustomVerticalBarChart("oprStatusTrendChartDiv4", data.map(item => ({anlsDt: item.anlsDt,  '총 공회전시간(시)': item.idleTm})), 'anlsDt');
        },
        onError: (error) => console.error('에러:', error)
    });
}



// 전역 변수로 차트 저장
let globalChartInstances = {};
let colors = [
    am5.color("#EB6E65"), // 노랑
    am5.color("#FF6B6B"), // 빨강
    am5.color("#4ECDC4"), // 청록
    am5.color("#45B7D1"), // 파랑
    am5.color("#96CEB4"), // 초록
    am5.color("#DDA0DD"), // 연보라
    am5.color("#98D8C8"), // 민트
    am5.color("#F7DC6F")  // 금색
];


function makeCustomVerticalBarChart(id, data, xKey = 'time') {
    
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

    // 각 센서별로 Y축과 시리즈 생성
    seriesInfo.forEach(sensorKey => {
        // 현재 색상을 변수에 저장 (클로저 문제 해결)
        let currentColor = colors[colorIndex % colors.length];
        
        // 각 시리즈마다 별도의 Y축 생성
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

        let series = chart.series.push(am5xy.ColumnSeries.new(root, {
            name: sensorKey,
            xAxis: xAxis,
            yAxis: yAxis,
            valueYField: sensorKey,
            categoryXField: "category",
            tooltip: am5.Tooltip.new(root, {
                labelText: "{name}: {valueY}"
            })
        }));
        
        series.columns.template.setAll({
            width: am5.percent(80),
        });

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