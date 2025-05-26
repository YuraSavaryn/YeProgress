import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import auth from "../auth"; // Імпортуємо Firebase auth
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";

const ProjectDetail = () => {
  const { id } = useParams(); // ID із URL
  const navigate = useNavigate();
  const [project, setProject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Отримання даних проєкту з бекенду
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
          collected: data.collected ?? 0, // Нормалізація
          goal: data.goal ?? 0,
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

  if (loading) return <div className="loading">Завантаження...</div>;
  if (error) return <div className="error">Помилка: {error}</div>;
  if (!project) return <div className="not-found">Проєкт не знайдено</div>;

  return (
    <>
      <Header />
      <div className="project-page">
        <div className="container">
          <div className="project-image-container">
            <img
              src={project.image} // Base64 або URL
              alt={project.title}
              className="project-image"
            />
          </div>

          <div className="project-info">
            <h1 className="project-detail-title">{project.title}</h1>
            <span className="project-category">{project.category}</span>

            <div className="project-progress">
              <div className="progress-bar">
                <div
                  className="progress-fill"
                  style={{ width: `${((project.collected || 0) / (project.goal || 1)) * 100}%` }}
                ></div>
              </div>
              <div className="progress-numbers">
                <span>Зібрано: {(project.collected || 0).toLocaleString()} ₴</span>
                <span>Ціль: {(project.goal || 0).toLocaleString()} ₴</span>
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
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default ProjectDetail;