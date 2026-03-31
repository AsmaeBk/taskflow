"use client"
import { useState } from 'react';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function Signup() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const router = useRouter();

  const handleSignup = async (e) => {
    e.preventDefault();
    if (password !== confirmPassword) {
      alert("Passwords do not match!");
      return;
    }
    const response = await fetch('http://localhost:8080/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ email, password })
    });

    if (response.ok) {
      alert("Sign up successful! Now please Sign In.");
      router.push('/login'); 
    } else {
      alert("Registration failed. Try a different email.");
    }
  };

  return (
    <div className="auth-body">
      <div className="auth-sidebar">
        <h1>Organize Your<br/>Life Today</h1>
      </div>
      
      <main className="auth-main">
        <div className="auth-card">
          <h1>Create Your Account</h1>
          <p>Join TaskFlow and start organizing your tasks beautifully.</p>
          
          <form onSubmit={handleSignup}>
            <div className="input-group">
              <label className="input-label">Your email</label>
              <input 
                type="email"
                placeholder="Email" 
                required
                value={email} 
                onChange={(e) => setEmail(e.target.value)} 
              />
            </div>
            
            <div className="input-group">
              <label className="input-label">Create Password</label>
              <input 
                type="password" 
                placeholder="Password" 
                required
                value={password} 
                onChange={(e) => setPassword(e.target.value)} 
              />
            </div>

            <div className="input-group">
              <label className="input-label">Confirm Password</label>
              <input 
                type="password" 
                placeholder="Confirm password" 
                required
                value={confirmPassword} 
                onChange={(e) => setConfirmPassword(e.target.value)} 
              />
            </div>

            <button type="submit" className="auth-submit-btn">Create Account</button>
            <div style={{ textAlign: 'center', marginTop: '20px' }}>
              <Link href="/login" className="signup-link">
                <p>Already have an account? <strong>Sign In</strong></p>
              </Link>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}