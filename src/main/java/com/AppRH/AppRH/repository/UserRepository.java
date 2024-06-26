

package com.AppRH.AppRH.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AppRH.AppRH.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByNomeAndRg(String nome, String rg);
}
