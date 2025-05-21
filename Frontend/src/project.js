import { useState, useEffect } from "react";

const projects = () => {
    const [projects, setProjects] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchProjects = async () => {
        const response = await fetch("api/projects", 
            {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Basic ${base64Credentials}`
                }
            }
        );
        const data = response.data;
    }
}