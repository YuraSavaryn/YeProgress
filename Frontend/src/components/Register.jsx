import React from "react";
import "../index.css";

const Register = () => {
  const handleGoogleRegister = () => {
    alert("Google авторизація поки не реалізована");
  };

  return (
    <div className="register-container">
      <h2 className="register-title">Реєстрація користувача</h2>
      <form className="register-form">
        <div className="form-group">
          <label>Ім'я</label>
          <input type="text" placeholder="Введіть ім’я" required />
        </div>
        <div className="form-group">
          <label>Прізвище</label>
          <input type="text" placeholder="Введіть прізвище" required />
        </div>
        <div className="form-group">
          <label>Електронна пошта</label>
          <input type="email" placeholder="example@email.com" required />
        </div>
        <div className="form-group">
          <label>Пароль</label>
          <input type="password" placeholder="********" required />
        </div>
        <div className="form-group">
          <label>Підтвердження паролю</label>
          <input type="password" placeholder="********" required />
        </div>
        <button type="submit" className="btn btn-first">Зареєструватись</button>
      </form>

      <div className="google-divider">або</div>

      <button onClick={handleGoogleRegister} className="google-btn">
  <img
    src="https://developers.google.com/identity/images/g-logo.png"
    alt="Google logo"
    className="google-icon"
  />
  Зареєструватись через Google
</button>

    </div>
  );
};

export default Register;
