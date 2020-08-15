package xyz.biandeshen.io;

import com.google.common.primitives.Bytes;
import org.bouncycastle.util.Arrays;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @FileName: TestByteArrayOutputStream
 * @Author: fjp
 * @Date: 2020/8/11 10:13
 * @Description: 测试
 * History:
 * <author>          <time>          <version>
 * fjp           2020/8/11           版本号
 */
public class TestByteArrayOutputStream {
	private byte[] body = new byte[0];
	
	@Test
	public void test() throws IOException {
		DecorByteArrayOutPutStream outputStream = new DecorByteArrayOutPutStream();
		outputStream.write("test1".getBytes(StandardCharsets.UTF_8), 2, 3);
		outputStream.write("test".getBytes());
		outputStream.flush();
		String string = outputStream.toString(StandardCharsets.UTF_8.toString());
		System.out.println("string = " + string);
		
		String bodystr = new String(body, StandardCharsets.UTF_8);
		String bodystr2 = new String(body);
		System.out.println("bodystr = " + bodystr);
		System.out.println("bodystr2 = " + bodystr2);
	}
	
	class DecorByteArrayOutPutStream extends ByteArrayOutputStream {
		
		@Override
		public synchronized void write(byte[] b, int off, int len) {
			super.write(b, off, len);
			
			TestByteArrayOutputStream.this.body = addBytes(TestByteArrayOutputStream.this.body, toByteArray());
			
			//byte[] concat = Bytes.concat(TestByteArrayOutputStream.this.body, Arrays.copyOfRange(buf, 0, count));
			//TestByteArrayOutputStream.this.body = concat;
			
		}
		
		@Override
		public void write(byte[] b) throws IOException {
			super.write(b);
			
			TestByteArrayOutputStream.this.body = addBytes(TestByteArrayOutputStream.this.body, toByteArray());
			
			//byte[] concat = Bytes.concat(TestByteArrayOutputStream.this.body, Arrays.copyOfRange(buf, 0, count));
			//TestByteArrayOutputStream.this.body = concat;
			
		}
		
		@Override
		public synchronized String toString(String charsetName) throws UnsupportedEncodingException {
			int bodysize = TestByteArrayOutputStream.this.body.length;
			return new String(buf, 0, count + bodysize, charsetName);
		}
		
		public byte[] addBytes(byte[] data1, byte[] data2) {
			byte[] data3 = new byte[data1.length + data2.length];
			System.arraycopy(data1, 0, data3, 0, data1.length);
			System.arraycopy(data2, 0, data3, data1.length, data2.length);
			return data3;
		}
		
	}
	
}
