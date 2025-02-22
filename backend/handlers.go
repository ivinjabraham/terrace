package main

import (
    "encoding/json"
    "fmt"
    "net/http"
)

func registerHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.Error(w, "Invalid request method. Use POST.", http.StatusMethodNotAllowed)
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

    if err := registerUser(creds.Username, creds.Password); err != nil {
        http.Error(w, "Registration failed.", http.StatusInternalServerError)
        return
    }

    w.WriteHeader(http.StatusCreated)
    fmt.Fprintln(w, "Registered successfully.")
}

func loginHandler(w http.ResponseWriter, r *http.Request) {
    if r.Method != http.MethodPost {
        http.Error(w, "Invalid request method. Use POST.", http.StatusMethodNotAllowed)
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

    if loginUser(creds.Username, creds.Password) {
        w.WriteHeader(http.StatusOK)
        fmt.Fprintln(w, "Logged in.")
    } else {
        http.Error(w, "Incorrect username/password", http.StatusUnauthorized)
    }
}

func helloWorld(w http.ResponseWriter, r *http.Request) {
    fmt.Fprintln(w, "Hello, World!")
}
