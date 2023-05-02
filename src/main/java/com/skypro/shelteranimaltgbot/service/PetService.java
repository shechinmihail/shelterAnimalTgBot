package com.skypro.shelteranimaltgbot.service;

import com.skypro.shelteranimaltgbot.model.Enum.StatusPet;
import com.skypro.shelteranimaltgbot.model.Pet;
import com.skypro.shelteranimaltgbot.repository.PetRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;

import static java.nio.file.StandardOpenOption.CREATE_NEW;


/**
 * Сервис PetService
 * Сервис для добавления, удаления, редактирования и поиска домашних питомцев в базе данных
 */
@Service
public class PetService {

    @Value("${path.to.avatars.folder}")
    private String avatarsDir;


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

    /*
    * public void uploadAvatar(long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for upload avatar");
        Student student = studentService.getStudent(studentId);
        Path filePath = Path.of(avatarsDir, student + "." + getExtensions(avatarFile.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatarFile.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        Avatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(avatarFile.getSize());
        avatar.setMediaType(avatarFile.getContentType());
        avatar.setData(avatarFile.getBytes());
        avatarRepository.save(avatar);
    }
    * */


    /**
     * добавляем фото питомца
     */
    public void uploadAvatar(Long petId, MultipartFile avatar) throws IOException {
        logger.info("Вызван метод загрузки фото");
        Pet pet = petRepository.findPetById(petId);
        Path filePath = Path.of(avatarsDir, pet.getId() + "_" + pet.getName() + "." + getExtensions(avatar.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (
                InputStream is = avatar.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024)
        ) {
            bis.transferTo(bos);
        }

        pet.setFilePath(filePath.toString());
        petRepository.save(pet);


    }

    /**
     * получаем расширение файла
     */
    private String getExtensions(String fileName) {
        logger.info("Вызван метод для получения расширений");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}
