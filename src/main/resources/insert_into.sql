INSERT INTO "TG".public.type_pet
VALUES ('1', 'Кошки'),
       ('2', 'Собаки');

INSERT INTO "TG".public.documents
VALUES ('1', 'Паспорт', '1'),
       ('2', 'Заявление', '1'),
       ('3', 'Справка', '1');

INSERT INTO "TG".public.shelter
VALUES ('1', 'Приют «Милые пушистик»
Приют открыл свои двери в 2018 году. С этого дня каждый день борется за жизни животных.
►Главная наша задача – найти хозяев нашим питомцам. Слово «хозяин» не совсем корректное – у нас равные права на жизнь – животные – наши друзья и любимцы!
►СДЕЛАЕМ ВСЕ ВОЗМОЖНОЕ ДЛЯ ЭТИХ СОЗДАНИЙ! Не проходите мимо и они будут вам БЛАГОДАРНЫ!
►Выбирайте себе друга! СМОТРИТЕ АЛЬБОМЫ, ПОМОГАЙТЕ, ПИШИТЕ, ЗВОНИТЕ!
____________________________________
"ЛУЧШЕ САМАЯ МАЛАЯ ПОМОЩЬ, ЧЕМ САМОЕ БОЛЬШОЕ СОЧУВСТВИЕ" ВЛАДИСЛАВ ЛОРАНЦ', 'Милые пушистики', 'bot_Botya_bot');


INSERT INTO "TG".public.pet
VALUES ('1', '2', null, 'Шарик', 'FREE', '1', '2'),
       ('2', '1', null, 'Бобик', 'FREE', '1', '2'),
       ('3', '2', null, 'Мурзик', 'FREE', '1', '1');


INSERT INTO "TG".public.take_pets
VALUES ('1', 'Правила знакомства', 'Правило текст'),
       ('2', 'Рекомендации по транспортировке', 'Рекомендация текст'),
       ('3', 'Обустройство дома для щенка/котенка', 'Обустройство текст'),
       ('4', 'Обустройство дома для взрослого животного', 'Обустройство текст'),
       ('5', 'Обустройство дома для животного с ограниченными возможностями', 'Обустройство текст'),
       ('6', 'Причины отказа в усыновлении животного', 'Причины текст'),
       ('7', 'Советы кинолога', 'Советы текст');