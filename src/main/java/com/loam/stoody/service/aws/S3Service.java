package com.loam.stoody.service.aws;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.loam.stoody.service.utils.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {

    // TODO: Move into constant-s class
    final String S3BucketNameStoodyTeacherCourseVideo = "stoody-standard-teacher-course-video";

    // Amazon S3 Client
    private final AmazonS3Client awsS3Client;

    // Uploads a (multipart)file to Amazon S3 Bucket and returns URL
    public String uploadFile(/*String bucketName : TODO: IMPLEMENT LATER!*/ MultipartFile file){

        // Prepare a key
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String key = UUID.randomUUID().toString() + "." + filenameExtension;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            awsS3Client.putObject(S3BucketNameStoodyTeacherCourseVideo, key, file.getInputStream(), metadata);
        }catch(IOException e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "An exception occured while uploading a file into Amazon S3 Bucket named:"
                            + S3BucketNameStoodyTeacherCourseVideo);
        }

        awsS3Client.setObjectAcl(S3BucketNameStoodyTeacherCourseVideo,key,
                CannedAccessControlList.PublicRead);

        // File URL
        return awsS3Client.getResourceUrl(S3BucketNameStoodyTeacherCourseVideo,key);
    }
}
