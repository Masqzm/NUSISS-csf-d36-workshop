package csf.day36.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import csf.day36.models.Post;
import csf.day36.service.FileUploadService;
import jakarta.json.Json;
import jakarta.json.JsonObject;

@Controller
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadSvc;

    private static final String BASE64_PREFIX = "data:image/png;base64,";

    @PostMapping(path="/api/upload", 
                consumes=MediaType.MULTIPART_FORM_DATA_VALUE,
                produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(@RequestPart("file") MultipartFile file,
                                        @RequestPart String comments) {
        String postId = "";
        
        try {
            postId = fileUploadSvc.upload(file, comments);
        } catch(SQLException | IOException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }

        JsonObject payload = Json.createObjectBuilder()
                                .add("postId", postId).build();

        return ResponseEntity.ok(payload.toString());
    }

    @GetMapping(path="/api/getImage/{postId}")
    public ResponseEntity<String> getImageByPostId(@PathVariable String postId) {
        Optional<Post> optPost = fileUploadSvc.getPostById(postId);

        if(optPost.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{}");

        Post p = optPost.get();

        String encodingString = Base64.getEncoder().encodeToString(p.getImage());

        JsonObject payload = Json.createObjectBuilder()
                            .add("image", BASE64_PREFIX + encodingString).build();

        return ResponseEntity.ok(payload.toString());
    }
}
