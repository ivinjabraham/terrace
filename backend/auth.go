package main

import (
	"time"
	"go.mongodb.org/mongo-driver/bson/primitive"
    "go.mongodb.org/mongo-driver/bson"
    "golang.org/x/crypto/bcrypt"
)

func hashPassword(password string) (string, error) {
    hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
    return string(hash), err
}

func checkPassword(hash, password string) bool {
    return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}

func registerUser(username, password string) error {
    hashedPass, err := hashPassword(password)
    if err != nil {
        return err
    }

    user := &User{
        UserID:     primitive.NewObjectID(),
        CreatedAt:  time.Now(),
        Username:   username,
        HashedPass: hashedPass,
    }

    _, err = collection.InsertOne(ctx, user)
    return err
}

func loginUser(username, password string) bool {
    var user User
    filter := bson.M{"username": username} 
    err := collection.FindOne(ctx, filter).Decode(&user)
    if err != nil {
        return false
    }

    return checkPassword(user.HashedPass, password)
}
