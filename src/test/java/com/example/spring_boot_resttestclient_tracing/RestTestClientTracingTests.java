package com.example.spring_boot_resttestclient_tracing;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.tracing.test.autoconfigure.AutoConfigureTracing;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.client.RestTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@AutoConfigureTracing
class RestTestClientTracingTests {

	@Autowired
	private RestTestClient restTestClient;

	@Test
	void traceparentIsSent() {
		String traceparent = this.restTestClient.get()
			.uri("/hello")
			.exchange()
			.expectStatus()
			.isOk()
			.returnResult(String.class)
			.getResponseBody();
		assertThat(traceparent).matches("00-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}");
	}

}
