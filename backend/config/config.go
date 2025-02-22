package config

import (
    "log"
    "os"
)

var (
    DB_URI     string
    JWT_SECRET []byte
)

func LoadConfig() {
    DB_URI = os.Getenv("DB_URI")
    if DB_URI == "" {
        log.Fatal("DB_URI environment variable not set")
    }

    JWT_SECRET = []byte(os.Getenv("JWT_SECRET"))
    if len(JWT_SECRET) == 0 {
        log.Fatal("JWT_SECRET environment variable not set")
    }
}