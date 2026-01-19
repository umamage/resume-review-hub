# Resume Review Backend API

A comprehensive Spring Boot backend for the Resume Review Hub application. This API allows users to upload resumes, receive review scores, get job suggestions, and track job applications.

## Features

- **Resume Upload**: Upload PDF resumes with automatic text extraction
- **Resume Review**: Automated scoring system analyzing format, content, and keywords
- **Job Suggestions**: AI-powered job recommendations based on resume content
- **Job Applications**: Track and manage job applications
- **RESTful API**: Full-featured REST API with CORS support

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/resumereview/
│   │   │   ├── controller/           # REST API endpoints
│   │   │   ├── service/              # Business logic
│   │   │   ├── model/                # JPA entities
│   │   │   ├── repository/           # Data access layer
│   │   │   ├── dto/                  # Data transfer objects
│   │   │   ├── exception/            # Exception handling
│   │   │   ├── ResumeReviewApplication.java
│   │   │   └── WebConfig.java
│   │   └── resources/
│   │       ├── application.yml       # Development config
│   │       └── application-prod.yml  # Production config
│   └── test/
│       └── java/com/resumereview/
└── pom.xml                           # Maven configuration
```

## Technology Stack

- **Java 17**: Programming language
- **Spring Boot 3.2.1**: Framework
- **Spring Data JPA**: ORM and data access
- **H2 Database**: Development database
- **MySQL 8**: Production database
- **Apache PDFBox**: PDF text extraction
- **Lombok**: Reducing boilerplate code
- **Maven**: Build tool

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0 (for production)

## Installation & Setup

### 1. Clone the Repository

```bash
cd /workspaces/resume-review-hub/backend
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

**Development (H2 Database):**
```bash
mvn spring-boot:run
```

**Production (MySQL):**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

The application will start at `http://localhost:8080/api`

### 4. H2 Database Console (Development)

Access the H2 console at: `http://localhost:8080/api/h2-console`
- URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## API Endpoints

### Resume Management

#### Upload Resume
```http
POST /api/resumes/upload
Content-Type: multipart/form-data

Body: file (PDF file)

Response: 201 Created
{
  "resumeId": 1,
  "fileName": "resume.pdf",
  "fileSize": 102400,
  "message": "Resume uploaded successfully",
  "success": true
}
```

#### Get Resume by ID
```http
GET /api/resumes/{id}

Response: 200 OK
{
  "id": 1,
  "fileName": "resume.pdf",
  "filePath": "/uploads/...",
  "fileSize": 102400,
  "extractedText": "...",
  "uploadedAt": "2024-01-19T10:30:00",
  "status": "UPLOADED"
}
```

#### Get All Resumes
```http
GET /api/resumes

Response: 200 OK
[{ resume objects }]
```

#### Update Resume Status
```http
PUT /api/resumes/{id}/status?status=REVIEWED

Response: 200 OK
{ resume object }
```

#### Delete Resume
```http
DELETE /api/resumes/{id}

Response: 204 No Content
```

---

### Review Scores

#### Generate Review Score
```http
POST /api/review-scores/generate/{resumeId}

Response: 201 Created
{
  "id": 1,
  "overallScore": 78.5,
  "formatScore": 85.0,
  "contentScore": 72.5,
  "keywordScore": 77.0,
  "feedback": "...",
  "suggestions": "..."
}
```

#### Get Review Score
```http
GET /api/review-scores/resume/{resumeId}

Response: 200 OK
{ review score object }
```

**Score Breakdown:**
- **Overall Score**: Average of all three scores (0-100)
- **Format Score**: Based on file format and structure (0-100)
- **Content Score**: Based on resume sections and completeness (0-100)
- **Keyword Score**: Based on industry keywords and technical terms (0-100)

---

### Job Suggestions

#### Generate Job Suggestions
```http
POST /api/job-suggestions/generate/{resumeId}

Response: 201 Created
[
  {
    "id": 1,
    "jobTitle": "Senior Software Engineer",
    "company": "Tech Corp",
    "matchScore": 85.5,
    "location": "Remote / Hybrid",
    "employmentType": "Full-time",
    "requiredSkills": "Java, Spring, Microservices",
    "jobUrl": "https://example.com/jobs/senior-software-engineer",
    "status": "ACTIVE"
  },
  ...
]
```

#### Get Job Suggestions
```http
GET /api/job-suggestions/resume/{resumeId}

Response: 200 OK
[{ job suggestion objects }]
```

#### Get Single Job Suggestion
```http
GET /api/job-suggestions/{id}

Response: 200 OK
{ job suggestion object }
```

---

### Job Applications

#### Apply for a Job
```http
POST /api/job-applications/apply?jobSuggestionId=1&resumeId=1&applicationNotes=Optional notes

Response: 201 Created
{
  "id": 1,
  "jobSuggestionId": 1,
  "jobTitle": "Senior Software Engineer",
  "company": "Tech Corp",
  "status": "APPLIED",
  "applicationNotes": "Optional notes",
  "responseStatus": null
}
```

#### Get Applications for Resume
```http
GET /api/job-applications/resume/{resumeId}

Response: 200 OK
[{ application objects }]
```

#### Get Single Application
```http
GET /api/job-applications/{id}

Response: 200 OK
{ application object }
```

#### Update Application Status
```http
PUT /api/job-applications/{id}/status?status=INTERVIEW_SCHEDULED

Response: 200 OK
{ updated application object }
```

#### Update Application Response
```http
PUT /api/job-applications/{id}/response?responseStatus=ACCEPTED&responseMessage=Congratulations!

Response: 200 OK
{ updated application object }
```

#### Delete Application
```http
DELETE /api/job-applications/{id}

Response: 204 No Content
```

---

## Database Schema

### Resumes Table
```sql
CREATE TABLE resumes (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  file_name VARCHAR(255) NOT NULL,
  file_path VARCHAR(500) NOT NULL,
  file_size BIGINT NOT NULL,
  extracted_text LONGTEXT,
  uploaded_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  status VARCHAR(50)
);
```

### Review Scores Table
```sql
CREATE TABLE review_scores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  resume_id BIGINT NOT NULL UNIQUE,
  overall_score DOUBLE NOT NULL,
  format_score DOUBLE NOT NULL,
  content_score DOUBLE NOT NULL,
  keyword_score DOUBLE NOT NULL,
  feedback LONGTEXT,
  suggestions LONGTEXT,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  FOREIGN KEY (resume_id) REFERENCES resumes(id)
);
```

### Job Suggestions Table
```sql
CREATE TABLE job_suggestions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  resume_id BIGINT NOT NULL,
  job_title VARCHAR(255) NOT NULL,
  company VARCHAR(255) NOT NULL,
  description LONGTEXT,
  match_score DOUBLE NOT NULL,
  location VARCHAR(500),
  employment_type VARCHAR(50),
  required_skills VARCHAR(1000),
  job_url VARCHAR(1000),
  suggested_at DATETIME NOT NULL,
  status VARCHAR(20),
  FOREIGN KEY (resume_id) REFERENCES resumes(id)
);
```

### Job Applications Table
```sql
CREATE TABLE job_applications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  job_suggestion_id BIGINT NOT NULL,
  resume_id BIGINT NOT NULL,
  status VARCHAR(50) NOT NULL,
  application_notes LONGTEXT,
  applied_at DATETIME NOT NULL,
  response_date DATETIME,
  response_status VARCHAR(50),
  response_message LONGTEXT,
  FOREIGN KEY (job_suggestion_id) REFERENCES job_suggestions(id),
  FOREIGN KEY (resume_id) REFERENCES resumes(id)
);
```

## Configuration

### Development (application.yml)
- **Database**: H2 in-memory
- **Server Port**: 8080
- **Context Path**: /api
- **Max File Size**: 10MB

### Production (application-prod.yml)
- **Database**: MySQL 8.0
- **Server Port**: 8080
- **Context Path**: /api
- **Connection Details**: Configure environment variables

## Environment Variables

For production deployment, set:
```bash
DB_PASSWORD=your_db_password
```

## Building for Production

### Create JAR Package
```bash
mvn clean package -DskipTests
```

### Run JAR
```bash
java -jar target/resume-review-backend-1.0.0.jar --spring.profiles.active=prod
```

## Error Handling

The API returns standardized error responses:

```json
{
  "message": "Error description",
  "error": "ERROR_TYPE",
  "status": 400,
  "timestamp": "2024-01-19T10:30:00"
}
```

**Common Error Codes:**
- `400 Bad Request`: Invalid input or file format
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## CORS Configuration

CORS is enabled for all origins with:
- Methods: GET, POST, PUT, DELETE, OPTIONS
- Max Age: 3600 seconds
- All headers allowed

## File Upload Configuration

- **Max File Size**: 10MB
- **Allowed Extensions**: .pdf, .doc, .docx
- **Storage Directory**: `./uploads/`

## Logging

Logs are configured by profile:
- **Development**: INFO level, console output
- **Production**: WARN level, file output to `logs/application.log`

## Testing

Run tests with:
```bash
mvn test
```

## Integration with Frontend

The React frontend at `http://localhost:3000` can connect to the backend at:
```
http://localhost:8080/api
```

Update the frontend API base URL in your environment configuration.

## Performance Considerations

1. **File Upload**: PDFs are processed asynchronously
2. **Indexing**: Database indexes on frequently queried fields
3. **Caching**: Consider implementing Redis for score caching
4. **Pagination**: Implement for large result sets

## Future Enhancements

- [ ] AI-based resume analysis using NLP
- [ ] Real job board integration
- [ ] User authentication and authorization
- [ ] Email notifications
- [ ] Advanced analytics and reporting
- [ ] Resume template recommendations
- [ ] Skills matching with job market trends
- [ ] Resume version history tracking

## License

This project is part of the Resume Review Hub application.

## Support

For issues or questions, please contact the development team.
