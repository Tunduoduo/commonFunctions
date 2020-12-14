package com.example.functions.util;

import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.Number;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

/**
 * excel export
 * @author TIAN
 *
 */
public class ExcelUtil {
	private  static Logger log = LoggerFactory.getLogger(ExcelUtil.class);
	public ExcelUtil() {
	}

	@SuppressWarnings("rawtypes")
	public static void ersList2Excel(final List<Map<String, Object>> list,
			final String title, final LinkedHashMap headers,
			final String fileName,HttpServletResponse response) {
		Date start= new Date() ;
		System.err.println("工具导出报 START"+start.getTime());
        if (list != null && list.size() == 0) {
			return;
		}
		try {
			OutputStream os = response
					.getOutputStream();
			response.setHeader(
					"Content-disposition",
					(new StringBuilder("attachment; filename="))
							.append(new String(fileName.getBytes("utf-8"),
									"iso-8859-1")).append(".xls").toString());
			response.setContentType(
					"application/msexcel");
			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet sheet = book.createSheet(title, 0);
			WritableCellFormat bigGreyBackground = setFmt();
			WritableCellFormat contentStyle = null;
			WritableCellFormat smallBackground = setTHd();
			sheet.setRowView(0, 800);
			Label label = new Label(0, 0, title, bigGreyBackground);
			sheet.addCell(label);
			sheet.addCell(new Label(0, 1, (new StringBuilder("导出日期:")).append(
					(new SimpleDateFormat("yyyy年MM月dd日")).format(new Date()))
					.toString(), timeStyle()));
			sheet.mergeCells(0, 0, headers.size() - 1, 0);
			sheet.mergeCells(0, 1, headers.size() - 1, 1);

			Iterator it = headers.entrySet().iterator();
			for (int headColum = 0; it.hasNext(); headColum++) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				sheet.setColumnView(headColum, 19);
				addSheet(sheet, entry.getValue(), smallBackground, label,
						headColum, 2);
			}
			jxl.write.NumberFormat formatMoney = new jxl.write.NumberFormat(
					"#,##0.00");
			WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
			for (int i = 0; i < list.size(); i++) {// 添加内容
				Map<String, Object> obj = list.get(i);
				if (obj != null && !"".equals(obj)) {
					Iterator its = headers.entrySet().iterator();
					for (int contentCol = 0; its.hasNext(); contentCol++) {
						String classify = "";// 分类如 Number ,
						java.util.Map.Entry entry = (java.util.Map.Entry) its
								.next();
						String key = (new StringBuilder()).append(
								entry.getKey()).toString();
						String vals[] = key.split("'-->'");
						if (vals.length > 2 && vals[2].equals("Number")) {
							classify = vals[2];
							contentStyle = new jxl.write.WritableCellFormat(
									formatMoney);
							contentStyle.setFont(wfc);
							contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
							contentStyle.setBorder(Border.ALL,
									BorderLineStyle.THIN, Colour.BLACK);
						} else if (vals.length == 2) {
							if (vals[1].equalsIgnoreCase("right")) {
								contentStyle = setlable(2);
							} else if (vals[1].equalsIgnoreCase("center")) {
								contentStyle = setlable(1);
							} else {
								contentStyle = setlable(99);
							}
						} else {
							contentStyle = setlable(99);
						}
						addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
								label, contentCol, i + 3, classify);
					}
				}

			}
			book.write();
			book.close();
			os.flush();
			os.close();
		} catch (Exception e) {
            log.error("工具导出报表异常"+e.getMessage());
			e.printStackTrace();
		}finally {
            Date end= new Date() ;
            log.info("工具导出报END"+end.getTime()+"subtractionTime "+(end.getTime()-start.getTime()));

        }


		return;
	}

	@SuppressWarnings("rawtypes")
	public static void ersList2Excel(final List<Map<String, Object>> list,
									 final String title, final LinkedHashMap headers,
									 final String fileName,String path) {
		Date start= new Date() ;
		log.info("工具导出报 START"+start.getTime());
		if (list != null && list.size() == 0) {
			return;
		}
		try {
			FileOutputStream os = new FileOutputStream(path);
			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet sheet = book.createSheet(title, 0);
			WritableCellFormat bigGreyBackground = setFmt();
			WritableCellFormat contentStyle = null;
			WritableCellFormat smallBackground = setTHd();
			sheet.setRowView(0, 800);
			Label label = new Label(0, 0, title, bigGreyBackground);
			sheet.addCell(label);
			sheet.addCell(new Label(0, 1, (new StringBuilder("导出日期:")).append(
					(new SimpleDateFormat("yyyy年MM月dd日")).format(new Date()))
					.toString(), timeStyle()));
			sheet.mergeCells(0, 0, headers.size() - 1, 0);
			sheet.mergeCells(0, 1, headers.size() - 1, 1);

			Iterator it = headers.entrySet().iterator();
			for (int headColum = 0; it.hasNext(); headColum++) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				sheet.setColumnView(headColum, 19);
				addSheet(sheet, entry.getValue(), smallBackground, label,
						headColum, 2);
			}
			jxl.write.NumberFormat formatMoney = new jxl.write.NumberFormat(
					"#,##0.00");
			WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
			for (int i = 0; i < list.size(); i++) {// 添加内容
				Map<String, Object> obj = list.get(i);
				if (obj != null && !"".equals(obj)) {
					Iterator its = headers.entrySet().iterator();
					for (int contentCol = 0; its.hasNext(); contentCol++) {
						String classify = "";// 分类如 Number ,
						java.util.Map.Entry entry = (java.util.Map.Entry) its
								.next();
						String key = (new StringBuilder()).append(
								entry.getKey()).toString();
						String vals[] = key.split("'-->'");
						if (vals.length > 2 && vals[2].equals("Number")) {
							classify = vals[2];
							contentStyle = new jxl.write.WritableCellFormat(
									formatMoney);
							contentStyle.setFont(wfc);
							contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
							contentStyle.setBorder(Border.ALL,
									BorderLineStyle.THIN, Colour.BLACK);
						} else if (vals.length == 2) {
							if (vals[1].equalsIgnoreCase("right")) {
								contentStyle = setlable(2);
							} else if (vals[1].equalsIgnoreCase("center")) {
								contentStyle = setlable(1);
							} else {
								contentStyle = setlable(99);
							}
						} else {
							contentStyle = setlable(99);
						}
						addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
								label, contentCol, i + 3, classify);
					}
				}

			}
			book.write();
			book.close();
			os.flush();
			os.close();
		} catch (Exception e) {
			log.error("工具导出报表异常"+e.getMessage());
			e.printStackTrace();
		}finally {
			Date end= new Date() ;
			log.info("工具导出报END"+end.getTime()+"subtractionTime "+(end.getTime()-start.getTime()));

		}


		return;
	}

	@SuppressWarnings("rawtypes")
	public static void ersList2ExcelMap(final List<Map<String, Object>> list,
			final String title, final LinkedHashMap headers,
			final String fileName,HttpServletResponse response) {
		if (list != null && list.size() == 0) {
			return;
		}
		try {
			OutputStream os = response
					.getOutputStream();
			response.setHeader(
					"Content-disposition",
					(new StringBuilder("attachment; filename="))
							.append(new String(fileName.getBytes("utf-8"),
									"iso-8859-1")).append(".xls").toString());
			response.setContentType(
					"application/msexcel");
			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet sheet = book.createSheet(title, 0);
			WritableCellFormat bigGreyBackground = setFmt();
			WritableCellFormat contentStyle = null;
			WritableCellFormat smallBackground = setTHd();
			sheet.setRowView(0, 800);
			Label label = new Label(0, 0, title, bigGreyBackground);
			sheet.addCell(label);
			sheet.addCell(new Label(0, 1, (new StringBuilder("导出日期:")).append(
					(new SimpleDateFormat("yyyy年MM月dd日")).format(new Date()))
					.toString(), timeStyle()));
			sheet.mergeCells(0, 0, headers.size() - 1, 0);
			sheet.mergeCells(0, 1, headers.size() - 1, 1);

			Iterator it = headers.entrySet().iterator();
			for (int headColum = 0; it.hasNext(); headColum++) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				sheet.setColumnView(headColum, 19);
				addSheet(sheet, entry.getValue(), smallBackground, label,
						headColum, 2);
			}
			jxl.write.NumberFormat formatMoney = new jxl.write.NumberFormat(
					"#,##0.00");
			WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
			for (int i = 0; i < list.size(); i++) {// 添加内容
				Map<String, Object> obj = list.get(i);
				if (obj != null && !"".equals(obj)) {
					Iterator its = headers.entrySet().iterator();
					for (int contentCol = 0; its.hasNext(); contentCol++) {
						String classify = "";// 分类如 Number ,
						java.util.Map.Entry entry = (java.util.Map.Entry) its
								.next();
						String key = (new StringBuilder()).append(
								entry.getKey()).toString();
						String vals[] = key.split("'-->'");
						if (vals.length > 2 && vals[2].equals("Number")) {
							classify = vals[2];
							contentStyle = new jxl.write.WritableCellFormat(
									formatMoney);
							contentStyle.setFont(wfc);
							contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
							contentStyle.setBorder(Border.ALL,
									BorderLineStyle.THIN, Colour.BLACK);
						} else if (vals.length == 2) {
							if (vals[1].equalsIgnoreCase("right")) {
								contentStyle = setlable(2);
							} else if (vals[1].equalsIgnoreCase("center")) {
								contentStyle = setlable(1);
							} else {
								contentStyle = setlable(99);
							}
						} else {
							contentStyle = setlable(99);
						}
						addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
								label, contentCol, i + 3, classify);
					}
				}

			}

			book.write();
			os.flush();
			book.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}



	@SuppressWarnings("rawtypes")
	public static void ersList2ExcelLinkedHashMap(final List<LinkedHashMap<String, Object>> list,
			final String title, final LinkedHashMap headers,
			final String fileName,HttpServletResponse response) {
		if (list != null && list.size() == 0) {
			return;
		}
		try {
			OutputStream os = response
					.getOutputStream();
			response.setHeader(
					"Content-disposition",
					(new StringBuilder("attachment; filename="))
							.append(new String(fileName.getBytes("utf-8"),
									"iso-8859-1")).append(".xls").toString());
			response.setContentType(
					"application/msexcel");

			WritableWorkbook book = Workbook.createWorkbook(os);
			WritableSheet sheet = book.createSheet(title, 0);
			WritableCellFormat bigGreyBackground = setFmt();
			WritableCellFormat contentStyle = null;
			WritableCellFormat smallBackground = setTHd();
			sheet.setRowView(0, 800);
			Label label = new Label(0, 0, title, bigGreyBackground);
			sheet.addCell(label);
			sheet.addCell(new Label(0, 1, (new StringBuilder("导出日期:")).append(
					(new SimpleDateFormat("yyyy年MM月dd日")).format(new Date()))
					.toString(), timeStyle()));
			sheet.mergeCells(0, 0, headers.size() - 1, 0);
			sheet.mergeCells(0, 1, headers.size() - 1, 1);

			Iterator it = headers.entrySet().iterator();
			for (int headColum = 0; it.hasNext(); headColum++) {
				java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
				sheet.setColumnView(headColum, 19);
				addSheet(sheet, entry.getValue(), smallBackground, label,
						headColum, 2);
			}
			jxl.write.NumberFormat formatMoney = new jxl.write.NumberFormat(
					"#,##0.00");
			WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
					WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
			for (int i = 0; i < list.size(); i++) {// 添加内容
				Map<String, Object> obj = list.get(i);
				if (obj != null && !"".equals(obj)) {
					Iterator its = headers.entrySet().iterator();
					for (int contentCol = 0; its.hasNext(); contentCol++) {
						String classify = "";// 分类如 Number ,
						java.util.Map.Entry entry = (java.util.Map.Entry) its
								.next();
						String key = (new StringBuilder()).append(
								entry.getKey()).toString();
						String vals[] = key.split("'-->'");
						if (vals.length > 2 && vals[2].equals("Number")) {
							classify = vals[2];
							contentStyle = new jxl.write.WritableCellFormat(
									formatMoney);
							contentStyle.setFont(wfc);
							contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
							contentStyle.setBorder(Border.ALL,
									BorderLineStyle.THIN, Colour.BLACK);
						} else if (vals.length == 2) {
							if (vals[1].equalsIgnoreCase("right")) {
								contentStyle = setlable(2);
							} else if (vals[1].equalsIgnoreCase("center")) {
								contentStyle = setlable(1);
							} else {
								contentStyle = setlable(99);
							}
						} else {
							contentStyle = setlable(99);
						}
						addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
								label, contentCol, i + 3, classify);
					}
				}

			}

			book.write();
			os.flush();
			book.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static void addSheetClassify(final WritableSheet sheet,
			final Object value, final WritableCellFormat fmt, Label label,
			final int contentCol, final int row, final String genre)
			throws Exception {
		String textValue = "";
		if (value != null) {
			if (value instanceof Date) {
				label = new Label(contentCol, row, (new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss")).format(value), fmt);
				sheet.addCell(label);
			} else if (genre.equals("Number")) {
				textValue = value.toString();
				String textNumber[] = textValue.split(",");
				String num = "";
				for (int i = 0; i < textNumber.length; i++) {
					num = (new StringBuilder(String.valueOf(num))).append(
							textNumber[i]).toString();
				}

				if (!num.equals("")) {
					Double number = new Double(num);
					Number labelNF = new Number(contentCol, row,
							number.doubleValue(), fmt);
					sheet.addCell(labelNF);
				} else {
					textValue = value.toString();
					label = new Label(contentCol, row, textValue, fmt);
					sheet.addCell(label);
				}
			} else {
				textValue = value.toString();
				label = new Label(contentCol, row, textValue, fmt);
				sheet.addCell(label);
			}
		} else {
			label = new Label(contentCol, row, textValue, fmt);
			sheet.addCell(label);
		}
	}

	public static void addSheet(final WritableSheet sheet, final Object value,
			final WritableCellFormat fmt, Label label, final int contentCol,
			final int row) throws Exception {
		String textValue = "";
		if (value != null) {
			if (value instanceof Date) {
				label = new Label(contentCol, row, (new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss")).format(value), fmt);
				sheet.addCell(label);
			} else {
				textValue = value.toString();
				label = new Label(contentCol, row, textValue, fmt);
				sheet.addCell(label);
			}
		} else {
			label = new Label(contentCol, row, textValue, fmt);
			sheet.addCell(label);
		}
	}

	public static WritableCellFormat setFmt() throws Exception {
		WritableFont bigTitleFont = new WritableFont(WritableFont.ARIAL, 16,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat bigGreyBackground = new WritableCellFormat(
				bigTitleFont);
		bigGreyBackground.setWrap(false);
		bigGreyBackground.setVerticalAlignment(VerticalAlignment.CENTRE);
		bigGreyBackground.setBorder(Border.ALL, BorderLineStyle.THIN,
				Colour.BLACK);
		bigGreyBackground.setAlignment(Alignment.CENTRE);
		bigGreyBackground.setBorder(Border.NONE, BorderLineStyle.THIN);
		return bigGreyBackground;
	}

	public static WritableCellFormat setlable(final int alignStyle)
			throws Exception {
		WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat wcfFC = new WritableCellFormat(wfc);
		wcfFC.setBackground(Colour.WHITE);
		wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		switch (alignStyle) {
		case 1: // '\001'
			wcfFC.setAlignment(Alignment.CENTRE);
			break;

		case 2: // '\002'
			wcfFC.setAlignment(Alignment.RIGHT);
			break;

		default:
			wcfFC.setAlignment(Alignment.LEFT);
			wcfFC.setIndentation(1);
			break;
		}
		return wcfFC;
	}

	public static WritableCellFormat setTHd() throws Exception {
		WritableFont smallBackground = new WritableFont(WritableFont.ARIAL, 11,
				WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
		WritableCellFormat wcfFC = new WritableCellFormat(smallBackground);
		wcfFC.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK);
		wcfFC.setVerticalAlignment(VerticalAlignment.CENTRE);
		wcfFC.setAlignment(Alignment.CENTRE);
		return wcfFC;
	}

	private static WritableCellFormat timeStyle() {
		WritableCellFormat wc = null;
		try {
			wc = new WritableCellFormat();
			wc.setBorder(Border.NONE, BorderLineStyle.THIN);
			wc.setAlignment(Alignment.RIGHT);
		} catch (Exception exception) {
		}
		return wc;
	}

	//专用于还款计划表
		@SuppressWarnings("rawtypes")
		public static void ers2List2Excel(final List<HashMap<String, Object>> list1,final List<Map<String, Object>> list2,
				final String title, final LinkedHashMap headers1,final LinkedHashMap headers2,
				final String fileName,HttpServletResponse response) {
			if (list1 != null && list1.size() == 0) {
				return;
			}
			if (list2 != null && list2.size() == 0) {
				return;
			}
			try {
				OutputStream os = response
						.getOutputStream();
				response.reset();
				response.setHeader(
						"Content-disposition",
						(new StringBuilder("attachment; filename="))
								.append(new String(fileName.getBytes("utf-8"),
										"iso-8859-1")).append(".xls").toString());
				response.setContentType(
						"application/msexcel");
				WritableWorkbook book = Workbook.createWorkbook(os);
				WritableSheet sheet = book.createSheet(title, 0);
				WritableCellFormat bigGreyBackground = setFmt();
				WritableCellFormat contentStyle = null;
				WritableCellFormat smallBackground = setTHd();
				sheet.setRowView(0, 1200);
				Label label = new Label(0, 0, title, bigGreyBackground);
				sheet.addCell(label);
				sheet.addCell(new Label(0, 1, "基本信息", bigGreyBackground));
				sheet.mergeCells(0, 0, headers1.size() - 1, 0);
				sheet.mergeCells(0, 1, headers1.size() - 1, 1);

				Iterator it = headers1.entrySet().iterator();
				for (int headColum = 0; it.hasNext(); headColum++) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					sheet.setColumnView(headColum, 19);
					addSheet(sheet, entry.getValue(), smallBackground, label,
							headColum, 2);
				}
				jxl.write.NumberFormat formatMoney = new jxl.write.NumberFormat(
						"#,##0.00");
				WritableFont wfc = new WritableFont(WritableFont.ARIAL, 11,
						WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
				for (int i = 0; i < list1.size(); i++) {// 添加内容
					HashMap<String, Object> obj = list1.get(i);
					if (obj != null && !"".equals(obj)) {
						Iterator its = headers1.entrySet().iterator();
						for (int contentCol = 0; its.hasNext(); contentCol++) {
							String classify = "";// 分类如 Number ,
							java.util.Map.Entry entry = (java.util.Map.Entry) its
									.next();
							String key = (new StringBuilder()).append(
									entry.getKey()).toString();
							String vals[] = key.split("'-->'");
							if (vals.length > 2 && vals[2].equals("Number")) {
								classify = vals[2];
								contentStyle = new jxl.write.WritableCellFormat(
										formatMoney);
								contentStyle.setFont(wfc);
								contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
								contentStyle.setBorder(Border.ALL,
										BorderLineStyle.THIN, Colour.BLACK);
							} else if (vals.length == 2) {
								if (vals[1].equalsIgnoreCase("right")) {
									contentStyle = setlable(2);
								} else if (vals[1].equalsIgnoreCase("center")) {
									contentStyle = setlable(1);
								} else {
									contentStyle = setlable(99);
								}
							} else {
								contentStyle = setlable(99);
							}
							addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
									label, contentCol, i + 3, classify);
						}
					}

				}
				//第二个表单
				sheet.addCell(new Label(0, 5, "详细信息", bigGreyBackground));
				sheet.mergeCells(0, 5, headers2.size() - 1, 5);

				it = headers2.entrySet().iterator();
				for (int headColum = 0; it.hasNext(); headColum++) {
					java.util.Map.Entry entry = (java.util.Map.Entry) it.next();
					sheet.setColumnView(headColum, 19);
					addSheet(sheet, entry.getValue(), smallBackground, label,
							headColum, 6);
				}
				formatMoney = new jxl.write.NumberFormat(
						"#,##0.00");
				wfc = new WritableFont(WritableFont.ARIAL, 11,
						WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE);
				for (int i = 0; i < list2.size(); i++) {// 添加内容
					Map<String, Object> obj = list2.get(i);
					if (obj != null && !"".equals(obj)) {
						Iterator its = headers2.entrySet().iterator();
						for (int contentCol = 0; its.hasNext(); contentCol++) {
							String classify = "";// 分类如 Number ,
							java.util.Map.Entry entry = (java.util.Map.Entry) its
									.next();
							String key = (new StringBuilder()).append(
									entry.getKey()).toString();
							String vals[] = key.split("'-->'");
							if (vals.length > 2 && vals[2].equals("Number")) {
								classify = vals[2];
								contentStyle = new jxl.write.WritableCellFormat(
										formatMoney);
								contentStyle.setFont(wfc);
								contentStyle.setBackground(Colour.WHITE);// 设置单元格的颜色为白色色
								contentStyle.setBorder(Border.ALL,
										BorderLineStyle.THIN, Colour.BLACK);
							} else if (vals.length == 2) {
								if (vals[1].equalsIgnoreCase("right")) {
									contentStyle = setlable(2);
								} else if (vals[1].equalsIgnoreCase("center")) {
									contentStyle = setlable(1);
								} else {
									contentStyle = setlable(99);
								}
							} else {
								contentStyle = setlable(99);
							}
							addSheetClassify(sheet, obj.get(vals[0]), contentStyle,
									label, contentCol, i + 7, classify);
						}
					}

				}

				book.write();
				os.flush();
				book.close();
				os.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		public static void main(String[] agrs){
//			String dataStr  = "[{applyId=52540, backUserId=668605, finishTransAmount=140.00, backPreOrdId=65146994016761778985, status=510, transPreIncomeRate=9.98, debtId=176010, realTransAmount=140.38, transFee=0.70, backUndertakeUserName=\"yk_kisv4678\", investAmount=140.00, transPreIncome=3.11}]";
//			Map<String,Object> data = new HashMap<String,Object>();
//			JSONArray jsonArray = JSONArray.fromObject(dataStr);  
//	        List<Map<String,Object>> mapListJson = (List)jsonArray;  
//	        for (int i = 0; i < mapListJson.size(); i++) {  
//	            Map<String,Object> obj=mapListJson.get(i);  
//	            for(Entry<String,Object> entry : obj.entrySet()){ 
//	            	 String strkey1 = entry.getKey();  
//	                 Object strval1 = entry.getValue();
//	            	 data.put(strkey1, strval1);
//	                 System.out.println("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");  
//	            }  
//	        }  
//	        System.out.println("");
			
			
		}

		/**
		 * 1.大数据sxssf--Excel2007以上数据下载
		 * 2.目前传入的格式只支持数字，字母，时间戳，如需其他格式，请补充
		 * @param response
		 * @param list
		 * @param headers 表头
		 * @param fileName 文件名称
		 * @param otherStr 其他描述信息（目前只存在第二行）
		 */
		public static  void export(HttpServletResponse response,Map<String,Object> headers,List<Map<String,Object>> list,String fileName,String otherStr) {  
		    try {  
		    	response.setCharacterEncoding("utf-8");  
		    	response.setContentType("multipart/form-data");  
		    	response.setHeader("Content-Disposition", "attachment;fileName=" + new String(fileName.getBytes("gb2312"), "ISO-8859-1")+ ".xlsx");  
		    	Map<String, Object> mapMessage = new HashMap<String, Object>();  
		        OutputStream out = response.getOutputStream();  
		        SXSSFWorkbook wb = new SXSSFWorkbook(-1);  
		        // turn off auto-flushing and accumulate all rows in memory  
		        wb.setCompressTempFiles(true); //使用gzip压缩,减小空间占用  
		        
		        Sheet sh = wb.createSheet("下载日志");  
		        Row rowHeader0 = sh.createRow(0);  
		        Cell cellHeader0 = rowHeader0.createCell(0);
		        cellHeader0.setCellValue(otherStr);
		        
		        Row rowHeader = sh.createRow(1);  
		        Cell cellHeader = rowHeader.createCell(0); 
		        Iterator it = headers.entrySet().iterator();
		        for(int i=0;it.hasNext();i++){
		        	//设置每一列的宽度,注意 要乘以256,因为1代表excel中一个字符的1/256的宽度  
		        	sh.setColumnWidth(i, 40 * 200);
			        cellHeader = rowHeader.createCell(i);  
			        cellHeader.setCellValue((String)((Entry)it.next()).getValue());  
		        }
		        if(!CollectionUtils.isEmpty(list)){
			        //写入数据
			        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
			        for(int rownum = 2; rownum < list.size() + 2; rownum++) {  
			            Row row = sh.createRow(rownum);  
			            Iterator it1 = headers.entrySet().iterator();
			            for(int i=0;it1.hasNext();i++){
			            	Cell cell = row.createCell(i); 
			            	Entry entry=(Entry)it1.next();
			            	if(list.get(rownum - 2).get(entry.getKey())==null){
			            		cell.setCellValue("");
			            	}else if (list.get(rownum - 2).get(entry.getKey()) instanceof String) {
			            		cell.setCellValue((String)list.get(rownum - 2).get(entry.getKey())); 
			            	}else if(list.get(rownum - 2).get(entry.getKey()) instanceof java.sql.Timestamp){
			            		cell.setCellValue(simpleDateFormat.format(list.get(rownum - 2).get(entry.getKey())));
			            	}
				        }
			        }  
		        }
		        wb.write(out);  
		        out.close();  
		        wb.dispose();  
		    } catch(Exception e) {  
		        e.printStackTrace();  
		    }  
		} 
}
