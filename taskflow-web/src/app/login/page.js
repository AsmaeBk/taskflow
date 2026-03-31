"use client"
import Link from 'next/link';
import { useState } from 'react';

export default function Login() {

  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();
    setLoading(true);

    const formData = new FormData(event.currentTarget);
    const email = formData.get('email');
    const password = formData.get('password');

    try {
      const response = await fetch('http://localhost:8080/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password }),
      });

      if (response.ok) {
        const token = await response.text(); 
        localStorage.setItem('token', token);
        localStorage.setItem('email', email);
        window.location.href = 'tasks';
      } else {
        console.error('Login failed');
        alert('Invalid credentials, please try again.');
      }
    } catch (error) {
      console.error('Network error:', error);
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className="login-body" >
      <main className="login-container" >
        <h1>Sign in</h1>
        <p>Welcome back! Please enter your credentials to access your account.</p>
        <form onSubmit={handleSubmit} method="post" >
          <div >
            <input type="email" id="email" name="email" placeholder="Email" required />
          </div>
          <div >
            <input type="password" id="password" name="password" placeholder="Password" required />
          </div>
          <button type="submit">Sign In</button>
          <Link href="/register" className="signup-link"><p>Don't have an account? Sign Up</p></Link>
        </form>
      </main>
    </div>
  );
}
