# 📚 Biblioteca Full Stack

Proyecto **Full Stack** para practicar desarrollo web utilizando:

- **Backend:** Java 17 + Spring Boot
- **Frontend:** React + Vite
- **Base de datos:** MySQL 8 (Docker)

El objetivo del proyecto es implementar un **gestor de biblioteca** con usuarios, libros, autores y préstamos, siguiendo buenas prácticas de arquitectura (capas, services, repositories, DTOs, etc.).

---

## 🧱 Estructura del proyecto

```
biblioteca-fullstack/
├── backend/      # Spring Boot (API REST)
├── frontend/     # React + Vite
├── db/           # Docker Compose (MySQL)
└── README.md
```

---

## ✅ Requisitos

Antes de levantar el proyecto asegurate de tener instalado:

- **Java 17**
- **Node.js 20.19+**
- **Docker Desktop**
- **Git** (opcional, pero recomendado)

---

## 🚀 Levantar el proyecto (modo desarrollo)

### 1️⃣ Levantar la base de datos (MySQL con Docker)

Desde la carpeta `db/`:

```powershell
cd db
docker compose up -d
```

Verificar que esté corriendo:

```powershell
docker ps
```

Deberías ver un contenedor llamado `biblioteca-mysql` en estado **Up**.

---

### 2️⃣ Levantar el backend (Spring Boot)

Desde la carpeta `backend/`:

```powershell
cd backend
.\mvnw.cmd spring-boot:run
```

El backend queda disponible en:

```
http://localhost:8080
```

---

### 3️⃣ Levantar el frontend (React + Vite)

Desde la carpeta `frontend/`:

```powershell
cd frontend
npm install   # solo la primera vez
npm run dev
```

El frontend queda disponible en:

```
http://localhost:5173
```

---

## 🛑 Detener el proyecto

### Backend / Frontend

En la terminal donde están corriendo:

```
Ctrl + C
```

### Base de datos (Docker)

Desde la carpeta `db/`:

```powershell
docker compose down
```

---

## ♻️ Resetear la base de datos

⚠️ **Esto borra todas las tablas y datos**

```powershell
cd db
docker compose down -v
docker compose up -d
```

---

## 🧪 Compilar el backend

Desde `backend/`:

```powershell
.\mvnw.cmd clean package
```

Si querés compilar sin ejecutar tests:

```powershell
.\mvnw.cmd clean package -DskipTests
```

---

## 🧠 Notas de arquitectura

- El backend sigue una arquitectura por capas:
  - `controller`
  - `service`
  - `repository`
  - `model (entity / enums)`

- La lógica de negocio vive en los **services**.
- La base de datos corre completamente en **Docker**.
- Las contraseñas se almacenan **hasheadas** (BCrypt).

---

## 📌 Próximos pasos

- Endpoints REST completos (CRUD)
- Manejo de errores global (`@ControllerAdvice`)
- Autenticación con JWT
- DTOs y validaciones avanzadas
- Migraciones con Flyway

---

## 👨‍💻 Autor

Proyecto desarrollado con fines educativos y de práctica Full Stack.

---

Si algo no levanta correctamente, revisá:
- Que Docker esté corriendo
- Que el puerto **3306** no esté ocupado
- Que Node y Java estén en las versiones requeridas

