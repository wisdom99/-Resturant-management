package com.wisdom.Resturantmanagement.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.wisdom.Resturantmanagement.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
