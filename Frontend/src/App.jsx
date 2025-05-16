import React from 'react';
import { Routes, Route } from 'react-router-dom';
import MainPage from "./components/main-page";
import Register from './components/Register';
import Login from './components/Login';
import MyProfile from './components/myFrofile';
import CreateProjectPage from './components/CreateProjectPage';
import ProjectsPage from './components/ProjectsPage';

const App = () => {
  return (
    <Routes>
      <Route path="/" element={<MainPage />} />
      <Route path="/register" element={<Register />} />
      <Route path="/login" element={<Login />} />
      <Route path="/account" element={<MyProfile />}></Route>
      <Route path="/projects" element={<ProjectsPage />} />
      <Route path="/create-project" element={<CreateProjectPage />} />
      <Route path="/projects/project:id" element=""></Route>
    </Routes>
  );
};

export default App;