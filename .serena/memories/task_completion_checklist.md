# Task Completion Checklist

When a coding task is completed, follow these steps to ensure code quality and correctness.

## 1. Compilation Check (Required)
```bash
./gradlew compileJava
```
- **Purpose**: Verify that code compiles without errors
- **When**: After any code changes
- **Action if fails**: Fix compilation errors before proceeding

## 2. Run Tests (Conditional)
```bash
./gradlew test
```
- **When to run**:
  - Modified existing functionality
  - Added new features
  - Changed service/repository logic
  - Modified domain models
  
- **When to skip**:
  - Only documentation changes
  - Configuration updates without code changes
  - Simple refactoring with no behavioral changes

- **Action if fails**: Fix failing tests or update tests to match new behavior

## 3. Run Specific Tests (Optional)
```bash
./gradlew test --tests "ClassName"
```
- **When**: Testing a specific component in isolation
- **Use case**: Faster feedback during iterative development

## 4. Full Build (Before Committing)
```bash
./gradlew build
```
- **When**: Before committing to version control
- **Purpose**: Complete validation including tests and build artifacts
- **Action if fails**: Address any issues before commit

## Code Quality Checklist

Before marking a task as complete, verify:

### Code Style
- [ ] Follows entity conventions (Lombok, static factory methods)
- [ ] DTOs use `record` without "Dto" suffix
- [ ] Exceptions follow singleton pattern with `raise()` method
- [ ] Boolean comparisons are direct (`if (isActive)` not `if (isActive == true)`)
- [ ] Descriptive naming for IDs, variables, and constants

### Architecture
- [ ] Business logic in appropriate service layer
- [ ] Query operations in `*QueryService` if separated
- [ ] DTOs in correct `dto/request/` or `dto/response/` package
- [ ] Proper use of `@Transactional` annotations
- [ ] Manual mappers for entity-DTO conversion

### Error Handling
- [ ] Custom exceptions extend `GlobalException`
- [ ] Error codes defined in `ErrorCode` enum
- [ ] Appropriate HTTP status codes used

### Documentation
- [ ] Complex logic has clear comments
- [ ] Public API methods are self-explanatory
- [ ] CLAUDE.md updated if new patterns introduced

## Quick Validation Flow

### For Small Changes (< 50 lines)
1. `./gradlew compileJava`
2. Review code style
3. Done

### For Medium Changes (Feature additions, refactoring)
1. `./gradlew compileJava`
2. `./gradlew test`
3. Review architecture adherence
4. Done

### For Major Changes (New domains, API changes)
1. `./gradlew compileJava`
2. `./gradlew test --tests "NewFeatureTests"`
3. `./gradlew build`
4. Full code quality checklist review
5. Update documentation if needed
6. Done

## Git Integration

After passing all checks:
```bash
git add .
git commit -m "descriptive message"
git push
```

## Notes
- The project uses Java 21 with Spring Boot 3.5.4
- Database DDL auto is set to `none` - manual schema management
- Redis is used for token storage - ensure Redis is running for auth features