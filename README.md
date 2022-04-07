# Инструкция по запуску:
1. Клонируем репозиторий.
2. В pom.xml -> пкм -> maven -> reload project.
3. Скачиваем и устаналиваем XAMPP (https://www.apachefriends.org/ru/index.html).
4. Запускаем, заходим в админ-панель (Admin).
5. Создаем БД survey_demo.
6. Запускаем SurveyDemoApplication.

Описание API:

/user/register - метод POST, доступен всем<br />
Принимает на вход json типа:<br />
{<br />
    "login": "admin",<br />
    "password": "admin",<br />
    "roles": "ROLE_USER,ROLE_ADMIN"<br />
}<br />
Возвращает "Successfully registered" в случае успешной регистрации, либо "Username already exists", если такой пользователь уже существует.

/user/authenticate - метод POST, доступен всем<br />
Принимает на вход json типа:<br />
{<br />
    "login": "admin",<br />
    "password": "admin"<br />
}<br />
Возвращает access токен, в случае успеха, либо статус-код ошибки.<br />
Access токен возвращается следующего вида:<br />
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTY0OTM2MzE4MSwiaWF0IjoxNjQ5MzI3MTgxfQ.4ffUbdz3fv10dOxuLU8HRHCUezZhUfZbMmn87RCx8nU<br />
Его нужно подставлять в заголовок "Authorization" при Http запросах на ограниченные по правам эндпоинты, подставлять в виде "Bearer token"<br />

/survey/create - метод POST, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "name": "Test Survey 1"<br />
}<br />
Где name - название опроса. Возвращает следующее:<br />
Survey{id=17, name='Test Survey 3', start='null', end='null', description='null'}<br />

/survey/fetch - метод GET, доступен USER, ADMIN<br />
Принимает на access токен в Authorization хедере.<br />
Возвращает все опросы без фильтрации:<br />
[<br />
    {<br />
        "id": 8,<br />
        "name": "Best Test Survey 1",<br />
        "start": 1649321365731,<br />
        "end": 1649321378385,<br />
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Leo vel fringilla est ullamcorper eget nulla facilisi etiam. Nec feugiat nisl pretium fusce id velit ut tortor pretium."<br />
    },<br />
    {<br />
        "id": 9,<br />
        "name": "Best Test Survey 2",<br />
        "start": 1649327244763,<br />
        "end": 1649327256948,<br />
        "description": null<br />
    },<br />
    {<br />
        "id": 17,<br />
        "name": "Test Survey 3",<br />
        "start": 1649331967161,<br />
        "end": null,<br />
        "description": null<br />
    }<br />
]<br />

/survey/fetch-active - метод GET, доступен USER, ADMIN<br />
Принимает на access токен в Authorization хедере.<br />
Возвращает только активные опросы:<br />
[<br />
    {<br />
        "id": 17,<br />
        "name": "Test Survey 3",<br />
        "start": 1649331967161,<br />
        "end": null,<br />
        "description": null<br />
    }<br />
]<br />
Активные опросы - это те, у которых задано начало, но не задан конец.<br />


/survey/modify - метод PUT, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "id": 17,<br />
    "name": "Best Test Survey 3",<br />
    "description": "descripton"<br />
}<br />
Возвращает измененный опрос:<br />
{<br />
    "id": 17,<br />
    "name": "Best Test Survey 3",<br />
    "start": 1649331967161,<br />
    "end": null,<br />
    "description": "descripton"<br />
}<br />

/survey/start - метод PUT, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
  "id": 17<br />
}<br />
Начинает опрос, делает его доступным для /survey/fetch-active, дата начала задается временем отправки запроса.<br />
Задать повторную дату опроса, либо удалить значение невозможно.<br />

/survey/end - метод PUT, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
  "id": 17<br />
}<br />
Заканчивает опрос, делает его недоступным для /survey/fetch-active, дата конца задается временем отправки запроса. Опросы без даты начала невозможно завершить.<br />
Задать повторную дату завершения опроса, либо удалить значение невозможно.<br />
{<br />
    "id": 17,<br />
    "name": "Best Test Survey 3",<br />
    "start": 1649331967161,<br />
    "end": 1649333978217,<br />
    "description": "descripton"<br />
}<br />

/survey/delete - метод DELETE, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
  "id": 17<br />
}<br />
Удаляет опрос, возвращает список всех опросов:<br />
[<br />
    {<br />
        "id": 8,<br />
        "name": "Best Test Survey 1",<br />
        "start": 1649321365731,<br />
        "end": 1649321378385,<br />
        "description": "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Leo vel fringilla est ullamcorper eget nulla facilisi etiam. Nec feugiat nisl pretium fusce id velit ut tortor pretium."<br />
    },<br />
    {<br />
        "id": 9,<br />
        "name": "Best Test Survey 2",<br />
        "start": 1649327244763,<br />
        "end": 1649327256948,<br />
        "description": null<br />
    }<br />
]<br />

/question/fetch - метод GET, доступен USER, ADMIN<br />
Принимает access токен в Authorization хедере.<br />
Возвращает все вопросы:<br />
[<br />
    {<br />
        "id": 10,<br />
        "surveyId": 8,<br />
        "text": "Хорошо ли вы сегодня спали?",<br />
        "type": "single",<br />
        "answers": null<br />
    },<br />
    {<br />
        "id": 11,<br />
        "surveyId": 9,<br />
        "text": "Хорошо ли вы сегодня кушали?",<br />
        "type": "single",<br />
        "answers": null<br />
    },<br />
    {<br />
        "id": 15,<br />
        "surveyId": 8,<br />
        "text": "Есть ли сонливость?",<br />
        "type": "single",<br />
        "answers": "Да,Нет"<br />
    },<br />
    {<br />
        "id": 18,<br />
        "surveyId": 17,<br />
        "text": "Хорошо ли вы сегодня кушали?",<br />
        "type": "single",<br />
        "answers": null<br />
    }<br />
]<br />

/question/fetch-by-id - метод POST, доступен USER, ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
  "id": 8<br />
}<br />

Возвращает вопросы прикрепленные к переданному Id опроса:<br />
[<br />
    {<br />
        "id": 10,<br />
        "surveyId": 8,<br />
        "text": "Хорошо ли вы сегодня спали?",<br />
        "type": "single",<br />
        "answers": null<br />
    },<br />
    {<br />
        "id": 15,<br />
        "surveyId": 8,<br />
        "text": "Есть ли сонливость?",<br />
        "type": "single",<br />
        "answers": "Да,Нет"<br />
    }<br />
]<br />

/question/create - метод POST, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "id": 8,<br />
    "text": "Выберите три цвета:",<br />
    "type": "multiple",<br />
    "answers": "yellow,red,blue,green"<br />
}<br />
Где id - это id опроса. Type - тип вопроса. Поддерживаемые типы вопросов: text/single/multiple<br />


Возвращает созданный вопрос:<br />
{<br />
    "id": 20,<br />
    "surveyId": 8,<br />
    "text": "Выберите три цвета:",<br />
    "type": "multiple",<br />
    "answers": "yellow,red,blue,green"<br />
}<br />

asnwers может быть пустым, если тип опроса - text, либо null. Реализована простая логика на проверку соответствия содержания ответов типу и содержанию вопроса.<br />
Например, если вопрос выглядит как "single" "Да,Нет", допустимыми ответами будут только "Да" или "Нет".<br />
Если вопрос выглядит как "multiple" "Хорошо,Плохо,Средне,Отлично", допустимыми ответами будет любая комбинация из этих слов через запятую, либо один вариант ответа.<br />
Если вопрос выглядит как "text", допустим любой вид ответа.<br />

/question/modify - метод PUT, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "id": 20,<br />
    "text": "Выберите три цвета:",<br />
    "type": "multiple",<br />
    "answers": "yellow,red,blue,white"<br />
}<br />
Где id - это id вопроса.<br />

Возвращает измененный вопрос:<br />
{<br />
    "id": 20,<br />
    "surveyId": 8,<br />
    "text": "Выберите три цвета:",<br />
    "type": "multiple",<br />
    "answers": "yellow,red,blue,white"<br />
}<br />

/question/delete - метод DELETE, доступен ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "id": 20<br />
}<br />

Возвращает список вопросов опроса, к которому был прикреплен удаленный вопрос:<br />
[<br />
    {<br />
        "id": 10,<br />
        "surveyId": 8,<br />
        "text": "Хорошо ли вы сегодня спали?",<br />
        "type": "single",<br />
        "answers": null<br />
    },<br />
    {<br />
        "id": 15,<br />
        "surveyId": 8,<br />
        "text": "Есть ли сонливость?",<br />
        "type": "single",<br />
        "answers": "Да,Нет"<br />
    }<br />
]<br />

/answer/create - метод POST, доступен USER, ADMIN<br />
Принимает на вход json типа, access токен в Authorization хедере:<br />
{<br />
    "surveyId": 21,<br />
    "questionId": 24,<br />
    "answer": "Всё хорошо!"<br />
}<br />
Где surveyId - id опроса, questionId - id вопроса, answer - ответ.<br />
Возвращает созданный ответ:<br />
{<br />
    "id": 27,<br />
    "userId": 4,<br />
    "surveyId": 21,<br />
    "questionId": 24,<br />
    "answer": "Всё хорошо!"<br />
}<br />
Отправка повторного запроса к /answer/create с теми же id, но другим answer возможна, в таком случае ответ просто перезаписывается.

/answer/complete - метод GET, доступен USER, ADMIN<br />
Принимает на вход access token в Authorization хедере.<br />
Возвращает:<br />
{<br />
    "17": [<br />
        {
            "id": 19,<br />
            "userId": 4,<br />
            "surveyId": 17,<br />
            "questionId": 18,<br />
            "answer": "Yes"<br />
        }<br />
    ],<br />
    "21": [<br />
        {
            "id": 25,<br />
            "userId": 4,<br />
            "surveyId": 21,<br />
            "questionId": 22,<br />
            "answer": "No"<br />
        },<br />
        {<br />
            "id": 26,<br />
            "userId": 4,<br />
            "surveyId": 21,<br />
            "questionId": 23,<br />
            "answer": "No"<br />
        },<br />
        {<br />
            "id": 27,<br />
            "userId": 4,<br />
            "surveyId": 21,<br />
            "questionId": 24,<br />
            "answer": "Всё хорошо!"<br />
        }<br />
    ],<br />
    "9": [<br />
        {<br />
            "id": 16,<br />
            "userId": 4,<br />
            "surveyId": 9,<br />
            "questionId": 11,<br />
            "answer": "No"<br />
        }<br />
    ]<br />
}<br />
Где ключ - это id опроса, внутри находятся ответы Answer на вопросы. Выводятся только те опросы, на которые пользователь дал ВСЕ ответы, то есть нет ни одного неответченного вопроса.<br />
