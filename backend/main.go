package main

import (
    "fmt"
    "log"
    "net/http"
    "backend/config"
)

func main() {
    config.LoadConfig()
    initDB()

    registerRoutes()

    fmt.Println("Server running on http://0.0.0.0:8080")
    log.Fatal(http.ListenAndServe("0.0.0.0:8080", nil))
}