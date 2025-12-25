dependencies {
    implementation(project(":common"))

    // Spring MVC (REST, search)
    implementation("org.springframework.boot:spring-boot-starter-web")

    // WebFlux (SSE / streaming)
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}
