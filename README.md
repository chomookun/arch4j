# ARCH4J (Application Archetype for Java) 

[![Sponsor](https://img.shields.io/badge/Sponsor-%E2%9D%A4-red?logo=github)](https://github.com/sponsors/chomookun)
[![Donate](https://img.shields.io/badge/Donate-Ko--fi-orange?logo=kofi)](https://ko-fi.com/chomookun)

spring-boot-based archetype for web application, batch application, CLI(Command Line Interface) application.


| Subproject                                                                         | Description                              |
|------------------------------------------------------------------------------------|------------------------------------------|
| **arch4j-core**                                                                    | Shared core component archetype project  |
| **arch4j-web**                                                                     | web application archetype project        |
| **arch4j-shell**                                                                   | Shell(CLI) application archetype project |
| **arch4j-batch**                                                                   | Batch application archetype project      |


---


## 🖥️ Demo site

Credentials: **developer/developer**

### Web application (self-hosting)
[![](https://img.shields.io/badge/Self--Hosting-https://arch4j--web.chomookun.org-orange?logo=linux)](https://arch4j-web.chomookun.org)
<br/>
The service is hosted on a personal home server, so performance may be slower.
(No money!!!)

### Web application (google cloud run)
[![](https://img.shields.io/badge/Cloud%20Run-https://gcp.arch4j--web.chomookun.org-blue?logo=google-cloud)](https://gcp.arch4j-web.chomookun.org)
<br/>
Due to a cold start, there is an initialization delay of approximately 30 seconds.<br/>
Trading daemon is not available on the demo site.<br/>
(No money!!!)


---


## 🧪 Running from source code

### Starts arch4j-daemon
Runs the daemon application.
```shell
# starts fintics-daemon
./gradlew :arch4j-daemon:bootRun
```

### Starts arch4j-web
Runs web application.
```shell
# starts arch4j-web
./gradlew :arch4j-web:bootRun
```

---

## 🧪 Running from release binary

Downloads Released archives.

### Starts arch4j-daemon

```shell
./bin/arch4j-daemon
```

### Starts arch4j-web
```shell
./bin/arch4j-web
```

---

## 🧪 Running from container image

### Starts arch4j-daemon
```shell
docker run -rm -p 8081:8081 docker.io/chomoookun/arch4j-daemon:latest
```

### Starts arch4j-web
```shell
docker run -rm -p 8080:8080 docker.io/chomoookun/arch4j-web:latest
```

---

## 🔗 References

### Git source repository
[![](https://img.shields.io/badge/Github-https://github.com/chomoomun/arch4j-green?logo=github)](https://github.com/chomookun/arch4j)

### UI Component javascript library
[![](https://img.shields.io/badge/Duice-https://github.com/chomookun/duice-blue?logo=github)](https://github.com/chomookun/duice)


