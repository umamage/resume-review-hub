# PostgreSQL Setup & Usage Guide

## Quick Start with Docker Compose

### Start PostgreSQL + Backend
```bash
cd backend
mvn clean package -DskipTests
docker-compose up -d
```

### Verify Services
```bash
docker-compose ps
# Both postgres and resume-review-backend should show as "Up"
```

### View Logs
```bash
docker-compose logs -f postgres
docker-compose logs -f backend
```

### Stop Services
```bash
docker-compose down
```

## Local PostgreSQL Setup

### Installation

#### macOS
```bash
brew install postgresql
brew services start postgresql
```

#### Ubuntu/Debian
```bash
sudo apt-get update
sudo apt-get install postgresql postgresql-contrib
sudo systemctl start postgresql
```

#### Windows
Download from: https://www.postgresql.org/download/windows/

### Create Database and User

Connect to PostgreSQL:
```bash
psql -U postgres
```

In PostgreSQL prompt:
```sql
-- Create database
CREATE DATABASE resume_review_db;

-- Create user (if using custom credentials)
CREATE USER postgres WITH PASSWORD 'postgres';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE resume_review_db TO postgres;

-- Connect to database
\c resume_review_db

-- Verify (list tables - will be empty initially)
\dt

-- Exit
\q
```

### Connection Details
```
Host: localhost
Port: 5432
Database: resume_review_db
User: postgres
Password: postgres
```

### Test Connection
```bash
psql -h localhost -U postgres -d resume_review_db
```

## Application Configuration

### Default Configuration (development)

The `application.yml` is configured for local PostgreSQL:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/resume_review_db
    username: postgres
    password: postgres
    driverClassName: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
```

### Environment Variables

```bash
# For Docker Compose (automatically set)
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/resume_review_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres
```

### Custom Configuration

Update `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://your-host:5432/your-db
    username: your-user
    password: your-password
```

## Running the Backend

### Option 1: Maven (Local PostgreSQL)
```bash
cd backend
mvn spring-boot:run
```

Backend will be available at: `http://localhost:8080/api`

### Option 2: Docker Compose (PostgreSQL in Container)
```bash
cd backend
docker-compose up -d
```

Backend will be available at: `http://localhost:8080/api`

### Option 3: Built JAR
```bash
cd backend
mvn clean package -DskipTests
java -jar target/resume-review-backend-1.0.0.jar
```

## Database Management

### Connect to PostgreSQL

#### Using psql CLI
```bash
psql -h localhost -U postgres -d resume_review_db
```

#### Using pgAdmin (GUI)
```bash
# Install pgAdmin
brew install pgadmin4  # macOS

# Then access at http://localhost:5050
```

### View Database Schema

In PostgreSQL prompt:
```sql
-- List all tables
\dt

-- Describe a table
\d resumes
\d review_scores
\d job_suggestions
\d job_applications

-- List column details
SELECT * FROM information_schema.columns 
WHERE table_name='resumes';
```

### Query Data

```sql
-- List all resumes
SELECT * FROM resumes;

-- List resumes uploaded in last hour
SELECT * FROM resumes 
WHERE uploaded_at > NOW() - INTERVAL '1 hour';

-- Count applications by status
SELECT status, COUNT(*) FROM job_applications 
GROUP BY status;

-- View review scores with resume names
SELECT r.file_name, rs.overall_score, rs.format_score
FROM review_scores rs
JOIN resumes r ON rs.resume_id = r.id
ORDER BY rs.overall_score DESC;

-- Find jobs by match score
SELECT * FROM job_suggestions 
WHERE match_score > 80 
ORDER BY match_score DESC;
```

### Clear Data (Development)

```sql
-- Truncate all tables (remove all data)
TRUNCATE TABLE job_applications CASCADE;
TRUNCATE TABLE job_suggestions CASCADE;
TRUNCATE TABLE review_scores CASCADE;
TRUNCATE TABLE resumes CASCADE;
```

### Drop and Recreate Database

```bash
# Stop backend first
# In PostgreSQL prompt:
psql -U postgres
```

```sql
-- Drop database
DROP DATABASE resume_review_db;

-- Create fresh database
CREATE DATABASE resume_review_db;

-- Exit
\q
```

## Backup and Restore

### Backup Database

```bash
# Backup to file
pg_dump -h localhost -U postgres resume_review_db > backup.sql

# Or custom format (more efficient)
pg_dump -h localhost -U postgres -Fc resume_review_db > backup.dump
```

### Restore Database

```bash
# Restore from SQL file
psql -h localhost -U postgres resume_review_db < backup.sql

# Or from custom format
pg_restore -h localhost -U postgres -d resume_review_db backup.dump
```

## Performance

### Check Active Connections
```sql
SELECT datname, usename, count(*) 
FROM pg_stat_activity 
GROUP BY datname, usename;
```

### Monitor Query Performance
```sql
-- Enable query timing
\timing

-- Then run your query
SELECT * FROM resumes LIMIT 1;
```

### Check Indexes
```sql
SELECT tablename, indexname 
FROM pg_indexes 
WHERE schemaname = 'public';
```

### Vacuum Database (Cleanup)
```sql
VACUUM ANALYZE;
```

## Docker-Specific Commands

### Access PostgreSQL Container

```bash
# Connect to container
docker exec -it resume-review-postgres psql -U postgres -d resume_review_db

# From inside container, you can run SQL commands
```

### View Container Logs

```bash
# View PostgreSQL logs
docker logs resume-review-postgres

# View backend logs
docker logs resume-review-backend

# Follow logs in real-time
docker logs -f resume-review-postgres
```

### Check Container Status

```bash
# List containers
docker ps

# Show container stats
docker stats resume-review-postgres

# Inspect container
docker inspect resume-review-postgres | jq '.[] | .NetworkSettings.IPAddress'
```

### Restart Services

```bash
# Restart PostgreSQL only
docker-compose restart postgres

# Restart backend
docker-compose restart backend

# Restart all
docker-compose restart
```

## Troubleshooting

### PostgreSQL Won't Start

```bash
# Check if port 5432 is in use
lsof -i :5432

# Or for Docker
docker ps -a | grep postgres

# Check logs
docker logs resume-review-postgres

# Solution: Use different port or stop conflicting service
```

### Connection Refused

```bash
# Verify PostgreSQL is running
pg_isready

# Check connection
psql -h localhost -U postgres -d resume_review_db

# Output should show:
# accepting connections
```

### Permission Denied

```bash
# Check user permissions
psql -U postgres -c "SELECT * FROM pg_user WHERE usename='postgres';"

# Grant all privileges
psql -U postgres -d resume_review_db -c "GRANT ALL PRIVILEGES ON DATABASE resume_review_db TO postgres;"
```

### Backend Can't Connect to Database

**Error**: `ERROR: Unexpected error: org.postgresql.util.PSQLException: Connection refused`

**Solutions**:

1. Check PostgreSQL is running:
   ```bash
   pg_isready
   # or for Docker
   docker-compose ps
   ```

2. Check credentials in `application.yml`:
   ```yaml
   datasource:
     url: jdbc:postgresql://localhost:5432/resume_review_db
     username: postgres
     password: postgres
   ```

3. Check network (if using Docker):
   ```bash
   docker network ls
   docker exec resume-review-backend ping postgres
   ```

4. Wait for database to be ready:
   ```bash
   docker-compose logs postgres | grep "ready to accept"
   ```

### Hibernate DDL Auto Errors

**Error**: `ERROR: relation "resumes" already exists`

**Solution**: Reset database or change `ddl-auto`:
- `update`: Update schema if needed (default, safe)
- `create`: Drop and recreate schema (destructive)
- `create-drop`: Drop schema on shutdown
- `validate`: Only validate schema exists

## Performance Tuning

### Increase Connection Pool (if needed)

Edit `application.yml`:
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
```

### Add Indexes for Queries

```sql
-- Index for resume lookups
CREATE INDEX idx_resumes_status ON resumes(status);
CREATE INDEX idx_resumes_uploaded_at ON resumes(uploaded_at);

-- Index for suggestions
CREATE INDEX idx_job_suggestions_resume_id ON job_suggestions(resume_id);
CREATE INDEX idx_job_suggestions_match_score ON job_suggestions(match_score);

-- Index for applications
CREATE INDEX idx_job_applications_resume_id ON job_applications(resume_id);
CREATE INDEX idx_job_applications_status ON job_applications(status);
```

## Production Checklist

- ✓ Use strong passwords
- ✓ Configure SSL connections
- ✓ Set up regular backups
- ✓ Monitor connection logs
- ✓ Configure connection pooling
- ✓ Set up database replication
- ✓ Use read replicas for heavy queries
- ✓ Configure query timeouts
- ✓ Monitor slow queries

## Useful Queries

### Database Statistics
```sql
-- Database size
SELECT pg_size_pretty(pg_database_size('resume_review_db'));

-- Table sizes
SELECT schemaname, tablename, 
       pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) 
FROM pg_tables 
WHERE schemaname != 'pg_catalog' 
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;

-- Index sizes
SELECT indexname, pg_size_pretty(pg_relation_size(indexname)) 
FROM pg_indexes 
ORDER BY pg_relation_size(indexname) DESC;
```

### Activity Monitoring
```sql
-- Current connections
SELECT usename, count(*), state 
FROM pg_stat_activity 
GROUP BY usename, state;

-- Long-running queries
SELECT query, query_start, state_change 
FROM pg_stat_activity 
WHERE query_start < now() - INTERVAL '1 minute';
```

---

**For more information:**
- [PostgreSQL Official Docs](https://www.postgresql.org/docs/)
- [pgAdmin Documentation](https://www.pgadmin.org/docs/)
- [Docker PostgreSQL Image](https://hub.docker.com/_/postgres)
