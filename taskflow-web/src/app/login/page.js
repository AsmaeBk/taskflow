export default function Login() {
  return (
    <div >
      <main className="login-container" >
        <h1>Sign in</h1>
        <p>Welcome back! Please enter your credentials to access your account.</p>
        <form action="/auth/login" method="post" >
          <div >
            <input type="email" id="email" name="email" placeholder="Email" required />
          </div>
          <div >
            <input type="password" id="password" name="password" placeholder="Password" required />
          </div>
          <button type="submit">Sign In</button>
        </form>
      </main>
    </div>
  );
}
