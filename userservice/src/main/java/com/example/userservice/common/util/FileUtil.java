package com.example.userservice.common.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.userservice.common.exception.FileValidationException;

public class FileUtil {

    private static final List<String> SUPPORTED_EXTENSIONS = List.of(
            "jpg", "jpeg", "png", "gif", "bmp", "tiff", "webp", "svg", "ico", "raw");

    private static final List<String> RESERVED_FILENAMES = List.of(
            "CON", "PRN", "AUX", "NUL",
            "COM1", "COM2", "COM3", "COM4", "COM5", "COM6", "COM7", "COM8", "COM9",
            "LPT1", "LPT2", "LPT3", "LPT4", "LPT5", "LPT6", "LPT7", "LPT8", "LPT9");

    private static final String RESERVED_UNIX_CHARS = "[/\\0:]"; // /, \0, : 등

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    public static Path saveFile(@NonNull Path targetDirPath, @NonNull MultipartFile file) throws IOException {
        // 파일 유효성 검사
        validateFile(file);

        // 디렉토리 생성
        Files.createDirectories(targetDirPath);

        // 파일 이름과 확장자 분리
        String originalFileName = file.getOriginalFilename();
        String fileNameWithoutExt = getFileBaseName(originalFileName);
        String fileExtension = getFileExtension(originalFileName);

        // 파일 경로와 이름이 중복되면 "(1)", "(2)" 방식으로 이름 변경
        Path finalTargetPath = getUniqueFilePath(targetDirPath, fileNameWithoutExt, fileExtension);

        // 파일 저장
        Files.copy(file.getInputStream(), finalTargetPath, StandardCopyOption.COPY_ATTRIBUTES);

        // 저장된 파일의 완전한 경로 반환 (경로 구분자 자동 처리)
        return finalTargetPath;
    }

    public static void validateFile(@NonNull MultipartFile file) {
        if (file == null || file.isEmpty())
            throw new FileValidationException("파일이 전달되지 않았습니다.");

        // 파일 크기검사
        if (file.getSize() > MAX_FILE_SIZE)
            throw new FileValidationException("파일 크기가 10MB를 초과했습니다.");

        // 파일 이름 검사
        String fileName = file.getOriginalFilename();
        if (fileName == null || fileName.isEmpty())
            throw new FileValidationException("파일 이름이 존재하지 않습니다.");

        // 빈 파일 이름 검사
        if (fileName.trim().isEmpty())
            throw new FileValidationException("파일 이름이 비어있습니다.");

        // 디렉토리 트래버설 방지
        if (fileName.contains(".."))
            throw new FileValidationException("잘못된 파일 이름입니다. " + fileName);

        // 파일 확장자 검사
        String extention = getFileExtension(fileName);
        if (!SUPPORTED_EXTENSIONS.contains(extention.toLowerCase()))
            throw new FileValidationException("지원하지 않는 파일 형식입니다. " + SUPPORTED_EXTENSIONS + " 파일만 업로드 가능합니다.");

        // 파일 이름 길이 검사
        if (fileName.length() > 255)
            throw new FileValidationException("파일 이름이 너무 깁니다.");

        // 파일 이름에 특수문자 검사
        if (!fileName.matches("^[a-zA-Z0-9가-힣._-]*$"))
            throw new FileValidationException("파일 이름에 특수문자가 포함되어 있습니다.");

        // 파일 이름에 '/' 또는 '\' 포함 여부 검사
        if (fileName.contains("/") || fileName.contains("\\"))
            throw new FileValidationException("파일 이름에 슬래시(/, \\) 문자를 포함할 수 없습니다.");

        // 파일 이름이 Windows 예약어인지 검사 (확장자 제거 후 확인)
        String nameWithoutExt = getFileBaseName(fileName);
        if (RESERVED_FILENAMES.contains(nameWithoutExt.toUpperCase()))
            throw new FileValidationException("예약된 파일 이름은 사용할 수 없습니다.");

        // Unix 시스템에서 사용할 수 없는 특수 문자 검사 (널 문자, 콜론 등)
        if (fileName.matches(".*[" + Pattern.quote(RESERVED_UNIX_CHARS) + "].*"))
            throw new FileValidationException("파일 이름에 시스템에서 사용할 수 없는 문자가 포함되어 있습니다.");

    }

    public static String getFileBaseName(String filePath) {
        String baseName = StringUtils.getFilename(filePath);
        return StringUtils.hasText(baseName) ? baseName : "";
    }

    public static String getFileExtension(String filePath) {
        String extention = StringUtils.getFilenameExtension(filePath);
        return StringUtils.hasText(extention) ? extention : "";
    }

    public static MediaType getContentType(String filePath) {
        String extension = getFileExtension(filePath);

        if (!StringUtils.hasText(extension))
            return MediaType.APPLICATION_OCTET_STREAM;  // 확장자가 없으면 기본값 반환

        return switch (extension.toLowerCase()) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            case "gif" -> MediaType.IMAGE_GIF;
            case "webp" -> MediaType.valueOf("image/webp"); // WebP 지원 추가
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    public static Path getUniqueFilePath(Path targetDirPath, String baseName, String extension) throws IOException {
        Path targetPath = targetDirPath.resolve(baseName + "." + extension);
        AtomicInteger counter = new AtomicInteger(1);

        // 중복된 파일 이름이 있을 경우 "(1)", "(2)" 방식으로 이름 변경
        while (Files.exists(targetPath)) {
            String newFileName = baseName + "(" + counter.getAndIncrement() + ")" + extension;
            targetPath = targetDirPath.resolve(newFileName);
        }

        return targetPath;
    }

}
