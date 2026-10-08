package com.example.spring_boot_resttestclient_tracing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HelloController {

	private static final Logger logger = LoggerFactory.getLogger(HelloController.class);

	@GetMapping("/hello")
	String hello(@RequestHeader(name = "traceparent", required = false) String traceparent) {
		logger.info("traceparent: {}", traceparent);
		return (traceparent != null) ? traceparent : "none";
	}

}
