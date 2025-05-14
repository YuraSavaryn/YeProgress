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
                        <img src="https://gwaramedia.com/wp-content/uploads/2022/07/tecz-51.jpg" height={280} width={470} alt="Енергетичний проєкт" />
                    </div>
                    <div className="project-content">
                         <a href=""><span className="project-category">Відбудова</span></a>
                        <h3 className="project-title">Модернізація енергетичної мережі</h3>
                        <p className="project-excerpt">Проєкт з відновлення та модернізації енергетичної інфраструктури в Харківській області з впровадженням сучасних технологій.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
                    </div>
                </div>
                <div className="project-card">
                    <div className="project-image">
                        <img src="https://hub.kyivstar.ua/assets/cms/uploads/biznes_tehnologii_jpg_a81a98106e.webp" height={280} width={470} alt="Технологічний стартап" />
                    </div>
                    <div className="project-content">
                        <a href=""><span className="project-category">Стартап</span></a> 
                        <h3 className="project-title">EcoFarm - розумне сільське господарство</h3>
                        <p className="project-excerpt">Інноваційна система моніторингу та управління сільськогосподарськими угіддями з використанням ІоТ та штучного інтелекту.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
                    </div>
                </div>
                <div className="project-card">
                    <div className="project-image">
                        <img src="https://vidnova.ua/wp-content/uploads/2024/03/IMG_2318-HDR-2-scaled.jpg" height={280} width={470} alt="Медичний заклад" />
                    </div>
                    <div className="project-content">
                        <a href=""><span className="project-category">Відбудова</span></a> 
                        <h3 className="project-title">Сучасна клініка в Миколаєві</h3>
                        <p className="project-excerpt">Проєкт з будівництва та обладнання сучасного медичного центру для забезпечення якісної медичної допомоги.</p>
                        <a href="#" className="btn btn-primary">Детальніше</a>
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

      <section className="About">
        <div className="container">
          <div className="about-us">
            <div className="img">
              <img src="https://fil-bud.if.ua/wp-content/uploads/2023/04/cde58075-0b4b-49e8-879b-1ceb345db6a0.jpg" width={400} height={250} alt="" />
            </div>

            <div className="info">
              Ми — платформа, що об’єднує українських підприємців, стартапи та проєкти з відбудови з інвесторами, які вірять у майбутнє України.
            Наша мета — зробити інвестиції прозорими, безпечними та доступними для кожного, хто хоче долучитися до економічного та соціального 
            відновлення нашої країни. Платформа дозволяє легко знайти проєкти, що потребують фінансування — від інфраструктурних ініціатив до інноваційних технологій.
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