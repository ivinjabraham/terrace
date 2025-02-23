package handlers

import (
	"backend/middleware"
	"backend/models"
	"backend/utils"
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
		"username":   user.Username,
		"score":      user.Score,
		"joined_at":  user.CreatedAt,
		"screentime": user.ScreenTime,
	})
}

func (h *Handler) UpdateScreenTime(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPut {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}

	username := r.Context().Value(middleware.ContextKey("username")).(string)

	var body struct {
		ScreenTime int64 `json:"screentime"`
	}

	if err := json.NewDecoder(r.Body).Decode(&body); err != nil {
		http.Error(w, "Invalid request body", http.StatusBadRequest)
		return
	}

	// Calculate new score based on screen time
	newScore := utils.CalculateScore(body.ScreenTime)

	result, err := h.db.Collection.UpdateOne(
		r.Context(),
		bson.M{"username": username},
		bson.M{"$set": bson.M{
			"screentime": body.ScreenTime,
			"score":      newScore,
		}},
	)

	if err != nil {
		http.Error(w, "Failed to update screentime", http.StatusInternalServerError)
		return
	}

	if result.MatchedCount == 0 {
		http.Error(w, "User not found", http.StatusNotFound)
		return
	}

	w.WriteHeader(http.StatusOK)
	json.NewEncoder(w).Encode(map[string]interface{}{
		"status": "updated",
		"score":  newScore,
	})
}
