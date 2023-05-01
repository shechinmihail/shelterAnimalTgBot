INSERT INTO TG.public.type_pet
VALUES ('1', 'Кошки'),
       ('2', 'Собаки');

INSERT INTO TG.public.documents
VALUES ('1', 'Паспорт', '1'),
       ('2', 'Заявление', '1'),
       ('3', 'Справка', '1');

INSERT INTO TG.public.shelter
VALUES ('1', 'Здесь будет информация о приюте..', 'Милые пушистики', 'SkyShelterBot');



INSERT INTO TG.public.pet
VALUES ('1', '2', null, 'Шарик', 'FREE', '2'),
       ('2', '1', null, 'Бобик', 'FREE', '2'),
       ('3', '2', null, 'Мурзик', 'FREE', '1');

INSERT INTO TG.public.take_pet
VALUES ('1', 'Описание как взять кошку'),
       ('2', 'Описание как взять собаку');