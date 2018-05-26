package hello;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Healthz {
  @RequestMapping("/healthz")
  public String home(){
      return null;
    }
}
