import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import auth from "../auth";
import '../index.css';

const Header = () => {
  const [isAuth, setIsAuth] = useState(false);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user) => {
      setIsAuth(!!user);
      setIsLoading(false);
    });

    return () => unsubscribe();
  }, []);

  if (isLoading) {
    return (
      <header>
        <div className="container">
          <div className="header-info">
            <div className="logo">
              <Link to="/">єПрогрес</Link>
            </div>
            <nav>
              <ul className="nav">
                <li><Link to="/projects">Проєкти</Link></li>
                <li><Link to="/about">Про нас</Link></li>
                <li><Link to="/contacts">Контакти</Link></li>
                <li>Завантаження...</li>
              </ul>
            </nav>
          </div>
        </div>
      </header>
    );
  }

  return (
    <header>
      <div className="container">
        <div className="header-info">
          <div className="logo">
            <Link to="/">єПрогрес</Link>
          </div>
          <nav>
            <ul className="nav">
              <li><Link to="/projects">Проєкти</Link></li>
              <li><Link to="/about">Про нас</Link></li>
              <li><Link to="/contacts">Контакти</Link></li>
              {isAuth ? (
                <li><Link to="/account">Аккаунт</Link></li>
              ) : (
                <li><Link to="/login">Увійти</Link></li>
              )}
            </ul>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;