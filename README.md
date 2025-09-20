Сервіс приймає відгуки через Telegram бота, робить аналіз через ChatGPT та зберігає відгук у БД, дублює в Google Docs і створює Trello-карти для критичних відгуків з оцінкою 4-5.

Щоб створити телеграм бота, потрібно у телеграмі знайти - **@BotFather**

**Приклад створення**

<img width="626" height="301" alt="1" src="https://github.com/user-attachments/assets/c4246fb8-0406-41d5-9b55-a4b00d2ec479" />

Далі після успішної реєстрації бота вам буде виданий API токен

<img width="592" height="338" alt="2" src="https://github.com/user-attachments/assets/280f46ab-78c7-4f02-9cf5-7c7bba8b920e" />

У проекті відкрити файл конфігурації проекту

<img width="319" height="160" alt="3" src="https://github.com/user-attachments/assets/eb7c76d3-2d49-427a-a067-618e22de08ae" />

Та заповнити його як на скріншоті

<img width="398" height="403" alt="image" src="https://github.com/user-attachments/assets/21668011-a1bd-4e9e-b4e5-d5bc032dcbe6" />

Щоб підключитися до локальної БД можна заповнити ці поля наступим чином.

Підключення та створення БД обов'язкове.

<img width="598" height="89" alt="4" src="https://github.com/user-attachments/assets/2703d29a-04c2-418e-b4a5-f11a9580accc" />

Щоб підключити ChatGPT у проект, потрібно перейти за посиланням та зарєеструвати акаунт - https://platform.openai.com/

У пошуку зліва потрібно ввести API keys

<img width="559" height="197" alt="image" src="https://github.com/user-attachments/assets/4e4df02c-295b-4f25-be23-16666cb06486" />

Та створити новий secret-key, який потім скопіювати у **OPEN_AI_KEY**

Вибрати модель ChatGPT який буде обробляти відгуки можна за наступним посиланням - https://platform.openai.com/docs/pricing

Наприклад: gpt-4o-mini та заповнити поле **OPEN_AI_MODEL**

У поле **OPEN_AI_URL** скопіювати і вставити наступне посилання - https://api.openai.com/v1/chat/completions

Його можна знайти тут - https://platform.openai.com/docs/api-reference/chat

Для підключення до Google Docs API потрібно створити новий пустий документ зі свого Google акаунту, відкрити його з правами Редактора (Editor), та скопіювати айді документу та заповнити **GOOGLE_DOCUMENT_ID**

<img width="585" height="43" alt="image" src="https://github.com/user-attachments/assets/cf26ee69-9bc1-44b5-b863-f83edecf5a7b" />

За наступним посиланням створити новий проект - https://console.cloud.google.com/projectselector2/iam-admin

Далі відкрити проект та вибрати APIs & Services

<img width="1137" height="248" alt="image" src="https://github.com/user-attachments/assets/161e90f8-0dcc-48e1-8ecc-3d331d2b7699" />

Зверху нажати **+ Enable APIs and services**

<img width="476" height="65" alt="image" src="https://github.com/user-attachments/assets/dc40a157-5043-4c9f-b0c9-894a64b25363" />

У пошуку знайти **Google Docs** та вибрати наступний API

<img width="471" height="111" alt="image" src="https://github.com/user-attachments/assets/ef911631-e31c-4350-918c-d4a6d5ebc43a" />

Нажати enable

<img width="399" height="219" alt="image" src="https://github.com/user-attachments/assets/cb939463-6c10-446b-b6b5-ce058aaeb525" />

Далі повернутись до проекту та вибрати IAM & Admin

<img width="1146" height="260" alt="image" src="https://github.com/user-attachments/assets/a7348270-2709-4ea8-900d-fdd251380863" />

У меню зліва вибрати **Service accounts** -> **+ Create service account**.

Після створення проекту зайти в нього та вибрати вкладку **Keys** та створити новий ключ у JSON форматі.

<img width="630" height="111" alt="image" src="https://github.com/user-attachments/assets/f22ff49e-622d-4223-a8a4-c2391ff6797b" />

Цей json файл потрібно перемістити за наступним шляхом у проекті - src\main\resources\Ваш ключ.json

<img width="464" height="212" alt="image" src="https://github.com/user-attachments/assets/8c1a7b48-2a71-4232-bed1-e37365ec2f25" />

Щоб все працювало корректно скопіюйте його абсолютний шлях -> ПКМ -> Copy Path/Reference -> Absolute Path та вставити цей шлях у **GOOGLE_KEY**

В **GOOGLE_APP_NAME** потрібно ввести назву свого проекту, наприклад Feedback App.

Щоб підключити **Trello** потрібно зареєструвати акаунт за посиланням trello.com, після успішної реєстрації вам відкриється доступ до API за наступним посиланням - https://trello.com/power-ups/admin/

Потрібно створити новий проект заповнивши відповіді поля та нажати create

<img width="777" height="776" alt="image" src="https://github.com/user-attachments/assets/1e087b99-a7f1-4a20-b81c-2cc644b4c98c" />

Після успішного створення проекту вас перекине до API keys де потрібно сгенерувати його та вставити до **TRELLO_KEY** у проекті.

За наступним посиланням вам потрібно отримати токен - https://trello.com/1/authorize?expiration=never&name=FeedbackApp&scope=read,write&response_type=token&key=API_KEY

Замість **API_KEY** вставити ваш API ключ який ви отримали раніше. та нажати Allow

<img width="534" height="296" alt="image" src="https://github.com/user-attachments/assets/08a19187-b054-4bdb-b1d8-2b61afa9816a" />

Отриманий токен скопіюйте до **TRELLO_TOKEN** у проекті.

Далі створіть нову Trello дошку на домашній сторінці.

<img width="636" height="163" alt="image" src="https://github.com/user-attachments/assets/ec14ed32-5d92-400f-8551-77360615c653" />

Створіть нову карточку, куди будуть записуватись критичні відгуки.

Щоб отримати id карточки, потрібно перейти за наступним посиланням - https://api.trello.com/1/boards/BOARD_ID/lists?key=API_KEY&token=TOKEN

У поле BOARD_ID вам потрібно ввести айді дошки. Щоб дізнатись його, потрібно зайти у свою створену дошку та скопіювати наступне

<img width="393" height="43" alt="image" src="https://github.com/user-attachments/assets/4d817bfb-3c57-4fc4-bf76-78e302865e35" />

Замість полів API_KEY TOKEN вам потрібно ввести раніше отримані данні.

Якщо ви ввели все вірно, ви повинні отримані ось такі поля у браузері, де потрібно лише поле id, яке потрібно скопіювати у **TRELLO_LIST_ID**

<img width="310" height="259" alt="image" src="https://github.com/user-attachments/assets/913db365-cc9c-4202-b472-2557c1ad66be" />

На даному етапі проект сконфігуровано та ним можна користуватися.

Адмін панель знаходиться за посиланням - http://localhost:8080/main.html
