import com.google.cloud.tools.jib.api.buildplan.ImageFormat
import com.google.cloud.tools.jib.gradle.JibExtension

plugins {
  `java-library`
  idea
  id("com.github.ben-manes.versions") version "0.29.0"
  id("com.google.cloud.tools.jib") version "2.4.0"
  id("org.jetbrains.gradle.plugin.idea-ext") version "0.8.1"
  id("org.sonarqube") version "3.0"
  id("org.springframework.boot") version "2.3.2.RELEASE"
}

group = "dev.hiberbee"
version = "1.0.0-SNAPSHOT"

tasks.withType<JavaCompile> {
  targetCompatibility = JavaVersion.VERSION_14.toString()
  sourceCompatibility = JavaVersion.VERSION_14.toString()
  options.isFork = true
  options.isIncremental = true
}

tasks.withType<Test> {
  useJUnitPlatform()
}

tasks.withType<Wrapper> {
  gradleVersion = "6.6-rc-3"
  distributionType = Wrapper.DistributionType.ALL
}

configure<JibExtension> {
  from {
    image = "openjdk:14"
  }
  to {
    image = "localhost:5000/hiberb/${project.name}"
    tags = setOf("latest")
  }
  container {
    jvmFlags = mutableListOf("-Xms512m")
    mainClass = "dev.hiberbee.example.ExampleApplication"
    ports = listOf("8080")
    labels = mapOf("org.opencontainers.image.authors" to "Vlad Volkov <vlad@hiberbee.com>")
    format = ImageFormat.OCI
  }
}

configurations {
  compileOnly {
    extendsFrom(project.configurations.getByName("annotationProcessor"))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(platform("org.springframework.boot:spring-boot-dependencies:2.3.2.RELEASE"))
  implementation(platform("de.codecentric:spring-boot-admin-dependencies:2.2.4"))
  annotationProcessor(platform("org.springframework.boot:spring-boot-dependencies:2.3.1.RELEASE"))
  implementation("org.springframework.boot:spring-boot-starter-actuator")
  implementation("org.springframework.boot:spring-boot-starter-cache")
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("de.codecentric:spring-boot-admin-starter-client")
  implementation("de.codecentric:spring-boot-admin-starter-server")
  implementation("org.springframework.boot:spring-boot-devtools")
  runtimeOnly("org.hsqldb:hsqldb")
  annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
  annotationProcessor("org.projectlombok:lombok")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
}
