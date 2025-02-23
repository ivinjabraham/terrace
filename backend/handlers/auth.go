package handlers

import (
    "context"
    "errors"
    "time"

    "github.com/golang-jwt/jwt/v5"
    "go.mongodb.org/mongo-driver/bson"
    "go.mongodb.org/mongo-driver/bson/primitive"
    "go.mongodb.org/mongo-driver/mongo"
    "golang.org/x/crypto/bcrypt"
    "backend/config"
    "backend/models"
)

var jwtSecret = config.JWT_SECRET

type Claims struct {
    Username string `json:"username"`
    jwt.RegisteredClaims
}

func hashPassword(password string) (string, error) {
    hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
    return string(hash), err
}

func checkPassword(hash, password string) bool {
    return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}

func generateToken(username string) (string, error) {
    expirationTime := time.Now().Add(24 * time.Hour)
    claims := &Claims{
        Username: username,
        RegisteredClaims: jwt.RegisteredClaims{
            ExpiresAt: jwt.NewNumericDate(expirationTime),
        },
    }

    token := jwt.NewWithClaims(jwt.SigningMethodHS256, claims)
    return token.SignedString(jwtSecret)
}

func RegisterUser(ctx context.Context, collection *mongo.Collection, username, password string) error {
    filter := bson.M{"username": username}
    var existingUser models.User

    err := collection.FindOne(ctx, filter).Decode(&existingUser)
    if err == nil {
        return errors.New("User already exists")
    }

    hashedPass, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
    if err != nil {
        return err
    }

    user := models.User{
        UserID:     primitive.NewObjectID(),
        CreatedAt:  time.Now(),
        Username:   username,
        HashedPass: string(hashedPass),
    }

    _, err = collection.InsertOne(ctx, user)
    return err
}

func LoginUser(ctx context.Context, collection *mongo.Collection, username, password string) (string, error) {
    var user models.User
    filter := bson.M{"username": username}

    err := collection.FindOne(ctx, filter).Decode(&user)
    if err != nil {
        return "", errors.New("User not found")
    }

    if !checkPassword(user.HashedPass, password) {
        return "", errors.New("Incorrect password")
    }

    return generateToken(username)
}

func ValidateToken(tokenString string) (*Claims, error) {
    claims := &Claims{}
    token, err := jwt.ParseWithClaims(tokenString, claims, func(token *jwt.Token) (interface{}, error) {
        return jwtSecret, nil
    })

    if err != nil || !token.Valid {
        return nil, errors.New("Invalid token")
    }

    return claims, nil
}