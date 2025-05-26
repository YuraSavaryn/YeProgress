import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import auth from "../auth";
import "../index.css";

const Header = () => {
  const [isAuth, setIsAuth] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged((user) => {
      setIsAuth(!!user);
      setIsLoading(false);
    });

    return () => unsubscribe();
  }, []);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  if (isLoading) {
    return (
      <header>
        <div className="container">
          <div className="header-info">
            <div className="logo">
              <Link to="/">єПрогрес</Link>
            </div>
            <button className="hamburger" onClick={toggleMenu}>
              ☰
            </button>
            <nav>
              <ul className={`nav ${isMenuOpen ? "nav-mobile active" : "nav-mobile"}`}>
                <li><Link to="/projects" onClick={toggleMenu}>Проєкти</Link></li>
                <li><Link to="/about" onClick={toggleMenu}>Про нас</Link></li>
                <li><Link to="/contacts" onClick={toggleMenu}>Контакти</Link></li>
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
          <button className="hamburger" onClick={toggleMenu}>
            ☰
          </button>
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
            <ul className={`nav-mobile ${isMenuOpen ? "active" : ""}`}>
              <li><Link to="/projects" onClick={toggleMenu}>Проєкти</Link></li>
              <li><Link to="/about" onClick={toggleMenu}>Про нас</Link></li>
              <li><Link to="/contacts" onClick={toggleMenu}>Контакти</Link></li>
              {isAuth ? (
                <li><Link to="/account" onClick={toggleMenu}>Аккаунт</Link></li>
              ) : (
                <li><Link to="/login" onClick={toggleMenu}>Увійти</Link></li>
              )}
            </ul>
          </nav>
        </div>
      </div>
    </header>
  );
};

export default Header;