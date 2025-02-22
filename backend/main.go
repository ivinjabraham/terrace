package main

import (
    "fmt"
    "log"
    "net/http"
)

func main() {
    initDB()

    http.HandleFunc("/", helloWorld)
    http.HandleFunc("/register", registerHandler)
    http.HandleFunc("/login", loginHandler)

    fmt.Println("Server is up and running on http://0.0.0.0:8080")
    if err := http.ListenAndServe("0.0.0.0:8080", nil); err != nil {
        log.Fatal(err)
    }
}
