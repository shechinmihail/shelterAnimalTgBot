package com.skypro.shelteranimaltgbot.repository;

import com.skypro.shelteranimaltgbot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    //метод проверяет есть ли в БД Юзер
    @Query("select count (a.userTelegramId) from User a where a.userTelegramId = :userId")
    int findByUserId(@Param("userId") Long userId);


    //Пример написания SQL запроса на обновление
//    @Modifying
//    @Transactional
//    @Query(value = "update users set phone  = :phone where users.user_telegram_id = :userTelegramId", nativeQuery = true)
//    void updatePhone(@Param("userTelegramId") Long userTelegramId,
//                     @Param("phone") String phone);

    User findByUserTelegramId(Long userTelegramId);


//    @Query("select u from User u where u.role =:role")
//    List<User> findByRole(@Param("role") Role role);
}