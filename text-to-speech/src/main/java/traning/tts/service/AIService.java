package traning.tts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioSpeechOptions;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.ai.openai.audio.speech.SpeechResponse;
import org.springframework.stereotype.Service;
import traning.tts.PromptUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIService {
    private final ChatClient chatClient;
    private final OpenAiAudioSpeechModel speechModel;
    private final OpenAiAudioSpeechOptions speechOptions;
    public void convertTextToSpeechAndSave(String text, String filePath) {
        ChatResponse chatResponse = chatClient.prompt().user(PromptUtils.INIT_PROMPT + text).call().chatResponse();
        AssistantMessage chatOutput = chatResponse.getResult().getOutput();
        String toText = chatOutput.getContent();

        SpeechPrompt speechPrompt = new SpeechPrompt(toText, speechOptions);
        SpeechResponse audioResponse = speechModel.call(speechPrompt);

        byte[] responseAsBytes = audioResponse.getResult().getOutput();
        InputStream audioStream = new ByteArrayInputStream(responseAsBytes);

        try {
            saveToFile(audioStream, filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveToFile(InputStream inputStream, String filePath) throws IOException {
        File file = new File(filePath);
        try (FileOutputStream outputStream = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) outputStream.write(buffer, 0, bytesRead);
        }
    }
}