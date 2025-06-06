import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import auth from "../auth";
import { getStorage, ref, uploadBytes, getDownloadURL } from "firebase/storage";
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";

const CreateProjectPage = () => {
  const navigate = useNavigate();
  const [newProject, setNewProject] = useState({
    title: "",
    description: "",
    goalAmount: "",      
    currentAmount: 0,    
    category: "Відбудова",
    bankaUrl: "",        
    image: null,
    imageUrl: "",
    status: "active",    
    approxDeadline: "",  
  });

  const [imagePreview, setImagePreview] = useState(null);
  const categories = ["Відбудова", "Стартап", "Інновації", "Освіта"];

  const handleCreateProject = async () => {
    try {
      // Валідація
      if (!newProject.title || !newProject.description) {
        throw new Error("Заповніть усі обов’язкові поля!");
      }

      const user = auth.currentUser;
      if (!user) throw new Error("Користувач не авторизований");

      let imageUrl = "https://placehold.co/600x400?text=No+Image";

      if (newProject.image) {
        const storage = getStorage();
        const storageRef = ref(storage, `project_images/${user.uid}/${newProject.image.name}`);
        await uploadBytes(storageRef, newProject.image);
        imageUrl = await getDownloadURL(storageRef);
      } else if (newProject.imageUrl) {
        imageUrl = newProject.imageUrl;
      }

      const token = await user.getIdToken();

      const body = {
        title: newProject.title,
        description: newProject.description,
        goalAmount: Number(newProject.goalAmount),
        currentAmount: 0, 
        category: newProject.category,
        bankaUrl: newProject.bankaUrl,
        mainImgUrl: newProject.imageUrl,
        status: newProject.status ?? "Активний",
        approxDeadline: newProject.approxDeadline ? new Date(newProject.approxDeadline).toISOString() : null,
        createdDate: new Date().toISOString(),
        firebaseId: user.uid,
      };

      const response = await fetch(`http://localhost:8080/api/campaigns/${user.uid}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
      }

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
      setNewProject({ ...newProject, image: file });
      const reader = new FileReader();
      reader.onloadend = () => {
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
                value={newProject.bankaUrl}
                onChange={(e) => setNewProject({ ...newProject, bankaUrl: e.target.value })}
                placeholder="https://send.monobank.ua/..."
              />
            </div>

            <div className="form-group">
              <label>Приблизний дедлайн</label>
              <input
                type="date"
                value={newProject.approxDeadline}
                onChange={(e) => setNewProject({ ...newProject, approxDeadline: e.target.value })}
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

            <div className="form-group">
              <label>Або вставте посилання на зображення</label>
              <input
                type="url"
                value={newProject.imageUrl}
                onChange={(e) => setNewProject({ ...newProject, imageUrl: e.target.value })}
                placeholder="https://example.com/image.jpg"
              />
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
