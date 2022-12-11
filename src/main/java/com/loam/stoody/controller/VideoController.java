//package com.loam.stoody.controller;
//
//import com.loam.stoody.service.VideoService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//@RestController
//@RequestMapping("/stoody/collections/videos")
//@RequiredArgsConstructor
//public class VideoController {
//    private final VideoService videoService;
//
//    // Testing purposes
////    @PostMapping("/uploadVideo")
////    @ResponseStatus(HttpStatus.CREATED)
////    public void uploadVideo(@RequestParam("file")MultipartFile file){
////        videoService.uploadVideo(file);
////    }
//
//    @RequestMapping("/index")
//    public ModelAndView hello() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("test.html");
//        return modelAndView;
//    }
//
//    @PostMapping("/upload")
//    public ResponseEntity<?> handleFileUpload( @RequestParam("file") MultipartFile file ) {
//
//        String fileName = file.getOriginalFilename();
//        try {
//            videoService.uploadVideo("test","best",file,null);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok("File uploaded successfully.");
//    }
//}

package com.loam.stoody.controller;

import com.loam.stoody.service.VideoService;
import com.loam.stoody.service.aws.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.sound.midi.Soundbank;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
@RequiredArgsConstructor
public class VideoController {
    private final VideoService videoService;

    @Autowired
    // TEST
    private S3Service s3Service;
    private static final String FILE_NAME = "fileName";

    @GetMapping
    public ResponseEntity<Object> findByName(HttpServletRequest request, @RequestBody(required = false) Map<String, String> params) {
        final String path = request.getServletPath();
        if (params.containsKey(FILE_NAME))
            return new ResponseEntity<>(s3Service.findByName(params.get(FILE_NAME)), HttpStatus.OK);
        return null;
    }

    @PostMapping
    public ResponseEntity<Object> saveFile(@RequestParam("extension") String extension) {
        return new ResponseEntity<>(s3Service.save(extension), HttpStatus.OK);
    }

    @PostMapping("/create")
    public void createVideoTest(){
        videoService.createTestVideo();
    }
    //------------------------------------------------------------
//
//    @GetMapping("/index")
//    public ModelAndView hello() {
//        ModelAndView mw = new ModelAndView();
//        mw.setViewName("test.html");
//        return mw;
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public void uploadVideo(@RequestParam("file")MultipartFile file){
//        videoService.uploadVideo(file);
//        System.out.println("Called");
//    }
//
//    @PostMapping("/upload")
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file ) {
//
//        try {
//            System.out.println("Upload started");
//            videoService.uploadVideo(file);
//            System.out.println("Upload finished");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//        return ResponseEntity.ok("File uploaded successfully.");
//    }
//
//    @GetMapping("/pause")
//    public ModelAndView pauseTest(){
//        System.out.println("Pause called");
//
//        ModelAndView mw = new ModelAndView();
//        mw.setViewName("login.html");
//
//        // Test pause
//        videoService.pauseTest();
//
//        System.out.println("Pause end");
//        return mw;
//    }
//
//    @GetMapping("/resume")
//    public ModelAndView resumeTest(){
//        System.out.println("Resume called");
//
//        ModelAndView mw = new ModelAndView();
//        mw.setViewName("login.html");
//
//        // Test pause
//        videoService.resumeTest();
//
//        System.out.println("Resume end");
//        return mw;
//    }
}
