# 📝 Project Changelog

> **Maintainer's Note**: This file documents all notable changes to the project. 
> Please follow the [Keep a Changelog](https://keepachangelog.com) principles.

## Table of Contents
- [Unreleased Changes](#unreleased)
- [Version History](#version-history)
- [Change Types](#change-types)
- [Maintenance Guide](#maintenance-guide)

## 🔄 Unreleased Changes <a name="unreleased"></a>

### 🚀 New Features
- **API**: Initial date serialization implementation
  - Added `@JsonFormat` annotations for consistent date handling
  - Configured ISO-8601 date formatting in API responses
  - Added support for Java 8 date/time types
  - Related files:
    - `src/main/java/com/elearn/entity/User.java`
    - `src/main/java/com/elearn/dto/UserDto.java`
    - `src/main/java/com/elearn/config/JacksonConfig.java`

### 🛠️ Changes
- **API**: Standardized date property naming
  - Changed `createAt` → `createdAt` in `UserDto`
  - Updated all related DTOs to use consistent naming
  - Affected files:
    - `src/main/java/com/elearn/dto/UserDto.java`

### 🐛 Bug Fixes
- **API**: Fixed date serialization issues
  - Resolved timezone inconsistencies
  - Fixed null date handling in API responses
  - Related PR: #123

### 🗑️ Removals
- **API**: Reverted date serialization implementation
  - Removed due to frontend compatibility issues
  - Future implementation will use `java.time` package
  - Removed files:
    - `src/main/java/com/elearn/config/JacksonConfig.java`

### 📦 Dependencies
```diff
- Removed: com.fasterxml.jackson.datatype:jackson-datatype-jsr310
```

### ⚠️ Breaking Changes
- Date format in API responses has changed to ISO-8601
- Requires frontend updates to handle the new format

### 🔄 Migration Guide

#### Date Serialization Changes (2024-06-09)

**Before (Old Format)**
```java
// Old approach (deprecated)
private Date createdAt;
```

**After (New Implementation)**
```java
// Recommended approach
@JsonFormat(shape = JsonFormat.Shape.STRING, 
           pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", 
           timezone = "UTC")
private Instant createdAt;
```

**Required Updates**
1. Update frontend to handle ISO-8601 formatted dates
2. Add Jackson Java 8 date/time module:
   ```xml
   <dependency>
       <groupId>com.fasterxml.jackson.datatype</groupId>
       <artifactId>jackson-datatype-jsr310</artifactId>
   </dependency>
   ```

## 📜 Version History <a name="version-history"></a>

### [1.0.0] - 2024-06-09
- Initial project setup
- Basic user management functionality
- API documentation with Swagger

## 📋 Change Types <a name="change-types"></a>

| Type      | Description                                 |
|-----------|---------------------------------------------|
| `feat`    | New feature                                 |
| `fix`     | Bug fix                                    |
| `docs`    | Documentation changes                      |
| `style`   | Code style changes (formatting, etc.)      |
| `refactor`| Code changes that don't fix bugs or add features |
| `perf`    | Performance improvements                   |
| `test`    | Adding or modifying tests                  |
| `chore`   | Build process or tooling changes           |


## 🛠️ Maintenance Guide <a name="maintenance-guide"></a>

### Adding New Entries
1. Add changes under the [Unreleased] section
2. Use the appropriate change type
3. Include relevant issue/PR numbers
4. Add migration notes if needed

### Release Process
1. Update version in `pom.xml`
2. Move [Unreleased] to new version
3. Update documentation
4. Create git tag
5. Push changes with tags

### Commit Message Format
```
<type>(<scope>): <description>

[optional body]

[optional footer(s)]
```

Example:
```
fix(api): correct date serialization in UserDto

- Fixed date format in API responses
- Updated validation rules

Fixes #123
```