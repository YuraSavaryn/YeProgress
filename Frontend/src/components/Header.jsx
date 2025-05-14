import React from "react";
import '../index.css'
import { Link } from "react-router-dom";

const Header = () => {
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
              <li><Link to="/login">Увійти</Link></li>
            </ul>
          </nav>
        </div>
      </div>
    </header>
  )
}

export default Header