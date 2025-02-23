package models

import (
    "time"
    "go.mongodb.org/mongo-driver/bson/primitive"
    "go.mongodb.org/mongo-driver/mongo"
)

type User struct {
    UserID      primitive.ObjectID `bson:"_id"`
    CreatedAt   time.Time          `bson:"created_at"`
    Username    string             `bson:"username"`
    HashedPass  string             `bson:"hashedpass"`
}

var UsersCollection *mongo.Collection