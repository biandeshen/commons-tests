package xyz.biandeshen.commonstests.单号池;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author fjp
 * @Title: DCreate
 * @ProjectName commons-tests
 * @Description: TODO
 * @date 2019/12/2714:25
 */
public class DCreate {
	
	public static void main(String[] args) {
		AtomicLong startwaybill = new AtomicLong(10000000001L);
		
		StringBuffer sb;
		String[] waybill = new String[1000];
		int i = 0;
		long start = System.currentTimeMillis();
		for (; i < waybill.length; i++) {
			//Long waybillnum = i;
			//Long l = waybillnum * 10 + waybillnum % 7;
			////System.out.println("l = " + l);
			////System.out.println("(l^0) = " + (l ^ 0));
			//System.out.println(i + "\t" + l + "\t"+ (l-i)+"\t"+((l-i)|(l-startwaybill.getAndDecrement())));
			////System.out.println((((l^startwaybill.get())&((l-i)|(l-startwaybill.getAndDecrement())))));
			////System.out.println();
			
			long waybillnum = startwaybill.addAndGet(1);
			sb = new StringBuffer("ZJS");
			//waybill[i] = sb.append(waybillnum * 10 + waybillnum % 7).toString();
			sb.append(Math.addExact(Math.multiplyExact(waybillnum, 10), Math.floorMod(waybillnum, 7)));
			waybill[i] = sb.toString();
		}
		for (String s : waybill) {
			System.out.println("s = " + s);
		}
		System.out.println("waybill[0] = " + waybill[0]);
		System.out.println("sb.append(10000000001L * 10 + 10000000001L % 7).toString() = " + (10000000001L * 10 + 10000000001L % 7));
		System.out.println("spend time = " + (System.currentTimeMillis() - start) + "ms");
	}
	
	//(1000*1000*1000*10+n)%7
	//(1000%7*1000%7*1000%7*10%7+n%7)%7
}
