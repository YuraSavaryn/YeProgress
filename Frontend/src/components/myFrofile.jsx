import React, { useEffect, useState } from "react";
import { signOut } from "firebase/auth";
import { Link, useNavigate } from "react-router-dom";
import { FaInstagram, FaFacebook, FaTwitter, FaLinkedin } from "react-icons/fa";
import auth from "../auth";
import Header from "./Header";
import "../index.css";

const MyProfile = () => {
  const [editMode, setEditMode] = useState(false);
  const navigate = useNavigate();
  const [profile, setProfile] = useState({
    name: "Іван Петренко",
    email: "ivan.petrenko@example.com",
    phone: "+380",
    bio: "Пристрасний меценат та автор кількох успішних краудфандингових проектів",
    avatar: "https://cdn-icons-png.flaticon.com/512/8345/8345328.png",
    location: "Київ, Україна",
    Instagram: "https://www.instagram.com/your_username/",
    Facebook: "",
    Twitter: "",
    LinkedIn: "",
  });

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

  useEffect(() => {
    const unsubscribe = auth.onAuthStateChanged(async (user) => {
      if (!user) {
        console.error("No user logged in, redirecting to login");
        navigate("/login");
        return;
      }

      const fetchUserProfile = async () => {
        try {
          const userId = user.uid;
          console.log("Fetching profile for user:", userId);
          const username = "admin";
          const password = "admin";
          const base64Credentials = btoa(`${username}:${password}`);

          const response = await fetch(`http://localhost:8080/api/users/${userId}`, {
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
          setProfile((prev) => ({
            ...prev,
            name: data.name || prev.name,
            email: data.email || prev.email,
            phone: data.phone || prev.phone,
            bio: data.bio || prev.bio,
            avatar: data.avatar || prev.avatar,
            location: data.location || prev.location,
            Instagram: data.Instagram || prev.Instagram,
            Facebook: data.Facebook || prev.Facebook,
            Twitter: data.Twitter || prev.Twitter,
            LinkedIn: data.LinkedIn || prev.LinkedIn,
          }));
        } catch (error) {
          console.error("Error fetching user profile:", error.message);
        }
      };

      fetchUserProfile();
    });

    return () => unsubscribe();
  }, [navigate]);

  const handleSignOut = async () => {
    const confirmLogOut = window.confirm("Чи дійсно ви хочете вийти з аккаунту?");
    if (confirmLogOut) {
      try {
        await signOut(auth);
        navigate("/");
      } catch (error) {
        console.error("Помилка: ", error);
        alert("Помилка при виході з аккаунту");
      }
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setProfile((prev) => ({ ...prev, [name]: value }));
  };

  const handleSaveProfile = () => {
    setEditMode(false);
    // Add logic to save profile to server here
  };

  const handleAvatarChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        setProfile((prev) => ({ ...prev, avatar: reader.result }));
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <>
      <Header />
      <div className="profile-container">
        <div className="profile-header">
          <div className="profile-top">
            <div className="avatar-container">
              <img src={profile.avatar} alt="Аватар" className="avatar" />
              {editMode && (
                <input
                  type="file"
                  accept="image/*"
                  onChange={handleAvatarChange}
                />
              )}
            </div>
            <div className="name-section">
              {editMode ? (
                <input
                  type="text"
                  name="name"
                  value={profile.name}
                  onChange={handleInputChange}
                  className="profile-input name-input"
                />
              ) : (
                <h2>{profile.name}</h2>
              )}
            </div>
          </div>
        </div>

        <div className="profile-content">
          <div className="profile-info">
            <div className="info-section">
              <div className="social-links">
                <a href={profile.Instagram} target="_blank" rel="noopener noreferrer">
                  <FaInstagram className="social-icon" />
                </a>
                <a href={profile.Facebook || "https://facebook.com/your_username"} target="_blank" rel="noopener noreferrer">
                  <FaFacebook className="social-icon" />
                </a>
                <a href={profile.Twitter || "https://twitter.com/your_username"} target="_blank" rel="noopener noreferrer">
                  <FaTwitter className="social-icon" />
                </a>
                <a href={profile.LinkedIn || "https://linkedin.com/in/your_username"} target="_blank" rel="noopener noreferrer">
                  <FaLinkedin className="social-icon" />
                </a>
              </div>

              {editMode ? (
                <>
                  <div className="form-group">
                    <label>Email:</label>
                    <input
                      type="email"
                      name="email"
                      value={profile.email}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>Номер телефону:</label>
                    <input
                      type="text"
                      name="phone"
                      value={profile.phone}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>Місцезнаходження:</label>
                    <input
                      type="text"
                      name="location"
                      value={profile.location}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>Про себе:</label>
                    <textarea
                      name="bio"
                      value={profile.bio}
                      onChange={handleInputChange}
                      className="profile-textarea"
                      rows="4"
                    />
                  </div>
                  <div className="form-group">
                    <label>Instagram:</label>
                    <input
                      type="text"
                      name="Instagram"
                      value={profile.Instagram}
                      onChange={handleInputChange}
                      className="profile-input"
                      placeholder="https://www.instagram.com/"
                    />
                  </div>
                </>
              ) : (
                <>
                  <p><strong>Email:</strong> {profile.email}</p>
                  <p><strong>Телефон:</strong> {profile.phone}</p>
                  <p><strong>Місцезнаходження:</strong> {profile.location}</p>
                  <p><strong>Про себе:</strong> {profile.bio}</p>
                </>
              )}
            </div>

            <button
              className={`btn ${editMode ? "btn-save" : "btn-edit"}`}
              onClick={editMode ? handleSaveProfile : () => setEditMode(true)}
            >
              {editMode ? (
                <>
                  <i className="fas fa-save"></i> Зберегти зміни
                </>
              ) : (
                <>
                  <i className="fas fa-edit"></i> Редагувати профіль
                </>
              )}
            </button>

            <div className="my-projects">
              <h2 className="header-my-projects">Мої проєкти</h2>
              <div className="project-cards-profile">
                {projects.map((project) => (
                  <div key={project.id} className="project-card">
                    <div className="project-image">
                      <img src={project.image} alt={project.title} />
                    </div>
                    <div className="project-content">
                      <span className="project-category">{project.category}</span>
                      <span className="project-active">{project.active}</span>
                      <h3 className="project-title">{project.title}</h3>
                      <p className="project-excerpt">{project.description}</p>
                      <div className="project-progress">
                        <div className="progress-bar">
                          <div
                            className="progress-fill"
                            style={{ width: `${(project.collected / project.goal) * 100}%` }}
                          ></div>
                        </div>
                        <div className="progress-info">
                          <span>Зібрано: {project.collected.toLocaleString()} ₴</span>
                          <span>Ціль: {project.goal.toLocaleString()} ₴</span>
                        </div>
                      </div>
                      <Link to={`/project/edit/${project.id}`} className="btn btn-primary">
                        Редагувати
                      </Link>
                    </div>
                  </div>
                ))}
              </div>
              <Link to="/create-project" className="btn btn-first create-btn">
                Створити новий проєкт
              </Link>
            </div>

            <div className="action-buttons">
              <button className="btn btn-first btn-logout" onClick={handleSignOut}>
                <i className="fas fa-sign-out-alt"></i> Вийти
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default MyProfile;