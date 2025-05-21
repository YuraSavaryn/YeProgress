import React from "react";
import { useParams } from "react-router-dom";
import '../index.css';
import Header from "./Header";

const ProjectDetail = () => {
    const { id } = useParams();
    
    const projects = [
        {
            id: 1,
            title: "Модернізація енергетичної мережі",
            description: "Проєкт з відновлення та модернізації енергетичної інфраструктури в Харківській області з впровадженням сучасних технологій.",
            category: "Відбудова",
            goal: 5000000,
            collected: 1250000,
            imageUrl: "https://gwaramedia.com/wp-content/uploads/2022/07/tecz-51.jpg"
        },
        {
            id: 2,
            title: "EcoFarm - розумне сільське господарство",
            description: "Інноваційна система моніторингу та управління сільськогосподарськими угіддями з використанням ІоТ та штучного інтелекту.",
            category: "Стартап",
            goal: 2500000,
            collected: 750000,
            imageUrl: "https://hub.kyivstar.ua/assets/cms/uploads/biznes_tehnologii_jpg_a81a98106e.webp"
        },
        {
            id: 3,
            title: "Сучасна клініка в Миколаєві",
            description: "Проєкт з будівництва та обладнання сучасного медичного центру для забезпечення якісної медичної допомоги.",
            category: "Відбудова",
            goal: 10000000,
            collected: 3200000,
            imageUrl: "https://vidnova.ua/wp-content/uploads/2024/03/IMG_2318-HDR-2-scaled.jpg"
        }
    ];

    const project = projects.find(p => p.id === parseInt(id));

    if (!project) {
        return <div className="not-found">Проєкт не знайдено</div>;
    }

    return (
        <>
        <Header />
        <div className="project-page">
            <div className="project-image-container">
                <img 
                    src={project.imageUrl} 
                    alt={project.title} 
                    className="project-image"
                />
            </div>
            
            <div className="project-info">
                <h1 className="project-detail-title">{project.title}</h1>
                <span className="project-category">{project.category}</span>
                
                <div className="project-progress">
                    <div className="progress-bar">
                        <div 
                            className="progress-fill" 
                            style={{ width: `${(project.collected / project.goal) * 100}%` }}
                        ></div>
                    </div>
                    <div className="progress-numbers">
                        <span>Зібрано: ${project.collected.toLocaleString()}</span>
                        <span>Ціль: ${project.goal.toLocaleString()}</span>
                    </div>
                </div>
                
                <div className="project-description">
                    <h3>Опис проєкту</h3>
                    <p>{project.description}</p>
                </div>
            </div>
        </div>
    </>
    );
};

export default ProjectDetail;