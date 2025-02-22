package main

import (
    "fmt"
    "log"
    "net/http"
)

func main() {
    initDB()

    http.HandleFunc("/register", registerHandler)
    http.HandleFunc("/login", loginHandler)

    fmt.Println("Server running on http://0.0.0.0:8080")
    log.Fatal(http.ListenAndServe("0.0.0.0:8080", nil))
}
