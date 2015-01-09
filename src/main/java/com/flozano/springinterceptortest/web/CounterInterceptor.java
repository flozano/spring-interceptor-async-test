package com.flozano.springinterceptortest.web;

import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CounterInterceptor implements HandlerInterceptor {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CounterInterceptor.class);

	private final AtomicInteger syncInvocations = new AtomicInteger(0);
	private final AtomicInteger asyncInvocations = new AtomicInteger(0);

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		LOGGER.info(
				"Invoked prehandle (request.getDispatcherType()={}, syncInvocations={}, asyncInvocations={})",
				request.getDispatcherType(), getSyncInvocations(),
				getAsyncInvocations());
		if (DispatcherType.ASYNC.equals(request.getDispatcherType())) {
			asyncInvocations.incrementAndGet();
		} else {
			syncInvocations.incrementAndGet();
		}
		return true;
	}

	public int getSyncInvocations() {
		return syncInvocations.get();
	}

	public int getAsyncInvocations() {
		return asyncInvocations.get();
	}

	public void reset() {
		asyncInvocations.set(0);
		syncInvocations.set(0);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
}
