package sparta.code3line.domain.file;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sparta.code3line.common.CommonResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/filetest")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<CommonResponse<List<String>>> uploadFile(@RequestPart(value = "file", required = false) List<MultipartFile> multipartFiles){

        CommonResponse<List<String>> response = new CommonResponse<>(
                "업로드 성공 🎉",
                201,
                fileService.uploadFile(multipartFiles)
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponse<String>> deleteFile(@RequestParam String fileUrl){

        CommonResponse<String> response = new CommonResponse<>(
                "삭제 성공 🎉",
                204,
                 fileService.deleteFile(fileUrl)
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);

    }
}
