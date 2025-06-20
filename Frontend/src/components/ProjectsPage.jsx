import React, { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import "../index.css";
import auth from "../auth";
import Header from "./Header";
import Footer from "./Footer";

const ProjectsPage = () => {
  const [projects, setProjects] = useState([]);

  const [categories, setCategories] = useState(["Всі", "Відбудова", "Стартап", "Інновації", "Освіта"]);
  const [selectedCategory, setSelectedCategory] = useState("Всі");
  const [showCreateForm, setShowCreateForm] = useState(false);
  const [newProject, setNewProject] = useState({
    title: "",
    description: "",
    goal: "",
    category: "Відбудова",
    monoLink: "",
    image: null
  });

  const filteredProjects = selectedCategory === "Всі" 
    ? projects 
    : projects.filter(project => project.category === selectedCategory);

  useEffect(() => {
  const fetchProjects = async () => {
    try {
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      const response = await fetch(`http://localhost:8080/api/campaigns`, {
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

      const campaigns = await response.json();

      const sampleImages = [
        "https://vechirniy.kyiv.ua/uploads/2022/12/16/photo_20221216_122956.jpg",
        "https://f.discover.ua/location/2071/ruE82.jpg",
        "https://funtime.kiev.ua/u/i/gallery/2021/05/park-imeni-shevchenko-6-609c2c85bd7cd.jpg",
        "https://images.prom.ua/2272683959_rgb-korol-16yadergtx108032gb.jpg",
        "https://www.finradnyk.site/wp-content/uploads/2023/08/startap.jpg"
      ];

      const normalizedData = campaigns.map((project, index) => ({
        id: project.campaignId ?? index + 1,
        title: project.title,
        description: project.description,
        goal: project.goalAmount ?? 0,
        collected: project.currentAmount ?? 0,
        monoLink: project.bankaUrl ?? "",
        image:
          project.mainImgUrl && project.mainImgUrl !== "https://placehold.co/600x400?text=No+Image"
            ? project.mainImgUrl
            : sampleImages[index % sampleImages.length],
        category: project.category ?? "Відбудова", // якщо є поле category
      }));

      setProjects(normalizedData);

    } catch (error) {
      console.error("Error fetching projects:", error.message);
    }
  };

  fetchProjects();
}, []);

  const [isAuth, setIsAuth] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
      const unsubscribe = auth.onAuthStateChanged((user) => {
      setIsAuth(!!user);
    });
      return () => unsubscribe();
    }, []);

    const handleCreateClick = () => {
      if (isAuth) {
        navigate("/create-project");
      } else {
        navigate("/login");
      }
    };

  const handleCreateProject = () => {
    const newProjectObj = {
      id: projects.length + 1,  
      title: newProject.title,
      description: newProject.description,
      category: newProject.category,
      goal: Number(newProject.goal) || 0,
      monoLink: newProject.monoLink,
      collected: 0,
      image: newProject.image || "https://placehold.co/600x400?text=No+Image"
    };
    
    setProjects([...projects, newProjectObj]);
    setNewProject({
      title: "",
      description: "",
      goal: "",
      category: "Відбудова",
      monoLink: "",
      image: null
    });
    setShowCreateForm(false);
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

  const truncateDescription = (description) => {
    if (description.length > 120) {
      return description.substring(0, 117) + "...";
    }
    return description;
  };

  return (
    <>
      <Header />
      <div className="projects-page">
        <div className="container">
          <div className="projects-header">
            <h2 className="section-title">Всі проєкти</h2>
            
            <div className="projects-controls">
              <div className="category-filter">
                <select 
                  value={selectedCategory} 
                  onChange={(e) => setSelectedCategory(e.target.value)}
                  className="filter-select"
                >
                  {categories.map(category => (
                    <option key={category} value={category}>{category}</option>
                  ))}
                </select>
              </div>
              
              <button 
                onClick={handleCreateClick} 
                className="btn btn-first create-btn"
              >
                Створити проєкт
              </button>
            </div>
          </div>

          {showCreateForm && (
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
                    value={newProject.category}
                    onChange={(e) => setNewProject({...newProject, category: e.target.value})}
                  >
                    {categories.filter(cat => cat !== "Всі").map(category => (
                      <option key={category} value={category}>{category}</option>
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
                  onClick={() => setShowCreateForm(false)}
                  className="btn btn-second"
                >
                  Скасувати
                </button>
              </div>
            </div>
          )}

          <div className="project-cards">
            {filteredProjects.map(project => (
              <div key={project.id} className="project-card">
                <div className="project-image">
                  <img src={project.image} alt={project.title}/>
                </div>
                <div className="project-content">
                  <div className="project-category">
                    <span>{project.category}</span>
                    </div> <br />
                  <Link to={`/project/${project.id}`} className="project-title-link">{project.title}</Link>
                  <p className="project-excerpt">{truncateDescription(project.description)}</p>
                  
                  <div className="project-progress">
                    <div className="progress-bar">
                      <div 
                        className="progress-fill" 
                        style={{width: `${(project.collected / project.goal) * 100}%`}}
                      ></div>
                    </div>
                      <div className="progress-info">
                        <span className="block">Зібрано: {project.collected.toLocaleString()} ₴</span>
                        <span className="block">Ціль: {project.goal.toLocaleString()} ₴</span>
                      </div>

                  </div>
                  
                  <Link to={`/project/${project.id}`} className="btn btn-primary">
                    Детальніше
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
      <Footer />
    </>
  );
};

export default ProjectsPage;