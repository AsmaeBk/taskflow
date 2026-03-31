"use client"
import { useEffect, useState } from 'react';

export default function TaskNav() {
   const [email, setEmail] = useState('');

  useEffect(() => {
    // Retrieve the email when the component loads
    const savedEmail = localStorage.getItem('email');
    if (savedEmail) {
      setEmail(savedEmail);
    }
  }, []);

    return (
          <nav className="taskflow-nav" >
            <strong>TaskFlow</strong>
            <div className="user-info">
            {email ? `Welcome, ${email}` : 'Guest'}
          </div>
          </nav>
    );
}
    