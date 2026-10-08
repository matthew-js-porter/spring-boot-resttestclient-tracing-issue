package com.example.spring_boot_resttestclient_tracing;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.micrometer.tracing.test.autoconfigure.AutoConfigureTracing;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
@AutoConfigureTracing
class TestRestTemplateTracingTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void traceparentIsSent() {
		String traceparent = this.restTemplate.getForObject("/hello", String.class);
		assertThat(traceparent).matches("00-[0-9a-f]{32}-[0-9a-f]{16}-[0-9a-f]{2}");
	}

}
