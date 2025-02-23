package config

import (
	"errors"
	"log"
	"os"

	"github.com/joho/godotenv"
)

type Config struct {
	DBURI     string
	JWTSecret string
}

func Load() (*Config, error) {
	if err := godotenv.Load(); err != nil {
		log.Println("Warning: No .env file found - using system environment variables")
	}

	cfg := &Config{
		DBURI:     os.Getenv("DB_URI"),
		JWTSecret: os.Getenv("JWT_SECRET"),
	}

	if cfg.DBURI == "" {
		return nil, errors.New("DB_URI environment variable not set")
	}

	if cfg.JWTSecret == "" {
		return nil, errors.New("JWT_SECRET environment variable not set")
	}

	return cfg, nil
}
