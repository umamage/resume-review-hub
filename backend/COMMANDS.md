# Quick Command Reference

## Project Directories

```bash
# Navigate to backend
cd /workspaces/resume-review-hub/backend

# View all files
tree -L 3 backend/ -I 'target'
```

## Build & Run Commands

### Development (H2 Database)
```bash
# Build
mvn clean install

# Run
mvn spring-boot:run

# Build and skip tests
mvn clean install -DskipTests
```

### Production (MySQL Database)
```bash
# Build package
mvn clean package -DskipTests

# Run JAR with production profile
java -jar target/resume-review-backend-1.0.0.jar \
  --spring.profiles.active=prod

# With custom database URL
java -jar target/resume-review-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:mysql://localhost:3306/resume_review_db \
  --spring.datasource.username=resumeuser \
  --spring.datasource.password=mypassword
```

## Docker Commands

### Docker Compose
```bash
# Build and start
docker-compose up -d

# Stop
docker-compose down

# Stop and remove volumes
docker-compose down -v

# View logs
docker-compose logs -f backend

# View MySQL logs
docker-compose logs -f mysql
```

### Docker Manual Build
```bash
# Build image
docker build -t resume-review-backend:1.0.0 .

# Run image with port mapping
docker run -p 8080:8080 resume-review-backend:1.0.0

# Run with volume for uploads
docker run -p 8080:8080 -v $(pwd)/uploads:/app/uploads \
  resume-review-backend:1.0.0
```

## Maven Commands

```bash
# Clean build
mvn clean

# Install dependencies
mvn install

# Compile
mvn compile

# Run tests
mvn test

# Skip tests during build
mvn install -DskipTests

# Package (create JAR)
mvn package

# Full clean package
mvn clean package -DskipTests

# Check for dependency updates
mvn versions:display-dependency-updates

# Update dependencies
mvn versions:use-latest-versions
```

## Testing Commands

### Unit Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ResumeControllerTest

# Run with verbose output
mvn test -X
```

### API Testing with cURL

```bash
# Upload resume
curl -X POST -F "file=@resume.pdf" \
  http://localhost:8080/api/resumes/upload

# Get all resumes
curl http://localhost:8080/api/resumes

# Get resume by ID
curl http://localhost:8080/api/resumes/1

# Generate review score
curl -X POST http://localhost:8080/api/review-scores/generate/1

# Generate job suggestions
curl -X POST http://localhost:8080/api/job-suggestions/generate/1

# Get job suggestions
curl http://localhost:8080/api/job-suggestions/resume/1

# Apply for job
curl -X POST "http://localhost:8080/api/job-applications/apply?jobSuggestionId=1&resumeId=1&applicationNotes=Interested"

# Get applications
curl http://localhost:8080/api/job-applications/resume/1

# Delete resume
curl -X DELETE http://localhost:8080/api/resumes/1
```

## Database Commands

### H2 Console (Development)
```
URL: http://localhost:8080/api/h2-console
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (empty)
```

### MySQL (Production)
```bash
# Connect to MySQL
mysql -h localhost -u resumeuser -p

# In MySQL prompt
USE resume_review_db;
SHOW TABLES;
SELECT * FROM resumes;
SELECT * FROM review_scores;
SELECT * FROM job_suggestions;
SELECT * FROM job_applications;
```

## Development Tools

### IDE Integration (VS Code)
```bash
# Open in VS Code
code /workspaces/resume-review-hub/backend

# Required Extensions:
# - Extension Pack for Java (Microsoft)
# - Spring Boot Extension Pack (VMware)
# - Apache PDFBox
```

### IDE Integration (IntelliJ IDEA)
```bash
# Open project
idea /workspaces/resume-review-hub/backend

# Maven tool window automatically loads
# Run configurations available
```

## System Utilities

```bash
# Find process on port 8080
lsof -i :8080

# Kill process
kill -9 <PID>

# Check if Maven is installed
mvn -version

# Check Java version
java -version

# List Docker containers
docker ps -a

# Check Docker image
docker images | grep resume-review

# View application logs
tail -f logs/application.log

# Clear Maven cache
rm -rf ~/.m2/repository
```

## File Management

```bash
# View backend structure
ls -la backend/

# View Java files
find backend -name "*.java" -type f

# View configuration
cat backend/src/main/resources/application.yml

# View pom.xml
cat backend/pom.xml

# Create uploads directory
mkdir -p backend/uploads

# View upload directory
ls -la backend/uploads/
```

## Documentation Access

```bash
# Read main README
cat backend/README.md

# Read quick start
cat backend/QUICKSTART.md

# Read API testing guide
cat backend/API_TESTING.md

# Read setup summary
cat BACKEND_SETUP_SUMMARY.md

# Read frontend integration
cat FRONTEND_INTEGRATION.md

# Read project index
cat INDEX.md
```

## Environment Variables

```bash
# Set database password
export DB_PASSWORD=mypassword

# Set profiles
export SPRING_PROFILES_ACTIVE=prod

# Check environment
echo $DB_PASSWORD

# Unset variable
unset DB_PASSWORD
```

## Port Management

```bash
# Check if port 8080 is available
netstat -tln | grep 8080

# Alternative check
lsof -i :8080

# Change port in application.yml
# server:
#   port: 9090
```

## Common Issues & Solutions

### Port Already in Use
```bash
# Find process
lsof -i :8080

# Kill it
kill -9 <PID>

# Or use different port (edit application.yml)
```

### Clean Build
```bash
# Complete reset
mvn clean
rm -rf target/
rm -rf ~/.m2/repository/com/resumereview

mvn clean install
mvn spring-boot:run
```

### Database Connection Issues
```bash
# Check configuration
cat backend/src/main/resources/application.yml

# Test MySQL connection
mysql -h localhost -u resumeuser -ppassword

# Check H2 console
curl http://localhost:8080/api/h2-console
```

### CORS Issues
```bash
# Verify backend is running
curl -I http://localhost:8080/api/resumes

# Check CORS configuration
grep -n "CorsRegistry" backend/src/main/java/com/resumereview/WebConfig.java
```

## Build Optimization

```bash
# Skip tests for faster build
mvn clean install -DskipTests

# Parallel build
mvn clean install -T 1C

# Update dependencies
mvn clean install -U

# Offline mode (after first build)
mvn clean install -o
```

## Debugging

```bash
# Enable debug mode
mvn -X clean install

# Run with debug logging
java -jar target/resume-review-backend-1.0.0.jar \
  --logging.level.root=DEBUG \
  --logging.level.com.resumereview=DEBUG

# Generate dependency tree
mvn dependency:tree

# Check for conflicts
mvn dependency:tree -Dverbose
```

## Production Deployment

```bash
# Build for production
mvn clean package -DskipTests

# Check file size
ls -lh target/resume-review-backend-1.0.0.jar

# Create uploads directory
mkdir -p /var/uploads/resume-review

# Set permissions
chmod 755 /var/uploads/resume-review

# Run as background process
nohup java -jar target/resume-review-backend-1.0.0.jar \
  --spring.profiles.active=prod \
  > app.log 2>&1 &

# Save PID to file
echo $! > app.pid
```

## Monitoring

```bash
# View running Java processes
ps aux | grep java

# Monitor resource usage
top -p <PID>

# Check memory usage
jps -l

# View logs in real-time
tail -f logs/application.log

# Count log lines
wc -l logs/application.log

# Search logs
grep "ERROR" logs/application.log
grep "exception" logs/application.log
```

## Git Commands (Optional)

```bash
# Initialize git (if not done)
cd /workspaces/resume-review-hub
git init

# Add backend to git
git add backend/

# Commit
git commit -m "Add Spring Boot backend"

# View status
git status

# View changes
git diff backend/pom.xml
```

## Docker Networking

```bash
# View Docker networks
docker network ls

# Inspect docker-compose network
docker network inspect resume-review-hub_default

# Connect to running container
docker exec -it resume-review-backend /bin/bash

# View container IP
docker inspect <container-id> | grep IPAddress
```

## Useful Aliases

```bash
# Add to ~/.bashrc
alias mvn-build="mvn clean package -DskipTests"
alias mvn-run="mvn spring-boot:run"
alias mvn-test="mvn test"
alias dc-up="docker-compose up -d"
alias dc-down="docker-compose down"
alias dc-logs="docker-compose logs -f"
alias port-check="lsof -i :8080"

# Source to use
source ~/.bashrc
```

---

## More Information

- See [backend/README.md](backend/README.md) for comprehensive documentation
- See [backend/QUICKSTART.md](backend/QUICKSTART.md) for setup instructions
- See [backend/API_TESTING.md](backend/API_TESTING.md) for API examples
- See [FRONTEND_INTEGRATION.md](FRONTEND_INTEGRATION.md) for frontend integration
- See [INDEX.md](INDEX.md) for complete project documentation

---

**Last Updated**: January 19, 2026
