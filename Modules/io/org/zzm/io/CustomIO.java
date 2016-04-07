package org.zzm.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.security.cert.CertPathValidatorException.Reason;

public class CustomIO {

	private String fileName;
	
	/**
	 * 用于控制文件类型，在readFileName()方法的判断中进行检查
	 */
	
	private String ext;

	/**
	 * 只读取特定类型的文档
	 * @param ext e.g. ext = ".csv";
	 */
	public CustomIO(String ext){
		this.ext = ext;
	}
	
	/**
	 * 读取所有文档
	 */
	public CustomIO(){
		this.ext = "";
	}
	
	/**
	 * Get resource path and name
	 * @param ext - if null, no file type request
	 * @return
	 */
	public String readFileName() {
		System.out.println("Please enter the file path: ");
		BufferedReader strin = new BufferedReader(new InputStreamReader(
				System.in));
		try {
			String fileName = strin.readLine();
			//检查文件是否存在，文件类型是否符合要求
			while (fileName == null || fileName.trim().equals("")
					|| fileName.indexOf(".") == -1   //必须要有文件名
					|| fileName.indexOf(ext) == -1
					|| !new File(fileName).exists()) {
				System.out.print("Please input the file path: ");
				strin = new BufferedReader(new InputStreamReader(System.in));
				fileName = strin.readLine();
			}
			this.fileName = fileName;
			this.ext = fileName.substring(fileName.lastIndexOf("."));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (strin != null) {
					strin.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			strin = null;
		}
		return fileName;
	}

	/**
	 * 先跑readFileName()方法来获取文件名，之后读入到内存中，最后一个分隔符已经处理了 e.g. 123|456 末尾没有分隔符
	 * @param fileName - run readFilename() first
	 * @param encode - UTF-8 is default
	 * @param delimeter - 如果为空，则不添加分割
	 * @return
	 */
	public String readFile(String encode, String delimeter)	{
		boolean isDelimit = false;
		if (encode == null) {
			encode = "UTF-8";
		}
		if (delimeter != null) {
			isDelimit = true;
		}
		FileInputStream fis = null;
		InputStreamReader isw = null;
		BufferedReader br = null; 
		StringBuffer readLine = new StringBuffer();
		try{
			readFileName();
			fis = new FileInputStream(fileName);
			isw = new InputStreamReader(fis, encode);
			br = new BufferedReader(isw);
			boolean bReadNext = true;
			while (bReadNext) {
				if (readLine.length() > 0 && isDelimit == true && delimeter != null) {
					readLine.append(delimeter);
				}
				// one line
				String strReadLine = br.readLine();
				
				if (strReadLine == null) {
					break;
				}
				readLine.append(strReadLine);
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try{
				if(br != null){
					br.close();
					br = null;
				}
				if(isw != null){
					isw.close();
					isw = null;
				}
				if(fis != null){
					fis.close();
					fis = null;
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return readLine.substring(0, readLine.length() - 1);
	}

	/**
	 * 
	 * @return
	 */
	private String getNewFileName() {
		System.out.println("Please output path:");
		BufferedReader strin = new BufferedReader(new InputStreamReader(
				System.in));
		String fileName = "";
		try{
			fileName = strin.readLine();
			String path = "";
			int lastSlashIndex = -1;
			if((lastSlashIndex = fileName.lastIndexOf("\\")) != -1){
				path = fileName.substring(0, lastSlashIndex);
			}
			while (fileName == null || fileName.trim().equals("")
					|| fileName.indexOf(".") == -1   //必须要有文件名
					|| fileName.indexOf(ext) == -1
					|| !new File(path).exists()) {	//确保输出的目录存在
				System.out.print("Please input the file path:");
				strin = new BufferedReader(new InputStreamReader(System.in));
				fileName = strin.readLine();
				if((lastSlashIndex = fileName.lastIndexOf("\\")) != -1){
					path = fileName.substring(0, lastSlashIndex);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return fileName;
	}

	public boolean writeFile(String content) {
		boolean isSuccess = false;
		File file = new File(getNewFileName());
		FileOutputStream out = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		try {
			out = new FileOutputStream(file);
			osw = new OutputStreamWriter(out, "UTF8");
			bw = new BufferedWriter(osw);
			bw.write(content);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try{
				if (bw != null)
					bw.close();
				if (osw != null)
					osw.close();
				if (out != null)
					out.close();
				isSuccess = true;
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return isSuccess;
	}
}
