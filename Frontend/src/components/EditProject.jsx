import React, { useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import auth from "../auth";
import "../index.css";
import Header from "./Header";
import Footer from "./Footer";

function EditProjectPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    category: "Відбудова",
    goal: "",
    collected: "",
    monoLink: "",
    image: "",
    active: "Активний",
  });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

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

        const project = await response.json();
        setFormData({
          title: project.title || "",
          description: project.description || "",
          category: project.category || "Відбудова",
          goal: project.goal || "",
          collected: project.collected || 0,
          monoLink: project.monoLink || "",
          image: project.image || "",
          active: project.active || "Активний",
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

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleImageUpload = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setFormData({ ...formData, image: reader.result });
      };
      reader.readAsDataURL(file);
    }
  };

  const handleUpdateProject = async () => {
    try {
      if (!formData.title || !formData.description || !formData.goal) {
        throw new Error("Заповніть усі обов’язкові поля (назва, опис, ціль)");
      }

      const user = auth.currentUser;
      if (!user) throw new Error("Користувач не авторизований");

      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      const response = await fetch(`http://localhost:8080/api/campaigns/${id}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
        body: JSON.stringify({
          title: formData.title,
          description: formData.description,
          goal: Number(formData.goal) || 0,
          category: formData.category,
          bankaUrl: formData.bankaUrl,
          image: formData.image || "https://placehold.co/600x400?text=No+Image",
          firebaseId: user.uid,
          collected: Number(formData.collected) || 0,
          createdAt: new Date().toISOString(),
          active: formData.active,
        }),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
      }

      await response.json();
      alert("Проєкт успішно оновлено!");
      navigate("/projects");
    } catch (error) {
      console.error("Error updating project:", error.message);
      alert(`Помилка: ${error.message}`);
    }
  };

  const handleCancel = () => {
    navigate("/projects");
    alert("Зміни скасовано.");
  };

  if (loading) return <div>Завантаження...</div>;
  if (error) return <div>Помилка: {error}</div>;

  return (
    <>
      <Header />
      <div className="projects-page">
        <div className="container">
          <div className="create-project-form">
            <h3>Редагування проєкту</h3>
            <div className="form-group">
              <label htmlFor="title">Назва проєкту</label>
              <input
                type="text"
                id="title"
                name="title"
                value={formData.title}
                onChange={handleChange}
                placeholder="Введіть назву проєкту"
              />
            </div>
            <div className="form-group">
              <label htmlFor="description">Опис</label>
              <textarea
                id="description"
                name="description"
                value={formData.description}
                onChange={handleChange}
                placeholder="Введіть опис проєкту"
              />
            </div>
            <div className="form-row">
              <div className="form-group">
                <label htmlFor="category">Категорія</label>
                <select id="category" name="category" value={formData.category} onChange={handleChange}>
                  <option value="Відбудова">Відбудова</option>
                  <option value="Стартап">Стартап</option>
                  <option value="Інновації">Інновації</option>
                  <option value="Освіта">Освіта</option>
                </select>
              </div>
              <div className="form-group">
                <label htmlFor="active">Статус</label>
                <select id="active" name="active" value={formData.active} onChange={handleChange}>
                  <option value="Активний">Активний</option>
                  <option value="Неактивний">Неактивний</option>
                </select>
              </div>
            </div>
            <div className="form-group">
              <label htmlFor="monoLink">Посилання на mono-банку</label>
              <input
                type="url"
                id="monoLink"
                name="monoLink"
                value={formData.bankaUrl}
                onChange={handleChange}
                placeholder="https://send.monobank.ua/..."
              />
            </div>
            <div className="form-group">
              <label htmlFor="image">Зображення проєкту</label>
              <input type="file" id="image" accept="image/*" onChange={handleImageUpload} />
              {formData.image && (
                <img src={formData.image} alt="Preview" style={{ maxWidth: "200px", marginTop: "10px" }} />
              )}
            </div>
            <div className="form-buttons">
              <button className="btn btn-first" onClick={handleUpdateProject}>
                Зберегти
              </button>
              <button className="btn btn-second" onClick={handleCancel}>
                Скасувати
              </button>
            </div>
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default EditProjectPage;