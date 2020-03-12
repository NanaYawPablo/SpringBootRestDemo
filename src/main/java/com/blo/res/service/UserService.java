package com.blo.res.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.blo.res.entity.User;
import com.blo.res.exceptionhandlers.UserNotFoundException;
import com.blo.res.repository.UserRepository;

/*
 * Service class for users
 */

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	//get all users
	public List<User> getAllUsers(){
		Iterable<User> users= this.userRepository.findAll();
		 List<User> usersList = new ArrayList<>();
			//place each user from iterable into List so you can use methods like sort,remove,contains etc.
		users.forEach(user->{
			usersList.add(user);
		});
		return usersList;
		
	}
	
	//count all users
	public Long usersCount() {
		return this.userRepository.count();
	}
	
	//findByID
	public User findUserbyID(int id) throws UserNotFoundException{
		User user= this.userRepository.findById(id);
		return user;
		
	}
	
	//findByUsername
	public User findUserbyUsername(String username) {
		User user= this.userRepository.findByUsername(username);
		if(user==null) throw new UserNotFoundException();
		return user;
	}
	
	//createUser
	public User createUser(User user) {
		return this.userRepository.save(user); //save method will never return null 
	}
	
	//deleteUser
	public void deleteUser(User user) {
		 this.userRepository.delete(user);
	}
	
	

}
