package com.contact.manager.repository;

import com.contact.manager.entity.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {
    @Query("from Contact as c where c.user.userId =:userId")
    public List<Contact> findContactByUser(@Param("userId") int userId);
}
