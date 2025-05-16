import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
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
    image: null
  });

  const categories = ["Відбудова", "Стартап", "Інновації", "Освіта"];

  const handleCreateProject = () => {
    // Тут буде логіка створення проекту
    alert("Проєкт успішно створено!");
    navigate("/projects");
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setNewProject({...newProject, image: reader.result});
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
                onChange={(e) => setNewProject({...newProject, title: e.target.value})}
                placeholder="Введіть назву проєкту"
              />
            </div>
            <div className="form-group">
              <label>Опис проєкту</label>
              <textarea 
                value={newProject.description}
                onChange={(e) => setNewProject({...newProject, description: e.target.value})}
                placeholder="Опишіть ваш проєкт"
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label>Грошова ціль збору (₴)</label>
                <input 
                  type="number" 
                  value={newProject.goal}
                  onChange={(e) => setNewProject({...newProject, goal: e.target.value})}
                  placeholder="Введіть суму"
                />
              </div>
              <div className="form-group">
                <label>Категорія</label>
                <select
                  className="select-category" 
                  value={newProject.category}
                  onChange={(e) => setNewProject({...newProject, category: e.target.value})}
                >
                  {categories.map(category => (
                    <option className="option-category" key={category} value={category}>{category}</option>
                  ))}
                </select>
              </div>
            </div>
            <div className="form-group">
              <label>Посилання на mono-банку</label>
              <input 
                type="url" 
                value={newProject.monoLink}
                onChange={(e) => setNewProject({...newProject, monoLink: e.target.value})}
                placeholder="https://send.monobank.ua/..."
              />
            </div>
            <div className="form-group">
              <label>Зображення проєкту</label>
              <input 
                type="file" 
                accept="image/*"
                onChange={handleImageUpload}
              />
            </div>
            <div className="form-buttons">
              <button 
                onClick={handleCreateProject}
                className="btn btn-first"
              >
                Опублікувати
              </button>
              <button 
                onClick={navigate("/projects")}
                className="btn btn-second"
              >
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