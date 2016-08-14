package com.mickyli.java.nio.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class FileChannelTest {

	public static void write(){
		try {
			File file = new File("data/data.txt");
	        FileOutputStream outputStream = new FileOutputStream(file);
			
	        FileChannel channel = outputStream.getChannel();
	        ByteBuffer buffer = ByteBuffer.allocate(1024);
	        String string = "java nio";
	        buffer.put(string.getBytes());
	        buffer.flip();     //此处必须要调用buffer的flip方法
	        channel.write(buffer);
	        channel.close();
	        outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void readCommon(){
		Charset charset = Charset.forName("GBK");  
        CharsetDecoder decoder = charset.newDecoder();  
        FileInputStream fis = null;  
        try {  
            fis = new FileInputStream("data/data-nio.txt");  
            FileChannel fileChannel = fis.getChannel();  
            /**
             * TODO
             * 当byte大于1024时，仍然乱码
             */
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);  
            CharBuffer charBuffer = CharBuffer.allocate(1024);  
            int bytes = fileChannel.read(byteBuffer);  
            while(bytes!=-1){  
                byteBuffer.flip();  
                decoder.decode(byteBuffer, charBuffer, false);  
                charBuffer.flip();  
                
                System.out.println(charBuffer);  
                charBuffer.clear();  
                byteBuffer.clear();  
                bytes = fileChannel.read(byteBuffer);  
            }  
            if(fis!=null){  
                fis.close();  
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
	}
	
	
	public static void read(){
		try {
			/**
			 * Java.nio.charset.Charset处理了字符转换问题
			 * 它通过构造CharsetEncoder和CharsetDecoder将字符序列转换成字节和逆转换。
			 */
			Charset charset = Charset.forName("GBK");
			// 创建解码器
			CharsetDecoder decoder = charset.newDecoder();
			
			RandomAccessFile aFile = new RandomAccessFile("data/data-nio.txt", "rw");
			
			FileChannel inChannel = aFile.getChannel();

			// 定义一个ByteBuffer,用于重复读取数据
			ByteBuffer byteBuffer = ByteBuffer.allocate(64);// 每次取出64字节
			// 将FileChannel的数据放入ByteBuffer中
			int bytesRead = inChannel.read(byteBuffer);
			while (bytesRead != -1) {
				System.out.println("Read " + bytesRead);
				
				/**
				 * 表示Buffer从写状态切换到读状态
				 * 把Buffer的limit固定到当前的position，然后把position归零
				 */
				byteBuffer.flip();
				
				/**
				 * 因为在GBK中字母占1byte汉字占2byte。charbuffer以两个字节为单位处理报文的
				 * ByteBuffer如果设置长度为偶数时，如果有奇数个字母那么最后的byte就是汉字的一部分，转码的时候就会出错。
				 * Bytebuffer设为奇数正好相反。
				 * 判断ByteBuffer.get(index)大于0小于127的个数，然后确定最后一位是否要放到下次读入的第一位 
				 * 
				 * TODO
				*/
                CharBuffer charBuffer = decoder.decode(byteBuffer);
				System.out.println(charBuffer);
				
				
//				while(byteBuffer.hasRemaining()){
//					System.out.print((char)byteBuffer.get());
//				}
				
				// 将ByteBuffer初始化，为下一次读取数据做准备
                byteBuffer.clear();
				
				/**
				 * 读取处理完后继续从channel中读取数据
				 * 为了返回-1无数据可读退出循环体
				 * 等同于while (inChannel.read(buf) != -1)
				 */
				bytesRead = inChannel.read(byteBuffer);
			}
			//RandomAccessFile的close方法会将对应的非空channel关闭
			aFile.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 一次性全部读进内存，消耗性能
	 */
	public static void readMap(){
		try {
			File file=new File("data/data-nio.txt");
			//以文件输入流FileInputStream创建FileChannel，以控制输入
			FileChannel inChannel=new FileInputStream(file).getChannel();
			//以文件输出流FileOutputStream创建FileChannel，以控制输出
			FileChannel outChannel=new FileOutputStream("data/a.txt").getChannel();
			//将FileChannel里的全部数据映射成ByteBuffer
			MappedByteBuffer buffer=inChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			//直接将buffer里的数据全部输出
			outChannel.write(buffer);
			//再次调用buffer的clear()方法,复原limit、position的位置
			buffer.clear();
			//使用GBK字符集来创建解码器
			Charset charset=Charset.forName("GBK");
			//创建解码器（CharsetDecoder）对象
			CharsetDecoder decoder=charset.newDecoder();
			//使用解码器将ByteBuffer转换成CharBuffer
			CharBuffer charBuffer=decoder.decode(buffer);
			System.out.println(charBuffer);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		//readCommon();
		//read();
		//readMap();
		write();
	}
}
