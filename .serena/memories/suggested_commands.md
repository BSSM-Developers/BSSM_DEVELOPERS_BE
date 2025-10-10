# Suggested Commands for Development

## Build and Run

### Build Project
```bash
./gradlew build
```
Compiles the project, runs tests, and creates the executable JAR.

### Run Application
```bash
./gradlew bootRun
```
Starts the Spring Boot application on default port (usually 8080).

### Clean Build
```bash
./gradlew clean build
```
Removes previous build artifacts and performs a fresh build.

## Testing

### Run All Tests
```bash
./gradlew test
```
Executes all unit and integration tests.

### Run Specific Test Class
```bash
./gradlew test --tests "ClassName"
```
Runs tests for a specific test class.

### Run Tests with Output
```bash
./gradlew test --info
```
Shows detailed test execution information.

## Code Quality

### Compile Check
```bash
./gradlew compileJava
```
Compiles Java source files and checks for compilation errors without running tests.

### Verify Build
```bash
./gradlew check
```
Runs all verification tasks including tests and code quality checks.

## Development Workflow

### Clean and Compile
```bash
./gradlew clean compileJava
```
Fresh compilation without running tests.

### Continuous Build
```bash
./gradlew build --continuous
```
Automatically rebuilds when source files change.

## Gradle Information

### List All Tasks
```bash
./gradlew tasks
```
Shows all available Gradle tasks.

### Dependencies
```bash
./gradlew dependencies
```
Displays project dependency tree.

## macOS-Specific System Commands

### File Operations
- `ls` - List directory contents
- `find . -name "pattern"` - Find files
- `grep -r "pattern" .` - Search in files

### Git Operations
- `git status` - Check repository status
- `git log --oneline` - View commit history
- `git diff` - Show changes

### Process Management
- `ps aux | grep java` - Find Java processes
- `lsof -i :8080` - Check what's using port 8080
- `kill -9 <PID>` - Force kill a process

## When Task is Completed

After implementing changes, run these commands in order:

1. **Compile Check** (fastest validation):
   ```bash
   ./gradlew compileJava
   ```

2. **Run Tests** (if code changes affect functionality):
   ```bash
   ./gradlew test
   ```

3. **Full Build** (before committing):
   ```bash
   ./gradlew build
   ```

## Configuration Files

- `build.gradle` - Build configuration and dependencies
- `src/main/resources/application.yaml` - Main application config
- `src/main/resources/application-dev.yaml` - Development profile config
- `CLAUDE.md` - Claude Code specific guidance