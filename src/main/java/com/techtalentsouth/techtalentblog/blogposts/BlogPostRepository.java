package com.techtalentsouth.techtalentblog.blogposts;

import org.springframework.data.repository.CrudRepository;

// how do we signal to the JPA that this is a repository?
// we do signal a lot of things like that something
//  is a controller by using annotation like @Controller

// but in this case we signify that this is a repository
// 		by what it inherits from
public interface BlogPostRepository extends CrudRepository<BlogPost, Long> {
	
}
