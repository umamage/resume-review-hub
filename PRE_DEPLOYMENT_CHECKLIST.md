# Pre-Deployment Checklist

Before pushing to GitHub and deploying on Render, verify:

## Code Quality

- [ ] All changes are committed: `git status` shows clean
- [ ] Latest code is on `main` branch: `git branch`
- [ ] No local uncommitted changes

## Backend Configuration

- [ ] ✅ `pom.xml` has PostgreSQL driver (42.7.1)
- [ ] ✅ `application.yml` uses environment variables for DB
- [ ] ✅ `application-prod.yml` exists with production config
- [ ] ✅ `Dockerfile` uses multi-stage build
- [ ] ✅ `.dockerignore` excludes unnecessary files
- [ ] ✅ `build.sh` script is executable

## Frontend Configuration

- [ ] ✅ `package.json` has all dependencies
- [ ] ✅ `vite.config.ts` configured correctly
- [ ] ✅ Build script runs: `npm run build`
- [ ] ✅ Frontend can access backend via `VITE_API_BASE_URL`

## Deployment Configuration

- [ ] ✅ `render.yaml` in root directory
- [ ] ✅ `render.yaml` specifies all 3 services
- [ ] ✅ Environment variables configured
- [ ] ✅ Database auto-setup enabled
- [ ] ✅ Health checks configured
- [ ] ✅ Service dependencies set

## Documentation

- [ ] ✅ `RENDER_DEPLOYMENT.md` complete
- [ ] ✅ `RENDER_QUICK_START.md` created
- [ ] ✅ `.env.example` exists

## GitHub Setup

- [ ] Repository is public
- [ ] Repository is pushed to GitHub
- [ ] All files are committed (`git push origin main`)

## Pre-Push Commands

```bash
# Verify everything is committed
git status

# Check branch
git branch

# Show what will be pushed
git log -1

# Final push
git push origin main
```

## Ready to Deploy?

If all checkboxes are checked ✅, you're ready!

```bash
# Final verification
git status  # Should say "working tree clean"
git push origin main

# Then go to https://render.com and deploy
```

---

**Start deployment once all items are verified!**
