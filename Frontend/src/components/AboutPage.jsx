import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import "../index.css";

const AboutPage = () => {
const teamMembers = [
    {
      name: "Юрій Саварин",
      position: "Team Lead",
      description: "Керівник команди з глибоким розумінням архітектури проекту. Юрій відповідає за організацію командної роботи, координацію процесів розробки та стратегічне планування. Також займається налаштуванням серверної частини та розробкою складної логіки для взаємодії з базою даних, забезпечуючи стабільність та масштабованість системи.",
      avatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-isolated-background-avatar-profile-picture-man_1293239-4868.jpg",
      funFact: "🎯 Фокусується на розробці архітектури проекту"
    },
    {
      name: "Сергій Ярема",
      position: "UX/UI Designer",
      description: "Відповідальний за створення інтуїтивного та привабливого користувацького досвіду. Сергій проводить детальний аналіз конкурентних рішень, розробляє дизайн-концепції та займається версткою сторінок. Його робота забезпечує високу якість користувацького інтерфейсу та відповідність сучасним стандартам веб-дизайну.",
      avatar: "https://img.freepik.com/premium-vector/man-avatar-profile-picture-isolated-background-avatar-profile-picture-man_1293239-4846.jpg",
      funFact: "🎨 Спеціалізується на створенні адаптивних дизайн-систем"
    }
  ];

  return (
    <>
      <Header />
      <main>
        {/* Hero секція */}
        <section className="about-hero">
          <div className="container">
            <div className="about-hero-content">
              <h1>Ми - команда, що творить майбутнє</h1>
              <p>Двоє друзів, які вирішили змінити світ інвестицій в Україні. Ми не просто робимо бізнес - ми будуємо мости між мріями та реальністю.</p>
            </div>
          </div>
        </section>

        {/* Наша місія */}
        <section className="mission-section">
          <div className="container">
            <div className="mission-content">
              <div className="mission-text">
                <h2>Наша місія</h2>
                <p>Ми створили єПрогрес, щоб кожен українець міг інвестувати в майбутнє своєї країни. Наша платформа - це не просто технологія, це спільнота людей, які вірять у силу колективних інвестицій та інновацій.</p>
                <p>Ми прагнемо зробити інвестування доступним, прозорим та захоплюючим. Кожен проєкт на нашій платформі - це крок до сильнішої України.</p>
              </div>
              <div className="mission-image">
                <img src="https://images.unsplash.com/photo-1522071820081-009f0129c71c?w=600&h=400&fit=crop" alt="Команда за роботою" />
              </div>
            </div>
          </div>
        </section>

        {/* Команда */}
        <section className="team-section">
          <div className="container">
            <h2 className="section-title">Знайомтесь - наша команда мрійників</h2>
            <div className="team-grid">
              {teamMembers.map((member, index) => (
                <div key={index} className="team-card">
                  <div className="team-avatar">
                    <img src={member.avatar} alt={member.name} />
                  </div>
                  <div className="team-info">
                    <h3>{member.name}</h3>
                    <p className="team-position">{member.position}</p>
                    <p className="team-description">{member.description}</p>
                    <div className="fun-fact">{member.funFact}</div>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </section>

        {/* Наші цінності */}
        <section className="values-section">
          <div className="container">
            <h2 className="section-title">Наші цінності</h2>
            <div className="values-grid">
              <div className="value-card">
                <div className="value-icon">🎯</div>
                <h3>Прозорість</h3>
                <p>Кожна копійка, кожен проєкт, кожне рішення - все відкрито та зрозуміло</p>
              </div>
              <div className="value-card">
                <div className="value-icon">💪</div>
                <h3>Надійність</h3>
                <p>Ваші інвестиції під нашим захистом 24/7</p>
              </div>
              <div className="value-card">
                <div className="value-icon">🚀</div>
                <h3>Інновації</h3>
                <p>Ми завжди в пошуку нових способів покращити ваш досвід</p>
              </div>
              <div className="value-card">
                <div className="value-icon">❤️</div>
                <h3>Любов до України</h3>
                <p>Кожен наш проєкт - це внесок у майбутнє нашої країни</p>
              </div>
            </div>
          </div>
        </section>
      </main>
      <Footer />

      <style jsx>{`
        .

        .about-hero {
          background: var(--gradient-bg);
          padding: 6rem 0 4rem;
          color: var(--light-text);
          text-align: center;
        }

        .about-hero h1 {
          font-size: 3rem;
          margin-bottom: 1.5rem;
          font-weight: 700;
        }

        .about-hero p {
          font-size: 1.2rem;
          max-width: 800px;
          margin: 0 auto;
          line-height: 1.6;
          opacity: 0.95;
        }

        .mission-section {
          padding: 5rem 9rem;
          background-color: white;
        }

        .mission-content {
          display: grid;
          grid-template-columns: 1fr 1fr;
          gap: 4rem;
          align-items: center;
        }

        .mission-text h2 {
          font-size: 2.5rem;
          color: var(--secondary-color);
          margin-bottom: 2rem;
          font-weight: 700;
        }

        .mission-text p {
          font-size: 1.1rem;
          line-height: 1.7;
          color: #555;
          margin-bottom: 1.5rem;
        }

        .mission-image img {
          width: 100%;
          border-radius: 15px;
          box-shadow: var(--card-shadow);
        }

        .team-section {
          padding: 5rem 9rem;
          background-color: var(--light-bg);
        }

        .team-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(285px, 1fr));
          gap: 0.8rem;
          margin-top: 3rem;
        }

        .team-card {
          background: white;
          border-radius: 20px;
          padding: 2rem;
          box-shadow: var(--card-shadow);
          transition: var(--hover-transition);
          text-align: center;
        }

        .team-card:hover {
          transform: translateY(-10px);
          box-shadow: 0 15px 30px rgba(138, 104, 207, 0.2);
        }

        .team-avatar {
          width: 120px;
          height: 120px;
          margin: 0 auto 1.5rem;
          border-radius: 50%;
          overflow: hidden;
          border: 4px solid var(--light-color);
        }

        .team-avatar img {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }

        .team-info h3 {
          font-size: 1.5rem;
          color: var(--secondary-color);
          margin-bottom: 0.5rem;
          font-weight: 600;
        }

        .team-position {
          color: var(--main-color);
          font-weight: 500;
          margin-bottom: 1rem;
          font-size: 1rem;
        }

        .team-description {
          color: #666;
          line-height: 1.6;
          margin-bottom: 1rem;
          text-align: left;
        }

        .fun-fact {
          background: var(--gradient-bg);
          color: white;
          padding: 0.5rem 1rem;
          border-radius: 20px;
          font-size: 0.9rem;
          font-weight: 500;
        }

        .values-section {
          padding: 5rem 9rem;
          background-color: white;
        }

        .values-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
          gap: 2rem;
          margin-top: 3rem;
        }

        .value-card {
          text-align: center;
          padding: 2rem;
          border-radius: 15px;
          background: var(--light-bg);
          transition: var(--hover-transition);
        }

        .value-card:hover {
          transform: translateY(-5px);
          box-shadow: var(--card-shadow);
        }

        .value-icon {
          font-size: 3rem;
          margin-bottom: 1rem;
        }

        .value-card h3 {
          color: var(--secondary-color);
          margin-bottom: 1rem;
          font-weight: 600;
        }

        .value-card p {
          color: #666;
          line-height: 1.6;
        }

        .stats-section {
          padding: 4rem 9rem;
          background: var(--gradient-bg);
          color: white;
        }

        .stats-grid {
          display: grid;
          grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
          gap: 2rem;
          text-align: center;
        }

        .stat-number {
          font-size: 3rem;
          font-weight: 700;
          margin-bottom: 0.5rem;
        }

        .stat-label {
          font-size: 1.1rem;
          opacity: 0.9;
        }

        @media (max-width: 768px) {
          .about-hero h1 {
            font-size: 2.2rem;
          }
          
          .mission-content {
            grid-template-columns: 1fr;
            gap: 2rem;
          }
          
          .team-grid {
            grid-template-columns: 1fr;
          }
          
          .stats-grid {
            grid-template-columns: repeat(2, 1fr);
          }
        }
      `}</style>
    </>
  );
};

export default AboutPage;