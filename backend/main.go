package main

import (
        "fmt"
        "log"
        "net/http"
        "database/sql"
        "encoding/json"
        "golang.org/x/crypto/bcrypt"
        _ "github.com/mattn/go-sqlite3" 
)

var db *sql.DB

func initDB() {
	var err error
	db, err = sql.Open("sqlite3", "auth.db")
	if err != nil {
		log.Fatal(err)
	}

	query := `CREATE TABLE IF NOT EXISTS users (
		id INTEGER PRIMARY KEY AUTOINCREMENT,
		username TEXT UNIQUE NOT NULL,
		password TEXT NOT NULL
	);`
	_, err = db.Exec(query)
	if err != nil {
		log.Fatal(err)
	}
}

func hashPassword(password string) (string, error) {
	hash, err := bcrypt.GenerateFromPassword([]byte(password), bcrypt.DefaultCost)
	return string(hash), err
}

func checkPassword(hash, password string) bool {
	return bcrypt.CompareHashAndPassword([]byte(hash), []byte(password)) == nil
}

func registerUser(username, password string) error {
	hashedpass, err := hashPassword(password)
	if err != nil {
		return err
	}

	_, err = db.Exec("INSERT INTO users (username, password) VALUES (?, ?)", username, hashedpass)
	return err
}

func loginUser(username, password string) bool {
	var hashedpass string
	err := db.QueryRow("SELECT password FROM users WHERE username = ?", username).Scan(&hashedpass)
	if err != nil {
		return false
	}

	return checkPassword(hashedpass, password)
}

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

func main() {
	initDB()

	http.HandleFunc("/", helloWorld)
	http.HandleFunc("/register", registerHandler)
	http.HandleFunc("/login", loginHandler)

	fmt.Println("Server is up and running on http://localhost:8080")
	if err := http.ListenAndServe(":8080", nil); err != nil {
		log.Fatal(err)
	}
}

