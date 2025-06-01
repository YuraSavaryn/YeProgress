import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import auth from "../auth";
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";

const ProjectDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");

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
        setProject({
          ...data,
          currentAmount: data.currentAmount ?? 0,
          goalAmount: data.goalAmount ?? 0,
          image: data.image || "https://placehold.co/600x400?text=No+Image",
        });

        setLoading(false);
      } catch (error) {
        console.error("Error fetching project:", error.message);
        setError(error.message);
        setLoading(false);
      }
    };

    fetchProject();
  }, [id]);

  const handleAddComment = () => {
    if (newComment.trim() === "") return;

    const comment = {
      id: Date.now(),
      text: newComment,
      author: "Анонім",
      date: new Date().toLocaleString(),
    };

    setComments([comment, ...comments]);
    setNewComment("");
  };

  if (loading) return <div className="loading">Завантаження...</div>;
  if (error) return <div className="error">Помилка: {error}</div>;
  if (!project) return <div className="not-found">Проєкт не знайдено</div>;

  const progressPercent = ((project.currentAmount || 0) / (project.goalAmount || 1)) * 100;

  return (
    <>
      <Header />
      <div className="project-page">
        <div className="container">
          <div className="project-image-container">
            <img src={project.image} alt={project.title} className="project-image styled-img" />
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
                <span>Зібрано: {(project.currentAmount || 0).toLocaleString()} ₴</span>
                <span>Ціль: {(project.goalAmount || 0).toLocaleString()} ₴</span>
              </div>
            </div>

            <div className="project-description">
              <h3>Опис проєкту</h3>
              <p>{project.description}</p>
            </div>

            <div className="project-actions">
              <button
                onClick={() => navigate(`/project/edit/${id}`)}
                className="btn btn-primary"
              >
                Редагувати
              </button>
              <button onClick={() => navigate("/projects")} className="btn btn-secondary">
                Назад до проєктів
              </button>
            </div>

            <div className="project-comments styled-comments">
              <h3>Коментарі</h3>

              <div className="comment-form">
                <textarea
                  value={newComment}
                  onChange={(e) => setNewComment(e.target.value)}
                  placeholder="Залиште свій коментар..."
                  rows={3}
                  className="comment-textarea"
                />
                <button className="btn btn-primary mt-2" onClick={handleAddComment}>
                  Додати коментар
                </button>
              </div>

              <ul className="comment-list mt-4">
                {comments.length === 0 && <li>Поки що немає коментарів.</li>}
                {comments.map((comment) => (
                  <li key={comment.id} className="comment-item mb-2">
                    <div className="comment-meta">
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
      <Footer />
    </>
  );
};

export default ProjectDetail;