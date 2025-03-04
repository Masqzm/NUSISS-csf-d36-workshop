package csf.day36.repo;

import java.io.IOException;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import csf.day36.models.Post;

@Repository
public class FileUploadRepo {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private DataSource dataSource;

    private static final String INSERT_POST = "INSERT INTO posts (post_id, comments, image) VALUES (?, ?, ?)";
    //private static final String SELECT_POST_BY_ID = "SELECT * FROM posts WHERE post_id = ?";
    private static final String SQL_GET_POST_BY_ID = "SELECT post_id, comments, image FROM posts WHERE post_id = ?";    // more optimized than above

    public String upload(MultipartFile file, String comments) throws SQLException, IOException {
		String postId = UUID.randomUUID().toString().substring(0, 8);
		
		// NATIVE MTD
        //try(Connection con = dataSource.getConnection()) {
        //    PreparedStatement ps = con.prepareStatement(INSERT_POST);
        //    ps.setString(1, postId);
        //    ps.setString(2, comments);
        //    ps.setBytes(3, file.getBytes());
        //    ps.executeUpdate();
        //
        //    return postId;
        //}
		
		try {
            // Attempt to read the file bytes
            byte[] fileBytes = file.getBytes();
    
            // Use JdbcTemplate to perform the update
            template.update(INSERT_POST, ps -> {
                ps.setString(1, postId);
                ps.setString(2, comments);
                ps.setBytes(3, fileBytes);
            });
        } catch (IOException ex) {
            // Handle the IOException here (e.g., log it or rethrow a custom exception)
            throw new RuntimeException("Failed to upload file content", ex);
        }
		
		return postId;
    }

    public Optional<Post> getPostById(String postId) {
        return template.query(SQL_GET_POST_BY_ID, 
            (ResultSet rs) -> {
                if(rs.next())
                    return Optional.of(Post.populate(rs));
                else   
                    return Optional.empty();
            },
            postId
        );
    }
}
