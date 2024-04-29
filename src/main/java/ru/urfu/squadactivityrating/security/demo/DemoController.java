package ru.urfu.squadactivityrating.security.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo-controller")
//@Hidden
public class DemoController {

  @GetMapping
  public ResponseEntity<String> sayHello() {
    return ResponseEntity.ok("Hello from secured endpoint WITHOUT AUTH");
  }

  @GetMapping("/with-auth")
  public ResponseEntity<String> sayHelloWithAuth() {
    return ResponseEntity.ok("Hello from secured endpoint with AUTH");
  }

}
