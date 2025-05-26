import React, { useState, useEffect } from "react";
import { useNavigate, Link } from 'react-router-dom';
import auth from "../auth";
import { createUserWithEmailAndPassword, sendEmailVerification, reload } from "firebase/auth";
import "../index.css";

const Register = () => {
  const navigate = useNavigate();
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [message, setMessage] = useState('');
  const [user, setUser] = useState(null);

  useEffect(() => {
    let interval;
    if (user) {
      interval = setInterval(async () => {
        await reload(user); // Оновлюємо інформацію про користувача
        if (user.emailVerified) {
          clearInterval(interval);
          setMessage("Email підтверджено! Перенаправляємо...");
          setTimeout(() => navigate("/"), 1500);
        }
      }, 3000); // перевіряємо кожні 3 секунди
    }
    return () => clearInterval(interval);
  }, [user, navigate]);

  const HandleSignUp = async (e) => {
    e.preventDefault();

    if (password !== confirmPassword) {
      alert("Паролі не співпадають!");
      return;
    }

    try {
      const userCredential = await createUserWithEmailAndPassword(auth, email, password);
      const newUser = userCredential.user;
      setUser(newUser);

      await sendEmailVerification(newUser);

      // Твоє API-запит сюди (можеш лишити як є)
      const username = "admin";
      const password2 = "admin";
      const base64Credentials = btoa(`${username}:${password2}`);

      const response = await fetch("http://localhost:8080/api/users", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Basic ${base64Credentials}`
        },
        body: JSON.stringify({
          id: newUser.uid,
          name: firstName,
          surname: lastName,
          phone: "0966353123",
          email: email,
          password: password,
          createdAt: new Date().toISOString(),
          isVerified: false
        })
      });

      if (!response.ok) {
        throw new Error("Помилка при надсиланні запиту");
      }

      setMessage("Лист підтвердження надіслано. Будь ласка, перевірте пошту і підтвердіть email.");
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
              <input type="text" required value={firstName} onChange={(e) => setFirstName(e.target.value)} className="auth-input" />
            </div>
            <div className="form-group">
              <label>Прізвище</label>
              <input type="text" required value={lastName} onChange={(e) => setLastName(e.target.value)} className="auth-input" />
            </div>
          </div>
          <div className="form-group">
            <label>Електронна пошта</label>
            <input type="email" required value={email} onChange={(e) => setEmail(e.target.value)} className="auth-input" />
          </div>
          <div className="form-group">
            <label>Пароль</label>
            <input type="password" required value={password} onChange={(e) => setPassword(e.target.value)} className="auth-input" />
          </div>
          <div className="form-group">
            <label>Підтвердження паролю</label>
            <input type="password" required value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} className="auth-input" />
          </div>
          <button type="submit" className="auth-button">Зареєструватись</button>
        </form>

        {message && <p style={{ marginTop: "20px", color: user?.emailVerified ? "green" : "orange" }}>{message}</p>}

        <div className="auth-footer">
          <p>
            Вже маєте акаунт? <Link to='/Login' className="auth-link">Увійти</Link>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Register;
