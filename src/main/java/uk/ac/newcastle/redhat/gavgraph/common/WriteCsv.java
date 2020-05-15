package uk.ac.newcastle.redhat.gavgraph.common;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.io.IOUtils.closeQuietly;

public class WriteCsv	 {

	/** CSV file's separator for column changing */
	private static final String CSV_COLUMN_SEPARATOR = ",";

	/** CSV file's separator for row changing */
	private static final String CSV_RN = "\r\n";

	private static final int BATCH = 50000;

	/**
	 * output the data into CSV file
	 *
	 * @param fileName file name（absolute path）
	 * @param datas data collection
	 * @param displayColNames displayed column names
	 * @param fieldNames filed names
	 * @throws IOException
	 */
	public static void writeCvs(String fileName, List<Map<String, Object>> datas, String[] displayColNames, String[] fieldNames) throws IOException {
		int x = datas.size() / BATCH + 1;
		if (datas.size() % BATCH == 0) {
			x = datas.size() / BATCH;
		}

		String[] fm = fileName.split("\\.", fileName.lastIndexOf("."));
		for (int i = 0; i < x; i++) {
			String curFilename = fileName;
			int s = i * BATCH;
			int e = 0;
			if (i == (x - 1)) {
				e = datas.size();
			} else {
				e = s + BATCH;
			}

			if (x > 1) {
				curFilename = fm[0] + "-" + (i + 1) + "." + fm[1];
			}
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			data.addAll(datas.subList(s, e));
			String inStr = formatCsvData(data, displayColNames, fieldNames,true);
			writeLineData(curFilename, inStr);
		}
	}

	public static void writeCvs(String fileName, List<Map<String, Object>> datas, String[] displayColNames, String[] fieldNames, boolean isAppend,boolean isWriteHeaders) throws Exception {
		String inStr = formatCsvData(datas, displayColNames, fieldNames, isWriteHeaders);
		write(inStr, fileName, isAppend);
	}

	/**
	 * write out the data into the corresponding csv file's column
	 */
	public static String formatCsvData(List<Map<String, Object>> data, String[] displayColNamesArr, String[] matchColNamesMapArr,boolean isWriteHeaders) {

		StringBuffer buf = new StringBuffer();

		if(isWriteHeaders){
			// write out the header of the column
			for (int i = 0; i < displayColNamesArr.length; i++) {
				buf.append(displayColNamesArr[i]).append(CSV_COLUMN_SEPARATOR);
			}
			buf.append(CSV_RN);
		}

		if (null != data) {
			// data out stream
			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < matchColNamesMapArr.length; j++) {
					buf.append(data.get(i).get(matchColNamesMapArr[j])).append(CSV_COLUMN_SEPARATOR);
				}
				buf.append(CSV_RN);
			}
		}
		return buf.toString();
	}

	/**
	 * write the data out into file
	 *
	 * @param fileName
	 * @param inStr
	 */
	public static void writeLineData(String fileName, String inStr) {
		OutputStream os = null;
		BufferedWriter bw = null;
		try {
			File file = new File(fileName);
			os = new FileOutputStream(file);// 得到输出流
			bw = new BufferedWriter(new OutputStreamWriter(os, "GB2312"));
			bw.write(inStr);
			bw.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
				if (bw != null) {
					bw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * writing file operation
	 *
	 * @param str content need to write
	 * @param fileName filename
	 * @throws Exception
	 */
	private static void write(String str, String fileName, boolean isAppend) throws Exception {
		if (fileName == null || str == null) {
			return;
		}
		// Checking the directory exist or not, if not, we need to create
		mkDir(fileName);
		BufferedWriter out = null;

		try {
			out = new BufferedWriter(new FileWriter(fileName, isAppend));
			out.write(str);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeQuietly(out);
		}
	}

	public static boolean mkDir(String dirName) {
		dirName = dirName.replaceAll("\\\\", "/");
		File file = new File(dirName);
		// see the file or directory exists or not
		if (file.exists()) {
			// see the file or directory is readable or not
			if (file.canWrite() == false) {
				return false;
			} else {
				return true;
			}
		} else{
			// if does not exist, create the directory
			String path = null;

			int firstSlash = dirName.indexOf("/");
			int finalSlash = dirName.lastIndexOf("/");

			// illegal filepath
			if (finalSlash == 0) {
				return false;
			// system root
			} else if (finalSlash == 1) {
				path = File.separator;
			// ensure the separator is part of the filepath
			} else if (firstSlash == finalSlash){
				path = dirName.substring(0, finalSlash + 1);
			} else {
				path = dirName.substring(0, finalSlash);
			}

			File dir = new File(path);
			return dir.mkdirs();
		}
	}

	public static void main(String[] args) {
		String fileName = "D:\\dissertation\\gavgraph\\src\\main\\resources\\static\\xml\\test.csv";

		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		String[] matchColNamesMapArr = new String[] { "a", "b", "c" };
		String[] displayColNamesArr = new String[] { "aa", "bb", "cc" };

		for (int i = 0; i < 100; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("a", "a is" + i);
			map.put("b", "b are" + i);
			map.put("c", "c were" + i);
			data.add(map);
		}

		try {
			writeCvs(fileName, data, displayColNamesArr, matchColNamesMapArr);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void exportCsv(String fileName, String content, HttpServletResponse response) throws IOException {
		// set the file's suffix
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhh24mmss");
		String fn = fileName.concat(sdf.format(new Date()).toString() + ".csv");

		// read the encoding format
		String csvEncoding = "UTF-8";

		// setting the responding
		response.setCharacterEncoding(csvEncoding);
		response.setContentType("text/csv; charset=" + csvEncoding);
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=30");
		response.setHeader("Content-Disposition", "attachment; filename=" + new String(fn.getBytes(), csvEncoding));

		// write out the responding
		OutputStream os = response.getOutputStream();
		os.write(content.getBytes("GBK"));
		os.flush();
		os.close();
	}
}