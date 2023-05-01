package com.skypro.shelteranimaltgbot.service;
import com.skypro.shelteranimaltgbot.model.TakePetFromShelter;
import com.skypro.shelteranimaltgbot.repository.TakePetFromShelterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.Optional;

@Service
public class TakePetFromShelterService {
    private final TakePetFromShelterRepository takePetFromShelterRepository;
    private final Logger logger = LoggerFactory.getLogger(PetService.class);

    public TakePetFromShelterService(TakePetFromShelterRepository takePetFromShelterRepository) {
        this.takePetFromShelterRepository = takePetFromShelterRepository;
    }

    /**
     * Добавление нового описания  и сохранение его в базе данных
     *
     * @param takePetFromShelter описание
     * @return новое описание
     */
    public TakePetFromShelter addDescription(TakePetFromShelter takePetFromShelter) {
        logger.info("Вызван метод добавления описания");
        TakePetFromShelter newDescription = takePetFromShelterRepository.save(takePetFromShelter);
        return newDescription;
    }


    /**
     * Поиск описания в базе данных по идентификатору (id)
     *
     * @param id идентификатор описания, не может быть null
     * @return найденное описание
     * @throws EntityNotFoundException если описание с указаным id не было найден в базе данных
     */
    public TakePetFromShelter findDescription(Long id) {
        logger.info("Вызван метод поиска описания по идентификатору (id)");
        return takePetFromShelterRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public String getDescription(Long id) {
        logger.info("Вызван метод вывода описания по идентификатору (id)");
        Optional<TakePetFromShelter> takePetFromShelter = takePetFromShelterRepository.findById(id);
        return takePetFromShelter.get().getDescription();

    }

    /**
     * Изменение описания в базе данных
     *
     * @param takePetFromShelter редактируеме описнаие
     * @return отредактированное описание
     * @throws EntityNotFoundException если описание не найдено
     */
    public TakePetFromShelter updateDescription(TakePetFromShelter takePetFromShelter) {
        logger.info("Вызван метод обновления описания");
        if (takePetFromShelter.getId() != null) {
            if (findDescription(takePetFromShelter.getId()) != null) {
                return takePetFromShelterRepository.save(takePetFromShelter);
            }
        }
        logger.error("По запросу описание не найдено");
        throw new EntityNotFoundException();
    }

    /**
     * получение всех рекомендаций по усыновлению животного из базы данных
     *
     * @return список(коллекцию) рекомендаций
     */
    public Collection<TakePetFromShelter> getAll() {
        logger.info("Вызван метод получения всех рекомендаций");
        return takePetFromShelterRepository.findAll();
    }

    /**
     * Удаление рекомендации из базы данных по идентификатору (id)
     *
     * @param id идентификатор рекомендации, не может быть null
     */
    public void deleteRecomendation(Long id) {
        logger.info("Вызван метод удаления рекомендации по идентификатору (id)");
        takePetFromShelterRepository.deleteById(id);
    }

}
