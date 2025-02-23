package handlers

import (
	"backend/middleware"
	"backend/models"
	"encoding/json"
	"net/http"

	"go.mongodb.org/mongo-driver/bson"
)

func (h *Handler) Profile(w http.ResponseWriter, r *http.Request) {
	username := r.Context().Value(middleware.ContextKey("username")).(string)

	var user models.User
	err := h.db.Collection.FindOne(r.Context(), bson.M{"username": username}).Decode(&user)
	if err != nil {
		http.Error(w, "User not found", http.StatusNotFound)
		return
	}

	json.NewEncoder(w).Encode(map[string]interface{}{
		"username":  user.Username,
		"score":     user.Score,
		"joined_at": user.CreatedAt,
	})
}
