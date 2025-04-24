import React from "react"
import '../index.css';
import Header from "./Header";
import Footer from "./Footer";

const MainPage = () => {
    return(
        <>
    <Header />
    <main>
      <section className="hero">
        <div className="container" id="hero-cont">
            <h1>Інвестуйте в майбутнє України</h1>
            <div className="hero-text">
              <p>Інвестиційна платформа, що з'єднує інвесторів з проєктами відбудови України та інноваційними стартапами. Разом ми створюємо сильнішу, сучаснішу та процвітаючу Україну</p>
            </div>
            <div className="buttons">
              <a href="#" className="btn btn-first"> Інвестувати зараз </a>
              <a href="#" className="btn btn-second"> <p>Дізнатися більше </p> </a>
            </div>
        </div>
      </section>

      <section className="projects">
        <div className="container">
            <h2 className="section-title">Поточні проєкти</h2>
            <div className="project-cards">
                <div className="project-card">
                    <div className="project-image">
                        <img src="" height={280} alt="Енергетичний проєкт" />
                    </div>
                    <div className="project-content">
                        <span className="project-category">Відбудова</span>
                        <h3 className="project-title">Модернізація енергетичної мережі</h3>
                        <p className="project-excerpt">Проєкт з відновлення та модернізації енергетичної інфраструктури в Харківській області з впровадженням сучасних технологій.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
                    </div>
                </div>
                <div className="project-card">
                    <div className="project-image">
                        <img src="" height={280} alt="Технологічний стартап" />
                    </div>
                    <div className="project-content">
                        <span className="project-category">Стартап</span>
                        <h3 className="project-title">EcoFarm - розумне сільське господарство</h3>
                        <p className="project-excerpt">Інноваційна система моніторингу та управління сільськогосподарськими угіддями з використанням ІоТ та штучного інтелекту.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
                    </div>
                </div>
                <div className="project-card">
                    <div className="project-image">
                        <img src="" height={280} alt="Медичний заклад" />
                    </div>
                    <div className="project-content">
                        <span className="project-category">Відбудова</span>
                        <h3 className="project-title">Сучасна клініка в Миколаєві</h3>
                        <p className="project-excerpt">Проєкт з будівництва та обладнання сучасного медичного центру для забезпечення якісної медичної допомоги.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
                    </div>
                </div>
            </div>
        </div>
    </section>

      <section className="investments">
        <div className="container">
          <div className="Invest">
            <h3 className="investition-title">У що інвестувати?</h3>
            <div className="invest-grid">
                <div className="invest-card">
                    <div className="invest-icon">🏙️</div>
                    <h3 className="invest-title">Відбудова інфраструктури</h3>
                    <p>Інвестуйте у проєкти відновлення та модернізації критичної інфраструктури, включаючи енергетику, транспорт та міське середовище.</p>
                </div>
                <div className="invest-card">
                    <div className="invest-icon">💡</div>
                    <h3 className="invest-title">Інноваційні стартапи</h3>
                    <p>Підтримуйте перспективні українські стартапи в сферах ІТ, агротехнологій, зеленої енергетики та інших інноваційних галузях.</p>
                </div>
                <div className="invest-card">
                    <div className="invest-icon">🤝</div>
                    <h3 className="invest-title">Прозорість і безпека</h3>
                    <p>Наша платформа забезпечує повну прозорість інвестицій, юридичний супровід та захист ваших вкладень.</p>
                </div>
            </div>
        </div>
        </div>
      </section>

      <section className="create-own-project">
        <div className="container">
          <div className="create-project">
            <h3 className="create-title">Створи своє оголошення</h3>
            <p>Опиши свій інвестиційний проєкт або ідею, щоб зацікавити потенційних інвесторів. Створи оголошення, яке допоможе твоїй ініціативі стати помітною</p>
            <div className="buttons">
              <a href="#" className="btn btn-first">Створити оголошення</a>
            </div>
          </div>
        </div>
      </section>

    </main>
    <Footer />
        </>
    )
}

export default MainPage