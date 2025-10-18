package entityClasses;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: Post Class </p>
 * 
 * <p> Description: This Post class represents a post entity in the system. It contains the post
 *  details such as id, author, and the content.. </p>
 * 
 * 
 * @author Jesus Miranda
 * 
 * 
 */ 

public class Post {
	
	/*
	 * These are the private attributes for this entity object
	 */
	private int id;
    private String author;
    private String title;
    private String content;
    private String thread;
    private LocalDateTime timestamp;
    private List<Integer> replyIds;
    private Boolean deleted;
    
    /*****
     * <p> Method: Post(String author, String title String content, String thread) </p>
     * 
     * <p> Description: This constructor is used to establish post objects. </p>
     * 
     * 
     * @param author specifies the author for this post
     * 
     * @param title specifies the title for this post
     * 
     * @param content specifies the text for this post
     * 
     * @param thread specifies the thread for this post
     * 
     */
    public Post(String author, String title, String content, String thread) {
        // this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.thread = thread != null ? thread : "General";
        this.timestamp = LocalDateTime.now();
        this.replyIds = new ArrayList<>();
        this.deleted = false;
    }
    
    /*****
     * <p> Method: Post(int id, String author, String title, String content, String thread, LocalDateTime timestamp, boolean deleted) </p>
     * 
     * <p> Description: This constructor is read from the database. </p>
     * 
     * 
     * @param id specifies the id for this post
     * @param author specifies the author for this post
     * @param title specifies the title for this post
     * @param content specifies the text for this post
     * @param thread specifies the thread for this post
     * @param timestamp specifies the timestamp for this post
     * @param deleted specifies the deletion state of this post
     * 
     */
    public Post(int id, String author, String title, String content, String thread, LocalDateTime timestamp, boolean deleted) {
    this.id = id;
    this.author = author;
    this.title = title;
    this.content = content;
    this.thread = thread;
    this.timestamp = timestamp;
    this.deleted = deleted;
}
    
    /*****
     * <p> Method: void addReply(int replyId) </p>
     * 
     * <p> Description: This method is used to add a reply to post. </p>
     * 
     * @param an int of replyId
     * 
     */
    public void addReply(int replyId) {
        replyIds.add(replyId);
    }
    
    /*****
     * <p> Method: void markDeleted() </p>
     * 
     * <p> Description: This method is used to mark a post as Deleted. </p>
     * 
     */
    public void markDeleted() {
        this.deleted = true;
    }

    /*****
     * <p> Method: String getId() </p>
     * 
     * <p> Description: This getter returns the post id. </p>
     * 
     * @return an int of id
     * 
     */
//    public int getId() {
//        return id;
//    }

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
     * <p> Method: String getTitle() </p>
     * 
     * <p> Description: This getter returns the title. </p>
     * 
     * @return a string of title
     * 
     */
    public String getTitle() {
        return title;
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
        return deleted ? "[Deleted]" : content;
    }
    
    /*****
     * <p> Method: String getThread() </p>
     * 
     * <p> Description: This getter returns the thread. </p>
     * 
     * @return a String of thread
     * 
     */
    public String getThread() {
        return thread;
    }
    
    /*****
     * <p> Method: LocalDateTime getTimestamp() </p>
     * 
     * <p> Description: This getter returns the timestamp. </p>
     * 
     * @return a LocalDateTime of timestamp
     * 
     */
    public String getTimestamp() {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yy hh:mm a");
        return timestamp.format(formatter);
    }
    
    /*****
     * <p> Method: List<\Integer> getReplyIds() </p>
     * 
     * <p> Description: This getter returns the reply ids. </p>
     * 
     * @return a List of replies ids
     * 
     */
    public List<Integer> getReplyIds() {
        return replyIds;
    }
    
    /*****
     * <p> Method: Boolean isDeleted() </p>
     * 
     * <p> Description: This getter returns a boolean on deletion state of post </p>
     * 
     * @return a boolean of state of post
     * 
     */
    public Boolean isDeleted() {
        return deleted;
    }
}