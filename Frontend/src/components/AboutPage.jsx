import React from "react";
import Header from "./Header";
import Footer from "./Footer";
import "../index.css";

const AboutPage = () => {
  const teamMembers = [
    {
      name: "Юра",
      position: "CEO & Головний Мрійник",
      description: "Колись мріяв стати космонавтом, але зрозумів, що будувати ракети для української економіки набагато цікавіше. Обожнює каву (п'є по 7 чашок на день), грає на укулеле та вірить, що кожен стартап може змінити світ. У вільний час збирає LEGO і розповідає всім, що це для 'розвитку креативного мислення'.",
      avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=300&h=300&fit=crop&crop=face",
      funFact: "🚀 Має колекцію з 47 ракет LEGO"
    },
    {
      name: "Сергій",
      position: "CTO & Технічний Чарівник",
      description: "Програміст з душею поета та руками, що можуть налагодити будь-який код (і кавомашину в офісі). Спить з відкритим MacBook, бачить сни мовою Python та щиро вважає, що найкращі ідеї приходять о 3 ранку. Колекціонує вінтажні клавіатури та знає напам'ять всі шоткати VS Code.",
      avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=300&h=300&fit=crop&crop=face",
      funFact: "⌨️ Найшвидший друкарик офісу - 127 слів/хв"
    },
    {
      name: "Іван",
      position: "CMO & Король Контенту",
      description: "Маркетолог, який може продати лід ескімосу, а ескімосу - обігрівач. Знає всі тренди соцмереж ще до того, як вони стануть трендами. Говорить емодзі краще за текстом, може створити вірусний мем за 5 хвилин та завжди знає, де найкраща піца в місті. Його інстаграм - це мистецтво.",
      avatar: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=300&h=300&fit=crop&crop=face",
      funFact: "📱 Перший в Україні набрав 100К фоловерів в TikTok"
    },
    {
      name: "Назар",
      position: "CFO & Фінансовий Ніндзя",
      description: "Бачить числа там, де інші бачать хаос. Може розрахувати рентабельність інвестицій в умі, знає курс валют на завтра і завжди знайде гроші в бюджеті на корпоратив. Грає в шахи, читає фінансові звіти перед сном та робить найкращі Excel-таблиці у всесвіті. Його коронна фраза: 'А чи це рентабельно?'",
      avatar: "https://images.unsplash.com/photo-1519085360753-af0119f7cbe7?w=300&h=300&fit=crop&crop=face",
      funFact: "📊 Створив Excel-формулу, яка передбачає погоду"
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
              <p>Четверо друзів, які вирішили змінити світ інвестицій в Україні. Ми не просто робимо бізнес - ми будуємо мости між мріями та реальністю.</p>
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

        {/* Статистика */}
        <section className="stats-section">
          <div className="container">
            <div className="stats-grid">
              <div className="stat-item">
                <div className="stat-number">250+</div>
                <div className="stat-label">Успішних проєктів</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">₴15М</div>
                <div className="stat-label">Залучено інвестицій</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">5000+</div>
                <div className="stat-label">Активних інвесторів</div>
              </div>
              <div className="stat-item">
                <div className="stat-number">95%</div>
                <div className="stat-label">Задоволених клієнтів</div>
              </div>
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </>
  );
};

export default AboutPage;