# BankApp
Proyecto desarrollado en **Spring Boot 3.4.4**, **Maven** y **Java 21**, usando una base **PostgreSQL**. 

A continuación, se detallan los pasos para desplegar la aplicación utilizando **Docker Compose**.

## Requisitos

Antes de comenzar, asegúrate de tener instalados los siguientes requisitos:

- **Docker**: [Instalar Docker](https://www.docker.com/get-started)
- **Docker Compose**: [Instalar Docker Compose](https://docs.docker.com/compose/install/)
- **Java**: OpenJDK 21 (solo para la construcción manual, no necesario si usas Docker)

## Despliegue

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/naczea/bankapp.git
   cd bankapp
   
2. **Construir las imagenes de docker**:

   ```bash
   docker-compose up --build

3. **Si existen problemas con la construcción usar**:

   ```bash
   sudo docker compose up --build
   sudo docker ps
4. **Verificar las imágenes desplegadas**:

   ```bash
   sudo docker ps
5. **Revisar los endpoints en el puerto 8080**:

   ```bash
   http://localhost:8080/appbank
   http://127.0.0.1:8080/appbank
