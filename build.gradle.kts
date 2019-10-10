plugins {
    application
}

group = "org.frcteam2910"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    compile(group = "com.github.sh0nk", name = "matplotlib4j", version = "0.4.0")

    testCompile("junit", "junit", "4.12")
}

configure<ApplicationPluginConvention> {
    mainClassName = "org.frcteam2910.pidsimexample.Simulation"
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<Wrapper> {
    gradleVersion = "5.6.2"
    distributionType = Wrapper.DistributionType.ALL
}