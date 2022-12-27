# Transcriber
Transcriber is a service for extracting text from any audio and video files.
The service has a web interface based on the thymeleaf template.
The service supports the registration of new users.
Demo functionality is available for all site visitors with a limit on the size of the uploaded file.
There are no restrictions for authorized users. Also, for authorized users, the history of the files that they uploaded is available.

![Image](img/mainPage.png?raw=true)

How the service works:
1. When a file is received, it is saved to disk;
2. Next, using the ffmpeg service, an audio track is cut from the file, converted to wav format and saved to disk;
3. The resulting wav file is redirected to the vosk service, which extracts text from the audio file.

## Dependencies
Transcriber service uses:
- java 11
- lombok
- springboot
- thymeleaf
- junit
- maven

Transcriber also uses the following services which can be started with docker-compose:
- [postgres](https://hub.docker.com/_/postgres)
- [vosk](https://hub.docker.com/r/alphacep/kaldi-en)
- [ffmpeg](https://hub.docker.com/r/kazhar/ffmpeg-api)

## Compile and run
Profiles:
- test - this profile used in memory db h2
- prod - this profile used connection to postgresql

For compile project and run test:
```bash
mvn -B package -DargLine="-Dspring.profiles.active=test" --file pom.xml
```

Idea run configuration:
- AllTest - run all tests with test profile
- TranscriberApplication - run project with test profile
- TranscriberApplicationInit - add default user at first run:
- - username: admin, password: admin, role: ADMIN
- - username: user, password: user, role: USER

Dependent services, can be run by docker-compose:
- postgres
- vosk
- ffmpeg

## Configuration
Transcriber used for configuration application.properties file. There is some custom preference:
```
storage.location=upload-fir #directory for save files
demo.max-file-size=10       #max file size [mb] for demo endpoint

ffmpeg.server.url=127.0.0.1 #url for connect to ffmpeg service
ffmpeg.server.port=3000     #port for connect to ffmpeg service

vosk.server.url=127.0.0.1   #url for connect to vosk service
vosk.server.port=2700       #port for connect to vosk service
```

## Roadmap
- Docker
- Update readme file, add doc
- Add admin panel page. Admin can block another users 
- Add statistic page.

## License