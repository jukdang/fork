/*
* 금일 운행현황 js
*/

/*
* 현재 운행 차량
*/
function setOprVhclCnt(){
    var ownrNm = $("#ownrSelect").val();
	
    customFetch({
        url: '/dashboard/getOprVhclCnt',
        method: 'POST',
        params: {
            ownrNm : ownrNm
        },
        loadingId : 'vhclCntLoadginArea',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        onSuccess: (response) => {
        	let data = response.data;
        	let vhclCnt = response.data.vhclCnt;
        	let vhclStatusCnt = response.data.vhclStatusCnt;
        	//console.log(data)
        	$("#totalVhclCnt").text(vhclCnt.totalCnt);
        	$("#oprVhclCnt").text(vhclCnt.oprCnt);
        	// vhclStatusCnt가 null인 경우 기본값 설정
			if (vhclStatusCnt === null || vhclStatusCnt === undefined) {
			   $("#totalOprDstnc").text('-');
			   $("#totalFuelEconom").text('-');
			   $("#totalCb").text('-');
			   $("#totalIdleTm").text('-');
			} else {
			   // 연비 계산 (km/m³)
			   const fuelEfficiency = (vhclStatusCnt.oprDstnc / vhclStatusCnt.fuelUsage).toFixed(2);
			   // 탄소배출량을 kg으로 변환 (g → kg)
			   const carbonEmissionKg = (vhclStatusCnt.cb / 1000).toFixed(2);
			   // 공회전시간을 시간으로 변환 (초 → 시간)
			   const idleTimeHours = (vhclStatusCnt.idleTm / 3600).toFixed(2);
			
			   $("#totalOprDstnc").text(formatNumber(vhclStatusCnt.oprDstnc));
			   $("#totalFuelEconom").text(formatNumber(fuelEfficiency));
			   $("#totalCb").text(formatNumber(carbonEmissionKg));
			   $("#totalIdleTm").text(formatNumber(idleTimeHours));
			}
        },
        onError: (error) => console.error('에러:', error)
    });
}