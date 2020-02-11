package org.mThree.ControllerFiles;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestController {



	@Test
	public void testGetDataByWeek(){
		Controller controller = new Controller();
		//System.out.println(controller.getDataByWeek("2019-12-06"));
		assertTrue(controller.getDataByWeek("2019-12-06").equals("151.81, 151.87, 146.65, 151.75, 104537379"));
	}
	@Test
	public void testGetDataByDay(){
		Controller controller = new Controller();
		assertTrue(controller.getDataByDay("2019-12-06").equals("150.99, 151.87, 150.27, 151.75, 16410400"));
	}
	@Test
	public void testGetDataBy5Min(){
		Controller controller = new Controller();
		assertTrue(controller.getDataBy5Min("2020-02-11 12:50:00").equals("186.76, 186.765, 186.52, 186.67, 308151"));
	}


}
