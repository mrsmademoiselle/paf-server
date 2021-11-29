package com.memorio.memorio.services;
import com.memorio.memorio.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.memorio.memorio.entities.User;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
	this.userRepository = userRepository;
    }

    public boolean saveUser(String username, String password) {
	if(userRepository.existsByUsername(username)){return false;}
	try {
	    userRepository.save(new User(username, password));
	    return true;
	} catch(Exception e) {
	    return false;
	}
    }

}
