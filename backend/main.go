package main

import (
	"backend/config"
	"backend/db"
	"backend/handlers"
	"backend/middleware"

	"fmt"
	"log"
	"net/http"
)

func main() {
	cfg, err := config.Load()
	if err != nil {
		log.Fatal(err)
	}

	database, err := db.NewConnection(cfg.DBURI, "terrace", "users")
	if err != nil {
		log.Fatal(err)
	}
	defer database.Disconnect()

	authHandler := handlers.NewHandler(database, cfg.JWTSecret)

	mux := http.NewServeMux()
	mux.HandleFunc("POST /register", authHandler.Register)
	mux.HandleFunc("POST /login", authHandler.Login)

	protectedMux := http.NewServeMux()
	protectedMux.HandleFunc("GET /api/users", authHandler.GetUsers)
	protectedMux.HandleFunc("GET /api/profile", authHandler.Profile)
	protectedMux.HandleFunc("PUT /api/screentime", authHandler.UpdateScreenTime)

	mux.Handle("/", middleware.JWTMiddleware(cfg.JWTSecret)(protectedMux))

	fmt.Println("Server running on http://0.0.0.0:8080")
	log.Fatal(http.ListenAndServe("0.0.0.0:8080", mux))

}
