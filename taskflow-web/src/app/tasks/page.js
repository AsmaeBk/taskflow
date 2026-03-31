"use client"
import { useState, useEffect } from 'react';

export default function Home() {

  const [loading, setLoading] = useState(false);
  const [tasks, setTasks] = useState([]);
  const [editingId, setEditingId] = useState(null);
  const [editText, setEditText] = useState("");
  const [newTitle, setNewTitle] = useState("");
  const [newDesc, setNewDesc] = useState("");

  const autumnPalette = [
    '#b4c7b1', // Sage
    '#d98b77', // Terracotta
    '#e8e4c9', // Sand
    '#a1887f', // Medium Latte
    '#f5f5dc'  // Beige
  ];

  const handleAddTask = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    
    // We send the object exactly as your Java 'Task' class expects it
    const taskData = {
      title: newTitle,
      description: newDesc,
      completed: false
      // userId is usually handled by the Backend via the JWT token!
    };

    const response = await fetch('http://localhost:8080/tasks', {
      method: 'POST',
      headers: { 
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json' 
      },
      body: JSON.stringify(taskData)
    });

    if (response.ok) {
      const createdTask = await response.json();
      setTasks([createdTask, ...tasks]);
      setNewTitle("");
      setNewDesc("");
    }
  };
  // Function to pick a color based on the task ID
  // (This ensures the color stays the same for that task every time you refresh)
  const getTaskColor = (id) => {
    return autumnPalette[id % autumnPalette.length];
  };


  useEffect(() => {
    const fetchTasks = async () => {
      setLoading(true);
      try {
        const token = localStorage.getItem('token');
        const response = await fetch('http://localhost:8080/tasks', {
          headers: { 'Authorization': `Bearer ${token}` },
        });
        if (response.ok) {
          const data = await response.json();
          setTasks(data);
        } else {
          console.error('Failed to fetch tasks');
        }
      } catch (error) {
        console.error('Network error:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTasks();
  }, []);
  
  const toggleTask = async (task) => {
    try {
      const token = localStorage.getItem('token');
      const response = await fetch(`http://localhost:8080/tasks/${task.id}`, {
        method: 'PUT',
        headers: {
           'Authorization': `Bearer ${token}` 
      
          , 'Content-Type': 'application/json' },
        
        body: JSON.stringify({ ...task, completed: !task.completed })
      });
      if (response.ok) {
        // Update the task's completed status in the UI
        setTasks(prevTasks => 
          prevTasks.map(t => 
            t.id === task.id ? { ...t, completed: !t.completed } : t
          )
        );
      } else {
        console.error('Failed to toggle task');
      }
    } catch (error) {
      console.error('Network error:', error);
    }
  }

  const sortedTasks = [...tasks].sort((a, b) => {
    if (a.completed === b.completed) return 0;
    return a.completed ? 1 : -1;
  });

  const handleDelete = async (id) => {
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`http://localhost:8080/tasks/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        // Remove the task from the screen immediately
        setTasks(prev => prev.filter(t => t.id !== id));
      }
    } catch (error) {
      console.error("Delete failed:", error);
    }
  };


  const startEditing = (task) => {
    setEditingId(task.id);
    setEditText(task.description);
  };

  const saveEdit = async (task) => {
    const token = localStorage.getItem('token');
    const updatedTask = { ...task, description: editText };

    try {
      const response = await fetch(`http://localhost:8080/tasks/${task.id}`, {
        method: 'PUT',
        headers: { 
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json' 
        },
        body: JSON.stringify(updatedTask)
      });

      if (response.ok) {
        setTasks(prev => prev.map(t => t.id === task.id ? updatedTask : t));
        setEditingId(null); // Close the input field
      }
    } catch (error) {
      console.error("Update failed:", error);
    }
  };

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email');
    window.location.href = '/login';
  };
  const taskList = sortedTasks.map(task => {
    const accentColor = getTaskColor(task.id);
    const isEditing = editingId === task.id;

    return (
      <div 
        key={task.id} 
        className="task-card" 
        style={{ '--task-accent': accentColor, borderLeftColor: accentColor }} 
      >
        <input 
          type="checkbox" 
          className="custom-checkbox"
          checked={task.completed}
          onChange={() => toggleTask(task)}
        />

        <div className="task-content">
          {/* Render Title */}
          <h3 className={task.completed ? "task-done" : ""}>
            {task.title || "Untitled Task"}
          </h3>

          {/* Render Description or Edit Input */}
          {isEditing ? (
            <input 
              className="edit-input"
              value={editText}
              onChange={(e) => setEditText(e.target.value)}
              onBlur={() => saveEdit(task)}
              onKeyDown={(e) => e.key === 'Enter' && saveEdit(task)}
              autoFocus
            />
          ) : (
            <p 
              className={task.completed ? "task-done" : ""}
              onClick={() => !task.completed && startEditing(task)}
              style={{ cursor: 'pointer' }}
            >
              {task.description}
            </p>
          )}
        </div>

        <button className="delete-btn" onClick={() => handleDelete(task.id)}>×</button>
      </div>
    );
  });
  
return (
  <div className="task-body">
    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '2rem' }}>
      <h1 style={{ visibility: tasks.length > 0 ? 'visible' : 'hidden', margin: 0 }}>My Tasks🍃</h1>
      <button 
        onClick={handleLogout} 
        style={{ 
          padding: '10px 20px', 
          backgroundColor: '#1a1a1a', 
          color: 'white', 
          border: 'none', 
          borderRadius: '8px', 
          cursor: 'pointer', 
          fontWeight: '600',
          transition: 'background 0.3s ease'
        }}
        onMouseOver={(e) => e.target.style.backgroundColor = '#d98b77'}
        onMouseOut={(e) => e.target.style.backgroundColor = '#1a1a1a'}
      >
        Sign Out
      </button>
    </div>
    
    {/* 1. Only show the container if we have tasks */}
    <main className={tasks.length > 0 ? "task-container" : "empty-state"}>
      <form onSubmit={handleAddTask} className="new-task-form">
        <input 
          placeholder="Title..." 
          value={newTitle} 
          onChange={(e) => setNewTitle(e.target.value)} 
          className="title-input"
        />
        <input 
          placeholder="Description..." 
          value={newDesc} 
          onChange={(e) => setNewDesc(e.target.value)} 
        />
        <button type="submit" className="add-btn">+</button>
      </form>

      {loading ? (
        <p>Loading your tasks... 🍃</p>
      ) : tasks.length > 0 ? (
        /* 2. Render the array directly here. 
           No extra wrapper div needed inside the main! */
        taskList
      ) : (
        <p>No tasks found. Time to take a break!</p>
      )}
      
    </main>
  </div>
);
  // return (
  //   <div className="task-body">
  //     <h1>My Tasks</h1>
  //     <main className={taskList.length > 0 ? "task-container" : ""} >
  //      <div className={taskList.length > 0 ?"task-card" : ""} >
  //       {loading ? 'Loading tasks...' : taskList.length > 0 ? taskList : 'No tasks found.'}
  //      </div> 
  //     </main>
  //   </div>
  // );
}
