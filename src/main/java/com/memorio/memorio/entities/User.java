package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
public class User{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String username;
	//TODO: Adjust datatype for password?
	private String password;
	private int id;
	private int wins;
	private int looses;

	public User(){}

	public User(String username, String password ){
		this.username = username;
		//TODO: make password hashing things?
		this.password = password;
	}

}
