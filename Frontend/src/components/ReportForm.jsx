import React, { useState } from "react";
import auth from "../auth";
import "../index.css";

const ReportForm = ({ projectId, projectTitle, onClose, onReportSubmitted }) => {
  const [reportText, setReportText] = useState("");
  const [submitting, setSubmitting] = useState(false);

  const handleSubmitReport = async (e) => {
    e.preventDefault();
    
    if (!reportText.trim()) {
      alert("Будь ласка, опишіть причину скарги");
      return;
    }

    setSubmitting(true);

    try {
      const user = auth.currentUser;
      if (!user) {
        alert("Будь ласка, увійдіть, щоб подати скаргу");
        return;
      }

      // Використовуємо Basic Auth як у ProjectDetailPage
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      // 1. Отримуємо ID користувача з бекенду
      const userResponse = await fetch(`http://localhost:8080/api/users/${user.uid}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
      });

      if (!userResponse.ok) {
        throw new Error("Не вдалося ідентифікувати користувача");
      }

      const userData = await userResponse.json();

      // 2. Відправляємо коментар зі статусом complaint: true
      const commentPayload = {
        userId: userData.userId,
        campaignId: parseInt(projectId),
        content: reportText.trim(),
        complaint: true, // ВАЖЛИВО: це позначає коментар як скаргу
        createdAt: new Date().toISOString(),
      };

      const response = await fetch("http://localhost:8080/api/campaign-comments", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
        body: JSON.stringify(commentPayload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Помилка: ${errorText}`);
      }

      // Створюємо об'єкт для миттєвого відображення
      const newReportComment = {
        id: Date.now(), // Тимчасовий ID
        text: reportText.trim(),
        author: (userData.name + " " + (userData.surname || "")) || "Анонім",
        complaint: true,
        date: new Date().toLocaleString(),
      };

      // Сповіщаємо батьківський компонент про нову скаргу
      if (onReportSubmitted) {
        onReportSubmitted(newReportComment);
      }

      alert("Скаргу успішно додано до коментарів!");
      onClose();
      
    } catch (error) {
      console.error("Помилка при подачі скарги:", error.message);
      alert(`Помилка: ${error.message}`);
    } finally {
      setSubmitting(false);
    }
  };

  return (
    <div className="report-form-overlay" onClick={onClose}>
      <div className="report-form-modal" onClick={(e) => e.stopPropagation()}>
        
        {/* Червона шапка */}
        <div className="report-form-header">
          <h3>📝 Скарга на проєкт</h3>
          <button onClick={onClose} className="close-btn">&times;</button>
        </div>
        
        <div className="report-form-body">
          <div className="report-project-info">
            <strong>📌 Проєкт:</strong> {projectTitle}
          </div>

          <form onSubmit={handleSubmitReport} className="report-form">
            <div className="form-group">
              <label>Причина скарги *</label>
              <textarea
                value={reportText}
                onChange={(e) => setReportText(e.target.value)}
                placeholder="Опишіть детальніше причину скарги, надайте докази або пояснення..."
                required
                rows={6}
                className="report-textarea"
                maxLength={1000}
              />
              <div className="char-counter">
                {reportText.length}/1000 символів
              </div>
            </div>

            <div className="report-form-buttons">
              <button 
                type="submit" 
                disabled={submitting}
                className="btn btn-complaint"
              >
                {submitting ? "Надсилання..." : "Надіслати скаргу"}
              </button>
              <button 
                type="button" 
                onClick={onClose}
                className="btn btn-second-grey"
              >
                Скасувати
              </button>
            </div>

            <div className="report-form-note">
              <small>
                ⓘ Ваша скарга буде опублікована як коментар з відповідною позначкою.
              </small>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
};

export default ReportForm;