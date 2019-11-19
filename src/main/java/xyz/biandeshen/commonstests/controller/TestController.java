package xyz.biandeshen.commonstests.controller;

import org.springframework.web.bind.annotation.*;
import spp.model.common.CommonMsgModel;


/**
 * @author fjp
 * @Title: TestController
 * @ProjectName commons-tools
 * @Description: TODO
 * @date 2019/3/1915:45
 */

@RestController
public class TestController {
	@RequestMapping(value = "/info", method = RequestMethod.GET)
	public String info() {
		return "hello world!";
	}
	
	@RequestMapping(value = "/interface", method = RequestMethod.POST)
	public void test(@RequestParam("clientFlag") String clientFlag, @RequestParam("verifyData") String verifyData, @RequestParam("data") String data) {
		System.err.println(clientFlag + "\t" + verifyData + "\t" + data);
	}
	
	@RequestMapping(value = "/interface1", method = RequestMethod.POST)
	public void test1(String clientFlag, String verifyData, String data) {
		System.err.println(clientFlag);
	}
	
	@RequestMapping(value = "/interface2", method = {RequestMethod.POST, RequestMethod.GET})
	public CommonMsgModel test2(@RequestBody CommonMsgModel cmm) {
		System.err.println(cmm);
		return cmm;
	}
	
	@RequestMapping(value = "/interface3", method = RequestMethod.POST)
	public void test3(String clientFlag, String verifyData, String data) {
		System.err.println(clientFlag);
	}
	
	@RequestMapping(value = "/interface4", method = RequestMethod.POST)
	public String test4(String msg_type, String logistic_provider_id, String logistics_interface,String data_digest) {
		System.out.println("msg_type = " + msg_type);
		System.out.println("logistic_provider_id = " + logistic_provider_id);
		System.out.println("logistics_interface = " + logistics_interface);
		System.out.println("data_digest = " + data_digest);
		return "<responses><logisticProviderID>ZJS</logisticProviderID><responseItems><response><success>true</success><mailNos>A000871434126</mailNos><reason /></response></responseItems></responses>";
	}
	
}
