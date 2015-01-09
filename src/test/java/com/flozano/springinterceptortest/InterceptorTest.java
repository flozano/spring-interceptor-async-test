package com.flozano.springinterceptortest;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import com.flozano.springinterceptortest.web.CounterInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Main.class)
@WebIntegrationTest({ "server.port=0", "management.port=0" })
public class InterceptorTest {

	@Value("${local.server.port}")
	int port;

	@Autowired
	CounterInterceptor theInterceptor;

	RestTemplate restTemplate = new TestRestTemplate();

	// ... interact with the running server
	@Test
	public void testSync() {
		restTemplate.getForObject("http://127.0.0.1:" + port + "/sync",
				String.class);
		assertEquals(0, theInterceptor.getAsyncInvocations());
		assertEquals(1, theInterceptor.getSyncInvocations());
	}

	@Test
	public void testAsync() {
		restTemplate.getForObject("http://127.0.0.1:" + port + "/async",
				String.class);
		assertEquals(1, theInterceptor.getSyncInvocations());
		assertEquals(1, theInterceptor.getAsyncInvocations());
	}

	@Before
	@After
	public void clear() {
		theInterceptor.reset();
	}
}