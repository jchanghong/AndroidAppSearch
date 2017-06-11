package com.handsomezhou.xdesktophelper.xfyun.util;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//import com.iflytek.cloud.ErrorCode;
//import com.iflytek.cloud.SpeechConstant;
//import com.iflytek.cloud.SpeechUtility;

/**
 * 功能性函数扩展类
 */
public class FucUtil {
	/**
	 * 读取asset目录下文件。
	 * @return content
	 */
	public static String readFile(Context mContext,String file,String code)
	{
		int len = 0;
		byte []buf = null;
		String result = "";
		try {
			InputStream in = mContext.getAssets().open(file);			
			len  = in.available();
			buf = new byte[len];
			in.read(buf, 0, len);
			
			result = new String(buf,code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * 将字节缓冲区按照固定大小进行分割成数组
	 * @param buffer 缓冲区
	 * @param length 缓冲区大小
	 * @param spsize 切割块大小
	 * @return
	 */
	public ArrayList<byte[]> splitBuffer(byte[] buffer,int length,int spsize)
	{
		ArrayList<byte[]> array = new ArrayList<byte[]>();
		if(spsize <= 0 || length <= 0 || buffer == null || buffer.length < length)
			return array;
		int size = 0;
		while(size < length)
		{
			int left = length - size;
			if(spsize < left)
			{
				byte[] sdata = new byte[spsize];
				System.arraycopy(buffer,size,sdata,0,spsize);
				array.add(sdata);
				size += spsize;
			}else
			{
				byte[] sdata = new byte[left];
				System.arraycopy(buffer,size,sdata,0,left);
				array.add(sdata);
				size += left;
			}
		}
		return array;
	}
	/**
	 * 获取语记是否包含离线听写资源，如未包含跳转至资源下载页面
	 *1.PLUS_LOCAL_ALL: 本地所有资源 
      2.PLUS_LOCAL_ASR: 本地识别资源
      3.PLUS_LOCAL_TTS: 本地合成资源
	 */
	public static String checkLocalResource(){
		return null;
	}
	
	/**
	 * 读取asset目录下音频文件。
	 * 
	 * @return 二进制文件数据
	 */
	public static byte[] readAudioFile(Context context, String filename) {
		try {
			InputStream ins = context.getAssets().open(filename);
			byte[] data = new byte[ins.available()];
			
			ins.read(data);
			ins.close();
			
			return data;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
