# Quick Start Guide - Resume Review Backend

## Prerequisites
- Java 17+
- Maven 3.6+
- Git
- Optional: Docker & Docker Compose

## Option 1: Local Development Setup

### 1. Build the Project
```bash
cd backend
mvn clean install
```

### 2. Run the Application
```bash
mvn spring-boot:run
```

The application will be available at: `http://localhost:8080/api`

### 3. Test the API

**Upload a Resume:**
```bash
curl -X POST -F "file=@resume.pdf" http://localhost:8080/api/resumes/upload
```

**Get All Resumes:**
```bash
curl http://localhost:8080/api/resumes
```

**Generate Review Score:**
```bash
curl -X POST http://localhost:8080/api/review-scores/generate/1
```

**Generate Job Suggestions:**
```bash
curl -X POST http://localhost:8080/api/job-suggestions/generate/1
```

**Apply for Job:**
```bash
curl -X POST "http://localhost:8080/api/job-applications/apply?jobSuggestionId=1&resumeId=1&applicationNotes=Interested"
```

### 4. Access H2 Console (Development)
- URL: `http://localhost:8080/api/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`

---

## Option 2: Docker Setup

### 1. Build and Run with Docker Compose
```bash
cd backend
mvn clean package -DskipTests
docker-compose up -d
```

Services will be available at:
- **Backend API**: `http://localhost:8080/api`
- **MySQL**: `localhost:3306`

### 2. Stop Services
```bash
docker-compose down
```

### 3. View Logs
```bash
docker-compose logs -f backend
```

---

## Project Structure Quick Reference

```
backend/
├── src/main/java/com/resumereview/
│   ├── controller/          → REST API endpoints
│   ├── service/             → Business logic
│   ├── model/               → Database entities
│   ├── repository/          → Data access
│   ├── dto/                 → Data transfer objects
│   ├── exception/           → Error handling
│   └── ResumeReviewApplication.java
├── src/main/resources/
│   ├── application.yml      → Development config
│   └── application-prod.yml → Production config
├── pom.xml                  → Dependencies
├── Dockerfile               → Docker image
├── docker-compose.yml       → Docker Compose
└── README.md
```

---

## Common Commands

### Maven Commands
```bash
# Clean and install
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Build JAR
mvn clean package

# Skip tests during build
mvn clean package -DskipTests
```

### Production Build & Run
```bash
# Build
mvn clean package -DskipTests

# Run with production profile
java -jar target/resume-review-backend-1.0.0.jar --spring.profiles.active=prod

# With MySQL
export DB_PASSWORD=yourpassword
java -jar target/resume-review-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:mysql://localhost:3306/resume_review_db \
  --spring.datasource.username=resumeuser
```

---

## API Endpoints Summary

| Method | Endpoint | Purpose |
|--------|----------|---------|
| POST | `/resumes/upload` | Upload a resume |
| GET | `/resumes` | Get all resumes |
| GET | `/resumes/{id}` | Get resume by ID |
| DELETE | `/resumes/{id}` | Delete resume |
| POST | `/review-scores/generate/{resumeId}` | Generate review score |
| GET | `/review-scores/resume/{resumeId}` | Get review score |
| POST | `/job-suggestions/generate/{resumeId}` | Generate job suggestions |
| GET | `/job-suggestions/resume/{resumeId}` | Get job suggestions |
| POST | `/job-applications/apply` | Apply for a job |
| GET | `/job-applications/resume/{resumeId}` | Get applications |
| DELETE | `/job-applications/{id}` | Delete application |

---

## Troubleshooting

### Port 8080 Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>
```

### Maven Issues
```bash
# Clear Maven cache
mvn clean

# Rebuild
mvn install
```

### Database Issues (Docker)
```bash
# Reset Docker containers
docker-compose down -v
docker-compose up -d
```

### PDF Extraction Not Working
- Ensure file is a valid PDF
- Check file permissions
- Review logs for errors

---

## Database Setup (MySQL)

For production, manually create the database:

```sql
CREATE DATABASE resume_review_db;
USE resume_review_db;

-- Tables will be created automatically by Hibernate
```

---

## Next Steps

1. ✅ Backend is running
2. 🔌 Connect frontend to `http://localhost:8080/api`
3. 📝 Test endpoints with Postman or cURL
4. 🔐 Add authentication layer (future enhancement)
5. 🚀 Deploy to production

---

## Frontend Integration

Update your React frontend's API base URL:

```typescript
// .env or config file
REACT_APP_API_URL=http://localhost:8080/api
```

Or in your fetch/axios configuration:
```javascript
const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

fetch(`${API_URL}/resumes`)
```

---

## Support & Resources

- Spring Boot Docs: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- H2 Database: https://www.h2database.com/
- Maven: https://maven.apache.org/

---

For more details, see [README.md](README.md)
