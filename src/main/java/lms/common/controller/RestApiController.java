package lms.common.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    // 학생 권한
    @GetMapping("/student")
    public String student() {
        return "학생 페이지 입니다.";
    }

    // 교수 권한
    @GetMapping("/professor")
    public String professor() {
        return "교수 페이지 입니다.";
    }

    // 관리자 권한
    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지 입니다.";
    }

   @GetMapping("/user")
   public String user() {
	   return "유저 페이지 입니다.";
   }
  
   
}
