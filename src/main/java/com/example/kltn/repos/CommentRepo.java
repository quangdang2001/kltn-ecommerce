package com.example.kltn.repos;

import com.example.kltn.models.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentRepo extends MongoRepository<Comment, String> {
}
