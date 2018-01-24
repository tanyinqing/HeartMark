package com.yuzhi.framework.util;



import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.yuzhi.framework.constant.ConnectionConstant;

public class FileUploadUtil {
	
	private static final String CHARSET = "utf-8"; 
	

	@SuppressWarnings("resource")
	public static Object upload(String serverUrl,String picPath) {
		Object result = "";
		
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		FileInputStream fin = null;

		String boundary = "---------------------------265001916915724";
		String urlServer = ConnectionConstant.SERVER_URL+serverUrl;
		String lineEnd = "\r\n";
		String pathOfPicture = picPath;
		int bytesAvailable, bufferSize, bytesRead;
		int maxBufferSize = 1 * 1024 * 512;
		byte[] buffer = null;

		try {
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(true);

			connection.setRequestMethod("POST");

			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "text/plain");
			connection.setRequestProperty("Charset", CHARSET); // ���ñ���
			
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			dos = new DataOutputStream(connection.getOutputStream());
			fin = new FileInputStream(pathOfPicture);

			String fileMeta = "--"
					+ boundary
					+ lineEnd
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ pathOfPicture + "\"" + lineEnd
					+ "Content-Type: image/jpeg" + lineEnd + lineEnd;
			dos.write(fileMeta.getBytes());
			
			bytesAvailable = fin.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fin.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fin.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fin.read(buffer, 0, bufferSize);
			}
			dos.writeBytes(lineEnd + lineEnd);

			dos.writeBytes("--" + boundary + "--");

			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line=bufferedReader.readLine()) != null){
				sb.append(line);
			}
			result = JsonUtil.convertJsonToObject(sb.toString());
			if (dos != null) {
				dos.flush();
				dos.close();
			}
		} catch (Exception e) {
		}
		return result;
	}

	public static Object putImg(String serverUrl,String picPath) {
		Object result = "";
		
		HttpURLConnection connection = null;
		DataOutputStream dos = null;
		FileInputStream fin = null;

		String boundary = "---------------------------265001916915724";
		String urlServer = ConnectionConstant.SERVER_URL+serverUrl;
		String lineEnd = "\r\n";
		String pathOfPicture = picPath;
		int bytesAvailable, bufferSize, bytesRead;
		int maxBufferSize = 1 * 1024 * 512;
		byte[] buffer = null;

		try {
			URL url = new URL(urlServer);
			connection = (HttpURLConnection) url.openConnection();

			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(true);

			connection.setRequestMethod("POST");

			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("Content-Type", "text/plain");
			connection.setRequestProperty("Charset", CHARSET);
			
			connection.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + boundary);

			dos = new DataOutputStream(connection.getOutputStream());
			fin = new FileInputStream(pathOfPicture);

			String fileMeta = "--"
					+ boundary
					+ lineEnd
					+ "Content-Disposition: form-data; name=\"upload\"; filename=\""
					+ pathOfPicture + "\"" + lineEnd
					+ "Content-Type: image/jpeg" + lineEnd + lineEnd;
			dos.write(fileMeta.getBytes());
			
			bytesAvailable = fin.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			bytesRead = fin.read(buffer, 0, bufferSize);
			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fin.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fin.read(buffer, 0, bufferSize);
			}
			dos.writeBytes(lineEnd + lineEnd);
			dos.writeBytes("--" + boundary + "--");

			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while((line=bufferedReader.readLine()) != null){
				sb.append(line);
			}
			result = JsonUtil.convertJsonToObject(sb.toString());
			if (dos != null) {
				dos.flush();
				dos.close();
			}
		} catch (Exception e) {
		}
		return result;
	}
}
