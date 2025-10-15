package entityClasses;

import java.time.LocalDateTime;

/*******
 * <p> Title: Reply Class </p>
 * 
 * <p> Description: This Reply class represents a reply entity in the system. It contains the reply
 *  details such as id, postId, and the author.. </p>
 * 
 * 
 * @author Jesus Miranda
 * 
 * 
 */ 

public class Reply {
	
	/*
	 * These are the private attributes for this entity object
	 */
    //private int id;
    private int postId;
    private String author;
    private String content;
    private LocalDateTime timestamp;
    private Boolean read;
    
    /*****
     * <p> Method: Reply(int id, int postId, String author, String content) </p>
     * 
     * <p> Description: This constructor is used to establish reply objects. </p>
     * 
     * @param id specifies the id for this reply
     * 
     * @param postId specifies the postId for this reply
     * 
     * @param author specifies the author for this reply
     * 
     * @param content specifies the text for this reply
     * 
     * 
     */
    public Reply(int id, int postId, String author, String content) {
        //this.id = id;
        this.postId = postId;
        this.author = author;
        this.content = content;
        this.timestamp = LocalDateTime.now();
        this.read = false;
    }
    
    /*****
     * <p> Method: void markRead() </p>
     * 
     * <p> Description: This method is used to mark a post as read. </p>
     * 
     */
    public void markRead() {
        this.read = true;
    }

    /*****
     * <p> Method: String getId() </p>
     * 
     * <p> Description: This getter returns the reply id. </p>
     * 
     * @return an int of reply id
     * 
     */
//    public int getId() {
//        return id;
//    }
    
    /*****
     * <p> Method: String getPostId() </p>
     * 
     * <p> Description: This getter returns the post id. </p>
     * 
     * @return an int of post id
     * 
     */
    public int getPostId() {
        return postId;
    }

    /*****
     * <p> Method: String getAuthor() </p>
     * 
     * <p> Description: This getter returns the author. </p>
     * 
     * @return a string of author
     * 
     */
    public String getAuthor() {
        return author;
    }
    
    /*****
     * <p> Method: String getContent() </p>
     * 
     * <p> Description: This getter returns the content. </p>
     * 
     * @return a String of content
     * 
     */
    public String getContent() {
        return content;
    }
    
    /*****
     * <p> Method: LocalDateTime getTimestamp() </p>
     * 
     * <p> Description: This getter returns the timestamp. </p>
     * 
     * @return a LocalDateTime of timestamp
     * 
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    /*****
     * <p> Method: Boolean isRead() </p>
     * 
     * <p> Description: This getter returns a boolean on read state of reply </p>
     * 
     * @return a boolean of state of reply
     * 
     */
    public Boolean isRead() {
        return read;
    }
}