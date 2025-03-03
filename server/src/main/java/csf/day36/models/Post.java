package csf.day36.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Post {
    private String postId;
    private String comments;
    private byte[] image;

    public Post() {}
    public Post(String postId, String comments, byte[] image) {
        this.postId = postId;
        this.comments = comments;
        this.image = image;
    }

    public static Post populate(ResultSet rs) throws SQLException {
        Post p = new Post(rs.getString("post_id"),
                        rs.getString("comments"),
                        rs.getBytes("image"));

        return p;
    }

    public String getPostId() {
        return postId;
    }
    public void setPostId(String postId) {
        this.postId = postId;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
}
