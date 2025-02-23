package main

import (
    "net/http"
    "strings"

    "backend/utils"
)

func JWTMiddleware(next http.HandlerFunc, jwtSecret []byte) http.HandlerFunc {
    return func(w http.ResponseWriter, r *http.Request) {
        authHeader := r.Header.Get("Authorization")
        if authHeader == "" {
            http.Error(w, "Missing token", http.StatusUnauthorized)
            return
        }

        tokenString := strings.TrimPrefix(authHeader, "Bearer ")
        claims, err := utils.ValidateToken(tokenString, jwtSecret)
        if err != nil {
            http.Error(w, "Invalid token", http.StatusUnauthorized)
            return
        }

        r.Header.Set("X-Username", claims.Username)
        next.ServeHTTP(w, r)
    }
}