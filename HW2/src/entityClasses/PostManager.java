package entityClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/*******
 * <p> Title: PostManager Class </p>
 * 
 * <p> Description: This PostManager class stores all posts and any subset. </p>
 * 
 * 
 * @author Jesus Miranda
 * 
 * 
 */ 

public class PostManager {
	
	/*
	 * These are the private attributes for this entity object
	 */
    private List<Post> allPosts;
    private List<Post> subsetPosts;
    
    /*****
     * <p> Method: PostManager() </p>
     * 
     * <p> Description: This constructor is used to establish the PostManager. </p>
     * 
     */
    public PostManager() {
        allPosts = new ArrayList<>();
        subsetPosts = new ArrayList<>();
    }
    
    /*****
     * <p> Method: void addPost(Post post) </p>
     * 
     * <p> Description: This method is used to add a post to allPosts  </p>
     * 
     * @param a post object
     * 
     */
    public void addPost(Post post) {
        allPosts.add(post);
    }

    /*****
     * <p> Method: List<\Post> getAllPosts() </p>
     * 
     * <p> Description: This method is get all posts  </p>
     * 
     * @return List of posts
     * 
     */
    public List<Post> getAllPosts() {
        return allPosts;
    }
    
    /*****
     * <p> Method: void setSubset(List<\Post> subset) </p>
     * 
     * <p> Description: This method is set the subset  </p>
     * 
     * @param a list of posts
     * 
     */
    public void setSubset(List<Post> subset) {
        subsetPosts = subset;
    }

    /*****
     * <p> Method: List<\Post> getSubset() </p>
     * 
     * <p> Description: This method is get the subset posts  </p>
     * 
     * @return List of posts
     * 
     */
    public List<Post> getSubset() {
        return subsetPosts;
    }
    
    /*****
     * <p> Method: List<\Post> searchPosts(String keyword) </p>
     * 
     * <p> Description: This method is used to search posts for a keyword(s). </p>
     * 
     * @param a string to use for search
     * 
     * @return a List of posts that contain keyword
     * 
     */
    public List<Post> searchPosts(String keyword) {
    	List<Post> result = allPosts.stream()
    			.filter(p -> !p.isDeleted() && p.getContent().toLowerCase().contains(keyword.toLowerCase()))
    			.collect(Collectors.toList());
    	setSubset(result);
    	return result;
    }
}