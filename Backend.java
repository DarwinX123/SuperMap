//Create new to do list

@PostMapping("/todos")
public Todo createTodo(@RequestBody Todo todo) {
    return todoRepository.save(todo);
}

//Get all existing to do list

@GetMapping("/todos")
public List<Todo> getAllTodos(@RequestParam(required = false) String creator, @RequestParam(required = false) LocalDate date) {
    if (creator != null && date != null) {
        return todoRepository.findByCreatorAndDate(creator, date);
    } else if (creator != null) {
        return todoRepository.findByCreator(creator);
    } else if (date != null) {
        return todoRepository.findByDate(date);
    } else {
        return todoRepository.findAll();
    }
}

//Update existing to do list

@PutMapping("/todos/{id}")
public ResponseEntity<Todo> updateTodo(@PathVariable Long id, @RequestBody Todo updatedTodo) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

    todo.setTitle(updatedTodo.getTitle());
    todo.setDescription(updatedTodo.getDescription());
    todo.setDueDate(updatedTodo.getDueDate());
    todo.setCreator(updatedTodo.getCreator());

    Todo updated = todoRepository.save(todo);
    return ResponseEntity.ok(updated);
}

//Delete to do list

@DeleteMapping("/todos/{id}")
public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

    todoRepository.delete(todo);
    return ResponseEntity.noContent().build();
}

//Get to do lst by ID

@GetMapping("/todos/{id}")
public ResponseEntity<Todo> getTodoById(@PathVariable Long id) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));
    return ResponseEntity.ok(todo);
}

//Mark to do list as complete

@PatchMapping("/todos/{id}/complete")
public ResponseEntity<Todo> markTodoAsCompleted(@PathVariable Long id) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

    todo.setCompleted(true);
    Todo updated = todoRepository.save(todo);
    return ResponseEntity.ok(updated);
}

//Get complete to do list

@GetMapping("/todos/completed")
public List<Todo> getCompletedTodos() {
    return todoRepository.findByCompletedTrue();
}

//Get overdue to do list

@GetMapping("/todos/overdue")
public List<Todo> getOverdueTodos() {
    LocalDate currentDate = LocalDate.now();
    return todoRepository.findByDueDateBeforeAndCompletedFalse(currentDate);
}

//Get to do list by priority

@GetMapping("/todos/priority/{priority}")
public List<Todo> getTodosByPriority(@PathVariable String priority) {
    return todoRepository.findByPriority(priority);
}

//Update to do list priority

@PutMapping("/todos/{id}/priority")
public ResponseEntity<Todo> updateTodoPriority(@PathVariable Long id, @RequestParam String priority) {
    Todo todo = todoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id: " + id));

    todo.setPriority(priority);
    Todo updated = todoRepository.save(todo);
    return ResponseEntity.ok(updated);
}
