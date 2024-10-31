package gt.edu.usac.sun.sun_item_analysis_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SunAnalysisItemRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SunAnalysisItemRestApplication.class, args);
	}
        
        @GetMapping("/")
        public String helloWorld(){
            return "Hello world";
        }

}
