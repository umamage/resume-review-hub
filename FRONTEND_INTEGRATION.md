# Frontend-Backend Integration Guide

This guide explains how to integrate your React frontend with the Spring Boot backend.

## 1. Backend Setup

The backend is fully set up at `/workspaces/resume-review-hub/backend/`

### Start the Backend
```bash
cd backend
mvn clean install
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080/api`

---

## 2. Frontend Configuration

### Update API Base URL

Create or update `.env` file in the React frontend root:

```bash
# .env
REACT_APP_API_URL=http://localhost:8080/api
```

Or for production:
```bash
REACT_APP_API_URL=https://your-backend-domain.com/api
```

### Install Axios (if not already installed)

```bash
npm install axios
```

---

## 3. Create API Service

Create `src/services/api.ts` in your React frontend:

```typescript
import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Resume endpoints
export const resumeAPI = {
  upload: (file: File) => {
    const formData = new FormData();
    formData.append('file', file);
    return api.post('/resumes/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  },
  getAll: () => api.get('/resumes'),
  getById: (id: number) => api.get(`/resumes/${id}`),
  updateStatus: (id: number, status: string) => 
    api.put(`/resumes/${id}/status?status=${status}`),
  delete: (id: number) => api.delete(`/resumes/${id}`),
};

// Review Score endpoints
export const reviewScoreAPI = {
  generate: (resumeId: number) => 
    api.post(`/review-scores/generate/${resumeId}`),
  get: (resumeId: number) => 
    api.get(`/review-scores/resume/${resumeId}`),
};

// Job Suggestion endpoints
export const jobSuggestionAPI = {
  generate: (resumeId: number) => 
    api.post(`/job-suggestions/generate/${resumeId}`),
  getByResume: (resumeId: number) => 
    api.get(`/job-suggestions/resume/${resumeId}`),
  getById: (id: number) => api.get(`/job-suggestions/${id}`),
};

// Job Application endpoints
export const jobApplicationAPI = {
  apply: (jobSuggestionId: number, resumeId: number, notes?: string) =>
    api.post('/job-applications/apply', null, {
      params: {
        jobSuggestionId,
        resumeId,
        ...(notes && { applicationNotes: notes }),
      },
    }),
  getByResume: (resumeId: number) => 
    api.get(`/job-applications/resume/${resumeId}`),
  getById: (id: number) => api.get(`/job-applications/${id}`),
  updateStatus: (id: number, status: string) =>
    api.put(`/job-applications/${id}/status?status=${status}`),
  updateResponse: (id: number, responseStatus: string, message: string) =>
    api.put(`/job-applications/${id}/response`, null, {
      params: { responseStatus, responseMessage: message },
    }),
  delete: (id: number) => api.delete(`/job-applications/${id}`),
};

export default api;
```

---

## 4. Connect Components to Backend

### Example: Resume Upload Component

```typescript
import { useState } from 'react';
import { resumeAPI, reviewScoreAPI, jobSuggestionAPI } from '@/services/api';

export default function ResumeUpload() {
  const [file, setFile] = useState<File | null>(null);
  const [resumeId, setResumeId] = useState<number | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleUpload = async () => {
    if (!file) return;

    setLoading(true);
    setError(null);

    try {
      // Upload resume
      const uploadResponse = await resumeAPI.upload(file);
      const newResumeId = uploadResponse.data.resumeId;
      setResumeId(newResumeId);

      // Generate review score
      await reviewScoreAPI.generate(newResumeId);

      // Generate job suggestions
      await jobSuggestionAPI.generate(newResumeId);

      console.log('Resume processed successfully!');
    } catch (err) {
      setError('Failed to upload resume. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <input
        type="file"
        accept=".pdf"
        onChange={(e) => setFile(e.target.files?.[0] || null)}
      />
      <button onClick={handleUpload} disabled={!file || loading}>
        {loading ? 'Uploading...' : 'Upload Resume'}
      </button>
      {error && <div className="error">{error}</div>}
      {resumeId && <div>Resume ID: {resumeId}</div>}
    </div>
  );
}
```

### Example: Review Score Display

```typescript
import { useEffect, useState } from 'react';
import { reviewScoreAPI } from '@/services/api';

interface ReviewScore {
  overallScore: number;
  formatScore: number;
  contentScore: number;
  keywordScore: number;
  feedback: string;
  suggestions: string;
}

export default function ReviewScoreDisplay({ resumeId }: { resumeId: number }) {
  const [score, setScore] = useState<ReviewScore | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!resumeId) return;

    const fetchScore = async () => {
      try {
        const response = await reviewScoreAPI.get(resumeId);
        setScore(response.data);
      } catch (error) {
        console.error('Failed to fetch review score:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchScore();
  }, [resumeId]);

  if (loading) return <div>Loading...</div>;
  if (!score) return <div>No score available</div>;

  return (
    <div className="score-container">
      <h2>Resume Review Score</h2>
      <div className="score">
        <div>Overall: {score.overallScore.toFixed(1)}/100</div>
        <div>Format: {score.formatScore.toFixed(1)}</div>
        <div>Content: {score.contentScore.toFixed(1)}</div>
        <div>Keywords: {score.keywordScore.toFixed(1)}</div>
      </div>
      <div className="feedback">{score.feedback}</div>
      <div className="suggestions">{score.suggestions}</div>
    </div>
  );
}
```

### Example: Job Suggestions Component

```typescript
import { useEffect, useState } from 'react';
import { jobSuggestionAPI } from '@/services/api';

interface JobSuggestion {
  id: number;
  jobTitle: string;
  company: string;
  matchScore: number;
  location: string;
  employmentType: string;
  requiredSkills: string;
  jobUrl: string;
}

export default function JobSuggestions({ resumeId }: { resumeId: number }) {
  const [suggestions, setSuggestions] = useState<JobSuggestion[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!resumeId) return;

    const fetchSuggestions = async () => {
      try {
        const response = await jobSuggestionAPI.getByResume(resumeId);
        setSuggestions(response.data);
      } catch (error) {
        console.error('Failed to fetch job suggestions:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchSuggestions();
  }, [resumeId]);

  if (loading) return <div>Loading jobs...</div>;
  if (suggestions.length === 0) return <div>No suggestions available</div>;

  return (
    <div className="jobs-container">
      <h2>Suggested Jobs</h2>
      {suggestions.map((job) => (
        <div key={job.id} className="job-card">
          <h3>{job.jobTitle}</h3>
          <p>{job.company}</p>
          <p>Match Score: {job.matchScore.toFixed(1)}%</p>
          <p>Location: {job.location}</p>
          <p>Type: {job.employmentType}</p>
          <p>Skills: {job.requiredSkills}</p>
          <a href={job.jobUrl} target="_blank" rel="noopener noreferrer">
            View Job
          </a>
        </div>
      ))}
    </div>
  );
}
```

### Example: Job Application Component

```typescript
import { useState } from 'react';
import { jobApplicationAPI } from '@/services/api';

export default function JobApplicationForm({
  jobSuggestionId,
  resumeId,
}: {
  jobSuggestionId: number;
  resumeId: number;
}) {
  const [notes, setNotes] = useState('');
  const [loading, setLoading] = useState(false);
  const [success, setSuccess] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleApply = async () => {
    setLoading(true);
    setError(null);

    try {
      await jobApplicationAPI.apply(jobSuggestionId, resumeId, notes);
      setSuccess(true);
      setNotes('');
      setTimeout(() => setSuccess(false), 3000);
    } catch (err) {
      setError('Failed to apply for job. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="application-form">
      <textarea
        value={notes}
        onChange={(e) => setNotes(e.target.value)}
        placeholder="Add notes about your application..."
      />
      <button onClick={handleApply} disabled={loading}>
        {loading ? 'Applying...' : 'Apply for Job'}
      </button>
      {success && <div className="success">Applied successfully!</div>}
      {error && <div className="error">{error}</div>}
    </div>
  );
}
```

---

## 5. Environment Configuration

### Development
```bash
# .env.development
REACT_APP_API_URL=http://localhost:8080/api
```

### Production
```bash
# .env.production
REACT_APP_API_URL=https://api.yourdomain.com
```

---

## 6. Error Handling

Add a global error handler:

```typescript
import axios, { AxiosError } from 'axios';

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.response?.status === 404) {
      console.error('Resource not found');
    } else if (error.response?.status === 400) {
      console.error('Bad request');
    } else if (error.response?.status === 500) {
      console.error('Server error');
    }
    return Promise.reject(error);
  }
);
```

---

## 7. CORS Configuration

The backend is configured with CORS enabled. If you encounter CORS issues:

1. Ensure backend is running
2. Check the CORS configuration in `WebConfig.java`
3. Update allowed origins if needed

---

## 8. Build & Deploy

### Frontend Build
```bash
npm run build
```

### Backend Build
```bash
cd backend
mvn clean package -DskipTests
```

### Docker Deployment
```bash
# Build both services
docker-compose up -d

# Backend available at: http://localhost:8080/api
# Frontend available at: http://localhost:3000
```

---

## 9. Testing the Integration

### 1. Start Backend
```bash
cd backend
mvn spring-boot:run
```

### 2. Start Frontend (separate terminal)
```bash
npm run dev
```

### 3. Test Upload
- Upload a PDF resume
- Check console for API calls
- Verify response in browser dev tools

### 4. Check Backend Logs
```bash
# Watch logs from backend
docker-compose logs -f backend
```

---

## 10. Troubleshooting

### CORS Error
```
Access to XMLHttpRequest blocked by CORS policy
```
**Solution**: Ensure backend is running and CORS is enabled

### 404 Not Found
```
POST /api/resumes/upload 404
```
**Solution**: Verify backend context path is `/api`

### Connection Refused
```
Failed to fetch: connect ECONNREFUSED
```
**Solution**: Start backend on port 8080

### Timeout Issues
```
Request timeout
```
**Solution**: Check backend health and increase timeout in axios config

---

## 11. Performance Tips

1. **Lazy Load Components**: Use React.lazy for job suggestions
2. **Cache Results**: Store resume scores locally
3. **Debounce Searches**: Prevent multiple API calls
4. **Error Boundaries**: Catch component errors gracefully
5. **Loading States**: Show spinners during API calls

---

## 12. Security Best Practices

1. ✓ Use HTTPS in production
2. ✓ Validate file types on frontend
3. ✓ Sanitize user input
4. ✓ Use environment variables for secrets
5. ✓ Implement rate limiting
6. ✓ Add authentication (JWT tokens)

---

## Complete Example: Full Workflow

```typescript
// Main component workflow
async function processResume(file: File) {
  try {
    // 1. Upload resume
    const uploadRes = await resumeAPI.upload(file);
    const resumeId = uploadRes.data.resumeId;

    // 2. Generate review score
    const scoreRes = await reviewScoreAPI.generate(resumeId);
    displayScore(scoreRes.data);

    // 3. Generate job suggestions
    const jobsRes = await jobSuggestionAPI.generate(resumeId);
    displayJobs(jobsRes.data);

    // 4. Apply for first job
    if (jobsRes.data.length > 0) {
      await jobApplicationAPI.apply(
        jobsRes.data[0].id,
        resumeId,
        'Interested in this role'
      );
    }

    // 5. Get applications history
    const appsRes = await jobApplicationAPI.getByResume(resumeId);
    displayApplications(appsRes.data);
  } catch (error) {
    handleError(error);
  }
}
```

---

## Next Steps

1. ✅ Backend is set up and running
2. ✅ Frontend API service is configured
3. 🔌 Connect your existing React components to the API
4. 🧪 Test all endpoints
5. 🚀 Deploy to production

For detailed API documentation, see: [backend/README.md](backend/README.md)

For testing examples, see: [backend/API_TESTING.md](backend/API_TESTING.md)

---

**Ready to integrate!** 🎉
