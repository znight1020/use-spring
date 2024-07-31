package traning.tts;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CreateFileExample {
    public static void main(String[] args) {
        // 파일 경로 설정
        String filePath = "C:\\Users\\SSAFY\\Desktop\\example.txt";

        // 파일에 쓸 내용
        String content = "안녕하세요, 이것은 예제 파일입니다!";

        // BufferedWriter를 사용하여 파일에 내용 쓰기
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
            System.out.println("파일이 성공적으로 저장되었습니다: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
