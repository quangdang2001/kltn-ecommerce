package com.example.kltn.repos;


import com.example.kltn.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends MongoRepository<User, String> {
    Boolean existsByEmailOrPhone(String email, String phone);
    User findUserByEmail(String email);
    User findUserByPhone(String phone);


}
