#Con FROM indicamos la imagen:
FROM eclipse-temurin:17-jdk-jammy AS builder

#Indicamos la carpetqa de trabajo
WORKDIR /app/msvc-usuarios

#Copiamos el pom del padre a la carpeta app/
COPY ./pom.xml /app
#Copiamos el wraper de maven
COPY ./msvc-usuarios/.mvn ./.mvn
#Copiamos el ejecutable
COPY ./msvc-usuarios/mvnw .
#Copiamos el pom del hijo
COPY ./msvc-usuarios/pom.xml .

#Añadimso el siguiente comando para que solo descargue las dependencias, ç
#indicamos que se salte los test y la compilacion del main
#indicamos que se salte el repackage
#indicamos que elimine tambien la carpeta target con -rf evitamos el fallo en caso de que no exista
#Con esto empaqueta el codigo fuente pero sin test y descarga las dependencias:
#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot-repackage.skip && rm -rf ./target
#Descargamos las dependencias en modo offline para que no busque librerias ni compile nada
RUN ./mvnw dependency:go-offline

#Copiamos el codigo fuente
COPY ./msvc-usuarios/src ./src

#Compilamos
RUN ./mvnw clean package -DskipTests

#Segunda compilacion
FROM eclipse-temurin:17-jdk-jammy

#Creamos un workdir
WORKDIR /app

#copiamos a la carpeta app el jar de la construccion anterior:
COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#Exponemos el puerto (opcional)
EXPOSE 8001

#Punto de entrada done ejecutamos el jar que hemos traido
ENTRYPOINT ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]