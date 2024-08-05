package com.badnarrators.celos.wiki.repository;

import com.badnarrators.celos.wiki.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, String> {

    List<Page> findByTitleContainingIgnoreCase(String title);

    List<Page> findByCategoryContainingIgnoreCase(String category);
}
