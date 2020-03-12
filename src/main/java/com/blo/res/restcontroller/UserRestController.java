package com.blo.res.restcontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.blo.res.entity.User;
import com.blo.res.exceptionhandlers.UserNotFoundException;
import com.blo.res.service.UserService;

@RestController
@RequestMapping(value = "/api/v1")
public class UserRestController {

	@Autowired
	UserService userService;

	/* GETS */
	@GetMapping(value = "/users" )
	public List<User> getAllUsers() {
		List<User>allUsers= this.userService.getAllUsers();
		// Creating links as per the hateoas principle.
        for (User user : allUsers) {
          user.add(linkTo(UserRestController.class).slash("users").slash(user.getId()).withSelfRel());
        }
        return allUsers;
	}

	@GetMapping(value="/users/{id}")
	public ResponseEntity<User> getUserById(@PathVariable(value = "id") int id) {
		User user = this.userService.findUserbyID(id); 
		if(user==null) throw new UserNotFoundException();
		//implementing HATEOAS self link here
		 Link selfLink = linkTo(UserRestController.class).slash("users").slash(user.getId()).withSelfRel();
		 user.add(selfLink);
		return new ResponseEntity<>(user,HttpStatus.OK);
	}

	/* POSTS */
	@PostMapping(value = "/users")
	public ResponseEntity<Object> postUser(@Valid @RequestBody User user) { 
		User createdUser = this.userService.createUser(user);
		//System.out.println(createdUser);
		return new ResponseEntity<>(HttpStatus.CREATED);
	}

	/* PUT */
	@PutMapping("/users/{id}")
	public ResponseEntity<User> updateUser(@Valid @RequestBody User newUser, @PathVariable (value = "id")  int id) {
		
		User user = userService.findUserbyID(id); 
		if(user==null) throw new UserNotFoundException();
		//if(newUser.getUsername()!=null)
		user.setUsername(newUser.getUsername());
		//user.setPassword(newUser.getPassword()); // no Setting password
		//if(newUser.getUser_type()!=0)
		user.setUser_type(newUser.getUser_type());
		User updatedUser = userService.createUser(user);
		//implementing HATEOAS self link here
		 Link selfLink = linkTo(UserRestController.class).slash("users").slash(user.getId()).withSelfRel();
		 user.add(selfLink);
		return ResponseEntity.ok(updatedUser);
	}
	
	/* PATCH */
	@PatchMapping("/users/{id}")
	public ResponseEntity<User> patchUser(@RequestBody User newUser, @PathVariable /* (value = "id") */ int id) {
		
		User user = userService.findUserbyID(id); 
		if(user==null) throw new UserNotFoundException();
		
		if(newUser.getUsername()!=null)user.setUsername(newUser.getUsername());
		
		//user.setPassword(newUser.getPassword()); // no Setting password
		
		if(newUser.getUser_type()>2) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);//throw bad request
		
		//if(newUser.getUser_type()!=0 )
		if(newUser.getUser_type()>0 && newUser.getUser_type()<3)//doing user_type input check here because @Valid wasnt added to this method
		{user.setUser_type(newUser.getUser_type());}
		User updatedUser = userService.createUser(user);
		//implementing HATEOAS self link here
		 Link selfLink = linkTo(UserRestController.class).slash("users").slash(user.getId()).withSelfRel();
		 user.add(selfLink);
		return ResponseEntity.ok(updatedUser);
	}
	
	/*	DELETE	*/
	 @DeleteMapping(value="/users/{id}")
	  public Map<String, Link> deleteUser(@PathVariable(value = "id") int id){
	    User user = userService.findUserbyID(id);
	    if (user==null) throw new UserNotFoundException();
	    userService.deleteUser(user);
	  //implementing HATEOAS GET allUsers link here
		 Link allUsersLink = linkTo(methodOn(UserRestController.class).getAllUsers()).withRel("ALL USERS");
	    Map<String, Link> response = new HashMap<>();
	    response.put("User deleted",allUsersLink);
	    return response;
	  }
	

}
