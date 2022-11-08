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
	"encoding/json"
	"fmt"
	"io"
	"net/http"
	"os"
	"path"
	"strconv"

	_ "github.com/go-sql-driver/mysql"
	"github.com/gorilla/mux"
	"github.com/jinzhu/gorm"
	_ "github.com/jinzhu/gorm/dialects/mysql"
	"github.com/rs/cors"
	"github.com/sirupsen/logrus"
)

// remote connection

var db, _ = gorm.Open("mysql", "changeme:changeme@(mysql:3306)/todolist?charset=utf8&parseTime=True")

// local connection
//var db, _ = gorm.Open("mysql", "root:root@tcp/todolist?charset=utf8&parseTime=True")

type TodoItemModel struct {
	Id          int `gorm:"primary_key"`
	Description string
	Completed   bool
}

func CreateItem(w http.ResponseWriter, r *http.Request) {
	description := r.FormValue("description")
	logrus.WithFields(logrus.Fields{"description": description}).Info("Add new TodoItem. Saving to database.")
	todo := &TodoItemModel{Description: description, Completed: false}
	db.Create(&todo)
	result := db.Last(&todo).Value
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(result)
}

func UpdateItem(w http.ResponseWriter, r *http.Request) {
	// Get URL parameter from mux
	vars := mux.Vars(r)
	id, _ := strconv.Atoi(vars["id"])

	// Test if the TodoItem exist in DB
	err := GetItemByID(id)
	if err == false {
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"updated": false, "error": "Record Not Found"}`)
	} else {
		completed, _ := strconv.ParseBool(r.FormValue("completed"))
		logrus.WithFields(logrus.Fields{"Id": id, "Completed": completed}).Info("Updating TodoItem")
		todo := &TodoItemModel{}
		db.First(&todo, id)
		todo.Completed = completed
		db.Save(&todo)
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"updated": true}`)
	}
}

func DeleteItem(w http.ResponseWriter, r *http.Request) {
	// Get URL parameter from mux
	vars := mux.Vars(r)
	id, _ := strconv.Atoi(vars["id"])

	// Test if the TodoItem exist in DB
	err := GetItemByID(id)
	if err == false {
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"deleted": false, "error": "Record Not Found"}`)
	} else {
		logrus.WithFields(logrus.Fields{"Id": id}).Info("Deleting TodoItem")
		todo := &TodoItemModel{}
		db.First(&todo, id)
		db.Delete(&todo)
		w.Header().Set("Content-Type", "application/json")
		io.WriteString(w, `{"deleted": true}`)
	}
}

func GetItemByID(Id int) bool {
	todo := &TodoItemModel{}
	result := db.First(&todo, Id)
	if result.Error != nil {
		logrus.Warn("TodoItem not found in database")
		return false
	}
	return true
}

func GetCompletedItems(w http.ResponseWriter, r *http.Request) {
	logrus.Info("Get completed TodoItems")
	completedTodoItems := GetTodoItems(true)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(completedTodoItems)
}

func GetIncompleteItems(w http.ResponseWriter, r *http.Request) {
	logrus.Info("Get Incomplete TodoItems")
	IncompleteTodoItems := GetTodoItems(false)
	w.Header().Set("Content-Type", "application/json")
	json.NewEncoder(w).Encode(IncompleteTodoItems)
}

func GetTodoItems(completed bool) interface{} {
	var todos []TodoItemModel
	TodoItems := db.Where("completed = ?", completed).Find(&todos).Value
	return TodoItems
}

func Healthz(w http.ResponseWriter, r *http.Request) {
	logrus.Info("API Health is OK")
	w.Header().Set("Content-Type", "application/json")
	io.WriteString(w, `{"alive": true}`)
}

func Home(w http.ResponseWriter, r *http.Request) {
	logrus.Info("Get index.html")
	p := path.Dir("index.html")
	// set header
	w.Header().Set("Content-type", "text/html")
	http.ServeFile(w, r, p)
}

func init() {
	logrus.SetFormatter(&logrus.TextFormatter{})
	logrus.SetReportCaller(true)
}

func prepopulate() {
	logrus.Info("Prepopulate the db")
	db.Create(&TodoItemModel{Description: "time to make the donuts"})
	db.Create(&TodoItemModel{Description: "prepopulate the db", Completed: true})

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

	defer db.Close()
	previousDb := db.Take(&TodoItemModel{})
	if previousDb.Error != nil {
		logrus.Info("A running instance of the db: todolist not found, creating")
		db.Debug().DropTableIfExists(&TodoItemModel{})
		db.Debug().AutoMigrate(&TodoItemModel{})
		prepopulate()
	}

	fs := http.FileServer(http.Dir("./resources/"))

	logrus.Info("Starting Todolist API server")
	router := mux.NewRouter()
	router.PathPrefix("/resources/").Handler(http.StripPrefix("/resources/", fs))
	router.HandleFunc("/", Home).Methods("GET")
	router.HandleFunc("/healthz", Healthz).Methods("GET")
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
