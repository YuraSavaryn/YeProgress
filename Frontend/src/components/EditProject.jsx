import React from "react";
import { useParams } from "react-router-dom";
import '../index.css';
import Header from "./Header";

const EditPage = () => {
    const { id } = useParams();

    return (
        <div>
            <Header />

        </div>
    )
} 

export default EditPage;