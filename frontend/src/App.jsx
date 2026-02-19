import React, { useState } from 'react';
import Header from './components/Header/Header';
import Footer from './components/Footer/Footer';
import AlunosPage from './pages/AlunosPage';
import TurmasPage from './pages/TurmasPage';
import ProfessoresPage from './pages/ProfessoresPage';
import LoginPage from './pages/LoginPage';
import { useAuth } from './context/AuthContext';
import './styles/GlobalStyle.css';

function App() {
  const { user } = useAuth();
  const [activeTab, setActiveTab] = useState('alunos');

  if (!user) {
    return <LoginPage />;
  }

  const renderPage = () => {
    switch(activeTab) {
      case 'alunos':
        return <AlunosPage />;
      case 'turmas':
        return <TurmasPage />;
      case 'professores':
        return <ProfessoresPage />;
      default:
        return <TurmasPage />;
    }
  };

  return (
    <div className="app-container">
      <Header activeTab={activeTab} setActiveTab={setActiveTab} />
      <main className="main-content">
        {renderPage()}
      </main>
      <Footer />
    </div>
  );
}

export default App;
