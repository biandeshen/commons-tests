package xyz.biandeshen.Java程序性能优化.designpatterns;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fjp
 * @Title: FlyWeight
 * @ProjectName commons-tests
 * @Description: 享元模式
 * @date 2019/12/2316:04
 */
public class FlyWeight {
	public static void main(String[] args) {
		ReportManagerFactory rmf = new ReportManagerFactory();
		IReportManager rm = rmf.getFinancialReportManager("A");
		System.out.println("rm.createReport() = " + rm.createReport());
	}
	
}


interface IReportManager {
	public String createReport();
}

// 具体享元类
class FinancialReportManager implements IReportManager {
	protected String tenantId = null;
	
	public FinancialReportManager(String tenantId) {
		this.tenantId = tenantId;
	}
	
	@Override
	public String createReport() {
		return "This is a financial report!";
	}
}

// 具体享元类
class EnployeeReportManager implements IReportManager {
	protected String teantId = null;
	
	public EnployeeReportManager(String teantId) {
		this.teantId = teantId;
	}
	
	@Override
	public String createReport() {
		return "This is a employee report!";
	}
}

// 享元工厂类
class ReportManagerFactory {
	Map<String, IReportManager> financialReportManager = new HashMap<>();
	Map<String, IReportManager> employeeReportManager = new HashMap<>();
	
	public IReportManager getFinancialReportManager(String teananId) {
		IReportManager r = financialReportManager.get(teananId);
		if (r == null) {
			r = new FinancialReportManager(teananId);
			financialReportManager.put(teananId, r);
		}
		return r;
	}
	
	public IReportManager getEmployeeReportManager(String teananId) {
		IReportManager r = employeeReportManager.get(teananId);
		if (r == null) {
			r = new EnployeeReportManager(teananId);
			employeeReportManager.put(teananId, r);
		}
		return r;
	}
}