"use client"
import { useState } from 'react';
import { useRouter } from 'next/navigation';

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const router = useRouter();

  const handleSignup = async (e) => {
    e.preventDefault();
    const response = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (response.ok) {
      alert("Sign up successful! Now please Sign In.");
      router.push('/login'); // Send them to the sign-in page
    } else {
      alert("Registration failed. Try a different email.");
    }
  };

  return (
    <div className="auth-body">
      <form onSubmit={handleSignup} className="auth-card">
        <h2>Create Account 🍃</h2>
        <input 
          placeholder="Email" 
          value={email} 
          onChange={(e) => setEmail(e.target.value)} 
        />
        <input 
          type="password" 
          placeholder="Password" 
          value={password} 
          onChange={(e) => setPassword(e.target.value)} 
        />
        <button type="submit" className="add-btn">Sign Up</button>
      </form>
    </div>
  );
}