# BLPS Lab1 - Camunda BPM Integration

Этот проект демонстрирует интеграцию Spring Boot приложения с Camunda BPM-движком для управления бизнес-процессами.

## Описание

Проект представляет собой систему подписок с интеграцией Camunda BPM, где вся бизнес-логика перенесена из статических сервисов в динамические BPMN процессы.

### Основные компоненты:

1. **BPMN процессы** - описаны в `src/main/resources/bpmn/`
2. **Camunda делегаты** - Java классы для выполнения бизнес-логики
3. **Camunda формы** - пользовательские формы для ввода данных
4. **Spring Boot интеграция** - конфигурация и контроллеры

## Структура BPMN процессов

### Основной процесс (`main.bpmn`)
- **Регистрация/Вход пользователя** - с отправкой OTP
- **Подтверждение аккаунта** - через OTP
- **Управление подписками** - подписка/отписка от услуг
- **Периодические задачи** - проверка просроченных подписок

### Делегаты (Java классы)
- `LoginDelegate` - вход в существующий аккаунт
- `UserRegistrationDelegate` - регистрация нового пользователя
- `SendOtpDelegate` - отправка OTP на email
- `ConfirmAccountDelegate` - подтверждение аккаунта через OTP
- `SubscribeDelegate` - подписка на услугу
- `UnsubscribeDelegate` - отписка от услуги
- `ExpireDelegate` - пометка просроченных подписок

## Требования

- Java 17+
- PostgreSQL
- Maven/Gradle

## Настройка базы данных

1. Создайте базу данных PostgreSQL:
```sql
CREATE DATABASE blps_lab1;
```

2. Настройте подключение в `application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/blps_lab1
    username: your_username
    password: your_password
```

3. Настройте email в `application.yml`:
```yaml
spring:
  mail:
    username: your-email@gmail.com
    password: your-app-password
```

## Запуск приложения

1. **Сборка проекта:**
```bash
./gradlew build
```

2. **Запуск приложения:**
```bash
./gradlew bootRun
```

3. **Доступ к приложению:**
- Основное приложение: http://localhost:8080
- Camunda Cockpit: http://localhost:8080/camunda
- Тестовая страница: http://localhost:8080/index.html

## API Endpoints

### Процессы Camunda
- `POST /api/process/start` - запуск основного процесса
- `POST /api/process/auth` - запуск процесса аутентификации
- `POST /api/process/subscribe` - запуск процесса подписки
- `GET /api/process/status/{processInstanceId}` - статус процесса

### Существующие API (для совместимости)
- `POST /api/auth/register` - регистрация
- `POST /api/auth/login` - вход
- `POST /api/auth/confirm/{otp}` - подтверждение OTP
- `GET /api/subscriptions/` - список предложений
- `POST /api/subscriptions/{id}` - подписка
- `DELETE /api/subscriptions/{id}` - отписка

## Camunda Forms

### Форма входа/регистрации (`signin.form`)
- Email
- Password
- Action (login/register)

### Форма OTP (`otp.form`)
- OTP код

### Форма подписки (`subscribe.form`)
- ID предложения
- Длительность в месяцах
- Действие (subscribe/unsubscribe)

## Архитектура

### До интеграции Camunda:
```
Controller -> Service -> Repository
```

### После интеграции Camunda:
```
Controller -> Camunda Process -> Delegate -> Service -> Repository
```

## Преимущества интеграции

1. **Визуальное моделирование** - бизнес-процессы видны в Camunda Cockpit
2. **Гибкость** - легко изменять процессы без перекомпиляции
3. **Мониторинг** - отслеживание выполнения процессов
4. **Масштабируемость** - асинхронная обработка и распределенные задачи
5. **Аудит** - полная история выполнения процессов

## Тестирование

1. Откройте http://localhost:8080/index.html
2. Попробуйте зарегистрировать пользователя
3. Проверьте отправку OTP
4. Подтвердите аккаунт
5. Создайте подписку на услугу

## Мониторинг процессов

1. Откройте Camunda Cockpit: http://localhost:8080/camunda
2. Войдите с учетными данными (по умолчанию: demo/demo)
3. Просмотрите активные процессы и их статус

## Troubleshooting

### Проблемы с базой данных
- Убедитесь, что PostgreSQL запущен
- Проверьте настройки подключения в `application.yml`

### Проблемы с email
- Настройте SMTP в `application.yml`
- Для Gmail используйте App Password

### Проблемы с Camunda
- Проверьте логи приложения
- Убедитесь, что BPMN файлы корректны
- Проверьте, что все делегаты зарегистрированы как Spring beans 