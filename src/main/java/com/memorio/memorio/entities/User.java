package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Das User-Objekt repräsentiert einen User für das Spiel.
 */
@Getter
@Setter
@Entity(name = "user")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@Column
	private String username;
	// TODO: Adjust datatype for password
	@Column
	private String password;
	@OneToOne
	private UserProfil userProfil;

	@Deprecated
	public User() {
	}

	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}

}