import React from "react";
import '../index.css'

const Footer = () => {
    return(
        <footer>
      <div className="container-footer">
        <div className="about">
            <div className="about-logo">єПрогрес</div>
            <p className="about-text">Інвестиційна платформа для відбудови та розвитку України через підтримку стратегічних проєктів та інноваційних стартапів</p>
        </div>
        <div className="links">
          <div className="links-head">Для інвесторів</div>
          <ul>
            <li><a href="#">Як інвестувати</a></li>
            <li><a href="#">Юридична інформація</a></li>
            <li><a href="#">FAQ</a></li>
          </ul>
        </div>

        <div className="copyright">
          © 2025 єПрогрес. Усі права захищено.
        </div>
      </div>
    </footer>
    )
}

export default Footer;