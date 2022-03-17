package com.dt.invocing.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoicingController {

	@GetMapping("/")
	public String index() {
		return "Invocing app is currently under development! Stay tuned for updates.";
	}

}
