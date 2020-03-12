/**
 * 
 */
package com.blo.res.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.hateoas.RepresentationModel;

/**
 * @author theblo
 * This is the User entity class
 *
 */

@Entity
@Table(name="USER") //required if your table name is different from the class name.

public class User 
extends RepresentationModel<User> { //extends RepresentationModel class in order to inherit the add() method associated with links in HATEOAS 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //use "IDENTITY" when db is handling AutoIncrement
	@Column(name="ID")
	private int id;
	
	@Column(name="USERNAME")// required if the field’s name differs from the table column’s name.
	@NotEmpty(message = "Please provide a username") // used for bean validation ie. @Valid
	private String username;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="USER_TYPE")
	@Min(value = 1, message = "User Type should be either 1 or 2")
    @Max(value = 2, message = "User Type should be either 1 or 2")
	private int user_type;
	
	
	//CONSTRUCTORS
	public User(int id, String username, String password, int user_type) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.user_type = user_type;
	}

	public User(String username, String password, int user_type) {
		super();
		this.username = username;
		this.password = password;
		this.user_type = user_type;
	}

	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	//to String
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", user_type=" + user_type
				+ "]";
	}



	//GETTERS AND SETTERS
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getUser_type() {
		return user_type;
	}


	public void setUser_type(int user_type) {
		this.user_type = user_type;
	}
	
	
	
	
	
	
	
}
