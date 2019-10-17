plugins {
    application
}

group = "com.github.kaboomboom3"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compile(project(":common"))

    compile(group = "com.github.sh0nk", name = "matplotlib4j", version = "0.4.0")
    compile(group = "commons-cli", name = "commons-cli", version = "1.4")

    testCompile("junit", "junit", "4.12")
}

configure<ApplicationPluginConvention> {
    mainClassName = "com.github.kaboomboom3.simulationtesting.Main"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Javadoc> {
    options {
        showAll()
    }
}
tasks.withType<Wrapper> {
    gradleVersion = "5.6.2"
    distributionType = Wrapper.DistributionType.ALL
}