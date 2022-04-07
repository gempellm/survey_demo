# Инструкция по запуску:
1. Клонируем репозиторий.
2. В pom.xml -> пкм -> maven -> reload project.
3. Скачиваем и устаналиваем XAMPP (https://www.apachefriends.org/ru/index.html).
4. Запускаем, заходим в админ-панель (Admin).
5. Создаем БД survey_demo.
6. Запускаем SurveyDemoApplication.

Описание API:

/user/register - метод POST, доступен всем
Принимает на вход json типа:
{
    "login": "admin",
    "password": "admin",
    "roles": "ROLE_USER,ROLE_ADMIN"
}
Возвращает "Successfully registered" в случае успешной регистрации, либо "Username already exists", если такой пользователь уже существует.

/user/authenticate - метод POST, доступен всем
Принимает на вход json типа:
{
    "login": "admin",
    "password": "admin"
}
Возвращает access токен, в случае успеха, либо статус-код ошибки.
Access токен возвращается следующего вида:
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0OTM2MzE4MSwiaWF0IjoxNjQ5MzI3MTgxfQ.4ffUbdz3fv10dOxuLU8HRHCUezZhUfZbMmn87RCx8nU
Его нужно подставлять в заголовок "Authorization" при Http запросах на ограниченные по правам эндпоинты, подставлять в виде "Bearer token"

/survey/create - метод POST, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "name": "Test Survey 1"
}
Где name - название опроса. Возвращает следующее:
Survey{id=17, name='Test Survey 3', start='null', end='null', description='null'}

/survey/fetch - метод GET, доступен USER, ADMIN
Принимает на access токен в Authorization хедере.
Возвращает все опросы без фильтрации:
[
    {
        "id": 8,
        "name": "Best Test Survey 1",
        "start": 1649321365731,
        "end": 1649321378385,
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Leo vel fringilla est ullamcorper eget nulla facilisi etiam. Nec feugiat nisl pretium fusce id velit ut tortor pretium."
    },
    {
        "id": 9,
        "name": "Best Test Survey 2",
        "start": 1649327244763,
        "end": 1649327256948,
        "description": null
    },
    {
        "id": 17,
        "name": "Test Survey 3",
        "start": 1649331967161,
        "end": null,
        "description": null
    }
]

/survey/fetch-active - метод GET, доступен USER, ADMIN
Принимает на access токен в Authorization хедере.
Возвращает только активные опросы:
[
    {
        "id": 17,
        "name": "Test Survey 3",
        "start": 1649331967161,
        "end": null,
        "description": null
    }
]
Активные опросы - это те, у которых задано начало, но не задан конец.


/survey/modify - метод PUT, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "id": 17,
    "name": "Best Test Survey 3",
    "description": "descripton"
}
Возвращает измененный опрос:
{
    "id": 17,
    "name": "Best Test Survey 3",
    "start": 1649331967161,
    "end": null,
    "description": "descripton"
}

/survey/start - метод PUT, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
  "id": 17
}
Начинает опрос, делает его доступным для /survey/fetch-active, дата начала задается временем отправки запроса.
Задать повторную дату опроса, либо удалить значение невозможно.

/survey/end - метод PUT, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
  "id": 17
}
Заканчивает опрос, делает его недоступным для /survey/fetch-active, дата конца задается временем отправки запроса. Опросы без даты начала невозможно завершить.
Задать повторную дату завершения опроса, либо удалить значение невозможно.
{
    "id": 17,
    "name": "Best Test Survey 3",
    "start": 1649331967161,
    "end": 1649333978217,
    "description": "descripton"
}

/survey/delete - метод DELETE, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
  "id": 17
}
Удаляет опрос, возвращает список всех опросов:
[
    {
        "id": 8,
        "name": "Best Test Survey 1",
        "start": 1649321365731,
        "end": 1649321378385,
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Leo vel fringilla est ullamcorper eget nulla facilisi etiam. Nec feugiat nisl pretium fusce id velit ut tortor pretium."
    },
    {
        "id": 9,
        "name": "Best Test Survey 2",
        "start": 1649327244763,
        "end": 1649327256948,
        "description": null
    }
]

/question/fetch - метод GET, доступен USER, ADMIN
Принимает access токен в Authorization хедере.
Возвращает все вопросы:
[
    {
        "id": 10,
        "surveyId": 8,
        "text": "Хорошо ли вы сегодня спали?",
        "type": "single",
        "answers": null
    },
    {
        "id": 11,
        "surveyId": 9,
        "text": "Хорошо ли вы сегодня кушали?",
        "type": "single",
        "answers": null
    },
    {
        "id": 15,
        "surveyId": 8,
        "text": "Есть ли сонливость?",
        "type": "single",
        "answers": "Да,Нет"
    },
    {
        "id": 18,
        "surveyId": 17,
        "text": "Хорошо ли вы сегодня кушали?",
        "type": "single",
        "answers": null
    }
]

/question/fetch-by-id - метод POST, доступен USER, ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
  "id": 8
}

Возвращает вопросы прикрепленные к переданному Id опроса:
[
    {
        "id": 10,
        "surveyId": 8,
        "text": "Хорошо ли вы сегодня спали?",
        "type": "single",
        "answers": null
    },
    {
        "id": 15,
        "surveyId": 8,
        "text": "Есть ли сонливость?",
        "type": "single",
        "answers": "Да,Нет"
    }
]

/question/create - метод POST, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "id": 8,
    "text": "Выберите три цвета:",
    "type": "multiple",
    "answers": "yellow,red,blue,green"
}
Где id - это id опроса.

Возвращает созданный вопрос:
{
    "id": 20,
    "surveyId": 8,
    "text": "Выберите три цвета:",
    "type": "multiple",
    "answers": "yellow,red,blue,green"
}

asnwers может быть пустым. Логика проверки корректности возможных ответов, прикрепленных к вопросу и их сопоставление с входящими ответами на вопрос, а также логика зависимости поведения от типа вопроса  не реализованы.

/question/modify - метод PUT, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "id": 20,
    "text": "Выберите три цвета:",
    "type": "multiple",
    "answers": "yellow,red,blue,white"
}
Где id - это id вопроса.

Возвращает измененный вопрос:
{
    "id": 20,
    "surveyId": 8,
    "text": "Выберите три цвета:",
    "type": "multiple",
    "answers": "yellow,red,blue,white"
}

/question/delete - метод DELETE, доступен ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "id": 20
}

Возвращает список вопросов опроса, к которому был прикреплен удаленный вопрос:
[
    {
        "id": 10,
        "surveyId": 8,
        "text": "Хорошо ли вы сегодня спали?",
        "type": "single",
        "answers": null
    },
    {
        "id": 15,
        "surveyId": 8,
        "text": "Есть ли сонливость?",
        "type": "single",
        "answers": "Да,Нет"
    }
]

/answer/create - метод POST, доступен USER, ADMIN
Принимает на вход json типа, access токен в Authorization хедере:
{
    "surveyId": 21,
    "questionId": 24,
    "answer": "Всё хорошо!"
}
Где surveyId - id опроса, questionId - id вопроса, answer - ответ.
Возвращает созданный ответ:
{
    "id": 27,
    "userId": 4,
    "surveyId": 21,
    "questionId": 24,
    "answer": "Всё хорошо!"
}

/answer/complete - метод GET, доступен USER, ADMIN
Принимает на вход access token в Authorization хедере.
Возвращает:
{
    "17": [
        {
            "id": 19,
            "userId": 4,
            "surveyId": 17,
            "questionId": 18,
            "answer": "Yes"
        }
    ],
    "21": [
        {
            "id": 25,
            "userId": 4,
            "surveyId": 21,
            "questionId": 22,
            "answer": "No"
        },
        {
            "id": 26,
            "userId": 4,
            "surveyId": 21,
            "questionId": 23,
            "answer": "No"
        },
        {
            "id": 27,
            "userId": 4,
            "surveyId": 21,
            "questionId": 24,
            "answer": "Всё хорошо!"
        }
    ],
    "9": [
        {
            "id": 16,
            "userId": 4,
            "surveyId": 9,
            "questionId": 11,
            "answer": "No"
        }
    ]
}
Где ключ - это id опроса, внутри находятся ответы Answer на вопросы. Выводятся только те опросы, на которые пользователь дал ВСЕ ответы, то есть нет ни одного неответченного вопроса.
