package sparta.code3line.domain.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sparta.code3line.common.exception.CustomException;
import sparta.code3line.common.exception.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private static final long MAX_IMAGE_SIZE = 10 * 1024 * 1024; //10mb
    private static final long MAX_VIDEO_SIZE = 200 * 1024 * 1024; //200mb

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;

    public List<String> uploadFile(List<MultipartFile> multipartFiles){

        List<String> fileUrlList = new ArrayList<>();

        // forEach 구문을 통해 multipartFiles 리스트로 넘어온 파일들을 순차적으로 fileNameList 에 추가
        multipartFiles.forEach(file -> {
            String originalFilename = file.getOriginalFilename();
            String fileName = createFileName(originalFilename);
            String extension = getFileExtension(originalFilename);

            ObjectMetadata objectMetadata = new ObjectMetadata();

            validateFileSize(file.getSize(), extension);
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try(InputStream inputStream = file.getInputStream()){
                amazonS3.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e){
                throw new CustomException(ErrorCode.PUT_OBJECT_EXCEPTION);
            }

            fileUrlList.add(amazonS3.getUrl(bucket, fileName).toString());
        });

        return fileUrlList;
    }

    // 먼저 파일 업로드시, 파일명을 난수화하기 위해 UUID 를 활용하여 난수를 돌린다.
    private String createFileName(String fileName){
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    //
    private String getFileExtension(String fileName){
        try{
            validateImageFileExtension(fileName);
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e){
            throw new CustomException(ErrorCode.FILE_NAME_INVALID);
        }
    }

    private void validateImageFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex == -1) {
            throw new CustomException(ErrorCode.EXTENSION_IS_EMPTY);
        }

        String extension = filename.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtentionList = Arrays.asList("jpg", "jpeg", "png", "avi", "mp4", "gif");

        if (!allowedExtentionList.contains(extension)) {
            throw new CustomException(ErrorCode.EXTENSION_INVALID);
        }
    }

    private void validateFileSize(long size, String extension) {
        if (isImageFile(extension) && size > MAX_IMAGE_SIZE) {
            throw new IllegalArgumentException("이미지 파일 크기는 10MB를 초과할 수 없습니다.");
        } else if (isVideoFile(extension) && size > MAX_VIDEO_SIZE) {
            throw new IllegalArgumentException("비디오 파일 크기는 200MB를 초과할 수 없습니다.");
        }
    }

    private boolean isImageFile(String extension) {
        return extension.equals("jpeg") || extension.equals("jpg") || extension.equals("png");
    }

    private boolean isVideoFile(String extension) {
        return extension.equals("mp4") || extension.equals("avi") || extension.equals("gif");
    }

    public String deleteFile(String fileUrl){
        String key = getKeyFromFileAddress(fileUrl);
        log.info(key);
        try{
            amazonS3.deleteObject(new DeleteObjectRequest(bucket, key));
            return null;
        }catch (Exception e){
            e.printStackTrace();
            throw new CustomException(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }

    private String getKeyFromFileAddress(String fileAddress){
        try{
            URL url = new URL(fileAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), StandardCharsets.UTF_8);
            return decodingKey.substring(1); // 맨 앞의 '/' 제거
        }catch (MalformedURLException e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.IO_EXCEPTION_ON_IMAGE_DELETE);
        }
    }
}
