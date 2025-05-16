import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";

const ProjectDetail = () => {
    const {projectId} = useParams();
    const [project, setProject] = useState(null);

}

export default ProjectDetail