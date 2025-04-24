import React from "react";
import '../index.css'

const Header = () => {
    return(
        <header>
      <div className="container">
        <div className="header-info">

          <div className="logo">
            <a href="#">єПрогрес</a>
          </div>
          <nav>
            <ul className="nav">
              <li> <a href="#">Проєкти</a> </li>
              <li> <a href="#">Про нас</a> </li>
              <li> <a href="#">Контакти</a> </li>
            </ul>
          </nav>
        </div>
      </div>
    </header>
    )
}

export default Header