package entityClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*******
 * <p> Title: ReplyManager Class </p>
 * 
 * <p> Description: This ReplyManager class stores all replies and any subset. </p>
 * 
 * 
 * @author Jesus Miranda
 * 
 * 
 */ 

public class ReplyManager {
	
	/*
	 * These are the private attributes for this entity object
	 */
	private List<Reply> allReplies;
    private List<Reply> subsetReplies;
    
    /*****
     * <p> Method: ReplyManager() </p>
     * 
     * <p> Description: This constructor is used to establish the ReplyManager. </p>
     * 
     */
    public ReplyManager() {
        allReplies = new ArrayList<>();
        subsetReplies = new ArrayList<>();
    }
    
    /*****
     * <p> Method: void addReply(Reply reply) </p>
     * 
     * <p> Description: This method is used to add a reply to allReplies  </p>
     * 
     * @param a Reply object
     * 
     */
    public void addReply(Reply reply) {
        allReplies.add(reply);
    }

    /*****
     * <p> Method: List<\Reply> getAllReplies() </p>
     * 
     * <p> Description: This method is get all replies  </p>
     * 
     * @return List of replies
     * 
     */
    public List<Reply> getAllReplies() {
        return allReplies;
    }
    
    /*****
     * <p> Method: void setSubset(List<\Reply> subset) </p>
     * 
     * <p> Description: This method is set the subset  </p>
     * 
     * @param a list of replies
     * 
     */
    public void setSubset(List<Reply> subset) {
        subsetReplies = subset;
    }

    /*****
     * <p> Method: List<\Reply> getSubset() </p>
     * 
     * <p> Description: This method is get the subset posts  </p>
     * 
     * @return List of replies
     * 
     */
    public List<Reply> getSubset() {
        return subsetReplies;
    }
    
    /*****
     * <p> Method: List<\Reply> searchReplies(String keyword) </p>
     * 
     * <p> Description: This method is used to search replies for a keyword(s). </p>
     * 
     * @param a string to use for search
     * 
     * @return a List of replies that contain keyword
     * 
     */
    public List<Reply> searchReplies(String keyword) {
        List<Reply> result = allReplies.stream()
                .filter(r -> r.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        setSubset(result);
        return result;
    }
    
    /*****
     * <p> Method: List<\Reply> getRepliesForPost(int postId) </p>
     * 
     * <p> Description: This method is used get replies for a specific post. </p>
     * 
     * @param an int of a postId
     * 
     * @return a List of replies within the post
     * 
     */
    public List<Reply> getRepliesForPost(int postId) {
        return allReplies.stream()
                .filter(r -> r.getPostId() == postId)
                .collect(Collectors.toList());
    }
}