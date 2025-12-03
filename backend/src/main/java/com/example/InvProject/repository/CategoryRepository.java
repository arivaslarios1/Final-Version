package com.example.InvProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.InvProject.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{
    
}
