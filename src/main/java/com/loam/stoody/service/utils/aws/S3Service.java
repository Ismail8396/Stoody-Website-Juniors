package com.loam.stoody.service.utils.aws;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private static final long multipartUploadThreshold = 20 * 1024 * 1025;// 20 MB

    // Amazon S3 Client
    private final AmazonS3Client awsS3Client;

    // PRE-SIGNED URL METHOD--------------------------------------------------------------------------------------------
    private static final Logger LOG = LoggerFactory.getLogger(S3Service.class);

    private String generateUrl(String fileName, HttpMethod httpMethod) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); // Generated URL will be valid for 24 hours
        return awsS3Client.generatePresignedUrl(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo, fileName, calendar.getTime(), httpMethod).toString();
    }

    @Async
    public String findByName(String fileName) {
        if (!awsS3Client.doesObjectExist(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo, fileName))
            return "File does not exist";
        LOG.info("Generating signed URL for file name {}", fileName);
        return generateUrl(fileName, HttpMethod.GET);
    }

    @Async
    public String save(String extension) {
        String fileName = UUID.randomUUID().toString() + extension;
        return generateUrl(fileName, HttpMethod.PUT);
    }
    // END OF PRE-SIGNED URL METHOD-------------------------------------------------------------------------------------

    // Uploads a (multipart)file to Amazon S3 Bucket and returns URL
    public String putObject(String bucketName, MultipartFile file) throws AmazonS3Exception, SdkClientException,
            AmazonServiceException, InterruptedException, IOException {

        // Prepare a key
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID() + "." + filenameExtension;

        // Build Transfer Manager
        TransferManager transferManager = TransferManagerBuilder.standard().withS3Client(awsS3Client)
                .withMultipartUploadThreshold(multipartUploadThreshold).
                withMinimumUploadPartSize(multipartUploadThreshold).build();

        // Create Object Metadata
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        // Create a request
        PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(),metadata);

        // Send upload request and wait for completion (or an answer)
        transferManager.upload(request).waitForCompletion();

        // Make S3 Object Public
        awsS3Client.setObjectAcl(bucketName, key,
                CannedAccessControlList.PublicRead);

        // Return Uploaded Object's URL
        return awsS3Client.getResourceUrl(bucketName, key);
    }
}





















//    // TODO: Test comprehensively with all the devices that app supports.
//    private File convertMultipartFileToFile(MultipartFile multipartFile){
//        // TODO: Server may overwrite temporary files.
//        final long suffix = (long) (Math.random()*(100000000-1+1)+1);
//        File file = new File(String.format("%s%s%s","src/main/resources/targetFile",suffix,".tmp"));
//
//        try {
//            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), file);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        try (OutputStream os = new FileOutputStream(file)) {
//            os.write(multipartFile.getBytes());
//        } catch (FileNotFoundException e) {
//            file = null;
//        } catch (IOException e) {
//            file = null;
//        }
//
//        // At the end, check both
//        if(multipartFile == null || file == null) {
//            return null;
//        }
//
//        return file;
//    }

// EXAMPLE OF PAUSING AN UPLOAD
/*
        // Initialize TransferManager.
        TransferManager tm = TransferManagerBuilder.defaultTransferManager();

        // Upload a file to Amazon S3.
        Upload myUpload = tm.upload(myBucket, myKey, myFile);

        // Sleep until data transferred to Amazon S3 is less than 20 MB.
        long MB = 1024 * 1024;
        TransferProgress progress = myUpload.getProgress();
        while( progress.getBytesTransferred() < 20*MB ) Thread.sleep(2000);

        // Initiate a pause with forceCancelTransfer as true.
        // This cancels the upload if the upload cannot be paused.
        boolean forceCancel = true;
        PauseResult<PersistableUpload> pauseResult = myUpload.tryPause(forceCancel);
 */

// EXAMPLE OF CANCELLING BEFORE A TIME AGO
/*
        String existingBucketName = "*** Provide existing bucket name ***";

        TransferManager tm = TransferManagerBuilder.defaultTransferManager();

        int sevenDays = 1000 * 60 * 60 * 24 * 7;
        Date oneWeekAgo = new Date(System.currentTimeMillis() - sevenDays);

        try {
            tm.abortMultipartUploads(existingBucketName, oneWeekAgo);
        } catch (AmazonClientException amazonClientException) {
            System.out.println("Unable to upload file, upload was aborted.");
            amazonClientException.printStackTrace();
        }
 */

// EXAMPLE OF PUTTING AN OBJECT WITH S3Client DIRECTLY
/*
//        ObjectMetadata metadata = new ObjectMetadata();
//        metadata.setContentLength(file.getSize());
//        metadata.setContentType(file.getContentType());
//
//        try {
//            awsS3Client.putObject(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo, key, file.getInputStream(), metadata);
//        }catch(IOException e){
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
//                    "An exception occured while uploading a file into Amazon S3 Bucket named:"
//                            + S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo);
//        }
 */

// EXAMPLE OF KEY PREPARATION (file type is MultipartFile)
/*
        // Prepare a key
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

        String key = UUID.randomUUID() + "." + filenameExtension;
 */

// EXAMPLE OF SETTING BUCKET OBJECT PUBLIC
/*

        awsS3Client.setObjectAcl(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo,key,
                CannedAccessControlList.PublicRead);
 */

// EXAMPLE OF GETTING BUCKET OBJECT URL
/*
        // File URL
        return awsS3Client.getResourceUrl(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo,key);
 */
// EXAMPLE OF WAITING THE UPLOADED DATA TO BE AT LEAST 20 BYTES
/*
        // Sleep until data transferred to Amazon S3 is less than 20 MB.
        long MB = 1024 * 1024;
        TransferProgress progress = myUpload.getProgress();
        // TO-DO: Is it good idea either to keep it below 20 MB or using Thread.sleep(n) here?
        while( progress.getBytesTransferred() < 20*MB )
            try {
                Thread.sleep(2000);
            }catch(Exception e){
                System.err.println("Thread could not be waited to reach the upload at least 20 MB while pausing the upload! Returning null as PauseResult...");
                return null;
            }
 */

// EXAMPLE OF TRACKING PROCESS (NOT THE IDEAL WAY)
/*
ProgressListener progressListener = progressEvent -> System.out.println(
  "Transferred bytes: " + progressEvent.getBytesTransferred());
PutObjectRequest request = new PutObjectRequest(
  bucketName, keyName, file);
request.setGeneralProgressListener(progressListener);
Upload upload = tm.upload(request);
 */

// EXAMPLE OF SOME POSSIBLE UPLOAD METHODS
/*
    private static final long multipartUploadThreshold = 100 * 1024 * 1025;// 100 MB

    // Driver Data
    private TransferManager tm = null;
    PauseResult<PersistableUpload> persistableUploadPauseResult = null;
    private Upload currentUpload = null;

    // Uploads a (multipart)file to Amazon S3 Bucket and returns URL
    public void requestUploadFile(MultipartFile file){
        // Prepare a key
        String filenameExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        String key = UUID.randomUUID() + "." + filenameExtension;

        fileUploadState = FUS_RUNNING;

        tm = TransferManagerBuilder.standard().withS3Client(awsS3Client)
                .withMultipartUploadThreshold(multipartUploadThreshold).withMinimumUploadPartSize(multipartUploadThreshold).build();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        PutObjectRequest request = null;
        try {
            request = new PutObjectRequest(S3BucketDetails.S3BucketNameStoodyTeacherCourseVideo, key, file.getInputStream(),metadata);

            // Listener
            ProgressListener progressListener = (progressEvent) -> {
                System.out.println("Transferred bytes: " + progressEvent.getBytesTransferred());

                if(currentUpload != null)// TODO: When upload is finished, set all values to default ones
                        System.out.println(currentUpload.getProgress().getPercentTransferred());
            };
            request.setGeneralProgressListener(progressListener);

        } catch (IOException e) {
            System.err.println("PutObjectRequest failed!");
            throw new RuntimeException(e);
        }

        currentUpload = tm.upload(request);
    }

    public PauseResult<PersistableUpload> pauseUpload(boolean forceCancel){
        if(fileUploadState != FUS_RUNNING) {
            System.err.println("No file is uploading to be paused currently! Returning null as PauseResult...");
            return null;
        }

        // Initiate a pause with forceCancelTransfer as true.
        // This cancels the upload if the upload cannot be paused.
        persistableUploadPauseResult = currentUpload.tryPause(forceCancel);

        // TODO: What if could not be paused(if force cancel is not true and file cannot be paused?)
        fileUploadState = FUS_PAUSED;

        // TODO: REMOVE THIS COMMENT AND MESSAGE LATER
        System.out.println("Pause upload is successfully executed");

        return persistableUploadPauseResult;
    }

    @Override
    public boolean resumeUpload() {
        if(fileUploadState != FUS_PAUSED) {
            System.err.println("Found no file to be paused! Returning false as resumeUpload() result...");
            return false;
        }

        if(persistableUploadPauseResult.getPauseStatus() == PauseStatus.SUCCESS) {
            tm.resumeUpload(persistableUploadPauseResult.getInfoToResume());

        }
        else
            System.out.println(persistableUploadPauseResult.getPauseStatus().toString());

        fileUploadState = FUS_RUNNING;

        // TODO: REMOVE THIS COMMENT AND MESSAGE LATER
        System.out.println("Resume upload is successfully executed");
        return true;
    }
 */