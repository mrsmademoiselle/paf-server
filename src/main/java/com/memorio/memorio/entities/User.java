package  com.memorio.memorio.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User{

	private String username;

	private int id;
	private int wins;

	private int looses;

	public User(){}

}
