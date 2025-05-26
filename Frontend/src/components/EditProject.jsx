import React from "react";
import { useParams } from "react-router-dom";
import { useState } from "react";
import '../index.css';
import Header from "./Header";

function EditProjectPage() {
      const [projects, setProjects] = useState([
        {
          id: 1,
          title: "Модернізація енергетичної мережі",
          description:
            "Проєкт з відновлення та модернізації енергетичної інфраструктури в Харківській області з впровадженням сучасних технологій.",
          category: "Відбудова",
          goal: 5000000,
          collected: 1250000,
          image: "https://gwaramedia.com/wp-content/uploads/2022/07/tecz-51.jpg",
          active: "Активний",
        },
        {
          id: 2,
          title: "EcoFarm - розумне сільське господарство",
          description:
            "Інноваційна система моніторингу та управління сільськогосподарськими угіддями з використанням ІоТ та штучного інтелекту.",
          category: "Стартап",
          goal: 2500000,
          collected: 750000,
          image: "https://hub.kyivstar.ua/assets/cms/uploads/biznes_tehnologii_jpg_a81a98106e.webp",
          active: "Активний",
        },
        {
          id: 3,
          title: "Сучасна клініка в Миколаєві",
          description:
            "Проєкт з будівництва та обладнання сучасного медичного центру для забезпечення якісної медичної допомоги.",
          category: "Відбудова",
          goal: 10000000,
          collected: 3200000,
          image: "https://vidnova.ua/wp-content/uploads/2024/03/IMG_2318-HDR-2-scaled.jpg",
          active: "Активний",
        },
      ]);

      const Id = useParams();
      const project = projects.find((p) => p.id === Id);

      const [formData, setFormData] = useState({
        title: project.title,
        description: project.description,
        category: project.category,
        goal: project.goal,
        collected: project.collected,
        image: project.image,
        active: project.active,
      });

      const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
      };

      const handleSave = () => {
        const updatedProjects = projects.map((p) =>
          p.id === projectId ? { ...p, ...formData } : p
        );
        setProjects(updatedProjects);
        console.log('Оновлені дані проєкту:', formData);
        alert('Зміни збережено! Перевірте консоль для деталей.');
      };

      const handleCancel = () => {
        setFormData({
          title: project.title,
          description: project.description,
          category: project.category,
          goal: project.goal,
          collected: project.collected,
          image: project.image,
          active: project.active,
        });
        alert('Зміни скасовано.');
      };

      return (
        <>
        <Header />
        <div className="create-project-form">
          <h3>Редагування оголошення</h3>
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
            ></textarea>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="category">Категорія</label>
              <select
                id="category"
                name="category"
                value={formData.category}
                onChange={handleChange}
              >
                <option value="Відбудова">Відбудова</option>
                <option value="Стартап">Стартап</option>
                <option value="Інновації">Інновації</option>
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="active">Статус</label>
              <select
                id="active"
                name="active"
                value={formData.active}
                onChange={handleChange}
              >
                <option value="Активний">Активний</option>
                <option value="Неактивний">Неактивний</option>
              </select>
            </div>
          </div>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="goal">Ціль (грн)</label>
              <input
                type="number"
                id="goal"
                name="goal"
                value={formData.goal}
                onChange={handleChange}
                placeholder="Введіть цільову суму"
              />
            </div>
            <div className="form-group">
              <label htmlFor="collected">Зібрано (грн)</label>
              <input
                type="number"
                id="collected"
                name="collected"
                value={formData.collected}
                onChange={handleChange}
                placeholder="Введіть зібрану суму"
              />
            </div>
          </div>
          <div className="form-group">
            <label htmlFor="image">URL зображення</label>
            <input
              type="text"
              id="image"
              name="image"
              value={formData.image}
              onChange={handleChange}
              placeholder="Введіть URL зображення"
            />
          </div>
          <div className="form-buttons">
            <button className="btn-first" onClick={handleSave}>
              Зберегти
            </button>
            <button className="btn-second" onClick={handleCancel}>
              Скасувати
            </button>
          </div>
        </div>
        </>
      );
    }

export default EditProjectPage;