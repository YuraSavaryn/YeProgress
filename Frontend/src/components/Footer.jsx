import React from "react";
import { Link } from "react-router-dom";
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
            <li><Link to="/invest">Як інвестувати</Link></li>
            <li><Link to="/legal">Юридична інформація</Link></li>
            <li><Link to="/faq">FAQ</Link></li>
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