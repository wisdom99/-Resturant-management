package com.wisdom.Resturantmanagement.service;

import com.wisdom.Resturantmanagement.model.User;

public interface UserService {
	void save(User user);

    User findByUsername(String username);

}
