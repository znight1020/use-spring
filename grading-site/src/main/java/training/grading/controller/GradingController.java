package training.grading.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import training.grading.dto.GradingData;
import training.grading.service.GradingService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GradingController {
    private final GradingService gradingService;

    @PostMapping("/grading")
    public ResponseEntity<Map<String, String>> grading(GradingData gradingData) {
        String user = gradingData.getUser();
        String code = gradingData.getCode();

        log.info("사용자 제출 코드\n{}", code);

        if(user == null || "".equals(user)){
            return new ResponseEntity<>(Map.of("result", "fail", "message", "사용자 이름 누락"), HttpStatus.BAD_REQUEST);
        }

        String result = gradingService.grading(user, code);
        if("correct".equals(result)) {
            return new ResponseEntity<>(Map.of("result", "success", "message", "맞았습니다!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Map.of("result", "fail", "message", "틀렸습니다."), HttpStatus.OK);
        }
    }

}
