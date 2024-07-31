package traning.tts.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.api.OpenAiAudioApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import traning.tts.PromptUtils;

@Configuration
public class AIConfig {
    @Value("${spring.ai.openai.api-key}")
    private String openAiApiKey;
    // ================================= OpenAI ChatGTP Config =================================
    @Bean
    public ChatClient chatClient(ChatClient.Builder builder) {
        return builder.defaultSystem(PromptUtils.INIT_PROMPT).build();
    }

    // ================================= OpenAI TTS Config =================================
    @Bean
    public OpenAiAudioApi openAiAudioApi() {
        return new OpenAiAudioApi(openAiApiKey);
    }

    @Bean
    public OpenAiAudioSpeechModel openAiAudioSpeechModel(OpenAiAudioApi openAiAudioApi) {
        return new OpenAiAudioSpeechModel(openAiAudioApi);
    }

    @Bean
    OpenAiAudioSpeechOptions openAiAudioSpeechOptions() {
        return OpenAiAudioSpeechOptions.builder()
                .withVoice(OpenAiAudioApi.SpeechRequest.Voice.SHIMMER)
                .withSpeed(1.5f)
                .withResponseFormat(OpenAiAudioApi.SpeechRequest.AudioResponseFormat.MP3)
                .withModel(OpenAiAudioApi.TtsModel.TTS_1_HD.value)
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
