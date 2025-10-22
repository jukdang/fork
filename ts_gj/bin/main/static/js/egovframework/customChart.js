/*
- 운전습관 리포트

위험운행행태 현황
#e47f7a
#7086e2
#7cdbca
#fbcd7b
#b5c8f7
#efb0ed

막대그래프
레드 #e47f7a
블루 #7086e2

라인그래프
레드 #d3584e
블루 #5d71cd

- 통합 대시보드

운행현황추이
#EB6E65

서비스 이용현황
정비사 #FF928A
관리자 #75A0FE

*/

// 색상 배열
let colors = [
    am5.color("#FF6B6B"), // 빨강
    am5.color("#4ECDC4"), // 청록
    am5.color("#45B7D1"), // 파랑
    am5.color("#96CEB4"), // 초록
    am5.color("#FFEAA7"), // 노랑
    am5.color("#DDA0DD"), // 연보라
    am5.color("#98D8C8"), // 민트
    am5.color("#F7DC6F")  // 금색
];

let lineColors = [
    am5.color("#d3584e"),
    am5.color("#5d71cd"),
    am5.color("#45B7D1"), // 파랑
    am5.color("#96CEB4"), // 초록
    am5.color("#DDA0DD"), // 연보라
    am5.color("#98D8C8"), // 민트
    am5.color("#F7DC6F")  // 금색
];

let barColors = [
    am5.color("#e47f7a"),
    am5.color("#7086e2")
];

let pieColors = [
    am5.color("#e47f7a"),
    am5.color("#7086e2"),
    am5.color("#7cdbca"),
    am5.color("#fbcd7b"),
    am5.color("#b5c8f7"),
    am5.color("#efb0ed"),
];


function makeLineChart(id, data, xKey = 'time') {

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

    // 테마 설정
    root.setThemes([
        am5themes_Animated.new(root)
    ]);

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
                dataPoint[key] = parseFloat(item[key]) || null;
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

    // X축 라벨 회전 및 크기 설정
    xAxis.get("renderer").labels.template.setAll({
        fontSize: 12,
        fontFamily: "Pretendard"
    });

    let colorIndex = 0;
    let seriesArray = [];

    // 각 센서별로 Y축과 시리즈 생성
    seriesInfo.forEach(sensorKey => {
        // 현재 색상을 변수에 저장 (클로저 문제 해결)
        let currentColor = lineColors[colorIndex % lineColors.length];

        // 각 시리즈마다 별도의 Y축 생성
        let yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        // Y축 색상을 라인과 동일하게 설정 및 크기 설정
        yAxis.get("renderer").labels.template.setAll({
            fill: currentColor,
            fontSize: 12,
            fontFamily: "Pretendard"
        });

        // Y축 그리드 제거
        yAxis.get("renderer").grid.template.setAll({
            visible: false
        });

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

        // 라인 스타일 및 색상 설정
        series.strokes.template.setAll({
            strokeWidth: 1.5,
            stroke: currentColor
        });

        // 시리즈의 fill과 stroke 속성 설정 (범례 색상을 위해 필요)
        series.set("fill", currentColor);
        series.set("stroke", currentColor);

        seriesArray.push(series);
        colorIndex++;

        // 첫 번째 라벨 숨기기 (데이터 설정 후)
        yAxis.get("renderer").labels.template.adapters.add("visible", function (visible, target) {
            const dataItem = target.dataItem;
            if (dataItem && dataItem.get("value") === 0) {
                return false; // ✅ Y축 0 값 숨김
            }
            return visible;
        });
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

    // 애니메이션 시작
    seriesArray.forEach(series => {
        series.appear(1000);
    });

    let legend = chart.children.push(am5.Legend.new(root, {
        centerX: am5.p50,
        x: am5.p50,
    }));

    legend.data.setAll(chart.series.values);

    legend.labels.template.setAll({
        fontSize: 14,
        fontFamily: "Pretendard"
    });
    chart.appear(1000, 100);

    // 차트를 전역 변수에 저장
    globalChartInstances[id] = {
        chart: chart,
        xAxis: xAxis,
        root: root,
        processedData: processedData
    };

    // 차트 완성을 알리는 콜백 (선택적)
    return new Promise((resolve) => {
        // 애니메이션이 완료된 후 resolve
        setTimeout(() => {
            resolve({
                chart: chart,
                xAxis: xAxis,
                root: root,
                processedData: processedData
            });
        }, 1100); // 애니메이션 시간보다 약간 길게
    });
}

function makeVerticalBarChart(id, data, xKey = 'time') {

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

    // 테마 설정
    root.setThemes([
        am5themes_Animated.new(root)
    ]);

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

    // X축 라벨 회전 및 크기 설정
    xAxis.get("renderer").labels.template.setAll({
        fontSize: 12,
        fontFamily: "Pretendard"
    });

    xAxis.get("renderer").setAll({
        cellStartLocation: 0.3, // 기본은 0
        cellEndLocation: 0.7    // 기본은 1
    });

    let colorIndex = 0;
    let seriesArray = [];

    // 각 센서별로 Y축과 시리즈 생성
    seriesInfo.forEach(sensorKey => {
        // 현재 색상을 변수에 저장 (클로저 문제 해결)
        let currentColor = barColors[colorIndex % barColors.length];

        // 각 시리즈마다 별도의 Y축 생성
        let yAxis = chart.yAxes.push(am5xy.ValueAxis.new(root, {
            renderer: am5xy.AxisRendererY.new(root, {})
        }));

        // Y축 색상을 라인과 동일하게 설정 및 크기 설정
        yAxis.get("renderer").labels.template.setAll({
            fill: currentColor,
            fontSize: 12,
            fontFamily: "Pretendard"
        });

        // Y축 그리드 제거
        yAxis.get("renderer").grid.template.setAll({
            visible: false
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

        // 첫 번째 라벨 숨기기 (데이터 설정 후)
        yAxis.get("renderer").labels.template.adapters.add("visible", function (visible, target) {
            const dataItem = target.dataItem;
            if (dataItem && dataItem.get("value") === 0) {
                return false; // ✅ Y축 0 값 숨김
            }
            return visible;
        });

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

    // 애니메이션 시작
    seriesArray.forEach(series => {
        series.appear(1000);
    });

    let legend = chart.children.push(am5.Legend.new(root, {
        centerX: am5.p50,
        x: am5.p50,
    }));

    legend.data.setAll(chart.series.values);

    legend.labels.template.setAll({
        fontSize: 14,
        fontFamily: "Pretendard"
    });
    chart.appear(1000, 100);

    // 차트를 전역 변수에 저장
    globalChartInstances[id] = {
        chart: chart,
        xAxis: xAxis,
        root: root,
        processedData: processedData
    };

    // 차트 완성을 알리는 콜백 (선택적)
    return new Promise((resolve) => {
        // 애니메이션이 완료된 후 resolve
        setTimeout(() => {
            resolve({
                chart: chart,
                xAxis: xAxis,
                root: root,
                processedData: processedData
            });
        }, 1100); // 애니메이션 시간보다 약간 길게
    });
}

function makeHorizontalBarChart(id, data, xKey = 'time') {
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

    // 테마 설정
    root.setThemes([am5themes_Animated.new(root)]);

    // 차트 생성
    let chart = root.container.children.push(am5xy.XYChart.new(root, {
        panX: false,
        panY: false,
        paddingTop: 0,
        paddingLeft: 0,
        paddingRight: 0,
    }));

    // 데이터 처리 및 시리즈 정보 수집
    let processedData = [];
    let seriesInfo = new Set();

    data.forEach(item => {
        let dataPoint = { category: item[xKey] };
        Object.keys(item).forEach(key => {
            if (key !== xKey) {
                dataPoint[key] = parseFloat(item[key]) || 0;
                seriesInfo.add(key);
            }
        });
        processedData.push(dataPoint);
    });

    // Y축: 카테고리축 (가로 막대 기준)
    let yAxis = chart.yAxes.push(am5xy.CategoryAxis.new(root, {
        categoryField: "category",
        renderer: am5xy.AxisRendererY.new(root, {
            cellStartLocation: 0.3,
            cellEndLocation: 0.7,
            minGridDistance: 1
        }),
    }));

    // Y축 색상을 라인과 동일하게 설정 및 크기 설정
    yAxis.get("renderer").labels.template.setAll({
        fontSize: 12,
        fontFamily: "Pretendard"
    });

    yAxis.data.setAll(processedData);

    // X축: 값(ValueAxis)
    let xAxis = chart.xAxes.push(am5xy.ValueAxis.new(root, {
        renderer: am5xy.AxisRendererX.new(root, { minGridDistance: 50 }),
        min: 0
    }));

    // X축 라벨 회전 및 크기 설정
    xAxis.get("renderer").labels.template.setAll({
        fontSize: 12,
        fontFamily: "Pretendard"
    });

    // X축 그리드 제거
    xAxis.get("renderer").grid.template.setAll({
        visible: false
    });

    // 첫 번째 라벨 숨기기 (데이터 설정 후)
    xAxis.get("renderer").labels.template.adapters.add("visible", function (visible, target) {
        const dataItem = target.dataItem;
        if (dataItem && dataItem.get("value") === 0) {
            return false;
        }
        return visible;
    });

    let colorIndex = 0;
    let seriesArray = [];

    // 시리즈 생성
    seriesInfo.forEach(sensorKey => {
        let currentColor = barColors[colorIndex % barColors.length];

        let series = chart.series.push(am5xy.ColumnSeries.new(root, {
            name: sensorKey,
            xAxis: xAxis,           // 값이 X축
            yAxis: yAxis,           // 카테고리 Y축
            valueXField: sensorKey,
            categoryYField: "category",
            tooltip: am5.Tooltip.new(root, {
                labelText: "[bold]{category}[/]\n{name}: {valueX}"
            })
        }));

        series.columns.template.setAll({
            height: am5.percent(80), // 가로 막대 폭
            strokeOpacity: 0
        });

        series.set("fill", currentColor);
        series.set("stroke", currentColor);

        series.data.setAll(processedData);
        series.appear();

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
        behavior: "zoomY"
    }));
    cursor.lineX.set("visible", false);

    // 애니메이션 시작
    seriesArray.forEach(series => {
        series.appear(1000);
    });

    let legend = chart.children.push(am5.Legend.new(root, {
        centerX: am5.p50,
        x: am5.p50,
        y: am5.p100, // 하단으로 이동
        layout: root.horizontalLayout
    }));

    legend.markers.template.setAll({
        width: 12,   // 아이콘 가로
        height: 12   // 아이콘 세로
    });

    legend.data.setAll(chart.series.values);

    legend.labels.template.setAll({
        fontSize: 14,
        fontFamily: "Pretendard"
    });

    chart.appear(1000, 100);

    // 차트를 전역 변수에 저장
    globalChartInstances[id] = {
        chart: chart,
        xAxis: xAxis,
        root: root,
        processedData: processedData
    };

    // 차트 완성을 알리는 콜백 (선택적)
    return new Promise((resolve) => {
        // 애니메이션이 완료된 후 resolve
        setTimeout(() => {
            resolve({
                chart: chart,
                xAxis: xAxis,
                root: root,
                processedData: processedData
            });
        }, 1100); // 애니메이션 시간보다 약간 길게
    });
}

function pieChart(id, data, xKey = 'time') {
    // 기존 차트가 있으면 제거
    if (globalChartInstances[id]) {
        globalChartInstances[id].root.dispose();
        delete globalChartInstances[id];
    }

    // 기존 차트가 있으면 제거
    if (am5.registry.rootElements[id]) {
        am5.registry.rootElements[id].dispose();
    }

    // Create root element
    // https://www.amcharts.com/docs/v5/getting-started/#Root_element
    var root = am5.Root.new(id);

    // Set themes
    // https://www.amcharts.com/docs/v5/concepts/themes/
    root.setThemes([
        am5themes_Animated.new(root)
    ]);

    // Create chart
    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/
    var chart = root.container.children.push(am5percent.PieChart.new(root, {
        radius: am5.percent(90),
        innerRadius: am5.percent(50),
        layout: root.horizontalLayout
    }));

    // Create series
    // https://www.amcharts.com/docs/v5/charts/percent-charts/pie-chart/#Series
    var series = chart.series.push(am5percent.PieSeries.new(root, {
        valueField: "건수",
        categoryField: xKey,
        alignLabels: false
    }));

    series.labels.template.setAll({
        visible: false,
        textType: "circular",
        centerX: 0,
        centerY: 0,
        fontSize: 12,
        fontFamily: "Pretendard"
    });

    // 파이 조각 위의 텍스트 숨기기
    series.labels.template.set("visible", false);

    // 파이와 텍스트 연결선 숨기기
    series.ticks.template.set("visible", false);

    // 컬러 세팅
    series.get("colors").set("colors", pieColors.map(c => am5.color(c)));

    series.data.setAll(data);

    var legend = chart.children.push(am5.Legend.new(root, {
        centerY: am5.percent(50),
        y: am5.percent(50),
        layout: root.verticalLayout,
        paddingLeft: 20
    }));

    // set width and max width of labels
    legend.labels.template.setAll({
        maxWidth: 140,
        width: 140,
        fontSize: 12,
        oversizedBehavior: "wrap",
        text: "",
        fontFamily: "Pretendard"
    });

    // 1. valueLabels 숨기기 (퍼센트 제거)
    legend.valueLabels.template.setAll({
        visible: true,
        fontSize: 12,
        paddingLeft: 0
    });

    // 2. labels 텍스트 커스터마이징
    legend.labels.template.adapters.add("text", function (text, target) {
        const dataItem = target.dataItem;
        if (!dataItem) return text;

        const category = dataItem.get("category"); // xKey 값
        const value = dataItem.get("value");       // "건수" 값

        return category + " : " + value + " 건";   // 원하는 포맷
    });

    legend.data.setAll(series.dataItems);

    // Play initial series animation
    // https://www.amcharts.com/docs/v5/concepts/animations/#Animation_of_series
    series.appear(1000, 100);

    processedData = data;

    // 차트를 전역 변수에 저장
    globalChartInstances[id] = {
        chart: chart,
        root: root,
        processedData: processedData
    };

    // 차트 완성을 알리는 콜백 (선택적)
    return new Promise((resolve) => {
        // 애니메이션이 완료된 후 resolve
        setTimeout(() => {
            resolve({
                chart: chart,
                root: root,
                processedData: processedData
            });
        }, 1100); // 애니메이션 시간보다 약간 길게
    });

}

// 전역 변수로 차트 저장
let globalChartInstances = {};