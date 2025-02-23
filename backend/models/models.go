package models

import (
	"time"

	"go.mongodb.org/mongo-driver/bson/primitive"
)

type User struct {
	UserID     primitive.ObjectID `bson:"_id"`
	CreatedAt  time.Time          `bson:"created_at"`
	Username   string             `bson:"username"`
	HashedPass string             `bson:"hashedpass"`
	Score      int                `bson:"score"`
	ScreenTime int64              `bson:"screentime"` // in milliseconds
	WeekStart  time.Time          `bson:"week_start"` // track when the user's week started
}

type LeaderboardEntry struct {
	Username      string `json:"username"`
	Score         int    `json:"score"`
	Rank          int    `json:"rank"`
	IsCurrentUser bool   `json:"isCurrentUser"`
}

type Week struct {
	ID        primitive.ObjectID `bson:"_id"`
	StartDate time.Time          `bson:"start_date"`
	EndDate   time.Time          `bson:"end_date"`
	Number    int                `bson:"week_number"`
}

type WeeklyWinner struct {
	Username string    `bson:"username"`
	Score    int       `bson:"score"`
	WeekEnd  time.Time `bson:"week_end"`
}
