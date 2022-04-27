conn = new Mongo();
db = conn.getDB("todolist");


db.myCollectionName.createIndex({ "todo_items_models.zip": 1 }, { unique: false });

