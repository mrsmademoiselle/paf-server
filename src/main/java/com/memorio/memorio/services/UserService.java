package com.memorio.memorio.services;
import com.memorio.memorio.repositories.UserRepository;
import com.memorio.memorio.web.dto.UserAuthDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.memorio.memorio.entities.User;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
	this.userRepository = userRepository;
    }

	@Autowired
	private PasswordEncoder bcryptEncoder;

	public boolean saveUser(UserAuthDto user) {
		if(this.userRepository.existsByUsername(user.getUsername())){
			return false;
		}
		try{
			// PW Encryption with bcypt
			User newUser = new User(user.getUsername(),bcryptEncoder.encode(user.getPassword()));
			userRepository.save(newUser);
			return true;

		}catch (Exception e){
			return false;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Kein Benutzer mit dem Namen " + username + " gefunden");
		}
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				new ArrayList<>());
	}

}
