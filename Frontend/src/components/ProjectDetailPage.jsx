import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import auth from "../auth";
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";
import ReportForm from "./ReportForm";

const ProjectDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  
  const [showReportForm, setShowReportForm] = useState(false);

  const baseImage = "https://cdn.abo.media/upload/article/res/770-430/f9bd7oale81czy5znmjf.jpg";

  useEffect(() => {
    const fetchProject = async () => {
      try {
        const username = "admin";
        const password = "admin";
        const base64Credentials = btoa(`${username}:${password}`);

        const response = await fetch(`http://localhost:8080/api/campaigns/${id}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Basic ${base64Credentials}`,
          },
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
        }

        const data = await response.json();

        const normalizedProject = {
          ...data,
          currentAmount: data.currentAmount ?? 0,
          goalAmount: data.goalAmount ?? 0,
          image: data.mainImgUrl || "https://st2.depositphotos.com/40945364/42167/v/450/depositphotos_421677674-stock-illustration-hands-holding-carton-box-banner.jpg",
          bankaUrl: data.bankaUrl ?? "Користувач не залишив банки :(",
          category: data.category || "Відбудова",
        };

        setProject(normalizedProject);
        setLoading(false);
      } catch (error) {
        setError(error.message);
        setLoading(false);
      }
    };

    fetchProject();
  }, [id]);

  useEffect(() => {
    const fetchComments = async () => {
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      try {
        const response = await fetch(`http://localhost:8080/api/campaign-comments/campaign/${id}`, {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Basic ${base64Credentials}`,
          },
        });

        if (response.ok) {
          const data = await response.json();
          const userIds = [...new Set(data.map((c) => c.userId))];
          let usersMap = {};

          if (userIds.length > 0) {
            const usersResponse = await fetch(`http://localhost:8080/api/users/comments/${userIds}`, {
              method: "GET", 
              headers: {
                "Content-Type": "application/json",
                Authorization: `Basic ${base64Credentials}`,
              },
            });

            if (usersResponse.ok) {
              const usersList = await usersResponse.json();
              usersList.forEach(user => {
                usersMap[user.userId] = user.name + " " + (user.surname || ""); 
              });
            }
          }

          const formatted = data.map((c) => ({
            id: c.commentId,
            text: c.content,
            author: usersMap[c.userId] || "Анонім",
            complaint: c.complaint, 
            date: c.createdAt ? new Date(c.createdAt).toLocaleString() : "01.01.2000",
          }));

          // Сортуємо так, щоб нові були зверху (опціонально)
          setComments(formatted.reverse());
        }
      } catch (err) {
        console.error("Помилка при завантаженні коментарів:", err.message);
      }
    };

    fetchComments();
  }, [id]);

  const handleAddComment = async () => {
    if (newComment.trim() === "") return;

    try {
      if (!auth.currentUser) {
        alert("Будь ласка, увійдіть, щоб залишити коментар.");
        return;
      }
      const userUid = auth.currentUser;
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      const userResponse = await fetch(`http://localhost:8080/api/users/${userUid.uid}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
      });

      if (!userResponse.ok) {
        throw new Error(`Не вдалося отримати користувача`);
      }

      const userData = await userResponse.json();
      const userId = userData.userId;

      const commentPayload = {
        userId: userId,
        campaignId: parseInt(id),
        content: newComment,
        complaint: false,
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
        throw new Error("Не вдалося надіслати коментар");
      }

      const addedComment = {
        id: Date.now(),
        text: newComment,
        author: (userData.name + " " + (userData.surname || "")) || "Анонім",
        complaint: false,
        date: new Date().toLocaleString(),
      };

      setComments([addedComment, ...comments]);
      setNewComment("");

    } catch (err) {
      console.error("Помилка при додаванні коментаря:", err.message);
      alert("Не вдалося надіслати коментар.");
    }
  };

  // Функція для додавання скарги у список коментарів без перезавантаження
  const handleReportSubmitted = (newReportComment) => {
    setComments([newReportComment, ...comments]);
  };

  if (loading) return <div className="loading">Завантаження...</div>;
  if (error) return <div className="error">Помилка: {error}</div>;
  if (!project) return <div className="not-found">Проєкт не знайдено</div>;

  const progressPercent = ((project.currentAmount || 0) / (project.goalAmount || 1)) * 100;

  return (
    <>
      <Header />
      <div className="project-page project-detail-page">
        <div className="container">
          <div className="project-image-container">
            <img src={project.image || baseImage} alt={project.title} className="project-image styled-img" />
          </div>

          <div className="project-info">
            <h1 className="project-detail-title styled-title">{project.title}</h1>
            <span className="project-category styled-category">{project.category}</span>

            <div className="project-progress styled-progress">
              <div className="progress-bar">
                <div
                  className="progress-fill"
                  style={{ width: `${progressPercent}%` }}
                ></div>
              </div>
              <div className="progress-numbers">
                <span>Зібрано: {(project.goalAmount || 0).toLocaleString()} ₴</span>
                <span>Ціль: {(project.currentAmount || 0).toLocaleString()} ₴</span>
              </div>
            </div>

            <div className="project-banka">
              <h3>Банка: </h3>
              <a href={project.bankaUrl}><p>{project.bankaUrl}</p></a>
            </div>

            <div className="project-description">
              <h3>Опис проєкту</h3>
              <p>{project.description}</p>
            </div>

            <div className="project-actions project-detail-actions">
              <button
                onClick={() => navigate("/projects")}
                className="btn btn-second project-detail-btn"
              >
                Назад до проєктів
              </button>
            </div>

            <div className="project-comments project-detail-comments">
              <h3>Коментарі</h3>

              <div className="comment-form project-detail-comment-form">
                <textarea
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  placeholder="Залиште свій коментар..."
                  rows={3}
                  className="comment-textarea project-detail-textarea"
                />
                
                <div className="comment-actions">
                  <button
                    className="btn btn-first project-detail-comment-btn"
                    onClick={handleAddComment}
                  >
                    Додати коментар
                  </button>

                  <button 
                    className="btn btn-complaint project-detail-report-btn"
                    onClick={() => setShowReportForm(true)}
                  >
                   Поскаржитися
                  </button>
                </div>
              </div>

              <ul className="comment-list project-detail-comment-list">
                {comments.length === 0 && <li className="project-detail-comment-item">Поки що немає коментарів.</li>}
                {comments.map((comment) => (
                  <li 
                    key={comment.id} 
                    className={`comment-item project-detail-comment-item ${comment.complaint ? 'complaint-item' : ''}`}
                    style={{ position: 'relative' }} 
                  >
                    {comment.complaint && (
                      <span className="complaint-badge">
                        Скарга
                      </span>
                    )}
                    
                    <div className="comment-meta project-detail-comment-meta">
                      <strong>{comment.author}</strong> — <small>{comment.date}</small>
                    </div>
                    <div>{comment.text}</div>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      </div>

      {/* Підключення зовнішнього компонента ReportForm з обробником успіху */}
      {showReportForm && (
        <ReportForm 
          projectId={id} 
          projectTitle={project.title} 
          onClose={() => setShowReportForm(false)} 
          onReportSubmitted={handleReportSubmitted}
        />
      )}

      <Footer />
    </>
  );
};

export default ProjectDetail;