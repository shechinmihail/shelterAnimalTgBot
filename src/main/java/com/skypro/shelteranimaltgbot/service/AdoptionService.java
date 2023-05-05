package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.exception.UserNotFoundException;
import com.skypro.shelteranimaltgbot.model.Adoption;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.model.User;
import com.skypro.shelteranimaltgbot.repository.AdoptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.Collection;

//TODO Создать AdoptionController

@Service
public class AdoptionService {

    private static final Logger logger = LoggerFactory.getLogger(User.class);
    @Autowired
    private AdoptionRepository adoptionRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private PetService petService;

    /**
     * Добавление нового записи усыновления и сохранение ее в базе данных
     *
     * @param adoption
     */
    public Adoption addAdoption(Adoption adoption) {
        logger.info("Вызван метод сохранения записи усыновления");
        adoptionRepository.save(adoption);
        return adoption;
    }


    /**
     * Поиск записи усыновления в базе данных по идентификатору (id)
     *
     * @param id идентификатор записи, не может быть null
     * @return найденная запись
     * @throws EntityNotFoundException если запись с указанным id не была найден в базе данных
     */
    public Adoption findAdoption(@NotNull Long id) {
        logger.info("Вызван метод поиска записи усыновления по идентификатору (id)");
        return adoptionRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }


    /**
     * Изменение данных записи усыновления в базе данных
     *
     * @param adoption редактируемая запись усыновления
     * @return отредактированная запись усыновления
     * @throws UserNotFoundException если запись не найдена
     */
    public Adoption updateAdoption(Adoption adoption) {
        logger.info("Вызван метод редактирования записи усыновления в базе данных");
        if (adoption.getId() != null) {
            if (adoptionRepository.findAdoptionById(adoption.getId())) {
                return adoptionRepository.save(adoption);
            }
        }
        logger.error("По запросу запись усыновления не найдена");
        throw new UserNotFoundException();
    }


    /**
     * Получение списка записей усыновления из базы данных
     *
     * @return список(коллекцию) записей усыновления
     */
    public Collection<Adoption> getAll() {
        logger.info("Вызван метод получения всех записей усыновления");
        return adoptionRepository.findAll();
    }


    /**
     * Удаление записи усыновления из базы данных по идентификатору (id)
     *
     * @param id записи усыновления, не может быть null
     */
    public void deleteAdoption(Long id) {
        logger.info("Вызван метод удаления записи усыновления по идентификатору (id)");
        adoptionRepository.deleteById(id);
    }


    /**
     * Метод отправки сообщения пользователю из swagger
     *
     * @param userTelegramId телеграм Id усыновителя
     * @param message        текст сообщения
     */
    public void sendMessageToUser(Long userTelegramId, String message) {
        //TODO написать метод отправки сообщения пользователю из swagger
    }


    public Adoption createRecord(Long userId, Long petId, Integer trialPeriod) {
        Adoption adoption = new Adoption();
        User user = userService.findUser(userId);
        Pet pet = petService.findPet(petId);
        adoption.setUser(user);
        adoption.setPet(pet);
        adoption.setTrialPeriod(trialPeriod);
        adoptionRepository.save(adoption);
        return adoption;

    }
}
