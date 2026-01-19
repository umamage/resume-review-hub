# Render Deployment - Final Configuration Guide

## Current Status

### ✅ Completed Tasks

1. **Frontend Integration with Backend**
   - Created API service (`src/services/api.ts`) with proper environment variable handling
   - Integrated resume upload component with actual backend API calls
   - Added error handling and user feedback
   - Updated score display to use data from backend
   - Implemented localStorage caching for resume data

2. **Production-Ready Frontend Server**
   - Created Node.js Express server (`server.js`) to properly serve SPA
   - Server correctly handles all routes by serving `index.html` for non-static paths
   - Built frontend successfully with `npm run build` (creates `/dist` folder)
   - Added `npm run start` script to start the production server
   - Updated `render.yaml` to use `npm run start` instead of `npm run preview`

3. **Environment Configuration**
   - `VITE_API_BASE_URL` environment variable configured in render.yaml
   - Default API URL fallback logic implemented for development and production
   - CORS properly configured on backend

4. **Build Process**
   - Frontend builds successfully with no errors
   - Express dependency added to package.json
   - render.yaml startCommand updated to use proper Node.js server

### ⏳ Next Steps on Render Dashboard

**1. Configure PostgreSQL Database**
   - Go to Render Dashboard
   - Click "New +" → PostgreSQL
   - Name: `resume-review-db`
   - Note the credentials (host, port, database, user, password)

**2. Set Backend Environment Variables**
   - Go to resume-review-backend service
   - Click "Environment" tab
   - Update the following variables with your PostgreSQL details:
     ```
     SPRING_DATASOURCE_URL=postgresql://[USER]:[PASSWORD]@[HOST]:[PORT]/[DATABASE]
     SPRING_DATASOURCE_USERNAME=[USER]
     SPRING_DATASOURCE_PASSWORD=[PASSWORD]
     SPRING_PROFILES_ACTIVE=prod
     SPRING_JPA_HIBERNATE_DDL_AUTO=update
     SERVER_PORT=8080
     ```
   - Click "Save Changes" → Service will redeploy

**3. Get Backend URL and Configure Frontend**
   - After backend deploys, get its URL (e.g., `https://resume-review-backend-xyz.onrender.com`)
   - Go to resume-review-frontend service
   - Click "Environment" tab
   - Update `VITE_API_BASE_URL` to:
     ```
     https://resume-review-backend-xyz.onrender.com/api
     ```
   - Click "Save Changes" → Frontend will rebuild and redeploy

### 🧪 Testing the Deployment

**After deployment completes:**

1. **Check Frontend**
   ```bash
   curl https://resume-review-hub.onrender.com/
   ```
   Should return HTML with React app

2. **Check Backend Health**
   ```bash
   curl https://resume-review-backend-xyz.onrender.com/api/resumes
   ```
   Should return JSON array or 200 status

3. **Browser Testing**
   - Open https://resume-review-hub.onrender.com/
   - Should see the Resume Review app UI
   - Open browser DevTools (F12)
   - Go to Console tab - should see no errors
   - Go to Network tab - upload a resume
   - Should see API request to `/api/resumes/upload`

### 🐛 Troubleshooting

**If frontend still shows plain HTML:**
- Check browser console for JavaScript errors (F12 → Console)
- Check Network tab to see if API calls are being made
- Verify `VITE_API_BASE_URL` is set correctly in frontend environment

**If API calls fail:**
- Verify backend service is running (check Render logs)
- Verify `VITE_API_BASE_URL` matches actual backend URL
- Check backend logs for errors
- Verify PostgreSQL database is created and credentials are correct

**If "Cannot GET /" error appears:**
- Server might not be built
- Run `npm run build` locally to verify it works
- Check Render build logs

### 📝 Key Files Modified

- `src/services/api.ts` - API client for backend communication
- `src/components/ResumeUpload.tsx` - Updated to use API
- `src/components/ScoreDisplay.tsx` - Updated to display API data
- `server.js` - Production Node.js server
- `package.json` - Added express dependency and start script
- `render.yaml` - Updated frontend startCommand and API URL

### 🚀 Deployment Commands (Reference)

```bash
# Build frontend
npm run build

# Test locally
npm run start

# Push to GitHub
git add .
git commit -m "Add API integration and production server"
git push origin main
```

### 📊 API Endpoints Used

- `POST /api/resumes/upload` - Upload resume and get analysis
- `GET /api/resumes` - Get all resumes
- `GET /api/jobs/suggestions` - Get job suggestions
- `POST /api/jobs/{jobId}/apply` - Apply for a job

All endpoints are documented in the API service (`src/services/api.ts`).

