# Terrace Backend

A Go backend service that scores users' screen time, featuring a weekly leaderboard system and JWT authentication.

## Features

- User authentication (register/login) with JWT
- Dynamic scoring system based on daily usage patterns
- Weekly leaderboard
- Automatic weekly score resets
- MongoDB integration

## Prerequisites

- Go 1.24 or higher
- MongoDB
- Git

## Installation

1. cd into `backend/`

```bash
cd backend/
```

2. Install dependencies:

```bash
go mod download
```

3. Create a `.env` file:

```bash
cp .env.example .env
```

## Deploying the Backend

1. Start MongoDB locally or use a remote instance
2. Update `.env` with your MongoDB URI
3. Run the server:

```bash
go run .
```

The server will start on `http://0.0.0.0:8080`


## API Endpoints

### Authentication

#### Register
- **POST** `/register`
- Body:

```json
{
    "username": "user",
    "password": "pwd"
}
```

#### Login
- **POST** `/login`
- Body:

```json
{
    "username": "user",
    "password": "pwd"
}
```
- Returns: JWT token

### Protected Routes

All protected routes require Bearer token authentication:

```bash
curl -X GET http://localhost:8080/api/ -H "Authorization: Bearer <token>"
```

#### Get Profile
- **GET** `/api/profile`
- Returns: User profile with score and screen time

#### Update Screen Time
- **PUT** `/api/screentime`
- Body:

```json
{
    "screentime": 3600000 // milliseconds
}
```

#### Get Leaderboard
- **GET** `/api/leaderboard`
- Returns: Sorted list of users with ranks

## Scoring System

The scoring system rewards users for maintaining low screen time:

- Base score: 1000 points
- Daily maximum: 10000 points
- Weekly milestones for unlocking constellations: 1500, 3000, 6000, 12000, 24000, 50000
- Factors considered:
  - Time of day
  - Current screen time
  - Expected usage (6 hours/day average)
  - Special bonuses for low usage late in day
  - Penalties for excessive usage early in day

For implementation details, see:
go:utils/utils.go
startLine: 22
endLine: 72


## Project Structure

- `/handlers`: API endpoint handlers
- `/middleware`: JWT authentication middleware
- `/models`: Data structures
- `/utils`: Helper functions and scoring logic
- `/config`: Configuration management
- `/db`: Database connection and operations

## Testing

Run utility test to check if the scoring system is working:

```bash
go test -v ./utils
```