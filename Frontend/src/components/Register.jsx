import React, { useState } from "react";
import { useNavigate, Link } from 'react-router-dom';
import auth from "../auth";
import { 
  createUserWithEmailAndPassword,
  signInWithPopup,
  GoogleAuthProvider 
} from "firebase/auth";
import "../index.css";

const Register = () => {
  const navigate = useNavigate();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const provider = new GoogleAuthProvider();

  const handleGoogleRegister = async () => {
    try {
      const result = await signInWithPopup(auth, provider);
      const user = result.user;
      console.log("Успішна реєстрація користувача: " + user.email);
      navigate("/");
    } catch (error) {
      alert("Помилка: " + error.message);
    }
  };

  const HandleSignUp = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Паролі не співпадають!");
      return;
    }

    try {
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      console.log("Користувача успішно створено: " + userCredential.user.email);
      navigate("/");
    } catch (error) {
      alert("Помилка: " + error.message);
    }
  }

  return (
    <div className="auth-container">
      <div className="auth-card">
        <h2 className="auth-title">Реєстрація користувача</h2>
        <form className="auth-form" onSubmit={HandleSignUp}>
          <div className="form-row">
            <div className="form-group">
              <label>Ім'я</label>
              <input 
                type="text" 
                placeholder="Введіть ім'я" 
                required 
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                className="auth-input"
              />
            </div>
            <div className="form-group">
              <label>Прізвище</label>
              <input 
                type="text" 
                placeholder="Введіть прізвище" 
                required 
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                className="auth-input"
              />
            </div>
          </div>
          <div className="form-group">
            <label>Електронна пошта</label>
            <input 
              type="email" 
              placeholder="example@email.com" 
              required 
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="auth-input"
            />
          </div>
          <div className="form-group">
            <label>Пароль</label>
            <input 
              type="password" 
              placeholder="********" 
              required 
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="auth-input"
            />
          </div>
          <div className="form-group">
            <label>Підтвердження паролю</label>
            <input 
              type="password" 
              placeholder="********" 
              required 
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="auth-input"
            />
          </div>
          <button type="submit" className="auth-button">
            Зареєструватись
          </button>
        </form>
        <div className="auth-divider">
          <span>або</span>
        </div>

        <button onClick={handleGoogleRegister} className="auth-google-btn">
          <img
            src="https://developers.google.com/identity/images/g-logo.png"
            alt="Google logo"
            className="google-icon"
          />
          <span>Зареєструватись через Google</span>
        </button>

        <div className="auth-footer">
          <p>
            Вже маєте акаунт?
            <Link to='/Login'
              className="auth-link"
            >
              Увійти
            </Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;