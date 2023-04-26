package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;


/**
 * Сервис PetService
 * Сервис для добавления, удаления, редактирования и поиска домашних питомцев в базе данных
 */
@Service
public class PetService {


    /**
     * Поле репозитория домашнего питомца
     */

    private final PetRepository petRepository;


    private final static Logger logger = LoggerFactory.getLogger(PetService.class);

    /**
     * Конструктор - создание нового объекта репозитория
     *
     * @param petRepository
     * @see PetRepository(PetRepository)
     */
    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    /**
     * Добавление нового домашнего питомца и сохранение его в базе данных
     *
     * @param pet домашний питомец
     * @return добавленный новый домашний питомец
     */
    public Pet addPet(Pet pet) {
        logger.info("Вызван метод добавления домашнего животного");
        return petRepository.save(pet);
    }


    /**
     * Поиск домашнего питомеца в базе данных по идентификатору (id)
     *
     * @param id идентификатор домашнего питомеца, не может быть null
     * @return найденый домашний питомец
     * @throws EntityNotFoundException если домвшний питомец с указаным id не был найден в базе данных
     */
    public Pet findPet(Long id) {
        logger.info("Вызван метод поиска домашнего питомца по идентификатору (id)");
        return petRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Изменение данных домашнего питомца в базе данных
     *
     * @param pet       редактируемый домашний питомец
     * @param statusPet статус домашнего питомеца
     * @return отредактированный домашний питомец
     * @throws EntityNotFoundException если домашний питомец не найден
     */
    public Pet updatePet(Pet pet, StatusPet statusPet) {
        logger.info("Вызван метод редактирования домашнего питомца в базе данных");
        if (pet.getId() != null) {
            if (findPet(pet.getId()) != null) {
                pet.setStatusPet(statusPet);
                return petRepository.save(pet);
            }
        }
        logger.error("По запросу домашний питомец не найден");
        throw new EntityNotFoundException();
    }

    /**
     * получение списка домашних питомцев из базы данных
     *
     * @return список(коллекцию) домашних питомцев
     */
    public Collection<Pet> getAllPet() {
        logger.info("Вызван метод получения всех домашних животных");
        return petRepository.findAll();
    }

    /**
     * Удаление домашнего питомца из базы данных по идентификатору (id)
     *
     * @param id идентификатор домашнего питомца, не может быть null
     */
    public void deletePet(Long id) {
        logger.info("Вызван метод удаления домашнего питомца по идентификатору (id)");
        petRepository.deleteById(id);
    }

    /**
     * возвращаем всех животных по типу
     *
     * @param typePet вид домашнего питомца, не может быть null
     */
    public Collection<Pet> getAllPetByTypePet(String typePet) {
        logger.info("Вызван метод показать всех питомцев по типу {} ", typePet);
        return new ArrayList<>(petRepository.findAll());

    }


}
