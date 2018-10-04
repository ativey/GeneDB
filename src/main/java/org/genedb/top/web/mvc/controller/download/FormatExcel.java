package org.genedb.top.web.mvc.controller.download;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.genedb.top.querying.tmpquery.GeneDetail;

/**
 * @author gv1
 */
public class FormatExcel extends FormatBase {
	
	private static Logger logger = LoggerFactory.getLogger(FormatExcel.class);
	
	HSSFWorkbook workbook;
	HSSFSheet sheet;
	int rcount = 0;
	OutputStream outputStream;
	
	public FormatExcel() {
		super();		
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet();
	}
	
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
	@Override
	public void formatBody(List<GeneDetail> entries) throws IOException {
		logger.info("excel export");
		for (GeneDetail entry : entries) {
			
			HSSFRow row = sheet.createRow(rcount);
			
			short count = 0;
			for (String fieldValue : getFieldValues(getExtractor(entry), outputOptions)) {
				
				HSSFCell cell = row.createCell(count);
				HSSFRichTextString richVal = new HSSFRichTextString(fieldValue);
				cell.setCellValue(richVal);
				
				count++;
				
			}
			
			rcount++;
		}
		
	}

	@Override
	public void formatFooter() throws IOException {
		workbook.write(outputStream);
	}
	
	@Override
	public void formatHeader() throws IOException {
		
		HSSFCellStyle style = workbook.createCellStyle();
		HSSFFont font = workbook.createFont();
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);
		
		HSSFRow heading = sheet.createRow(rcount);
		short count = 0;
		for (OutputOption outputOption : outputOptions) {
			
			HSSFCell cell = heading.createCell(count);
			cell.setCellStyle(style);
			
			String value = outputOption.name();
			HSSFRichTextString richVal = new HSSFRichTextString(value);
			cell.setCellValue(richVal);
			count++;
		}
		rcount++;

	}

}
