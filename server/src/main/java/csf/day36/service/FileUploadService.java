package csf.day36.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import csf.day36.models.Post;
import csf.day36.repo.FileUploadRepo;

@Service
public class FileUploadService {
    @Autowired
    private FileUploadRepo fileUploadRepo;

    public String upload(MultipartFile file, String comments) throws SQLException, IOException {
        return fileUploadRepo.upload(file, comments);
    }
    
    public Optional<Post> getPostById(String postId) {
        return fileUploadRepo.getPostById(postId);
    }
}
