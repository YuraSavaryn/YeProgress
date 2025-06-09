import React from "react"
import '../index.css';
import Header from "./Header";
import Footer from "./Footer";
import auth from "../auth";
import { Link, useNavigate } from "react-router-dom";
import { useState, useEffect} from "react";

const MainPage = () => {
  const [projects, setProjects] = useState([
      {
        id: 1,
        title: "Модернізація енергетичної мережі",
        description: "Проєкт з відновлення та модернізації енергетичної інфраструктури в Харківській області з впровадженням сучасних технологій.",
        category: "Відбудова",
        goal: 5000000,
        collected: 1250000,
        image: "https://gwaramedia.com/wp-content/uploads/2022/07/tecz-51.jpg"
      },
      {
        id: 2,
        title: "EcoFarm - розумне сільське господарство",
        description: "Інноваційна система моніторингу та управління сільськогосподарськими угіддями з використанням ІоТ та штучного інтелекту.",
        category: "Стартап",
        goal: 2500000,
        collected: 750000,
        image: "https://hub.kyivstar.ua/assets/cms/uploads/biznes_tehnologii_jpg_a81a98106e.webp"
      },
      {
        id: 3,
        title: "Сучасна клініка в Миколаєві",
        description: "Проєкт з будівництва та обладнання сучасного медичного центру для забезпечення якісної медичної допомоги.",
        category: "Відбудова",
        goal: 10000000,
        collected: 3200000,
        image: "https://vidnova.ua/wp-content/uploads/2024/03/IMG_2318-HDR-2-scaled.jpg"
      }
    ]);

    const [isAuth, setIsAuth] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
      const unsubscribe = auth.onAuthStateChanged((user) => {
      setIsAuth(!!user);
    });
      return () => unsubscribe();
    }, []);

    const handleCreateClick = () => {
      if (isAuth) {
        navigate("/create-project");
      } else {
        navigate("/login");
      }
    };

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
              <Link to="/projects" className="btn btn-first"> Інвестувати зараз </Link>
              <Link to="/about" className="btn btn-second"> <p>Дізнатися більше </p> </Link>
            </div>
        </div>
      </section>

      <section className="projects">
        <div className="container">
            <h2 className="section-title">Поточні проєкти</h2>
            <div className="project-cards">
              {projects.map(project => (
                <div key={project.id} className="project-card">
                  <div className="project-image">
                    <img src={project.image} alt={project.title} />
                  </div>
                  <div className="project-content">
                    <span className="project-category">{project.category}</span> <br />
                    <Link to={`/project/${project.id}`} className="project-title-link">{project.title}</Link>
                    <p className="project-excerpt">{project.description}</p>
                    
                    <div className="project-progress">
                      <div className="progress-bar">
                        <div 
                          className="progress-fill" 
                          style={{width: `${(project.collected / project.goal) * 100}%`}}
                        ></div>
                      </div>
                      <div className="progress-info">
                        <span>Зібрано: {project.collected.toLocaleString()} ₴</span>
                        <span>Ціль: {project.goal.toLocaleString()} ₴</span>
                      </div>
                    </div>
                    
                    <Link to={`/project/${project.id}`} className="btn btn-primary">
                      Детальніше
                    </Link>
                  </div>
                </div>
              ))}
            </div>
        </div>
    </section>

    <section className="create-own-project">
        <div className="container">
          <div className="create-project">
            <h3 className="create-title">Створи своє оголошення</h3>
            <p>Опиши свій інвестиційний проєкт або ідею, щоб зацікавити потенційних інвесторів. Створи оголошення, яке допоможе твоїй ініціативі стати помітною</p>
            <div className="buttons">
              <button onClick={handleCreateClick} className="btn btn-first">
                Створити оголошення
              </button>
            </div>
          </div>
        </div>
      </section>

      <section className="investments">
        <div className="container">
          <div className="Invest" id="Invest">
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