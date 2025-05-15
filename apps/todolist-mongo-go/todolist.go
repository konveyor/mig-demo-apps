// MIT License

// Copyright (c) 2020 Mohamad Fadhil

// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:

// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.

// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package main

import (
	"context"
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"path"
	"strconv"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
	"go.mongodb.org/mongo-driver/mongo/writeconcern"

	"github.com/gorilla/mux"
	"github.com/rs/cors"
	"github.com/sirupsen/logrus"
	log "github.com/sirupsen/logrus"
)

// remote connection
//var clientOptions = options.Client().ApplyURI("mongodb://changeme:changeme@mongo:27017")

// local connection
//var clientOptions = options.Client().ApplyURI("mongodb://changeme:changeme@localhost:27017")

// Connect to MongoDB
// var db, err = mongo.Connect(context.TODO(), clientOptions)
// var tododb = db.Database("todolist").Collection("TodoItemModel")

var db *mongo.Client
var tododb *mongo.Collection

type TodoItemModel struct {
	Id          primitive.ObjectID `bson:"_id,omitempty"`
	Description string
	Completed   bool
}

func connectToMongo() *mongo.Client {
	remote := connectToMongoRemote()
	pingErr := remote.Ping(context.TODO(), nil)
	if pingErr != nil {
		log.Error("Failed to ping remoteMongoDB")
	}
	if pingErr != nil {
		local := connectToMongoLocal()
		if local == nil {
			log.Fatal("Failed to connect to MongoDB")
			return nil
		}
		db = local
	} else {
		db = remote
	}
	return db
}

func connectToMongoLocal() *mongo.Client {
	log.Info("Attempting to connect to: mongodb://changeme:changeme@localhost:27017")
	clientOptions := options.Client().
		ApplyURI("mongodb://changeme:changeme@localhost:27017").
		SetWriteConcern(writeconcern.New(writeconcern.W(1), writeconcern.J(true)))
	db, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Error(("Local Connection failed"))
		return nil
	}
	return db
}

func connectToMongoRemote() *mongo.Client {
	log.Info("Attempting to connect to: mongodb://changeme:changeme@mongo:27017")
	clientOptions := options.Client().
		ApplyURI("mongodb://changeme:changeme@mongo:27017").
		SetWriteConcern(writeconcern.New(writeconcern.W(1), writeconcern.J(true)))
	db, err := mongo.Connect(context.TODO(), clientOptions)
	if err != nil {
		log.Error(("Remote Connection failed"))
		return nil
	}
	return db
}

func CreateItem(w http.ResponseWriter, r *http.Request) {
	description := r.FormValue("description")
	log.WithFields(log.Fields{"description": description}).Info("Add new TodoItem. Saving to database.")
	todo := &TodoItemModel{Description: description, Completed: false}
	result, err := tododb.InsertOne(context.TODO(), todo)
	id := result.InsertedID.(primitive.ObjectID)
	todo.Id = id
	log.Info("inserted document with ID %v\n", id.Hex())
	if err != nil {
		log.Fatal(err)
	} else {
		// result := make(map[string]string)
		// result["Id"] = id
		w.Header().Set("Content-Type", "application/json")
		json.NewEncoder(w).Encode(todo)
	}
}

func UpdateItem(w http.ResponseWriter, r *http.Request) {
	// Get URL parameter from mux
	vars := mux.Vars(r)
	// id, _ := strconv.Atoi(vars["id"])
	// id, _ := strconv.ParseInt(vars["id"], 10, 64)
	// Test if the TodoItem exist in DB
	id := vars["id"]
	err := GetItemByID(id)
	if err == false {
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"updated": false, "error": "Record Not Found"}`)
	} else {
		completed, _ := strconv.ParseBool(r.FormValue("completed"))
		log.WithFields(log.Fields{"_id": id, "Completed": completed}).Info("Updating TodoItem")
		objID, err := primitive.ObjectIDFromHex(id)
		if err != nil {
			panic(err)
		}
		filter := bson.M{"_id": objID}
		_, err = tododb.UpdateOne(
			context.TODO(),
			filter,
			bson.D{
				{"$set", bson.D{{"completed", completed}}},
			},
		)
		if err != nil {
			fmt.Print(err.Error())
		}
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"updated": true}`)
	}
}

func DeleteItem(w http.ResponseWriter, r *http.Request) {
	// Get URL parameter from mux
	vars := mux.Vars(r)
	for k, v := range mux.Vars(r) {
		log.Info("key=%v, value=%v", k, v)
	}
	// id, _ := strconv.Atoi(vars["id"])
	id := vars["id"]
	// log.Warn("FAK")
	// log.Info(id)
	// Test if the TodoItem exist in DB
	err := GetItemByID(id)
	if err == false {
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"deleted": false, "error": "Record Not Found"}`)
	} else {
		log.WithFields(log.Fields{"_id": id}).Info("Deleting TodoItem")
		objID, err := primitive.ObjectIDFromHex(id)
		if err != nil {
			panic(err)
		}
		filter := bson.M{"_id": objID}
		opts := options.Delete().SetCollation(&options.Collation{
			Locale:    "en_US",
			Strength:  1,
			CaseLevel: false,
		})
		res, err := tododb.DeleteOne(context.TODO(), filter, opts)
		if err != nil {
			log.Fatal(err)
		}
		log.Info("deleted %v documents\n", res.DeletedCount)
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"deleted": true}`)
	}
}

func GetItemByID(Id string) bool {
	objID, err := primitive.ObjectIDFromHex(Id)
	if err != nil {
		panic(err)
	}
	filter := bson.M{"_id": objID}
	var result TodoItemModel
	err = tododb.FindOne(context.TODO(), filter).Decode(&result)
	if err != nil {
		log.Error("ID NOT found")
		return false
	} else {
		log.Infof("%+v\n", result)
		return true
	}
}

func GetCompletedItems(w http.ResponseWriter, r *http.Request) {
	log.Info("Get completed TodoItems")
	completedTodoItems := GetTodoItems(true)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(completedTodoItems)
}

func GetIncompleteItems(w http.ResponseWriter, r *http.Request) {
	log.Info("Get Incomplete TodoItems")
	IncompleteTodoItems := GetTodoItems(false)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(IncompleteTodoItems)
}

func GetTodoItems(completed bool) interface{} {
	findOptions := options.Find()
	findOptions.SetLimit(50)

	var results []*TodoItemModel
	filter := bson.M{"completed": completed}
	//TodoItems := db.Where("completed = ?", completed).Find(&todos).Value
	//return TodoItems
	cur, err := tododb.Find(context.TODO(), filter, findOptions)
	if err != nil {
		log.Fatal(err)
	}

	// Iterate through the cursor
	for cur.Next(context.TODO()) {
		var elem TodoItemModel
		err := cur.Decode(&elem)
		if err != nil {
			log.Fatal(err)
		}

		results = append(results, &elem)
	}
	return results

}

func Healthz(w http.ResponseWriter, r *http.Request) {
	log.Info("API Health is OK")
	w.Header().Set("Content-Type", "application/json")
	io.WriteString(w, `{"alive": true}`)
}

func Home(w http.ResponseWriter, r *http.Request) {
	log.Info("Get index.html")
	p := path.Dir("index.html")
	// set header
	w.Header().Set("Content-type", "text/html")
	http.ServeFile(w, r, p)
}

func init() {
	log.SetFormatter(&log.TextFormatter{})
	log.SetReportCaller(true)
}

func prepopulate(collection *mongo.Collection) {
	log.Info("Prepopulate the db")
	prepop := TodoItemModel{Description: "prepopulate the db", Completed: true}
	donuts := TodoItemModel{Description: "time", Completed: false}
	both_prepop := []interface{}{prepop, donuts}

	insertManyResult, err := collection.InsertMany(context.TODO(), both_prepop)
	if err != nil {
		log.Fatal(err)
	}
	fmt.Println("Inserted multiple prepopulate documents: ", insertManyResult.InsertedIDs)
}

func GetLogFile(w http.ResponseWriter, r *http.Request) {
	// if file not found we simply get a 404
	filename := "/tmp/log/todoapp/app.log"
	http.ServeFile(w, r, filename)
}

func faviconHandler(w http.ResponseWriter, r *http.Request) {
	http.ServeFile(w, r, "favicon.ico")
}

func main() {
	// logging to volume
	if _, err := os.Stat("/tmp/log/todoapp"); os.IsNotExist(err) {
		os.MkdirAll("/tmp/log/todoapp", 0700)
	}
	f, err := os.OpenFile("/tmp/log/todoapp/app.log", os.O_WRONLY|os.O_CREATE|os.O_APPEND, 0644)
	// if directory or volume is not mounted, do not exit
	if err != nil {
		fmt.Println("Failed to create logfile" + "logrus.txt")
		logrus.Info("Failed: log file /tmp/log/todoapp/app.log create failed")
		f.Close()
	} else {
		defer f.Close()
		multi := io.MultiWriter(f, os.Stdout)
		logrus.SetOutput(multi)
		logrus.Info("Success: Attached volume and redirected logs to /tmp/log/todoapp/app.log")
	}

	// Connect to MongoDB
	db = connectToMongo()

	// collection
	tododb = db.Database("todolist").Collection("TodoItemModel")
	fmt.Println("Connected to MongoDB!")

	fs := http.FileServer(http.Dir("./resources/"))

	log.Info("Starting Todolist API server")
	router := mux.NewRouter()
	router.PathPrefix("/resources/").Handler(http.StripPrefix("/resources/", fs))
	router.HandleFunc("/", Home).Methods("GET")
	router.HandleFunc("/favicon.ico", faviconHandler)
	router.HandleFunc("/healthz", Healthz).Methods("GET")
	router.HandleFunc("/log", GetLogFile).Methods("GET")
	router.HandleFunc("/todo-completed", GetCompletedItems).Methods("GET")
	router.HandleFunc("/todo-incomplete", GetIncompleteItems).Methods("GET")
	router.HandleFunc("/todo", CreateItem).Methods("POST")
	router.HandleFunc("/todo/{id}", UpdateItem).Methods("POST")
	router.HandleFunc("/todo/{id}", DeleteItem).Methods("DELETE")

	handler := cors.New(cors.Options{
		AllowedMethods: []string{"GET", "POST", "DELETE", "PATCH", "OPTIONS"},
	}).Handler(router)

	http.ListenAndServe(":8000", handler)
}
