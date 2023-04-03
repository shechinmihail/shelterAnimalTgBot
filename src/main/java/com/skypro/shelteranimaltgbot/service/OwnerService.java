package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Owner;
import com.skypro.shelteranimaltgbot.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class OwnerService {
    @Autowired
    private OwnerRepository ownerRepository;
    private static final Logger logger = LoggerFactory.getLogger(Owner.class);

    /**
     * Добавление владельца домашнего питомца и сохранение его в БД
     *
     * @param owner создается владелец домашнего питомца
     * @return добавленный владелец домашнего питомца
     */
    public Owner addOwner(Owner owner) {
        logger.info("Вызван метод добавления владельца");
        return ownerRepository.save(owner);
    }

    /**
     * Поиск владельца домашнего питомца в БД по идентификатору (id)
     *
     * @param id идентификатор владельца, не может быть null
     * @return найденый владелец домашнего питомца
     * @throws EntityNotFoundException если владелец домашнего питомца с указаным id не был найден в БД
     */
    public Owner findOwner(Long id) {
        logger.info("Вызван метод поиска владельца домашнего питомца по идентификатору (id)");
        return ownerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

}
