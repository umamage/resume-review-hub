# Complete API Testing Guide - PostgreSQL Backend

## Prerequisites

- Backend running on `http://localhost:8080/api`
- PostgreSQL running on `localhost:5432`
- Postman installed or use web version at postman.co

## Import Postman Collection

1. Download collection: `backend/Resume-Review-Hub-API.postman_collection.json`
2. Open Postman
3. Click "Import" → "Upload Files"
4. Select the JSON file
5. Collection is now ready to use

## Quick Test Checklist

- [ ] Database connection verified
- [ ] Backend running without errors
- [ ] Postman collection imported
- [ ] Environment variables set
- [ ] Upload resume successfully
- [ ] Generate review score
- [ ] Get job suggestions
- [ ] Apply for job
- [ ] All endpoints tested

## Complete Testing Workflow

### 1. Upload a Resume

**Endpoint**: POST `/resumes/upload`

**Steps in Postman**:
1. Open "Upload Resume" request
2. Go to "Body" tab
3. Select "form-data"
4. Key: `file` (type: File)
5. Click "Select Files" and choose a PDF resume
6. Click "Send"

**Expected Response** (201 Created):
```json
{
  "resumeId": 1,
  "fileName": "resume.pdf",
  "fileSize": 48523,
  "message": "Resume uploaded successfully",
  "success": true
}
```

**What to do next**: 
- Copy the `resumeId` value
- Update Postman variable `{{resume_id}}` to this value

---

### 2. Verify Resume Upload

**Endpoint**: GET `/resumes/{{resume_id}}`

**Steps**:
1. Open "Get Resume by ID" request
2. Click "Send"

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "fileName": "resume.pdf",
  "filePath": "/uploads/uuid_resume.pdf",
  "fileSize": 48523,
  "extractedText": "John Doe\nSoftware Engineer...",
  "uploadedAt": "2024-01-19T10:30:00",
  "updatedAt": "2024-01-19T10:30:00",
  "status": "UPLOADED"
}
```

**Verification Points**:
- ✓ Resume ID matches what we uploaded
- ✓ Extracted text is not empty
- ✓ File size matches
- ✓ Status is "UPLOADED"

---

### 3. Get All Resumes

**Endpoint**: GET `/resumes`

**Steps**:
1. Open "Get All Resumes" request
2. Click "Send"

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "fileName": "resume.pdf",
    ...
  },
  {
    "id": 2,
    "fileName": "another-resume.pdf",
    ...
  }
]
```

---

### 4. Generate Review Score

**Endpoint**: POST `/review-scores/generate/{{resume_id}}`

**Steps**:
1. Open "Generate Review Score" request
2. Click "Send"
3. Wait for processing (may take 2-5 seconds)

**Expected Response** (201 Created):
```json
{
  "id": 1,
  "overallScore": 78.5,
  "formatScore": 85.0,
  "contentScore": 72.5,
  "keywordScore": 77.0,
  "feedback": "• Excellent resume format and structure.\n• Good content with room for improvement.\n• Good keyword usage. Consider adding more industry-specific terms.",
  "suggestions": "✓ Add a detailed 'Experience' section if missing.\n✓ Create a 'Skills' section highlighting...\n✓ Use action verbs to describe achievements."
}
```

**What the scores mean**:
- **Format Score** (85.0): Layout, structure, and readability
- **Content Score** (72.5): Completeness of sections and details
- **Keyword Score** (77.0): Technical skills and industry terms
- **Overall Score** (78.5): Average of all three scores

**Interpretation**:
- 80-100: Excellent
- 70-79: Good
- 60-69: Fair
- Below 60: Needs improvement

---

### 5. Get Review Score

**Endpoint**: GET `/review-scores/resume/{{resume_id}}`

**Steps**:
1. Open "Get Review Score" request
2. Click "Send"

**Expected Response** (200 OK):
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

---

### 6. Generate Job Suggestions

**Endpoint**: POST `/job-suggestions/generate/{{resume_id}}`

**Steps**:
1. Open "Generate Job Suggestions" request
2. Click "Send"
3. Wait for processing

**Expected Response** (201 Created):
```json
[
  {
    "id": 1,
    "jobTitle": "Senior Software Engineer",
    "company": "Tech Corp",
    "description": "We are looking for a talented Senior Software Engineer...",
    "matchScore": 85.5,
    "location": "Remote / Hybrid",
    "employmentType": "Full-time",
    "requiredSkills": "5+ years experience, Leadership, System Design",
    "jobUrl": "https://example.com/jobs/senior-software-engineer",
    "status": "ACTIVE"
  },
  {
    "id": 2,
    "jobTitle": "Full Stack Developer",
    "company": "Digital Solutions Inc",
    "matchScore": 72.0,
    ...
  },
  ...
]
```

**Match Score Interpretation**:
- 80+: Highly qualified
- 70-79: Well qualified
- 60-69: Qualified
- Below 60: May need additional skills

**What to do next**:
- Copy the first job's `id` 
- Update Postman variable `{{job_id}}` to this value

---

### 7. View Job Suggestions

**Endpoint**: GET `/job-suggestions/resume/{{resume_id}}`

**Steps**:
1. Open "Get Job Suggestions for Resume" request
2. Click "Send"

**Expected Response** (200 OK):
- Array of job suggestions (same as generation response)

---

### 8. Apply for a Job

**Endpoint**: POST `/job-applications/apply`

**Parameters**:
- `jobSuggestionId`: {{job_id}}
- `resumeId`: {{resume_id}}
- `applicationNotes`: Your custom notes (optional)

**Steps**:
1. Open "Apply for Job" request
2. Update parameters if needed
3. Click "Send"

**Expected Response** (201 Created):
```json
{
  "id": 1,
  "jobSuggestionId": 1,
  "jobTitle": "Senior Software Engineer",
  "company": "Tech Corp",
  "status": "APPLIED",
  "applicationNotes": "Very interested in this position",
  "responseStatus": null
}
```

**What to do next**:
- Copy the application `id`
- Update Postman variable `{{application_id}}` to this value

---

### 9. Get All Applications

**Endpoint**: GET `/job-applications/resume/{{resume_id}}`

**Steps**:
1. Open "Get Applications for Resume" request
2. Click "Send"

**Expected Response** (200 OK):
```json
[
  {
    "id": 1,
    "jobSuggestionId": 1,
    "jobTitle": "Senior Software Engineer",
    "company": "Tech Corp",
    "status": "APPLIED",
    "applicationNotes": "Very interested in this position",
    "responseStatus": null
  }
]
```

---

### 10. Update Application Status

**Endpoint**: PUT `/job-applications/{{application_id}}/status`

**Parameters**:
- `status`: "INTERVIEW_SCHEDULED" (or other status)

**Possible Status Values**:
- APPLIED
- INTERVIEW_SCHEDULED
- INTERVIEW_COMPLETED
- OFFER_EXTENDED
- ACCEPTED
- REJECTED
- WITHDRAWN

**Steps**:
1. Open "Update Application Status" request
2. Change status parameter to desired status
3. Click "Send"

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "jobSuggestionId": 1,
  "jobTitle": "Senior Software Engineer",
  "company": "Tech Corp",
  "status": "INTERVIEW_SCHEDULED",
  "applicationNotes": "Very interested in this position",
  "responseStatus": null
}
```

---

### 11. Record Employer Response

**Endpoint**: PUT `/job-applications/{{application_id}}/response`

**Parameters**:
- `responseStatus`: "ACCEPTED" (or REJECTED, PENDING)
- `responseMessage`: "Congratulations! We'd like to offer you the position..."

**Steps**:
1. Open "Update Application Response" request
2. Update parameters with employer response
3. Click "Send"

**Expected Response** (200 OK):
```json
{
  "id": 1,
  "jobSuggestionId": 1,
  "jobTitle": "Senior Software Engineer",
  "company": "Tech Corp",
  "status": "INTERVIEW_SCHEDULED",
  "applicationNotes": "Very interested in this position",
  "responseStatus": "ACCEPTED"
}
```

---

## Error Testing

### Test 404 - Resume Not Found

**Request**: GET `/resumes/999`

**Expected Response** (404 Not Found):
```json
{
  "message": "Resume not found with ID: 999",
  "error": "RESOURCE_NOT_FOUND",
  "status": 404,
  "timestamp": "2024-01-19T10:35:00"
}
```

### Test 400 - Already Applied

**Request**: POST `/job-applications/apply` (apply twice for same job)

**Expected Response** (400 Bad Request):
```json
{
  "message": "Already applied for this job",
  "error": "INVALID_ARGUMENT",
  "status": 400,
  "timestamp": "2024-01-19T10:36:00"
}
```

### Test 400 - Invalid Resume ID

**Request**: GET `/job-suggestions/resume/999` (non-existent)

**Expected Response** (404 Not Found):
```json
{
  "message": "Resume not found with ID: 999",
  "error": "RESOURCE_NOT_FOUND",
  "status": 404,
  "timestamp": "2024-01-19T10:37:00"
}
```

---

## Advanced Testing

### Using Postman Test Scripts

Add this to the "Tests" tab of "Upload Resume" request:

```javascript
pm.test("Status code is 201", function () {
    pm.response.to.have.status(201);
});

pm.test("Response has resumeId", function () {
    var jsonData = pm.response.json();
    pm.expect(jsonData.resumeId).to.be.a('number');
});

pm.test("Set resume_id variable", function () {
    var jsonData = pm.response.json();
    pm.environment.set("resume_id", jsonData.resumeId);
});
```

### Run Full Collection

1. Click "Resume Review Hub API" collection
2. Click "..." (three dots)
3. Select "Run collection"
4. Click "Run Resume Review Hub API"

This runs all requests in sequence!

---

## Performance Testing

### Measure Response Time

Click on response → "Response time" shows in milliseconds

**Expected Times**:
- Upload: 500-2000ms
- Generate Score: 1000-3000ms
- Generate Jobs: 500-1500ms
- Other GETs: 100-500ms

### Load Testing (Advanced)

Use Apache Bench:
```bash
ab -n 100 -c 10 http://localhost:8080/api/resumes
```

---

## Database Verification

### Connect to PostgreSQL

```bash
psql -h localhost -U postgres -d resume_review_db
```

### Check Created Data

```sql
-- View all resumes
SELECT * FROM resumes;

-- View review scores
SELECT * FROM review_scores;

-- View job suggestions
SELECT * FROM job_suggestions;

-- View applications
SELECT * FROM job_applications;
```

---

## Testing Scenarios

### Scenario 1: Perfect Resume
1. Upload resume from strong candidate
2. Generate score (should be 80+)
3. Generate jobs (multiple high-match jobs)
4. Apply to top 3 jobs
5. Verify all applications saved

### Scenario 2: Weak Resume
1. Upload minimal resume
2. Generate score (should be below 70)
3. Check suggestions for improvement
4. Generate jobs (fewer suggestions)

### Scenario 3: Full Recruitment Workflow
1. Upload 3 different resumes
2. Generate scores for each
3. Generate jobs for each
4. Apply to multiple jobs
5. Update status as "INTERVIEW_SCHEDULED"
6. Record "ACCEPTED" response

### Scenario 4: Error Handling
1. Try uploading empty file
2. Try getting non-existent resume
3. Try applying twice for same job
4. Try using invalid IDs

---

## Postman Tips

### Set Default Base URL
1. Go to Environments
2. Edit "Resume Review - Dev"
3. Set `base_url` = `http://localhost:8080/api`
4. All requests will use this

### Use Pre-request Scripts
Add to collection level for automatic setup:

```javascript
// Get current environment
var env = pm.environment;

// Set base URL if not set
if (!env.get("base_url")) {
    env.set("base_url", "http://localhost:8080/api");
}
```

### Save Request Examples
After each successful request:
1. Click "Send"
2. Response appears
3. Click "Save response"
4. Name: "Resume Upload - Success"

---

## Common Issues & Solutions

| Issue | Solution |
|-------|----------|
| 401 Unauthorized | No authentication required (not implemented yet) |
| 400 Bad Request | Check parameters and body format |
| 404 Not Found | Verify IDs from previous responses |
| 500 Server Error | Check backend logs: `docker-compose logs backend` |
| Connection Refused | Ensure backend is running: `mvn spring-boot:run` |
| CORS Error | Backend CORS is configured, use correct URL |
| Database Error | Check PostgreSQL: `docker-compose ps` |

---

## Next Steps

✅ Complete all tests in this guide
✅ Verify all 16 endpoints work
✅ Test error scenarios
✅ Review response formats
✅ Check database entries
✅ Ready for frontend integration!

---

**Start testing now!** 🚀

Need help? Check:
- [Postman Documentation](https://learning.postman.com/)
- [backend/API_TESTING.md](../backend/API_TESTING.md) for cURL examples
- [POSTMAN_SETUP.md](POSTMAN_SETUP.md) for setup details
