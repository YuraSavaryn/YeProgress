import React, { useState } from "react";
import Header from "./Header";
import Footer from "./Footer";
import "../index.css";

const ContactsPage = () => {
  const [formData, setFormData] = useState({
    name: '',
    email: '',
    subject: '',
    message: ''
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

const handleSubmit = async (e) => {
  e.preventDefault();

  try {
    const response = await fetch("http://localhost:8080/api/contact", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(formData),
    });

    const result = await response.json();

    if (response.ok) {
      alert(result.message); // Повідомлення з сервера: "Повідомлення успішно відправлено!"
      setFormData({
        name: '',
        email: '',
        subject: '',
        message: ''
      });
    } else {
      alert("Помилка: " + result.message);
    }
  } catch (error) {
    alert("Помилка при відправці: " + error.message);
  }
};

  return (
    <div className="contacts-page">
      <Header />
      <main>
        {/* Hero секція */}
        <section className="contact-hero">
          <div className="container">
            <div className="contact-hero-content">
              <h1>Давайте поговоримо!</h1>
              <p>Маєте питання, ідеї чи просто хочете сказати "привіт"? Ми завжди раді спілкуванню!</p>
            </div>
          </div>
        </section>

        {/* Основний контент */}
        <section className="contact-main">
          <div className="container">
            <div className="contact-content">
              
              {/* Форма зв'язку */}
              <div className="contact-form-section">
                <h2>Напишіть нам</h2>
                <form className="contact-form" onSubmit={handleSubmit}>
                  <div className="form-row">
                    <div className="form-group">
                      <label>Ваше ім'я</label>
                      <input 
                        type="text" 
                        name="name"
                        placeholder="Як до вас звертатися?"
                        required
                        value={formData.name}
                        onChange={handleChange}
                        className="contact-input"
                      />
                    </div>
                    <div className="form-group">
                      <label>Email</label>
                      <input 
                        type="email" 
                        name="email"
                        placeholder="ваш@email.com"
                        required
                        value={formData.email}
                        onChange={handleChange}
                        className="contact-input"
                      />
                    </div>
                  </div>
                  
                  <div className="form-group">
                    <label>Тема повідомлення</label>
                    <select 
                      name="subject"
                      required
                      value={formData.subject}
                      onChange={handleChange}
                      className="contact-input"
                    >
                      <option value="">Оберіть тему</option>
                      <option value="investment">Питання про інвестиції</option>
                      <option value="project">Подача проєкту</option>
                      <option value="partnership">Партнерство</option>
                      <option value="support">Технічна підтримка</option>
                      <option value="press">Преса та ЗМІ</option>
                      <option value="other">Інше</option>
                    </select>
                  </div>

                  <div className="form-group">
                    <label>Ваше повідомлення</label>
                    <textarea 
                      name="message"
                      placeholder="Розкажіть нам детальніше..."
                      required
                      value={formData.message}
                      onChange={handleChange}
                      className="contact-input contact-textarea"
                      rows="6"
                    />
                  </div>

                  <button type="submit" className="contact-submit-btn">
                    Відправити повідомлення 📤
                  </button>
                </form>
              </div>

              {/* Інформація про контакти */}
              <div className="contact-info-section">
                <h2>Як з нами зв'язатися</h2>
                
                <div className="contact-methods">
                  <div className="contact-method">
                    <div className="contact-icon">📧</div>
                    <div className="contact-details">
                      <h3>Email</h3>
                      <p>cicipisi@cooperate.com</p>
                      <span>Відповідаємо протягом 24 годин</span>
                    </div>
                  </div>

                  <div className="contact-method">
                    <div className="contact-icon">📍</div>
                    <div className="contact-details">
                      <h3>Офіс</h3>
                      <p>м. Київ, вул. Інноваційна, 42</p>
                      <span>Приходьте на каву!</span>
                    </div>
                  </div>

                  <div className="contact-method">
                    <div className="contact-icon">⏰</div>
                    <div className="contact-details">
                      <h3>Режим роботи</h3>
                      <p>Пн-Пт: 9:00 - 18:00</p>
                      <span>А ідеї у нас цілодобово 💡</span>
                    </div>
                  </div>

                  <div className="contact-method">
                    <div className="contact-icon">💬</div>
                    <div className="contact-details">
                      <h3>Соціальні мережі</h3>
                      <div className="social-links">
                        <a href="#" className="social-link">LinkedIn</a>
                        <a href="#" className="social-link">Facebook</a>
                        <a href="#" className="social-link">Instagram</a>
                      </div>
                      <span>Слідкуйте за нашими новинами</span>
                    </div>
                  </div>
                </div>

                {/* Цікаві факти */}
                <div className="fun-section">
                  <h3>Цікаві факти про нас 🎉</h3>
                  <div className="fun-facts">
                    <div className="fun-fact-item">☕ Випиваємо 47 чашок кави на день (всією командою)</div>
                    <div className="fun-fact-item">🎵 У офісі завжди грає приємна музика</div>
                    <div className="fun-fact-item">🌱 Маємо 12 рослин (всі живі!)</div>
                    <div className="fun-fact-item">🍕 П'ятниця = день піци</div>
                    <div className="fun-fact-item">🦄 Вірimo в єдинорогів-стартапів</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* FAQ секція */}
        <section className="faq-section">
          <div className="container">
            <h2 className="section-title">Часті питання</h2>
            <div className="faq-grid">
              <div className="faq-item">
                <h3>Як швидко ви відповідаєте?</h3>
                <p>Зазвичай протягом 24 годин. Але якщо питання дуже цікаве - можемо відповісти миттєво! ⚡</p>
              </div>
              <div className="faq-item">
                <h3>Чи можна до вас завітати?</h3>
                <p>Звісно! Попередньо напишіть нам, і ми приготуємо найкращу каву в місті ☕</p>
              </div>
              <div className="faq-item">
                <h3>Працюєте у вихідні?</h3>
                <p>Офіційно - ні. Але іноді не можемо встояти перед класною ідеєю навіть у суботу 😄</p>
              </div>
              <div className="faq-item">
                <h3>Як подати проєкт?</h3>
                <p>Просто напишіть нам! Ми обов'язково розглянемо і дамо детальний фідбек 📝</p>
              </div>
            </div>
          </div>
        </section>
      </main>
      <Footer />
    </div>
  );
};

export default ContactsPage;