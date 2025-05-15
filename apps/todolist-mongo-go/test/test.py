#!/usr/bin/env python
# -*- coding: utf-8 -*-

import argparse
from datetime import datetime
import json
import requests
import pdb


def updateToDo(base_url, id, completed):
  """Update data to the todo application

  Args:
    base_url: url
    item_dict: dict of todo item
    completed: bool

  Returns:
    {"updated": true/false}
  """
  data = {
      "id": id,
      "completed": completed
  }

  # Set the endpoint URL
  endpoint = base_url + "/todo/" + str(id)
  # Send a POST request with the data and the endpoint URL
  response = requests.post(endpoint, data=data, verify=False)
  # Check the status code of the response
  if response.status_code == 201 or response.status_code == 200:
      print("Task updated successfully!")
      return True
  else:
      print("Error updating task.")
      return False
  

def createToDo(base_url, description, completed):
  """Post data to the todo application

  Args:
    base_url: url
    description: todo list description
    completed: bool

  Returns:
    id of todo item in db
  """
  data = {
      "description": description,
      "completed": completed
  }

  # Set the endpoint URL
  endpoint = base_url + "/todo"
  # Send a POST request with the data and the endpoint URL
  response = requests.post(endpoint, data=data, verify=False)
  # Check the status code of the response
  if response.status_code == 201 or response.status_code == 200:
      print("Task created successfully!")
  else:
      print("Error creating task.")
  response_dict = json.loads(response.text)
  return response_dict

def checkToDoLists(base_url, completed):
  """Post data to the todo application

  Args:
    base_url: url
    completed: bool

  Returns:
    json list
  """
  # Set the endpoint URL
  if completed:
    endpoint = base_url + "/todo-completed"
  else:
    endpoint = base_url + "/todo-incomplete"
  # Send a POST request with the data and the endpoint URL
  response = requests.get(endpoint, verify=False)
  # Check the status code of the response
  if response.status_code == 201 or response.status_code == 200:
      print("Got list of items")
  else:
      print("Failed to get list of items")
  response_dict = json.loads(response.text)
  return response_dict

def checkAppLog(base_url):
  """Get log data from the todo application
  Args:
    base_url: url

  Returns:
    bool 
  """
  log = False
  endpoint = base_url + "/log"

  # Send a POST request with the data and the endpoint URL
  response = requests.get(endpoint, verify=False)
  # Check the status code of the response
  if response.status_code == 201 or response.status_code == 200:
      print("Got the log")
      log = True
  else:
      print("Failed to get the app log")
      log = False
  return log

def deleteToDoItems(base_url,item):
  """Post data to the todo application

  Args:
    base_url: url
    item: dict

  Returns:
    bool
  """

  endpoint = base_url + "/todo/" + str(item["Id"])
  # Send a POST request with the data and the endpoint URL
  response = requests.delete(endpoint, verify=False)
  # Check the status code of the response
  if response.status_code == 201 or response.status_code == 200:
      print("Deleted item " + str(item["Id"]))
      return True
  else:
      print("Failed to delete item " + str(item["Id"]))
      return False



def main():
   # Define the argument parser
   parser = argparse.ArgumentParser(description='Test the todo application.')

   # Add an argument for the base URL
   parser.add_argument('--base_url', default="http://localhost:8000",
                      help='The base URL of the todo application.')

   # Parse the arguments
   args = parser.parse_args()
   base_url = args.base_url

   # create date
   date = datetime.today().strftime('%Y-%m-%d-%H:%M:%S')
   # create todo items
   test1 = createToDo(base_url, "pytest-1-" + date, False)   
   test2 = createToDo(base_url, "pytest-2-" + date, False)
   test3 = createToDo(base_url, "pytest-1-" + date, False)

   # update todo items
   success = updateToDo(base_url, test1["Id"], True)
   success = updateToDo(base_url, test2["Id"], True)

   # check todo's
   completed = checkToDoLists(base_url, True)
   incomplete = checkToDoLists(base_url, False)
   print("COMPLETED ITEMS:")
   print(completed)
   print("INCOMPLETE ITEMS:")
   print(incomplete)

   # test complete or incomplete
   found_completed = False
   for i in completed:
       if test1["Description"] == i["Description"]:
          found_completed = True
          

   found_incomplete = False
   for i in incomplete:
      if test3["Description"] == i["Description"]:
         found_incomplete = True
   
   if found_completed == False or found_incomplete == False:
      print("FAILED complete / incomplete TEST")
      exit(1)
   else:
      print("SUCCESS!")

   # Delete items
   deleteToDoItems(base_url, test1)
   deleteToDoItems(base_url, test3)
   completed = checkToDoLists(base_url, True)
   incomplete = checkToDoLists(base_url, False)

   # Test deleted items
   found_completed = False
   for i in completed:
       if test1["Description"] == i["Description"]:
          found_completed = True

   found_incomplete = False
   for i in incomplete:
      if test3["Description"] == i["Description"]:
         found_incomplete = True
    
   if found_completed == True or found_incomplete == True:
      print("FAILED Delete TEST")
      exit(1)
   else:
      print("SUCCESS!")

   # Test the app log
   if checkAppLog(base_url):
     print("LOG FOUND: SUCCESS!")
   else:
     print("FAILED!")
     exit(1)

   print("FULL TEST PASSED!")

if __name__ == "__main__":
    main()

