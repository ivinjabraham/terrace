package main

import (
    "net/http"
    "encoding/json"

    "backend/handlers"
    "backend/config"
)

func registerRoutes() {
    http.HandleFunc("/register", func(w http.ResponseWriter, r *http.Request) {
        if r.Method != http.MethodPost {
            http.Error(w, "Invalid request method. Use POST.", http.StatusBadRequest)
            return
        }

        var creds struct {
            Username string `json:"username"`
            Password string `json:"password"`
        }
        if err := json.NewDecoder(r.Body).Decode(&creds); err != nil {
            http.Error(w, "Bad request", http.StatusBadRequest)
            return
        }
        err := handlers.RegisterUser(r.Context(), collection, creds.Username, creds.Password)
        if err != nil {
            if err.Error() == "Username taken" {
                http.Error(w, err.Error(), http.StatusConflict) // 409 Conflict
            } else {
                http.Error(w, err.Error(), http.StatusInternalServerError)
            }
            return
        }
        w.WriteHeader(http.StatusCreated)
    })

    http.HandleFunc("/login", func(w http.ResponseWriter, r *http.Request) {
        if r.Method != http.MethodPost {
            http.Error(w, "Invalid request method. Use POST.", http.StatusBadRequest)
            return
        }

        var creds struct {
            Username string `json:"username"`
            Password string `json:"password"`
        }
        if err := json.NewDecoder(r.Body).Decode(&creds); err != nil {
            http.Error(w, "Bad request", http.StatusBadRequest)
            return
        }

        token, err := handlers.LoginUser(r.Context(), collection, creds.Username, creds.Password)
        if err != nil {
            http.Error(w, "Invalid credentials", http.StatusUnauthorized)
            return
        }
        json.NewEncoder(w).Encode(map[string]string{"token": token})
    })

    protectedRoutes := http.NewServeMux()
    protectedRoutes.HandleFunc("/profile", func(w http.ResponseWriter, r *http.Request) {
        handlers.ProfileHandler(w, r)
    })

    http.Handle("/api/", JWTMiddleware(protectedRoutes.ServeHTTP, config.JWT_SECRET))
}