package handlers

import (
	"backend/models"
	"encoding/json"
	"net/http"

	"go.mongodb.org/mongo-driver/bson"
)

func (h *AuthHandler) GetUsers(w http.ResponseWriter, r *http.Request) {
	cursor, err := h.db.Collection.Find(r.Context(), bson.M{})
	if err != nil {
		http.Error(w, "Failed to fetch users", http.StatusInternalServerError)
		return
	}

	var users []models.User
	if err = cursor.All(r.Context(), &users); err != nil {
		http.Error(w, "Failed to decode users", http.StatusInternalServerError)
		return
	}

	// Remove sensitive data from response
	type PublicUser struct {
		Username string `json:"username"`
		Score    int    `json:"score"`
	}

	publicUsers := make([]PublicUser, len(users))
	for i, u := range users {
		publicUsers[i] = PublicUser{
			Username: u.Username,
			Score:    u.Score,
		}
	}

	json.NewEncoder(w).Encode(publicUsers)
}
