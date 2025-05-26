import React, { useState } from "react";
import { useNavigate, Link } from 'react-router-dom';
import "../index.css";
import auth from "../auth";
import { signInWithPopup, GoogleAuthProvider, fetchSignInMethodsForEmail, EmailAuthProvider, signInWithEmailAndPassword, linkWithCredential } from "firebase/auth";

const Login = () => {
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const provider = new GoogleAuthProvider();

  const HandleLogin = async (e) => {
    e.preventDefault();

    try {
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      console.log("Користувач увійшов: " + userCredential.user.email);
      navigate("/");
    } catch (error) {
      alert("Помилка входу: " + error.message);
    }
  };

  const handleGoogleLogin = async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;
      console.log("Google login: " + user.email);
      navigate("/");
    } catch (error) {
      if (error.code === 'auth/account-exists-with-different-credential') {
        const pendingCred = GoogleAuthProvider.credentialFromError(error);
        const email = error.customData.email;
        const methods = await fetchSignInMethodsForEmail(auth, email);

        if (methods.includes('password')) {
          const password = prompt(`Цей email вже зареєстрований. Введіть пароль для облікового запису ${email} щоб пов’язати з Google:`);
          const userCredential = await signInWithEmailAndPassword(auth, email, password);

          await linkWithCredential(userCredential.user, pendingCred);
          alert("Google акаунт успішно прив’язано!");
          navigate("/");
        } else {
          alert("Обліковий запис вже існує, але має інший тип входу: " + methods.join(', '));
        }
      } else {
        alert("Помилка Google Auth: " + error.message);
      }
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Вхід в систему</h2>
        <form className="auth-form" onSubmit={HandleLogin}>
          <div className="form-group">
            <label>Електронна пошта</label>
            <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} className="auth-input" />
          </div>
          <div className="form-group">
            <label>Пароль</label>
            <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} className="auth-input" />
          </div>
          <button type="submit" className="auth-button">Увійти</button>
        </form>

        <div className="auth-divider">
          <span>або</span>
        </div>

        <button onClick={handleGoogleLogin} className="auth-google-btn">
          <img src="https://developers.google.com/identity/images/g-logo.png" alt="Google logo" className="google-icon" />
          <span>Увійти через Google</span>
        </button>

        <div className="auth-footer">
          <p>Ще не зареєстровані? <Link to="/register" className="auth-link">Створити акаунт</Link></p>
        </div>
      </div>
    </div>
  );
};

export default Login;
