package spring.demo.action;

import org.junit.Test;
import spring.demo.service.IModifyService;
import spring.demo.service.IQueryService;
import spring.framework.annotation.Autowired;
import spring.framework.annotation.Controller;
import spring.framework.context.ApplicationContext;


import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 公布接口url
 * @author Tom
 *
 */
@Controller
public class MyAction {

	public IQueryService getQueryService() {
		return queryService;
	}

	@Autowired("queryService1")
	IQueryService queryService;
	@Autowired
	IModifyService modifyService;

	public void print(){
		System.out.println(queryService);
	}

	@Test
	public void test() {
		ApplicationContext appContextText = new ApplicationContext();
		appContextText.setConfigLocations("classpath: application.properties");
		appContextText.onRefresh();

		System.out.println(queryService.query("a"));
	}
}
