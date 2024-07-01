package training.grading.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.*;

@Slf4j
@Service
public class GradingServiceImpl implements GradingService {
    @Override
    public String grading(String user, String code) {
        ClassPathResource inputPath = new ClassPathResource("/input/input.txt");
        ClassPathResource outputPath = new ClassPathResource("/output/output.txt");

        File userDir = new File("src/main/resources/" + user);
        File userSolution = new File(userDir, "Solution.java");

        try {
            if(!userDir.exists()) {
                userDir.mkdirs();
            }

            /**
             * Solution.java user code 저장
             * main - Resources 폴더 - userName 폴더
             * */
            FileUtils.writeStringToFile(userSolution, code, "UTF-8");

            /**
             * user 제출 코드 컴파일 실행
             * */
            Process compileProcess = getCompile(userSolution);

            /**
             * 컴파일 성공 검증
             * */
            String message = executeCompile(compileProcess);
            if (message.equals("compile failed")) return message;

            /**
             * 사용자 제출 코드에 input 파일 넣어 실행 결과 runProcess에 기록해두기
             * */
            Process runProcess = testInputValueToUserSolution(userDir, inputPath);

            /**
             * runProcess에 기록해 둔 값 추출
             * */
            StringBuilder processOutput = getActualValue(runProcess);

            // Compare with expected output
            String incorrect = comparisonActualAndExpectedValues(outputPath, processOutput);
            if (incorrect != null) return incorrect;

        } catch(FileNotFoundException e) {
            log.info("파일을 찾을 수 없습니다.", e);
        } catch (IOException e) {
            log.info("텍스트를 불러오는 데 실패했습니다.", e);
        } catch (InterruptedException e) {
            log.info("컴파일 오류 발생", e);
        }
        return "correct";
    }

    private static String comparisonActualAndExpectedValues(ClassPathResource outputPath, StringBuilder processOutput) throws IOException {
        BufferedReader outputFileString;
        outputFileString = new BufferedReader(new InputStreamReader(outputPath.getInputStream()));
        String expectedOutputLine;
        BufferedReader processOutputReaderForComparison = new BufferedReader(new StringReader(processOutput.toString()));
        while ((expectedOutputLine = outputFileString.readLine()) != null) {
            String actualOutputLine = processOutputReaderForComparison.readLine();

            log.info("기대 값: {}", expectedOutputLine);
            log.info("실제 값: {}", actualOutputLine);

            if (!expectedOutputLine.equals(actualOutputLine)) {
                log.info("일치하지 않는 정답: 기댓값 = {}, 실제값 = {}", expectedOutputLine, actualOutputLine);
                return "incorrect";
            }
        }
        return null;
    }

    private static Process testInputValueToUserSolution(File userDir, ClassPathResource inputPath) throws IOException {
        BufferedReader inputFileString;
        Process runProcess = Runtime.getRuntime().exec("java -cp " + userDir.getAbsolutePath() + " Solution");
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(runProcess.getOutputStream()));
        inputFileString = new BufferedReader(new InputStreamReader(inputPath.getInputStream()));
        String inputLine;
        while ((inputLine = inputFileString.readLine()) != null) {
            writer.write(inputLine);
            writer.newLine();
        }
        writer.flush();
        writer.close();
        return runProcess;
    }

    private static StringBuilder getActualValue(Process runProcess) throws IOException {
        BufferedReader processOutputReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        StringBuilder processOutput = new StringBuilder();
        String processOutputLine;
        while ((processOutputLine = processOutputReader.readLine()) != null) {
            processOutput.append(processOutputLine).append("\n");
        }

        log.info("processOutput: {}", processOutput);
        return processOutput;
    }

    private static Process getCompile(File userSolution) throws IOException, InterruptedException {
        // Solution.java 컴파일
        log.info("javac " + userSolution.getAbsolutePath());
        Process compileProcess = Runtime.getRuntime().exec("javac " + userSolution.getAbsolutePath());
        compileProcess.waitFor();
        return compileProcess;
    }

    private static String executeCompile(Process compileProcess) {
        if (compileProcess.exitValue() != 0) {
            log.info("compile failed");
            return "compile failed";
        }
        return "compile success";
    }
}
