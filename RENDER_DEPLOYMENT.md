# Deploy Resume Review Hub on Render

This guide walks you through deploying the entire application (Frontend + Backend + Database) on Render.

## Overview

Render is a modern cloud platform similar to Heroku but with better performance and pricing. Your deployment will include:

- **PostgreSQL Database** (Managed by Render)
- **Spring Boot Backend** (Docker container on Render Service)
- **React Frontend** (Web service on Render)

## Architecture

```
┌─────────────────────────────────────────────────────┐
│          Render Dashboard                           │
├─────────────────────────────────────────────────────┤
│                                                     │
│  ┌──────────────────┐  ┌─────────────────────────┐ │
│  │  Frontend (Web)  │  │  Backend (Service)      │ │
│  │  React/Vite      │  │  Spring Boot (Docker)   │ │
│  │  Static Files    │  │  Port 8080              │ │
│  └────────┬─────────┘  └──────────┬──────────────┘ │
│           │                       │                │
│           └───────────┬───────────┘                │
│                       │                            │
│              ┌────────▼─────────┐                  │
│              │  PostgreSQL DB   │                  │
│              │  Managed Service │                  │
│              │  Port 5432       │                  │
│              └──────────────────┘                  │
│                                                     │
└─────────────────────────────────────────────────────┘
```

## Prerequisites

1. **GitHub Repository** - Your code must be pushed to GitHub
2. **Render Account** - Sign up at https://render.com (free account OK)
3. **PostgreSQL Knowledge** - Basic understanding of databases

## Step-by-Step Deployment

### Step 1: Prepare Your Repository

Make sure all changes are committed and pushed:

```bash
cd /workspaces/resume-review-hub

# Check git status
git status

# Add all files
git add .

# Commit
git commit -m "Add Render deployment configuration"

# Push to GitHub
git push origin main
```

**Files that should be committed:**
- ✅ `render.yaml` - Deployment configuration
- ✅ `backend/Dockerfile` - Backend containerization
- ✅ `backend/.dockerignore` - Docker exclusions
- ✅ `backend/src/main/resources/application-prod.yml` - Production config
- ✅ `.env.example` - Environment reference

### Step 2: Connect GitHub to Render

1. Go to https://render.com
2. Sign up or log in
3. Click **"New +"** in the top right
4. Select **"Blueprint"**
5. Select **"Public Git repository"**
6. Paste your GitHub repository URL:
   ```
   https://github.com/umamage/resume-review-hub
   ```
7. Click **"Connect"**

### Step 3: Authorize Render on GitHub

1. Click **"Install"** to authorize Render
2. Select your GitHub account
3. Give Render access to your repositories
4. Select **resume-review-hub** repository
5. Click **"Install"**
6. Return to Render and confirm authorization

### Step 4: Deploy Using render.yaml

1. Render will automatically detect `render.yaml`
2. Review the configuration:
   - PostgreSQL database
   - Backend service (Spring Boot)
   - Frontend service (React)
3. Click **"Deploy Blueprint"**

### Step 5: Monitor Deployment

The deployment will take 10-15 minutes. Follow these steps:

1. **Database Creation** (~2 minutes)
   - Render creates PostgreSQL database
   - Sets up credentials automatically

2. **Backend Build & Deploy** (~5-8 minutes)
   - Maven builds the Docker image
   - Docker image is pushed to Render
   - Container starts on Render

3. **Frontend Build & Deploy** (~3-5 minutes)
   - npm install and build
   - Static files deployed
   - Frontend served

### Step 6: Verify Deployment

Once all services show "Live":

**Test Backend:**
```bash
# Replace YOUR-BACKEND-URL with your actual URL
curl https://YOUR-BACKEND-URL.onrender.com/api/resumes

# Should return: []
```

**View Frontend:**
- Open: `https://YOUR-FRONTEND-URL.onrender.com`
- You should see the Resume Review Hub application

**View Logs:**
1. In Render dashboard, click on each service
2. Go to **Logs** tab
3. Check for errors or warnings

## Finding Your URLs

After deployment completes:

1. Go to https://render.com/dashboard
2. You'll see three services:
   - **resume-review-frontend** → Copy the URL for frontend
   - **resume-review-backend** → Copy the URL for backend
   - **resume-review-db** → Database (internal only)

Example URLs:
```
Frontend: https://resume-review-frontend-abc123.onrender.com
Backend:  https://resume-review-backend-xyz789.onrender.com
```

## Configure Frontend for Cloud Backend

The frontend needs to know the backend URL. Update it:

1. Go to **resume-review-frontend** service in Render
2. Click **"Environment"** tab
3. Add/update variable:
   ```
   VITE_API_BASE_URL=https://resume-review-backend-xyz789.onrender.com/api
   ```
4. Click **"Save"**
5. Render will automatically redeploy

## Testing on Cloud

Once deployed, you can test the API:

### Option 1: Using Postman

1. Download the Postman collection (from your repo)
2. Import in Postman
3. Create environment with:
   ```
   base_url: https://your-backend-url.onrender.com/api
   resume_id: 1
   job_id: 1
   application_id: 1
   ```
4. Run requests

### Option 2: Using cURL

```bash
# Get all resumes
curl https://YOUR-BACKEND-URL.onrender.com/api/resumes

# Upload resume (from frontend UI)
# The frontend handles file uploads
```

## Common Issues & Solutions

### Issue: Backend returns 502 Bad Gateway

**Cause:** Backend still starting or database not ready

**Solution:**
1. Wait 5 minutes for startup
2. Check backend logs in Render
3. Restart service: Click service → **Restart** button

### Issue: Frontend shows blank page

**Cause:** Incorrect API base URL in frontend environment

**Solution:**
1. Check `VITE_API_BASE_URL` is set correctly
2. Must include `/api` at the end
3. Redeploy frontend after updating

### Issue: Database connection error

**Cause:** Connection string misconfigured

**Solution:**
1. Check backend logs: `render.yaml` should auto-set credentials
2. Verify `SPRING_DATASOURCE_URL` matches database URL
3. Database must be "Live" before backend starts

### Issue: Deployment takes too long

**Cause:** First build is large (Maven dependencies)

**Solution:**
1. Normal for first deploy (10-15 minutes)
2. Subsequent deploys are faster (2-3 minutes)
3. Be patient!

### Issue: Out of memory error

**Cause:** Render free tier has limited resources

**Solution:**
1. Upgrade to paid plan (Starter: $12/month)
2. Or optimize application startup

## Production Deployment Checklist

Before going live:

- ✅ Database is PostgreSQL (done)
- ✅ Backend uses environment variables (done)
- ✅ Frontend points to cloud backend (done)
- ✅ All credentials auto-managed (done)
- ✅ Health checks configured (done)
- ✅ CORS enabled for frontend (check backend code)
- ✅ SSL/HTTPS automatic (done by Render)
- ✅ Database backups enabled (Render automatic)

## Scaling Up

When you need more resources:

### Upgrade Backend
1. Go to **resume-review-backend** service
2. Click **"Settings"**
3. Change **Plan** from "Starter" to "Standard"
4. Service auto-scales

### Upgrade Database
1. Go to **resume-review-db** service
2. Click **"Settings"**
3. Increase **RAM** or **Storage**
4. Database scales automatically

### Enable Auto-scaling
1. Go to service **Settings**
2. Enable **"Auto-scale"** (for paid plans)
3. Set min/max instances

## Custom Domain

To add your own domain (e.g., resumereview.com):

1. Buy domain from registrar (GoDaddy, Namecheap, etc.)
2. In Render, go to frontend service
3. Click **"Settings"** → **"Custom Domain"**
4. Enter your domain
5. Follow DNS instructions from registrar
6. Point DNS to Render nameservers
7. Takes 24-48 hours to propagate

## Environment Variables Reference

**Backend variables (auto-set by Render):**
```
SPRING_DATASOURCE_URL      # PostgreSQL connection string
SPRING_DATASOURCE_USERNAME # Postgres username
SPRING_DATASOURCE_PASSWORD # Postgres password
SPRING_PROFILES_ACTIVE     # Set to "prod"
SERVER_PORT                # Set to 8080
```

**Frontend variables (manual set):**
```
VITE_API_BASE_URL  # Backend URL (e.g., https://backend-xyz.onrender.com/api)
NODE_ENV           # Set to "production"
```

## Monitoring & Logs

### View Logs
1. Go to service in Render dashboard
2. Click **"Logs"** tab
3. Real-time logs appear

### Check Status
1. Services show "Live" when healthy
2. Orange circle = deploying
3. Gray circle = issue

### Restart Service
1. Click service
2. Click **"Restart"** button (top right)
3. Service restarts in ~30 seconds

## Backup & Recovery

Render automatically:
- ✅ Backs up PostgreSQL daily
- ✅ Keeps backups for 7 days
- ✅ Allows point-in-time recovery

To restore:
1. Contact Render support
2. Or manually backup:
   ```bash
   # From your local machine
   pg_dump postgresql://user:pass@db.onrender.com/dbname > backup.sql
   ```

## Next Steps

1. **Test everything** on cloud
2. **Monitor logs** for errors
3. **Optimize performance** if needed
4. **Set up monitoring** (Render dashboard)
5. **Add custom domain** when ready
6. **Enable auto-scaling** for production

## Support & Resources

- **Render Docs:** https://render.com/docs
- **Render Support:** https://render.com/support
- **Spring Boot on Container:** https://spring.io/guides/gs/spring-boot-docker/
- **React Deployment:** https://vitejs.dev/guide/static-deploy.html

## Quick Command Reference

```bash
# Push changes to trigger redeploy
git push origin main

# View build logs (in Render dashboard)
# Click service → Logs

# Redeploy after environment changes
# In Render: Settings → Redeploy

# Check database connection
psql postgresql://user:pass@db.onrender.com/resume_review_db
```

## Troubleshooting Checklist

Before contacting support, verify:

- ✅ All services show "Live" status
- ✅ Database accepts connections
- ✅ Backend logs don't show errors
- ✅ Frontend environment variable set correctly
- ✅ Git repository is public
- ✅ All files committed and pushed
- ✅ `render.yaml` is in root directory

---

**Your application is now deployed on Render!** 🚀

Access it at:
- Frontend: `https://resume-review-frontend-XXX.onrender.com`
- Backend: `https://resume-review-backend-YYY.onrender.com/api`
