import { createRoot } from 'react-dom/client';
import { StrictMode } from 'react';
import App from './App.jsx';

const root = document.getElementById('root');
createRoot(root).render(
    <StrictMode>
        <App />
    </StrictMode>,
)
