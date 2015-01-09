package com.flozano.springinterceptortest.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class ThingController implements AutoCloseable {

	private final ExecutorService executorService = Executors
			.newCachedThreadPool();

	@RequestMapping(value = "/async", method = RequestMethod.GET)
	public DeferredResult<String> getAsync() {
		DeferredResult<String> dr = new DeferredResult<>();
		executorService.submit(() -> perform(dr));
		return dr;
	}

	@RequestMapping(value = "/sync", method = RequestMethod.GET)
	public String getSync() {
		return "OK!!!";
	}

	private void perform(DeferredResult<String> dr) {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		dr.setResult("Allright");
	}

	@Override
	public void close() throws Exception {
		executorService.shutdownNow();
	}
}
