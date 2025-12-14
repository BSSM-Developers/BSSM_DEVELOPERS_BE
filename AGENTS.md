# Repository Guidelines

## Project Structure & Module Organization
- Root Gradle project (`build.gradle`, `settings.gradle`) targeting Java 21 and Spring Boot 3.5.
- Main code lives in `src/main/java/com/example/bssm_dev`, organized by feature:
  - `common` (shared DTOs, utilities, senders, annotations, resolvers).
  - `domain/*` (auth, signup, user, docs, api) each with `controller`, `service`, `repository`, `model`, `mapper`, `dto`, and `exception` packages.
  - `global` (JWT, Feign clients, app configuration, and error handling).
- Configuration in `src/main/resources/application.yaml` with `-dev`/`-prod` overrides.
- Tests mirror packages under `src/test/java/com/example/bssm_dev`.

## Build, Test, and Development Commands
- `./gradlew clean build` — compile, run tests, and produce the jar.
- `./gradlew test` — execute the JUnit 5 test suite only.
- `./gradlew bootRun --args='--spring.profiles.active=dev'` — run the API locally with the dev profile.
- Use the Gradle wrapper (`./gradlew`) rather than a global Gradle install.

## Coding Style & Naming Conventions
- Java 21, Spring Boot, Lombok for boilerplate; prefer constructor injection over field injection.
- Package-by-feature is the default; place new components inside the matching `domain/*` module.
- Controllers/services/repositories/models end with `Controller`, `Service`, `Repository`, `*Entity` or `*Document`; DTOs end with `Request`/`Response`.
- Keep 4-space indentation; format with your IDE’s built-in formatter for Java/Spring.
- QueryDSL is available; generate Q-types via the standard build when adding new JPA entities.

## Testing Guidelines
- JUnit 5 via `spring-boot-starter-test`; align test packages with production packages.
- Name files `ClassNameTest`; favor slice tests or SpringBootTest where integration is required.
- Add focused assertions around security, validation, and repository queries; avoid relying on shared state between tests.
- Run `./gradlew test` before sending changes.

## Commit & Pull Request Guidelines
- Follow the existing pattern: `<type> :: <summary>` (e.g., `feat :: add signup validator`, `fix :: handle proxy timeout`). Types: feat, fix, chore, refactor, docs, test, delete.
- Commits should be small and scoped; keep messages in English or concise Korean as seen in history.
- PRs: include a short summary, the active profile(s) used for testing, linked issues, and screenshots or sample responses for API-visible changes.

## Security & Configuration Tips
- Do not commit secrets or real keys; supply them via environment variables or local `application-*.yaml` overrides.
- When running locally, set database, Redis, JWT, and external API credentials through profile-specific config or environment variables.
