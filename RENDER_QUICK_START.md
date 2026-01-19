# Quick Render Deployment Guide

## What Was Created

✅ **render.yaml** - Deployment configuration for all 3 services  
✅ **backend/Dockerfile** - Multi-stage Docker build  
✅ **backend/.dockerignore** - Optimize Docker build  
✅ **application-prod.yml** - Production database configuration  
✅ **RENDER_DEPLOYMENT.md** - Complete deployment guide  
✅ **Build scripts** - Automated build processes  

## 3-Step Deployment

### 1. Push to GitHub
```bash
cd /workspaces/resume-review-hub
git add .
git commit -m "Add Render deployment configuration"
git push origin main
```

### 2. Deploy on Render
- Go to https://render.com
- Sign up (free account)
- Click **"New +" → "Blueprint"**
- Paste: `https://github.com/umamage/resume-review-hub`
- Click **"Deploy Blueprint"**

### 3. Wait for Deployment
- Takes **10-15 minutes** for first deploy
- PostgreSQL database auto-created
- Backend auto-built with Maven
- Frontend auto-built with npm

## After Deployment

**Your URLs:**
- Frontend: `https://resume-review-frontend-XXX.onrender.com`
- Backend: `https://resume-review-backend-YYY.onrender.com/api`
- Database: Auto-managed by Render

**Configure Frontend:**
1. Go to **resume-review-frontend** in Render dashboard
2. **Environment** tab
3. Set: `VITE_API_BASE_URL=https://your-backend-url.onrender.com/api`
4. Click **Save** (auto-redeploy)

**Test Backend:**
```bash
curl https://your-backend-url.onrender.com/api/resumes
# Should return: []
```

## What's Included

| Component | Technology | Status |
|-----------|-----------|--------|
| **Frontend** | React + Vite | Deployed |
| **Backend** | Spring Boot + Java 17 | Deployed |
| **Database** | PostgreSQL 15 | Managed by Render |
| **SSL/HTTPS** | Auto-generated | Enabled |
| **Backups** | Daily automatic | 7-day retention |
| **Auto-deploy** | Git push trigger | On main branch |

## Environment Variables

**Automatically Set by Render:**
```
SPRING_DATASOURCE_URL          ← PostgreSQL URL
SPRING_DATASOURCE_USERNAME     ← Postgres user
SPRING_DATASOURCE_PASSWORD     ← Postgres password
SPRING_PROFILES_ACTIVE=prod    ← Production mode
SERVER_PORT=8080               ← Backend port
```

**Manually Set:**
```
VITE_API_BASE_URL              ← Backend URL for frontend
NODE_ENV=production            ← Production mode
```

## Troubleshooting

| Problem | Solution |
|---------|----------|
| Backend shows "502 Bad Gateway" | Wait 5 min, check logs |
| Frontend blank page | Set `VITE_API_BASE_URL` correctly |
| Database connection error | Database should be "Live" first |
| Deployment fails | Push to GitHub again to trigger rebuild |

## Cost

- **PostgreSQL Database:** $15/month (Starter)
- **Backend Service:** $12/month (Starter)
- **Frontend Service:** Free (static site)
- **Total:** ~$27/month for starter tier

**Free tier available** for testing (limited resources).

## Auto-Redeploy

Changes auto-deploy when you push to `main`:
```bash
git push origin main
# Render automatically redeploys in ~2-3 minutes
```

## View Logs

1. Go to https://render.com/dashboard
2. Click on service name
3. **Logs** tab shows real-time output

## Next Steps

1. ✅ Push code to GitHub
2. ✅ Deploy on Render
3. ✅ Test API endpoints
4. ✅ Configure custom domain (optional)
5. ✅ Monitor performance

**Full details in [RENDER_DEPLOYMENT.md](RENDER_DEPLOYMENT.md)**

---

**Your app is production-ready!** 🚀
