package main

import (
    "context"
    "log"
    "go.mongodb.org/mongo-driver/mongo"
    "go.mongodb.org/mongo-driver/mongo/options"
    "backend/config"
)

var collection *mongo.Collection
var ctx = context.TODO()

func initDB() {
    clientOptions := options.Client().ApplyURI(config.DB_URI)
    client, err := mongo.Connect(ctx, clientOptions)
    if err != nil {
        log.Fatal(err)
    }

    err = client.Ping(ctx, nil)
    if err != nil {
        log.Fatal(err)
    }

    collection = client.Database("terrace").Collection("users")
}