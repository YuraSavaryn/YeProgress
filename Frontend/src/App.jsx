import React from 'react';
import { Routes, Route } from 'react-router-dom';
import MainPage from "./components/main-page";
import Register from './components/Register';
import Login from './components/Login';
import MyProfile from './components/myFrofile';
import CreateProjectPage from './components/CreateProjectPage';
import ProjectsPage from './components/ProjectsPage';
import ProjectDetail from './components/ProjectDetailPage';
import EditProjectPage from './components/EditProject';
import AboutPage from './components/AboutPage';
import ContactsPage from './components/ContactsPage';
import LegalPage from './components/LegalPage';
import FAQPage from './components/FAQPage';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/account" element={<MyProfile />}></Route>
      <Route path="/projects" element={<ProjectsPage />} />
      <Route path="/create-project" element={<CreateProjectPage />} />
      <Route path="/project/:id" element={<ProjectDetail />}></Route>
      <Route path="/project/edit/:id" element={<EditProjectPage />}></Route>
      <Route path="/about" element={<AboutPage />} />
      <Route path="/contacts" element={<ContactsPage />} />
      <Route path="/invest" element={<MainPage />} />
      <Route path="/legal" element={<LegalPage />} />
      <Route path="/faq" element={<FAQPage />} />
    </Routes>
  );
};

export default App;