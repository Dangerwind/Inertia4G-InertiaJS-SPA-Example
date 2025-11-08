**Spring Boot + Inertia.js + React: Полный пример приложения**
---  
   Это демонстрационное приложение показывает, как интегрировать Spring Boot с Inertia.js и React для 
создания современных SPA-приложений (Single Page Applications) без написания традиционных REST API.

![скрин страницы с продуктами](https://github.com/Dangerwind/Inertia4G-InertiaJS-SPA-Example/blob/main/img/mainscr.png)
   
**Описание проекта**
  
Spring Boot + Inertia.js — это подход к разработке веб-приложений, где:
   
- Backend (Spring Boot): Обрабатывает бизнес-логику, базу данных, валидацию
- Frontend (React): Отвечает за UI/UX, рендерит компоненты
- Inertia.js: Связывает backend и frontend, заменяя традиционные API-запросы на "серверный рендеринг" компонентов
   
Вместо создания JSON API и клиентской маршрутизации, Inertia.js отправляет JSON-ответы с данными и названием 
React-компонента. Браузер рендерит нужный компонент без полной перезагрузки страницы.
Полная навигация SPA — без перезагрузки страниц. Валидация на сервере — ошибки возвращаются в JSON для отображения в 
React. Автоматические редиректы — после POST/PUT/DELETE пользователь перенаправляется на нужную страницу.
   
**Как работает Inertia.js в Spring Boot**
   
app.html (backend/src/main/resources/templates/) — Точка входа, это шаблон, который:

- Подключает CSS/JS ресурсы
- Создает корневой div для React
- Вставляет JSON-данные для первой страницы (SSR)

Ключевой элемент:
<div id="app" data-page="@PageObject@"></div>
PageObject - JSON объект от Spring Boot (компонент + props)
При первом запросе браузер получает полный HTML с данными.
При последующих — XHR запросы с заголовком X-Inertia: true

---
   
Подключаем Inertia в `build.gradle.kts`
```kotlin
implementation("io.github.inertia4j:inertia4j-spring:1.0.4")
```

Импортируем и нжектируем в нашем контроллере:
```java
import io.github.inertia4j.spring.Inertia;

// ваш код

@Autowired
private Inertia inertia;

```
   
В Inertia4J для Spring Boot (адаптер Inertia.js для Java) доступны два ключевых метода для управления ответами:
**inertia.render()** и **inertia.redirect()**
   
Метод **inertia.render()** — рендеринг компонента.
`inertia.render(componentName, props)` — основной метод для отображения React-компонента с данными. 
Он возвращает InertiaPageResponse (со статусом 200, OK), который Inertia4J преобразует в:
- HTML для первого запроса (вставляет JSON в app.html) 
- JSON для Inertia-запросов (компонент + props)
   
Метод **inertia.redirect()** — Редиректы
`inertia.redirect(url)` — метод для редиректов после POST/PUT/DELETE. 
Inertia4J автоматически использует 303 (See Other) статус для POST/PUT/DELETE
и 302 (Found) для GET-запросов. Inertia.js на фронтенде следует за редиректом и загружает новый компонент.
При редиректах можно передавать flash-сообщения на следующую страницу через RequestAttributes. 

В тестах следует отправлять в заголовке X-Inertia: true, и, как минимум проверять статусы ответа и пути редиректа.

---
   
**Установка**
   
Клонируйте репозиторий:
```bash
git clone https://github.com/Dangerwind/Inertia4G-InertiaJS-SPA-Example.git
cd Inertia4G-InertiaJS-SPA-Example
```

Для работы frontend необходимо установить [npm](https://nodejs.org/)

Для запуска backend-приложения требуется:

- Java 21 (JDK)
- Gradle 

Можно воспользоваться скриптом `setup-java-backend.sh`, который автоматически установит и настроит
Java и Gradle для вашей системы.
```bash
./setup-java-backend.sh
```

Откройте 2 окна терминала, перейдите в папку проекта Inertia4G-InertiaJS-SPA-Exampl.
В одном окне терминала запустите сборку frontend:
```bash
make build-frontend:
```

Далее там же запустите frontend:
```bash
make run-frontend
```
Терминал напишет что сервер запустился на http://localhost:5173/

Во втором терминале запустите backend:
```bash
make run-backend
```

Сервер запустится на http://localhost:8080 и по этой ссылке будет доступна главная страница приложения.

