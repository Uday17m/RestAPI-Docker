# REST API with Docker Compose

This project demonstrates how to use a Java REST API for CRUD operations with a MySQL database, all managed using Docker Compose. The application code is developed in Spring Tool Suite (STS) using Java 17.

## Prerequisites

- Java 17
- Spring Tool Suite (STS)
- Docker and Docker Compose installed on your machine
- MySQL database

---

## Steps to Build and Run the Application

### 1. Develop the REST API

1. Write the REST API code in Spring Tool Suite (STS).
2. Implement CRUD operations:
   - **POST**: Add new records
   - **GET**: Retrieve records
   - **PUT**: Update existing records
   - **DELETE**: Remove records
3. Verify the application works locally.

### 2. Create the Dockerfile

Create a `Dockerfile` to containerize the application. Use the following steps:

1. Define the base image and environment in the Dockerfile.
2. Package the application into a Docker image:
   ```bash
   docker build -t restapi .
   ```
3. Run the containerized application:
   ```bash
   docker run -p 8081:8081 restapi
   ```

### 3. Set Up the MySQL Database

1. Pull the MySQL Docker image:
   ```bash
   docker pull mysql
   ```
2. Create a MySQL container:
   ```bash
   docker run -p 3307:3306 --name mysqlcontainer \
       -e MYSQL_ROOT_PASSWORD=root \
       -e MYSQL_DATABASE=sample-1 \
       -d mysql
   ```

### 4. Connect the Application to MySQL

1. Create a Docker network:
   ```bash
   docker network create networkmysql
   ```
2. Connect the MySQL container to the network:
   ```bash
   docker network connect networkmysql mysqlcontainer
   ```
3. Build the `restapi` image:
   ```bash
   docker build -t restapi .
   ```
4. Run the `restapi` container and connect it to the same network:
   ```bash
   docker run -p 8081:8081 --name restcontainer --net networkmysql \
       -e MYSQL_HOST=mysqlcontainer \
       -e MYSQL_PORT=3306 \
       -e MYSQL_DB_NAME=sample-1 \
       -e MYSQL_USER=root \
       -e MYSQL_PASSWORD=root \
       restapi
   ```

### 5. Create a Docker Compose File

Create a `docker-compose.yml` file to manage multiple containers. Below is an example:

```yaml
version: '3.8'
services:
  restapi:
    build: .
    ports:
      - "8081:8081"
    environment:
      MYSQL_HOST: mysqlcontainer
      MYSQL_PORT: 3306
      MYSQL_DB_NAME: sample-1
      MYSQL_USER: root
      MYSQL_PASSWORD: root
    networks:
      - networkmysql

  mysql:
    image: mysql
    ports:
      - "3307:3306"
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: sample-1
    networks:
      - networkmysql

networks:
  networkmysql:
    driver: bridge
```

### 6. Run the Application with Docker Compose

1. Start the containers using Docker Compose:
   ```bash
   docker compose up -d
   ```
2. Stop and remove the containers:
   ```bash
   docker compose down
   ```

---

## API Endpoints

Test the REST API using Postman or any HTTP client:

- **POST**: Add a new student:
  ```
  POST http://localhost:8081/students/store
  ```
- **GET**: Retrieve all students:
  ```
  GET http://localhost:8081/students/
  ```
- **PUT**: Update an existing student:
  ```
  PUT http://localhost:8081/students/update
  ```
- **DELETE**: Delete a student by ID:
  ```
  DELETE http://localhost:8081/students/delete/{id}
  ```

---

# Deployment Guide: Deploying CRUD Application to AWS EKS
---

## Steps to Deploy CRUD Application

---



1.  MySQL Deployment and Service**
Create a file named `mysql-deployment.yaml` with the following content:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: mysql-db
  labels:
    app: mysql
spec:
  replicas: 1
  selector:
    matchLabels:
      app: mysql
  template:
    metadata:
      labels:
        app: mysql
    spec:
      containers:
        - name: mysql
          image: mysql:latest
          ports:
            - containerPort: 3306
          env:
            - name: MYSQL_DATABASE
              value: sample-1
            - name: MYSQL_USER
              value: root
            - name: MYSQL_PASSWORD
              value: root
            - name: MYSQL_ROOT_PASSWORD
              value: root
          volumeMounts:
            - name: mysql-storage
              mountPath: /var/lib/mysql
      volumes:
        - name: mysql-storage
          persistentVolumeClaim:
            claimName: mysql-pvc
---
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: mysql-pvc
spec:
  accessModes:
    - ReadWriteOnce
  resources:
    requests:
      storage: 1Gi
```

2. Create a file named `mysql-service.yaml`:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: mysql-service
spec:
  ports:
    - port: 3306
      targetPort: 3306
  selector:
    app: mysql
  type: ClusterIP
```

3. Server Deployment and Service**
Create a file named `server-deployment.yaml`:
```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: crud-server
  labels:
    app: crud-server
spec:
  replicas: 1
  selector:
    matchLabels:
      app: crud-server
  template:
    metadata:
      labels:
        app: crud-server
    spec:
      containers:
        - name: crud-server
          image: <your-dockerhub-username>/crud-server:latest
          ports:
            - containerPort: 8081
          env:
            - name: SPRING_DATASOURCE_URL
              value: jdbc:mysql://mysql-service:3306/sample-1
            - name: SPRING_DATASOURCE_USERNAME
              value: root
            - name: SPRING_DATASOURCE_PASSWORD
              value: root
```

4. Create a file named `server-service.yaml`:
```yaml
apiVersion: v1
kind: Service
metadata:
  name: crud-server-service
spec:
  ports:
    - port: 8081
      targetPort: 8081
  selector:
    app: crud-server
  type: LoadBalancer
```

---

 Deploy to AWS EKS**

 Set Context to Your EKS Cluster**
```bash
aws eks --region <your-region> update-kubeconfig --name <your-cluster-name>
```

 Apply Kubernetes Resources**
1. Deploy MySQL resources:
   ```bash
   kubectl apply -f mysql-deployment.yaml
   kubectl apply -f mysql-service.yaml
   ```

2. Deploy Server resources:
   ```bash
   kubectl apply -f server-deployment.yaml
   kubectl apply -f server-service.yaml
   ```

---
. Verify Deployment**
1. Check running pods:
   ```bash
   kubectl get pods
   ```

2. Check services:
   ```bash
   kubectl get services
   ```
   Note the external IP of the `crud-server-service`. This is the LoadBalancer endpoint where your application will be accessible.

---






