package com.dt.core.dao;

import org.apache.poi.hssf.usermodel.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

public class RcdSetExporter {
    private static final String ENCODING = "UTF-8";
    private static final String ROWS_TAG = "rows";
    private static final String ROW_TAG = "row";
    private static final String TOTAL_TAG = "totals";
    private static final String INDEX_TAG = "__i__";

    private RcdSet set = null;

    public RcdSetExporter(RcdSet set) {
        /*
         * if (ENCODING == null) { ENCODING = ((AppContext)
         * SpringUtil.getSringContext().getBean(
         * BeanNames.AppContext)).getCharacterEncoding(); }
         */
        this.set = set;
    }

    public String asExtGridXMLString() {
        return asExtGridXML().asXML();
    }

    public String asExtGridXMLString(boolean withIndex) {
        return asExtGridXML(withIndex).asXML();
    }

    public Document asExtGridXML() {
        return asExtGridXML(-1);
    }

    public Document asExtGridXML(boolean withIndex) {
        return asExtGridXML(withIndex ? 1 : -1);
    }

    public String asExtGridXMLString(int beginIndex) {
        return asExtGridXML(beginIndex).asXML();
    }

    public Document asExtGridXML(int beginIndex) {
        boolean idxFlag = beginIndex > -1;

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding(ENCODING);
        Element root = document.addElement(ROWS_TAG);
        Element totals = root.addElement(TOTAL_TAG);

        int total = set.getTotalRowCount();

        if (total == -1)
            total = set.size();

        totals.addCDATA(total + "");

        String[] names = set.getMetaData().getColumnLabels();


        for (Rcd r : set) {
            Element row = root.addElement(ROW_TAG);

            if (idxFlag) {
                Element cell = row.addElement(INDEX_TAG);
                cell.addCDATA(beginIndex + "");
                beginIndex++;
            }

            for (String name : names) {
                Object value = r.getValue(name);
                if (value == null)
                    value = "";
                Element cell = row.addElement(name.toUpperCase());
                cell.addCDATA(value.toString());
            }
        }

        return document;
    }

    public Document asXML(String rootNodeName, String rowNodeName) {

        Document document = DocumentHelper.createDocument();
        document.setXMLEncoding(ENCODING);
        Element root = document.addElement(rootNodeName);
        String[] names = set.getMetaData().getColumnLabels();
        for (Rcd r : set) {
            Element row = root.addElement(ROW_TAG);
            for (String name : names) {
                Object value = r.getValue(name);
                if (value == null)
                    value = "";
                Element cell = row.addElement(name.toUpperCase());
                cell.addCDATA(value.toString());
            }
        }
        return document;
    }

    public Document asXML() {
        return asXML("data", "row");
    }

    public HSSFWorkbook asExcel(String sheetName, HashMap<String, String> columnsMap, boolean notIncludeMap) {
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet(sheetName);

        HSSFRow headRow = sheet.createRow(0);
        String[] colNames = this.set.getMetaData().getColumnLabels();
        int col = 0;
        for (short i = 0; i < colNames.length; i++) {
            HSSFCell cell = headRow.createCell(col);
            String text = colNames[i];

            if (notIncludeMap && columnsMap.get(text) == null) {
                continue;
            }

            if (columnsMap.get(text) != null) {
                text = columnsMap.get(text);
            }
            cell.setCellValue(new HSSFRichTextString(text));
            col++;
        }

        short rowIdx = 1;
        for (Rcd r : set) {
            col = 0;
            HSSFRow row = sheet.createRow(rowIdx);
            for (short i = 0; i < colNames.length; i++) {
                if (notIncludeMap && columnsMap.get(colNames[i]) == null) {
                    continue;
                }

                HSSFCell cell = row.createCell(col);
                setExcelCellValue(wb, r.getValue(colNames[i]), cell);
                col++;
            }
            rowIdx++;
        }

        return wb;
    }

    public HSSFWorkbook asExcel(String sheetName, String... columnsMap) {

        HashMap<String, String> colMap = new HashMap<String, String>();
        for (int i = 0; i < columnsMap.length; i++) {
            String key = columnsMap[i];
            i++;
            String value = null;
            if (i < columnsMap.length) {
                value = columnsMap[i];
            }
            colMap.put(key, value);
        }
        return asExcel(sheetName, colMap, false);

    }

    private void setExcelCellValue(HSSFWorkbook wb, Object value, HSSFCell cell) {
        if (value == null)
            return;
//		Class cls = value.getClass();
//		if (cls.equals(Boolean.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_BOOLEAN);
//			cell.setCellValue((Boolean) value);
//		} else if (cls.equals(Byte.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue((Byte) value);
//		} else if (cls.equals(Short.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue((Short) value);
//		} else if (cls.equals(Integer.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue((Integer) value);
//		} else if (cls.equals(Long.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue((Long) value);
//		} else if (cls.equals(Float.class)) {
//			cell.setCellValue((Float) value);
//		} else if (cls.equals(BigDecimal.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue(((BigDecimal) value).doubleValue());
//		} else if (cls.equals(BigInteger.class)) {
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			cell.setCellValue(((BigInteger) value).longValue());
//		} else if (cls.equals(java.util.Date.class)) {
//			HSSFCellStyle dateCellStyle = wb.createCellStyle();
//			short df = wb.createDataFormat().getFormat("yyyy-mm-dd hh:mm");
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			dateCellStyle.setDataFormat(df);
//			cell.setCellStyle(dateCellStyle);
//
//			cell.setCellValue((java.util.Date) value);
//		} else if (cls.equals(java.sql.Date.class)) {
//			HSSFCellStyle dateCellStyle = wb.createCellStyle();
//			short df = wb.createDataFormat().getFormat("yyyy-mm-dd hh:mm");
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			dateCellStyle.setDataFormat(df);
//			cell.setCellStyle(dateCellStyle);
//			cell.setCellValue((java.sql.Date) value);
//		} else if (cls.equals(java.sql.Timestamp.class)) {
//			HSSFCellStyle dateCellStyle = wb.createCellStyle();
//			short df = wb.createDataFormat().getFormat("yyyy-mm-dd hh:mm");
//			cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
//			dateCellStyle.setDataFormat(df);
//			cell.setCellStyle(dateCellStyle);
//			cell.setCellValue((java.sql.Timestamp) value);
//		} else {
//			cell.setCellValue(new HSSFRichTextString(value.toString()));
//		}
    }

    public void asExcel(HttpServletResponse response, String fileName, String sheetName,
                        HashMap<String, String> fieldMap) {
        try {

            HSSFWorkbook wb = asExcel(sheetName, fieldMap, false);
            response.setCharacterEncoding("GBK");
            response.setContentType("application/x-msdownload;charset=GB2312");
            response.setHeader("Content-Disposition",
                    "attachment;filename=" + new String(fileName.getBytes("GBK"), "iso8859-1") + ".xls");
            wb.write(response.getOutputStream());
            response.getOutputStream().flush();
            response.getOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
