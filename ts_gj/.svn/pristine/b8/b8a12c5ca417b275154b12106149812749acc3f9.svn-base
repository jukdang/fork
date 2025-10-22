package egovframework.theimc.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.egovframe.rte.psl.dataaccess.util.EgovMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonExcelDownload {
	
	/**
	 * 범용 Excel 파일 생성 메소드
	 */
	public void generateExcelDownload(List<EgovMap> dataList, 
	                                Map<String, String> columnMapping,
	                                Map<String, Integer> columnWidths,
	                                String sheetName,
	                                String fileName,
	                                HttpServletResponse response) throws IOException {
		
		ByteArrayOutputStream baos = null;
		OutputStream outputStream = null;
		Workbook workbook = null;
		
	    try {
	        // Excel 파일 생성
	        workbook = new XSSFWorkbook();
	        Sheet sheet = workbook.createSheet(sheetName);
	        
	        // 데이터 스타일 설정 (간단하게)
	        CellStyle dataStyle = workbook.createCellStyle();
	        dataStyle.setBorderTop(BorderStyle.THIN);
	        dataStyle.setBorderBottom(BorderStyle.THIN);
	        dataStyle.setBorderLeft(BorderStyle.THIN);
	        dataStyle.setBorderRight(BorderStyle.THIN);
	        dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	        dataStyle.setWrapText(true);
	        
	        // 헤더 생성
	        Row headerRow = sheet.createRow(0);
	        String[] columnKeys = columnMapping.keySet().toArray(new String[0]);
	        
	        for (int i = 0; i < columnKeys.length; i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columnMapping.get(columnKeys[i])); // 한글명으로 설정
	            cell.setCellStyle(dataStyle);
	        }
	        
	        // 데이터 입력
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	        ObjectMapper objectMapper = new ObjectMapper();
	        
	        if (dataList != null && !dataList.isEmpty()) {
	            for (int i = 0; i < dataList.size(); i++) {
	                Row row = sheet.createRow(i + 1);
	                EgovMap item = dataList.get(i);
	                
	                for (int j = 0; j < columnKeys.length; j++) {
	                    Cell cell = row.createCell(j);
	                    String columnKey = columnKeys[j];
	                    Object value = item.get(columnKey);
	                    
	                    if (value != null) {
	                        String cellValue = "";
	                        
	                        // 특별 처리가 필요한 컬럼들
	                        if ("ansCn".equals(columnKey)) {
	                            // JSON 파싱
	                            try {
	                                JsonNode answerNode = objectMapper.readTree(value.toString());
	                                if (answerNode.has("success") && answerNode.get("success").asBoolean()) {
	                                    cellValue = answerNode.has("answer") ? answerNode.get("answer").asText() : "";
	                                } else {
	                                    cellValue = answerNode.has("error") ? answerNode.get("error").asText() : "";
	                                }
	                                cellValue = cellValue.replaceAll("<[^>]*>", ""); // HTML 태그 제거
	                            } catch (IOException e) {
	                                cellValue = value.toString();
	                            } catch (Exception e) {
	                                cellValue = value.toString();
	                            }
	                        } else if ("evl".equals(columnKey)) {
	                            // 평가 컬럼
	                            String evl = value.toString();
	                            if ("U".equals(evl)) {
	                                cellValue = "만족";
	                            } else if ("D".equals(evl)) {
	                                cellValue = "불만족";
	                            }
	                        } else if ("regDt".equals(columnKey) || columnKey.endsWith("Dt")) {
	                            // 날짜 컬럼
	                            try {
	                                if (value instanceof Date) {
	                                    cellValue = dateFormat.format((Date) value);
	                                } else {
	                                    cellValue = value.toString();
	                                }
	                            } catch (IllegalArgumentException e) {
	                                cellValue = value.toString();
	                            } catch (Exception e) {
	                                cellValue = value.toString();
	                            }
	                        } else {
	                            // 일반 컬럼
	                            cellValue = value.toString();
	                        }
	                        
	                        cell.setCellValue(cellValue);
	                    }
	                    cell.setCellStyle(dataStyle);
	                }
	            }
	        }
	        
	        // 컬럼 너비 설정
	        for (int i = 0; i < columnKeys.length; i++) {
	            String columnKey = columnKeys[i];
	            if (columnWidths.containsKey(columnKey)) {
	                sheet.setColumnWidth(i, columnWidths.get(columnKey));
	            } else {
	                //sheet.autoSizeColumn(i);
	                int currentWidth = sheet.getColumnWidth(i);
	                sheet.setColumnWidth(i, Math.min(Math.max(currentWidth, 2000), 8000));
	            }
	        }
	        
	        // 파일명 설정
	        String fullFileName = fileName + "_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx";
	        String encodedFileName = URLEncoder.encode(fullFileName, "UTF-8").replaceAll("\\+", "%20");
	        
	        // 응답 헤더 설정
	        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
	        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
	        response.setHeader("Pragma", "no-cache");
	        response.setHeader("Expires", "0");
	        
	        // Excel 파일을 응답으로 전송
	        baos = new ByteArrayOutputStream();
	        workbook.write(baos);

	        byte[] excelBytes = baos.toByteArray();
	        response.setContentLength(excelBytes.length);

	        outputStream = response.getOutputStream();
	        outputStream.write(excelBytes);
	        outputStream.flush();
	        
	    } catch (ClientAbortException e) {
	        if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "ExcelDownload", "generateExcelDownload", "ClientAbortException", e);
            }
	    } catch (Exception e) {
	    	if (log.isErrorEnabled()) {
                log.error("[ERROR] {} {} {}: ", "ExcelDownload", "generateExcelDownload", "Exception", e);
            }
	        if (!response.isCommitted()) {
	            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        }
	        throw new IOException("Excel 생성 중 오류가 발생했습니다.", e);
	    } finally {
	        if (outputStream != null) {
	            try { outputStream.close(); } catch (IOException e) { log.error("[ERROR] {} {} {}: ", "ExcelDownload", "outputStream.close();", "IOException", e); }
	        }
	        if (baos != null) {
	            try { baos.close(); } catch (IOException e) { log.error("[ERROR] {} {} {}: ", "ExcelDownload", "baos.close();", "IOException", e); }
	        }
	        if (workbook != null) {
	            try { workbook.close(); } catch (IOException e) { log.error("[ERROR] {} {} {}: ", "ExcelDownload", "workbook.close();", "IOException", e); }
	        }
	    }
	}
}
