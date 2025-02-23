package handlers

import (
	"backend/models"
	"encoding/json"
	"net/http"

	"go.mongodb.org/mongo-driver/bson"
)

func (h *Handler) GetUsers(w http.ResponseWriter, r *http.Request) {
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

	Users := make([]models.GetUser, len(users))
	for i, u := range users {
		Users[i] = models.GetUser{
			Username:   u.Username,
			Score:      u.Score,
			ScreenTime: u.ScreenTime,
		}
	}

	json.NewEncoder(w).Encode(Users)
}
