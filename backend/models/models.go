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
}

type GetUser struct {
	Username      string `json:"username"`
	Score         int    `json:"score"`
	Rank          int    `json:"rank"`
	IsCurrentUser bool   `json:"isCurrentUser"`
}
