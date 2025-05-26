import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import auth from "../auth";
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";

const CreateProjectPage = () => {
  const navigate = useNavigate();
  const [newProject, setNewProject] = useState({
    title: "",
    description: "",
    goal: "",
    category: "Відбудова",
    monoLink: "",
    image: null,
  });
  const [imagePreview, setImagePreview] = useState(null); // Для попереднього перегляду
  const categories = ["Відбудова", "Стартап", "Інновації", "Освіта"];

  const handleCreateProject = async () => {
    try {
      // Валідація
      if (!newProject.title || !newProject.description || !newProject.goal) {
        throw new Error("Заповніть усі обов’язкові поля!");
      }

      const user = auth.currentUser;
      if (!user) throw new Error("Користувач не авторизований");

      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      const response = await fetch(`http://localhost:8080/api/campaigns`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
        body: JSON.stringify({
          title: newProject.title,
          description: newProject.description,
          goal: Number(newProject.goal) || 0,
          category: newProject.category,
          monoLink: newProject.monoLink,
          image: newProject.image || "https://placehold.co/600x400?text=No+Image",
          firebaseId: user.uid,
          createdAt: new Date().toISOString(),
          collected: 0,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
      }

      await response.json();
      alert("Проєкт успішно створено!");
      navigate("/projects");
    } catch (error) {
      console.error("Error creating project:", error.message);
      alert(`Помилка: ${error.message}`);
    }
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      if (file.size > 2 * 1024 * 1024) {
        alert("Файл занадто великий! Максимальний розмір: 2 МБ");
        return;
      }
      const reader = new FileReader();
      reader.onloadend = () => {
        setNewProject({ ...newProject, image: reader.result });
        setImagePreview(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <>
      <Header />
      <div className="projects-page">
        <div className="container">
          <div className="create-project-form">
            <h3>Створити новий проєкт</h3>
            <div className="form-group">
              <label>Назва проєкту</label>
              <input
                type="text"
                value={newProject.title}
                onChange={(e) => setNewProject({ ...newProject, title: e.target.value })}
                placeholder="Введіть назву проєкту"
              />
            </div>
            <div className="form-group">
              <label>Опис проєкту</label>
              <textarea
                value={newProject.description}
                onChange={(e) => setNewProject({ ...newProject, description: e.target.value })}
                placeholder="Опишіть ваш проєкт"
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Грошова ціль збору (₴)</label>
                <input
                  type="number"
                  value={newProject.goal}
                  onChange={(e) => setNewProject({ ...newProject, goal: e.target.value })}
                  placeholder="Введіть суму"
                />
              </div>
              <div className="form-group">
                <label>Категорія</label>
                <select
                  value={newProject.category}
                  onChange={(e) => setNewProject({ ...newProject, category: e.target.value })}
                >
                  {categories.map((category) => (
                    <option key={category} value={category}>
                      {category}
                    </option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-group">
              <label>Посилання на mono-банку</label>
              <input
                type="url"
                value={newProject.monoLink}
                onChange={(e) => setNewProject({ ...newProject, monoLink: e.target.value })}
                placeholder="https://send.monobank.ua/..."
              />
            </div>
            <div className="form-group">
              <label>Зображення проєкту</label>
              <input type="file" accept="image/*" onChange={handleImageUpload} />
              {imagePreview && (
                <img
                  src={imagePreview}
                  alt="Preview"
                  style={{ maxWidth: "200px", marginTop: "10px" }}
                />
              )}
            </div>
            <div className="form-buttons">
              <button onClick={handleCreateProject} className="btn btn-first">
                Опублікувати
              </button>
              <button onClick={() => navigate("/projects")} className="btn btn-second">
                Скасувати
              </button>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default CreateProjectPage;