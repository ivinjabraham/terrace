package utils

import (
	"golang.org/x/crypto/bcrypt"
)

func HashPassword(password string) (string, error) {
	hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(hash), err
}

func CheckPassword(hash, password string) bool {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}

func CalculateScore(screenTime int64) int {
	baseScore := 1000

	screenTimeHours := float64(screenTime) / (1000 * 60 * 60)

	var penalty int

	if screenTimeHours <= 2 {
		penalty = int(screenTimeHours * 50)
	} else if screenTimeHours <= 4 {
		penalty = 100 + int((screenTimeHours-2)*100)
	} else {
		penalty = 300 + int((screenTimeHours-4)*200)
	}

	finalScore := baseScore - penalty

	if finalScore < 0 {
		return 0
	}

	return finalScore
}
