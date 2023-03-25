## Java Spring Boot Client for Telegram Bot 
## Телеграм - бот для приюта домашних животных  

Java-библиотека для взаимодействия с Telegram Bot API

 - Поддержка всех методов Bot API 6.6
 - [Паспорт] Telegram и API расшифровки


 [Паспорт]: <https://core.telegram.org/passport>
## Подключение библиотеки
Gradle:
```sh
implementation 'com.github.pengrad:java-telegram-bot-api:6.5.0'
```
Maven:

```sh
<dependency>
  <groupId>com.github.pengrad</groupId>
  <artifactId>java-telegram-bot-api</artifactId>
  <version>6.5.0</version>
</dependency>
```

## Использование
```sh
// Create your bot passing the token received from @BotFather
TelegramBot bot = new TelegramBot("BOT_TOKEN");

// Register for updates
bot.setUpdatesListener(updates -> {
    // ... process updates
    // return id of last processed update or confirm them all
    return UpdatesListener.CONFIRMED_UPDATES_ALL;
});
// Send messages
long chatId = update.message().chat().id();
SendResponse response = bot.execute(new SendMessage(chatId, "Hello!"));
```



