package handlers

import (
	"backend/models"
	"context"
	"encoding/json"
	"net/http"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func ProfileHandler(w http.ResponseWriter, r *http.Request) {
	username := r.Header.Get("X-Username")

	var user models.User
	ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancel()

	err := models.UsersCollection.FindOne(ctx, bson.M{"username": username}).Decode(&user)
	if err != nil {
		http.Error(w, "User not found", http.StatusNotFound)
		return
	}

	type UserResponse struct {
		UserID    primitive.ObjectID `json:"user_id"`
		CreatedAt time.Time          `json:"created_at"`
		Username  string             `json:"username"`
	}

	userResponse := UserResponse{
		UserID:    user.UserID,
		CreatedAt: user.CreatedAt,
		Username:  user.Username,
	}

	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(userResponse)
}
