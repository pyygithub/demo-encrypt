package com.thtf.dao;

import com.thtf.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/8/12 10:11
 * Version: v1.0
 * ========================
 */
public interface UserRepository extends JpaRepository<User, Long> {

}
