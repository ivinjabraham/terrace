package utils

import (
	"math"
	"time"

	"golang.org/x/crypto/bcrypt"
)

// For testing
var timeNow = time.Now

func HashPassword(password string) (string, error) {
	hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(hash), err
}

func CheckPassword(hash, password string) bool {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}

func CalculateScore(screenTime int64) int {
	// Get current hour of the day (0-23)
	currentHour := timeNow().Hour()

	// Convert screenTime from milliseconds to hours
	screenTimeHours := float64(screenTime) / (1000 * 60 * 60)

	// Calculate expected screentime based on hours passed
	// Average person uses 6 hours per day
	expectedScreenTime := float64(currentHour) * (6.0 / 24.0)

	// Calculate score bonus based on how much less screentime they have compared to expected
	var bonus float64
	if screenTimeHours < expectedScreenTime {
		// More bonus for larger differences from expected time
		difference := expectedScreenTime - screenTimeHours
		// More moderate exponential growth
		bonus = math.Pow(1.5, difference) * 400

		// Bonus for maintaining low usage late in day
		if currentHour > 20 && screenTimeHours < 2 {
			bonus *= 1.8
		}
	} else {
		// Penalty for being over expected time
		excess := screenTimeHours - expectedScreenTime
		// More moderate penalty
		penalty := math.Pow(1.3, excess) * 300
		bonus = -penalty

		// Extra penalty for early excessive usage
		if currentHour < 6 && screenTimeHours > 2 {
			bonus *= 1.5
		}
	}

	// Base score calculation (starting from 1000)
	baseScore := 1000 + int(bonus)

	// Cap at 10000 for daily maximum
	if baseScore > 10000 {
		return 10000
	}

	// Ensure minimum of 0
	if baseScore < 0 {
		return 0
	}

	return baseScore
}

func GetWeekStart() time.Time {
	now := time.Now()
	weekday := now.Weekday()
	if weekday == time.Sunday {
		weekday = 7
	}
	return now.AddDate(0, 0, -int(weekday-1)).Truncate(24 * time.Hour)
}

func IsNewWeek(lastWeekStart time.Time) bool {
	currentWeekStart := GetWeekStart()
	return currentWeekStart.After(lastWeekStart)
}
