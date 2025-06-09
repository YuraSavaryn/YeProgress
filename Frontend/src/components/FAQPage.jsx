import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import Header from './Header';
import '../index.css';

const FAQPage = () => {
  const [openIndex, setOpenIndex] = useState(null);

  const faqs = [
    {
      question: "Що таке єПрогрес?",
      answer: "єПрогрес – це інвестиційна платформа, яка підтримує проєкти, спрямовані на відбудову та розвиток України. Ми надаємо можливість інвесторам підтримувати інноваційні стартапи та стратегічні ініціативи, сприяючи економічному зростанню країни."
    },
    {
      question: "Як я можу інвестувати через платформу?",
      answer: "Для інвестування перейдіть до розділу <a href='/projects' className='social-link'>Проєкти</a>, оберіть проєкт, ознайомтесь із його деталями та натисніть кнопку інвестування. Ви будете перенаправлені на платіжну сторінку Monobank для безпечного здійснення транзакції."
    },
    {
      question: "Чи безпечно інвестувати через єПрогрес?",
      answer: "Ми співпрацюємо з Monobank для забезпечення безпеки всіх фінансових операцій. Усі проєкти проходять ретельну перевірку перед публікацією, але ми рекомендуємо інвесторам самостійно оцінювати ризики та ознайомлюватися з умовами проєктів."
    },
    {
      question: "Які проєкти можна підтримати?",
      answer: "На платформі представлені різноманітні проєкти: від технологічних стартапів до ініціатив із відновлення інфраструктури. Усі проєкти спрямовані на розвиток України та проходять перевірку перед публікацією."
    },
    {
      question: "Як створити власний проєкт?",
      answer: "Зареєструйтесь на платформі, перейдіть до розділу <a href='/create-project' className='social-link'>Створити проєкт</a>, заповніть форму та подайте заявку. Після перевірки ваш проєкт може бути опубліковано для залучення інвестицій."
    },
    {
      question: "Як зв’язатися з підтримкою?",
      answer: "Ви можете звернутися до нас через <a href='/contacts' className='social-link'>форму зворотного зв’язку</a> на сторінці контактів. Наша команда відповідає протягом 24 годин."
    }
  ];

  const toggleAccordion = (index) => {
    setOpenIndex(openIndex === index ? null : index);
  };

  return (
    <>
    <Header />
    <div className="faq-page">
      <div className="faq-hero">
        <h1>Поширені запитання (FAQ)</h1>
        <p>Знайдіть відповіді на найпоширеніші запитання про роботу платформи єПрогрес, процес інвестування та створення проєктів.</p>
      </div>
      <div className="faq-content">
        <div className="faq-main">
          <h2 className="section-title">Відповіді на ваші запитання</h2>
          {faqs.map((faq, index) => (
            <div className="accordion-item" key={index}>
              <div
                className="accordion-header"
                onClick={() => toggleAccordion(index)}
              >
                <span>{faq.question}</span>
                <span className={`accordion-icon ${openIndex === index ? 'open' : ''}`}>+</span>
              </div>
              <div className={`accordion-content ${openIndex === index ? 'open' : ''}`}>
                <p dangerouslySetInnerHTML={{ __html: faq.answer }} />
              </div>
            </div>
          ))}
          <div className="fun-fact-section">
            <h3>Цікавий факт</h3>
            <div className="fun-fact-item">
              Платформа єПрогрес уже підтримала понад 100 проєктів, які сприяють відбудові України!
            </div>
          </div>
        </div>
        <div className="sidebar">
          <h3>Швидкі посилання</h3>
          <ul>
            <li><Link to="/invest">Як інвестувати</Link></li>
            <li><Link to="/legal">Юридична інформація</Link></li>
            <li><Link to="/contacts">Контакти</Link></li>
            <li><Link to="/about">Про нас</Link></li>
          </ul>
        </div>
      </div>
    </div>
    </>
  );
};

export default FAQPage;