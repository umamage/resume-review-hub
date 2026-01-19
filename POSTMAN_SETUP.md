# Postman Setup Guide - Resume Review Hub API

## Overview

This guide will help you import the Postman collection and start testing the Resume Review Hub API with PostgreSQL backend.

## Prerequisites

- [Postman](https://www.postman.com/downloads/) (desktop or web)
- Backend running on `http://localhost:8080/api`
- PostgreSQL database running on `localhost:5432`

## Step 1: Import the Postman Collection

### Option A: Direct Import from File

1. **Open Postman**
   - Launch the Postman desktop application or go to web.postman.co

2. **Import Collection**
   - Click "Import" button (top left)
   - Select "Upload Files"
   - Navigate to: `backend/Resume-Review-Hub-API.postman_collection.json`
   - Click "Import"

### Option B: Manual Import

1. Click "Collections" on the left sidebar
2. Click "New" → "Collection"
3. Name it "Resume Review Hub API"
4. Add the endpoints manually from this guide

## Step 2: Configure Environment Variables

The collection uses these variables. You can set them globally or per request:

```
base_url: http://localhost:8080/api
resume_id: 1
job_id: 1
application_id: 1
```

### To Set Environment Variables in Postman:

1. Click the "Environments" icon (top left)
2. Click "Create new environment"
3. Name it "Resume Review - Dev"
4. Add variables:
   - `base_url`: `http://localhost:8080/api`
   - `resume_id`: `1`
   - `job_id`: `1`
   - `application_id`: `1`
5. Click "Save"

## Step 3: Start the Backend with PostgreSQL

### Option A: Docker Compose (Recommended)

```bash
cd backend
mvn clean package -DskipTests
docker-compose up -d
```

Wait for both services to be healthy:
```bash
docker-compose ps
```

### Option B: Local PostgreSQL + Maven

1. **Install PostgreSQL locally** (if not already installed)
   ```bash
   # macOS
   brew install postgresql
   
   # Ubuntu
   sudo apt-get install postgresql postgresql-contrib
   
   # Windows
   # Download from https://www.postgresql.org/download/windows/
   ```

2. **Create database and user:**
   ```bash
   psql -U postgres
   ```

   In the PostgreSQL prompt:
   ```sql
   CREATE DATABASE resume_review_db;
   CREATE USER postgres WITH PASSWORD 'postgres';
   GRANT ALL PRIVILEGES ON DATABASE resume_review_db TO postgres;
   \q
   ```

3. **Start backend:**
   ```bash
   cd backend
   mvn clean install
   mvn spring-boot:run
   ```

Backend will be ready when you see:
```
Tomcat started on port(s): 8080 (http)
```

## Step 4: Test the API

### Complete Workflow

Follow this sequence to test all features:

#### 1. Upload a Resume
- **Request**: `Upload Resume` in Postman
- **Method**: POST
- **Body**: Form-data with file field set to your PDF resume
- **Expected Response**: 201 Created with resume ID

```json
{
  "resumeId": 1,
  "fileName": "resume.pdf",
  "fileSize": 102400,
  "message": "Resume uploaded successfully",
  "success": true
}
```

**Save the resume ID** (update `resume_id` variable)

#### 2. Get All Resumes
- **Request**: `Get All Resumes`
- **Method**: GET
- **Expected Response**: 200 OK with array of resumes

#### 3. Get Resume by ID
- **Request**: `Get Resume by ID`
- **Method**: GET
- **URL**: `{{base_url}}/resumes/{{resume_id}}`
- **Expected Response**: 200 OK with resume details

#### 4. Generate Review Score
- **Request**: `Generate Review Score`
- **Method**: POST
- **URL**: `{{base_url}}/review-scores/generate/{{resume_id}}`
- **Expected Response**: 201 Created with scores

```json
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

#### 5. Get Review Score
- **Request**: `Get Review Score`
- **Method**: GET
- **URL**: `{{base_url}}/review-scores/resume/{{resume_id}}`
- **Expected Response**: 200 OK with review score

#### 6. Generate Job Suggestions
- **Request**: `Generate Job Suggestions`
- **Method**: POST
- **URL**: `{{base_url}}/job-suggestions/generate/{{resume_id}}`
- **Expected Response**: 201 Created with job array

**Save a job ID from the response** (update `job_id` variable)

#### 7. Get Job Suggestions
- **Request**: `Get Job Suggestions for Resume`
- **Method**: GET
- **URL**: `{{base_url}}/job-suggestions/resume/{{resume_id}}`
- **Expected Response**: 200 OK with jobs array

#### 8. Apply for Job
- **Request**: `Apply for Job`
- **Method**: POST
- **URL Parameters**:
  - `jobSuggestionId`: {{job_id}}
  - `resumeId`: {{resume_id}}
  - `applicationNotes`: "Your notes here"
- **Expected Response**: 201 Created with application

**Save the application ID** (update `application_id` variable)

#### 9. Get Applications
- **Request**: `Get Applications for Resume`
- **Method**: GET
- **Expected Response**: 200 OK with applications array

#### 10. Update Application Status
- **Request**: `Update Application Status`
- **Method**: PUT
- **URL Parameters**:
  - `status`: "INTERVIEW_SCHEDULED"
- **Expected Response**: 200 OK with updated application

#### 11. Update Application Response
- **Request**: `Update Application Response`
- **Method**: PUT
- **URL Parameters**:
  - `responseStatus`: "ACCEPTED"
  - `responseMessage`: "Congratulations you're hired!"
- **Expected Response**: 200 OK

## Step 5: Testing Tips

### Use Variables
Update variables in Postman after each response to use the returned IDs:

1. After uploading resume: Set `resume_id` to the returned ID
2. After generating jobs: Set `job_id` to the first job ID
3. After applying: Set `application_id` to the returned ID

### Check Response Status Codes

| Status | Meaning |
|--------|---------|
| 200 OK | Success |
| 201 Created | Resource created |
| 204 No Content | Success with no body |
| 400 Bad Request | Invalid request |
| 404 Not Found | Resource not found |
| 500 Error | Server error |

### View Response Bodies

Click the "Body" tab to see:
- JSON response
- Headers
- Cookies

### Test Error Cases

1. **Try getting non-existent resume**: `GET /resumes/999`
   - Expected: 404 Not Found

2. **Try uploading non-PDF file**: Use text file in upload
   - Expected: 400 Bad Request

3. **Try applying twice for same job**: Apply twice with same IDs
   - Expected: 400 Bad Request with "Already applied"

## Step 6: Manage Collections

### Save Responses
1. Click "Send" on any request
2. Click "Save response"
3. Name it (e.g., "Resume Upload Success")
4. Use it as example for other requests

### Create Folders for Organization
- Already organized in the collection:
  - Resume Management (5 endpoints)
  - Review Scores (2 endpoints)
  - Job Suggestions (3 endpoints)
  - Job Applications (6 endpoints)

### Add Pre-request Scripts
To automate variable updates, add pre-request script:

```javascript
// Get resume ID from URL
var url = pm.request.url.toString();
if (url.includes("/resumes/")) {
    var resumeId = url.split("/").pop();
    pm.environment.set("resume_id", resumeId);
}
```

## Step 7: Troubleshooting

### Connection Refused
```
Error: connect ECONNREFUSED 127.0.0.1:8080
```
**Solution**: Ensure backend is running
```bash
mvn spring-boot:run
# or
docker-compose up -d
```

### 404 Not Found
```
{
  "message": "Resume not found with ID: 999",
  "error": "RESOURCE_NOT_FOUND",
  "status": 404
}
```
**Solution**: Use valid IDs from previous requests

### 400 Bad Request
```
{
  "message": "File cannot be empty",
  "error": "INVALID_ARGUMENT",
  "status": 400
}
```
**Solution**: Check request parameters and body

### CORS Error
```
Access to XMLHttpRequest blocked by CORS policy
```
**Solution**: Backend CORS is configured, ensure you're using correct URL with `/api` context

### Database Connection Error
```
ERROR: Unexpected error: org.postgresql.util.PSQLException:
Connection refused
```
**Solution**: Ensure PostgreSQL is running:
```bash
docker-compose ps
# or
pg_isready
```

## PostgreSQL Connection Details

When using local PostgreSQL or Docker:

```
Host: localhost
Port: 5432
Database: resume_review_db
User: postgres
Password: postgres
```

To verify connection:
```bash
psql -h localhost -U postgres -d resume_review_db
```

Then check tables:
```sql
\dt
SELECT * FROM resumes;
```

## Advanced Testing

### Run Complete Workflow as Tests

1. Open `Resume Review Hub API` collection
2. Click "..." (three dots)
3. Select "Run collection"
4. Configure:
   - Environment: "Resume Review - Dev"
   - Iterations: 1
   - Delay: 100ms
5. Click "Run Resume Review Hub API"

### Export Results

1. After running collection
2. Click "Export Results"
3. Save as JSON for reporting

### Validate with Tests

Add test scripts to requests. Example for "Upload Resume":

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has resumeId", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.resumeId).to.be.a('number');
});

pm.test("Response has success true", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.success).to.equal(true);
});
```

## Next Steps

1. ✅ Import collection
2. ✅ Configure environment
3. ✅ Start backend with PostgreSQL
4. ✅ Test complete workflow
5. 📊 Analyze responses
6. 🐛 Debug any issues
7. 🚀 Ready for deployment

## Useful Resources

- [Postman Documentation](https://learning.postman.com/)
- [API Testing Best Practices](https://learning.postman.com/docs/writing-scripts/test-scripts/)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

---

**Happy Testing!** 🚀
