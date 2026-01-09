dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.1")
    testImplementation("io.projectreactor:reactor-test:3.6.1")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
