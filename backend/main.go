package main

import (
        "fmt"
        "log"
        "net/http"
)

func main() {
        http.HandleFunc("/", helloWorld)

        fmt.Println("Server is up and running on http://localhost:8080")
        if err := http.ListenAndServe(":8080", nil); err != nil {
                log.Fatal(err)
        }
}

func helloWorld(w http.ResponseWriter, r *http.Request) {
        fmt.Fprintln(w, "Hello, World!")
}
