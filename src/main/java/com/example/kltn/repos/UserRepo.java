package com.example.kltn.repos;


import com.example.kltn.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Boolean existsByEmailOrPhone(String email, String phone);
    User findUserByEmail(String email);
    User findUserByPhone(String phone);
    @Query(value = "{'id': ?0}",fields = "{'password': 0, 'token':  0}")
    User findUserByIdExcept(String id);

    @Query(value = "{'id':  ?0}", fields = "{'addresses':  1}")
    List<User.Address> findUserAddress(String userId);
    @Query(value = "{'id':  ?0, 'addresses': {'idDefault':  true}}", fields = "{'addresses':  1}")
    User.Address findUserAddressDefault(String userId);
}
