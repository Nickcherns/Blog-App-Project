package com.techtalentsouth.techtalentblog.blogposts;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BlogPostController {
	// we cant initialize this, we can't construct this..
	// so we ask SpringBoot to magically fill this value in when our 
	//		BlogPostCOnstroller is created
	// we do this by adding the '@Autowired' annotation
	@Autowired
	private BlogPostRepository blogPostRepository;
		
	/* 	A request mapping as written here responds to all HTTP
	 * 		requests
	 * 
	 * GET/PUT/POST/DELETE/PATCH -- all will be processed
	 * 		by this mapping. We can specify that we only want
	 * 		to respond to GET methods by adding a second
	 * 		parameter labeled 'method' and saying we respond to
	 * 		RequestMethod.GET
	 */
	 // @RequestMapping(path="/", method= RequestMethod.GET)
	
	 // this does same as commented line above
	/* @GetMapping
	 * @PostMapping
	 * @PutMapping
	 * 	-- these can be stacked as well 
	 */
	

	@GetMapping(path="/") 
	public String index(Model model) {
		// since we are using the @Controller annotation rather
		//   than the @RestController annotation, the String we are returning
		//   is actually a reference to a HTML template page
		
		// the model variable is a lot like a HashMap, except all the keys
		//		have to be strings
		
		List<BlogPost> posts = new ArrayList<>();
		for(BlogPost post: blogPostRepository.findAll()) {
			posts.add(post);
		};
		model.addAttribute("posts", posts);
		return "blogpost/index";
		
		//when we return a string from a @Controller endpoint,
		//   the string we return is a path from
		//  "src/main/resources/templates" to a HTML template file
		
	}
		
	@GetMapping(path="/blogposts/new")
	public String newBlog(Model model) {
		model.addAttribute("blogPost", new BlogPost());
		return "blogpost/new";
	}
		
	@PostMapping(path="/blogposts")
	public String addNewBlogPost(BlogPost blogPost, Model model) {
		// we can now write the code to save the blogPost to the database
		// we will need a BlogPost Repository == its an interface,
		//   we can't create it, only SpringBoot can create it
		blogPostRepository.save(blogPost);
		model.addAttribute("blogPost", blogPost);
		System.out.println("---------------------------------");
		System.out.println(blogPost);
		
		return "blogpost/result";
	}
	
	@GetMapping(path="/blogposts/{id}") 
	public String editPostWithId(@PathVariable Long id, Model model) {
		// we can now load in the block post that has that id
		Optional<BlogPost> postBox = blogPostRepository.findById(id);
		if(postBox.isPresent()) {
			BlogPost post = postBox.get();
			model.addAttribute("blogPost", post);
		}
		return "blogpost/edit";
	}
	
	@PostMapping(path="/blogposts/{id}")
	public String updateExistingPost(@PathVariable Long id, BlogPost blogPost, Model model) {
		Optional<BlogPost> postBox = blogPostRepository.findById(id);
		if(postBox.isPresent()) {
			BlogPost actualPost = postBox.get();
			actualPost.setTitle(blogPost.getTitle());
			actualPost.setAuthor(blogPost.getAuthor());
			actualPost.setBlogEntry(blogPost.getBlogEntry());
			blogPostRepository.save(actualPost); // update the existing blogPost
			// this is an update, rather than creating a new database entry 
			//  because actualPost has the 'id' field set to the primary key of an existing db record.
			model.addAttribute("blogPost", actualPost);
		}
		return "blogpost/result";
	}
	
	@GetMapping(path="/blogposts/delete/{id}")
	public String deletePostById(@PathVariable Long id) {
		blogPostRepository.deleteById(id);
		return "blogpost/delete";
	}
	
	
}

