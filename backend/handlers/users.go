package handlers

import (
	"backend/models"
	"encoding/json"
	"net/http"

	"backend/middleware"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func (h *Handler) GetUsers(w http.ResponseWriter, r *http.Request) {
	// Get current user from context
	currentUser := r.Context().Value(middleware.UsernameKey).(string)

	// Find and sort users
	cursor, err := h.db.Collection.Find(r.Context(), bson.M{}, options.Find().SetSort(bson.D{{Key: "score", Value: -1}}))
	if err != nil {
		http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
		return
	}

	var users []models.User
	if err = cursor.All(r.Context(), &users); err != nil {
		http.Error(w, "Failed to decode users", http.StatusInternalServerError)
		return
	}

	// Map to response model with rank and current user check
	response := make([]models.GetUser, len(users))
	for i, u := range users {
		response[i] = models.GetUser{
			Username:      u.Username,
			Score:         u.Score,
			Rank:          i + 1,
			IsCurrentUser: u.Username == currentUser,
		}
	}

	json.NewEncoder(w).Encode(response)
}
