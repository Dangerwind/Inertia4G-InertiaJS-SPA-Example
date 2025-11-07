plugins {
	application
	id("org.springframework.boot") version "3.5.7"
	id("io.spring.dependency-management") version "1.1.7"
	id("io.freefair.lombok") version "8.6"
	id("java")
}

group = "com.github.Dangerwind"
version = "0.0.1-SNAPSHOT"
description = "Demo project using Spring Boot, Inertia.js and React"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
	// Если inertia4j-core всё же потребуется явно и не подтянется транзитивно:
	// maven { url = uri("https://maven.pkg.github.com/Inertia4J/inertia4j") }
	// credentials { username = findProperty("gpr.user") as String?; password = findProperty("gpr.key") as String? }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2")

	// Inertia4J Spring adapter (1.0.4)
	implementation("io.github.inertia4j:inertia4j-spring:1.0.4")

	//JsonNullableModule для сериализации JSON
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")

	// чтобы мапить модели в dto
	implementation("org.mapstruct:mapstruct:1.6.3")
	annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


}

tasks.withType<Test> {
	useJUnitPlatform()
}

application {
	mainClass.set("com.github.Dangerwind.springbootinertiareact.App")
}
