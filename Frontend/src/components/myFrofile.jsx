import React, { useEffect, useState } from "react";
import { signOut } from "firebase/auth";
import { Link, useNavigate } from "react-router-dom";
import { FaInstagram, FaFacebook, FaTwitter, FaLinkedin, FaTrash } from "react-icons/fa";
import auth from "../auth";
import Header from "./Header";
import { getStorage, ref, uploadBytes, getDownloadURL } from "firebase/storage";
import "../index.css";

const MyProfile = () => {
  const [editMode, setEditMode] = useState(false);
  const navigate = useNavigate();
  const [previewAvatar, setPreviewAvatar] = useState(null);
  const [profile, setProfile] = useState({
    name: "Іван",
    surname: "",
    email: "ivan.petrenko@example.com",
    phone: "+380",
    description: "Пристрасний меценат та автор кількох успішних краудфандингових проектів",
    avatar: "https://cdn-icons-png.flaticon.com/512/8345/8345328.png",
    location: "Київ, Україна",
    isVerified: false,
    Instagram: "https://www.instagram.com/your_username/",
    Facebook: "",
    Twitter: "",
    LinkedIn: "",
  });

  const [projects, setProjects] = useState([]);

  const truncateDescription = (description) => {
    if (description.length > 120) {
      return description.substring(0, 117) + "...";
    }
    return description;
  };

  const handleDeleteProject = async (projectId) => {
    try {
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      const response = await fetch(`http://localhost:8080/api/campaigns/${projectId}`, {
        method: "DELETE",
        headers: {
          Authorization: `Basic ${base64Credentials}`
        }
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
      }

      setProjects((prevProjects) => prevProjects.filter(p => p.id !== projectId));
    } catch (error) {
      console.error("Error deleting project:", error.message);
      alert("Не вдалося видалити проєкт.");
    }
  };

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

          const sampleImages = [
            "https://vechirniy.kyiv.ua/uploads/2022/12/16/photo_20221216_122956.jpg",
            "https://f.discover.ua/location/2071/ruE82.jpg",
            "https://funtime.kiev.ua/u/i/gallery/2021/05/park-imeni-shevchenko-6-609c2c85bd7cd.jpg",
            "https://images.prom.ua/2272683959_rgb-korol-16yadergtx108032gb.jpg",
            "https://www.finradnyk.site/wp-content/uploads/2023/08/startap.jpg"
          ];

          const data = await response.json();
          setProfile((prev) => ({
            ...prev,
            name: data.name || prev.name,
            surname: data.surname || prev.surname,
            email: data.email || prev.email,
            phone: data.phone || prev.phone,
            description: data.description || prev.description,
            avatar: data.avatar || prev.avatar,
            location: data.location || prev.location,
            isVerified: data.isVerified || prev.isVerified,
            Instagram: data.Instagram || prev.Instagram,
            Facebook: data.Facebook || prev.Facebook,
            Twitter: data.Twitter || prev.Twitter,
            LinkedIn: data.LinkedIn || prev.LinkedIn,
          }));

          const projectsResponse = await fetch(`http://localhost:8080/api/campaigns/user/${userId}`, {
            method: "GET",
            headers: {
              "Content-Type": "application/json",
              Authorization: `Basic ${base64Credentials}`,
            },
          });

          if (!projectsResponse.ok) {
            const errorText = await projectsResponse.text();
            throw new Error(`HTTP error! Status: ${projectsResponse.status}, Message: ${errorText}`);
          }

          const projectData = await projectsResponse.json();
          console.log("Fetched projects:", projectData);
          const normalizedProjects = projectData.map((proj, index) => ({
            id: proj.campaignId ?? index,
            title: proj.title,
            description: proj.description,
            image: proj.mainImgUrl && proj.mainImgUrl !== "https://placehold.co/600x400?text=No+Image"
              ? proj.mainImgUrl
              : sampleImages[index % sampleImages.length],
            collected: Number(proj.currentAmount),
            goal: Number(proj.goalAmount),
            category: "Інше",
            active: proj.status === "IN_PROGRESS" ? "Активний" : "Активний",
            createdDate: proj.createdDate,
            approxDeadline: proj.approxDeadline,
          }));

          console.log("IDs проектів:", normalizedProjects.map(p => p.id));
          setProjects(normalizedProjects);

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

  const handleVerification = async () => {
    const user = auth.currentUser;
    if (!user) {
      alert("Користувач не авторизований");
      return;
    }

    const username = "admin";
    const password = "admin";
    const base64Credentials = btoa(`${username}:${password}`);
    const userId = user.uid;

    try {
      const response = await fetch(`http://localhost:8080/api/users/verified/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`HTTP error! Status: ${response.status}, Message: ${errorText}`);
      }

      setProfile((prev) => ({ ...prev, isVerified: true }));
      alert("Профіль успішно верифіковано!");
    } catch (error) {
      console.error("Помилка при верифікації:", error.message);
      alert("Не вдалося верифікувати профіль.");
    }
  };

  const handleSaveProfile = async () => {
    try {
      const user = auth.currentUser;

      if (!user) {
        alert("Користувач не авторизований");
        return;
      }

      const userId = user.uid;
      const username = "admin";
      const password = "admin";
      const base64Credentials = btoa(`${username}:${password}`);

      console.log("Sending profile data:", profile);

      const editResponse = await fetch(`http://localhost:8080/api/users/${userId}`, {
        method: "PUT", 
        headers: {
          "Content-Type": "application/json",
          Authorization: `Basic ${base64Credentials}`,
        },
        body: JSON.stringify(profile),
      });

      if (!editResponse.ok) {
        const errorText = await editResponse.text();
        throw new Error(`HTTP error! Status: ${editResponse.status}, Message: ${errorText}`);
      }

      const updatedProfile = await editResponse.json();
      console.log("Updated profile response:", updatedProfile);

      setProfile((prev) => ({
        ...prev,
        ...updatedProfile,
      }));

      setEditMode(false);
      alert("Профіль успішно оновлено!");
    } catch (error) {
      console.error("Помилка при оновленні профілю:", error.message);
      alert("Не вдалося оновити профіль. Спробуйте пізніше.");
    }
  };

  const handleAvatarChange = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    const previewUrl = URL.createObjectURL(file);
    setPreviewAvatar(previewUrl);

    try {
      const user = auth.currentUser;
      if (!user) throw new Error("Користувач не авторизований");

      const storage = getStorage();
      const storageRef = ref(storage, `avatars/${user.uid}`);
      
      await uploadBytes(storageRef, file);
      const url = await getDownloadURL(storageRef);

      setProfile((prev) => ({ ...prev, avatar: url }));
      setPreviewAvatar(null);
    } catch (error) {
      console.error("Помилка при завантаженні аватара:", error);
      alert("Не вдалося завантажити аватар");
    }
  };

  return (
    <>
      <Header />
      <div className="profile-container">
        <div className="profile-header">
          <div className="profile-top">
            <div className="avatar-container">
              <img src={previewAvatar || profile.avatar} alt="Аватар" className="avatar" />
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
                <>
                  <input
                    type="text"
                    name="name"
                    value={profile.name}
                    onChange={handleInputChange}
                    className="profile-input name-input"
                  />
                  <input
                    type="text"
                    name="surname"
                    value={profile.surname}
                    onChange={handleInputChange}
                    className="profile-input name-input"
                  />
                </>
              ) : (
                <h2>{profile.name + " " + profile.surname}</h2>
              )}
            </div>
          </div>
        </div>

        <div className="profile-content">
          <div className="profile-info">
            <div className="info-section">
              {!profile.isVerified && (
                <button className="btn btn-verify" onClick={handleVerification}>
                  Верифікувати
                </button>
              )}
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
                      name="description"
                      value={profile.description}
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
                  <div className="form-group">
                    <label>Facebook:</label>
                    <input
                      type="text"
                      name="Facebook"
                      value={profile.Facebook}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>Twitter:</label>
                    <input
                      type="text"
                      name="Twitter"
                      value={profile.Twitter}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                  <div className="form-group">
                    <label>LinkedIn:</label>
                    <input
                      type="text"
                      name="LinkedIn"
                      value={profile.LinkedIn}
                      onChange={handleInputChange}
                      className="profile-input"
                    />
                  </div>
                </>
              ) : (
                <>
                  <p><strong>Статус верифікації:</strong> {profile.isVerified ? "Верифікований" : "Не верифікований"}</p>
                  <p><strong>Email:</strong> {profile.email}</p>
                  <p><strong>Телефон:</strong> {profile.phone}</p>
                  <p><strong>Місцезнаходження:</strong> {profile.location}</p>
                  <p><strong>Про себе:</strong> {profile.description}</p>
                </>
              )}
            </div>

            <div className="profile-buttons">
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
            </div>

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
                      <p className="project-excerpt">{truncateDescription(project.description)}</p>
                      <div className="project-progress">
                        <div className="progress-bar">
                          <div
                            className="progress-fill"
                            style={{ width: `${(project.collected / project.goal) * 100}%` }}
                          ></div>
                        </div>
                        <div className="progress-info">
                          <p>Зібрано: {project.collected ? project.collected.toLocaleString() : "0"} ₴</p>
                          <p>Ціль: {project.goal ? project.goal.toLocaleString() : "0"} ₴</p>
                        </div>
                      </div>
                      <Link to={`/project/edit/${project.id}`} className="btn btn-primary">
                        Редагувати
                      </Link>
                      <button
                        className="btn btn-danger"
                        onClick={() => {
                          if (window.confirm("Ви впевнені, що хочете видалити цей проєкт?")) {
                            handleDeleteProject(project.id);
                          }
                        }}
                        style={{ marginLeft: "8px", cursor: "pointer" }}
                        title="Видалити проєкт"
                      >
                        <FaTrash />
                      </button>
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