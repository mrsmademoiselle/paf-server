package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column
	private String username;
	//TODO: Adjust datatype for password
	@Column
	private String password;
	@Column
	private int wins;
	@Column
	private int looses;

	@Deprecated
	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		//TODO: password hashing things?
		this.password = password;
	}

}