package traning.tts.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import traning.tts.service.AIService;

@RestController
@RequiredArgsConstructor
public class AIController {
    private final AIService aiService;

    private static final String RADIO_SCRIPT = "최근에 정말 재미있는 생일을 보냈어요. 동물원에서 생일 파티를 했는데, 예상치 못한 일이 벌어졌답니다!\n" +
            "생일 당일 동물원에 갔는데, 기린이 제 생일을 축하해주더라고요. 기린이 제게 다가와서 머리를 숙이더니 ‘생일 축하해!’라고 말하는 듯한 포즈를 취했어요. 그때 기린을 위해 준비된 카드도 받았는데, ‘당신의 하루가 기린처럼 높은 행복으로 가득하길 바래요!’라는 메시지가 적혀 있었죠.\n" +
            "그리고 또 코알라와 만날 기회도 있었어요. 코알라는 제가 좋아하는 동물이라서 더 좋았는데, 저를 위해 준비한 작은 선물 상자를 주었어요. 상자 안에는 코알라 인형이랑 귀여운 쪽지가 들어 있었어요. 쪽지에는 ‘생일 축하해!’라는 글이 적혀 있었죠.\n" +
            "진짜 동물들이 이렇게 직접 축하해주다니, 너무 신기하고 재밌었어요. 동물원에서 보낸 생일이 정말 특별했답니다.\n" +
            "사연 읽어주셔서 감사합니다!";

    @GetMapping("/test")
    public String test() {
        aiService.convertTextToSpeechAndSave(RADIO_SCRIPT, "C:\\Users\\SSAFY\\Documents\\example5.mp3");
        return "test";
    }

}
