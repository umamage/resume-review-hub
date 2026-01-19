# API Testing Guide

This guide provides examples for testing all backend endpoints using cURL or Postman.

## Base URL
```
http://localhost:8080/api
```

---

## 1. Resume Management Endpoints

### Upload a Resume
```bash
curl -X POST \
  -F "file=@/path/to/resume.pdf" \
  http://localhost:8080/api/resumes/upload

# Response:
# {
#   "resumeId": 1,
#   "fileName": "resume.pdf",
#   "fileSize": 102400,
#   "message": "Resume uploaded successfully",
#   "success": true
# }
```

### Get All Resumes
```bash
curl -X GET http://localhost:8080/api/resumes

# Response:
# [
#   {
#     "id": 1,
#     "fileName": "resume.pdf",
#     "filePath": "/uploads/uuid_resume.pdf",
#     "fileSize": 102400,
#     "extractedText": "...",
#     "uploadedAt": "2024-01-19T10:30:00",
#     "status": "UPLOADED"
#   }
# ]
```

### Get Resume by ID
```bash
curl -X GET http://localhost:8080/api/resumes/1

# Response:
# {
#   "id": 1,
#   "fileName": "resume.pdf",
#   ...
# }
```

### Get Resume Status
```bash
curl -X GET http://localhost:8080/api/resumes/1/status

# Response: "UPLOADED"
```

### Update Resume Status
```bash
curl -X PUT "http://localhost:8080/api/resumes/1/status?status=REVIEWED"

# Response:
# {
#   "id": 1,
#   "status": "REVIEWED",
#   ...
# }
```

### Delete Resume
```bash
curl -X DELETE http://localhost:8080/api/resumes/1

# Response: 204 No Content
```

---

## 2. Review Score Endpoints

### Generate Review Score
```bash
curl -X POST http://localhost:8080/api/review-scores/generate/1

# Response:
# {
#   "id": 1,
#   "overallScore": 78.5,
#   "formatScore": 85.0,
#   "contentScore": 72.5,
#   "keywordScore": 77.0,
#   "feedback": "✓ Excellent resume format...\n✓ Good content coverage...",
#   "suggestions": "✓ Add more industry keywords...\n✓ Expand experience section..."
# }
```

### Get Review Score
```bash
curl -X GET http://localhost:8080/api/review-scores/resume/1

# Response:
# {
#   "id": 1,
#   "overallScore": 78.5,
#   ...
# }
```

---

## 3. Job Suggestions Endpoints

### Generate Job Suggestions
```bash
curl -X POST http://localhost:8080/api/job-suggestions/generate/1

# Response:
# [
#   {
#     "id": 1,
#     "jobTitle": "Senior Software Engineer",
#     "company": "Tech Corp",
#     "description": "We are looking for...",
#     "matchScore": 85.5,
#     "location": "Remote / Hybrid",
#     "employmentType": "Full-time",
#     "requiredSkills": "Java, Spring, Microservices",
#     "jobUrl": "https://example.com/jobs/...",
#     "status": "ACTIVE"
#   },
#   {
#     "id": 2,
#     "jobTitle": "Full Stack Developer",
#     "company": "Digital Solutions Inc",
#     "matchScore": 72.0,
#     ...
#   }
# ]
```

### Get Job Suggestions for Resume
```bash
curl -X GET http://localhost:8080/api/job-suggestions/resume/1

# Response: [array of job suggestions]
```

### Get Single Job Suggestion
```bash
curl -X GET http://localhost:8080/api/job-suggestions/1

# Response:
# {
#   "id": 1,
#   "jobTitle": "Senior Software Engineer",
#   ...
# }
```

---

## 4. Job Application Endpoints

### Apply for a Job
```bash
curl -X POST "http://localhost:8080/api/job-applications/apply?jobSuggestionId=1&resumeId=1&applicationNotes=Very%20interested%20in%20this%20role"

# Response:
# {
#   "id": 1,
#   "jobSuggestionId": 1,
#   "jobTitle": "Senior Software Engineer",
#   "company": "Tech Corp",
#   "status": "APPLIED",
#   "applicationNotes": "Very interested in this role",
#   "responseStatus": null
# }
```

### Get Applications for Resume
```bash
curl -X GET http://localhost:8080/api/job-applications/resume/1

# Response: [array of applications]
```

### Get Single Application
```bash
curl -X GET http://localhost:8080/api/job-applications/1

# Response:
# {
#   "id": 1,
#   "jobSuggestionId": 1,
#   ...
# }
```

### Update Application Status
```bash
curl -X PUT "http://localhost:8080/api/job-applications/1/status?status=INTERVIEW_SCHEDULED"

# Response:
# {
#   "id": 1,
#   "status": "INTERVIEW_SCHEDULED",
#   ...
# }
```

### Update Application Response
```bash
curl -X PUT "http://localhost:8080/api/job-applications/1/response?responseStatus=ACCEPTED&responseMessage=Congratulations"

# Response:
# {
#   "id": 1,
#   "responseStatus": "ACCEPTED",
#   ...
# }
```

### Delete Application
```bash
curl -X DELETE http://localhost:8080/api/job-applications/1

# Response: 204 No Content
```

---

## Complete Workflow Example

### Step 1: Upload Resume
```bash
UPLOAD_RESPONSE=$(curl -s -X POST \
  -F "file=@resume.pdf" \
  http://localhost:8080/api/resumes/upload)

RESUME_ID=$(echo $UPLOAD_RESPONSE | jq -r '.resumeId')
echo "Resume uploaded with ID: $RESUME_ID"
```

### Step 2: Generate Review Score
```bash
curl -s -X POST http://localhost:8080/api/review-scores/generate/$RESUME_ID | jq .
```

### Step 3: Generate Job Suggestions
```bash
SUGGESTIONS=$(curl -s -X POST http://localhost:8080/api/job-suggestions/generate/$RESUME_ID)
echo $SUGGESTIONS | jq .

# Extract first job suggestion ID
JOB_ID=$(echo $SUGGESTIONS | jq -r '.[0].id')
echo "First job suggestion ID: $JOB_ID"
```

### Step 4: Apply for Job
```bash
curl -s -X POST "http://localhost:8080/api/job-applications/apply?jobSuggestionId=$JOB_ID&resumeId=$RESUME_ID&applicationNotes=Interested" | jq .
```

### Step 5: Track Application
```bash
curl -s -X GET http://localhost:8080/api/job-applications/resume/$RESUME_ID | jq .
```

---

## Postman Collection

You can import this collection into Postman:

### Resume Endpoints Collection
```json
{
  "info": {
    "name": "Resume Review API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Resume",
      "item": [
        {
          "name": "Upload Resume",
          "request": {
            "method": "POST",
            "url": "{{base_url}}/resumes/upload"
          }
        },
        {
          "name": "Get All Resumes",
          "request": {
            "method": "GET",
            "url": "{{base_url}}/resumes"
          }
        }
      ]
    }
  ]
}
```

---

## Testing with Postman

1. Create a new collection called "Resume Review API"
2. Set environment variable: `base_url = http://localhost:8080/api`
3. Create requests for each endpoint
4. Use form-data for file uploads
5. Use query parameters for request parameters

---

## Error Responses

### Resume Not Found
```bash
curl -X GET http://localhost:8080/api/resumes/999

# Response: 404 Not Found
# {
#   "message": "Resume not found with ID: 999",
#   "error": "RESOURCE_NOT_FOUND",
#   "status": 404,
#   "timestamp": "2024-01-19T10:30:00"
# }
```

### Invalid File
```bash
curl -X POST -F "file=@document.txt" http://localhost:8080/api/resumes/upload

# Response: 400 Bad Request
# {
#   "message": "Invalid file type",
#   "error": "INVALID_ARGUMENT",
#   "status": 400,
#   "timestamp": "2024-01-19T10:30:00"
# }
```

### File Too Large
```bash
# Response: 400 Bad Request
# {
#   "message": "File size exceeds maximum allowed size",
#   "error": "INVALID_ARGUMENT",
#   "status": 400,
#   "timestamp": "2024-01-19T10:30:00"
# }
```

---

## Performance Testing

### Load Test with Apache Bench
```bash
ab -n 1000 -c 10 http://localhost:8080/api/resumes
```

### Load Test with wrk
```bash
wrk -t4 -c100 -d30s http://localhost:8080/api/resumes
```

---

## Debugging Tips

### Enable detailed logging
Edit `application.yml`:
```yaml
logging:
  level:
    com.resumereview: DEBUG
    org.springframework.web: DEBUG
```

### Check database with H2 Console
- Navigate to: `http://localhost:8080/api/h2-console`
- Query: `SELECT * FROM RESUMES;`

### View uploaded files
```bash
ls -la uploads/
```

---

## Testing Tips

1. Always upload a valid PDF resume first
2. Use the returned resume ID for subsequent operations
3. Generate review score before getting suggestions
4. Test error cases (missing files, invalid IDs, etc.)
5. Check database state between tests
6. Monitor logs for debugging

---

For more information, see [README.md](README.md) and [QUICKSTART.md](QUICKSTART.md)
