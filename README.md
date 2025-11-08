Spring Boot + Inertia.js + React: Полный пример приложения
Это демонстрационное приложение показывает, как интегрировать Spring Boot с Inertia.js и React для 
создания современных SPA-приложений (Single Page Applications) без написания традиционных REST API. 
Проект демонстрирует полный цикл работы с данными: от отображения списка товаров до создания новых товаров.

Описание проекта
Spring Boot + Inertia.js — это подход к разработке веб-приложений, где:
Backend (Spring Boot): Обрабатывает бизнес-логику, базу данных, валидацию
Frontend (React): Отвечает за UI/UX, рендерит компоненты
Inertia.js: Связывает backend и frontend, заменяя традиционные API-запросы на "серверный рендеринг" компонентов
Вместо создания JSON API и клиентской маршрутизации, Inertia.js отправляет JSON-ответы с данными и названием 
React-компонента. Браузер рендерит нужный компонент без полной перезагрузки страницы.
Полная навигация SPA — без перезагрузки страниц. Валидация на сервере — ошибки возвращаются в JSON для отображения в 
React. Автоматические редиректы — после POST/PUT/DELETE пользователь перенаправляется на нужную страницу.

Преимущества подхода
Для разработчиков
Единый стек: Один язык (Java) для backend и логики, React только для UI
Меньше кода: Нет нужды в API endpoints, serializers, client-side state
Знакомая архитектура: Как традиционный MVC, но с SPA поведением
Валидация на сервере: Автоматическая обработка ошибок в формах
SEO-friendly: Полноценный HTML на первом запросе

Backend:
```bash
cd backend
./gradlew build
```
```bash
./gradlew Run
```

Сервер запустится на http://localhost:8080


Frontend:
cd frontend
npm install

npm run dev


Как работает Inertia.js в Spring Boot
app.html (backend/src/main/resources/templates/) — Точка входа, это шаблон, который:
Подключает CSS/JS ресурсы
Создает корневой div для React
Вставляет JSON-данные для первой страницы (SSR)

Ключевой элемент:
<div id="app" data-page="@PageObject@"></div>
PageObject - JSON объект от Spring Boot (компонент + props)
При первом запросе браузер получает полный HTML с данными
При последующих — XHR запросы с заголовком X-Inertia: true

Подключаем Inertia в build.gradle.kts
```kotlin
implementation("io.github.inertia4j:inertia4j-spring:1.0.4")
```
Инжектируем в нашем контроллере:
```java
@Autowired
private Inertia inertia;
```
В Inertia4J для Spring Boot (адаптер Inertia.js для Java) доступны два ключевых метода для управления ответами:
inertia.render() и inertia.redirect().

Метод inertia.render() — Рендеринг компонента
inertia.render(componentName, props) — основной метод для отображения React-компонента с данными. 
Он возвращает InertiaPageResponse (со статусом 200, OK), который Inertia4J преобразует в:

HTML для первого запроса (вставляет JSON в app.html) 
JSON для Inertia-запросов (компонент + props)


Метод inertia.redirect() — Редиректы
inertia.redirect(url) — метод для редиректов после POST/PUT/DELETE. 
Inertia4J автоматически использует 303 (See Other) статус для POST/PUT/DELETE
и 302 (Found) для GET-запросов. Inertia.js на фронтенде автоматически следует за редиректом и загружает новый компонент.
При редиректах можно передавать flash-сообщения на следующую страницу через RequestAttributes. 

В тестах следует отправлять в заголовке X-Inertia: true, и, как минимум проверять статусы ответа и пути редиректа.

sudo apt update && sudo apt upgrade -y
sudo apt install -y curl unzip zip git build-essential ca-certificates

curl -s "https://get.sdkman.io" | bash
# затем открой новый терминал или выполни:
source "$HOME/.sdkman/bin/sdkman-init.sh"

sdk install java 21.* 

sdk install gradle

java -version
gradle -v



./setup-java-backend.sh
source ~/.bashrc