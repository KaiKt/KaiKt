import groovy.lang.Closure

plugins {
	java
	kotlin("jvm") version "1.9.23"
	id("com.palantir.git-version") version "3.0.0"

	`maven-publish`
}

repositories {
	mavenCentral()
	maven("https://repository.sonatype.org/content/groups/public/")
	maven("https://git.fuwafuwa.moe/api/packages/Taskeren/maven")
}

dependencies {
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("com.squareup.okhttp3:okhttp:4.12.0")
	implementation("org.apache.logging.log4j:log4j-api:2.23.1")
	implementation("org.apache.logging.log4j:log4j-core:2.23.1")
	implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.23.1")
	implementation("org.slf4j:slf4j-api:2.0.12")
	implementation("org.java-websocket:Java-WebSocket:1.5.6")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
	implementation("org.greenrobot:eventbus:3.2.0")
	testImplementation(kotlin("test"))
}

val gitVersion: Closure<String> by extra

println("KaiKt Version ${gitVersion()}")

tasks {
	withType<Test> {
		useJUnitPlatform()
	}

	java {
		withJavadocJar()
		withSourcesJar()
	}

	javadoc {
		if(JavaVersion.current().isJava9Compatible) {
			(options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
		}
	}

	publishing {
		repositories {
			maven {
				name = "fuwafuwa"
				url = uri("https://git.fuwafuwa.moe/api/packages/Taskeren/maven")
				credentials(HttpHeaderCredentials::class.java) {
					name = "Authorization"
					value = "token ${findProperty("GITEA_TOKEN") ?: System.getenv("GITEA_TOKEN")}"
				}
				authentication {
					create<HttpHeaderAuthentication>("header")
				}
			}
		}

		publications {
			create<MavenPublication>("maven") {
				groupId = "cn.taskeren"
				artifactId = "kaikt"
				version = gitVersion()

				from(project.components["java"])

				pom {
					name = "KaiKt"
					description = "Another Kook bot framework."
					url = "https://github.com/KaiKt/KaiKt"
					licenses {
						license {
							name = "SATA-License"
							url = "https://github.com/zTrix/sata-license"
						}
					}
					developers {
						developer {
							id = "taskeren"
							name = "Taskeren Anthony"
							email = "nitu2003@126.com"
						}
					}
					scm {
						connection = "scm:git:https://github.com/KaiKt/KaiKt.git"
						url = "https://github.com/KaiKt/KaiKt"
					}
				}
			}
		}
	}
}