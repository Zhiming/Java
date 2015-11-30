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
	 * ���ڿ����ļ����ͣ���readFileName()�������ж��н��м��
	 */
	
	private String ext;

	/**
	 * ֻ��ȡ�ض����͵��ĵ�
	 * @param ext e.g. ext = ".csv";
	 */
	public CustomIO(String ext){
		this.ext = ext;
	}
	
	/**
	 * ��ȡ�����ĵ�
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
			//����ļ��Ƿ���ڣ��ļ������Ƿ����Ҫ��
			while (fileName == null || fileName.trim().equals("")
					|| fileName.indexOf(".") == -1   //����Ҫ���ļ���
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
	 * ����readFileName()��������ȡ�ļ�����֮����뵽�ڴ��У����һ���ָ����Ѿ������� e.g. 123|456 ĩβû�зָ���
	 * @param fileName - run readFilename() first
	 * @param encode - UTF-8 is default
	 * @param delimeter - ���Ϊ�գ�����ӷָ�
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
					|| fileName.indexOf(".") == -1   //����Ҫ���ļ���
					|| fileName.indexOf(ext) == -1
					|| !new File(path).exists()) {	//ȷ�������Ŀ¼����
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
